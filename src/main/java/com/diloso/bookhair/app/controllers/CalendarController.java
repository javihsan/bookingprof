package com.diloso.bookhair.app.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
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

import com.diloso.bookhair.app.negocio.dto.AnnualDiaryDTO;
import com.diloso.bookhair.app.negocio.dto.AppointmentDTO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.negocio.dto.EventDTO;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatDTO;
import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;
import com.diloso.bookhair.app.negocio.dto.TaskDTO;
import com.diloso.bookhair.app.negocio.manager.IAnnualDiaryManager;
import com.diloso.bookhair.app.negocio.manager.ICalendarManager;
import com.diloso.bookhair.app.negocio.manager.IDiaryManager;
import com.diloso.bookhair.app.negocio.manager.IEventManager;
import com.diloso.bookhair.app.negocio.manager.IFirmManager;
import com.diloso.bookhair.app.negocio.manager.ILocalManager;
import com.diloso.bookhair.app.negocio.manager.ILocalTaskManager;
import com.diloso.bookhair.app.negocio.manager.IMultiTextManager;
import com.diloso.bookhair.app.negocio.manager.IRepeatManager;
import com.diloso.bookhair.app.negocio.manager.ISemanalDiaryManager;
import com.diloso.bookhair.app.negocio.manager.ITaskManager;
import com.diloso.bookhair.app.negocio.manager.LocalTaskManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value={"/*/calendar", "/calendar"})
public class CalendarController {

	//protected static final Logger log = Logger.getLogger(CalendarController.class);
	
	@Autowired
	protected MessageSource messageSourceApp;
	
	@Autowired
	protected IAnnualDiaryManager iAnnualDiaryManager;
	
	@Autowired
	protected ILocalManager iLocalManager;
	
	@Autowired
	protected ICalendarManager iCalendarManager;
	
	@Autowired
	protected IEventManager iEventManager;

	@Autowired
	protected IRepeatManager iRepeatManager;
	
	@Autowired
	protected ILocalTaskManager iLocalTaskManager;
	
	@Autowired
	protected ITaskManager iTaskManager;
	
	@Autowired
	protected IDiaryManager iDiaryManager;
	
	@Autowired
	protected ISemanalDiaryManager iSemanalDiaryManager;
	
	@Autowired
	protected IMultiTextManager iMultiTextManager;
	
	@Autowired
	protected IFirmManager iFirmManager;
	
	/*@Autowired
	protected ProfessionalDAO professionalDAO;*/
	
	@Autowired
	protected RepeatController repeatController;
	
	public static final String CHAR_SEP_DATE = "-";
	public static final String CHAR_SEP_DATE_HOUR = ":";
	
	public static final String CAL = "CAL";
	public static final String DIARY = "DIARY";
	public static final String EVENTS = "EVENTS";
	public static final String EVENTS_APO = "EVENTS_APO";
	public static final String EVENTS_PROV = "EVENTS_PROV";
	
	public static final String KEY_CACHE = "BK";
	
	public static final int NUM_DAYS_NEXT_APO = 3;
	
	@ExceptionHandler(UncategorizedDataAccessException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	protected void error(Exception t, HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.BAD_REQUEST.value(), t.getMessage());
	}
	
	/* La fecha no esta señalada como cerrada en la agenda anual del local ni en la semanal
	 * 
	 */
	protected boolean validateListApo(HttpServletRequest arg0, LocalDTO local, String selectedDate, int dayWeek, boolean exists) throws UncategorizedDataAccessException {
		boolean res = true;
		String message = "";
		Locale locale = RequestContextUtils.getLocale(arg0);

		AnnualDiaryDTO annualDiaryDTO = iAnnualDiaryManager.getAnnualDiaryByDay(local.getId(),selectedDate);
		if (annualDiaryDTO!=null){
			if (annualDiaryDTO.getAnuClosed()==1){
				message = messageSourceApp.getMessage("form.error.calendar.dateClosed", null, locale);
				res = false;
			}
		} else {
			for (int closedweekDay : local.getLocSemanalDiary().getClosedDiary()) {
				if (dayWeek==closedweekDay){
					message = messageSourceApp.getMessage("form.error.calendar.dateClosed", null, locale);
					res = false;
					break;
				}
			}
		}
		
		if (!res && !exists){ // Si solo estamos buscando si hay huecos libres no lanzamos exception
			throw new ErrorService(message, null);
		}
		return res;
	}
	
	@RequestMapping("/operator/listApoByDay")
	protected @ResponseBody
	List<AppointmentDTO> listApoByDayAdmin(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("selectedDate") String selectedDate, @RequestParam("selectedTasks") String selectedTasks, @RequestParam("selectedTasksCount") String selectedTasksCount, @RequestParam("selectedCalendars") String selectedCalendars) throws Exception {
		List<LocalTaskDTO> listLocalTaskCombi = iLocalTaskManager.getLocalTaskCombi(new Long(localId), RequestContextUtils.getLocale(arg0).getLanguage(), "");
		List<Long> listCalendarCandidate = getCalendarsId(selectedCalendars);
		return listApoByDay (arg0, localId, selectedDate, getListLocalTasks(selectedTasks,selectedTasksCount,listLocalTaskCombi), listCalendarCandidate, true);
	}

	
	@RequestMapping("/booking/listApoByDay")
	protected @ResponseBody
	List<AppointmentDTO> listApoByDay(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("selectedDate") String selectedDate,  @RequestParam("selectedTasks") String selectedTasks, @RequestParam("selectedTasksCount") String selectedTasksCount, @RequestParam("selectedCalendars") String selectedCalendars) throws Exception {
		List<LocalTaskDTO> listLocalTaskCombi = iLocalTaskManager.getLocalTaskCombi(new Long(localId), RequestContextUtils.getLocale(arg0).getLanguage(), "");
		List<Long> listCalendarCandidate = getCalendarsId(selectedCalendars);
		return listApoByDay (arg0, localId, selectedDate, getListLocalTasks(selectedTasks,selectedTasksCount,listLocalTaskCombi), listCalendarCandidate, false);
	}
		
	@RequestMapping("/operator/listApoByDaySP")
	protected @ResponseBody
	List<AppointmentDTO> listApoByDayAdminSP(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("selectedDate") String selectedDate,  @RequestParam("selectedTasks") String selectedTasks, @RequestParam("selectedTasksCount") String selectedTasksCount, @RequestParam("selectedCalendars") String selectedCalendars) throws Exception {
		List<Long> listCalendarCandidate = getCalendarsId(selectedCalendars);
		return listApoByDay (arg0, localId, selectedDate, getListLocalTasks(selectedTasks), listCalendarCandidate, true, true);
	}
	
	@RequestMapping("/booking/listApoByDaySP")
	protected @ResponseBody
	List<AppointmentDTO> listApoByDaySP(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("selectedDate") String selectedDate,  @RequestParam("selectedTasks") String selectedTasks, @RequestParam("selectedTasksCount") String selectedTasksCount, @RequestParam("selectedCalendars") String selectedCalendars) throws Exception {
		List<Long> listCalendarCandidate = getCalendarsId(selectedCalendars);
		return listApoByDay (arg0, localId, selectedDate, getListLocalTasks(selectedTasks), listCalendarCandidate, false, true);
	}
	
	@RequestMapping("/operator/listApoByDayGoods")
	protected @ResponseBody
	List<AppointmentDTO> listApoByDayAdminGoods(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("selectedDate") String selectedDate,  @RequestParam("selectedTasks") String selectedTasks, @RequestParam("selectedTasksCount") String selectedTasksCount, @RequestParam("selectedCalendars") String selectedCalendars, @RequestParam("numLines") int numLines, @RequestParam("numPallets") int numPallets) throws Exception {
		List<Long> listCalendarCandidate = getCalendarsId(selectedCalendars);
		return listApoByDay (arg0, localId, selectedDate, getListLocalTasksGoods(numLines,numPallets,localId,RequestContextUtils.getLocale(arg0)), listCalendarCandidate, true, true);
	}
	
	@RequestMapping("/booking/listApoByDayGoods")
	protected @ResponseBody
	List<AppointmentDTO> listApoByDayGoods(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("selectedDate") String selectedDate,  @RequestParam("selectedTasks") String selectedTasks, @RequestParam("selectedTasksCount") String selectedTasksCount, @RequestParam("selectedCalendars") String selectedCalendars, @RequestParam("numLines") int numLines, @RequestParam("numPallets") int numPallets) throws Exception {
		List<Long> listCalendarCandidate = getCalendarsId(selectedCalendars);
		return listApoByDay (arg0, localId, selectedDate, getListLocalTasksGoods(numLines,numPallets,localId,RequestContextUtils.getLocale(arg0)), listCalendarCandidate, false, true);
	}
	
