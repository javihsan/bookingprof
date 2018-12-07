package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.LocalTaskDAO;
import com.diloso.bookhair.app.negocio.dao.MultiTextDAO;
import com.diloso.bookhair.app.negocio.dao.TaskDAO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.MultiTextDTO;
import com.diloso.bookhair.app.negocio.dto.TaskDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.LocalTask;
import com.diloso.bookhair.app.persist.transformer.LocalTaskTransformer;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@Component
@Scope(value = "singleton")
public class LocalTaskManager extends Manager implements LocalTaskDAO {

	public static final String KEY_MULTI_LOCAL_TASK_NAME = "local_task_name_";
	public static final String FIELD_MULTI_LOCAL_TASK_NAME = "lotNameMulti";
	public static final String FIELD_MULTI_LOCAL_TASK_ENTITY_NAME = FIELD_MULTI_LOCAL_TASK_NAME + "Id";

	@Autowired
	protected MultiTextDAO multiTextDAO;
	
	@Autowired
	protected TaskDAO taskDAO;
	
	@Autowired
	protected LocalTaskTransformer localTaskTransformer;
	
	public LocalTaskManager() {

	}

	public LocalTaskDTO create(LocalTaskDTO localTask) throws Exception {
		EntityManager em = getEntityManager();
		LocalTask entityLocalTask = localTaskTransformer
				.transformDTOToEntity(localTask);
		try {
			em.getTransaction().begin();
			em.persist(entityLocalTask);
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
		return localTaskTransformer.transformEntityToDTO(
				entityLocalTask);
	}

	public LocalTaskDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		LocalTask oldEntityLocalTask = new LocalTask();
		try {
			em.getTransaction().begin();
			LocalTask entityLocalTask = (LocalTask) em
					.find(LocalTask.class, id);
			PropertyUtils.copyProperties(oldEntityLocalTask, entityLocalTask);
			em.remove(em.merge(entityLocalTask));
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
		return localTaskTransformer.transformEntityToDTO(
				oldEntityLocalTask);
	}

	public LocalTaskDTO update(LocalTaskDTO localTask) throws Exception {
		EntityManager em = getEntityManager();
		LocalTask entityLocalTask = localTaskTransformer
				.transformDTOToEntity(localTask);
		LocalTask oldEntityLocalTask = null;
		try {
			em.getTransaction().begin();
			oldEntityLocalTask = (LocalTask) em.find(LocalTask.class,
					entityLocalTask.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityLocalTask,
					oldEntityLocalTask);
			entityLocalTask = em.merge(entityLocalTask);
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
		return localTaskTransformer.transformEntityToDTO(
				entityLocalTask);
	}

	public LocalTaskDTO getById(long id) {
		Entity entityLocalTask = null;
		try {
			Key k = KeyFactory.createKey(LocalTask.class.getSimpleName(), id);
			entityLocalTask = DatastoreServiceFactory.getDatastoreService()
					.get(k);
		} catch (Exception ex) {
		}
		return localTaskTransformer.transformEntityToDTO(
				entityLocalTask);
	}
	
	public LocalTaskDTO getByName(String multiKey) {
		EntityManager em = getEntityManager();
		List<LocalTask> resultQuery = null;
		LocalTaskDTO localTask = null;
		try {
			Query query = em.createNamedQuery("getLocalTaskMultiKey");
			query.setParameter("lotNameMulti", multiKey);
			resultQuery = (List<LocalTask>) query.getResultList();
			if (resultQuery.size() == 1) {
				localTask = localTaskTransformer.transformEntityToDTO(
						resultQuery.get(0));
			}
		} finally {
			em.close();
		}
		return localTask;
	}

	public List<LocalTaskDTO> getLocalTaskSimple(long lotLocalId, String lang) {
		EntityManager em = getEntityManager();
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		List<LocalTask> resultQuery = null;
		LocalTaskDTO localTask = null;
		MultiTextDTO multiTextKey = null;
		try {
			Query query = em.createNamedQuery("getLocalTask");
			query.setParameter("lotLocalId", lotLocalId);
			resultQuery = (List<LocalTask>) query.getResultList();
			String name = "";
			for (LocalTask entityLocalTask : resultQuery) {
				if ((entityLocalTask.getLotTaskCombiId() == null
						|| entityLocalTask.getLotTaskCombiId().size() == 0) && entityLocalTask.getLotTaskDuration()>0) {
					localTask = localTaskTransformer
							.transformEntityToDTO(entityLocalTask);
					multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
							localTask.getLotNameMulti());
					name = multiTextKey.getMulText();
					localTask.setLotName(name);
					result.add(localTask);
				}
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public List<LocalTaskDTO> getLocalTaskSimpleInv(long lotLocalId, String lang) {
		EntityManager em = getEntityManager();
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		List<LocalTask> resultQuery = null;
		LocalTaskDTO localTask = null;
		MultiTextDTO multiTextKey = null;
		try {
			Query query = em.createNamedQuery("getLocalTask");
			query.setParameter("lotLocalId", lotLocalId);
			resultQuery = (List<LocalTask>) query.getResultList();
			String name = "";
			for (LocalTask entityLocalTask : resultQuery) {
				if ((entityLocalTask.getLotTaskCombiId() == null
						|| entityLocalTask.getLotTaskCombiId().size() == 0) && entityLocalTask.getLotTaskRate()>0) {
					localTask = localTaskTransformer
							.transformEntityToDTO(entityLocalTask);
					multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
							localTask.getLotNameMulti());
					name = multiTextKey.getMulText();
					localTask.setLotName(name);
					result.add(localTask);
				}
			}
		} finally {
			em.close();
		}
		return result;
	}

	public List<LocalTaskDTO> getLocalTaskCombi(long lotLocalId, String lang,
			String charAND) {
		EntityManager em = getEntityManager();
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		List<LocalTask> resultQuery = null;
		LocalTaskDTO localTask = null;
		MultiTextDTO multiTextKey = null;
		try {
			Query query = em.createNamedQuery("getLocalTask");
			query.setParameter("lotLocalId", lotLocalId);
			resultQuery = (List<LocalTask>) query.getResultList();
			String name = "";
			for (LocalTask entityLocalTask : resultQuery) {
				if (entityLocalTask.getLotTaskCombiId() != null
						&& entityLocalTask.getLotTaskCombiId().size() > 0) {
					localTask = localTaskTransformer
							.transformEntityToDTO(entityLocalTask);
					name = "";
					for (Long taskId : localTask.getLotTaskCombiId()) {
						if (name.length() > 0) {
							name += " " + charAND + " ";
						}
						multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
								getById(taskId).getLotNameMulti());
						name += multiTextKey.getMulText();
					}
					localTask.setLotName(name);
					result.add(localTask);
				}
			}
		} finally {
			em.close();
		}
		return result;
	}

	public List<LocalTaskDTO> getLocalTaskAndCombi(long lotLocalId,
			String lang, String charAND) {
		EntityManager em = getEntityManager();
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		List<LocalTask> resultQuery = null;
		LocalTaskDTO localTask = null;
		MultiTextDTO multiTextKey = null;
		TaskDTO taskParent = null;
		try {
			Query query = em.createNamedQuery("getLocalTask");
			query.setParameter("lotLocalId", lotLocalId);
			resultQuery = (List<LocalTask>) query.getResultList();
			String name = null;
			for (LocalTask entityLocalTask : resultQuery) {
				localTask = localTaskTransformer
						.transformEntityToDTO(entityLocalTask);
				name = "";
				if (localTask.getLotTaskCombiId() != null
						&& localTask.getLotTaskCombiId().size() > 0) {
					for (Long taskId : localTask.getLotTaskCombiId()) {
						if (name.length() > 0) {
							name += " " + charAND + " ";
						}
						multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
								getById(taskId).getLotNameMulti());
						name += multiTextKey.getMulText();
					}
				} else if (localTask.getLotTaskDuration()>0) {
					multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
							localTask.getLotNameMulti());
					name = multiTextKey.getMulText();
				}
				if (name.length()>0){
					localTask.setLotName(name);
					result.add(localTask);
				}
			}

		} finally {
			em.close();
		}
		return result;
	}

