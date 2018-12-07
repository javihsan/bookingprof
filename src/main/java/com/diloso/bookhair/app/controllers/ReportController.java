package com.diloso.bookhair.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.mail.SendFailedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dao.BilledDAO;
import com.diloso.bookhair.app.negocio.dao.CalendarDAO;
import com.diloso.bookhair.app.negocio.dao.EventDAO;
import com.diloso.bookhair.app.negocio.dao.FirmDAO;
import com.diloso.bookhair.app.negocio.dao.LocalDAO;
import com.diloso.bookhair.app.negocio.dao.LocalTaskDAO;
import com.diloso.bookhair.app.negocio.dao.ProductDAO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.ProductDTO;
import com.diloso.bookhair.app.negocio.dto.generator.NotifCalendarDTO;
import com.diloso.bookhair.app.negocio.dto.report.ReportDTO;

@Controller
@RequestMapping(value={"/*/report", "/report"})
public class ReportController {

	protected String TYPE_STRING = "string";
	protected String TYPE_NUMBER = "number";
	
	@Autowired
	protected MessageSource messageSourceApp;
	
	@Autowired
	protected LocalDAO localDAO;
	
	@Autowired
	protected CalendarDAO calendarDAO;
	
	@Autowired
	protected FirmDAO firmDAO;
	
	@Autowired
	protected EventDAO eventDAO;
	
	@Autowired
	protected BilledDAO billedDAO;
	
	@Autowired
	protected LocalTaskDAO localTaskDAO;
	
	@Autowired
	protected ProductDAO productDAO;
	
