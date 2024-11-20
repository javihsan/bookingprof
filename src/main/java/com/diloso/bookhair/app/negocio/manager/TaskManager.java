package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.TaskDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.TaskDAO;
import com.diloso.bookhair.app.persist.entities.Task;
import com.diloso.bookhair.app.persist.mapper.TaskMapper;

@Component
@Scope(value = "singleton")
public class TaskManager implements ITaskManager {

	public static final String ENABLED = "enabled";
	public static final String ORDER_KEY_ASC = "__key__";
	
	public static final String KEY_MULTI_TASK_NAME = "task_name_";
	public static final String FIELD_MULTI_TASK_NAME = "tasNameMulti";
	public static final String FIELD_MULTI_TASK_ENTITY_NAME = FIELD_MULTI_TASK_NAME + "Id";

	@Autowired
	private TaskDAO taskDAO;
	
	@Autowired
	protected IMultiTextManager multiTextManager;
	
	@Autowired
	protected TaskMapper mapper;
	
	public TaskManager() {

	}

	@Override
	public TaskDTO create(TaskDTO taskDTO) throws Exception {
		Task task = mapper.map(taskDTO);
		task = taskDAO.create(task);
		return mapper.map(task);
	}

	@Override
	public TaskDTO remove(long id) throws Exception {
		Task task = taskDAO.get(id);
		taskDAO.delete(id);
		if (task == null) {
			return null;
		}
		return mapper.map(task);
	}

	@Override
	public TaskDTO update(TaskDTO taskDTO) throws Exception {
		Task task = mapper.map(taskDTO);
		Task oldTask = taskDAO.get(taskDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(task, oldTask);
		} catch (Exception e) {
		}
		task = taskDAO.update(task);
		if (task == null) {
			return null;
		}
		return mapper.map(task);
	}

	@Override
	public TaskDTO getById(long id) {
		Task task = taskDAO.get(id);
		if (task == null) {
			return null;
		}
		return mapper.map(task);		
	}

	@Override
	public TaskDTO getByName(String multiKey) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(FIELD_MULTI_TASK_NAME, multiKey);
		filters.put(ENABLED, 1);
		List<Task> resultQuery = taskDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}

	@Override
	public List<TaskDTO> getTaskByLang(String lang, List<Long> classTasksFirm) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<Task> resultQuery = taskDAO.listOrderFilter(filters, orders);
		List<TaskDTO> result = new ArrayList<TaskDTO>();
		resultQuery.stream().forEach(entity -> {
			if (classTasksFirm.contains(entity.getTasClassId())){
				String name = "";
				TaskDTO task = mapper.map(entity);
				MultiTextDTO multiTextKey = multiTextManager.getByLanCodeAndKey(lang, task.getTasClass().getTclNameMulti());
				name = "- "+multiTextKey.getMulText()+" - ";
				multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
						task.getTasNameMulti());
				name += multiTextKey.getMulText();
				task.setTasName(name);
				result.add(task);
			}
		});
		return result;
	}

	@Override
	public List<TaskDTO> getTask() {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<Task> resultQuery = taskDAO.listOrderFilter(filters, orders);
		List<TaskDTO> result = new ArrayList<TaskDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;
	}

}