	public List<LocalTaskDTO> getLocalTaskAndCombiVisible(long lotLocalId,
			String lang, String charAND) {
		EntityManager em = getEntityManager();
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		List<LocalTask> resultQuery = null;
		LocalTaskDTO localTask = null;
		MultiTextDTO multiTextKey = null;
		try {
			Query query = em.createNamedQuery("getLocalTaskVisible");
			query.setParameter("lotLocalId", lotLocalId);
			resultQuery = (List<LocalTask>) query.getResultList();
			String name = null;
			for (LocalTask entityLocalTask : resultQuery) {
				localTask = localTaskTransformer
						.transformEntityToDTO(entityLocalTask);
				name = "";
				if (localTask.getLotTaskCombiId() != null
						&& localTask.getLotTaskCombiId().size() > 0) {
					for (Long taskId : localTask.getLotTaskCombiId()) {
						if (name.length() > 0) {
							name += " " + charAND + " ";
						}
						multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
								getById(taskId).getLotNameMulti());
						name += multiTextKey.getMulText();
					}
				} else if (localTask.getLotTaskDuration()>0) {
					multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
							localTask.getLotNameMulti());
					name = multiTextKey.getMulText();
				}
				if (name.length()>0){
					localTask.setLotName(name);
					result.add(localTask);
				}
			}

		} finally {
			em.close();
		}
		return result;
	}
	
	public List<LocalTaskDTO> getLocalTask(long lotLocalId,
			String lang, String charAND) {
		EntityManager em = getEntityManager();
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		List<LocalTask> resultQuery = null;
		LocalTaskDTO localTask = null;
		MultiTextDTO multiTextKey = null;
		TaskDTO taskParent = null;
		try {
			Query query = em.createNamedQuery("getLocalTask");
			query.setParameter("lotLocalId", lotLocalId);
			resultQuery = (List<LocalTask>) query.getResultList();
			String name = null;
			for (LocalTask entityLocalTask : resultQuery) {
				localTask = localTaskTransformer
						.transformEntityToDTO(entityLocalTask);
				if (localTask.getLotTaskCombiId() != null
						&& localTask.getLotTaskCombiId().size() > 0) {
					name = "";
					for (Long taskId : localTask.getLotTaskCombiId()) {
						if (name.length() > 0) {
							name += " " + charAND + " ";
						}
						multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
								getById(taskId).getLotNameMulti());
						name += multiTextKey.getMulText();
					}
				} else {
					multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
							localTask.getLotNameMulti());
					name = multiTextKey.getMulText();
				}
				localTask.setLotName(name);
				result.add(localTask);
			}

		} finally {
			em.close();
		}
		return result;
	}

	
	public List<LocalTaskDTO> getLocalTaskSimpleAdmin(long lotLocalId,
			String lang) {
		EntityManager em = getEntityManager();
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		List<LocalTask> resultQuery = null;
		LocalTaskDTO localTask = null;
		MultiTextDTO multiTextKey = null;
		try {
			Query query = em.createNamedQuery("getLocalTaskAdmin");
			query.setParameter("lotLocalId", lotLocalId);
			resultQuery = (List<LocalTask>) query.getResultList();
			String name = "";
			for (LocalTask entityLocalTask : resultQuery) {
				if (entityLocalTask.getLotTaskCombiId() == null
						|| entityLocalTask.getLotTaskCombiId().size() == 0) {
					localTask = localTaskTransformer
							.transformEntityToDTO(entityLocalTask);
					multiTextKey = multiTextDAO.getByLanCodeAndKey(lang,
							localTask.getLotNameMulti());
					name = multiTextKey.getMulText();
					localTask.setLotName(name);
					result.add(localTask);
				}
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public List<LocalTaskDTO> getLocalTaskAdmin(long lotLocalId, String lang) {
		EntityManager em = getEntityManager();
		List<LocalTaskDTO> result = new ArrayList<LocalTaskDTO>();
		List<LocalTask> resultQuery = null;
		LocalTaskDTO localTask = null;
		try {
			Query query = em.createNamedQuery("getLocalTaskAdmin");
			query.setParameter("lotLocalId", lotLocalId);
			resultQuery = (List<LocalTask>) query.getResultList();
			for (LocalTask entityLocalTask : resultQuery) {
				localTask = localTaskTransformer
							.transformEntityToDTO(entityLocalTask);
				result.add(localTask);
			}
		} finally {
			em.close();
		}
		return result;
	}
	
}