package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.TaskDAO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.TaskDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Task;
import com.diloso.bookhair.app.persist.transformer.TaskTransformer;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Component
@Scope(value = "singleton")
public class TaskManager extends Manager implements TaskDAO {

	public static final String KEY_MULTI_TASK_NAME = "task_name_";
	public static final String FIELD_MULTI_TASK_NAME = "tasNameMulti";
	public static final String FIELD_MULTI_TASK_ENTITY_NAME = FIELD_MULTI_TASK_NAME + "Id";

	@Autowired
	protected MultiTextDAO multiTextDAO;
	
	@Autowired
	protected TaskTransformer taskTransformer;
	
	public TaskManager() {

	}

	public TaskDTO create(TaskDTO task) throws Exception {
		EntityManager em = getEntityManager();
		Task entityTask = taskTransformer.transformDTOToEntity(
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
		return taskTransformer.transformEntityToDTO(entityTask);
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
		return taskTransformer
				.transformEntityToDTO(oldEntityTask);
	}

	public TaskDTO update(TaskDTO task) throws Exception {
		EntityManager em = getEntityManager();
		Task entityTask = taskTransformer.transformDTOToEntity(
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
		return taskTransformer.transformEntityToDTO(entityTask);
	}

	public TaskDTO getById(long id) {
		Entity entityTask = null;
		try {
			Key k = KeyFactory.createKey(Task.class.getSimpleName(), id);
			entityTask = DatastoreServiceFactory.getDatastoreService().get(k);
		} catch (Exception ex) {
		}
		return taskTransformer.transformEntityToDTO(entityTask);
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
				task = taskTransformer.transformEntityToDTO(
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
				task = taskTransformer.transformEntityToDTO(
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
					task = taskTransformer.transformEntityToDTO(
							entityTask);
					multiTextKey = multiTextDAO.getByLanCodeAndKey(lang, task.getTasClass().getTclNameMulti());
					name = "- "+multiTextKey.getMulText()+" - ";
					multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
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

}