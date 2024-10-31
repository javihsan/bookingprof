package com.diloso.bookhair.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value={"/*/operatornew", "/operatornew"})
public class OperatorNewController {

	@RequestMapping("")
	public ModelAndView init(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {
		
		ModelAndView mav = new ModelAndView("/app/bookingOperatorNew");
		return mav;

	}

	

}
