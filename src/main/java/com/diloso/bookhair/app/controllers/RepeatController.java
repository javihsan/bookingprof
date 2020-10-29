package com.diloso.bookhair.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dao.AnnualDiaryDAO;
import com.diloso.bookhair.app.negocio.dao.CalendarDAO;
import com.diloso.bookhair.app.negocio.dao.DiaryDAO;
import com.diloso.bookhair.app.negocio.dao.LocalDAO;
import com.diloso.bookhair.app.negocio.dao.LocalTaskDAO;
import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.RepeatDAO;
import com.diloso.bookhair.app.negocio.dao.SemanalDiaryDAO;
import com.diloso.bookhair.app.negocio.dto.AnnualDiaryDTO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatDTO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.negocio.dto.generator.NotifCalendarDTO;
import com.diloso.bookhair.app.negocio.utils.templates.Generator;
import com.diloso.bookhair.app.persist.manager.RepeatManager;

@Controller
@RequestMapping("/repeat")
public class RepeatController {
	
	protected static final Logger log = Logger.getLogger(RepeatController.class.getName());

	protected static final String REP_NAME_PARAM = "repName";
	
	//@Autowired
	protected MessageSource messageSourceApp;
	
	//@Autowired
	protected Generator generatorVelocity;
	
	//@Autowired
	protected LocalDAO localDAO;
	
	//@Autowired
	protected CalendarDAO calendarDAO;
	
	//@Autowired
	protected RepeatDAO repeatDAO;
	
	//@Autowired
	protected LocalTaskDAO localTaskDAO;
	
	//@Autowired
	protected MultiTextDAO multiTextDAO;

	//@Autowired
	protected CalendarController calController;
	
	//@Autowired
	protected AnnualDiaryDAO annualDiaryDAO;
	
	//@Autowired
	protected DiaryDAO diaryDAO;
	
	//@Autowired
	protected SemanalDiaryDAO semanalDiaryDAO;
	
	@ExceptionHandler(UncategorizedDataAccessException.class)
	@ResponseStatus(value=HttpStatus.CONFLICT)
	protected void error(Exception t, HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.CONFLICT.value(), t.getMessage());
	}
	
	/* Exista espacio libre para las tareas ¿Comprobar que en la semana del calendario no haya colisiones??
	*/
	protected boolean validateNew(HttpServletRequest arg0, String defaultNameValue, Date time, LocalDTO local, Long calendarId) throws UncategorizedDataAccessException {
		boolean res = true;
		String message = "";
		Locale locale = RequestContextUtils.getLocale(arg0);
		if (defaultNameValue==null || defaultNameValue.length()==0){
			message =  messageSourceApp.getMessage("form.error.repeat.nameReq", null, locale);
			res = false;
		} 
//		if (!calController.existsSpaceTasks(listLocalTasks, listCalendarOpen, time, local.getLocApoDuration(), null, admin)){	
//			message = messageSourceApp.getMessage("form.error.client.apoBooked", null, locale);
//			res = false;
//		}
		
		if (!res){
			throw new ErrorService(message, null);
		}
		return res;
	}
	
	/* El nombre, tarea y duracion no está vacío.
	*/
	protected boolean validateNew(HttpServletRequest arg0, String defaultNameValue, String strLotTaskId, String strLotTaskDuration, String strLotTaskRate) throws UncategorizedDataAccessException {
		boolean res = true;
		String message = "";
		Locale locale = RequestContextUtils.getLocale(arg0);
		if (defaultNameValue==null || defaultNameValue.length()==0){
			message =  messageSourceApp.getMessage("form.error.localTask.nameReq", null, locale);
			res = false;
		} else if (strLotTaskId==null || strLotTaskId.length()==0){
			message = messageSourceApp.getMessage("form.error.localTask.taskReq", null, locale);
			res = false;
		} else if (strLotTaskDuration==null || strLotTaskDuration.length()==0){
			message = messageSourceApp.getMessage("form.error.localTask.durationReq", null, locale);
			res = false;
		} else if (strLotTaskRate==null || strLotTaskRate.length()==0){
			message = messageSourceApp.getMessage("form.error.localTask.rateReq", null, locale);
			res = false;
		} else {
			try{
				strLotTaskRate = strLotTaskRate.replace(",", ".");
				new Float(strLotTaskRate);
				
			} catch( NumberFormatException e){
				message = messageSourceApp.getMessage("form.error.localTask.rateNum", null, locale);
				res = false;
			}
		}
		
		if (!res){
			throw new ErrorService(message, null);
		}
		return res;
	}
	
	//@RequestMapping(method = RequestMethod.POST, value = "/manager/newMatrix")
	@RequestMapping(method = RequestMethod.GET, value = "/manager/newMatrix")
	@ResponseStatus(HttpStatus.OK)
	protected void newMatrix(HttpServletRequest arg0, HttpServletResponse arg1, @RequestParam("localId") String localId)
			throws Exception {
		newObject (arg0,arg1,localId);
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/newRes")
	@ResponseStatus(HttpStatus.OK)
	protected void newObjectRes(HttpServletRequest arg0, HttpServletResponse arg1, @RequestParam("localId") String localId)
			throws Exception {
		newObject (arg0,arg1,localId);
	}
	
	protected void newObject(HttpServletRequest arg0, HttpServletResponse arg1, String localId)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		String langDefault = locale.getLanguage();
		String defaultNameValue = "Clase de padel 8-10";//arg0.getParameter(REP_NAME_PARAM+"_"+langDefault);
		//String defaultNameValue = "Clase de tenis 5-7";//arg0.getParameter(REP_NAME_PARAM+"_"+langDefault);
		// Propiedades de local
		LocalDTO local = localDAO.getById(new Long(localId));
		
		//Long calendarId = new Long(arg0.getParameter("calendarId"));
		Long calendarId = new Long("6468426906206208");//Pista 2 Padel  Pista 1 tenis 4792771185475584
		//CalendarDTO calendar = calendarDAO.getById(new Long(calendarId));
		
		//Long localTaskId = new Long(arg0.getParameter("localTaskId"));
		Long localTaskId = new Long("5813117976051712");
		LocalTaskDTO localTask = localTaskDAO.getById(new Long(localTaskId));
		
		//Long lngRepStartTime = new Long(arg0.getParameter("repStartTime"));
		//Date repStartTime = new Date(lngRepStartTime);
		Date repStartTime = new Date();
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.setTime(repStartTime);
		calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND, 0);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		//calendarGreg.add(Calendar.MONTH, 1);
		repStartTime = calendarGreg.getTime();
		
