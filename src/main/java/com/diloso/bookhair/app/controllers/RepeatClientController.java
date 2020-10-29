package com.diloso.bookhair.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.MessageSource;
import org.springframework.dao.UncategorizedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dao.CalendarDAO;
import com.diloso.bookhair.app.negocio.dao.ClientDAO;
import com.diloso.bookhair.app.negocio.dao.FirmDAO;
import com.diloso.bookhair.app.negocio.dao.LocalDAO;
import com.diloso.bookhair.app.negocio.dao.LocalTaskDAO;
import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.RepeatClientDAO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatClientDTO;
import com.diloso.bookhair.app.negocio.dto.generator.NotifCalendarDTO;
import com.diloso.bookhair.app.negocio.utils.templates.Generator;

@Controller
@RequestMapping(value={"/*/repeatClient", "/repeatClient"})
public class RepeatClientController {
	
	protected static final Logger log = Logger.getLogger(RepeatClientController.class.getName());
	
	//@Autowired
	protected MessageSource messageSourceApp;
	
	//@Autowired
	protected Generator generatorVelocity;
	
	//@Autowired
	protected LocalDAO localDAO;
	
	//@Autowired
	protected CalendarDAO calendarDAO;
	
	//@Autowired
	protected RepeatClientDAO repeatClientDAO;
	
	//@Autowired
	protected LocalTaskDAO localTaskDAO;
	
	//@Autowired
	protected ClientDAO clientDAO;
	
	//@Autowired
	protected FirmDAO firmDAO;
	
	//@Autowired
	protected MultiTextDAO multiTextDAO;

	//@Autowired
	protected CalendarController calController;
	
