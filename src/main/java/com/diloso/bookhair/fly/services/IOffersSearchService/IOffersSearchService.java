package com.diloso.bookhair.fly.services.IOffersSearchService;

import java.util.List;

import com.amadeus.exceptions.ResponseException;
import com.diloso.bookhair.fly.services.dto.FlightOfferDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchFlexDTO;

public interface IOffersSearchService {
	
	public List<FlightOfferDTO> search(FlightSearchDTO searchDTO) throws ResponseException;
	
	public List<FlightOfferDTO> searchFlex(FlightSearchFlexDTO searchDTO) throws ResponseException;
	
	public List<FlightOfferDTO> searchFlexPrice(FlightSearchFlexDTO searchDTO) throws ResponseException;

}