	@RequestMapping("/booking/listNextDayWithApo")
	protected @ResponseBody
	List<String> listNextDayWithApo(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("selectedDate") String selectedDate,  @RequestParam("selectedTasks") String selectedTasks, @RequestParam("selectedTasksCount") String selectedTasksCount, @RequestParam("selectedCalendars") String selectedCalendars) throws Exception {
		List<LocalTaskDTO> listLocalTaskCombi = iLocalTaskManager.getLocalTaskCombi(new Long(localId), RequestContextUtils.getLocale(arg0).getLanguage(), "");
		List<Long> listCalendarCandidate = getCalendarsId(selectedCalendars);
		List<String> result = new ArrayList<>();
		List<AppointmentDTO> resultApo;
		
		String[] dates = selectedDate.split(CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.HOUR_OF_DAY,0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND,0);
		calendarGreg.set(Calendar.MILLISECOND,0);
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));

		// Vamos NUM_DAYS_NEXT_APO días atrás
		calendarGreg.add(Calendar.DAY_OF_YEAR, -NUM_DAYS_NEXT_APO);
		String selectedDateParam;
		int daysNext = (NUM_DAYS_NEXT_APO *2) +1;
		// Recorremos NUM_DAYS_NEXT_APO*2+1 dias, excepto el dia de hoy
		for (int i = 1; i <= daysNext; i++) {
			selectedDateParam = calendarGreg.get(Calendar.YEAR) + CHAR_SEP_DATE + (calendarGreg.get(Calendar.MONTH)+1) + CHAR_SEP_DATE + calendarGreg.get(Calendar.DAY_OF_MONTH);
			if (!selectedDateParam.equals(selectedDate)){ 
				resultApo = listApoByDay (arg0, localId, selectedDateParam, getListLocalTasks(selectedTasks,selectedTasksCount,listLocalTaskCombi), listCalendarCandidate, false, false, true);
				if (resultApo!=null && resultApo.size()> 0){
					result.add(selectedDateParam);
				}
			}
			calendarGreg.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}
	
	@RequestMapping("/booking/listNextDayWithApoSP")
	protected @ResponseBody
	List<String> listNextDayWithApoSP(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("selectedDate") String selectedDate,  @RequestParam("selectedTasks") String selectedTasks, @RequestParam("selectedTasksCount") String selectedTasksCount, @RequestParam("selectedCalendars") String selectedCalendars) throws Exception {
		List<Long> listCalendarCandidate = getCalendarsId(selectedCalendars);
		List<String> result = new ArrayList<>();
		List<AppointmentDTO> resultApo;
		
		String[] dates = selectedDate.split(CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.HOUR_OF_DAY,0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND,0);
		calendarGreg.set(Calendar.MILLISECOND,0);
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
				
		// Vamos NUM_DAYS_NEXT_APO días atrás
		calendarGreg.add(Calendar.DAY_OF_YEAR, -NUM_DAYS_NEXT_APO);
		String selectedDateParam;
		int daysNext = (NUM_DAYS_NEXT_APO *2) +1;
		// Recorremos NUM_DAYS_NEXT_APO*2+1 dias, excepto el dia de hoy
		for (int i = 1; i <= daysNext; i++) {
			selectedDateParam = calendarGreg.get(Calendar.YEAR) + CHAR_SEP_DATE + (calendarGreg.get(Calendar.MONTH)+1) + CHAR_SEP_DATE + calendarGreg.get(Calendar.DAY_OF_MONTH);
			if (!selectedDateParam.equals(selectedDate)){ 
				resultApo = listApoByDay (arg0, localId, selectedDateParam, getListLocalTasks(selectedTasks), listCalendarCandidate, false, true, true);
				if (resultApo!=null && resultApo.size()> 0){
					result.add(selectedDateParam);
				}
			}
			calendarGreg.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}
	
	@RequestMapping("/booking/listNextDayWithApoGoods")
	protected @ResponseBody
	List<String> listNextDayWithApoGoods(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("selectedDate") String selectedDate,  @RequestParam("selectedTasks") String selectedTasks, @RequestParam("selectedTasksCount") String selectedTasksCount, @RequestParam("selectedCalendars") String selectedCalendars, @RequestParam("numLines") int numLines, @RequestParam("numPallets") int numPallets) throws Exception {
		List<Long> listCalendarCandidate = getCalendarsId(selectedCalendars);
		List<String> result = new ArrayList<>();
		List<AppointmentDTO> resultApo; 
		
		String[] dates = selectedDate.split(CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.HOUR_OF_DAY,0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND,0);
		calendarGreg.set(Calendar.MILLISECOND,0);
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
				
		// Vamos NUM_DAYS_NEXT_APO días atrás
		calendarGreg.add(Calendar.DAY_OF_YEAR, -NUM_DAYS_NEXT_APO);
		String selectedDateParam;
		int daysNext = (NUM_DAYS_NEXT_APO *2) +1;
		// Recorremos NUM_DAYS_NEXT_APO*2+1 dias, excepto el dia de hoy
		for (int i = 1; i <= daysNext; i++) {
			selectedDateParam = calendarGreg.get(Calendar.YEAR) + CHAR_SEP_DATE + (calendarGreg.get(Calendar.MONTH)+1) + CHAR_SEP_DATE + calendarGreg.get(Calendar.DAY_OF_MONTH);
			if (!selectedDateParam.equals(selectedDate)){ 
				resultApo = listApoByDay (arg0, localId, selectedDateParam, getListLocalTasksGoods(numLines,numPallets,localId,RequestContextUtils.getLocale(arg0)), listCalendarCandidate, false, true, true);
				if (resultApo!=null && resultApo.size()> 0){
					result.add(selectedDateParam);
				}
			}
			calendarGreg.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}
	
	// Obtenemos lista de Id Calendars a partir del parametro del servicio
	public List<Long> getCalendarsId (String selectedCalendars){
		if (selectedCalendars==null || selectedCalendars.length()==0){
			return null;
		}
		List<Long> listCalendarsId = new ArrayList<Long>();
		String[] a = selectedCalendars.split(",");
		for (String strId : a) {
			listCalendarsId.add(Long.parseLong(strId));
		}
		return listCalendarsId;
	}
	
	// Detectamos grupos de tareas individuales que son tarea combinada
	public List<LocalTaskDTO> getLocalTasksMatchCombi(List<LocalTaskDTO> listLocalTasks, List<LocalTaskDTO> listLocalTaskCombi){
		
		List<LocalTaskDTO> listTaskSimple = new ArrayList<LocalTaskDTO>();
		
		for (LocalTaskDTO localTask : listLocalTasks) {
			if (localTask.getLotTaskCombiId()==null){ // Solo las simples
				listTaskSimple.add(localTask);
			}
		}
		
		orderingLocalTasksMatchCombi(listTaskSimple.size(), new ArrayList<Integer>(), listLocalTasks, listTaskSimple, listLocalTaskCombi);
		
		return listLocalTasks;
	}
	
	// Si detectamos grupos de tareas individuales que son tarea combinada, las sustituimos por esta
	protected void orderingLocalTasksMatchCombi(int num, List<Integer> listIndx, List<LocalTaskDTO> listLocalTasks, List<LocalTaskDTO> listTaskSimple, List<LocalTaskDTO> listLocalTaskCombi){
		
		if (listIndx.size()<num){
			for (int i=0;i<num;i++){
				if (!listIndx.contains(i)){
					ArrayList<Integer> listIndxAux = new ArrayList<Integer>();
					listIndxAux.addAll(listIndx);
					listIndxAux.add(i);
					orderingLocalTasksMatchCombi(num, listIndxAux, listLocalTasks, listTaskSimple, listLocalTaskCombi);
				}
			}
		}
		if (listIndx.size()>1){
			for (LocalTaskDTO localTaskCombi : listLocalTaskCombi) { // Lista de combinaciones
				List<Long> combi = localTaskCombi.getLotTaskCombiId(); // Lista de tareas de la combinacion
				/* La combinación coincide con la conjugacion de tareas: 
				 * es de la misma longitud y todas las tareas de la conjugacion están incluidas en la combinacion
				 */
				if (combi.size()==listIndx.size()){
					boolean res = true;
					int i = 0;
					Integer posListIndx = null;
					while (res && i<listIndx.size()) {
						posListIndx = (Integer)listIndx.get(i);
						res = combi.contains(listTaskSimple.get(posListIndx).getId());
						i++;
					}
					if (res){ // Si encontramos una coincidencia de combinacion
						// Eliminamos las tareas de la lista y las sustituimos por la combinacion
						boolean first = true;
						for (Integer posListIndx2 : listIndx) {
							if (first){
								i = listLocalTasks.indexOf(listTaskSimple.get(posListIndx2));
								if (i>=0){
									listLocalTasks.set(i,localTaskCombi);
								}
								first = false;
							} else {
								listLocalTasks.remove(listTaskSimple.get(posListIndx2));
							}	
						}
					}
				}
			}
		}
	}
	
	/*
	 * Reordenamos y obtenemos la lista de tareas individuales a reservar, una cita por cada una.
	 * Por cada persona: 
	 *    Obtenemos las tareas seleccionadas
	 *    Detectamos grupos de tareas individuales que son tarea combinada, y las sustituimos por esta
	 *    Ordenamos las tareas por clases
	 *    Añadimos las tareas individules, las individules de las combinadas y sus huecos 
	 */
	public List<List<LocalTaskDTO>> getListLocalTasks(String selectedTasks, String selectedTasksCount, List<LocalTaskDTO> listLocalTaskCombi){
		String[] aCount = selectedTasksCount.split(",");
		String[] a = selectedTasks.split(",");
		List<List<LocalTaskDTO>> listResult = new ArrayList<List<LocalTaskDTO>>();
		List<LocalTaskDTO> list = null;
		List<LocalTaskDTO> listTasksMatchCombi = null;

		LocalTaskDTO localTaskAux = null;

		int aux = 0;
		// Obtenemos las tareas seleccionadas por cada persona
		for (String strCount : aCount) {
			int numTasksPer = Integer.parseInt(strCount);
			listTasksMatchCombi = new ArrayList<LocalTaskDTO>();
			for (int h=0;h<numTasksPer;h++){
				String strTaskId = a[aux];
				aux++;
				localTaskAux = iLocalTaskManager.getById(new Long(strTaskId));
				listTasksMatchCombi.add(localTaskAux);
			}	
			// Detectamos grupos de tareas individuales que son tarea combinada, y las sustituimos por esta
			getLocalTasksMatchCombi(listTasksMatchCombi, listLocalTaskCombi);
			// Ordenamos las tareas por clases
			Collections.sort(listTasksMatchCombi, new SortByClassTask());
			// Añadimos las tareas individules, las individules de las combinadas y sus huecos 
			list = new ArrayList<LocalTaskDTO>();
			for (LocalTaskDTO localTask : listTasksMatchCombi) {
				List<Long> combi = localTask.getLotTaskCombiId();
				if (combi!=null){
					List<Integer> combiRes = localTask.getLotTaskCombiRes();
					int indx = 0;
					int res = 0;
					for (Long localTaskComId : combi) {
						localTask = iLocalTaskManager.getById(localTaskComId);
						if (indx>0){// la primera nunca es hueco
							res = combiRes.get(indx-1);
							if (res>0){ // Si hay espacio entre tareas
								localTaskAux = new LocalTaskDTO();
								localTaskAux.setLotTaskDuration(res);
								list.add(localTaskAux);
							}
						}
						// Nos aseguramos que prevale el hueco del combinado y no el del servicio individual (caso de no ser el último servicio del combinado)
						if(indx<combi.size()-1){
							localTask.setLotTaskPost(0);
						}
						list.add(localTask);
						indx++;
					}
				} else {
					list.add(localTask);
				}
			}	
			listResult.add(list);
		}
		return listResult;
	}
	
	/*
	 * Es una sola tarea.
	 */
	public List<List<LocalTaskDTO>> getListLocalTasks(String selectedTasks){
		List<List<LocalTaskDTO>> listResult = new ArrayList<List<LocalTaskDTO>>();
		List<LocalTaskDTO> list = new ArrayList<LocalTaskDTO>();
		LocalTaskDTO localTask = iLocalTaskManager.getById(new Long(selectedTasks));
		//if (lotTaskDuration!=null){
			//localTask.setLotTaskDuration(lotTaskDuration);
		//}	
		list.add(localTask);
		listResult.add(list);
		return listResult;
	}
	
	/*
	 * Es Adveo.
	 */
	public List<List<LocalTaskDTO>> getListLocalTasksGoods(int numLines, int numPallets, String localId, Locale locale){
		double coefTime = 2.5;
		List<List<LocalTaskDTO>> listResult = new ArrayList<List<LocalTaskDTO>>();
		List<LocalTaskDTO> list = new ArrayList<LocalTaskDTO>();

		// Es furgoneta
		String nameKey = LocalTaskManager.KEY_MULTI_LOCAL_TASK_NAME+localId+"_de 1 - 2 toneladas";
		double valueTime = coefTime*numLines;
		if (numLines < numPallets){ // Es trailer
			nameKey = LocalTaskManager.KEY_MULTI_LOCAL_TASK_NAME+localId+"_hasta 1 tonelada";
			valueTime = coefTime*numPallets;
		}
		LocalTaskDTO localTask = iLocalTaskManager.getByName(nameKey);
		localTask.setLotDefault(localTask.getLotTaskDuration()); // PROVISIONAL: se guarda el tiempo entre citas en lotDefault, que a su vez viene predeterminado por lotTaskDuration
		localTask.setLotTaskDuration(round(valueTime,0)); // PROVISIONAL: se guarda la duración de la cita calculada en lotTaskDuration
		String nameAux = messageSourceApp.getMessage("label.template.numLines", null, locale) +": "+numLines;
		nameAux += " - "+messageSourceApp.getMessage("label.template.numPallets", null, locale) +": "+numPallets;
		localTask.setLotName(nameAux); // PROVISIONAL: se guarda el numLines y numPallets en LotName
		list.add(localTask);
		listResult.add(list);
		return listResult;
	}
	
	public int round (double num, int dec) {
		BigDecimal res = new BigDecimal(num).setScale(dec, BigDecimal.ROUND_UP);
		return res.intValue();
	}
	
	public List<Map<String,Object>> getListCalendarOpen(LocalDTO local, String selectedDate, List<Long> listCalendarCandidate){
		
		int dayWeek = getWeekDay(selectedDate);
		
		List<Map<String,Object>> listCalendarOpen = new ArrayList<Map<String,Object>>();
		Map<String,Object> calendarOpenMap = null;
		
		List<CalendarDTO> listCalendar = iCalendarManager.getCalendar(local.getId());
		DiaryDTO diaryCalDTO = null;

		AnnualDiaryDTO annualDiaryDTO = null;
		
		boolean open = true;
		for (CalendarDTO calendar : listCalendar) {
			if (listCalendarCandidate==null || listCalendarCandidate.contains(calendar.getId())){
				diaryCalDTO = null;
				open = true;
				// Comprobamos si esta fecha esta señalada en la agenda anual del puesto
				annualDiaryDTO = iAnnualDiaryManager.getAnnualDiaryCalendarByDay(calendar.getId(),selectedDate);
				if (annualDiaryDTO!=null){ // Esta fecha esta señalada en la agenda anual del puesto
					if (annualDiaryDTO.getAnuClosed()==1) { // Puesto cerrado esta fecha
						open = false;
					} else if (annualDiaryDTO.getAnuDayDiary()!=null){
						diaryCalDTO = annualDiaryDTO.getAnuDayDiary(); // añadimos citas siguiendo la agenda de la fecha anual del puesto
					}
				} 
				if (open && diaryCalDTO==null){ // añadimos citas siguiendo la agenda por defecto del día de la semana del puesto
					diaryCalDTO = calendar.getCalSemanalDiary().getDiary(dayWeek);
					for (int closedweekDay : calendar.getCalSemanalDiary().getClosedDiary()) { // Comprobamos que no esta señalada como cerrada en la agenda semanal del puesto
						if (dayWeek==closedweekDay){
							diaryCalDTO = null;
							break;
						}
					}	
				}
				if (diaryCalDTO!=null){
					calendarOpenMap = new HashMap<String,Object>();
					calendarOpenMap.put(CAL, calendar);
					calendarOpenMap.put(DIARY, diaryCalDTO.getDiaTimes());
					listCalendarOpen.add(calendarOpenMap);
				}
			}	
		}
		return listCalendarOpen;
	}
	
	protected List<AppointmentDTO> listApoByDay(HttpServletRequest arg0, String localId, String selectedDate, List<List<LocalTaskDTO>> listLocalTasks, List<Long> listCalendarCandidate, boolean admin) throws Exception {
		return listApoByDay(arg0, localId, selectedDate, listLocalTasks, listCalendarCandidate, admin, false, false);
	}
	
	protected List<AppointmentDTO> listApoByDay(HttpServletRequest arg0, String localId, String selectedDate, List<List<LocalTaskDTO>> listLocalTasks, List<Long> listCalendarCandidate, boolean admin, boolean SP) throws Exception {
		return listApoByDay(arg0, localId, selectedDate, listLocalTasks, listCalendarCandidate, admin, SP, false);
	}
	
	protected List<AppointmentDTO> listApoByDay(HttpServletRequest arg0, String localId, String selectedDate, List<List<LocalTaskDTO>> listLocalTasks, List<Long> listCalendarCandidate, boolean admin, boolean SP, boolean exists) throws Exception {

		// Propiedades de local
		LocalDTO local = iLocalManager.getById(new Long(localId));
		int dayWeek = getWeekDay(selectedDate);
		
		if (validateListApo(arg0, local, selectedDate, dayWeek, exists)){
			
			// Comprobamos si esta fecha esta señalada en la agenda anual del local
			AnnualDiaryDTO annualDiaryDTO = iAnnualDiaryManager.getAnnualDiaryByDay(local.getId(),selectedDate);
			DiaryDTO diaryDTO = null;
			if (annualDiaryDTO!=null){ // añadimos citas siguiendo la agenda de la fecha anual del local
				diaryDTO = annualDiaryDTO.getAnuDayDiary();
			} else { // añadimos citas siguiendo la agenda por defecto del día de la semana del local
				diaryDTO = local.getLocSemanalDiary().getDiary(dayWeek);
			}
			if (diaryDTO==null){
				return null;
			}
			List<Map<String,Object>> listCalendarOpen = getListCalendarOpen(local, selectedDate, listCalendarCandidate);
			
			List<AppointmentDTO> listApoLocal = getAppointments(selectedDate, local, diaryDTO.getDiaTimes(), listCalendarOpen, listLocalTasks, admin, SP, exists);
			return listApoLocal;
		}
		return null;
	}
	
	protected List<AppointmentDTO> getAppointments(String selectedDate, LocalDTO local, List<String> diaTimes, List<Map<String,Object>> listCalendarOpen, List<List<LocalTaskDTO>> listLocalTasks, boolean admin, boolean SP, boolean exists)
	throws Exception {
				
		List<AppointmentDTO> listApo = new ArrayList<AppointmentDTO>();
		
		String[] dates = selectedDate.split(CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.HOUR_OF_DAY,0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND,0);
		calendarGreg.set(Calendar.MILLISECOND,0);
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		
		Date selectedDay = calendarGreg.getTime();

		// Si la fecha seleccionada está entre la fecha mínima y la máxima
		if (validateSelectedDate(selectedDay, local.getLocOpenDays(),admin)){
			
			// Propiedades de firma
			FirmDTO firm = iFirmManager.getById(local.getResFirId());
			boolean selCalAfter = firm.getFirConfig().getConfigLocal().getConfigLocSelCalAfter()==1;
			
			AppointmentDTO apo = null;
			Date startTime = null;
			Date endTime = null;
			Date candidateTime = null;
			
			// Minima fecha: momento actual + tiempo de reserva de eventos - si no hay usuario manager.
			Date minTime = getDateTimeRestricted(local.getLocTimeRestricted(),admin,local.getLocWhere().getWheTimeZone());
						
			calendarGreg.setTime(selectedDay);
			calendarGreg.set(Calendar.SECOND,0);
			calendarGreg.set(Calendar.MILLISECOND,0);
			
			List<EventDTO> listEvents = null;
			for (Map<String,Object> calendarOpen : listCalendarOpen) {
				listEvents = iEventManager.getEventByDay((CalendarDTO)calendarOpen.get(CAL), selectedDate);
				if (firm.getFirConfig().getConfigLocal().getConfigLocRepeat()==1){
					List<RepeatDTO> listRepeatLocal = repeatController.listCalendarByDay(((CalendarDTO)calendarOpen.get(CAL)).getId(), selectedDate);
					listEvents.addAll(listRepeatLocal);
				}
				calendarOpen.put(EVENTS, listEvents);
			}
			
			if (firm.getFirDomain().equals("adveo")){
				local.setLocApoDuration(listLocalTasks.get(0).get(0).getLotDefault());
			}
			
			for (String date : diaTimes) {
				if (startTime==null){
					String[] hours = date.split(CHAR_SEP_DATE_HOUR);
					String hour = hours[0];
					String min = hours[1];
					calendarGreg.set(Calendar.HOUR_OF_DAY, new Integer(hour));
					calendarGreg.set(Calendar.MINUTE, new Integer(min));
					startTime = calendarGreg.getTime();
				} else {
						
					String[] hours = date.split(CHAR_SEP_DATE_HOUR);
					String hour = hours[0];
					String min = hours[1];
					calendarGreg.set(Calendar.HOUR_OF_DAY, new Integer(hour));
					calendarGreg.set(Calendar.MINUTE, new Integer(min));
					endTime = calendarGreg.getTime();
					
					calendarGreg.setTime(startTime);
					calendarGreg.set(Calendar.SECOND,0);
					calendarGreg.set(Calendar.MILLISECOND,0);
					candidateTime = calendarGreg.getTime();
					
					while(candidateTime.before(endTime)){
						
						// Minima fecha: momento actual mas tiempo de reserva de eventos
						if(minTime==null || candidateTime.after(minTime)){ 
							// Si SP, apalicable Si solo hay una persona, y solo tiene un servicio
							if (SP){
								if (existsSpaceTasksSP(listLocalTasks.get(0), listCalendarOpen, candidateTime, local.getLocApoDuration(), endTime, false, selCalAfter)){
									// Lista de eventos de esta reserva
									listEvents = new ArrayList<EventDTO>();
									for (Map<String,Object> calendarOpen : listCalendarOpen) {
										// Añadimos los eventos de esta reserva
										listEvents.addAll((List<EventDTO>)calendarOpen.get(EVENTS_APO));
									}
									Calendar calendarGregApo = new GregorianCalendar();
									calendarGregApo.setTime(candidateTime);
									for (EventDTO eventDTO : listEvents) {
										calendarGregApo.setTime(eventDTO.getEveStartTime());
										apo = new AppointmentDTO();
										apo.setApoName(StringUtils.leftPad(""+calendarGregApo.get(Calendar.HOUR_OF_DAY), 2, "0")
												+CHAR_SEP_DATE_HOUR+
												StringUtils.leftPad(""+calendarGregApo.get(Calendar.MINUTE), 2, "0"));
										apo.setApoStartTime(calendarGregApo.getTime());
										apo.setApoCalendarId(eventDTO.getEveCalendarId());
										apo.setApoCalendarName(eventDTO.getEveCalendarName());
										listApo.add(apo);
										// Si solo estamos buscando si hay huecos libres ya podemos retornar
										if (exists){
											return listApo;
										}
									}	
								}
							} else {
								if (existsSpaceTasks(listLocalTasks, listCalendarOpen, candidateTime, local.getLocApoDuration(), endTime, false)){
													
									// Lista de eventos de esta reserva
									listEvents = new ArrayList<EventDTO>();
									for (Map<String,Object> calendarOpen : listCalendarOpen) {
										// Añadimos los eventos de esta reserva
										listEvents.addAll((List<EventDTO>)calendarOpen.get(EVENTS_APO));
									}
									Calendar calendarGregApo = new GregorianCalendar();
									calendarGregApo.setTime(candidateTime);
									if (!listEvents.isEmpty()){
										// Buscamos la hora menor
										Collections.sort(listEvents, new SortByStartTime());
										calendarGregApo.setTime(listEvents.get(0).getEveStartTime());
									}
									apo = new AppointmentDTO();
									apo.setApoName(StringUtils.leftPad(""+calendarGregApo.get(Calendar.HOUR_OF_DAY), 2, "0")
												+CHAR_SEP_DATE_HOUR+
												StringUtils.leftPad(""+calendarGregApo.get(Calendar.MINUTE), 2, "0"));
									apo.setApoStartTime(calendarGregApo.getTime());
									listApo.add(apo);
									// Si solo estamos buscando si hay huecos libres ya podemos retornar
									if (exists){
										return listApo;
									}
								}
							}	
						}
						calendarGreg.add(Calendar.MINUTE, local.getLocApoDuration());
						candidateTime = calendarGreg.getTime();
					}
					startTime = null;
				}
				
			}
		}
		return listApo;
	}
	
	public boolean existsSpaceTasks(List<List<LocalTaskDTO>> listLocalTasks, List<Map<String,Object>> listCalendarOpen, Date candidateTime, int locApoDuration, Date endTime, boolean admin){
		
		List<List<Integer>> combiOrderLocalTasks = new ArrayList<List<Integer>>();
		orderingListLocalTasks(listLocalTasks.size(), new ArrayList<Integer>(), combiOrderLocalTasks);
		
		Calendar bookingTimeNext = null;
		WrapInt calendarIndx = null;
		List<List<LocalTaskDTO>> listLocalTasksOrder = null;
		boolean valid = false;
		// Lo intentamos con todas las combinaciones posibles de orden de personas
		for (List<Integer> listOrder : combiOrderLocalTasks) {
			valid = true;
			
			for (Map<String,Object> calendarOpen : listCalendarOpen) {
				calendarOpen.put(EVENTS_APO, new ArrayList<EventDTO>());
			}
			
			// Cuando debería empezar la próxima persona, inicializamos a candidateTime
			bookingTimeNext = new GregorianCalendar(); 
			bookingTimeNext.setTime(candidateTime);
			bookingTimeNext.set(Calendar.SECOND,0);
			bookingTimeNext.set(Calendar.MILLISECOND,0);
			
			calendarIndx = new WrapInt(0); 
		
			listLocalTasksOrder = new ArrayList<List<LocalTaskDTO>>();
			for (Integer order : listOrder) {
				listLocalTasksOrder.add(listLocalTasks.get(order));
			}
			
			for (List<LocalTaskDTO> localTaskListPerson : listLocalTasksOrder){
				if (!existsSpace(localTaskListPerson, listCalendarOpen, candidateTime, bookingTimeNext, endTime, calendarIndx, locApoDuration, admin)){
					valid = false;
					break;
				}
			}
			if (valid){
				break;
			}
		}
		
		return valid;
	}
	
	public boolean existsSpaceTasksSP(List<LocalTaskDTO> listLocalTasks, List<Map<String,Object>> listCalendarOpen, Date candidateTime, int locApoDuration, Date endTime, boolean admin, boolean selCalAfter){
		
		Calendar bookingTimeNext = null;
		WrapInt calendarIndx = new WrapInt(0); 
		
		for (Map<String,Object> calendarOpen : listCalendarOpen) {
			calendarOpen.put(EVENTS_APO, new ArrayList<EventDTO>());
		}
			
		// Inicializamos a candidateTime
		bookingTimeNext = new GregorianCalendar(); 
		bookingTimeNext.setTime(candidateTime);
		bookingTimeNext.set(Calendar.SECOND,0);
		bookingTimeNext.set(Calendar.MILLISECOND,0);
		
		return existsSpace(listLocalTasks, listCalendarOpen, candidateTime, bookingTimeNext, endTime, calendarIndx, locApoDuration, admin, true, selCalAfter);
	}
	
	protected void orderingListLocalTasks(int num, List<Integer> listIndx, List<List<Integer>> listIndxReturn){
		if (listIndx.size()<num){
			for (int i=0;i<num;i++){
				if (!listIndx.contains(i)){
					ArrayList<Integer> listIndxAux = new ArrayList<Integer>();
					listIndxAux.addAll(listIndx);
					listIndxAux.add(i);
					orderingListLocalTasks(num, listIndxAux, listIndxReturn);
				}
			}
		}
		if (listIndx.size()==num){
			listIndxReturn.add(listIndx);
		}
	}

	public boolean existsSpace(List<LocalTaskDTO> localTaskListPerson, List<Map<String,Object>> listCalendarOpen, Date candidateTime, Calendar bookingTimeNext, Date endTime, WrapInt calendarIndx, int locApoDuration, boolean admin){
		return existsSpace(localTaskListPerson, listCalendarOpen, candidateTime, bookingTimeNext, endTime, calendarIndx, locApoDuration, admin, false, false);
	}
	
	public boolean existsSpace(List<LocalTaskDTO> localTaskListPerson, List<Map<String,Object>> listCalendarOpen, Date candidateTime, Calendar bookingTimeNext, Date endTime, WrapInt calendarIndx, int locApoDuration, boolean admin, boolean SP, boolean selCalAfter){

		List<EventDTO> listEvents = null;
		Date apoStartTime = null;
		Date apoEndTime = null;
		Date apoEndTimeFin = null;
		boolean valid = false;
		
		Calendar maxSpaceTimeGreg = new GregorianCalendar();
		maxSpaceTimeGreg.setTime(candidateTime);
		while (!maxSpaceTimeGreg.getTime().after(bookingTimeNext.getTime())){
			maxSpaceTimeGreg.add(Calendar.MINUTE, locApoDuration);
		}
		
		Date maxSpaceTime = maxSpaceTimeGreg.getTime();
		
		if (endTime!=null && maxSpaceTime.after(endTime)){
			maxSpaceTime = endTime;
		}
		
		// Reseteamos la lista de eventos provisionales de esta persona con la lista de eventos de esta reserva 
		for (Map<String,Object> calendarOpen : listCalendarOpen) {
			listEvents = new ArrayList<EventDTO>();
			listEvents.addAll((List<EventDTO>)calendarOpen.get(EVENTS_APO));
			calendarOpen.put(EVENTS_PROV, listEvents);
		}
		
		/* Para la primera localTask de esta persona:
		   Comprobamos los huecos ordenados por hora entre:
		  - la hora candidata (candidateTime) 
		  - y la hora de reserva que le tocaría a la persona contando hasta la proxima cita candidata (maxSpaceTime) 
		*/
		List<Date> listScanDate = new ArrayList<Date>();
		Calendar bookingTimePerson = new GregorianCalendar();
		listScanDate = scaningSpace (candidateTime, maxSpaceTime, endTime, localTaskListPerson.get(0), listCalendarOpen, calendarIndx, admin);
		if (listScanDate.isEmpty()){
			return false;
		}
		for (Date scanDate : listScanDate) {
			
			bookingTimePerson.setTime(scanDate);
			valid = true;
		
			for (LocalTaskDTO localTaskPerson : localTaskListPerson){
				if (localTaskPerson.getId() != null){ // No es un hueco	
					
					apoStartTime = bookingTimePerson.getTime();
					bookingTimePerson.add(Calendar.MINUTE, localTaskPerson.getLotTaskDuration());
					apoEndTime = bookingTimePerson.getTime();
					
					// Comprobamos que la hora de fin de la cita no ha pasado la hora de cierre
					apoEndTimeFin = apoEndTime;
					if (localTaskPerson.getLotTaskPost()!=null && localTaskPerson.getLotTaskPost()>0){ // Sumamos el posible tiempo extra del postservicio, que no ocupa tiempo del calendario pero tiene que estar dentro de la hora de cierre
						Calendar endTimeCal = new GregorianCalendar();
						endTimeCal.setTime(apoEndTimeFin);
						endTimeCal.add(Calendar.MINUTE, localTaskPerson.getLotTaskPost());
						apoEndTimeFin = endTimeCal.getTime();
					}
					if ((endTime!=null && apoEndTimeFin.after(endTime))){
						valid = false;
						break;
					} 
					if (!existsSpaceApo (apoStartTime, apoEndTime, localTaskPerson, listCalendarOpen, calendarIndx, admin, SP, selCalAfter)){
						valid = false;
						break;
					}
				} 
				else {
					bookingTimePerson.add(Calendar.MINUTE, localTaskPerson.getLotTaskDuration());
				}
			}
			if (valid){
				break;
			}
			
			// Reseteamos la lista de eventos provisionales de esta persona con la lista de eventos de esta reserva 
			for (Map<String,Object> calendarOpen : listCalendarOpen) {
				listEvents = new ArrayList<EventDTO>();
				listEvents.addAll((List<EventDTO>)calendarOpen.get(EVENTS_APO));
				calendarOpen.put(EVENTS_PROV, listEvents);
			}
			
		}
		if (!valid){
			return false;
		}
		calendarIndx.setValue((calendarIndx.getValue()==listCalendarOpen.size()-1)? 0: calendarIndx.getValue()+1);
		if (bookingTimePerson.getTime().after(bookingTimeNext.getTime())){
			bookingTimeNext.setTime(bookingTimePerson.getTime());
		}
		// Añadimos los eventos de esta reserva
		for (Map<String,Object> calendarOpen : listCalendarOpen) {
			listEvents = new ArrayList<EventDTO>();
			listEvents.addAll((List<EventDTO>)calendarOpen.get(EVENTS_PROV));
			calendarOpen.put(EVENTS_APO, listEvents);
		}
		return true;
	}
		
	protected List<Date> scaningSpace (Date minSpaceTime, Date maxSpaceTime, Date endTime, LocalTaskDTO localTaskPerson, List<Map<String,Object>> listCalendarOpen, WrapInt calendarIndx, boolean admin){
		
		List<Date> listSpaces = new ArrayList<Date>(); 
		
		if (!minSpaceTime.before(maxSpaceTime)){
			return listSpaces;
		}
			
		Map<String,Object> calendarOpen = null;
		CalendarDTO calendar = null;
		// Lista de eventos de esta reserva
		List<EventDTO> listEventsProv = null;
		// Lista de eventos a examinar
		List<EventDTO> listEvents = null;
		Date startSpace = null;
		Date endSpace = null;
		List<EventDTO> listEventsIn = null;

		EventDTO eventFirst = null;
		EventDTO eventSecond = null;
		Date min = minSpaceTime;
		
		List<String> diaTimes = null;
		
		for (int i = 0; i < listCalendarOpen.size(); i++) {
			calendarOpen = listCalendarOpen.get(calendarIndx.getValue());
		 	
			calendar = (CalendarDTO)calendarOpen.get(CAL);
					
			// Comprobamos que este calendario puede hacer esta tarea
			if (calendar.getCalLocalTasksId().contains(localTaskPerson.getId())){
							
				diaTimes = ((List<String>)calendarOpen.get(DIARY));
				
				// Lista de eventos provisionales de esta reserva
				listEventsProv = (List<EventDTO>)calendarOpen.get(EVENTS_PROV);
				
				// Lista de eventos a examinar
				listEvents = new ArrayList<EventDTO>();
				// Añadimos los eventos de en otras reservas
				listEvents.addAll((List<EventDTO>)calendarOpen.get(EVENTS));
				// Añadimos los eventos de esta reserva
				listEvents.addAll(listEventsProv);
				listEventsIn = new ArrayList<EventDTO>();
				for (EventDTO event : listEvents) {
					if (
						((event.getEveStartTime().after(minSpaceTime) || event.getEveStartTime().equals(minSpaceTime)) && event.getEveStartTime().before(maxSpaceTime)) 
						||
						(event.getEveEndTime().after(minSpaceTime) && event.getEveStartTime().before(maxSpaceTime))
					){
						listEventsIn.add(event);
					}
				} 
				 
				Collections.sort(listEventsIn, new SortByStartTime());
	
				if (listEventsIn.isEmpty()){
					if (!listSpaces.contains(minSpaceTime)){
						listSpaces.add(minSpaceTime);
					}
				} else {
					min = minSpaceTime;
					for (int indx = 0; indx < listEventsIn.size(); indx++) {
						eventFirst = (EventDTO)listEventsIn.get(indx);
						if ((indx+1) < listEventsIn.size()){
							eventSecond = (EventDTO)listEventsIn.get(indx+1);
						} else{
							eventSecond = null;
						}	
						if (eventFirst.getEveStartTime().after(min)) {
							startSpace = min;
							endSpace = eventFirst.getEveStartTime();
							if ((endSpace.getTime()-startSpace.getTime()>=localTaskPerson.getLotTaskDuration()*60*1000)){
								if (!listSpaces.contains(startSpace)){
									listSpaces.add(startSpace);
								}	
							}
						} 
						if (eventFirst.getEveEndTime().before(maxSpaceTime)) {
							startSpace = eventFirst.getEveEndTime();
							if (eventSecond!=null){
								endSpace = eventSecond.getEveStartTime();
							} else {
								endSpace = endTime;
							}
							if (endSpace==null || 
									(endSpace.getTime()-startSpace.getTime()>=localTaskPerson.getLotTaskDuration()*60*1000)){
								if (!listSpaces.contains(startSpace)){
									listSpaces.add(startSpace);
								}	
							}
						}
						min = endSpace;
					}	
				}
			}	
			calendarIndx.setValue((calendarIndx.getValue()==listCalendarOpen.size()-1)? 0: calendarIndx.getValue()+1);
		}
		Collections.sort(listSpaces, new SortByTime());

		return listSpaces;
	}
	
	
	protected boolean existsSpaceApo (Date apoStartTime, Date apoEndTime, LocalTaskDTO localTaskPerson, List<Map<String,Object>> listCalendarOpen, WrapInt calendarIndx, boolean admin, boolean SP, boolean selCalAfter){
		if (SP){
			return existsSpaceApoSP (apoStartTime, apoEndTime, localTaskPerson, listCalendarOpen, calendarIndx, admin, selCalAfter);
		}
		Date apoEndTimeFin = null;
		Map<String,Object> calendarOpen = null;
		CalendarDTO calendar = null;
		List<String> diaTimes = null;
		boolean valid = false;
		// Lista de eventos de esta reserva
		List<EventDTO> listEventsProv = null;
		// Lista de eventos a examinar
		List<EventDTO> listEvents = null;
		for (int i = 0; i < listCalendarOpen.size(); i++) {
			calendarOpen = listCalendarOpen.get(calendarIndx.getValue());
							 	
			valid = false;
			calendar = (CalendarDTO)calendarOpen.get(CAL);
							
			// Comprobamos que este calendario puede hacer esta tarea
			if (calendar.getCalLocalTasksId().contains(localTaskPerson.getId())){
				
				// Comprobamos que este calendario está abierto en este ahora
				diaTimes = ((List<String>)calendarOpen.get(DIARY));
				apoEndTimeFin = apoEndTime;
				if (localTaskPerson.getLotTaskPost()!=null && localTaskPerson.getLotTaskPost()>0){ // Sumamos el posible tiempo extra del postservicio, que no ocupa tiempo del calendario pero tiene que estar dentro de la hora de cierre del calendario
					Calendar endTimeCal = new GregorianCalendar();
					endTimeCal.setTime(apoEndTimeFin);
					endTimeCal.add(Calendar.MINUTE, localTaskPerson.getLotTaskPost());
					apoEndTimeFin = endTimeCal.getTime();
				}
				if (validateCalendarHours(diaTimes, apoStartTime, apoEndTimeFin, admin)){
								
					// Lista de eventos de esta reserva
					listEventsProv = (List<EventDTO>)calendarOpen.get(EVENTS_PROV);
					
					// Lista de eventos a examinar
					listEvents = new ArrayList<EventDTO>();
					// Añadimos los eventos de en otras reservas
					listEvents.addAll((List<EventDTO>)calendarOpen.get(EVENTS));
					// Añadimos los eventos de esta reserva
					listEvents.addAll(listEventsProv);
					
					valid = true;
					for (EventDTO event : listEvents) {
					
						if (apoEndTime.before(event.getEveStartTime())){ // Si la hora de fin de la cita es antes que la hora de la lista ordenada
							break; // No comparamos más
						}
				
						if (
								((apoStartTime.after(event.getEveStartTime()) || apoStartTime.equals(event.getEveStartTime())) && apoStartTime.before(event.getEveEndTime())) 
								||
								(apoEndTime.after(event.getEveStartTime()) && apoStartTime.before(event.getEveEndTime()))
							){
							valid = false;
							break;
						}
					}
					if (valid){
						EventDTO event = new EventDTO();
						event.setEveCalendarId(calendar.getId());
						event.setEveStartTime(apoStartTime);
						event.setEveEndTime(apoEndTime);
						event.setEveEndTimeShow(apoEndTimeFin);
						event.setEveLocalTask(localTaskPerson);
						listEventsProv.add(event);
						break;
					}
				}	
			}	
			calendarIndx.setValue((calendarIndx.getValue()==listCalendarOpen.size()-1)? 0: calendarIndx.getValue()+1);
		}
		return valid;
	}
	
	protected boolean existsSpaceApoSP (Date apoStartTime, Date apoEndTime, LocalTaskDTO localTaskPerson, List<Map<String,Object>> listCalendarOpen, WrapInt calendarIndx, boolean admin, boolean selCalAfter){
		
		Date apoEndTimeFin = null;
		Map<String,Object> calendarOpen = null;
		CalendarDTO calendar = null;
		List<String> diaTimes = null;
		boolean valid = false;
		boolean validCalendar = false;
		// Lista de eventos de esta reserva
		List<EventDTO> listEventsProv = null;
		// Lista de eventos a examinar
		List<EventDTO> listEvents = null;
		for (int i = 0; i < listCalendarOpen.size(); i++) {
			calendarOpen = listCalendarOpen.get(calendarIndx.getValue());
							 	
			validCalendar = false;
			calendar = (CalendarDTO)calendarOpen.get(CAL);
							
			// Comprobamos que este calendario puede hacer esta tarea
			if (calendar.getCalLocalTasksId().contains(localTaskPerson.getId())){
				
				// Comprobamos que este calendario está abierto en este ahora
				diaTimes = ((List<String>)calendarOpen.get(DIARY));
				apoEndTimeFin = apoEndTime;
				if (localTaskPerson.getLotTaskPost()!=null && localTaskPerson.getLotTaskPost()>0){ // Sumamos el posible tiempo extra del postservicio, que no ocupa tiempo del calendario pero tiene que estar dentro de la hora de cierre del calendario
					Calendar endTimeCal = new GregorianCalendar();
					endTimeCal.setTime(apoEndTimeFin);
					endTimeCal.add(Calendar.MINUTE, localTaskPerson.getLotTaskPost());
					apoEndTimeFin = endTimeCal.getTime();
				}
				if (validateCalendarHours(diaTimes, apoStartTime, apoEndTimeFin, admin)){
								
					// Lista de eventos de esta reserva
					listEventsProv = (List<EventDTO>)calendarOpen.get(EVENTS_PROV);
					
					// Lista de eventos a examinar
					listEvents = new ArrayList<EventDTO>();
					// Añadimos los eventos de en otras reservas
					listEvents.addAll((List<EventDTO>)calendarOpen.get(EVENTS));
					// Añadimos los eventos de esta reserva
					listEvents.addAll(listEventsProv);
					
					validCalendar = true;
					for (EventDTO event : listEvents) {
					
						if (apoEndTime.before(event.getEveStartTime())){ // Si la hora de fin de la cita es antes que la hora de la lista ordenada
							break; // No comparamos más
						}
				
						if (
								((apoStartTime.after(event.getEveStartTime()) || apoStartTime.equals(event.getEveStartTime())) && apoStartTime.before(event.getEveEndTime())) 
								||
								(apoEndTime.after(event.getEveStartTime()) && apoStartTime.before(event.getEveEndTime()))
							){
							validCalendar = false;
							break;
						}
					}
					if (validCalendar){
						EventDTO event = new EventDTO();
						event.setEveCalendarId(calendar.getId());
						event.setEveStartTime(apoStartTime);
						event.setEveEndTime(apoEndTime);
						event.setEveEndTimeShow(apoEndTimeFin);
						event.setEveLocalTask(localTaskPerson);
						event.setEveCalendarName(calendar.getCalName());
						listEventsProv.add(event);
						valid = true;
						if (!selCalAfter){
							break; //hacemos break porque ya no buscamos mas calendars
						}
					}
				}	
			}	
			calendarIndx.setValue((calendarIndx.getValue()==listCalendarOpen.size()-1)? 0: calendarIndx.getValue()+1);
		}
		return valid;
	}
	
	protected boolean validateCalendarHours (List<String> diaTimes, Date eveStartTime, Date eveEndTime, boolean admin){
		
		if (admin) { // Si hay usuario manager entonces no hay restricción de hora
			return true;
		}
		
		Date startTime = null;
		Date endTime = null;
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.setTime(eveStartTime);
		for (String date : diaTimes) {
			if (startTime==null){
				String[] hours = date.split(CHAR_SEP_DATE_HOUR);
				String hour = hours[0];
				String min = hours[1];
				calendarGreg.set(Calendar.HOUR_OF_DAY, new Integer(hour));
				calendarGreg.set(Calendar.MINUTE, new Integer(min));
				startTime = calendarGreg.getTime();
			} else {
				String[] hours = date.split(CHAR_SEP_DATE_HOUR);
				String hour = hours[0];
				String min = hours[1];
				calendarGreg.set(Calendar.HOUR_OF_DAY, new Integer(hour));
				calendarGreg.set(Calendar.MINUTE, new Integer(min));
				endTime = calendarGreg.getTime();
				
				if (
						((eveStartTime.after(startTime) || eveStartTime.equals(startTime)) && eveStartTime.before(endTime)) 
						&&
						(eveEndTime.before(endTime) || eveEndTime.equals(endTime))
					){
					return true;
				}
				startTime = null;
			}
		}
		return false;
	}
		
	
	/*
	 *  Minima fecha: dia de hoy a las 0 horas
	 *  Maxima fecha: dia de hoy a las 0 horas + tiempo de apertura de agenda del local
	 */
	public boolean validateSelectedDate(Date time, int locOpenDays, boolean admin){
		
		if (admin) { // Si hay usuario manager entonces no hay restricción de fecha
			return true;
		}
		
		// Minima fecha: dia de hoy a las 0 horas
		Date minTime = getTodateZero();
		// Maxima fecha: dia de hoy a las 0 horas + tiempo de apertura de agenda del local
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.setTime(minTime);
		calendarGreg.add(Calendar.DAY_OF_YEAR, locOpenDays);
		Date maxTime = calendarGreg.getTime();
		
		// Si la fecha seleccionada está entre la fecha mínima y la máxima
		return (time.after(minTime) || time.equals(minTime)) && (time.before(maxTime) ||time.equals(maxTime));
	}
	
	/*
	 *  Minima fecha: momento actual + tiempo de reserva de eventos
	 *  Maxima fecha: dia de hoy a las 0 horas + tiempo de apertura de agenda del local
	 */
	public boolean validateDate(Date time, int locTimeRestricted, int locOpenDays, boolean admin, String timeZone){
		
		if (admin) { // Si hay usuario manager entonces no hay restricción de fecha
			return true;
		}
		
		// Minima fecha: momento actual + tiempo de reserva de eventos
		Date minTime = getDateTimeRestricted(locTimeRestricted, admin, timeZone);
		
		// Maxima fecha: dia de hoy a las 0 horas + tiempo de apertura de agenda del local
		Date maxTime = getTodateZero();
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.setTime(minTime);
		calendarGreg.add(Calendar.DAY_OF_YEAR, locOpenDays);
		maxTime = calendarGreg.getTime();
		
		// Si la fecha seleccionada está entre la fecha mínima y la máxima
		return (time.after(minTime) || time.equals(minTime)) && (time.before(maxTime) ||time.equals(maxTime));
	}
	
	public Date getDateTimeRestricted (int locTimeRestricted, boolean admin, String timeZone){
		
		if (admin) { // Si hay usuario manager entonces no hay restricción de fecha
			return null;
		}
		
		// Minima fecha: momento actual + tiempo de reserva de eventos
		Calendar minTimeGreg = new GregorianCalendar();
		minTimeGreg.add(Calendar.MINUTE, locTimeRestricted);
		Date minDate = minTimeGreg.getTime();
		// Calculamos el desplazamiento de la zona horaria desde UTC
		TimeZone calendarTimeZone = TimeZone.getTimeZone(timeZone);
		minDate =  new Date(minDate.getTime() + calendarTimeZone.getOffset(minDate.getTime()));
		return minDate;
	}
	
	public Date getTodateZero (){
		// Dia de hoy a las 0 horas
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.HOUR_OF_DAY,0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND,0);
		calendarGreg.set(Calendar.MILLISECOND,0);
		return calendarGreg.getTime();
	}
	
	public int getWeekDay(String strDate) {

		String[] dates = strDate.split(CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		int res = calendarGreg.getTime().getDay() - 1;
		if (res == -1)
			res = 6;
		return res;
	}

	
	/* El nombre y la descripcion no está vacío.
	*/
	protected boolean validateNew(HttpServletRequest arg0, String calName, String calDesc) throws UncategorizedDataAccessException {
		boolean res = true;
		String message = "";
		Locale locale = RequestContextUtils.getLocale(arg0);
		if (calName==null || calName.length()==0){
			message =  messageSourceApp.getMessage("form.error.calendar.nameReq", null, locale);
			res = false;
		} else if (calDesc==null || calDesc.length()==0){
			message =  messageSourceApp.getMessage("form.error.calendar.descReq", null, locale);
			res = false;
		}
		
		if (!res){
			throw new ErrorService(message, null);
		}
		return res;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/manager/new")
	@ResponseStatus(HttpStatus.OK)
	protected void newObject(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		
		String calId = arg0.getParameter("id");
		
		String calName = arg0.getParameter("calName");
	    String calDesc = arg0.getParameter("calDesc");
		
		if (validateNew(arg0, calName, calDesc)){ 	
			
			CalendarDTO calendar = new CalendarDTO();
			
			if (calId!=null){ // Existe
				calendar = iCalendarManager.getById(new Long(calId));
			}
		
			// Propiedades de local
			Long localId = new Long(arg0.getParameter("localId"));
			LocalDTO local = iLocalManager.getById(localId);
			
		    /*	 
		    //String calProfEmail = arg0.getParameter("calProf");
		    
			ProfessionalDTO calProf = new ProfessionalDTO();
			calProf.setEnabled(1);
			calProf.setResFirId(resFirId);
			//calProf.setWhoName(calProfName);
			calProf.setWhoEmail(calProfEmail);
			calProf = professionalDAO.create(calProf);*/
			
		
			if (calId!=null){ // Existe
				
				calendar.setCalName(calName);
				calendar.setCalDesc(calDesc);
				
				//calendar.setCalProf(calProf);

				calendar = iCalendarManager.update(calendar);
				
			} else { // Es nuevo
				
				// Asignamos las tareas del local
				List<LocalTaskDTO> listLocalTaskLocal = iLocalTaskManager.getLocalTaskSimple(localId, locale.getLanguage());
				List<Long> listLocalTask = new ArrayList<Long>();
				for (LocalTaskDTO localTask : listLocalTaskLocal){
					listLocalTask.add(localTask.getId());
				}	
				calendar.setCalLocalTasksId(listLocalTask);
				
				// Asignamos los horarios habituales del local
				DiaryDTO diary = new DiaryDTO();
				diary.setEnabled(1);
				diary.setDiaTimes(local.getLocSemanalDiary().getSemMonDiary().getDiaTimes());
				DiaryDTO diaryCreatedMon = iDiaryManager.create(diary);
				
				diary = new DiaryDTO();
				diary.setEnabled(1);
				diary.setDiaTimes(local.getLocSemanalDiary().getSemTueDiary().getDiaTimes());
				DiaryDTO diaryCreatedTue = iDiaryManager.create(diary);
				
				diary = new DiaryDTO();
				diary.setEnabled(1);
				diary.setDiaTimes(local.getLocSemanalDiary().getSemWedDiary().getDiaTimes());
				DiaryDTO diaryCreatedWed = iDiaryManager.create(diary);
				
				diary = new DiaryDTO();
				diary.setEnabled(1);
				diary.setDiaTimes(local.getLocSemanalDiary().getSemThuDiary().getDiaTimes());
				DiaryDTO diaryCreatedThu = iDiaryManager.create(diary);
				
				diary = new DiaryDTO();
				diary.setEnabled(1);
				diary.setDiaTimes(local.getLocSemanalDiary().getSemFriDiary().getDiaTimes());
				DiaryDTO diaryCreatedFri = iDiaryManager.create(diary);
				
				diary = new DiaryDTO();
				diary.setEnabled(1);
				diary.setDiaTimes(local.getLocSemanalDiary().getSemSatDiary().getDiaTimes());
				DiaryDTO diaryCreatedSat = iDiaryManager.create(diary);
				
				diary = new DiaryDTO();
				diary.setEnabled(1);
				diary.setDiaTimes(local.getLocSemanalDiary().getSemSunDiary().getDiaTimes());
				DiaryDTO diaryCreatedSun = iDiaryManager.create(diary);
				
					
				SemanalDiaryDTO semanalDiary = new SemanalDiaryDTO();
				semanalDiary.setEnabled(1);
				semanalDiary.setSemMonDiary(diaryCreatedMon);
				semanalDiary.setSemTueDiary(diaryCreatedTue);
				semanalDiary.setSemWedDiary(diaryCreatedWed);
				semanalDiary.setSemThuDiary(diaryCreatedThu);
				semanalDiary.setSemFriDiary(diaryCreatedFri);
				semanalDiary.setSemSatDiary(diaryCreatedSat);
				semanalDiary.setSemSunDiary(diaryCreatedSun);
				
				semanalDiary = iSemanalDiaryManager.create(semanalDiary);
				
				calendar.setCalSemanalDiary(semanalDiary);
				
				calendar.setEnabled(1);
				calendar.setResFirId(local.getResFirId());
				calendar.setCalName(calName);
				calendar.setCalDesc(calDesc);
				calendar.setCalLocalId(localId);
		
				//calendar.setCalProf(calProf);
				
				calendar = iCalendarManager.create(calendar);
				
				// Asignamos los horarios especiales y cerrados del local
				List<AnnualDiaryDTO> ListAnnualDiary = iAnnualDiaryManager.getAnnualDiary(local.getId());
				AnnualDiaryDTO annualDiary = null;
				for (AnnualDiaryDTO annualDiaryLocal : ListAnnualDiary) {
					annualDiary = new AnnualDiaryDTO();
					annualDiary.setEnabled(1);
					annualDiary.setAnuCalendarId(calendar.getId());
					annualDiary.setAnuDate(annualDiaryLocal.getAnuDate());
					annualDiary.setAnuClosed(annualDiaryLocal.getAnuClosed());
					if (annualDiaryLocal.getAnuDayDiary()!=null){
						diary = new DiaryDTO();
						diary.setEnabled(1);
						diary.setDiaTimes(annualDiaryLocal.getAnuDayDiary().getDiaTimes());
						annualDiary.setAnuDayDiary(diary);
					}
					iAnnualDiaryManager.create(annualDiary);
				}
			}
		}		
	}
	
	@RequestMapping("/manager/list")
	protected @ResponseBody
	List<CalendarDTO> list(HttpServletRequest arg0, @RequestParam("localId") String localId) throws Exception {

		Locale locale = RequestContextUtils.getLocale(arg0);
		
		LocalDTO local = iLocalManager.getById(new Long(localId));
		
		List<CalendarDTO> listCalendar = iCalendarManager.getCalendarAdmin(local.getId());
		String strCalLabelLocalTasks = null;

		LocalTaskDTO localTask = null;

		MultiTextDTO multiTextKey = null;
		for (CalendarDTO calendar : listCalendar) {
			strCalLabelLocalTasks = "";
			if (calendar.getCalLocalTasksId() != null) {
				for (Long taskId : calendar.getCalLocalTasksId()) {
					if (strCalLabelLocalTasks.length()>0){
						strCalLabelLocalTasks += " , ";
					}
					localTask = iLocalTaskManager.getById(taskId);
					multiTextKey = iMultiTextManager.getByLanCodeAndKey(locale.getLanguage(), localTask.getLotNameMulti());
					strCalLabelLocalTasks +=  multiTextKey.getMulText();
				}
			}
			calendar.setCalLabelLocalTasks(strCalLabelLocalTasks);
		}
		return listCalendar;
	}
	
	@RequestMapping("/list")
	protected @ResponseBody
	List<CalendarDTO> list(@RequestParam("localId") String localId) throws Exception {

		LocalDTO local = iLocalManager.getById(new Long(localId));

		List<CalendarDTO> listCalendar = iCalendarManager.getCalendar(local.getId());
				
		return listCalendar;
	}
	
	@RequestMapping("/operator/listDiary")
	protected @ResponseBody
	List<CalendarDTO> listDiary(@RequestParam("localId") String localId) throws Exception {

		LocalDTO local = iLocalManager.getById(new Long(localId));

		List<CalendarDTO> listCalendar = iCalendarManager.getCalendarAdmin(local.getId());
				
		return listCalendar;
	}
	
	@RequestMapping("/numCals")
	protected @ResponseBody
	Integer numCals(@RequestParam("localId") String localId) throws Exception {
				
		return iCalendarManager.getNumCalendarAdmin(new Long(localId));
	}
	
	@RequestMapping("/listCandidate")
	protected @ResponseBody
	List<CalendarDTO> listCandidate(@RequestParam("localId") String localId, @RequestParam("selectedTasks") String selectedTasks) throws Exception {
		
		List<CalendarDTO> listCalendarResult = new ArrayList<CalendarDTO>();
		
		List<Long> listLocalTask = getListLocalTasksId(selectedTasks);
	
		LocalDTO local = iLocalManager.getById(new Long(localId));
		List<CalendarDTO> listCalendar = iCalendarManager.getCalendar(local.getId());
		boolean isValid = true;
		for (CalendarDTO calendarDTO : listCalendar) {
			isValid = true;
			for (Long idTask : listLocalTask) {
				if (!calendarDTO.getCalLocalTasksId().contains(idTask)){
					isValid = false;
					break;
				}
			}
			if (isValid){
				listCalendarResult.add(calendarDTO);
			}
		}
		return listCalendarResult;
	}
	
	public List<Long> getListLocalTasksId(String selectedTasks){
		List<Long> list = new ArrayList<Long>();
		String[] a = selectedTasks.split(",");
		LocalTaskDTO localTask = null;
		List<Long> combiRes = null;
		for (String strId : a) {
			localTask = iLocalTaskManager.getById(new Long(strId));
			combiRes = localTask.getLotTaskCombiId();
			if (combiRes==null){
				if(!list.contains(localTask.getId())){
					list.add(localTask.getId());
				}
			} else{
				for (Long localTaskComId : combiRes) {
					if(!list.contains(localTaskComId)){
						list.add(localTaskComId);
					}	
				}
			}
		}	
		return list;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/enabled")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	protected void enabled(@RequestParam("id") Long id) throws Exception {
		
		CalendarDTO calendar = iCalendarManager.getById(id);
		if (calendar!=null){
			if (calendar.getEnabled()==1){
				calendar.setEnabled(0);
			} else {
				calendar.setEnabled(1);
			}
			iCalendarManager.update(calendar);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/tasks")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	protected void tasks(@RequestParam("id") Long id, @RequestParam("selectedTasks") String selectedTasks) throws Exception {
		
		CalendarDTO calendar = iCalendarManager.getById(id);
		if (calendar!=null){
			List<Long> listLocalTask = new ArrayList<Long>();
			String[] a = selectedTasks.split(",");
			for (String strTaskId : a) {
				listLocalTask.add(new Long (strTaskId));
			}
			calendar.setCalLocalTasksId(listLocalTask);
			iCalendarManager.update(calendar);
		}
	}
	
	protected class WrapInt{
	    protected int value;
	    
	    WrapInt(int value){
	    	this.value = value;
	    }
	    
		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	    
	}

	protected class SortByClassTask implements Comparator {
		public int compare(Object obj1, Object obj2) {
			Long id1 = null;
			Long id2 = null;
			TaskDTO task1 = null;
			TaskDTO task2 = null;
			if (((LocalTaskDTO)obj1).getLotTaskId()!=null){ // Es individual 
				id1 = ((LocalTaskDTO)obj1).getLotTaskId();
			} else { // Es combinada, cogemos la primera
				id1 = (Long)((LocalTaskDTO)obj1).getLotTaskCombiId().get(0);
				id1 = iLocalTaskManager.getById(id1).getLotTaskId();
			}
			if (((LocalTaskDTO)obj2).getLotTaskId()!=null){// Es individual
				id2 = ((LocalTaskDTO)obj2).getLotTaskId();
			} else { // Es combinada, cogemos la primera
				id2 = (Long)((LocalTaskDTO)obj2).getLotTaskCombiId().get(0);
				id2 = iLocalTaskManager.getById(id2).getLotTaskId();
			}
			task1 = iTaskManager.getById(id1);
			task2 = iTaskManager.getById(id2);
			return (task1.getTasClass().getId())
					.compareTo((task2.getTasClass().getId()));
		}
	}
	
	protected class SortByStartTime implements Comparator {
		public int compare(Object obj1, Object obj2) {
			return (((EventDTO) obj1).getEveStartTime())
					.compareTo(((EventDTO) obj2).getEveStartTime());
		}
	}
	
	protected class SortByTime implements Comparator {
		public int compare(Object obj1, Object obj2) {
			return (((Date) obj1)).compareTo(((Date) obj2));
		}
	}
	
	
	public void setMessageSourceApp(MessageSource messageSourceApp) {
		this.messageSourceApp = messageSourceApp;
	}

	public void setAnnualDiaryDAO(IAnnualDiaryManager iAnnualDiaryManager) {
		this.iAnnualDiaryManager = iAnnualDiaryManager;
	}

	public void setLocalDAO(ILocalManager iLocalManager) {
		this.iLocalManager = iLocalManager;
	}

	public void setCalendarDAO(ICalendarManager iCalendarManager) {
		this.iCalendarManager = iCalendarManager;
	}

	public void setEventDAO(IEventManager iEventManager) {
		this.iEventManager = iEventManager;
	}

	public void setRepeatDAO(IRepeatManager iRepeatManager) {
		this.iRepeatManager = iRepeatManager;
	}

	public void setLocalTaskDAO(ILocalTaskManager iLocalTaskManager) {
		this.iLocalTaskManager = iLocalTaskManager;
	}

	public void setTaskDAO(ITaskManager iTaskManager) {
		this.iTaskManager = iTaskManager;
	}

	public void setDiaryDAO(IDiaryManager iDiaryManager) {
		this.iDiaryManager = iDiaryManager;
	}

	public void setSemanalDiaryDAO(ISemanalDiaryManager iSemanalDiaryManager) {
		this.iSemanalDiaryManager = iSemanalDiaryManager;
	}

	public void setMultiTextDAO(IMultiTextManager iMultiTextManager) {
		this.iMultiTextManager = iMultiTextManager;
	}

	public void setFirmDAO(IFirmManager iFirmManager) {
		this.iFirmManager = iFirmManager;
	}

	public void setRepeatController(RepeatController repeatController) {
		this.repeatController = repeatController;
	}
	
	
}
