package com.diloso.bookhair.fly.services;

import java.util.List;

import com.amadeus.exceptions.ResponseException;
import com.diloso.bookhair.fly.services.dto.FlightOfferDTO;
import com.diloso.bookhair.fly.services.dto.LocationDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchFlexDTO;

public interface IOffersSearchService {
	
	public List<FlightOfferDTO> search(FlightSearchDTO searchDTO) throws ResponseException;
	
	public List<FlightOfferDTO> searchFlex(FlightSearchFlexDTO searchDTO) throws ResponseException;
	
	public List<FlightOfferDTO> searchFlexPrice(FlightSearchFlexDTO searchDTO) throws ResponseException;
	
	public String skyscannerUrl(FlightSearchDTO searchDTO);
	
	public List<LocationDTO> locations(String keyword) throws ResponseException;
	
}
