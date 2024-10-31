package com.diloso.bookhair.app.controllers;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dto.BilledDTO;
import com.diloso.bookhair.app.negocio.manager.IBilledManager;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value={"/*/billed", "/billed"})
public class BilledController {
	
	@Autowired
	protected IBilledManager billedManager;
	
	
	@RequestMapping("/operator/listByInvoice")
	protected @ResponseBody
	List<BilledDTO> list(HttpServletRequest arg0, @RequestParam("id") Long id) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		List<BilledDTO> listBilled = billedManager.getBilledByInvoice(id, locale.getLanguage());	
					
		return listBilled;
	}
	
	@RequestMapping("/operator/get")
	protected @ResponseBody
	BilledDTO get(@RequestParam("id") Long id) throws Exception {

		BilledDTO billed = billedManager.getById(id);	
					
		return billed;
	}
	
		
}
