package com.diloso.bookhair.fly.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import com.diloso.bookhair.app.controllers.CalendarController;
import com.diloso.bookhair.app.datastore.StringUtils;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.utils.Utils;
import com.diloso.bookhair.fly.services.dto.FlightOfferDTO;
import com.diloso.bookhair.fly.services.dto.LocationDTO;
import com.diloso.bookhair.fly.services.dto.LocationType;
import com.diloso.bookhair.fly.services.dto.input.DateRangeDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchFlexDTO;
import com.diloso.bookhair.fly.services.utils.MapperFly;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@Component
public class OffersSearchService implements IOffersSearchService {

	public static final String URL_SKY_BASE = "https://www.skyscanner.com/transport/flights";
	public static final String URL_SKY_PATH = "/";
	
	@Autowired
	protected AmedeusService amedeusService;
	
	@Autowired
	protected MapperFly mapper;
	
	@Override
	public List<FlightOfferDTO> search(FlightSearchDTO searchDTO) throws ResponseException {

		Amadeus amadeus = amedeusService.get();

		if (searchDTO.getMax()==null) {
			searchDTO.setMax(20);
		}
		Params params = mapper.map(searchDTO);
		

		FlightOfferSearch[] flightOfferSearch = amadeus.shopping.flightOffersSearch.get(params);
		
		List<FlightOfferDTO> result = mapper.map(flightOfferSearch);
		int minDepartureTimes;
		int minReturnTimes;
		for (FlightOfferDTO flightOfferDTO : result) {
			minDepartureTimes = Utils.getDateFly(flightOfferDTO.getDepartureDate()).getHours();
			searchDTO.setMinDepartureHour(String.valueOf(minDepartureTimes));
			searchDTO.setMaxDepartureHour(String.valueOf(minDepartureTimes+1));
			if (flightOfferDTO.getReturnDate()!=null) {
				minReturnTimes = Utils.getDateFly(flightOfferDTO.getReturnDate()).getHours();
				searchDTO.setMinReturnHour(String.valueOf(minReturnTimes));
				searchDTO.setMaxReturnHour(String.valueOf(minReturnTimes+1));
			}
			searchDTO.setIncludedAirlineCodes(StringUtils.join(",",flightOfferDTO.getCarriersCode()));
			flightOfferDTO.setUrlSkyscanner(skyscannerUrl(searchDTO));
		}
		return result;
	}
	
	@Override
	public List<FlightOfferDTO> searchFlex(FlightSearchFlexDTO searchDTO) throws ResponseException {

		return searchFlex(searchDTO, false);
	}
	
	@Override
	public List<FlightOfferDTO> searchFlexPrice(FlightSearchFlexDTO searchDTO) throws ResponseException {

	    return searchFlex(searchDTO, true);
	}
	
	protected List<FlightOfferDTO> searchFlex(FlightSearchFlexDTO searchDTO, boolean simple) throws ResponseException {

		// Only max search
		int maxSearchs = 1; //31
		
		Amadeus amadeus = amedeusService.get();

		// Only best price of day
		searchDTO.setMax(1);
		
		Params params = mapper.map(searchDTO);
	
		List<FlightOfferDTO> result = new ArrayList<FlightOfferDTO>();
		List<DateRangeDTO> listDateRanges =  getDatesSearch(searchDTO);
		int idx = 0;
		int minDepartureTimes;
		int minReturnTimes;
		String departureDate;
		String returnDate;
		for (DateRangeDTO dateRange : listDateRanges) {
			if (idx < maxSearchs) {
				params = mapper.map(params,dateRange);
				FlightOfferSearch[] flightOfferSearch = amadeus.shopping.flightOffersSearch.get(params);
				List<FlightOfferDTO> resultAux = mapper.map(flightOfferSearch, simple);
				for (FlightOfferDTO flightOfferDTO : resultAux) {
					departureDate = flightOfferDTO.getDepartureDate();
					returnDate = flightOfferDTO.getReturnDate();
					searchDTO.setDepartureDate(departureDate.substring(0,departureDate.indexOf(Utils.formatSeparatorDateFly)));
					searchDTO.setReturnDate(returnDate.substring(0,returnDate.indexOf(Utils.formatSeparatorDateFly)));
					minDepartureTimes = Utils.getDateFly(departureDate).getHours();
					minReturnTimes = Utils.getDateFly(returnDate).getHours();
					searchDTO.setMinDepartureHour(String.valueOf(minDepartureTimes));
					searchDTO.setMinReturnHour(String.valueOf(minReturnTimes));
					searchDTO.setMaxDepartureHour(String.valueOf(minDepartureTimes+1));
					searchDTO.setMaxReturnHour(String.valueOf(minReturnTimes+1));
					searchDTO.setIncludedAirlineCodes(StringUtils.joinForUrl(",",flightOfferDTO.getCarriersCode()));
					flightOfferDTO.setUrlSkyscanner(skyscannerUrl(searchDTO));
				}
				result.addAll(resultAux);
				idx++;
			} else {
				break;
			}
		}
	    return result;
	}
	
