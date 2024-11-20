package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.LocalTaskDAO;
import com.diloso.bookhair.app.persist.entities.LocalTask;
import com.diloso.bookhair.app.persist.mapper.LocalTaskMapper;

@Component
@Scope(value = "singleton")
public class LocalTaskManager implements ILocalTaskManager {

	public static final String KEY_MULTI_LOCAL_TASK_NAME = "local_task_name_";
	public static final String FIELD_MULTI_LOCAL_TASK_NAME = "lotNameMulti";

	public static final String LOT_LOCAL_ID = "lotLocalId";
	public static final String LOT_VISIBLE = "lotVisible";
	public static final String ENABLED = "enabled";
	public static final String ORDER_KEY_ASC = "__key__";

	@Autowired
	private LocalTaskDAO localTaskDAO;

	@Autowired
	protected IMultiTextManager multiTextManager;

	@Autowired
	protected ITaskManager taskManager;

	@Autowired
	protected LocalTaskMapper mapper;

	public LocalTaskManager() {

	}

	@Override
	public LocalTaskDTO create(LocalTaskDTO localTaskDTO) throws Exception {
		LocalTask localTask = mapper.map(localTaskDTO);
		localTask = localTaskDAO.create(localTask);
		return mapper.map(localTask);
	}

	@Override
	public LocalTaskDTO remove(long id) throws Exception {
		LocalTask localTask = localTaskDAO.get(id);
		localTaskDAO.delete(id);
		if (localTask == null) {
			return null;
		}
		return mapper.map(localTask);
	}

	@Override
	public LocalTaskDTO update(LocalTaskDTO localTaskDTO) throws Exception {
		LocalTask localTask = mapper.map(localTaskDTO);
		LocalTask oldLocalTask = localTaskDAO.get(localTaskDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(localTask, oldLocalTask);
		} catch (Exception e) {
		}
		localTask = localTaskDAO.update(localTask);
		if (localTask == null) {
			return null;
		}
		return mapper.map(localTask);
	}

	@Override
	public LocalTaskDTO getById(long id) {
		LocalTask localTask = localTaskDAO.get(id);
		if (localTask == null) {
			return null;
		}
		return mapper.map(localTask);
	}

