package com.diloso.bookhair.fly.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diloso.bookhair.fly.services.IOffersSearchService.IOffersSearchService;
import com.diloso.bookhair.fly.services.dto.FlightOfferDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value={"/*/aiport", "/aiport"})
public class AirportController {
	
	@Autowired
	protected IOffersSearchService offersSearchService;
	
	@RequestMapping("/search")
	protected @ResponseBody
	List<FlightOfferDTO> search (HttpServletRequest arg0, HttpServletResponse arg1, FlightSearchDTO searchDTO) throws Exception {

		return offersSearchService.search(searchDTO);
	}

	@RequestMapping("/destinations")
	protected @ResponseBody
	List<FlightOfferDTO> destinations (HttpServletRequest arg0, HttpServletResponse arg1, FlightSearchDTO searchDTO) throws Exception {

		return offersSearchService.search(searchDTO);
	}
}
