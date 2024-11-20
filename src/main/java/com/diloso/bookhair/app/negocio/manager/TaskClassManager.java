package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.TaskClassDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.TaskClassDAO;
import com.diloso.bookhair.app.persist.entities.TaskClass;
import com.diloso.bookhair.app.persist.mapper.TaskClassMapper;

@Component
@Scope(value = "singleton")
public class TaskClassManager implements ITaskClassManager {

	public static final String ENABLED = "enabled";
	public static final String ORDER_KEY_ASC = "__key__";
	public static final String TC_NAME_MULTI = "tclNameMulti";
	
	public static final String KEY_MULTI_TASKCLASS_NAME = "taskClass_name_";

	@Autowired
	private TaskClassDAO taskClassDAO;
	
	@Autowired
	protected IMultiTextManager multiTextManager;

	@Autowired
	protected TaskClassMapper mapper;
	
	public TaskClassManager() {

	}

	@Override
	public TaskClassDTO create(TaskClassDTO taskClassDTO) throws Exception {
		TaskClass taskClass = mapper.map(taskClassDTO);
		taskClass = taskClassDAO.create(taskClass);
		return mapper.map(taskClass);
	}

	@Override
	public TaskClassDTO remove(long id) throws Exception {
		TaskClass taskClass = taskClassDAO.get(id);
		taskClassDAO.delete(id);
		if (taskClass == null) {
			return null;
		}
		return mapper.map(taskClass);
	}

	@Override
	public TaskClassDTO update(TaskClassDTO taskClassDTO) throws Exception {
		TaskClass taskClass = mapper.map(taskClassDTO);
		TaskClass oldTaskClass = taskClassDAO.get(taskClassDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(taskClass, oldTaskClass);
		} catch (Exception e) {
		}
		taskClass = taskClassDAO.update(taskClass);
		if (taskClass == null) {
			return null;
		}
		return mapper.map(taskClass);
	}

	@Override
	public TaskClassDTO getById(long id) {
		TaskClass taskClass = taskClassDAO.get(id);
		if (taskClass == null) {
			return null;
		}
		return mapper.map(taskClass);		
	}

	@Override
	public TaskClassDTO getByName(String multiKey) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(TC_NAME_MULTI, multiKey);
		filters.put(ENABLED, 1);
		List<TaskClass> resultQuery = taskClassDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}

	@Override
	public List<TaskClassDTO> getTaskClassByLang(String lang) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<TaskClass> resultQuery = taskClassDAO.listOrderFilter(filters, orders);
		List<TaskClassDTO> result = new ArrayList<TaskClassDTO>();
		resultQuery.stream().forEach(entity -> {
			TaskClassDTO taskClass = mapper.map(entity);
			MultiTextDTO multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
					taskClass.getTclNameMulti());
			String name = multiTextKey.getMulText();
			if (name != null) {
				taskClass.setTclName(name);
				result.add(taskClass);
			}
		});
		return result;
	}

	@Override
	public List<TaskClassDTO> getTaskClass() {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<TaskClass> resultQuery = taskClassDAO.listOrderFilter(filters, orders);
		List<TaskClassDTO> result = new ArrayList<TaskClassDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;		
	}

		
	
}