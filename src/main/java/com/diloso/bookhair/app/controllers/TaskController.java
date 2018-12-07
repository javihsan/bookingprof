package com.diloso.bookhair.app.controllers;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.diloso.bookhair.app.negocio.dao.FirmDAO;
import com.diloso.bookhair.app.negocio.dao.TaskDAO;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.dto.TaskDTO;

@Controller
@RequestMapping(value={"/*/task", "/task"})
public class TaskController {
	
	@Autowired
	protected MessageSource messageSourceApp;
	
	/*@Autowired
	protected MultiTextDAO multiTextDAO;
	
	@Autowired
	protected LangDAO langDAO;*/
	
	@Autowired
	protected TaskDAO taskDAO;
	
	@Autowired
	protected FirmDAO firmDAO;
	
	/*
	@RequestMapping(method = RequestMethod.POST, value = "/manager/new")
	@ResponseStatus(HttpStatus.OK)
	protected void newObject(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		TaskDTO task = new TaskDTO();
		task.setEnabled(1);
		task.setTasName("");
		
		List<MultiTextDTO> listMulti = new ArrayList<MultiTextDTO>();
		MultiTextDTO nameMulti = null;
		List<LangDTO> listLang = langDAO.getLang();
		for (LangDTO langDTO : listLang) {
			nameMulti = new MultiTextDTO();
			nameMulti.setEnabled(1);
			nameMulti.setMulKey(TaskDTO.KEY_MULTI_TASK_NAME);
			nameMulti.setMulText("Corte de pelo");
			nameMulti = multiTextDAO.create(nameMulti);
			multiList.add(listMulti);
		}
			
		taskDTO.setTasName(listMulti);
			
		task = taskDAO.create(task);
		
	}*/
	
		
	@RequestMapping("/list")
	public @ResponseBody
	List<TaskDTO> list(HttpServletRequest arg0, @RequestParam("domain") String domain) throws Exception {

		Locale locale = RequestContextUtils.getLocale(arg0);
		
		FirmDTO firm = firmDAO.getFirmDomain(domain);

		List<TaskDTO> listTask = taskDAO.getTaskByLang(locale.getLanguage(), firm.getFirClassTasks());	
					
		return listTask;
	}
	

	@RequestMapping("/get")
	protected @ResponseBody
	TaskDTO get(@RequestParam("id") Long id) throws Exception {

		TaskDTO task = taskDAO.getById(id);	
					
		return task;
	}
	

}