	@Override
	public LocalTaskDTO getByName(String multiKey) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(FIELD_MULTI_LOCAL_TASK_NAME, multiKey);
		filters.put(ENABLED, 1);
		List<LocalTask> resultQuery = localTaskDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskSimple(long lotLocalId, String lang) {
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(LOT_LOCAL_ID, lotLocalId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<LocalTask> resultQuery = localTaskDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			if ((entity.getLotTaskCombiId() == null || entity.getLotTaskCombiId().size() == 0)
					&& entity.getLotTaskDuration() > 0) {
				LocalTaskDTO localTask = mapper.map(entity);
				MultiTextDTO multiTextKey = multiTextManager.getByLanCodeAndKey(lang, localTask.getLotNameMulti());
				String name = multiTextKey.getMulText();
				localTask.setLotName(name);
				result.add(localTask);
			}
		});
		return result;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskSimpleInv(long lotLocalId, String lang) {
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(LOT_LOCAL_ID, lotLocalId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<LocalTask> resultQuery = localTaskDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			if ((entity.getLotTaskCombiId() == null || entity.getLotTaskCombiId().size() == 0)
					&& entity.getLotTaskRate() > 0) {
				LocalTaskDTO localTask = mapper.map(entity);
				MultiTextDTO multiTextKey = multiTextManager.getByLanCodeAndKey(lang, localTask.getLotNameMulti());
				String name = multiTextKey.getMulText();
				localTask.setLotName(name);
				result.add(localTask);
			}
		});
		return result;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskCombi(long lotLocalId, String lang, String charAND) {
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(LOT_LOCAL_ID, lotLocalId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<LocalTask> resultQuery = localTaskDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			if (entity.getLotTaskCombiId() != null && entity.getLotTaskCombiId().size() > 0) {
				LocalTaskDTO localTask = mapper.map(entity);
				String name = "";
				MultiTextDTO multiTextKey = null;
				for (Long taskId : localTask.getLotTaskCombiId()) {
					if (name.length() > 0) {
						name += " " + charAND + " ";
					}
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang, getById(taskId).getLotNameMulti());
					name += multiTextKey.getMulText();
				}
				localTask.setLotName(name);
				result.add(localTask);
			}
		});
		return result;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskAndCombi(long lotLocalId, String lang, String charAND) {
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(LOT_LOCAL_ID, lotLocalId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<LocalTask> resultQuery = localTaskDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			LocalTaskDTO localTask = mapper.map(entity);
			String name = "";
			MultiTextDTO multiTextKey = null;
			if (localTask.getLotTaskCombiId() != null && localTask.getLotTaskCombiId().size() > 0) {
				for (Long taskId : localTask.getLotTaskCombiId()) {
					if (name.length() > 0) {
						name += " " + charAND + " ";
					}
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang, getById(taskId).getLotNameMulti());
					name += multiTextKey.getMulText();
				}
			} else if (localTask.getLotTaskDuration() > 0) {
				multiTextKey = multiTextManager.getByLanCodeAndKey(lang, localTask.getLotNameMulti());
				name = multiTextKey.getMulText();
			}
			if (name.length() > 0) {
				localTask.setLotName(name);
				result.add(localTask);
			}
		});

		return result;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskAndCombiVisible(long lotLocalId, String lang, String charAND) {
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(LOT_LOCAL_ID, lotLocalId);
		filters.put(LOT_VISIBLE, 1);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<LocalTask> resultQuery = localTaskDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			LocalTaskDTO localTask = mapper.map(entity);
			String name = "";
			MultiTextDTO multiTextKey = null;
			if (localTask.getLotTaskCombiId() != null && localTask.getLotTaskCombiId().size() > 0) {
				for (Long taskId : localTask.getLotTaskCombiId()) {
					if (name.length() > 0) {
						name += " " + charAND + " ";
					}
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang, getById(taskId).getLotNameMulti());
					name += multiTextKey.getMulText();
				}
			} else if (localTask.getLotTaskDuration() > 0) {
				multiTextKey = multiTextManager.getByLanCodeAndKey(lang, localTask.getLotNameMulti());
				name = multiTextKey.getMulText();
			}
			if (name.length() > 0) {
				localTask.setLotName(name);
				result.add(localTask);
			}
		});

		return result;
	}

	@Override
	public List<LocalTaskDTO> getLocalTask(long lotLocalId, String lang, String charAND) {
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(LOT_LOCAL_ID, lotLocalId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<LocalTask> resultQuery = localTaskDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			LocalTaskDTO localTask = mapper.map(entity);
			String name = null;
			MultiTextDTO multiTextKey = null;
			if (entity.getLotTaskCombiId() != null && entity.getLotTaskCombiId().size() > 0) {
				name = "";
				for (Long taskId : localTask.getLotTaskCombiId()) {
					if (name.length() > 0) {
						name += " " + charAND + " ";
					}
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang, getById(taskId).getLotNameMulti());
					name += multiTextKey.getMulText();
				}
			} else {
				multiTextKey = multiTextManager.getByLanCodeAndKey(lang, localTask.getLotNameMulti());
				name = multiTextKey.getMulText();
			}
			localTask.setLotName(name);
			result.add(localTask);
		});

		return result;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskSimpleAdmin(long lotLocalId, String lang) {
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(LOT_LOCAL_ID, lotLocalId);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<LocalTask> resultQuery = localTaskDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			if (entity.getLotTaskCombiId() == null || entity.getLotTaskCombiId().size() == 0) {
				LocalTaskDTO localTask = mapper.map(entity);
				MultiTextDTO multiTextKey = multiTextManager.getByLanCodeAndKey(lang, localTask.getLotNameMulti());
				String name = multiTextKey.getMulText();
				localTask.setLotName(name);
				result.add(localTask);
			}
		});
		return result;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskAdmin(long lotLocalId, String lang) {
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(LOT_LOCAL_ID, lotLocalId);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_ASC);
		List<LocalTask> resultQuery = localTaskDAO.listOrderFilter(filters, orders);
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;
	}

}