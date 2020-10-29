package com.diloso.bookhair.app.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.diloso.bookhair.app.negocio.dao.AnnualDiaryDAO;
import com.diloso.bookhair.app.negocio.dao.CalendarDAO;
import com.diloso.bookhair.app.negocio.dao.DiaryDAO;
import com.diloso.bookhair.app.negocio.dao.EventDAOGoogle;
import com.diloso.bookhair.app.negocio.dao.LocalDAO;
import com.diloso.bookhair.app.negocio.dto.AnnualDiaryDTO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

@Controller
@RequestMapping(value={"/*/annual", "/annual"})
public class AnnualController {
	
	//@Autowired
	protected AnnualDiaryDAO annualDiaryDAO;
	
	//@Autowired
	protected LocalDAO localDAO;
	
	//@Autowired
	protected CalendarDAO calendarDAO;
	
	//@Autowired
	protected DiaryDAO diaryDAO;

	//@Autowired
	protected EventDAOGoogle eventDAOGoogle;
		
	//@Autowired
	protected CalendarController calController;
	
	@RequestMapping("/listByMonth")
	protected @ResponseBody
	List<AnnualDiaryDTO> listByMonth(@RequestParam("localId") Long localId, @RequestParam("selectedDate") String selectedDate) throws Exception {

		List<AnnualDiaryDTO> listAnnual = annualDiaryDAO.getAnnualDiaryByMonth(localId, selectedDate);
	
		return listAnnual;
	}
	
	@RequestMapping("/listCalendarByMonth")
	protected @ResponseBody
	List<AnnualDiaryDTO> listCalendarByMonth(@RequestParam("id") Long id, @RequestParam("selectedDate") String selectedDate) throws Exception {

		List<AnnualDiaryDTO> listAnnual = annualDiaryDAO.getAnnualDiaryCalendarByMonth(id, selectedDate);
	
		return listAnnual;
	}
	
	@RequestMapping("/manager/getAnnualDiaryByDate")
	protected @ResponseBody
	List<AnnualDiaryDTO> getAnnualDiaryByDate(@RequestParam("localId") Long localId, @RequestParam("selectedDate") String selectedDate) throws Exception {
		
		LocalDTO local = localDAO.getById(localId);
		
		List<AnnualDiaryDTO> listAnnual = annualDiaryDAO.getAnnualDiaryByDate(localId, selectedDate, local.getLocOpenDays());
	
		return listAnnual;
	}
	
	@RequestMapping("/manager/getAnnualDiaryCalendarByDate")
	protected @ResponseBody
	List<AnnualDiaryDTO> getAnnualDiaryCalendarByDate(@RequestParam("id") Long id, @RequestParam("localId") Long localId, @RequestParam("selectedDate") String selectedDate) throws Exception {
		
		LocalDTO local = localDAO.getById(localId);
		
		List<AnnualDiaryDTO> listAnnual = annualDiaryDAO.getAnnualDiaryCalendarByDate(id, selectedDate, local.getLocOpenDays());
	
		return listAnnual;
	}
	
