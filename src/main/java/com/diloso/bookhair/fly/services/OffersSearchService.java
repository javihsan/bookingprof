package com.diloso.bookhair.fly.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.diloso.bookhair.app.controllers.CalendarController;
import com.diloso.bookhair.app.negocio.utils.Utils;
import com.diloso.bookhair.fly.services.IOffersSearchService.IOffersSearchService;
import com.diloso.bookhair.fly.services.dto.FlightOfferDTO;
import com.diloso.bookhair.fly.services.dto.input.DateRangeDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchFlexDTO;
import com.diloso.bookhair.fly.services.utils.MapperFly;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OffersSearchService implements IOffersSearchService {

	@Autowired
	protected AmedeusService amedeusService;
	
	@Autowired
	protected MapperFly mapperParams;
	
	@Override
	public List<FlightOfferDTO> search(FlightSearchDTO searchDTO) throws ResponseException {

		Amadeus amadeus = amedeusService.get();

		Params params = mapperParams.map(searchDTO);

		FlightOfferSearch[] flightOfferSearch = amadeus.shopping.flightOffersSearch.get(params);
		
	    return mapperParams.map(flightOfferSearch, searchDTO.getDestinationLocationCode());
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
		
		Params params = mapperParams.map(searchDTO);
	
		List<FlightOfferDTO> result = new ArrayList<FlightOfferDTO>();
		List<DateRangeDTO> listDateRanges =  getDatesSearch(searchDTO);
		int idx = 0;
		for (DateRangeDTO dateRange : listDateRanges) {
			if (idx < maxSearchs) {
				params = mapperParams.map(params,dateRange);
				FlightOfferSearch[] flightOfferSearch = amadeus.shopping.flightOffersSearch.get(params);
				result.addAll(mapperParams.map(flightOfferSearch, searchDTO.getDestinationLocationCode(), simple));
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

		StringBuffer buffer = new StringBuffer("https://www.skyscanner.es/transporte/vuelos");
		buffer.append("/" + searchDTO.getOriginLocationCode().toLowerCase());
		buffer.append("/" + searchDTO.getDestinationLocationCode().toLowerCase());
		buffer.append("/" + searchDTO.getDepartureDate().replaceAll("-", ""));
		if (searchDTO.getReturnDate() != null) {
			buffer.append("/" + searchDTO.getReturnDate().replaceAll("-", ""));
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
		buffer.append("&departure-times=120-1439");
		String direct = searchDTO.getNonStop() != null ? searchDTO.getNonStop().toString() : "false";
		buffer.append("&preferdirects=" + direct);

		return buffer.toString();
	}
    
}