	@RequestMapping("")
	public ModelAndView init(HttpServletRequest arg0,
			HttpServletResponse arg1) throws Exception {

		ModelAndView mav = new ModelAndView("/app/report");
		return mav;

	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/manager/send")
	@ResponseStatus(HttpStatus.OK)
	protected void send (HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		Long localId = new Long(arg0.getParameter("localId"));
		
		// Propiedades de local
		LocalDTO local = localDAO.getById(localId);
		
		FirmDTO firm = firmDAO.getById(local.getResFirId());
		
		String selectedDateStart = arg0.getParameter("selectedDateStart");
		String selectedDateEnd = arg0.getParameter("selectedDateEnd");
		
		String content = arg0.getParameter("content");
		
		NotifCalendarDTO modelNot = new NotifCalendarDTO();
		modelNot.setLocale(locale);
		
		String mail = firm.getFirRespon().getWhoEmail();
		modelNot.setNocDesEmail(mail);
		modelNot.setNocDesEmailCC(local.getLocRespon().getWhoEmail());
	
		String title = messageSourceApp.getMessage("report.mail.title", null, locale);
		title += " " + firm.getFirName() + " " + selectedDateStart + " - " + selectedDateEnd;
		
		modelNot.setNocSummary(title);
	    modelNot.setNocContent(content);
	    
		MailController mailController = new MailController();
		int retrys = 0;
		while (retrys<MailController.MAX_RETRYS_EMAIL) {
			try {
				TimeUnit.SECONDS.sleep(1);
				mailController.report(arg0, arg1, modelNot);
				retrys = MailController.MAX_RETRYS_EMAIL;
			} catch (SendFailedException e) {
				if (++retrys == MailController.MAX_RETRYS_EMAIL) {
					throw e;
				}
			}
		}
		
	}
	
	@RequestMapping("/manager/sales/all")
	protected @ResponseBody
	ReportDTO salesAll(HttpServletRequest arg0, HttpServletResponse arg1, @RequestParam("localId") String localId, @RequestParam("selectedDateStart") String selectedDateStart, @RequestParam("selectedDateEnd") String selectedDateEnd) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
	
		LocalDTO local = localDAO.getById(new Long(localId));
		
		List<CalendarDTO> listCalendar = calendarDAO.getCalendarAdmin(local.getId());

		float salesFin = 0;
		float salesTask = 0;
		float salesProduct = 0;
				
		for (CalendarDTO calendar : listCalendar) {
			
			salesFin += billedDAO.getBilledSales(calendar,selectedDateStart,selectedDateEnd);
			salesTask += billedDAO.getBilledSalesTask(calendar,selectedDateStart,selectedDateEnd,null);
			salesProduct += billedDAO.getBilledSalesProduct(calendar,selectedDateStart,selectedDateEnd,null);
		}	
		
		ReportDTO reportDTO = new ReportDTO();
		
		reportDTO.addColumn("", TYPE_STRING, "");
		reportDTO.addColumn(messageSourceApp.getMessage("report.sales.sales", null, locale), TYPE_NUMBER, messageSourceApp.getMessage("report.sales.sales", null, locale));
			
		List<Object> listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.total", null, locale));
		listColumnValues.add(salesFin);
		reportDTO.addRow(listColumnValues);
		
		listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.sales.tasks", null, locale));
		listColumnValues.add(salesTask);
		reportDTO.addRow(listColumnValues);
		
		listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.sales.products", null, locale));
		listColumnValues.add(salesProduct);
		reportDTO.addRow(listColumnValues);
		
		return reportDTO;
	}

	
	@RequestMapping("/manager/sales/task")
	protected @ResponseBody
	ReportDTO salesTask(HttpServletRequest arg0,
			HttpServletResponse arg1,  @RequestParam("localId") String localId, @RequestParam("selectedDateStart") String selectedDateStart, @RequestParam("selectedDateEnd") String selectedDateEnd) throws Exception {

		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		LocalDTO local = localDAO.getById(new Long(localId));
		
		List<CalendarDTO> listCalendar = calendarDAO.getCalendarAdmin(local.getId());
		
		ReportDTO reportDTO = new ReportDTO();
		reportDTO.addColumn(messageSourceApp.getMessage("report.sales.task", null, locale), TYPE_STRING, messageSourceApp.getMessage("report.sales.task", null, locale));
		reportDTO.addColumn(messageSourceApp.getMessage("report.sales.sales", null, locale), TYPE_NUMBER, messageSourceApp.getMessage("report.sales.sales", null, locale));
		
		float sales = 0;
		
		for (CalendarDTO calendar : listCalendar) {
			sales += billedDAO.getBilledSalesTask(calendar,selectedDateStart,selectedDateEnd,null);
		}	
		
		List<Object> listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.total", null, locale));
		listColumnValues.add(sales);
		reportDTO.addRow(listColumnValues);
		
		List<LocalTaskDTO> listLocalTask = localTaskDAO.getLocalTaskSimpleAdmin(new Long(localId), locale.getLanguage());	
		for (LocalTaskDTO localTask: listLocalTask) {
			if (localTask.getLotTaskRate()>0){
				sales = 0;
				for (CalendarDTO calendar : listCalendar) {
					sales += billedDAO.getBilledSalesTask(calendar,selectedDateStart,selectedDateEnd,localTask.getId());
				}
				if (localTask.getEnabled()==0){
					if (sales==0){
						localTask.setLotName("");
					} else {
						localTask.setLotName(localTask.getLotName() + "*");
					}	
				}
				if (!localTask.getLotName().equals("")){
					listColumnValues = new ArrayList<Object>();
				
					listColumnValues.add(localTask.getLotName());
					listColumnValues.add(sales);
					reportDTO.addRow(listColumnValues);
				}	
			}	
		}	
		
		return reportDTO;
	}
	
