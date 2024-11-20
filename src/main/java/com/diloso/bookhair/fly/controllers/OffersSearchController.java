package com.diloso.bookhair.fly.controllers;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.datastore.UtilidadesData;
import com.diloso.bookhair.app.negocio.utils.Utils;
import com.diloso.bookhair.fly.services.IOffersSearchService.IOffersSearchService;
import com.diloso.bookhair.fly.services.dto.FlightOfferDTO;
import com.diloso.bookhair.fly.services.dto.input.DateRangeDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchDTO;
import com.diloso.bookhair.fly.services.dto.input.FlightSearchFlexDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value={"/*/flight-offers", "/flight-offers"})
public class OffersSearchController {
	
	@Autowired
	protected IOffersSearchService offersSearchService;
	
	@RequestMapping("/search")
	protected @ResponseBody
	List<FlightOfferDTO> search(HttpServletRequest arg0, HttpServletResponse arg1, FlightSearchDTO searchDTO) throws Exception {

		return offersSearchService.search(searchDTO);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/search")
	@ResponseStatus(HttpStatus.OK)
	protected @ResponseBody
	List<FlightOfferDTO> searchPost(HttpServletRequest arg0, HttpServletResponse arg1, @RequestBody FlightSearchDTO searchDTO) throws Exception {

		return offersSearchService.search(searchDTO);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/search-flex")
	@ResponseStatus(HttpStatus.OK)
	protected @ResponseBody
	List<FlightOfferDTO> searchFlex(HttpServletRequest arg0, HttpServletResponse arg1, @RequestBody FlightSearchFlexDTO searchDTO) throws Exception {
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		for (DateRangeDTO dateRange : searchDTO.getDateRangers()) {
			Date startDate = Utils.getDate(dateRange.getStartDate(), locale);
			Date endDate = Utils.getDate(dateRange.getEndDate(), locale);
			if (
				!( endDate.after(startDate) )
				||( searchDTO.getNumDays()>UtilidadesData.daysDifference(startDate,endDate) )
			   ){
				throw new Exception();
			}
		}	
		return offersSearchService.searchFlex(searchDTO);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/search-flex-price")
	@ResponseStatus(HttpStatus.OK)
	protected @ResponseBody
	List<FlightOfferDTO> searchFlexPrice(HttpServletRequest arg0, HttpServletResponse arg1, @RequestBody FlightSearchFlexDTO searchDTO) throws Exception {
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		for (DateRangeDTO dateRange : searchDTO.getDateRangers()) {
			Date startDate = Utils.getDate(dateRange.getStartDate(), locale);
			Date endDate = Utils.getDate(dateRange.getEndDate(), locale);
			if (
				!( endDate.after(startDate) )
				||( searchDTO.getNumDays()>UtilidadesData.daysDifference(startDate,endDate) )
			   ){
				throw new Exception();
			}
		}	
		return offersSearchService.searchFlexPrice(searchDTO);
	}
}
