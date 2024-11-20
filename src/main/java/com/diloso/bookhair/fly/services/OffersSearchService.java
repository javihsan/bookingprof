package com.diloso.bookhair.fly.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
		
	    return mapperParams.map(flightOfferSearch);
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

		Amadeus amadeus = amedeusService.get();

		// Solo obtenemos el mejor precio del dia
		searchDTO.setMax(1);
		Params params = mapperParams.map(searchDTO);
	
		List<FlightOfferDTO> result = new ArrayList<FlightOfferDTO>();
		List<DateRangeDTO> listDateRanges =  getDatesSearch(searchDTO);
		for (DateRangeDTO dateRange : listDateRanges) {
			params = mapperParams.map(params,dateRange);
			FlightOfferSearch[] flightOfferSearch = amadeus.shopping.flightOffersSearch.get(params);
			result.addAll(mapperParams.map(flightOfferSearch, simple));
		}
	    return result;
	}
	
	protected List<DateRangeDTO> getDatesSearch(FlightSearchFlexDTO searchDTO) {
		
		List<DateRangeDTO> result = new ArrayList<DateRangeDTO>();
		
		for (DateRangeDTO dateRange : searchDTO.getDateRangers()) {
			result.addAll(getDatesSearch(dateRange, searchDTO.getNumDays(), searchDTO.getNumDays()));  
		}
				
		return result;
	}

	protected List<DateRangeDTO> getDatesSearch(DateRangeDTO dateRange, Integer numDays, Integer numNonWorkingDays) {
		
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
		
		Calendar calendarWithNumDays = new GregorianCalendar();
		calendarWithNumDays.setTime(calendarStart.getTime()); 
		calendarWithNumDays.add(Calendar.DAY_OF_YEAR, numDays);

		while (!calendarWithNumDays.after(calendarEnd)) {
			result.add(getDateRange(calendarStart, numDays, numNonWorkingDays));
			// Go to next day
			calendarStart.add(Calendar.DAY_OF_YEAR, 1);
			calendarWithNumDays.add(Calendar.DAY_OF_YEAR, 1); 
		} 
			
		return result;
	}
	
	protected DateRangeDTO getDateRange(Calendar calendarGreg, Integer numDays, Integer numNonWorkingDays) {
		
		DateRangeDTO result = new DateRangeDTO();
		String startDate = Utils.getStrCalendar(calendarGreg);
		result.setStartDate(startDate);
						
		// Vamos numDays días adelante
		Calendar calendarWithNumDays = new GregorianCalendar();
		calendarWithNumDays.setTime(calendarGreg.getTime()); 
		calendarWithNumDays.add(Calendar.DAY_OF_YEAR, numDays);
		String endDate = Utils.getStrCalendar(calendarWithNumDays);
		result.setEndDate(endDate);
		
		return result;
	}
}
