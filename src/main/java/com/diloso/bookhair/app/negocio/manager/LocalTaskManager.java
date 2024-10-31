package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.persist.dao.LocalTaskDAO;
import com.diloso.bookhair.app.persist.mapper.LocalTaskMapper;

@Component
@Scope(value = "singleton")
public class LocalTaskManager implements ILocalTaskManager {

	public static final String KEY_MULTI_LOCAL_TASK_NAME = "local_task_name_";
	public static final String FIELD_MULTI_LOCAL_TASK_NAME = "lotNameMulti";
	public static final String FIELD_MULTI_LOCAL_TASK_ENTITY_NAME = FIELD_MULTI_LOCAL_TASK_NAME + "Id";

	@Autowired
	private LocalTaskDAO localTaskDAO;
	
	@Autowired
	protected IMultiTextManager multiTextManager;
	
	@Autowired
	protected ITaskManager taskManager;
	
	@Autowired
	protected LocalTaskMapper localTaskMapper;
	
	public LocalTaskManager() {

	}

	@Override
	public LocalTaskDTO create(LocalTaskDTO task) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTaskDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTaskDTO update(LocalTaskDTO task) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTaskDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTaskDTO getByName(String multiKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskSimple(long lotLocalId, String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskSimpleInv(long lotLocalId, String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskCombi(long lotLocalId, String lang, String charAND) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskAndCombi(long lotLocalId, String lang, String charAND) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskAndCombiVisible(long lotLocalId, String lang, String charAND) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalTaskDTO> getLocalTask(long lotLocalId, String lang, String charAND) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskSimpleAdmin(long lotLocalId, String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalTaskDTO> getLocalTaskAdmin(long lotLocalId, String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	/*
	public LocalTaskDTO create(LocalTaskDTO localTask) throws Exception {
		EntityManager em = getEntityManager();
		LocalTask entityLocalTask = localTaskMapper
				.map(localTask);
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
		return localTaskMapper.map(
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
		return localTaskMapper.map(
				oldEntityLocalTask);
	}

	public LocalTaskDTO update(LocalTaskDTO localTask) throws Exception {
		EntityManager em = getEntityManager();
		LocalTask entityLocalTask = localTaskMapper
				.map(localTask);
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
		return localTaskMapper.map(
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
		return localTaskMapper.map(
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
				localTask = localTaskMapper.map(
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
					localTask = localTaskMapper
							.map(entityLocalTask);
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
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
					localTask = localTaskMapper
							.map(entityLocalTask);
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
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
					localTask = localTaskMapper
							.map(entityLocalTask);
					name = "";
					for (Long taskId : localTask.getLotTaskCombiId()) {
						if (name.length() > 0) {
							name += " " + charAND + " ";
						}
						multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
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
				localTask = localTaskMapper
						.map(entityLocalTask);
				name = "";
				if (localTask.getLotTaskCombiId() != null
						&& localTask.getLotTaskCombiId().size() > 0) {
					for (Long taskId : localTask.getLotTaskCombiId()) {
						if (name.length() > 0) {
							name += " " + charAND + " ";
						}
						multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
								getById(taskId).getLotNameMulti());
						name += multiTextKey.getMulText();
					}
				} else if (localTask.getLotTaskDuration()>0) {
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
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
				localTask = localTaskMapper
						.map(entityLocalTask);
				name = "";
				if (localTask.getLotTaskCombiId() != null
						&& localTask.getLotTaskCombiId().size() > 0) {
					for (Long taskId : localTask.getLotTaskCombiId()) {
						if (name.length() > 0) {
							name += " " + charAND + " ";
						}
						multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
								getById(taskId).getLotNameMulti());
						name += multiTextKey.getMulText();
					}
				} else if (localTask.getLotTaskDuration()>0) {
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
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
				localTask = localTaskMapper
						.map(entityLocalTask);
				if (localTask.getLotTaskCombiId() != null
						&& localTask.getLotTaskCombiId().size() > 0) {
					name = "";
					for (Long taskId : localTask.getLotTaskCombiId()) {
						if (name.length() > 0) {
							name += " " + charAND + " ";
						}
						multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
								getById(taskId).getLotNameMulti());
						name += multiTextKey.getMulText();
					}
				} else {
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
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
					localTask = localTaskMapper
							.map(entityLocalTask);
					multiTextKey = multiTextManager.getByLanCodeAndKey(lang,
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
				localTask = localTaskMapper
							.map(entityLocalTask);
				result.add(localTask);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setMultiTextDAO(IMultiTextManager multiTextManager) {
		this.multiTextManager = multiTextManager;
	}

	public void setTaskDAO(ITaskManager taskManager) {
		this.taskManager = taskManager;
	}

	public void setLocalTaskTransformer(LocalTaskMapper localTaskMapper) {
		this.localTaskMapper = localTaskMapper;
	}
	*/
	
	
}