	@ExceptionHandler(UncategorizedDataAccessException.class)
	@ResponseStatus(value=HttpStatus.CONFLICT)
	protected void error(Exception t, HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.CONFLICT.value(), t.getMessage());
	}
	
	/* La fecha es correcta
	 *    Minima fecha: momento actual + tiempo de reserva de repeatClientss
	 *    Maxima fecha: dia de hoy a las 0 horas + tiempo de apertura de agenda del local
	 * Exista espacio libre para las tareas
	 * El cliente no haya reservado más de local.getLocNumUsuDays()
	 * El nombre de cliente es obligatorio si es nuevo 
	 * El email  de cliente es obligatorio si es nuevo y si no admin
	 * El telf   de cliente es obligatorio si es nuevo strCliId  y si no admin
	 * El mail está repetido si es nuevo
	*/
	protected boolean validateNew(HttpServletRequest arg0, boolean isNew, String cliName, String cliEmail, String cliTelf, Date time, LocalDTO local, List<List<LocalTaskDTO>> listLocalTasks, boolean admin) throws UncategorizedDataAccessException {
		boolean res = true;
		String message = "";
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		if(isNew && (cliName == null || cliName.length()==0)){
			message = messageSourceApp.getMessage("form.error.client.nameReq", null, locale);
			res = false;
		} else if(isNew && !admin && (cliEmail == null|| cliEmail.length()==0)){
			message = messageSourceApp.getMessage("form.error.client.emailReq", null, locale);
			res = false;
		} else if(isNew && admin && existsEmail(local.getResFirId(),cliEmail)){
			message = messageSourceApp.getMessage("form.error.client.emailRep", null, locale);
			res = false;
		} else if(isNew && !admin && (cliTelf == null|| cliTelf.length()==0)){
			message = messageSourceApp.getMessage("form.error.client.telfReq", null, locale);
			res = false;
		} else if (!calController.validateDate(time, local.getLocTimeRestricted(), local.getLocOpenDays(),admin,local.getLocWhere().getWheTimeZone())){
			message = messageSourceApp.getMessage("form.error.client.dateInv", null, locale);
			res = false;
		} else {
			ClientDTO repClient = null;
			if (!admin){ // Si no es admin, buscamos por el email, que es obligatorio
				repClient = clientDAO.getByEmail(local.getResFirId(),cliEmail);
			} 
			if (!isNew && !admin && (getRepeatClientByClientAgo(local.getId(), repClient.getId(), local.getLocNumUsuDays()).size()>0) ){
				message = messageSourceApp.getMessage("form.error.client.bookingOne1", null, locale)+" "+local.getLocNumUsuDays()+" "+messageSourceApp.getMessage("form.error.client.bookingOne2", null, locale);
				res = false;
			} else {
				if (!calController.existsSpaceTasks(listLocalTasks, null, time, local.getLocApoDuration(), null, admin)){	
					message = messageSourceApp.getMessage("form.error.client.apoBooked", null, locale);
					res = false;
				} 
			}
		}
		
		if (!res){
			throw new ErrorService(message, null);
		}
		return res;
	}
	
	protected boolean existsEmail(Long resFirId, String cliEmail){
		if (cliEmail != null && cliEmail.length()>0){
			ClientDTO repClient = clientDAO.getByEmail(resFirId,cliEmail);
			if (repClient!=null){
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/operator/new")
	@ResponseStatus(HttpStatus.OK)
	protected void newObjectAdmin(HttpServletRequest arg0, HttpServletResponse arg1, String localId, @RequestParam("selectedCalendars") String selectedCalendars)
			throws Exception {
		List<Long> listCalendarCandidate = calController.getCalendarsId(selectedCalendars);
		String selectedTasks = arg0.getParameter("selectedTasks");
		List<List<LocalTaskDTO>> listLocalTasks = calController.getListLocalTasks(selectedTasks);
		newObject (arg0,arg1,localId,listLocalTasks,listCalendarCandidate,true);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/new")
	@ResponseStatus(HttpStatus.OK)
	protected void newObject(HttpServletRequest arg0, HttpServletResponse arg1, String localId, @RequestParam("selectedCalendars") String selectedCalendars)
			throws Exception {
		List<Long> listCalendarCandidate = calController.getCalendarsId(selectedCalendars);
		String selectedTasks = arg0.getParameter("selectedTasks");
		List<List<LocalTaskDTO>> listLocalTasks = calController.getListLocalTasks(selectedTasks);
		newObject (arg0,arg1,localId,listLocalTasks,listCalendarCandidate,false);
	}
	
	protected void newObject(HttpServletRequest arg0, HttpServletResponse arg1, String localId, List<List<LocalTaskDTO>> listLocalTasks, List<Long> listCalendarCandidate, boolean admin)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		// Propiedades de local
		LocalDTO local = localDAO.getById(new Long(localId));
		
		Long lngRepStartTime = new Long(arg0.getParameter("repStartTime"));
		Date repStartTime = new Date(lngRepStartTime);
		
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.setTime(repStartTime);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		repStartTime = calendarGreg.getTime();
		
		String selectedDate = calendarGreg.get(Calendar.YEAR)
				+ CalendarController.CHAR_SEP_DATE
				+ (calendarGreg.get(Calendar.MONTH) +1)
				+ CalendarController.CHAR_SEP_DATE
				+ calendarGreg.get(Calendar.DAY_OF_MONTH);
		
		String strCliId = arg0.getParameter("cliId");
		String cliName = null;
		String cliEmail = null;
		String cliTelf = null;
		
		boolean isNew = false;
		
		if (strCliId==null){
			cliName = arg0.getParameter("cliName");
			cliEmail = arg0.getParameter("cliEmail").toLowerCase();
			cliTelf = arg0.getParameter("cliTelf");
			if (admin){ // Es nuevo seguro. Puede que no venga email (luego es nuevo); pero si viene hemos comprobado seguro que no está repetido. 
				isNew = true;
			} else if (!existsEmail(local.getResFirId(),cliEmail)){ // Viene el mail seguro, comprobamos que no exista 
				isNew = true;
			}
		}
		
		if (validateNew(arg0, isNew, cliName, cliEmail, cliTelf, repStartTime, local, listLocalTasks, admin)){ 
		
			RepeatClientDTO repeatClientOrig = new RepeatClientDTO();
			
			TimeZone calendarTimeZone = TimeZone.getTimeZone(local.getLocWhere().getWheTimeZone());
			calendarGreg = new GregorianCalendar();
			calendarGreg.set(Calendar.MILLISECOND, 0);
			Date repBookingTime = calendarGreg.getTime();
			// Calculamos el desplazamiento de la zona horaria desde UTC
			repBookingTime =  new Date(repBookingTime.getTime() + calendarTimeZone.getOffset(repBookingTime.getTime()));
				
			// Propiedades de cliente
			ClientDTO repClient = null;
			if (!admin){ // Si no es admin, buscamos por el email, que es obligatorio
				repClient = clientDAO.getByEmail(local.getResFirId(),cliEmail);
			} else { // Es admin
				if (!isNew){ // Si no es nuevo, buscamos por el id
					repClient = clientDAO.getById(new Long(strCliId));
				}
			}
			if (isNew) {// Es nuevo
				repClient = new ClientDTO();
				repClient.setEnabled(1);
				repClient.setResFirId(local.getResFirId());
				if (cliEmail != null && cliEmail.length()>0){
					repClient.setWhoEmail(cliEmail);
				}
				repClient.setWhoName(cliName);
				if (cliTelf.startsWith("6")){
					repClient.setWhoTelf1(cliTelf);
				} else {
					repClient.setWhoTelf2(cliTelf);	
				}
				repClient.setCliCreationTime(repBookingTime);
				repClient = clientDAO.create(repClient);
			
			} else if (!admin){
				// Puede que hayan puesto un nombre y telf distinto del guardado en la BBDD
				repClient.setWhoName(cliName);
				if (cliTelf.startsWith("6")){
					repClient.setWhoTelf1(cliTelf);
				} else {
					repClient.setWhoTelf2(cliTelf);	
				}
				repClient = clientDAO.update(repClient);
			}
			
			// Propiedades de booking
			int repBooking = 0;
			if (admin){ // Si hay usuario manager entonces el ha reservado
				repBooking = 1;
			}
			
			repeatClientOrig.setEnabled(1);
			repeatClientOrig.setRecClient(repClient);
			
			repeatClientOrig.setRecBooking(repBooking);
			repeatClientOrig.setRecBookingTime(repBookingTime);
			
			NotifCalendarDTO modelNot = null;
			int indx = 0;
			Date repStartTimeDS = null;
			Date repEndTimeDS = null;
			calendarGreg.setTime(repStartTime);

			List<RepeatClientDTO> listRepeatClientAsign = new ArrayList<RepeatClientDTO>();
			//for (Map<String,Object> calendarOpen : listCalendarOpen) {
				//listRepeatClientAsign.addAll((List<RepeatClientDTO>)calendarOpen.get(CalendarController.EVENTS_APO));
			//}
			String tasksMail = messageSourceApp.getMessage("mail.invite.tasks1", null, locale)+" "+ listRepeatClientAsign.size() + " "+messageSourceApp.getMessage("mail.invite.tasks2", null, locale)+":";
			MultiTextDTO multiTextKey = null;
			for (RepeatClientDTO repeatClientAux : listRepeatClientAsign) {
				repStartTimeDS = repeatClientAux.getRecRepeat().getEveStartTime();
				repEndTimeDS = repeatClientAux.getRecRepeat().getEveEndTime();
				if (indx==0){
					modelNot = new NotifCalendarDTO();
					modelNot.setLocale(locale);
					
					modelNot.setNocDtStart(repStartTimeDS);
					
					String nameApp = messageSourceApp.getMessage("mail.appName", null, locale);
					modelNot.setNocOrgName(nameApp);
					// Solo si no es Matriz
//					if (repeatAux.getRepMartix()!=null){
//						modelNot.setNocUID(repeatAux.getRepMartix()+"_"+repBookingTime.getTime()+ "@"+ nameApp);
//					}	
				} 
				multiTextKey = multiTextDAO.getByLanCodeAndKey(locale.getLanguage(),
						repeatClientAux.getRecRepeat().getEveLocalTask().getLotNameMulti());
				tasksMail += " 1 " + multiTextKey.getMulText();
				
				RepeatClientDTO repeatClient = new RepeatClientDTO();
				PropertyUtils.copyProperties(repeatClient, repeatClientOrig);
				
				repeatClient = repeatClientDAO.create(repeatClient);
				
				indx ++;
				
			}
			
			// Envio de email
			if (repClient.getWhoEmail() != null && repClient.getWhoEmail().length()>0){
				
				modelNot.setNocDtEnd(repEndTimeDS);
				
				modelNot.setNocDesEmail(repClient.getWhoEmail());
				modelNot.setNocDesName(repClient.getWhoName());
			    modelNot.setNocLocation(local.getLocLocation());
			    modelNot.setNocDtCreated(repBookingTime);
			    modelNot.setNocDtStamp(repBookingTime);
			    modelNot.setNocTasks(tasksMail);
		
				FirmDTO firm = firmDAO.getById(local.getResFirId());
				String title = messageSourceApp.getMessage("mail.invite.title", null, locale);
				title += " " + firm.getFirName();

				String content = "<div>" + title + "</div>" + messageSourceApp.getMessage("mail.invite.text", null, locale);
				content = generatorVelocity.generateContent(modelNot, content).toString();
								
				modelNot.setNocSummary(title);
			    modelNot.setNocContent(content);
				
			    // Si está reservando el cliente y el local quiere recibir copia
			    if (!admin && local.getLocMailBookign()!=null && local.getLocMailBookign()==1){
			    	modelNot.setNocDesEmailCC(local.getLocRespon().getWhoEmail());
			    }
			    
				new MailController().invite(arg0, arg1, modelNot);
			}
			
		}
	
	}
	
					
	protected List<RepeatClientDTO> getRepeatClientByClientAgo(Long localId, Long clientId, int numDays ) {

		List<CalendarDTO> listCalendar = calendarDAO.getCalendar(localId);
		List<RepeatClientDTO> listRepeatClientAux = null;
		List<RepeatClientDTO> listRepeatClientLocal = new ArrayList<RepeatClientDTO>();
		for (CalendarDTO calendar : listCalendar) {
			if (numDays!=-1){
				// Calculamos el desplazamiento de la zona horaria desde UTC
				LocalDTO local = localDAO.getById(new Long(localId));
				TimeZone calendarTimeZone = TimeZone.getTimeZone(local.getLocWhere().getWheTimeZone());
				Date minDate =  new Date();
				minDate =  new Date(minDate.getTime() + calendarTimeZone.getOffset(minDate.getTime()));
				listRepeatClientAux = repeatClientDAO.getRepeatClientByClientAgo(calendar, clientId, minDate, numDays);
			} else {
				listRepeatClientAux = repeatClientDAO.getRepeatClientByClientAgo(calendar, clientId, null, -1);
			}
			// Añadimos los repeatClientss de este puesto a los del local
			for (RepeatClientDTO repeatClient : listRepeatClientAux) {
				listRepeatClientLocal.add(repeatClient);
			}
		}

		return listRepeatClientLocal;
	}
	/*
	@RequestMapping("/operator/listByClientAgo")
	private @ResponseBody
	List<RepeatClientDTO> getRepeatClientByClientAgo(@RequestParam("localId") String localId, HttpServletRequest arg0, @RequestParam("id") Long id) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);

		List<RepeatClientDTO> listRepeatClientLocal = getRepeatClientByClientAgo(new Long(localId), id, -1);
		List<RepeatClientDTO> listRepeatClientClient = new ArrayList<RepeatClientDTO>();
		RepeatClientDTO repeatClientCandidate = null;
		MultiTextDTO multiTextKey = null;
		String strTasks = "";
		String ics = "";
		for (RepeatClientDTO repeatClient : listRepeatClientLocal) {
			if (!ics.equals(repeatClient.getRepICS()) && repeatClientCandidate!=null){
				listRepeatClientClient.add(repeatClientCandidate);
				strTasks = "";
			}
			multiTextKey = multiTextDAO.getByLanCodeAndKey(locale.getLanguage(),repeatClient.getRepLocalTask().getLotNameMulti());
			if (strTasks!=""){
				strTasks += " , " + multiTextKey.getMulText();
			} else {
				strTasks = multiTextKey.getMulText();	
			}
			repeatClient.getEveLocalTask().setLotName(strTasks);
			repeatClientCandidate = repeatClient;
			ics = repeatClient.getRepICS();
		}
		if (repeatClientCandidate!=null){
			listRepeatClientClient.add(repeatClientCandidate);
		}
		return listRepeatClientClient;
	}*/
	
	
	
	@RequestMapping(method = RequestMethod.PUT, value = "/operator/cancel")
	@ResponseStatus(HttpStatus.OK)
	public void cancel(HttpServletRequest arg0, HttpServletResponse arg1, @RequestParam("localId") String localId, @RequestParam("id") Long id, @RequestParam("send") String send, @RequestParam("text") String text)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);

		LocalDTO local = localDAO.getById(new Long(localId));
		
		RepeatClientDTO repeatClient = repeatClientDAO.getById(id);
		if (repeatClient!=null){
			repeatClient.setEnabled(0);
			repeatClientDAO.update(repeatClient);
			
			log.info("RepeatClient cancelado : "+repeatClient.getId());
			
			if (send!= null && send.equals("1")){
				if (repeatClient.getRecRepeat().getEveICS()!=null){
					String cliEmail = repeatClient.getRecClient().getWhoEmail();
					// Mandar el mail
					if (cliEmail != null){
					
						NotifCalendarDTO modelNot = new NotifCalendarDTO();
						modelNot.setLocale(locale);
						
						modelNot.setNocDtStart(repeatClient.getRecRepeat().getEveStartTime());
							
						String nameApp = messageSourceApp.getMessage("mail.appName", null, locale);
						modelNot.setNocOrgName(nameApp);
						modelNot.setNocUID(repeatClient.getRecRepeat().getEveICS());
						
						modelNot.setNocDesEmail(cliEmail);
						modelNot.setNocDesName(repeatClient.getRecClient().getWhoName());
					    modelNot.setNocLocation(local.getLocLocation());
					    modelNot.setNocDtCreated(repeatClient.getRecRepeat().getEveBookingTime());
					    modelNot.setNocDtStamp(repeatClient.getRecRepeat().getEveBookingTime());
					
					    String title = messageSourceApp.getMessage("mail.invite.title", null, locale);
						title += " " + local.getLocName();
						title += " " + messageSourceApp.getMessage("mail.cancel.title", null, locale);
						
						String content = "<div>" + title + "</div>" + messageSourceApp.getMessage("mail.cancel.text", null, locale);
						content = generatorVelocity.generateContent(modelNot, content).toString();
						
						if (text!= null && !text.equals("")){
							content += "<p>"+text+"</p>";
						}
	
						modelNot.setNocSummary(title);
					    modelNot.setNocContent(content);
						
					    /*
					    List<String> recipientCC = new ArrayList<String>();
					    recipientCC.add(calendar.getCalProf().getWhoEmail());*/
					    
						new MailController().cancel(arg0, arg1, modelNot/*, recipientCC*/);
					}
				}	
			}
		}
	}

	public void setMessageSourceApp(MessageSource messageSourceApp) {
		this.messageSourceApp = messageSourceApp;
	}

	public void setGeneratorVelocity(Generator generatorVelocity) {
		this.generatorVelocity = generatorVelocity;
	}

	public void setLocalDAO(LocalDAO localDAO) {
		this.localDAO = localDAO;
	}

	public void setCalendarDAO(CalendarDAO calendarDAO) {
		this.calendarDAO = calendarDAO;
	}

	public void setRepeatClientDAO(RepeatClientDAO repeatClientDAO) {
		this.repeatClientDAO = repeatClientDAO;
	}

	public void setLocalTaskDAO(LocalTaskDAO localTaskDAO) {
		this.localTaskDAO = localTaskDAO;
	}

	public void setClientDAO(ClientDAO clientDAO) {
		this.clientDAO = clientDAO;
	}

	public void setFirmDAO(FirmDAO firmDAO) {
		this.firmDAO = firmDAO;
	}

	public void setMultiTextDAO(MultiTextDAO multiTextDAO) {
		this.multiTextDAO = multiTextDAO;
	}

	public void setCalController(CalendarController calController) {
		this.calController = calController;
	}	
	
}
