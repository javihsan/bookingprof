package com.diloso.bookhair.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
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

import com.diloso.bookhair.app.negocio.dao.CalendarDAO;
import com.diloso.bookhair.app.negocio.dao.DiaryDAO;
import com.diloso.bookhair.app.negocio.dao.LangDAO;
import com.diloso.bookhair.app.negocio.dao.LocalDAO;
import com.diloso.bookhair.app.negocio.dao.LocalTaskDAO;
import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.SemanalDiaryDAO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.persist.manager.LocalTaskManager;

@Controller
@RequestMapping(value={"/*/localTask", "/localTask"})
public class LocalTaskController {
	
	protected static final String LOT_NAME_PARAM = "lotName";
	
	@Autowired
	protected MessageSource messageSourceApp;
		
	@Autowired
	protected LocalDAO localDAO;
	
	@Autowired
	protected CalendarDAO calendarDAO;
	
	@Autowired
	protected MultiTextDAO multiTextDAO;
	
	@Autowired
	protected LangDAO langDAO;
	
	@Autowired
	protected LocalTaskDAO localTaskDAO;
	
	@Autowired
	protected DiaryDAO diaryDAO;
	
	@Autowired
	protected SemanalDiaryDAO semanalDiaryDAO;
	
	@ExceptionHandler(UncategorizedDataAccessException.class)
	@ResponseStatus(value=HttpStatus.CONFLICT,reason="")
	protected void error(Exception t, HttpServletResponse response) throws IOException{
		response.sendError(HttpStatus.CONFLICT.value(), t.getMessage());
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
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/new")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody
	LocalTaskDTO newObject (HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		String langDefault = locale.getLanguage();
		
		String lotId = arg0.getParameter("id");
		
		String defaultNameValue = arg0.getParameter(LOT_NAME_PARAM+"_"+langDefault);			
				
		String strLotTaskId = arg0.getParameter("lotTaskId");
		String strLotTaskDuration = arg0.getParameter("lotTaskDuration");
		String strLotTaskPost = arg0.getParameter("lotTaskPost");
		String strLotTaskRate = arg0.getParameter("lotTaskRate");
		
		String strLotVisible = arg0.getParameter("lotVisible");
		
		LocalTaskDTO localTask = null;
		
		if (validateNew(arg0, defaultNameValue, strLotTaskId, strLotTaskDuration, strLotTaskRate)){

			Long localId = new Long(arg0.getParameter("localId"));
			LocalDTO local = localDAO.getById(new Long(localId));
			
			localTask = new LocalTaskDTO();
			
			Long lotTaskId = new Long(strLotTaskId);
			Integer lotTaskDuration = new Integer(strLotTaskDuration);
			strLotTaskRate = strLotTaskRate.replace(",", ".");
			Float lotTaskRate = new Float(strLotTaskRate);
			
			boolean setLocalDefault = lotTaskDuration>0;
			if (localTaskDAO.getLocalTaskSimple(localId,langDefault).size()>0){
				setLocalDefault = false;
			}
			
			if (lotId!=null){ // Existe
				localTask = localTaskDAO.getById(new Long(lotId));
			}
			
			String nameValue = "";
			String keyNameMulti = LocalTaskManager.KEY_MULTI_LOCAL_TASK_NAME+localId+"_"+defaultNameValue.toLowerCase();
			Map<String,String> hashNamesParam = new HashMap<String,String>();

			List<LangDTO> listLang = local.getLocLangs();
			for (LangDTO lang : listLang) {
				nameValue = arg0.getParameter(LOT_NAME_PARAM+"_"+lang.getLanCode());
				if (nameValue == null || nameValue.length()==0){
					nameValue = defaultNameValue;
				}
				hashNamesParam.put(lang.getLanCode(), nameValue);
			}

			if (lotId!=null){ // Existe
				List<MultiTextDTO> listMulti = multiTextDAO.getMultiTextByKey(localTask.getLotNameMulti());
				for (MultiTextDTO nameMulti: listMulti){
					nameValue = hashNamesParam.get(nameMulti.getMulLanCode());
					nameMulti.setMulText(nameValue);
					multiTextDAO.update(nameMulti);
				}
			} else {
				int indx = 0;
				while (multiTextDAO.getByLanCodeAndKey(locale.getLanguage(), keyNameMulti)!=null){
					keyNameMulti = LocalTaskManager.KEY_MULTI_LOCAL_TASK_NAME+localId+"_"+defaultNameValue.toLowerCase()+"_"+indx;
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
					multiTextDAO.create(nameMulti);
				}
			}
			
			if (lotId!=null){ // Existe
				localTask.setLotTaskId(lotTaskId);
				localTask.setLotTaskDuration(lotTaskDuration);
				if (strLotTaskPost!=null){
					Integer lotTaskPost = new Integer(strLotTaskPost);
					localTask.setLotTaskPost(lotTaskPost);
				}
				localTask.setLotTaskRate(lotTaskRate);
				localTask.setLotVisible(new Integer(strLotVisible));
				localTaskDAO.update(localTask);
			} else { // Es nuevo
				localTask.setEnabled(1);
				localTask.setLotLocalId(localId);
				localTask.setLotNameMulti(keyNameMulti);
				localTask.setLotTaskId(lotTaskId);
				localTask.setLotTaskDuration(lotTaskDuration);
				if (strLotTaskPost!=null){
					Integer lotTaskPost = new Integer(strLotTaskPost);
					localTask.setLotTaskPost(lotTaskPost);
				}
				localTask.setLotTaskRate(lotTaskRate);
				localTask.setLotVisible(new Integer(strLotVisible));
				localTask = localTaskDAO.create(localTask);
				
				if (setLocalDefault){
					localTask.setLotDefault(1);
					local.setLocTaskDefaultId(localTask.getId());
					localDAO.update(local);
				}
			}
		}
		return localTask;
	}
	
	
	/* El tareas y duracion no está vacío.
	*/
	protected boolean validateNewCombi(HttpServletRequest arg0, String selectLotTaskCombiId, String selectLotTaskCombiIRes) throws UncategorizedDataAccessException {
		boolean res = true;
		String message = "";
		Locale locale = RequestContextUtils.getLocale(arg0);
		if (selectLotTaskCombiId==null || selectLotTaskCombiId.length()==0){
			message = messageSourceApp.getMessage("form.error.localTask.taskReq", null, locale);
			res = false;
		} else if (selectLotTaskCombiIRes==null || selectLotTaskCombiIRes.length()==0){
			message = messageSourceApp.getMessage("form.error.localTask.durationReq", null, locale);
			res = false;
		} 
		
		if (!res){
			throw new ErrorService(message, null);
		}
		return res;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/newCombi")
	@ResponseStatus(HttpStatus.OK)
	protected void newObjectCombi(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		String lotId = arg0.getParameter("id");
		
		String selectLotTaskCombiId = arg0.getParameter("lotTaskCombiId");
		String selectLotTaskCombiIRes = arg0.getParameter("lotTaskCombiRes");
		String strLotVisible = arg0.getParameter("lotVisible");
		
		if (validateNewCombi(arg0, selectLotTaskCombiId, selectLotTaskCombiIRes)){

			Long localId = new Long(arg0.getParameter("localId"));
			
			List<Long> lotTaskCombiId = new ArrayList<Long>();
			String[] a = selectLotTaskCombiId.split(",");
			for (String str : a) {
				lotTaskCombiId.add(new Long(str));
			}
			
			List<Integer> lotTaskCombiRes = new ArrayList<Integer>();
			a = selectLotTaskCombiIRes.split(",");
			for (String str : a) {
				lotTaskCombiRes.add(new Integer(str));
			}
			
			LocalTaskDTO localTask = new LocalTaskDTO();
			
			if (lotId!=null){ // Existe
				localTask = localTaskDAO.getById(new Long(lotId));
				localTask.setLotTaskCombiId(lotTaskCombiId);
				localTask.setLotTaskCombiRes(lotTaskCombiRes);
				localTask.setLotVisible(new Integer(strLotVisible));
				localTaskDAO.update(localTask);
			} else { // Es nuevo
				localTask.setEnabled(1);
				localTask.setLotLocalId(localId);
				localTask.setLotTaskCombiId(lotTaskCombiId);
				localTask.setLotTaskCombiRes(lotTaskCombiRes);
				localTask.setLotVisible(new Integer(strLotVisible));
				localTask = localTaskDAO.create(localTask);
			}
		}
	}
	
	/* El id sea != null, no esté en ninguna combinada
	*/
	protected boolean validateRemove(HttpServletRequest arg0, Long id, String localId) throws UncategorizedDataAccessException {
		boolean res = true;
		String message = "";
		Locale locale = RequestContextUtils.getLocale(arg0);
		if (id==null){
			message = messageSourceApp.getMessage("form.error.localTask.taskReq", null, locale);
			res = false;
		} else{
			List<LocalTaskDTO> listLocalTask = localTaskDAO.getLocalTaskCombi(new Long(localId), locale.getLanguage(), "");
			for (LocalTaskDTO localTaskDTO : listLocalTask) {
				for (Long combiId : localTaskDTO.getLotTaskCombiId()) {
					if (combiId.equals(id)){
						message = messageSourceApp.getMessage("form.error.localTask.existsCombi", null, locale);
						res = false;
						break;
					}
				}
			}
		} 
		
		if (!res){
			throw new ErrorService(message, null);
		}
		return res;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/remove")
	@ResponseStatus(HttpStatus.OK)
	protected void remove(HttpServletRequest arg0, @RequestParam("localId") String localId, @RequestParam("id") Long id)
			throws Exception {
			
		if (validateRemove(arg0, id, localId)){
			LocalTaskDTO localTask = localTaskDAO.getById(id);
			localTask.setEnabled(0);
			localTaskDAO.update(localTask);
			
			// Se borra de todos los puestos del local
			List<CalendarDTO> listCalendar = calendarDAO.getCalendar(new Long(localId));
			for (CalendarDTO calendar : listCalendar) {
				List<Long> listLocalTask = new ArrayList<Long>();
				if (calendar.getCalLocalTasksId()!=null){
					for (Long localTaskId : calendar.getCalLocalTasksId()){
						if (!localTask.getId().equals(localTaskId)){
							listLocalTask.add(localTaskId);
						}
					}
				}	
				calendar.setCalLocalTasksId(listLocalTask);
				calendarDAO.update(calendar);
			}
			
		}
	}	

	@RequestMapping("/listCombi")
	protected @ResponseBody
	List<LocalTaskDTO> listCombi(HttpServletRequest arg0, @RequestParam("localId") Long localId, @RequestParam("lanCode") String lanCode) throws Exception {
		
		Locale locale = new Locale(lanCode);
		String charAND =  messageSourceApp.getMessage("general.and", null, locale);
		
		return localTaskDAO.getLocalTaskAndCombiVisible(localId, locale.getLanguage(), charAND);	
					
	}
	
	@RequestMapping("/operator/listCombi")
	protected @ResponseBody
	List<LocalTaskDTO> listCombiOperator(HttpServletRequest arg0, @RequestParam("localId") Long localId) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
		String charAND =  messageSourceApp.getMessage("general.and", null, locale);
		
		return localTaskDAO.getLocalTaskAndCombi(localId, locale.getLanguage(), charAND);	
					
	}

	
	@RequestMapping("/manager/listCombi")
	protected @ResponseBody
	List<LocalTaskDTO> listCombiManager(HttpServletRequest arg0, @RequestParam("localId") Long localId) throws Exception {
				
		Locale locale = RequestContextUtils.getLocale(arg0);
		String charAND =  messageSourceApp.getMessage("general.and", null, locale);
		
		List<LocalTaskDTO> listTask = localTaskDAO.getLocalTask(localId, locale.getLanguage(), charAND);	
					
		return listTask;
	}
	
	@RequestMapping("/manager/listOnlySimple")
	protected @ResponseBody
	List<LocalTaskDTO> listOnlySimple(HttpServletRequest arg0, @RequestParam("localId") Long localId) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
				
		List<LocalTaskDTO> listTask = localTaskDAO.getLocalTaskSimple(localId, locale.getLanguage());	
					
		return listTask;
	}
	
	@RequestMapping("/manager/listOnlySimpleInv")
	protected @ResponseBody
	List<LocalTaskDTO> listOnlySimpleInv(HttpServletRequest arg0, @RequestParam("localId") Long localId) throws Exception {
		
		Locale locale = RequestContextUtils.getLocale(arg0);
				
		List<LocalTaskDTO> listTask = localTaskDAO.getLocalTaskSimpleInv(localId, locale.getLanguage());	
					
		return listTask;
	}
	
	@RequestMapping("/manager/get")
	protected @ResponseBody
	LocalTaskDTO get(@RequestParam("id") Long id) throws Exception {

		LocalTaskDTO localTask = localTaskDAO.getById(id);	
					
		return localTask;
	}
	
	
}
