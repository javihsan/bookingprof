package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.TaskClassDTO;
import com.diloso.bookhair.app.persist.dao.TaskClassDAO;
import com.diloso.bookhair.app.persist.mapper.TaskClassMapper;

@Component
@Scope(value = "singleton")
public class TaskClassManager implements ITaskClassManager {

	public static final String KEY_MULTI_TASKCLASS_NAME = "taskClass_name_";

	@Autowired
	private TaskClassDAO taskClassDAO;
	
	@Autowired
	protected IMultiTextManager multiTextManager;

	@Autowired
	protected TaskClassMapper taskClassMapper;
	
	public TaskClassManager() {

	}

	@Override
	public TaskClassDTO create(TaskClassDTO taskClass) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskClassDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskClassDTO update(TaskClassDTO taskClass) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskClassDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskClassDTO getByName(String multiKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskClassDTO> getTaskClassByLang(String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskClassDTO> getTaskClass() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/*
	public TaskClassDTO create(TaskClassDTO taskClass) throws Exception {
		EntityManager em = getEntityManager();
		TaskClass entityTaskClass = taskClassMapper.map(
				taskClass);
		try {
			em.getTransaction().begin();
			em.persist(entityTaskClass);
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
		return taskClassMapper.map(entityTaskClass);
	}

	public TaskClassDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		TaskClass oldEntityTaskClass = new TaskClass();
		try {
			em.getTransaction().begin();
			TaskClass entityTaskClass = (TaskClass) em.find(TaskClass.class, id);
			PropertyUtils.copyProperties(oldEntityTaskClass, entityTaskClass);
			em.remove(em.merge(entityTaskClass));
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
		return taskClassMapper
				.map(oldEntityTaskClass);
	}

	public TaskClassDTO update(TaskClassDTO taskClass) throws Exception {
		EntityManager em = getEntityManager();
		TaskClass entityTaskClass = taskClassMapper.map(
				taskClass);
		TaskClass oldEntityTaskClass = null;
		try {
			em.getTransaction().begin();
			oldEntityTaskClass = (TaskClass) em.find(TaskClass.class, entityTaskClass.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityTaskClass,
					oldEntityTaskClass);
			entityTaskClass = em.merge(entityTaskClass);
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
		return taskClassMapper.map(entityTaskClass);
	}

	public TaskClassDTO getById(long id) {
		TaskClass entityTaskClass = null;
		EntityManager em = getEntityManager();
		try {
			entityTaskClass = (TaskClass) em.find(TaskClass.class, id);
		} finally {
			em.close();
		}
		return taskClassMapper.map(entityTaskClass);
	}
	
	public TaskClassDTO getByName(String multiKey) {
		EntityManager em = getEntityManager();
		List<TaskClass> resultQuery = null;
		TaskClassDTO taskClass = null;
		try {
			Query query = em.createNamedQuery("getTaskClassMultiKey");
			query.setParameter("tclNameMulti", multiKey);
			resultQuery = (List<TaskClass>) query.getResultList();
			if (resultQuery.size() == 1) {
				taskClass = taskClassMapper.map(
						resultQuery.get(0));
			}
		} finally {
			em.close();
		}
		return taskClass;
	}
	
	public List<TaskClassDTO> getTaskClassByLang(String lang) {
		EntityManager em = getEntityManager();
		List<TaskClassDTO> result = new ArrayList<TaskClassDTO>();
		List<TaskClass> resultQuery = null;
		TaskClassDTO taskClass = null;
		MultiTextDTO multiTextKey = null;
		try {
			Query query = em.createNamedQuery("getTaskClass");
			resultQuery = (List<TaskClass>) query.getResultList();
			String name = "";
			for (TaskClass entityTaskClass : resultQuery) {
				taskClass = taskClassMapper.map(
						entityTaskClass);
				multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
						taskClass.getTclNameMulti());
				name = multiTextKey.getMulText();
				if (name != null) {
					taskClass.setTclName(name);
					result.add(taskClass);
				}
			}
		} finally {
			em.close();
		}
		return result;
	}

	public List<TaskClassDTO> getTaskClass() {
		EntityManager em = getEntityManager();
		List<TaskClassDTO> result = new ArrayList<TaskClassDTO>();
		List<TaskClass> resultQuery = null;
		TaskClassDTO taskClass = null;
		try {
			Query query = em.createNamedQuery("getTaskClass");
			resultQuery = (List<TaskClass>) query.getResultList();
			for (TaskClass entityTaskClass : resultQuery) {
				taskClass = taskClassMapper.map(
						entityTaskClass);
				result.add(taskClass);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setMultiTextDAO(multiTextManager multiTextManager) {
		this.multiTextManager = multiTextManager;
	}

	public void setTaskClassTransformer(TaskClassMapper taskClassMapper) {
		this.taskClassMapper = taskClassMapper;
	}
*/
	
	
}