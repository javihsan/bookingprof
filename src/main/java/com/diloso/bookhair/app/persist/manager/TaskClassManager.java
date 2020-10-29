package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.TaskClassDAO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.TaskClassDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.TaskClass;
import com.diloso.bookhair.app.persist.transformer.TaskClassTransformer;

@Component
@Scope(value = "singleton")
public class TaskClassManager extends Manager implements TaskClassDAO {

	public static final String KEY_MULTI_TASKCLASS_NAME = "taskClass_name_";

	//@Autowired
	protected MultiTextDAO multiTextDAO;

	//@Autowired
	protected TaskClassTransformer taskClassTransformer;
	
	public TaskClassManager() {

	}

	public TaskClassDTO create(TaskClassDTO taskClass) throws Exception {
		EntityManager em = getEntityManager();
		TaskClass entityTaskClass = taskClassTransformer.transformDTOToEntity(
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
		return taskClassTransformer.transformEntityToDTO(entityTaskClass);
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
		return taskClassTransformer
				.transformEntityToDTO(oldEntityTaskClass);
	}

	public TaskClassDTO update(TaskClassDTO taskClass) throws Exception {
		EntityManager em = getEntityManager();
		TaskClass entityTaskClass = taskClassTransformer.transformDTOToEntity(
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
		return taskClassTransformer.transformEntityToDTO(entityTaskClass);
	}

	public TaskClassDTO getById(long id) {
		TaskClass entityTaskClass = null;
		EntityManager em = getEntityManager();
		try {
			entityTaskClass = (TaskClass) em.find(TaskClass.class, id);
		} finally {
			em.close();
		}
		return taskClassTransformer.transformEntityToDTO(entityTaskClass);
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
				taskClass = taskClassTransformer.transformEntityToDTO(
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
				taskClass = taskClassTransformer.transformEntityToDTO(
						entityTaskClass);
				multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
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
				taskClass = taskClassTransformer.transformEntityToDTO(
						entityTaskClass);
				result.add(taskClass);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setMultiTextDAO(MultiTextDAO multiTextDAO) {
		this.multiTextDAO = multiTextDAO;
	}

	public void setTaskClassTransformer(TaskClassTransformer taskClassTransformer) {
		this.taskClassTransformer = taskClassTransformer;
	}

	
	
}