	@RequestMapping("/manager/listByDay")
	protected @ResponseBody
	DiaryDTO listByDay(@RequestParam("localId") Long localId, @RequestParam("selectedDate") String selectedDate) throws Exception {
		
		LocalDTO local = localDAO.getById(localId);
		
		DiaryDTO diaryDTO = null;
		
		// Comprobamos si esta fecha esta señalada en la agenda anual del local
		AnnualDiaryDTO annualDiaryDTO = annualDiaryDAO.getAnnualDiaryByDay(new Long(localId), selectedDate);
		if (annualDiaryDTO!=null){ // Esta fecha esta señalada en la agenda anual del local
			if (annualDiaryDTO.getAnuClosed()==0 && annualDiaryDTO.getAnuDayDiary()!=null){
				diaryDTO = annualDiaryDTO.getAnuDayDiary(); // asignamos la agenda de la fecha anual del local
			}
		}
		if (diaryDTO==null){ // asignamos la agenda por defecto del día de la semana del puesto
			int dayWeek = calController.getWeekDay(selectedDate);
			diaryDTO = local.getLocSemanalDiary().getDiary(dayWeek);
		}
	
		return diaryDTO;
	}
	
	
	@RequestMapping("/manager/listCalendarByDay")
	protected @ResponseBody
	DiaryDTO listCalendarByDay(@RequestParam("id") Long id, @RequestParam("selectedDate") String selectedDate) throws Exception {

		// Propiedades de calendar
		CalendarDTO calendar = calendarDAO.getById(id);
		
		DiaryDTO diaryDTO = null;
		
		// Comprobamos si esta fecha esta señalada en la agenda anual del puesto
		AnnualDiaryDTO annualDiaryDTO = annualDiaryDAO.getAnnualDiaryCalendarByDay(id, selectedDate);
		if (annualDiaryDTO!=null){ // Esta fecha esta señalada en la agenda anual del puesto
			if (annualDiaryDTO.getAnuClosed()==0 && annualDiaryDTO.getAnuDayDiary()!=null){
				diaryDTO = annualDiaryDTO.getAnuDayDiary(); // asignamos la agenda de la fecha anual del puesto
			}
		}
		if (diaryDTO==null){ // asignamos la agenda por defecto del día de la semana del puesto
			int dayWeek = calController.getWeekDay(selectedDate);
			diaryDTO = calendar.getCalSemanalDiary().getDiary(dayWeek);
		}
	
		return diaryDTO;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/closeGoogleLocal")
	@ResponseStatus(HttpStatus.OK)
	protected void closeGoogleLocal(@RequestParam("id") Long id)
			throws Exception {

		// Propiedades de local
		LocalDTO local = localDAO.getById(id);
		
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.MILLISECOND, 0);
		Date eveStartTime = calendarGreg.getTime();
		calendarGreg.add(Calendar.YEAR, 1);
		Date eveEndTime = calendarGreg.getTime();
		
		TimeZone calendarTimeZone = TimeZone.getTimeZone(local.getLocWhere().getWheTimeZone());
		
		DateTime startTime = new DateTime(eveStartTime, calendarTimeZone);
		DateTime endTime = new DateTime(eveEndTime, calendarTimeZone);
		
		List<Event> datesGoogle = eventDAOGoogle.getEvent(local,startTime,endTime);
		
		for (Event dateGoogle : datesGoogle) {
			startTime = dateGoogle.getStart().getDate();
			calendarGreg.setTime(new Date(startTime.getValue()));
			String strTime = calendarGreg.get(Calendar.YEAR)+CalendarController.CHAR_SEP_DATE+(calendarGreg.get(Calendar.MONTH)+1)+CalendarController.CHAR_SEP_DATE+calendarGreg.get(Calendar.DAY_OF_MONTH);
			closeLocal(id,strTime,true);
		}
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/closeLocal")
	@ResponseStatus(HttpStatus.OK)
	protected void closeLocal(@RequestParam("localId") Long localId, @RequestParam("selectedDate") String selectedDate)
			throws Exception {
		closeLocal(localId, selectedDate, false);
	}

	protected void closeLocal(Long localId, String selectedDate, boolean closeIfClean) throws Exception {	
		
		// Propiedades de local
		LocalDTO local = localDAO.getById(localId);
					
		AnnualDiaryDTO annualDiary = annualDiaryDAO.getAnnualDiaryByDay(localId, selectedDate);
		int close = 1; // por defecto la vamos a cerrar
		/* La creamos. Solo se puede crear para cerrar, porque abrir significa que estaba 
		 * cerrada, con lo que existiría. 
		 * A no ser que fuera un cerrado por dia semanal, entonces solo se puede crear para abrir, 
		 * porque cerrar significa que estaba abierta, con lo que existiría. 
		 */
		if (annualDiary==null){
			if (!closeIfClean){ // Si no es un "intento de cerrado si no existe"
				int dayWeek = calController.getWeekDay(selectedDate);
				for (int closedWeekDay : local.getLocSemanalDiary().getClosedDiary()) {
					if (dayWeek==closedWeekDay){
						close = 0; // Cerrado por dia semanal, la vamos a abrir
						break;
					}
				}
			}
			
			annualDiary = getAnnualDiary(selectedDate,null,close);
			annualDiary.setAnuLocalId(localId);
			annualDiary= annualDiaryDAO.create(annualDiary);
			
		} else if (!closeIfClean){ // Si no es un "intento de cerrado si no existe", la actualizamos, cambiamos su actual estado
			if (annualDiary.getAnuClosed()==1){
				close = 0;
			} else {
				close = 1;
			}
			annualDiary.setAnuClosed(close);
			annualDiaryDAO.update(annualDiary);
		}

		// Hay que modificar (abrir/cerrar) todos los puestos del local
		List<CalendarDTO> listCalendar = calendarDAO.getCalendar(local.getId());
		for (CalendarDTO calendar : listCalendar) {
			closeCalendar(calendar.getId(),selectedDate,close);
		}
				
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/closeCalendar")
	@ResponseStatus(HttpStatus.OK)
	protected void closeCalendar(@RequestParam("id") Long id, @RequestParam("selectedDate") String selectedDate)
			throws Exception {
		
		closeCalendar(id, selectedDate, null);
	}
	
	protected void closeCalendar(Long id, String selectedDate, Integer close) throws Exception {
		
		// Propiedades de calendar
		CalendarDTO calendar = calendarDAO.getById(id);
				
		AnnualDiaryDTO annualDiary = annualDiaryDAO.getAnnualDiaryCalendarByDay(id, selectedDate);
		/* La creamos. Solo se puede crear para cerrar, porque abrir significa que estaba 
		 * cerrada, con lo que existiría. 
		 * A no ser que fuera un cerrado por dia semanal, entonces solo se puede crear para abrir, 
		 * porque cerrar significa que estaba abierta, con lo que existiría. 
		 */
		if (annualDiary==null){
			
			if (close==null){
				close = 1; // por defecto la vamos a cerrar
				int dayWeek = calController.getWeekDay(selectedDate);
				for (int closedWeekDay : calendar.getCalSemanalDiary().getClosedDiary()) {
					if (dayWeek==closedWeekDay){
						close = 0; // Cerrado por dia semanal, la vamos a abrir
						break;
					}
				}
			}
			annualDiary = getAnnualDiary(selectedDate,null,close);
			annualDiary.setAnuCalendarId(id);
			annualDiaryDAO.create(annualDiary);
		} else {
			if (close==null){
				if (annualDiary.getAnuClosed()==1){
					close = 0;
				} else {
					close = 1;
				}
			}
			annualDiary.setAnuClosed(close);
			annualDiaryDAO.update(annualDiary);
		}
	}

	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/hoursLocal")
	@ResponseStatus(HttpStatus.OK)
	protected void hoursLocal(@RequestParam("localId") Long localId, @RequestParam("selectedDate") String selectedDate, @RequestParam("selectedTimes") String selectedTimes)
			throws Exception {
					
		AnnualDiaryDTO annualDiary = annualDiaryDAO.getAnnualDiaryByDay(localId, selectedDate);
		if (annualDiary==null){ // La creamos
			annualDiary = getAnnualDiary(selectedDate,selectedTimes,-1);
			annualDiary.setAnuLocalId(localId);
			annualDiaryDAO.create(annualDiary);
		} else if (annualDiary.getAnuClosed()==0){ // Si está abierta, la modificamos
			String[] a = selectedTimes.split(",");
			List<String> diaTimes = new ArrayList<String>();
			for (String strTime : a) {
				diaTimes.add(strTime);
			}

			DiaryDTO diary = new DiaryDTO();
			if (annualDiary.getAnuDayDiary()!=null){ // Si ya tiene horario especial lo modificamos
				diary = annualDiary.getAnuDayDiary();
				diary.setDiaTimes(diaTimes);
				diaryDAO.update(diary);
			} else { // Si no tiene horario especial lo creamos
				diary.setDiaTimes(diaTimes);
				diary.setEnabled(1);
				diary = diaryDAO.create(diary);
			}
			annualDiary.setAnuDayDiary(diary);
			annualDiaryDAO.update(annualDiary);
		}
		
		List<CalendarDTO> listCalendar = calendarDAO.getCalendar(localId);
		for (CalendarDTO calendar : listCalendar) {
			hoursCalendar(calendar.getId(), selectedDate, selectedTimes);
		}
		
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/hoursCalendar")
	@ResponseStatus(HttpStatus.OK)
	protected void hoursCalendar(@RequestParam("id") Long id, @RequestParam("selectedDate") String selectedDate, @RequestParam("selectedTimes") String selectedTimes)
			throws Exception {
		
		AnnualDiaryDTO annualDiary = annualDiaryDAO.getAnnualDiaryCalendarByDay(id, selectedDate);
		if (annualDiary==null){ // La creamos
			annualDiary = getAnnualDiary(selectedDate,selectedTimes,-1);
			annualDiary.setAnuCalendarId(id);
			annualDiaryDAO.create(annualDiary);
		} else if (annualDiary.getAnuClosed()==0){ // Si está abierta, la modificamos
			String[] a = selectedTimes.split(",");
			List<String> diaTimes = new ArrayList<String>();
			for (String strTime : a) {
				diaTimes.add(strTime);
			}

			DiaryDTO diary = new DiaryDTO();
			if (annualDiary.getAnuDayDiary()!=null){ // Si ya tiene horario especial lo modificamos
				diary = annualDiary.getAnuDayDiary();
				diary.setDiaTimes(diaTimes);
				diaryDAO.update(diary);
			} else { // Si no tiene horario especial lo creamos
				diary.setDiaTimes(diaTimes);
				diary.setEnabled(1);
				diary = diaryDAO.create(diary);
			}
			annualDiary.setAnuDayDiary(diary);
			annualDiaryDAO.update(annualDiary);
		}
				
	}
	
	protected AnnualDiaryDTO getAnnualDiary(String selectedDate, String selectedTimes, int close) 
			throws Exception {
		
		String[] dates = selectedDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];
		
		AnnualDiaryDTO annualDiary = new AnnualDiaryDTO();
		annualDiary.setEnabled(1);
		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.set(Calendar.HOUR_OF_DAY,0);
		calendarGreg.set(Calendar.MINUTE,0);
		calendarGreg.set(Calendar.SECOND,0);
		calendarGreg.set(Calendar.MILLISECOND,0);
		
		annualDiary.setAnuDate(calendarGreg.getTime());
		
		if (selectedTimes==null){ // Abrir o cerrar
			annualDiary.setAnuClosed(close);
		} else { // Abrierto con horario especial
			annualDiary.setAnuClosed(0);

			DiaryDTO diary = new DiaryDTO();
			diary.setEnabled(1);
			String[] a = selectedTimes.split(",");
			List<String> diaTimes = new ArrayList<String>();
			for (String strTime : a) {
				diaTimes.add(strTime);
			}
			diary.setDiaTimes(diaTimes);
			diary = diaryDAO.create(diary);
			annualDiary.setAnuDayDiary(diary);
		}
		
		return annualDiary;
	}

	public void setAnnualDiaryDAO(AnnualDiaryDAO annualDiaryDAO) {
		this.annualDiaryDAO = annualDiaryDAO;
	}

	public void setLocalDAO(LocalDAO localDAO) {
		this.localDAO = localDAO;
	}

	public void setCalendarDAO(CalendarDAO calendarDAO) {
		this.calendarDAO = calendarDAO;
	}

	public void setDiaryDAO(DiaryDAO diaryDAO) {
		this.diaryDAO = diaryDAO;
	}

	public void setEventDAOGoogle(EventDAOGoogle eventDAOGoogle) {
		this.eventDAOGoogle = eventDAOGoogle;
	}

	public void setCalController(CalendarController calController) {
		this.calController = calController;
	}
	
	
}

