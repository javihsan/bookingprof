package com.diloso.bookhair.app.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.UncategorizedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.manager.IClientManager;
import com.diloso.bookhair.app.negocio.manager.IFirmManager;
import com.diloso.bookhair.app.negocio.manager.ILocalManager;
import com.diloso.bookhair.app.negocio.utils.Utils;
import com.diloso.weblogin.aut.AppUser;
import com.diloso.weblogin.aut.DatastoreUserRegistry;
import com.diloso.weblogin.aut.UserRegistry;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value={"/*/client", "/client"})
public class ClientController {
	
	protected static final Logger log = Logger.getLogger(ClientController.class.getName());
	
	public static final String KEY_CACHE_CLIENTS = "CLIENTS";
	
	@Autowired
	protected MessageSource messageSourceApp;
	
	@Autowired
	protected IFirmManager iFirmManager;
	
	@Autowired
	protected IClientManager iClientManager;
	
	@Autowired
	protected ILocalManager iLocalManager;
	
	protected UserRegistry userRegistry = new DatastoreUserRegistry();
	
	@ExceptionHandler(UncategorizedDataAccessException.class)
	@ResponseStatus(value=HttpStatus.CONFLICT,reason="")
	protected void error(Exception t, HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.CONFLICT.value(), t.getMessage());
	}
	
	/* El nombre no está vacío
	*/
	protected boolean validateNew(HttpServletRequest arg0, String cliName) throws UncategorizedDataAccessException {
		boolean res = true;
		String message = "";
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		if (cliName==null || cliName.length()==0){
			message = messageSourceApp.getMessage("form.error.client.nameReq", null, locale);
			res = false;
		}
		
		if (!res){
			throw new ErrorService(message, null);
		}
		return res;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/operator/update")
	@ResponseStatus(HttpStatus.OK)
	protected void update(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		Long id = Long.parseLong(arg0.getParameter("id"));
		String cliEmail = arg0.getParameter("cliEmail").toLowerCase();
		String cliName = arg0.getParameter("cliName");
		String cliSurname = arg0.getParameter("cliSurname");
		String cliTelf1 = arg0.getParameter("cliTelf1");
		String cliTelf2 = arg0.getParameter("cliTelf2");
		String cliDesc = arg0.getParameter("cliDesc");
		Integer cliGender = Integer.parseInt(arg0.getParameter("cliGender"));
		String strCliBirthday = arg0.getParameter("cliBirthday");
				
		if (validateNew(arg0, cliName)){ 
			
			ClientDTO client = new ClientDTO();
			client.setId(id);
			client.setWhoEmail(cliEmail);
			client.setWhoName(cliName);
			client.setWhoSurname(cliSurname);
			client.setWhoTelf1(cliTelf1);
			client.setWhoTelf2(cliTelf2);
			client.setWhoDesc(cliDesc);
			if (cliGender>=0){
				client.setWhoGender(cliGender);
			}	
			if (strCliBirthday!=null && !strCliBirthday.equals("")){
				client.setWhoBirthday(Utils.getDate(strCliBirthday, locale));
			}
			/*
			List<String> firGwtUsers = new ArrayList<String>();
			String[] a = strFirGwtUsers.split(",");
			AppUser user = null;
			Set<AppRole> roles = EnumSet.noneOf(AppRole.class);
			//roles.add(AppRole.OPERATOR_READ);
			//roles.add(AppRole.USER);
			for (String strUser : a) {
				if (strUser.length()>0){
					firGwtUsers.add(strUser);
					if(userRegistry.findUser(strUser)==null){
						user = new AppUser(strUser, roles, true);
						userRegistry.registerUser(user);
					}	
				}	
			}
			firm.setFirGwtUsers(firGwtUsers);
			firm = firmDAO.update(firm);
			*/
			iClientManager.update(client);

		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/operator/remove")
	@ResponseStatus(HttpStatus.OK)
	public void remove(HttpServletRequest arg0, HttpServletResponse arg1, @RequestParam("id") Long id)
			throws Exception {
		
		ClientDTO client = iClientManager.getById(id);
		if (client!=null){
			client.setEnabled(0);
			iClientManager.update(client);
			log.info("Cliente borrado : "+client.getId());
		}	
	}
	
	@RequestMapping("/operator/list")
	protected @ResponseBody
	List<ClientDTO> list(@RequestParam("domain") String domain) throws Exception {

		FirmDTO firm = iFirmManager.getFirmDomain(domain);
		
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		String key = CalendarController.KEY_CACHE + domain + KEY_CACHE_CLIENTS;
		List<ClientDTO> listClient = (List<ClientDTO>) syncCache.get(key);
		if (listClient == null) {
			listClient = iClientManager.getClient(firm.getId());
			syncCache.put (key, listClient, Expiration.byDeltaSeconds(18000)); // 5 horas 60*60*5 segundos en un día
		}

		//List<ClientDTO> listClient = clientDAO.getClient(firm.getId());	
					
		return listClient;
	}
	
	@RequestMapping("/operator/listByEmail")
	protected @ResponseBody
	ClientDTO listByEmail(@RequestParam("domain") String domain, @RequestParam("email") String email) throws Exception {
		
		FirmDTO firm = iFirmManager.getFirmDomain(domain);
		
		ClientDTO client = iClientManager.getByEmail(firm.getId(), email);	
					
		return client;
	}
	
	@RequestMapping("/booking/getBySession")
	protected @ResponseBody
	ClientDTO getBySession(@RequestParam("firmId") String firmId) throws Exception {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		
		ClientDTO client = iClientManager.getByEmail(new Long(firmId), email);	
					
		return client;
	}
	
	@RequestMapping("/operator/getUserSession")
	protected @ResponseBody
	AppUser getUserSession(@RequestParam("firmId") String firmId) throws Exception {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		AppUser user = userRegistry.findUser(email,new Long(firmId));
							
		return user;
	}

	public void setMessageSourceApp(MessageSource messageSourceApp) {
		this.messageSourceApp = messageSourceApp;
	}

	public void setFirmDAO(IFirmManager iFirmManager) {
		this.iFirmManager = iFirmManager;
	}

	public void setClientDAO(IClientManager iClientManager) {
		this.iClientManager = iClientManager;
	}

	public void setLocalDAO(ILocalManager iLocalManager) {
		this.iLocalManager = iLocalManager;
	}

	public void setUserRegistry(UserRegistry userRegistry) {
		this.userRegistry = userRegistry;
	}
	
	
}
