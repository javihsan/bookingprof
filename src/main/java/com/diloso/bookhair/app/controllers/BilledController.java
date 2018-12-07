package com.diloso.bookhair.app.controllers;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dao.BilledDAO;
import com.diloso.bookhair.app.negocio.dto.BilledDTO;

@Controller
@RequestMapping(value={"/*/billed", "/billed"})
public class BilledController {
	
	@Autowired
	protected BilledDAO billedDAO;
	
	
	@RequestMapping("/operator/listByInvoice")
	protected @ResponseBody
	List<BilledDTO> list(HttpServletRequest arg0, @RequestParam("id") Long id) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		List<BilledDTO> listBilled = billedDAO.getBilledByInvoice(id, locale.getLanguage());	
					
		return listBilled;
	}
	
	@RequestMapping("/operator/get")
	protected @ResponseBody
	BilledDTO get(@RequestParam("id") Long id) throws Exception {

		BilledDTO billed = billedDAO.getById(id);	
					
		return billed;
	}
	
		
}
