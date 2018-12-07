package com.diloso.bookhair.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.mail.SendFailedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
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

import com.diloso.bookhair.app.negocio.config.impl.ConfigClientField;
import com.diloso.bookhair.app.negocio.dao.CalendarDAO;
import com.diloso.bookhair.app.negocio.dao.ClientDAO;
import com.diloso.bookhair.app.negocio.dao.EventDAO;
import com.diloso.bookhair.app.negocio.dao.EventDAOGoogle;
import com.diloso.bookhair.app.negocio.dao.FirmDAO;
import com.diloso.bookhair.app.negocio.dao.LocalDAO;
import com.diloso.bookhair.app.negocio.dao.LocalTaskDAO;
import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.RepeatDAO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.dto.EventDTO;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatDTO;
import com.diloso.bookhair.app.negocio.dto.generator.NotifCalendarDTO;
import com.diloso.bookhair.app.negocio.utils.Utils;
import com.diloso.bookhair.app.negocio.utils.templates.Generator;


@Controller
@RequestMapping(value={"/*/event", "/event"})
public class EventController {
	
	protected static final Logger log = Logger.getLogger(EventController.class.getName());
	
	public static final String CHAR_TAG_BR = "<br>";
	
	@Autowired
	protected MessageSource messageSourceApp;
	
	@Autowired
	protected Generator generatorVelocity;
	
	@Autowired
	protected LocalDAO localDAO;
	
	@Autowired
	protected CalendarDAO calendarDAO;
	
	@Autowired
	protected EventDAO eventDAO;
	
	@Autowired
	protected RepeatDAO repeatDAO;
	
	@Autowired
	protected LocalTaskDAO localTaskDAO;
	
	@Autowired
	protected ClientDAO clientDAO;
	
	@Autowired
	protected FirmDAO firmDAO;
	
	@Autowired
	protected MultiTextDAO multiTextDAO;
	
	@Autowired
	protected EventDAOGoogle eventDAOGoogle;
	
	@Autowired
	protected CalendarController calController;
	
	@Autowired
	protected RepeatController repeatController;
	
