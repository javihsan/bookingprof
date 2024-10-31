package com.diloso.bookhair.app.controllers;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

import com.diloso.bookhair.app.negocio.dto.BilledDTO;
import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.dto.EventDTO;
import com.diloso.bookhair.app.negocio.dto.InvoiceDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.ProductDTO;
import com.diloso.bookhair.app.negocio.manager.IBilledManager;
import com.diloso.bookhair.app.negocio.manager.ICalendarManager;
import com.diloso.bookhair.app.negocio.manager.IClientManager;
import com.diloso.bookhair.app.negocio.manager.IEventManager;
import com.diloso.bookhair.app.negocio.manager.IInvoiceManager;
import com.diloso.bookhair.app.negocio.manager.ILocalManager;
import com.diloso.bookhair.app.negocio.manager.ILocalTaskManager;
import com.diloso.bookhair.app.negocio.manager.IProductManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value={"/*/invoice", "/invoice"})
public class InvoiceController {
	
	@Autowired
	protected MessageSource messageSourceApp;
		
	@Autowired
	protected ILocalManager localManager;
	
	@Autowired
	protected IClientManager clientManager;
	
	@Autowired
	protected IInvoiceManager invoiceManager;
	
	@Autowired
	protected ILocalTaskManager localTaskManager;
	
	@Autowired
	protected IProductManager productManager;
	
	@Autowired
	protected IEventManager eventManager;
	
	@Autowired
	protected IBilledManager billedManager;
	
	@Autowired
	protected ICalendarManager calendarManager;
	
