package com.diloso.bookhair.app.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.UncategorizedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.ProductClassDTO;
import com.diloso.bookhair.app.negocio.manager.ILangManager;
import com.diloso.bookhair.app.negocio.manager.IMultiTextManager;
import com.diloso.bookhair.app.negocio.manager.IProductClassManager;
import com.diloso.bookhair.app.negocio.manager.ProductClassManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value={"/*/productClass", "/productClass"})
public class ProductClassController {
	
	protected static final String TCL_NAME_PARAM = "pclName";
	
	@Autowired
	protected MessageSource messageSourceApp;
	
	@Autowired
	protected IProductClassManager productClassManager;
	
	@Autowired
	protected IMultiTextManager multiTextManager;
	
	@Autowired
	protected ILangManager langManager;
	
	@ExceptionHandler(UncategorizedDataAccessException.class)
	@ResponseStatus(value=HttpStatus.CONFLICT,reason="")
	protected void error(Exception t, HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.CONFLICT.value(), t.getMessage());
	}
	
	/* El nombre y el valor no est� vac�o.
	*/
	protected boolean validateNew(HttpServletRequest arg0, String defaultNameValue) throws UncategorizedDataAccessException {
		boolean res = true;
		String message = "";
		Locale locale = RequestContextUtils.getLocale(arg0);
		if (defaultNameValue==null || defaultNameValue.length()==0){
			message =  messageSourceApp.getMessage("form.error.productClass.nameReq", null, locale);
			res = false;
		}
		
		if (!res){
			throw new ErrorService(message, null);
		}
		return res;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/manager/new")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	protected ProductClassDTO newObject(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		String langDefault = locale.getLanguage();
		
		String pclId = arg0.getParameter("id");
		
		String defaultNameValue = arg0.getParameter(TCL_NAME_PARAM+"_"+langDefault);			
				
		if (validateNew(arg0, defaultNameValue)){ 	
						
			Long localId = new Long(arg0.getParameter("localId"));
			
			ProductClassDTO productClass = new ProductClassDTO();
			
			if (pclId!=null){ // Existe
				productClass = productClassManager.getById(new Long(pclId));
			}
			
			String nameValue = "";
			String keyNameMulti = ProductClassManager.KEY_MULTI_TASKCLASS_NAME+localId+"_"+defaultNameValue.toLowerCase();
			Map<String,String> hashNamesParam = new HashMap<String,String>();

			List<LangDTO> listLang = langManager.getLang();	
			for (LangDTO lang : listLang) {
				nameValue = arg0.getParameter(TCL_NAME_PARAM+"_"+lang.getLanCode());
				if (nameValue == null || nameValue.length()==0){
					nameValue = defaultNameValue;
				}
				hashNamesParam.put(lang.getLanCode(), nameValue);
			}

			if (pclId!=null){ // Existe
				List<MultiTextDTO> listMulti = multiTextManager.getMultiTextByKey(productClass.getPclNameMulti());
				for (MultiTextDTO nameMulti: listMulti){
					nameValue = hashNamesParam.get(nameMulti.getMulLanCode());
					nameMulti.setMulText(nameValue);
					multiTextManager.update(nameMulti);
				}
			} else {
				int indx = 0;
				while (multiTextManager.getByLanCodeAndKey(locale.getLanguage(), keyNameMulti)!=null){
					keyNameMulti = ProductClassManager.KEY_MULTI_TASKCLASS_NAME+localId+"_"+defaultNameValue.toLowerCase()+"_"+indx;
					indx ++;
				}
				
				MultiTextDTO nameMulti = null;
				for (String codeLangMap : hashNamesParam.keySet()) {
					nameValue = hashNamesParam.get(codeLangMap);
					nameMulti = new MultiTextDTO();
					nameMulti.setEnabled(1);
					nameMulti.setMulKey(keyNameMulti);
					nameMulti.setMulLanCode(codeLangMap);
					nameMulti.setMulText(nameValue);
					multiTextManager.create(nameMulti);
				}
			}
					
			if (pclId!=null){ // Existe
				productClass = productClassManager.update(productClass);
			} else { // Es nuevo
				productClass.setEnabled(1);
				productClass.setPclNameMulti(keyNameMulti);
				productClass = productClassManager.create(productClass);
			}
			return productClass;
		}
		return null;
	}

	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/remove")
	@ResponseStatus(HttpStatus.OK)
	protected void remove(@RequestParam("id") Long id)
			throws Exception {
			
		if (id!=null){ // Existe
			ProductClassDTO productClass = productClassManager.getById(id);
			
			List<MultiTextDTO> listMulti = multiTextManager.getMultiTextByKey(productClass.getPclNameMulti());
			for (MultiTextDTO nameMulti: listMulti){
				multiTextManager.remove(nameMulti.getId());
			}
			productClassManager.remove(id);
		}
	}
	
	@RequestMapping("/operator/list")
	protected @ResponseBody
	List<ProductClassDTO> list(HttpServletRequest arg0) throws Exception {

		Locale locale = RequestContextUtils.getLocale(arg0);
		
		List<ProductClassDTO> listProductClass = productClassManager.getProductClassByLang(locale.getLanguage());	
					
		return listProductClass;
	}
		
	@RequestMapping("/manager/get")
	protected @ResponseBody
	ProductClassDTO get(@RequestParam("id") Long id) throws Exception {

		ProductClassDTO productClass = productClassManager.getById(id);	
					
		return productClass;
	}

	public void setMessageSourceApp(MessageSource messageSourceApp) {
		this.messageSourceApp = messageSourceApp;
	}

	public void setProductClassDAO(IProductClassManager iProductClassManager) {
		this.productClassManager = iProductClassManager;
	}

	public void setMultiTextDAO(IMultiTextManager iMultiTextManager) {
		this.multiTextManager = iMultiTextManager;
	}

	public void setLangDAO(ILangManager iLangManager) {
		this.langManager = iLangManager;
	}
	

}
