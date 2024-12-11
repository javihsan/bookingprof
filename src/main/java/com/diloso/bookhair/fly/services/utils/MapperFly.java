package com.diloso.bookhair.fly.services.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amadeus.Params;
import com.amadeus.resources.Destination;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOfferSearch.AirportInfo;
import com.amadeus.resources.FlightOfferSearch.Itinerary;
import com.amadeus.resources.FlightOfferSearch.SearchSegment;
import com.amadeus.resources.Location;
import com.diloso.bookhair.fly.services.dto.AirportInfoDTO;
import com.diloso.bookhair.fly.services.dto.FlightOfferDTO;
import com.diloso.bookhair.fly.services.dto.ItineraryDTO;
import com.diloso.bookhair.fly.services.dto.LocationDTO;
import com.diloso.bookhair.fly.services.dto.LocationType;
import com.diloso.bookhair.fly.services.dto.SearchSegmentDTO;
import com.diloso.bookhair.fly.services.dto.input.DateRangeDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchDTO;

import jakarta.inject.Singleton;

@Component
@Singleton
public class MapperFly {

	protected static final String ORIGIN_LOC = "originLocationCode";
	protected static final String DEST_LOC = "destinationLocationCode";
	protected static final String DEPARTURE_DATE = "departureDate";
	protected static final String RETURN_DATE = "returnDate";
	protected static final String ADULTS = "adults";
	protected static final String CHILDREN = "children";
	protected static final String INFANTS = "infants";
	protected static final String AIRLINE_IN = "includedAirlineCodes";
	protected static final String AIRLINE_EX = "excludedAirlineCodes";
	protected static final String NON_STOP = "nonStop";
	protected static final String CURRENCY = "currencyCode";
	protected static final String TRAVEL_CLASS = "travelClass";
	protected static final String MAX_PRICE = "maxPrice";
	protected static final String MAX = "max";
	
	
	public Params map(FlightSearchDTO searchDTO){
		
		if (searchDTO.getOriginLocationCode()!=null) {
			Params params = Params.with(ORIGIN_LOC, searchDTO.getOriginLocationCode());
			
			if (searchDTO.getOriginLocationCode()!=null) {
				params.and(DEST_LOC, searchDTO.getDestinationLocationCode());
			}
						
			if (searchDTO.getDepartureDate()!=null) {
				params.and(DEPARTURE_DATE, searchDTO.getDepartureDate());
			}
			
			if (searchDTO.getReturnDate()!=null) {
				params.and(RETURN_DATE, searchDTO.getReturnDate());
			}
			
			if (searchDTO.getAdults()!=null) {
				params.and(ADULTS, searchDTO.getAdults());
			}
			
			if (searchDTO.getChildren()!=null) {
				params.and(CHILDREN, searchDTO.getChildren());
			}
			
			if (searchDTO.getInfants()!=null) {
				params.and(INFANTS, searchDTO.getInfants());
			}
			
			if (searchDTO.getIncludedAirlineCodes()!=null) {
				params.and(AIRLINE_IN, searchDTO.getIncludedAirlineCodes());
			}
			
			if (searchDTO.getExcludedAirlineCodes()!=null) {
				params.and(AIRLINE_EX, searchDTO.getExcludedAirlineCodes());
			}
			
			if (searchDTO.getNonStop()!=null) {
				params.and(NON_STOP, searchDTO.getNonStop());
			}
			
			if (searchDTO.getCurrencyCode()!=null) {
				params.and(CURRENCY, searchDTO.getCurrencyCode());
			}
			
			if (searchDTO.getTravelClass()!=null) {
				params.and(TRAVEL_CLASS, searchDTO.getTravelClass());
			}
			
			if (searchDTO.getMaxPrice()!=null) {
				params.and(MAX_PRICE, searchDTO.getMaxPrice());
			}
			
			if (searchDTO.getMax()!=null) {
				params.and(MAX, searchDTO.getMax());
			}
			
			return params;
		}
		return null;
	}
	
	public AirportInfoDTO map (AirportInfo airportInfo) {
		AirportInfoDTO result = new AirportInfoDTO();
		
		result.setAt(airportInfo.getAt());
		result.setIataCode(airportInfo.getIataCode());
		result.setTerminal(airportInfo.getTerminal());

		return result;
	}
	
	public SearchSegmentDTO map (SearchSegment searchSegment) {
		SearchSegmentDTO result = new SearchSegmentDTO();
		
		result.setCarrierCode(searchSegment.getCarrierCode());
		result.setArrival(map(searchSegment.getArrival()));
		result.setDeparture(map(searchSegment.getDeparture()));
		return result;
	}
	
	public ItineraryDTO map (Itinerary itinerary) {
		ItineraryDTO result = new ItineraryDTO();
		result.setDuration(itinerary.getDuration());
		List<SearchSegmentDTO> segments = new ArrayList<SearchSegmentDTO>(); 
		for (SearchSegment segment : itinerary.getSegments()) {
			segments.add(map(segment));
		}
		result.setSegments(segments);
		return result;
	}
	
	public FlightOfferDTO map (FlightOfferSearch flightOffer, boolean simple) {
		
		FlightOfferDTO result = new FlightOfferDTO();
		if (!simple){
			List<ItineraryDTO> itineraries = new ArrayList<ItineraryDTO>(); 
			for (Itinerary itinerary : flightOffer.getItineraries()) {
				itineraries.add(map(itinerary));
			}
			result.setItineraries(itineraries);
		}
		result.setPrice(Float.valueOf(flightOffer.getPrice().getGrandTotal()));
		result.setNumberOfBookableSeats(flightOffer.getNumberOfBookableSeats());
		result.setDepartureDate(flightOffer.getItineraries()[0].getSegments()[0].getDeparture().getAt());
		if (flightOffer.getItineraries().length>1){
			result.setReturnDate(flightOffer.getItineraries()[1].getSegments()[0].getDeparture().getAt());
		}	
		result.setCarriersCode(Arrays.asList(flightOffer.getValidatingAirlineCodes()));
		return result;
	}
	
	public List<FlightOfferDTO> map(FlightOfferSearch[] flightOfferSearch){
		
		return map(flightOfferSearch, false);		
	}
	
	public List<FlightOfferDTO> map(FlightOfferSearch[] flightOfferSearch, boolean simple){
		
		List<FlightOfferDTO> result = new ArrayList<FlightOfferDTO>(); 
		for (FlightOfferSearch flightOffer : flightOfferSearch) {
			result.add(map(flightOffer, simple));
		}
		return result;		
	}
	
	public Params map(Params params, DateRangeDTO dateRangeDTO){
		
		params.and(DEPARTURE_DATE, dateRangeDTO.getStartDate());
		if (dateRangeDTO.getEndDate()!=null) {
			params.and(RETURN_DATE, dateRangeDTO.getEndDate());
		}	
		
		return params;
	}
	
	public LocationDTO map (Location location) {
		LocationDTO result = new LocationDTO();
		
		result.setType(location.getSubType());
		if (location.getSubType().equals(LocationType.AIRPORT.toString())) {
			result.setName(location.getAddress().getCityCode()+" "+location.getName());
		} else {
			result.setName(location.getName());
		}
		result.setIataCode(location.getIataCode());
		result.setCityCode(location.getAddress().getCityCode());
		return result;
	}
	
	public LocationDTO map (Destination destination) {
		LocationDTO result = new LocationDTO();
		
		result.setType(destination.getSubtype());
		result.setName(destination.getName());
		result.setIataCode(destination.getIataCode());
		//result.setCityCode(destination.getAddress().getCityCode());
		return result;
	}
}