	@RequestMapping("/manager/sales/product")
	protected @ResponseBody
	ReportDTO salesProduct(HttpServletRequest arg0,
			HttpServletResponse arg1,  @RequestParam("localId") String localId, @RequestParam("selectedDateStart") String selectedDateStart, @RequestParam("selectedDateEnd") String selectedDateEnd) throws Exception {

		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		LocalDTO local = localDAO.getById(new Long(localId));
		
		List<CalendarDTO> listCalendar = calendarDAO.getCalendarAdmin(local.getId());
		
		ReportDTO reportDTO = new ReportDTO();
		reportDTO.addColumn(messageSourceApp.getMessage("report.sales.product", null, locale), TYPE_STRING, messageSourceApp.getMessage("report.sales.product", null, locale));
		reportDTO.addColumn(messageSourceApp.getMessage("report.sales.sales", null, locale), TYPE_NUMBER, messageSourceApp.getMessage("report.sales.sales", null, locale));
		
		float sales = 0;
		
		for (CalendarDTO calendar : listCalendar) {
			sales += billedDAO.getBilledSalesProduct(calendar,selectedDateStart,selectedDateEnd,null);
		}	
		
		List<Object> listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.total", null, locale));
		listColumnValues.add(sales);
		reportDTO.addRow(listColumnValues);
		
		List<ProductDTO> listProduct = productDAO.getProductAdminByLang(new Long(localId), locale.getLanguage());	
		for (ProductDTO product: listProduct) {
			sales = 0;
			for (CalendarDTO calendar : listCalendar) {
				sales += billedDAO.getBilledSalesProduct(calendar,selectedDateStart,selectedDateEnd,product.getId());
			}
			if (product.getEnabled()==0){
				if (sales==0){
					product.setProName("");
				} else {
					product.setProName(product.getProName() + "*");
				}	
			}
			if (!product.getProName().equals("")){
				listColumnValues = new ArrayList<Object>();
			
				listColumnValues.add(product.getProName());
				listColumnValues.add(sales);
				reportDTO.addRow(listColumnValues);
			}	
			
		}	
		
		return reportDTO;
	}
	
	
	@RequestMapping("/manager/apo/all")
	protected @ResponseBody
	ReportDTO apoAll(HttpServletRequest arg0, HttpServletResponse arg1, @RequestParam("localId") String localId, @RequestParam("selectedDateStart") String selectedDateStart, @RequestParam("selectedDateEnd") String selectedDateEnd) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
	
		LocalDTO local = localDAO.getById(new Long(localId));
		
		List<CalendarDTO> listCalendar = calendarDAO.getCalendarAdmin(local.getId());
		
		int numEventsFin = 0;
		//int numEventsReject = 0;
		int numEventsUnfin = 0;
		int numEventsTotal = 0;
		
		for (CalendarDTO calendar : listCalendar) {
			numEventsFin += eventDAO.getEventNumber(calendar,selectedDateStart,selectedDateEnd,true);
			//numEventsReject += eventDAO.getEventNumber(calendar,selectedDateStart,selectedDateEnd,true);
			numEventsUnfin += eventDAO.getEventNumber(calendar,selectedDateStart,selectedDateEnd,false);
			numEventsTotal += eventDAO.getEventNumber(calendar,selectedDateStart,selectedDateEnd,null);
		}	
		
		
		ReportDTO reportDTO = new ReportDTO();
		
		reportDTO.addColumn(messageSourceApp.getMessage("report.apo.finished", null, locale), TYPE_STRING, messageSourceApp.getMessage("report.apo.finished", null, locale));
		reportDTO.addColumn(messageSourceApp.getMessage("report.apo.apos", null, locale), TYPE_NUMBER, messageSourceApp.getMessage("report.apo.apos", null, locale));
			
		List<Object> listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.total", null, locale));
		listColumnValues.add(numEventsTotal);
		reportDTO.addRow(listColumnValues);
			
		listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.apo.finished", null, locale));
		listColumnValues.add(numEventsFin);
		reportDTO.addRow(listColumnValues);
		
		listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.apo.unfinished", null, locale));
		listColumnValues.add(numEventsUnfin);
		reportDTO.addRow(listColumnValues);
		
