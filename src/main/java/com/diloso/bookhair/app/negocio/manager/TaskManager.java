package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.TaskDTO;
import com.diloso.bookhair.app.persist.dao.TaskDAO;
import com.diloso.bookhair.app.persist.mapper.TaskMapper;

@Component
@Scope(value = "singleton")
public class TaskManager implements ITaskManager {

	public static final String KEY_MULTI_TASK_NAME = "task_name_";
	public static final String FIELD_MULTI_TASK_NAME = "tasNameMulti";
	public static final String FIELD_MULTI_TASK_ENTITY_NAME = FIELD_MULTI_TASK_NAME + "Id";

	@Autowired
	private TaskDAO taskDAO;
	
	@Autowired
	protected IMultiTextManager multiTextManager;
	
	@Autowired
	protected TaskMapper taskMapper;
	
	public TaskManager() {

	}

	@Override
	public TaskDTO create(TaskDTO task) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskDTO update(TaskDTO task) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskDTO getByName(String multiKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskDTO> getTaskByLang(String lang, List<Long> classTasksFirm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskDTO> getTask() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	/*
	public TaskDTO create(TaskDTO task) throws Exception {
		EntityManager em = getEntityManager();
		Task entityTask = taskMapper.map(
				task);
		try {
			em.getTransaction().begin();
			em.persist(entityTask);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			} catch (Exception e) {
				throw e;
			}
			throw ex;
		} finally {
			em.close();
		}
		return taskMapper.map(entityTask);
	}

	public TaskDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Task oldEntityTask = new Task();
		try {
			em.getTransaction().begin();
			Task entityTask = (Task) em.find(Task.class, id);
			PropertyUtils.copyProperties(oldEntityTask, entityTask);
			em.remove(em.merge(entityTask));
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			} catch (Exception e) {
				throw e;
			}
			throw ex;
		} finally {
			em.close();
		}
		return taskMapper
				.map(oldEntityTask);
	}

	public TaskDTO update(TaskDTO task) throws Exception {
		EntityManager em = getEntityManager();
		Task entityTask = taskMapper.map(
				task);
		Task oldEntityTask = null;
		try {
			em.getTransaction().begin();
			oldEntityTask = (Task) em.find(Task.class, entityTask.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityTask,
					oldEntityTask);
			entityTask = em.merge(entityTask);
			em.getTransaction().commit();
		} catch (Exception ex) {
			try {
				if (em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			} catch (Exception e) {
				throw e;
			}
			throw ex;
		} finally {
			em.close();
		}
		return taskMapper.map(entityTask);
	}

	public TaskDTO getById(long id) {
		Entity entityTask = null;
		try {
			Key k = KeyFactory.createKey(Task.class.getSimpleName(), id);
			entityTask = DatastoreServiceFactory.getDatastoreService().get(k);
		} catch (Exception ex) {
		}
		return taskMapper.map(entityTask);
	}

	public TaskDTO getByName(String multiKey) {
		EntityManager em = getEntityManager();
		List<Task> resultQuery = null;
		TaskDTO task = null;
		try {
			Query query = em.createNamedQuery("getTaskMultiKey");
			query.setParameter("tasNameMulti", multiKey);
			resultQuery = (List<Task>) query.getResultList();
			if (resultQuery.size() == 1) {
				task = taskMapper.map(
						resultQuery.get(0));
			}
		} finally {
			em.close();
		}
		return task;
	}
	
	public List<TaskDTO> getTask() {
		EntityManager em = getEntityManager();
		List<TaskDTO> result = new ArrayList<TaskDTO>();
		List<Task> resultQuery = null;
		TaskDTO task = null;
		try {
			Query query = em.createNamedQuery("getTask");
			resultQuery = (List<Task>) query.getResultList();
			for (Task entityTask : resultQuery) {
				task = taskMapper.map(
						entityTask);
				result.add(task);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public List<TaskDTO> getTaskByLang(String lang, List<Long> classTasksFirm){
		EntityManager em = getEntityManager();
		List<TaskDTO> result = new ArrayList<TaskDTO>();
		List<Task> resultQuery = null;
		TaskDTO task = null;
		MultiTextDTO multiTextKey = null;
		try {
			Query query = em.createNamedQuery("getTask");
			resultQuery = (List<Task>) query.getResultList();
			String name = "";
			for (Task entityTask : resultQuery) {
				if (classTasksFirm.contains(entityTask.getTasClassId())){
					task = taskMapper.map(
							entityTask);
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang, task.getTasClass().getTclNameMulti());
					name = "- "+multiTextKey.getMulText()+" - ";
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
							task.getTasNameMulti());
					name += multiTextKey.getMulText();
					task.setTasName(name);
					result.add(task);
				}	
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setMultiTextDAO(IMultiTextManager iMultiTextManager) {
		this.multiTextManager = iMultiTextManager;
	}

	public void setTaskTransformer(TaskMapper taskMapper) {
		this.taskMapper = taskMapper;
	}
*/
	
}