	@ExceptionHandler(UncategorizedDataAccessException.class)
	@ResponseStatus(value=HttpStatus.CONFLICT)
	protected void error(Exception t, HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.CONFLICT.value(), t.getMessage());
	}
	
	/* La fecha es correcta
	 *    Minima fecha: momento actual + tiempo de reserva de eventos
	 *    Maxima fecha: dia de hoy a las 0 horas + tiempo de apertura de agenda del local
	 * Exista espacio libre para las tareas
	 * El cliente no haya reservado más de local.getLocNumUsuDays()
	 * El nombre de cliente es obligatorio si es nuevo 
	 * El email  de cliente es obligatorio si es nuevo y si no admin
	 * El telf   de cliente es obligatorio si es nuevo strCliId  y si no admin
	 * El mail está repetido si es nuevo
	*/
	protected boolean validateNew(HttpServletRequest arg0, boolean isNew, String cliName, String cliEmail, String cliTelf, Date time, LocalDTO local, List<Map<String,Object>> listCalendarOpen, List<List<LocalTaskDTO>> listLocalTasks, boolean admin, boolean SP) throws UncategorizedDataAccessException {
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
			ClientDTO eveClient = null;
			if (!admin){ // Si no es admin, buscamos por el email, que es obligatorio
				eveClient = clientDAO.getByEmail(local.getResFirId(),cliEmail);
			} 
			if (!isNew && !admin && (getEventByClientAgo(local.getId(), eveClient.getId(), local.getLocNumUsuDays()).size()>0) ){
				message = messageSourceApp.getMessage("form.error.client.bookingOne1", null, locale)+" "+local.getLocNumUsuDays()+" "+messageSourceApp.getMessage("form.error.client.bookingOne2", null, locale);
				res = false;
			} else {
				// si los posibles puestos son más de uno: admin es siempre false, se respeta el horario del puesto, ampliar por manager
				if (listCalendarOpen.size()>1){
					admin = false;
				}
				if (SP){
					if (!calController.existsSpaceTasksSP(listLocalTasks.get(0), listCalendarOpen, time, local.getLocApoDuration(), null, admin, false)){
						message = messageSourceApp.getMessage("form.error.client.apoBooked", null, locale);
						res = false;
					}
				} else {
					if (!calController.existsSpaceTasks(listLocalTasks, listCalendarOpen, time, local.getLocApoDuration(), null, admin)){	
						message = messageSourceApp.getMessage("form.error.client.apoBooked", null, locale);
						res = false;
					}
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
			ClientDTO eveClient = clientDAO.getByEmail(resFirId,cliEmail);
			if (eveClient!=null){
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
		String selectedTasksCount = arg0.getParameter("selectedTasksCount");
		List<LocalTaskDTO> listLocalTaskCombi = localTaskDAO.getLocalTaskCombi(new Long(localId), RequestContextUtils.getLocale(arg0).getLanguage(), "");
		List<List<LocalTaskDTO>> listLocalTasks = calController.getListLocalTasks(selectedTasks,selectedTasksCount,listLocalTaskCombi);
		newObject (arg0,arg1,localId,listLocalTasks,listCalendarCandidate,true, false);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/booking/new")
	@ResponseStatus(HttpStatus.OK)
	protected void newObject(HttpServletRequest arg0, HttpServletResponse arg1, String localId, @RequestParam("selectedCalendars") String selectedCalendars)
			throws Exception {
		List<Long> listCalendarCandidate = calController.getCalendarsId(selectedCalendars);
		String selectedTasks = arg0.getParameter("selectedTasks");
		String selectedTasksCount = arg0.getParameter("selectedTasksCount");
		List<LocalTaskDTO> listLocalTaskCombi = localTaskDAO.getLocalTaskCombi(new Long(localId), RequestContextUtils.getLocale(arg0).getLanguage(), "");
		List<List<LocalTaskDTO>> listLocalTasks = calController.getListLocalTasks(selectedTasks,selectedTasksCount,listLocalTaskCombi);
		newObject (arg0,arg1,localId,listLocalTasks,listCalendarCandidate,false, false);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/operator/newSP")
	@ResponseStatus(HttpStatus.OK)
	protected void newObjectAdminSP(HttpServletRequest arg0, HttpServletResponse arg1, String localId, @RequestParam("selectedCalendars") String selectedCalendars)
			throws Exception {
		List<Long> listCalendarCandidate = calController.getCalendarsId(selectedCalendars);
		String selectedTasks = arg0.getParameter("selectedTasks");
		List<List<LocalTaskDTO>> listLocalTasks = calController.getListLocalTasks(selectedTasks);
		newObject (arg0,arg1,localId,listLocalTasks,listCalendarCandidate,true, true);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/booking/newSP")
	@ResponseStatus(HttpStatus.OK)
	protected void newObjectSP(HttpServletRequest arg0, HttpServletResponse arg1, String localId, @RequestParam("selectedCalendars") String selectedCalendars)
			throws Exception {
		List<Long> listCalendarCandidate = calController.getCalendarsId(selectedCalendars);
		String selectedTasks = arg0.getParameter("selectedTasks");
		List<List<LocalTaskDTO>> listLocalTasks = calController.getListLocalTasks(selectedTasks);
		newObject (arg0,arg1,localId,listLocalTasks,listCalendarCandidate,false, true);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/operator/newGoods")
	@ResponseStatus(HttpStatus.OK)
	protected void newObjectAdminGoods(HttpServletRequest arg0, HttpServletResponse arg1, String localId, @RequestParam("selectedCalendars") String selectedCalendars, @RequestParam("numLines") int numLines, @RequestParam("numPallets") int numPallets)
			throws Exception {
		List<Long> listCalendarCandidate = calController.getCalendarsId(selectedCalendars);
		List<List<LocalTaskDTO>> listLocalTasks = calController.getListLocalTasksGoods(numLines,numPallets,localId,RequestContextUtils.getLocale(arg0));
		newObject (arg0,arg1,localId,listLocalTasks,listCalendarCandidate,true, true);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/booking/newGoods")
	@ResponseStatus(HttpStatus.OK)
	protected void newObjectGoods(HttpServletRequest arg0, HttpServletResponse arg1, String localId, @RequestParam("selectedCalendars") String selectedCalendars, @RequestParam("numLines") int numLines, @RequestParam("numPallets") int numPallets)
			throws Exception {
		List<Long> listCalendarCandidate = calController.getCalendarsId(selectedCalendars);
		List<List<LocalTaskDTO>> listLocalTasks = calController.getListLocalTasksGoods(numLines,numPallets,localId,RequestContextUtils.getLocale(arg0));
		newObject (arg0,arg1,localId,listLocalTasks,listCalendarCandidate,false, true);
	}
	
	protected void newObject(HttpServletRequest arg0, HttpServletResponse arg1, String localId, List<List<LocalTaskDTO>> listLocalTasks, List<Long> listCalendarCandidate, boolean admin, boolean SP)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		// Propiedades de local
		LocalDTO local = localDAO.getById(new Long(localId));
		
		// Propiedades de firma
		FirmDTO firm = firmDAO.getById(local.getResFirId());
		
		Long lngEveStartTime = new Long(arg0.getParameter("eveStartTime"));
		Date eveStartTime = new Date(lngEveStartTime);
		
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.setTime(eveStartTime);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		eveStartTime = calendarGreg.getTime();
		
		String selectedDate = calendarGreg.get(Calendar.YEAR)
				+ CalendarController.CHAR_SEP_DATE
				+ (calendarGreg.get(Calendar.MONTH) +1)
				+ CalendarController.CHAR_SEP_DATE
				+ calendarGreg.get(Calendar.DAY_OF_MONTH);
		
		List<Map<String,Object>> listCalendarOpen = calController.getListCalendarOpen(local, selectedDate, listCalendarCandidate);
		List<EventDTO> listEvents = null;
		for (Map<String,Object> calendarOpen : listCalendarOpen) {
			listEvents = eventDAO.getEventByDay((CalendarDTO)calendarOpen.get(CalendarController.CAL), selectedDate);
			if (firm.getFirConfig().getConfigLocal().getConfigLocRepeat()==1){
				List<RepeatDTO> listRepeatLocal = repeatController.listCalendarByDay(((CalendarDTO)calendarOpen.get(CalendarController.CAL)).getId(), selectedDate);
				listEvents.addAll(listRepeatLocal);
			}
			calendarOpen.put(CalendarController.EVENTS, listEvents);
			calendarOpen.put(CalendarController.EVENTS_APO, new ArrayList<EventDTO>());
		}
		
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
		
		String eveDescAlega = arg0.getParameter("eveDescAlega");
						
		if (validateNew(arg0, isNew, cliName, cliEmail, cliTelf, eveStartTime, local, listCalendarOpen, listLocalTasks, admin, SP)){ 
		
			EventDTO eventOrig = new EventDTO();
			
			TimeZone calendarTimeZone = TimeZone.getTimeZone(local.getLocWhere().getWheTimeZone());
			calendarGreg = new GregorianCalendar();
			calendarGreg.set(Calendar.MILLISECOND, 0);
			Date eveBookingTime = calendarGreg.getTime();
			// Calculamos el desplazamiento de la zona horaria desde UTC
			eveBookingTime =  new Date(eveBookingTime.getTime() + calendarTimeZone.getOffset(eveBookingTime.getTime()));
						
			// Propiedades de cliente
			ClientDTO eveClient = null;
			if (!admin){ // Si no es admin 
				if (!isNew){ // Si no es nuevo, buscamos por el email, que es obligatorio
					eveClient = clientDAO.getByEmail(local.getResFirId(),cliEmail);
				}	
			} else { // Es admin
				if (!isNew){ // Si no es nuevo, buscamos por el id
					eveClient = clientDAO.getById(new Long(strCliId));
				}
			}
			if (isNew) {// Es nuevo
				eveClient = new ClientDTO();
				eveClient.setEnabled(1);
				eveClient.setResFirId(local.getResFirId());
				if (cliEmail != null && cliEmail.length()>0){
					eveClient.setWhoEmail(cliEmail);
				}
				eveClient.setWhoName(cliName);
				if (cliTelf.startsWith("6")){
					eveClient.setWhoTelf1(cliTelf);
				} else {
					eveClient.setWhoTelf2(cliTelf);	
				}
				eveClient.setCliCreationTime(eveBookingTime);
				eveClient = clientDAO.create(eveClient);
			
			} else if (!admin){
				// Puede que hayan puesto un nombre y telf distinto del guardado en la BBDD
				eveClient.setWhoName(cliName);
				if (cliTelf.startsWith("6")){
					eveClient.setWhoTelf1(cliTelf);
				} else {
					eveClient.setWhoTelf2(cliTelf);	
				}
				eveClient = clientDAO.update(eveClient);
			}
			
			// Propiedades de booking
			int eveBooking = 0;
			if (admin){ // Si hay usuario manager entonces el ha reservado
				eveBooking = 1;
			}
			
			if (firm.getFirDomain().equals("adveo")){
				eveDescAlega = listLocalTasks.get(0).get(0).getLotName() +" - " + eveDescAlega;
 			}
			
			eventOrig.setEnabled(1);
			eventOrig.setEveDesc(eveDescAlega);
			eventOrig.setEveClient(eveClient);
			
			eventOrig.setEveBooking(eveBooking);
			eventOrig.setEveBookingTime(eveBookingTime);
			eventOrig.setEveConsumed(0);
			eventOrig.setEveNotified(0);
			
			NotifCalendarDTO modelNot = null;
			int indx = 0;
			Date eveStartTimeDS = null;
			Date eveEndTimeDS = null;
			Date eveEndTimeShow = null;
			calendarGreg.setTime(eveStartTime);

			List<EventDTO> listEventAsign = new ArrayList<EventDTO>();
			for (Map<String,Object> calendarOpen : listCalendarOpen) {
				listEventAsign.addAll((List<EventDTO>)calendarOpen.get(CalendarController.EVENTS_APO));
			}
			
			String tasksMail = "";
			if (!firm.getFirConfig().getConfigLocal().getConfigLocSP()){
				tasksMail += messageSourceApp.getMessage("mail.invite.tasks1", null, locale)+" "+ listEventAsign.size() + " "+messageSourceApp.getMessage("mail.invite.tasks2", null, locale)+":";
			}	
			MultiTextDTO multiTextKey = null;
			EventDTO event;
			for (EventDTO eventAux : listEventAsign) {
				eveStartTimeDS = eventAux.getEveStartTime();
				eveEndTimeDS = eventAux.getEveEndTime();
				eveEndTimeShow = eventAux.getEveEndTimeShow();
				if (indx==0){
					modelNot = new NotifCalendarDTO();
					modelNot.setLocale(locale);
					modelNot.setTimeZone(calendarTimeZone);
					
					modelNot.setNocDtStart(eveStartTimeDS);
					
					String nameApp = messageSourceApp.getMessage("mail.appName", null, locale);
					modelNot.setNocOrgName(nameApp);
					modelNot.setNocLocName(firm.getFirName());
					modelNot.setNocLocEmail(local.getLocRespon().getWhoEmail());
					modelNot.setNocLocTelf(local.getLocRespon().getWhoTelf());
					modelNot.setNocUID(eveClient.getId()+"_"+eveBookingTime.getTime()+ "@"+ nameApp);
				} 
				if (firm.getFirDomain().equals("adveo")){
					tasksMail += listLocalTasks.get(0).get(0).getLotName();
				} else {
					if (local.getLocMulServices() == 1){
						tasksMail += " 1 ";
					}
					multiTextKey = multiTextDAO.getByLanCodeAndKey(locale.getLanguage(), eventAux.getEveLocalTask().getLotNameMulti());
					tasksMail += multiTextKey.getMulText();
				}	
				if (listCalendarCandidate!=null && listCalendarCandidate.size()>0){
					String keyStrCal = MultiTextController.WEB_CONFIG+"configDenCal" +"."+ firm.getFirConfig().getConfigDenon().getListDenon().get("configDenCal") + ".label.header.places";
					String strCal = messageSourceApp.getMessage(keyStrCal, null, locale);//config.configDenCal.court.label.header.places
					tasksMail += CHAR_TAG_BR+strCal+": ";
					for (Long calCanId : listCalendarCandidate) {
						tasksMail += getCalendarName(calCanId, listCalendarOpen);
					}
				}
				
				event = new EventDTO();
				PropertyUtils.copyProperties(event, eventOrig);
				
				event.setEveCalendarId(eventAux.getEveCalendarId());
				
				event.setEveLocalTask(eventAux.getEveLocalTask());
				
				event.setEveStartTime(eveStartTimeDS);
				event.setEveEndTime(eveEndTimeDS);
				event.setEveICS(modelNot.getNocUID());

				// Configuracion de campos extras de evento
				Map<String,ConfigClientField> configClientBook = firm.getFirConfig().getConfigClient().getExtraBook(); 
				if (!configClientBook.isEmpty()){
					ConfigClientField celebrationDateField = configClientBook.get("celebrationDate");
					if (celebrationDateField!=null){
						String strCelebrationDate = arg0.getParameter("celebrationDate");
						Date celebrationDate = Utils.getDateYearLast(strCelebrationDate, locale);
						event.setEveCelebrationTime(celebrationDate);	
					}					
				}
				
				event = eventDAO.create(event);
			
				// Sincronizacion con GCalendar
				if (local.getLocSinGCalendar()!=null){
					// Estas propiedades no persisten en BBDD
					event.getEveLocalTask().setLotName(multiTextKey.getMulText());
					event.setEveLocale(locale);
					String eveIDGCalendar = eventDAOGoogle.insertEvent(local,event);
					event.setEveIDGCalendar(eveIDGCalendar);
					event = eventDAO.update(event);
				}
				
				indx ++;
				
			}
			
			// Envio de email
			if (eveClient.getWhoEmail() != null && eveClient.getWhoEmail().length()>0){
				
				modelNot.setNocDtEnd(eveEndTimeShow);
				
				modelNot.setNocDesEmail(eveClient.getWhoEmail());
				modelNot.setNocDesName(eveClient.getWhoName());
			    modelNot.setNocLocation(local.getLocLocation());
			    modelNot.setNocDtCreated(eveBookingTime);
			    modelNot.setNocDtStamp(eveBookingTime);
			    modelNot.setNocTasks(tasksMail);
		
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
			    
				MailController mailController = new MailController();
				int retrys = 0;
				while (retrys<MailController.MAX_RETRYS_EMAIL) {
					try {
						TimeUnit.SECONDS.sleep(1);
						mailController.invite(arg0, arg1, modelNot);
						retrys = MailController.MAX_RETRYS_EMAIL;
					} catch (SendFailedException e) {
						retrys++;
					}
				}
			}
			
		}
	
	}
	
	private String getCalendarName(Long id,List<Map<String,Object>> listCalendars){
		for (Map<String,Object> calendar : listCalendars) {
			if (((CalendarDTO)calendar.get(CalendarController.CAL)).getId().equals(id)){
				return ((CalendarDTO)calendar.get(CalendarController.CAL)).getCalName();
			}
		}
		return "";
	}
	
	@RequestMapping("/operator/listByDiaryRepeat")
	protected @ResponseBody
	List<EventDTO> listByDiaryRepeat(@RequestParam("localId") String localId, HttpServletRequest arg0, @RequestParam("selectedDate") String selectedDate) throws Exception {
 
		List<EventDTO> listEventLocal = listByDiary(localId, arg0, selectedDate);
		
		List<RepeatDTO> listRepeatLocal = repeatController.listByDiary(localId, arg0, selectedDate);
		listEventLocal.addAll(listRepeatLocal);

		return listEventLocal;
	}
	
	@RequestMapping("/operator/listByDiary")
	protected @ResponseBody
	List<EventDTO> listByDiary(@RequestParam("localId") String localId, HttpServletRequest arg0, @RequestParam("selectedDate") String selectedDate) throws Exception {
 
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		List<CalendarDTO> listCalendar = calendarDAO.getCalendarAdmin(new Long(localId));

		List<EventDTO> listEventLocal = new ArrayList<EventDTO>();
		MultiTextDTO multiTextKey = null;
		for (CalendarDTO calendar : listCalendar) {
			List<EventDTO> listEventAux = eventDAO.getEventByWeek(calendar,selectedDate);
			// Añadimos los eventos de este puesto a los del local
			for (EventDTO event : listEventAux) {
				multiTextKey = multiTextDAO.getByLanCodeAndKey(locale.getLanguage(),event.getEveLocalTask().getLotNameMulti());
				event.getEveLocalTask().setLotName(multiTextKey.getMulText());
				event.setEveCalendarName(calendar.getCalName());
				listEventLocal.add(event);
			}
		}

		return listEventLocal;
	}
	
	@RequestMapping("/operator/listByDayRepeat")
	protected @ResponseBody
	List<EventDTO> listByDayRepeat(@RequestParam("localId") Long localId, @RequestParam("selectedDate") String selectedDate) throws Exception {

 
		List<EventDTO> listEventLocal = listByDay(localId, selectedDate);
		
		List<RepeatDTO> listRepeatLocal = repeatController.listByDay(localId, selectedDate);
		listEventLocal.addAll(listRepeatLocal);

		return listEventLocal;
	}
	
	@RequestMapping("/operator/listByDay")
	protected @ResponseBody
	List<EventDTO> listByDay(@RequestParam("localId") Long localId, @RequestParam("selectedDate") String selectedDate) throws Exception {

		List<CalendarDTO> listCalendar = calendarDAO.getCalendarAdmin(localId);

		List<EventDTO> listEventLocal = new ArrayList<EventDTO>();
		for (CalendarDTO calendar : listCalendar) {
			List<EventDTO> listEventAux = eventDAO.getEventByDay(calendar,selectedDate);
			// Añadimos los eventos de este puesto a los del local
			for (EventDTO event : listEventAux) {
				listEventLocal.add(event);
			}
		}

		return listEventLocal;
	}
	
	@RequestMapping("/operator/listCalendarByDayRepeat")
	protected @ResponseBody
	List<EventDTO> listCalendarByDayRepeat(@RequestParam("id") Long id, @RequestParam("selectedDate") String selectedDate) throws Exception {

		List<EventDTO> listEventLocal = listCalendarByDay(id, selectedDate);
		
		List<RepeatDTO> listRepeatLocal = repeatController.listCalendarByDay(id, selectedDate);
		listEventLocal.addAll(listRepeatLocal);

		return listEventLocal;
	}
	
	@RequestMapping("/operator/listCalendarByDay")
	protected @ResponseBody
	List<EventDTO> listCalendarByDay(@RequestParam("id") Long id, @RequestParam("selectedDate") String selectedDate) throws Exception {

		CalendarDTO calendar = calendarDAO.getById(id);
		List<EventDTO> listEvent = eventDAO.getEventByDay(calendar,selectedDate);

		return listEvent;
	}
	
		
	@RequestMapping("/operator/listByICS")
	protected @ResponseBody
	List<EventDTO> getEventByICS(HttpServletRequest arg0, @RequestParam("ICS") String ICS) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);

		List<EventDTO> listEventLocal = eventDAO.getEventByICS(ICS);
		MultiTextDTO multiTextKey = null;
		for (EventDTO event : listEventLocal) {
			multiTextKey = multiTextDAO.getByLanCodeAndKey(locale.getLanguage(),event.getEveLocalTask().getLotNameMulti());
			event.getEveLocalTask().setLotName(multiTextKey.getMulText());
		}
		
		return listEventLocal;
	}
	
	protected List<EventDTO> getEventByClientAgo(Long localId, Long clientId, int numDays ) {

		List<CalendarDTO> listCalendar = calendarDAO.getCalendar(localId);
		List<EventDTO> listEventAux = null;
		List<EventDTO> listEventLocal = new ArrayList<EventDTO>();
		for (CalendarDTO calendar : listCalendar) {
			if (numDays!=-1){
				// Calculamos el desplazamiento de la zona horaria desde UTC
				LocalDTO local = localDAO.getById(new Long(localId));
				TimeZone calendarTimeZone = TimeZone.getTimeZone(local.getLocWhere().getWheTimeZone());
				Date minDate =  new Date();
				minDate =  new Date(minDate.getTime() + calendarTimeZone.getOffset(minDate.getTime()));
				listEventAux = eventDAO.getEventByClientAgo(calendar, clientId,minDate, numDays);
			} else {
				listEventAux = eventDAO.getEventByClientAgo(calendar, clientId, null, -1);
			}
			// Añadimos los eventos de este puesto a los del local
			for (EventDTO event : listEventAux) {
				listEventLocal.add(event);
			}
		}

		return listEventLocal;
	}
	
	@RequestMapping("/operator/listByClientAgo")
	private @ResponseBody
	List<EventDTO> getEventByClientAgo(@RequestParam("localId") String localId, HttpServletRequest arg0, @RequestParam("id") Long id) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);

		List<EventDTO> listEventLocal = getEventByClientAgo(new Long(localId), id, -1);
		List<EventDTO> listEventClient = new ArrayList<EventDTO>();
		EventDTO eventCandidate = null;
		MultiTextDTO multiTextKey = null;
		String strTasks = "";
		String ics = "";
		for (EventDTO event : listEventLocal) {
			if (!ics.equals(event.getEveICS()) && eventCandidate!=null){
				listEventClient.add(eventCandidate);
				strTasks = "";
			}
			multiTextKey = multiTextDAO.getByLanCodeAndKey(locale.getLanguage(),event.getEveLocalTask().getLotNameMulti());
			if (strTasks!=""){
				strTasks += " , " + multiTextKey.getMulText();
			} else {
				strTasks = multiTextKey.getMulText();	
			}
			event.getEveLocalTask().setLotName(strTasks);
			eventCandidate = event;
			ics = event.getEveICS();
		}
		if (eventCandidate!=null){
			listEventClient.add(eventCandidate);
		}
		return listEventClient;
	}
	
		
	@RequestMapping(method = RequestMethod.PUT, value = "/operator/notify")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	protected int notify(@RequestParam("id") Long id)
			throws Exception {
		
		EventDTO event = eventDAO.getById(id);
		if (event!=null){
			if (event.getEveNotified()==1){
				event.setEveNotified(0);
			} else {
				event.setEveNotified(1);
			}
			eventDAO.update(event);
		}
		return event.getEveNotified();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/operator/cancel")
	@ResponseStatus(HttpStatus.OK)
	public void cancel(HttpServletRequest arg0, HttpServletResponse arg1, @RequestParam("localId") String localId, @RequestParam("id") Long id, @RequestParam("send") String send, @RequestParam("text") String text)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);

		LocalDTO local = localDAO.getById(new Long(localId));
		
		EventDTO event = eventDAO.getById(id);
		if (event!=null){
			event.setEnabled(0);
			eventDAO.update(event);
			
			// Sincronizacion con GCalendar
			if (local.getLocSinGCalendar()!=null){
				eventDAOGoogle.removeEvent(local,event);
			}
			
			log.info("Evento cancelado : "+event.getId());
			
			if (send!= null && send.equals("1")){
				if (event.getEveICS()!=null){
					String cliEmail = event.getEveClient().getWhoEmail();
					// Mandar el mail
					if (cliEmail != null){
					
						NotifCalendarDTO modelNot = new NotifCalendarDTO();
						modelNot.setLocale(locale);
						TimeZone calendarTimeZone = TimeZone.getTimeZone(local.getLocWhere().getWheTimeZone());
						modelNot.setTimeZone(calendarTimeZone);
						
						modelNot.setNocDtStart(event.getEveStartTime());
							
						String nameApp = messageSourceApp.getMessage("mail.appName", null, locale);
						modelNot.setNocOrgName(nameApp);
						modelNot.setNocUID(event.getEveICS());
						
						modelNot.setNocDesEmail(cliEmail);
						modelNot.setNocDesName(event.getEveClient().getWhoName());
					    modelNot.setNocLocation(local.getLocLocation());
					    modelNot.setNocDtCreated(event.getEveBookingTime());
					    modelNot.setNocDtStamp(event.getEveBookingTime());
					
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
					    
						MailController mailController = new MailController();
						int retrys = 0;
						while (retrys<MailController.MAX_RETRYS_EMAIL) {
							try {
								TimeUnit.SECONDS.sleep(1);
								mailController.cancel(arg0, arg1, modelNot/* , recipientCC */);
								retrys = MailController.MAX_RETRYS_EMAIL;
							} catch (SendFailedException e) {
								if (++retrys == MailController.MAX_RETRYS_EMAIL) {
									throw e;
								}
							}
						}
					}
				}	
			}
		}
	}
	
	
	@RequestMapping(method = RequestMethod.PUT, value = "/operator/consume")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	private int consume(@RequestParam("ICS") String ICS) throws Exception {
		Integer putConsumed = null;
		List<EventDTO> listEventLocal = eventDAO.getEventByICS(ICS);
		for (EventDTO event : listEventLocal) {
			if (putConsumed==null){ // Solo lo hacemos en el primer evento de la reserva
				if (event.getEveConsumed()>0){
					putConsumed = 0;
				} else {
					putConsumed = 1;
				}
			}
			event.setEveConsumed(putConsumed);
			eventDAO.update(event);
		}
		return putConsumed;
	}
	
	
	@RequestMapping(method = RequestMethod.PUT, value = "/operator/consumeComent")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	private int consumeComent(@RequestParam("ICS") String ICS, @RequestParam("sel") int sel, @RequestParam("text") String text) throws Exception {
		Integer putConsumed = null;
		List<EventDTO> listEventLocal = eventDAO.getEventByICS(ICS);
		for (EventDTO event : listEventLocal) {
			if (putConsumed==null){ // Solo lo hacemos en el primer evento de la reserva
				if (event.getEveConsumed()>0){
					putConsumed = 0;
				} else {
					putConsumed = sel;
				}
			}
			event.setEveConsumed(putConsumed);
			if (text.length()>0){
				text = event.getEveDesc() + text;
				event.setEveDesc(text);
			}	
			eventDAO.update(event);
		}
		return putConsumed;
	}
}