		return reportDTO;
	}
	
	@RequestMapping("/manager/apo/booking")
	protected @ResponseBody
	ReportDTO apoBooking(HttpServletRequest arg0, HttpServletResponse arg1, @RequestParam("localId") String localId, @RequestParam("selectedDateStart") String selectedDateStart, @RequestParam("selectedDateEnd") String selectedDateEnd) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
	
		LocalDTO local = localDAO.getById(new Long(localId));
		
		List<CalendarDTO> listCalendar = calendarDAO.getCalendarAdmin(local.getId());
		
		int numEventsClient = 0;
		int numEventsProf = 0;
		int numEventsTotal = 0;
		
		for (CalendarDTO calendar : listCalendar) {
			numEventsClient += eventDAO.getEventNumberBooking(calendar,selectedDateStart,selectedDateEnd,0);
			numEventsProf += eventDAO.getEventNumberBooking(calendar,selectedDateStart,selectedDateEnd,1);
			numEventsTotal += eventDAO.getEventNumberBooking(calendar,selectedDateStart,selectedDateEnd,null);
		}	
		
		
		ReportDTO reportDTO = new ReportDTO();
		
		reportDTO.addColumn(messageSourceApp.getMessage("report.apo.booking", null, locale), TYPE_STRING, messageSourceApp.getMessage("report.apo.booking", null, locale));
		reportDTO.addColumn(messageSourceApp.getMessage("report.apo.apos", null, locale), TYPE_NUMBER, messageSourceApp.getMessage("report.apo.apos", null, locale));
			
		List<Object> listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.total", null, locale));
		listColumnValues.add(numEventsTotal);
		reportDTO.addRow(listColumnValues);
			
		listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.apo.client", null, locale));
		listColumnValues.add(numEventsClient);
		reportDTO.addRow(listColumnValues);
		
		listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.apo.prof", null, locale));
		listColumnValues.add(numEventsProf);
		reportDTO.addRow(listColumnValues);
		
		return reportDTO;
	}
	
	@RequestMapping("/manager/apo/task")
	private @ResponseBody
	ReportDTO apoTask(HttpServletRequest arg0,
			HttpServletResponse arg1,  @RequestParam("localId") String localId, @RequestParam("selectedDateStart") String selectedDateStart, @RequestParam("selectedDateEnd") String selectedDateEnd) throws Exception {

		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		LocalDTO local = localDAO.getById(new Long(localId));
		
		List<CalendarDTO> listCalendar = calendarDAO.getCalendarAdmin(local.getId());
		
		ReportDTO reportDTO = new ReportDTO();
		reportDTO.addColumn(messageSourceApp.getMessage("report.sales.tasks", null, locale), TYPE_STRING, messageSourceApp.getMessage("report.sales.tasks", null, locale));
		reportDTO.addColumn(messageSourceApp.getMessage("report.apo.worked", null, locale), TYPE_NUMBER, messageSourceApp.getMessage("report.apo.worked", null, locale));
		
		int numEvents = 0;	
		
		for (CalendarDTO calendar : listCalendar) {
			numEvents += eventDAO.getEventNumberTask(calendar,selectedDateStart,selectedDateEnd,null,true);		
		}	
		
		List<Object> listColumnValues = new ArrayList<Object>();
		listColumnValues.add(messageSourceApp.getMessage("report.total", null, locale));
		listColumnValues.add(numEvents);
		reportDTO.addRow(listColumnValues);
		
		List<LocalTaskDTO> listLocalTask = localTaskDAO.getLocalTaskSimpleAdmin(new Long(localId), locale.getLanguage());	
		for (LocalTaskDTO localTask: listLocalTask) {
			if (localTask.getLotTaskDuration()>0){
				numEvents = 0;	
				for (CalendarDTO calendar : listCalendar) {
					numEvents += eventDAO.getEventNumberTask(calendar,selectedDateStart,selectedDateEnd,localTask.getId(),true);		
				}
				if (localTask.getEnabled()==0){
					if (numEvents==0){
						localTask.setLotName("");
					} else {
						localTask.setLotName(localTask.getLotName() + "*");
					}	
				}
				if (!localTask.getLotName().equals("")){
					listColumnValues = new ArrayList<Object>();
				
					listColumnValues.add(localTask.getLotName());
					listColumnValues.add(numEvents);
					reportDTO.addRow(listColumnValues);
				}
			}	
		}	
		
		return reportDTO;
	}
	
}