	protected List<DateRangeDTO> getDatesSearch(FlightSearchFlexDTO searchDTO) {
		
		List<DateRangeDTO> result = new ArrayList<DateRangeDTO>();
		
		for (DateRangeDTO dateRange : searchDTO.getDateRangers()) {
			result.addAll(getDatesSearch(dateRange, searchDTO.getNumDays(), searchDTO.getStartWeekDays()));  
		}
				
		return result;
	}

	protected List<DateRangeDTO> getDatesSearch(DateRangeDTO dateRange, Integer numDays, List<Integer> startWeekDays) {
		
		List<DateRangeDTO> result = new ArrayList<DateRangeDTO>();
				
		// Start date, to 00:00:00
		String startDate = dateRange.getStartDate();
		String[] dates = startDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarStart = new GregorianCalendar();
		calendarStart.set(Calendar.HOUR_OF_DAY,0);
		calendarStart.set(Calendar.MINUTE, 0);
		calendarStart.set(Calendar.SECOND,0);
		calendarStart.set(Calendar.MILLISECOND,0);
		calendarStart.set(Calendar.YEAR, new Integer(year));
		calendarStart.set(Calendar.MONTH, new Integer(month) - 1);
		calendarStart.set(Calendar.DAY_OF_MONTH, new Integer(day));
				
		// End date, to 00:00:00
		String endDate = dateRange.getEndDate();
		dates = endDate.split(CalendarController.CHAR_SEP_DATE);
		year = dates[0];
		month = dates[1];
		day = dates[2];

		Calendar calendarEnd = new GregorianCalendar();
		calendarEnd.set(Calendar.HOUR_OF_DAY,0);
		calendarEnd.set(Calendar.MINUTE, 0);
		calendarEnd.set(Calendar.SECOND,0);
		calendarEnd.set(Calendar.MILLISECOND,0);
		calendarEnd.set(Calendar.YEAR, new Integer(year));
		calendarEnd.set(Calendar.MONTH, new Integer(month) - 1);
		calendarEnd.set(Calendar.DAY_OF_MONTH, new Integer(day));
		
		// Only departure date, not return
		if (numDays == null) {
		
			DateRangeDTO rangeNotReturn = null;
		
			while (!calendarStart.after(calendarEnd)) {
				if (validateRangeDate(calendarStart, startWeekDays)) {
					rangeNotReturn = new DateRangeDTO();
					rangeNotReturn.setStartDate(Utils.getStrCalendar(calendarStart));
					result.add(rangeNotReturn);
				}
				// Go to next day
				calendarStart.add(Calendar.DAY_OF_YEAR, 1);
			}
			
		} else {
		
			Calendar calendarWithNumDays = new GregorianCalendar();
			calendarWithNumDays.setTime(calendarStart.getTime()); 
			calendarWithNumDays.add(Calendar.DAY_OF_YEAR, numDays-1);
			
			while (!calendarWithNumDays.after(calendarEnd)) {
				if (validateRangeDate(calendarStart, startWeekDays)) {
					result.add(getDateRange(calendarStart, calendarWithNumDays));
				}
				// Go to next day
				calendarStart.add(Calendar.DAY_OF_YEAR, 1);
				calendarWithNumDays.add(Calendar.DAY_OF_YEAR, 1);
			} 
		}	
		
		return result;
	}
	
	// Validates that the dateStart is included in startWeekDays
	protected boolean validateRangeDate(Calendar calendarStart, List<Integer> startWeekDays) {
		if (startWeekDays.contains(calendarStart.get(Calendar.DAY_OF_WEEK))) {
			return true;
		};
		return false;
	}
	
	protected DateRangeDTO getDateRange(Calendar calendarGreg, Calendar calendarWithNumDays) {
		
		DateRangeDTO result = new DateRangeDTO();
		
		String startDate = Utils.getStrCalendar(calendarGreg);
		result.setStartDate(startDate);
						
		String endDate = Utils.getStrCalendar(calendarWithNumDays);
		result.setEndDate(endDate);
		
		return result;
	}
	
