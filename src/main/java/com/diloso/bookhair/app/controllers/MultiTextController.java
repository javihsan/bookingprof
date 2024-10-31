package com.diloso.bookhair.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.config.impl.ConfigFirm;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.manager.IFirmManager;
import com.diloso.bookhair.app.negocio.manager.IMultiTextManager;
import com.diloso.bookhair.app.negocio.utils.ApplicationContextProvider;
import com.diloso.bookhair.app.negocio.utils.ExtendMessageSource;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
@RequestMapping(value={"/*/multiText", "/multiText"})
public class MultiTextController {
	
	public static final String WEB_PROP = "web.";
	public static final String WEB_CONFIG = "config.";
	
	@Autowired
	protected IMultiTextManager multiTextManager;
	
	@Autowired
	protected IFirmManager firmManager;
	
	@RequestMapping("/get")
	protected @ResponseBody
	MultiTextDTO get(@RequestParam("id") Long id) throws Exception {

		MultiTextDTO multiText = multiTextManager.getById(id);	
					
		return multiText;
	}
	
	
	@RequestMapping("/list")
	protected @ResponseBody
	List<MultiTextDTO> list() throws Exception {

		List<MultiTextDTO> listMultiText = multiTextManager.getMultiText();	
					
		return listMultiText;
	}

	
	@RequestMapping("/listByKey")
	protected @ResponseBody
	List<MultiTextDTO> listByKey(@RequestParam("key") String key) throws Exception {

		List<MultiTextDTO> listMultiText = multiTextManager.getMultiTextByKey(key);
			
		return listMultiText;
	}
	
	@RequestMapping("/listLocaleTexts")
	protected @ResponseBody
	List<MultiTextDTO> listLocaleTexts(HttpServletRequest arg0, HttpServletResponse arg1, @RequestParam("lanCode") String lanCode, @RequestParam("domain") String domain) throws Exception {
		
		if (lanCode.equals("")){
			lanCode = RequestContextUtils.getLocale(arg0).getLanguage();
		}
		
		// Set Locale, quitar poruqe en los móviles no va, pasar como parametro
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(arg0);
		LocaleEditor localeEditor = new LocaleEditor();
		localeEditor.setAsText(lanCode);
		localeResolver.setLocale(arg0, arg1, (Locale) localeEditor.getValue());
		
		// Using the synchronous cache
		String keyMem = "listLocaleTexts_"+domain+"_"+lanCode;
	  	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
	    List<MultiTextDTO> listMultiText = (List<MultiTextDTO>) syncCache.get(keyMem); // read from cache
	    if (listMultiText == null) {
		    
			FirmDTO firm = firmManager.getFirmDomain(domain);
			ConfigFirm configFirm = firm.getFirConfig();
			
			listMultiText = new ArrayList<MultiTextDTO>();
			ExtendMessageSource messageSourceApp = (ExtendMessageSource)ApplicationContextProvider.getApplicationContext().getBean("messageSource");
			Properties props = messageSourceApp.getResolvedProps(lanCode);
			MultiTextDTO multiText = null;
			String keyConfig = null;
			String valueConfig = null;
			String keyOriginal = null;
			for (String key : props.stringPropertyNames()) {
				keyOriginal = key;
				if (!key.startsWith(WEB_PROP)){ // Filtramos solo las de la app, no las de la web
					if (key.startsWith(WEB_CONFIG)){ // Propiedad configurable, obtenemos solo la indicada en la config
						String[] a = key.split("\\.");
						keyConfig = a[1];
						valueConfig = a[2];
						if (configFirm.getConfigDenon().getListDenon().get(keyConfig).equals(valueConfig)){
							key = key.substring(key.indexOf(valueConfig)+valueConfig.length()+1);	
						} else {
							key = null;
						}
					}
					if (key!=null){
						multiText = new MultiTextDTO();
						multiText.setMulKey(key);
						multiText.setMulLanCode(lanCode);
						multiText.setMulText(props.getProperty(keyOriginal));
						listMultiText.add(multiText);
					}	
				}	
			}
			
			syncCache.put(keyMem, listMultiText); // populate cache
	    }	
		return listMultiText;
	}

	public void setMultiTextDAO(IMultiTextManager iMultiTextManager) {
		this.multiTextManager = iMultiTextManager;
	}

	public void setFirmDAO(IFirmManager iFirmManager) {
		this.firmManager = iFirmManager;
	}
	
	

}