	@ExceptionHandler(UncategorizedDataAccessException.class)
	@ResponseStatus(value=HttpStatus.CONFLICT,reason="")
	protected void error(Exception t, HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.CONFLICT.value(), t.getMessage());
	}
	
	/* El cliente, fecha a facturar, servicios (o productos) y tarifas no está vacío.
	*/
	protected boolean validateNew(HttpServletRequest arg0, boolean isNew, String strInvClientId, String cliName, Date invTime, String strInvTask, String strInvRate) throws UncategorizedDataAccessException {
		boolean res = true;
		String message = "";
		Locale locale = RequestContextUtils.getLocale(arg0);
		if(isNew && (cliName == null || cliName.length()==0)){
			message = messageSourceApp.getMessage("form.error.client.nameReq", null, locale);
			res = false;
		} else if (!isNew && (strInvClientId==null || strInvClientId.length()==0)){
			message = messageSourceApp.getMessage("form.error.invoice.clientReq", null, locale);
			res = false;
		} else if (invTime==null){
			message = messageSourceApp.getMessage("form.error.invoice.timeReq", null, locale);
			res = false;
		}  else if (strInvTask==null || strInvTask.length()==0){
			message = messageSourceApp.getMessage("form.error.invoice.taskReq", null, locale);
			res = false;
		} else if (strInvRate==null || strInvRate.length()==0){
			message = messageSourceApp.getMessage("form.error.invoice.rateReq", null, locale);
			res = false;
		} else {
			try{
				
				String[] rates = strInvRate.split(",");
				for (String str : rates) {
					new Float(str);
				}
		
			} catch( NumberFormatException e){
				message = messageSourceApp.getMessage("form.error.invoice.rateNum", null, locale);
				res = false;
			}
		}
		
		if (!res){
			throw new ErrorService(message, null);
		}
		return res;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/operator/new")
	@ResponseStatus(HttpStatus.OK)
	protected @ResponseBody
	InvoiceDTO newObject (HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		Long lngInvTime = new Long(arg0.getParameter("invTime"));
		Date invTime = new Date(lngInvTime);
		
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.setTime(invTime);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		invTime = calendarGreg.getTime();
		
		String strInvClientId = arg0.getParameter("invClientId");
		String cliName = null;
		String cliSurname = null;
		String cliEmail = null;
				
		boolean isNew = false;
		
		if (strInvClientId==null){
			cliName = arg0.getParameter("cliName");
			cliSurname = arg0.getParameter("cliSurname");
			cliEmail = arg0.getParameter("cliEmail").toLowerCase();
			// Es nuevo seguro. Puede que no venga email (luego es nuevo); pero si viene hemos comprobado seguro que no está repetido. 
			isNew = true;
		}
		
		String strInvTask = arg0.getParameter("bilTaskId");
		String strInvType = arg0.getParameter("bilTypeId");
		String strInvCalendar = arg0.getParameter("bilCalendarId");
		String strInvRate = arg0.getParameter("bilRateId");
		
		InvoiceDTO invoice = null;
		
		if (validateNew(arg0, isNew, strInvClientId, cliName, invTime, strInvTask, strInvRate)){
			
			Long localId = new Long(arg0.getParameter("localId"));
			LocalDTO local = localManager.getById(localId);
						
			TimeZone calendarTimeZone = TimeZone.getTimeZone(local.getLocWhere().getWheTimeZone());
			calendarGreg = new GregorianCalendar();
			calendarGreg.set(Calendar.MILLISECOND, 0);
			Date invIssueTime = calendarGreg.getTime();
			// Calculamos el desplazamiento de la zona horaria desde UTC
			invIssueTime =  new Date(invIssueTime.getTime() + calendarTimeZone.getOffset(invIssueTime.getTime()));
			
			String invDesc = arg0.getParameter("invDesc");
			
			ClientDTO invClient = null;
			if (!isNew){ // Si no es nuevo, buscamos por el id
				invClient = clientManager.getById(new Long(strInvClientId));	
			} else {
				invClient = new ClientDTO();
				invClient.setEnabled(1);
				invClient.setResFirId(local.getResFirId());
				if (cliEmail != null && cliEmail.length()>0){
					invClient.setWhoEmail(cliEmail);
				}
				invClient.setWhoName(cliName);
				invClient.setWhoSurname(cliSurname);
				invClient.setCliCreationTime(invIssueTime);
				invClient = clientManager.create(invClient);
			}
			
			// Rates
			Float invRate = new Float(0);
			String[] rates = strInvRate.split(",");
			for (String str : rates) {
				invRate += new Float(str);
			}
							
			invoice = new InvoiceDTO();
				
			invoice.setEnabled(1);
			invoice.setInvLocalId(localId);
			invoice.setInvIssueTime(invIssueTime);
			invoice.setInvDesc(invDesc);
			invoice.setInvClient(invClient);
			invoice.setInvRate(invRate);
			invoice.setInvTime(invTime);
			
			invoice = invoiceManager.create(invoice);
			
			String ICS = arg0.getParameter("ICS");
			if (ICS !=null){
				List<EventDTO> listEventLocal = eventManager.getEventByICS(ICS);
				for (EventDTO event : listEventLocal) {
					event.setEveConsumed(1);
					eventManager.update(event);
				}
			}	
			
			String[] tasks = strInvTask.split(",");
			String[] types = strInvType.split(",");
			String[] calendars = strInvCalendar.split(",");
			int i = 0;
			for (String strTask : tasks) {
				
				Long bilTaskId = new Long(strTask);
				Integer bilType = new Integer(types[i]);
				Long bilCalendarId = new Long(calendars[i]);
				Float bilRate = new Float(rates[i]);
				
				newBilled (local, invoice, bilRate, bilCalendarId, bilTaskId, bilType);
				
				i++;
			}

	
		}
		return invoice;
	}
	
	
	protected void newBilled (LocalDTO local, InvoiceDTO bilInvoice, Float bilRate, Long bilCalendarId, Long bilTaskId, Integer bilType)	throws Exception {
		
		LocalTaskDTO bilTask = null;
		ProductDTO bilProduct = null;

		if (bilType==0){
			bilTask = localTaskManager.getById(bilTaskId);
		} else {
			bilProduct = productManager.getById(bilTaskId);
		}
		
		BilledDTO billed = new BilledDTO();
		
		billed.setEnabled(1);
		billed.setBilCalendarId(bilCalendarId);
		billed.setBilClient(bilInvoice.getInvClient());
		billed.setBilInvoiceId(bilInvoice.getId());
		billed.setBilLocalTask(bilTask);
		billed.setBilProduct(bilProduct);
		billed.setBilRate(bilRate);
		billed.setBilTime(bilInvoice.getInvTime());
				
		billed = billedManager.create(billed);
	
	}

	@RequestMapping("/operator/get")
	protected @ResponseBody
	InvoiceDTO get(@RequestParam("id") Long id) throws Exception {

		InvoiceDTO invoice = invoiceManager.getById(id);	
					
		return invoice;
	}
	
	@RequestMapping("/operator/listByDiary")
	protected @ResponseBody
	List<InvoiceDTO> listByDiary(HttpServletRequest arg0, @RequestParam("localId") Long localId, @RequestParam("selectedDate") String selectedDate) throws Exception {

		Locale locale = RequestContextUtils.getLocale(arg0);
		
		List<InvoiceDTO> listInvoice = invoiceManager.getInvoiceByWeek(localId, selectedDate);
		
		for (InvoiceDTO invoice : listInvoice) {
			List<BilledDTO> listBilled = billedManager.getBilledByInvoice(invoice.getId(), locale.getLanguage());
			String strBilleds = "";
			for (BilledDTO billed : listBilled) {
				if (strBilleds!=""){
					strBilleds += " , ";
				}			
				if (billed.getBilLocalTask()!=null){
					strBilleds += billed.getBilLocalTask().getLotName();
				} else {	 
					strBilleds += billed.getBilProduct().getProName();
				}
			}
			invoice.setInvBilleds(strBilleds);
		}

		return listInvoice;
	}
	
	@RequestMapping("/operator/listByClientAgo")
	protected @ResponseBody
	List<InvoiceDTO> listByClientAgo( HttpServletRequest arg0, @RequestParam("localId") Long localId, @RequestParam("id") Long id) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		List<InvoiceDTO> listInvoice = invoiceManager.getInvoiceByClientAgo(localId,id);

		for (InvoiceDTO invoice : listInvoice) {
			List<BilledDTO> listBilled = billedManager.getBilledByInvoice(invoice.getId(), locale.getLanguage());
			String strBilleds = "";
			for (BilledDTO billed : listBilled) {
				if (strBilleds!=""){
					strBilleds += " , ";
				}			
				if (billed.getBilLocalTask()!=null){
					strBilleds += billed.getBilLocalTask().getLotName();
				} else {	 
					strBilleds += billed.getBilProduct().getProName();
				}
			}
			invoice.setInvBilleds(strBilleds);
		}

		return listInvoice;
	}

	public void setMessageSourceApp(MessageSource messageSourceApp) {
		this.messageSourceApp = messageSourceApp;
	}

	public void setLocalDAO(ILocalManager iLocalManager) {
		this.localManager = iLocalManager;
	}

	public void setClientDAO(IClientManager iClientManager) {
		this.clientManager = iClientManager;
	}

	public void setInvoiceDAO(IInvoiceManager iInvoiceManager) {
		this.invoiceManager = iInvoiceManager;
	}

	public void setLocalTaskDAO(ILocalTaskManager iLocalTaskManager) {
		this.localTaskManager = iLocalTaskManager;
	}

	public void setProductDAO(IProductManager iProductManager) {
		this.productManager = iProductManager;
	}

	public void setEventDAO(IEventManager iEventManager) {
		this.eventManager = iEventManager;
	}

	public void setBilledDAO(IBilledManager iBilledManager) {
		this.billedManager = iBilledManager;
	}

	public void setCalendarDAO(ICalendarManager iCalendarManager) {
		this.calendarManager = iCalendarManager;
	}
	
	
	
}