	@Override
	public String skyscannerUrl(FlightSearchDTO searchDTO) {

		StringBuffer buffer = new StringBuffer(URL_SKY_BASE);
		buffer.append(URL_SKY_PATH + searchDTO.getOriginLocationCode().toLowerCase());
		buffer.append(URL_SKY_PATH + searchDTO.getDestinationLocationCode().toLowerCase());
		buffer.append(URL_SKY_PATH + searchDTO.getDepartureDate().replaceAll("-", ""));
		if (searchDTO.getReturnDate() != null) {
			buffer.append(URL_SKY_PATH + searchDTO.getReturnDate().replaceAll("-", ""));
		}
		buffer.append("/?");
		buffer.append("adultsv2=" + searchDTO.getAdults());
		StringBuffer children = null;
		if (searchDTO.getChildren() != null && searchDTO.getChildren() != 0) {
			int count = searchDTO.getChildren();
			if (children == null) {
				children = new StringBuffer("&childrenv2=9");
				count--;
			}
			for (int idx = 0; idx < count; idx++) {
				children.append("%7C9");
			}
		}
		if (searchDTO.getInfants() != null && searchDTO.getInfants() != 0) {
			int count = searchDTO.getInfants();
			if (children == null) {
				children = new StringBuffer("&childrenv2=1");
				count--;
			}
			for (int idx = 0; idx < count; idx++) {
				children.append("%7C1");
			}
		}
		if (children != null) {
			buffer.append(children);
		}
		String cabinClass = searchDTO.getTravelClass() != null ? searchDTO.getTravelClass().toLowerCase() : "economy";
		buffer.append("&cabinclass=" + cabinClass);
		if (searchDTO.getMinDepartureHour()!=null) {
			int minDepartureTimes = Integer.parseInt(searchDTO.getMinDepartureHour())*60;
			buffer.append("&departure-times="+minDepartureTimes);
			if (searchDTO.getMaxDepartureHour()!=null) {
				buffer.append("-"+Integer.parseInt(searchDTO.getMaxDepartureHour())*60);
			} else {
				buffer.append("-1439");
			}
			if (searchDTO.getMinReturnHour()!=null) {
				int minReturnTimes = Integer.parseInt(searchDTO.getMinReturnHour())*60;
				buffer.append(","+minReturnTimes);
				if (searchDTO.getMaxReturnHour()!=null) {
					buffer.append("-"+Integer.parseInt(searchDTO.getMaxReturnHour())*60);
				} else {
					buffer.append("-1439");
				}				
			} 
		}
		String direct = searchDTO.getNonStop() != null ? searchDTO.getNonStop().toString() : "false";
		buffer.append("&preferdirects=" + direct);
		
		if (searchDTO.getIncludedAirlineCodes()!= null && searchDTO.getIncludedAirlineCodes().length()>0){
			StringBuffer airlines = new StringBuffer("&airlines=");
			if (searchDTO.getIncludedAirlineCodes().contains("IB")) {
				airlines.append("-32222,");	
			}
			if (searchDTO.getIncludedAirlineCodes().contains("AY")) {
				airlines.append("-32317,");	
			}
			if (searchDTO.getIncludedAirlineCodes().contains("UX")) {
				airlines.append("-32680,");	
			}
			if (searchDTO.getIncludedAirlineCodes().contains("AA")) {
				airlines.append("-32573,");	
			}
			if (searchDTO.getIncludedAirlineCodes().contains("BA")) {
				airlines.append("-32480,");	
			}
			if (searchDTO.getIncludedAirlineCodes().contains("AF")) {
				airlines.append("-32677,");	
			}
			if (searchDTO.getIncludedAirlineCodes().contains("KL")) {
				airlines.append("-32132,");	
			}
			if (searchDTO.getIncludedAirlineCodes().contains("FR")) {
				airlines.append("-31915,");	
			}
			if (searchDTO.getIncludedAirlineCodes().contains("VY")) {
				airlines.append("-31685,");	
			}
			if (searchDTO.getIncludedAirlineCodes().contains("VS")) {
				airlines.append("-31697,");	
			}
			buffer.append(airlines.deleteCharAt(airlines.length()-1));
		}
		return buffer.toString();
	}
	
	// Airport & City Search (autocomplete)
	// Find all the cities and airports starting by the keyword 'XXX'
	@Override
	public List<LocationDTO> locations(String keyword) throws ResponseException {
	
		// Using the synchronous cache
		String keyMem = "locations_fly_"+keyword;
	  	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	    List<LocationDTO> result = (List<LocationDTO>) syncCache.get(keyMem); // read from cache
	    if (result == null || result.isEmpty()) {
		
			Amadeus amadeus = amedeusService.get();
			
			Location[] locations = amadeus.referenceData.locations.get(Params
			  .with("keyword", keyword)
			  .and("subType", Locations.ANY)
				  .and("page[limit]", 20));		
		    result = new ArrayList<LocationDTO>();
			for (Location location : locations) {
				if (location.getSubType().equals(LocationType.CITY.toString()) 
					|| location.getSubType().equals(LocationType.AIRPORT.toString())) {
					result.add(mapper.map(location));
				}
			}
			// Order by cityCode, type(first CITY)
			/*result = result.stream().sorted((o1, o2)->{
					if (o1.getCityCode().compareTo(o2.getCityCode()) == 0) {
			            return o2.getType().compareTo(o1.getType());
			        } else {
			            return o1.getCityCode().compareTo(o2.getCityCode());
			        } 
				}).
	            collect(Collectors.toList());*/
			syncCache.put(keyMem, result); // populate cache
	    }	
		return result;
	}

}