//		Long lngRepEndTime = new Long(arg0.getParameter("repEndTime"));
//		Date repEndTime = new Date(lngRepEndTime);
		calendarGreg.add(Calendar.MONTH, 5); 
		Date repEndTime = calendarGreg.getTime();
		//calendarGreg.setTime(repEndTime);
		//repEndTime = calendarGreg.getTime();
		
		Integer repMaxClients = 5; 
		
		if (validateNew(arg0, defaultNameValue, repStartTime, local, calendarId)){ 
		
			String nameValue = "";
			String keyNameMulti = RepeatManager.KEY_MULTI_REPEAT_NAME+localId+"_"+defaultNameValue.toLowerCase();
			Map<String,String> hashNamesParam = new HashMap<String,String>();

			List<LangDTO> listLang = local.getLocLangs();
			for (LangDTO lang : listLang) {
				nameValue = arg0.getParameter(REP_NAME_PARAM+"_"+lang.getLanCode());
				if (nameValue == null || nameValue.length()==0){
					nameValue = defaultNameValue;
				}
				hashNamesParam.put(lang.getLanCode(), nameValue);
			}
			int indx = 0;
			while (multiTextDAO.getByLanCodeAndKey(locale.getLanguage(), keyNameMulti)!=null){
				keyNameMulti = RepeatManager.KEY_MULTI_REPEAT_NAME+localId+"_"+defaultNameValue.toLowerCase()+"_"+indx;
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
				//multiTextDAO.create(nameMulti);
			}
			
			RepeatDTO repeat = new RepeatDTO();
			
			repeat.setRepNameMulti(keyNameMulti);
			
			repeat.setRepType(0);
			//repeat.setMatrix(null); //Es Matrix, no tiene una Matrix superior
			repeat.setRepMaxClients(repMaxClients);
			
			TimeZone calendarTimeZone = TimeZone.getTimeZone(local.getLocWhere().getWheTimeZone());
			calendarGreg = new GregorianCalendar();
			calendarGreg.set(Calendar.MILLISECOND, 0);
			Date repBookingTime = calendarGreg.getTime();
			// Calculamos el desplazamiento de la zona horaria desde UTC
			repBookingTime =  new Date(repBookingTime.getTime() + calendarTimeZone.getOffset(repBookingTime.getTime()));
			
			repeat.setEnabled(1);
			
			repeat.setEveBookingTime(repBookingTime);
			//repeat.setEveConsumed(0); //Solo para One, no para Matrix
												
			repeat.setEveStartTime(repStartTime);
			repeat.setEveEndTime(repEndTime);
			
			repeat.setEveCalendarId(calendarId);
			repeat.setEveLocalTask(localTask);
		/*
			DiaryDTO diary = new DiaryDTO();
			diary.setEnabled(1);
			List<String> diaTimes = new ArrayList<String>();
			
			String strTime = "18:00";
			diaTimes.add(strTime);
			strTime = "19:00";
			diaTimes.add(strTime);
			
			diary.setDiaTimes(diaTimes);
			
			DiaryDTO diaryCreatedTue = diaryDAO.create(diary);
			DiaryDTO diaryCreatedThu = diaryDAO.create(diary);
			
			diaTimes = new ArrayList<String>();
			diary.setDiaTimes(diaTimes);
			DiaryDTO diaryCreatedMon = diaryDAO.create(diary);
			DiaryDTO diaryCreatedWed = diaryDAO.create(diary);
			DiaryDTO diaryCreatedFri = diaryDAO.create(diary);
			DiaryDTO diaryCreatedSat = diaryDAO.create(diary);
			DiaryDTO diaryCreatedSun = diaryDAO.create(diary);

			SemanalDiaryDTO semanalDiary = new SemanalDiaryDTO();
			semanalDiary.setEnabled(1);
			semanalDiary.setSemMonDiary(diaryCreatedMon);
			semanalDiary.setSemTueDiary(diaryCreatedTue);
			semanalDiary.setSemWedDiary(diaryCreatedWed);
			semanalDiary.setSemThuDiary(diaryCreatedThu);
			semanalDiary.setSemFriDiary(diaryCreatedFri);
			semanalDiary.setSemSatDiary(diaryCreatedSat);
			semanalDiary.setSemSunDiary(diaryCreatedSun);
			semanalDiary = semanalDiaryDAO.create(semanalDiary);*/
			
			SemanalDiaryDTO semanalDiary = semanalDiaryDAO.getById(new Long("5109430534275072"));
						
			repeat.setRepSemanalDiary(semanalDiary);
			
			repeatDAO.create(repeat);
			
		}
	}
	
	@RequestMapping("/operator/listByDiary")
	protected @ResponseBody
	List<RepeatDTO> listByDiary(@RequestParam("localId") String localId, HttpServletRequest arg0, @RequestParam("selectedDate") String selectedDate) throws Exception {
 
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		List<CalendarDTO> listCalendar = calendarDAO.getCalendarAdmin(new Long(localId));

		String[] dates = selectedDate
				.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];
				
		List<RepeatDTO> listRepeatLocal = new ArrayList<RepeatDTO>();
		MultiTextDTO multiTextKey = null;
		Date startDate = null;
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.MILLISECOND, 0);
		//AnnualDiaryDTO annualDiaryDTO = null; 
		DiaryDTO diaryDTO = null;
		RepeatDTO repeatAux = null;
		for (CalendarDTO calendar : listCalendar) {
			List<RepeatDTO> listRepeatAux = repeatDAO.getRepeatByWeek(calendar,selectedDate);
			// Añadimos los repeats de este puesto a los del local
			for (RepeatDTO repeat : listRepeatAux) {
				multiTextKey = multiTextDAO.getByLanCodeAndKey(locale.getLanguage(),repeat.getRepNameMulti());
				repeat.setRepName(multiTextKey.getMulText());
				repeat.setEveCalendarName(calendar.getCalName());
				
				// Por cada día de la semana que cumpla con el Semanary se incluye un nuevo evento
				startDate = repeat.getEveStartTime();
				calendarGreg.setTime(startDate);
				calendarGreg.set(Calendar.YEAR, new Integer(year));
				calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
				calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
				
				for (int dayWeek=0;dayWeek<7;dayWeek++){
					
					// Comprobamos si esta fecha esta señalada en la agenda anual del repeat
					/*AnnualDiaryDTO annualDiaryDTO = annualDiaryDAO.getAnnualDiaryRepatByDay(repeat.getId(),selectedDate);
					diaryDTO = null;
					if (annualDiaryDTO!=null){ // añadimos citas siguiendo la agenda de la fecha anual del local
						diaryDTO = annualDiaryDTO.getAnuDayDiary();
					} else { // añadimos citas siguiendo la agenda por defecto del día de la semana del local
						diaryDTO = repeat.getRepSemanalDiary().getDiary(dayWeek);
					}*/
					
					diaryDTO = repeat.getRepSemanalDiary().getDiary(dayWeek);
					if (diaryDTO!=null && diaryDTO.getDiaTimes()!=null){
						repeatAux = new RepeatDTO();
						PropertyUtils.copyProperties(repeatAux, repeat);
						String hourTime = diaryDTO.getDiaTimes().get(0);
						dates = hourTime.split(CalendarController.CHAR_SEP_DATE_HOUR);
						calendarGreg.set(Calendar.HOUR_OF_DAY, new Integer(dates[0]));
						calendarGreg.set(Calendar.MINUTE, new Integer(dates[1]));
						repeatAux.setEveStartTime(calendarGreg.getTime());
						// Añadimos y luego quitamos los minutos de duracion de la tarea para determinar el endTime
						calendarGreg.add(Calendar.MINUTE, repeat.getEveLocalTask().getLotTaskDuration());
						repeatAux.setEveEndTime(calendarGreg.getTime());
						calendarGreg.add(Calendar.MINUTE, -repeat.getEveLocalTask().getLotTaskDuration());
						listRepeatLocal.add(repeatAux);
					}
					calendarGreg.add(Calendar.DAY_OF_MONTH, 1);
				}	
			}
		}

		return listRepeatLocal;
	}

	
	@RequestMapping("/operator/listByDay")
	protected @ResponseBody
	List<RepeatDTO> listByDay(@RequestParam("localId") Long localId, @RequestParam("selectedDate") String selectedDate) throws Exception {

		List<CalendarDTO> listCalendar = calendarDAO.getCalendarAdmin(localId);

		String[] dates = selectedDate
				.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];
		
		List<RepeatDTO> listRepeatLocal = new ArrayList<RepeatDTO>();
		Date startDate = null;
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.MILLISECOND, 0);
		//AnnualDiaryDTO annualDiaryDTO = null; 
		DiaryDTO diaryDTO = null;
		RepeatDTO repeatAux = null;
		for (CalendarDTO calendar : listCalendar) {
			List<RepeatDTO> listRepeatAux = repeatDAO.getRepeatByDay(calendar,selectedDate);
			// Añadimos los repeats de este puesto a los del local
			for (RepeatDTO repeat : listRepeatAux) {
				
				// Por cada día de la semana que cumpla con el Semanary se incluye un nuevo evento
				startDate = repeat.getEveStartTime();
				calendarGreg.setTime(startDate);
				calendarGreg.set(Calendar.YEAR, new Integer(year));
				calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
				calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
				
				int dayWeek = calendarGreg.get(Calendar.DAY_OF_WEEK)-2;
				if (dayWeek==-2){
					dayWeek= 0; 
				}
				else if (dayWeek==-1){
					dayWeek= 1;
				}
					
				// Comprobamos si esta fecha esta señalada en la agenda anual del repeat
				/*AnnualDiaryDTO annualDiaryDTO = annualDiaryDAO.getAnnualDiaryRepatByDay(repeat.getId(),selectedDate);
				diaryDTO = null;
				if (annualDiaryDTO!=null){ // añadimos citas siguiendo la agenda de la fecha anual del local
					diaryDTO = annualDiaryDTO.getAnuDayDiary();
				} else { // añadimos citas siguiendo la agenda por defecto del día de la semana del local
					diaryDTO = repeat.getRepSemanalDiary().getDiary(dayWeek);
				}*/
				
				diaryDTO = repeat.getRepSemanalDiary().getDiary(dayWeek);
				if (diaryDTO!=null && diaryDTO.getDiaTimes()!=null){
					repeatAux = new RepeatDTO();
					PropertyUtils.copyProperties(repeatAux, repeat);
					String hourTime = diaryDTO.getDiaTimes().get(0);
					dates = hourTime.split(CalendarController.CHAR_SEP_DATE_HOUR);
					calendarGreg.set(Calendar.HOUR_OF_DAY, new Integer(dates[0]));
					calendarGreg.set(Calendar.MINUTE, new Integer(dates[1]));
					repeatAux.setEveStartTime(calendarGreg.getTime());
					// Añadimos y luego quitamos los minutos de duracion de la tarea para determinar el endTime
					calendarGreg.add(Calendar.MINUTE, repeat.getEveLocalTask().getLotTaskDuration());
					repeatAux.setEveEndTime(calendarGreg.getTime());
					calendarGreg.add(Calendar.MINUTE, -repeat.getEveLocalTask().getLotTaskDuration());
					listRepeatLocal.add(repeatAux);
				}
			}
		}

		return listRepeatLocal;
	}
	
	@RequestMapping("/operator/listCalendarByDay")
	protected @ResponseBody
	List<RepeatDTO> listCalendarByDay(@RequestParam("id") Long id, @RequestParam("selectedDate") String selectedDate) throws Exception {

		CalendarDTO calendar = calendarDAO.getById(id);
		
		String[] dates = selectedDate
				.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];
		
		List<RepeatDTO> listRepeat = new ArrayList<RepeatDTO>();
		Date startDate = null;
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.MILLISECOND, 0);
		AnnualDiaryDTO annualDiaryDTO = null; 
		DiaryDTO diaryDTO = null;
		RepeatDTO repeatAux = null;
		List<RepeatDTO> listRepeatAux = repeatDAO.getRepeatByDay(calendar,selectedDate);
		// Añadimos los repeats de este puesto a los del local
		for (RepeatDTO repeat : listRepeatAux) {
			
			// Por cada día de la semana que cumpla con el Semanary se incluye un nuevo evento
			startDate = repeat.getEveStartTime();
			calendarGreg.setTime(startDate);
			calendarGreg.set(Calendar.YEAR, new Integer(year));
			calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
			calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
			
			int dayWeek = calendarGreg.get(Calendar.DAY_OF_WEEK)-2;
			if (dayWeek==-2){
				dayWeek= 0; 
			}
			else if (dayWeek==-1){
				dayWeek= 1;
			}
				
			// Comprobamos si esta fecha esta señalada en la agenda anual del repeat
			/*AnnualDiaryDTO annualDiaryDTO = annualDiaryDAO.getAnnualDiaryRepatByDay(repeat.getId(),selectedDate);
			diaryDTO = null;
			if (annualDiaryDTO!=null){ // añadimos citas siguiendo la agenda de la fecha anual del local
				diaryDTO = annualDiaryDTO.getAnuDayDiary();
			} else { // añadimos citas siguiendo la agenda por defecto del día de la semana del local
				diaryDTO = repeat.getRepSemanalDiary().getDiary(dayWeek);
			}*/
			
			diaryDTO = repeat.getRepSemanalDiary().getDiary(dayWeek);
			if (diaryDTO!=null && diaryDTO.getDiaTimes()!=null){
				repeatAux = new RepeatDTO();
				PropertyUtils.copyProperties(repeatAux, repeat);
				String hourTime = diaryDTO.getDiaTimes().get(0);
				dates = hourTime.split(CalendarController.CHAR_SEP_DATE_HOUR);
				calendarGreg.set(Calendar.HOUR_OF_DAY, new Integer(dates[0]));
				calendarGreg.set(Calendar.MINUTE, new Integer(dates[1]));
				repeatAux.setEveStartTime(calendarGreg.getTime());
				listRepeat.add(repeatAux);
			}
		}
		return listRepeat;
	}
	
	/*
	protected List<RepeatDTO> getRepeatByClientAgo(Long localId, Long clientId, int numDays ) {

		List<CalendarDTO> listCalendar = calendarDAO.getCalendar(localId);
		List<RepeatDTO> listRepeatAux = null;
		List<RepeatDTO> listRepeatLocal = new ArrayList<RepeatDTO>();
		for (CalendarDTO calendar : listCalendar) {
			if (numDays!=-1){
				// Calculamos el desplazamiento de la zona horaria desde UTC
				LocalDTO local = localDAO.getById(new Long(localId));
				TimeZone calendarTimeZone = TimeZone.getTimeZone(local.getLocWhere().getWheTimeZone());
				Date minDate =  new Date();
				minDate =  new Date(minDate.getTime() + calendarTimeZone.getOffset(minDate.getTime()));
				listRepeatAux = repeatDAO.getRepeatByClientAgo(calendar, clientId,minDate, numDays);
			} else {
				listRepeatAux = repeatDAO.getRepeatByClientAgo(calendar, clientId, null, -1);
			}
			// Añadimos los repeatss de este puesto a los del local
			for (RepeatDTO repeat : listRepeatAux) {
				listRepeatLocal.add(repeat);
			}
		}

		return listRepeatLocal;
	}
	
	@RequestMapping("/operator/listByClientAgo")
	private @ResponseBody
	List<RepeatDTO> getRepeatByClientAgo(@RequestParam("localId") String localId, HttpServletRequest arg0, @RequestParam("id") Long id) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);

		List<RepeatDTO> listRepeatLocal = getRepeatByClientAgo(new Long(localId), id, -1);
		List<RepeatDTO> listRepeatClient = new ArrayList<RepeatDTO>();
		RepeatDTO repeatCandidate = null;
		MultiTextDTO multiTextKey = null;
		String strTasks = "";
		String ics = "";
		for (RepeatDTO repeat : listRepeatLocal) {
			if (!ics.equals(repeat.getRepICS()) && repeatCandidate!=null){
				listRepeatClient.add(repeatCandidate);
				strTasks = "";
			}
			multiTextKey = multiTextDAO.getByLanCodeAndKey(locale.getLanguage(),repeat.getRepLocalTask().getLotNameMulti());
			if (strTasks!=""){
				strTasks += " , " + multiTextKey.getMulText();
			} else {
				strTasks = multiTextKey.getMulText();	
			}
			repeat.getEveLocalTask().setLotName(strTasks);
			repeatCandidate = repeat;
			ics = repeat.getRepICS();
		}
		if (repeatCandidate!=null){
			listRepeatClient.add(repeatCandidate);
		}
		return listRepeatClient;
	}*/
		
	@RequestMapping(method = RequestMethod.PUT, value = "/operator/cancel")
	@ResponseStatus(HttpStatus.OK)
	public void cancel(HttpServletRequest arg0, HttpServletResponse arg1, @RequestParam("localId") String localId, @RequestParam("id") Long id, @RequestParam("send") String send, @RequestParam("text") String text)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);

		LocalDTO local = localDAO.getById(new Long(localId));
		
		RepeatDTO repeat = repeatDAO.getById(id);
		if (repeat!=null){
			repeat.setEnabled(0);
			repeatDAO.update(repeat);
			
			log.info("Repeat cancelado : "+repeat.getId());
			
			if (send!= null && send.equals("1")){
				if (repeat.getEveICS()!=null){
					String cliEmail = repeat.getEveClient().getWhoEmail();
					// Mandar el mail
					if (cliEmail != null){
					
						NotifCalendarDTO modelNot = new NotifCalendarDTO();
						modelNot.setLocale(locale);
						
						modelNot.setNocDtStart(repeat.getEveStartTime());
							
						String nameApp = messageSourceApp.getMessage("mail.appName", null, locale);
						modelNot.setNocOrgName(nameApp);
						modelNot.setNocUID(repeat.getEveICS());
						
						modelNot.setNocDesEmail(cliEmail);
						modelNot.setNocDesName(repeat.getEveClient().getWhoName());
					    modelNot.setNocLocation(local.getLocLocation());
					    modelNot.setNocDtCreated(repeat.getEveBookingTime());
					    modelNot.setNocDtStamp(repeat.getEveBookingTime());
					
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

	public void setRepeatDAO(RepeatDAO repeatDAO) {
		this.repeatDAO = repeatDAO;
	}

	public void setLocalTaskDAO(LocalTaskDAO localTaskDAO) {
		this.localTaskDAO = localTaskDAO;
	}

	public void setMultiTextDAO(MultiTextDAO multiTextDAO) {
		this.multiTextDAO = multiTextDAO;
	}

	public void setCalController(CalendarController calController) {
		this.calController = calController;
	}

	public void setAnnualDiaryDAO(AnnualDiaryDAO annualDiaryDAO) {
		this.annualDiaryDAO = annualDiaryDAO;
	}

	public void setDiaryDAO(DiaryDAO diaryDAO) {
		this.diaryDAO = diaryDAO;
	}

	public void setSemanalDiaryDAO(SemanalDiaryDAO semanalDiaryDAO) {
		this.semanalDiaryDAO = semanalDiaryDAO;
	}
	
	/*
	@RequestMapping(method = RequestMethod.PUT, value = "/operator/consume")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	private int consume(@RequestParam("ICS") String ICS) throws Exception {
		Integer putConsumed = null;
		List<RepeatDTO> listRepeatLocal = repeatDAO.getRepeatByICS(ICS);
		for (RepeatDTO repeat : listRepeatLocal) {
			if (putConsumed==null){ // Solo lo hacemos en el primer repeats de la reserva
				if (repeat.getEveConsumed()==1){
					putConsumed = 0;
				} else {
					putConsumed = 1;
				}
			}
			repeat.setEveConsumed(putConsumed);
			repeatDAO.update(repeat);
		}
		return putConsumed;
	}*/
	
	
}
