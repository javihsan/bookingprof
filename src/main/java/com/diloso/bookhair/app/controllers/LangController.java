package com.diloso.bookhair.app.controllers;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dao.LangDAO;
import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;

@Controller
@RequestMapping(value={"/*/lang", "/lang"})
public class LangController {
	
	@Autowired
	protected LangDAO langDAO;
	
	@Autowired
	protected MultiTextDAO multiTextDAO;
	
	@RequestMapping("/list")
	public @ResponseBody
	List<LangDTO> list() throws Exception {

		List<LangDTO> listLang = langDAO.getLang();	
					
		return listLang;
	}
	

	@RequestMapping("/get")
	protected @ResponseBody
	LangDTO get(@RequestParam("id") Long id) throws Exception {

		LangDTO lang = langDAO.getById(id);	
					
		return lang;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/admin/new")
	@ResponseStatus(HttpStatus.OK)
	protected void newObject(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		LangDTO lang = new LangDTO();
		lang.setEnabled(1);
		lang.setLanCode("fr");
		lang.setLanName("Français");
		
		lang = langDAO.create(lang);

		// Obtenemos todos los multitext de sistema del idioma actual
		List<MultiTextDTO> listMultiDefault = multiTextDAO.getMultiTextSystemByLanCode(locale.getLanguage());
		MultiTextDTO multiTextKey = null;
		for (MultiTextDTO multiTextDefault : listMultiDefault) {
			multiTextKey = multiTextDAO.getByLanCodeAndKey(lang.getLanCode(),multiTextDefault.getMulKey());
			if (multiTextKey==null){ // Si no existe en el nuevo idioma, insertar con nuevo idioma y el texto del idioma actual
				multiTextKey = new MultiTextDTO();
				multiTextKey.setEnabled(1);
				multiTextKey.setMulKey(multiTextDefault.getMulKey());
				multiTextKey.setMulLanCode(lang.getLanCode());
				multiTextKey.setMulText(multiTextDefault.getMulText());
				multiTextDAO.create(multiTextKey);
			}
		}


	}
	

}
