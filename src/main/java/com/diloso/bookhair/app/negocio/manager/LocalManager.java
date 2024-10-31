package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.LocalDTO;
import com.diloso.bookhair.app.persist.dao.LocalDAO;
import com.diloso.bookhair.app.persist.entities.Local;
import com.diloso.bookhair.app.persist.mapper.LocalMapper;

@Component
@Scope(value = "singleton")
public class LocalManager implements ILocalManager {

	public static final String ENABLED = "enabled";
	public static final String LOC_FIR_ID = "resFirId";
	public static final String ORDER_KY_DESC = "-__key__";
	
	@Autowired
	private LocalDAO localDAO;
	
	@Autowired
	protected LocalMapper mapper;
	
	public LocalManager() {

	}

	@Override
	public LocalDTO create(LocalDTO localDTO) throws Exception {
		Local local = mapper.map(localDTO);
		local = localDAO.create(local);
		return mapper.map(local);
	}

	@Override
	public LocalDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalDTO update(LocalDTO local) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalDTO getById(long id) {
		Local local = localDAO.get(id);
		if (local == null) {
			return null;
		}
		return mapper.map(local);		
	}

	@Override
	public List<Long> getLocal(long resFirId) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(LOC_FIR_ID, resFirId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KY_DESC);
		List<Local> listLocal = localDAO.listOrderFilter(filters,orders);
		List<Long> result = new ArrayList<Long>();
		for (Local local : listLocal) {
			result.add(local.getId());
		}
		/*
		LocalDTO local = null;
		if (listLocal.size() == 1) {
			local = mapper.map(
					listLocal.get(0));
		}*/
		return result;
	}

	@Override
	public List<Long> getLocalClient(long resFirId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalDTO> getLocalList(long resFirId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalDTO> getLocalListClient(long resFirId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LocalDTO> getLocalAdmin(long resFirId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	public LocalDTO create(LocalDTO local) throws Exception {
		EntityManager em = getEntityManager();
		Local entityLocal = localMapper
				.map(local);
		try {
			em.getTransaction().begin();
			em.persist(entityLocal);
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
		return localMapper.map(entityLocal);
	}

	public LocalDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Local oldEntityLocal = new Local();
		try {
			em.getTransaction().begin();
			Local entityLocal = (Local) em.find(Local.class, id);
			PropertyUtils.copyProperties(oldEntityLocal, entityLocal);
			em.remove(em.merge(entityLocal));
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
		return localMapper.map(
				oldEntityLocal);
	}

	public LocalDTO update(LocalDTO local) throws Exception {
		EntityManager em = getEntityManager();
		Local entityLocal = localMapper
				.map(local);
		Local oldEntityLocal = null;
		try {
			em.getTransaction().begin();
			oldEntityLocal = (Local) em.find(Local.class, entityLocal.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityLocal,
					oldEntityLocal);
			entityLocal = em.merge(entityLocal);
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
		return localMapper.map(entityLocal);
	}

	public LocalDTO getById(long id) {
		Local entityLocal = null;
		EntityManager em = getEntityManager();
		try {
			entityLocal = (Local) em.find(Local.class, id);
		} finally {
			em.close();
		}
		return localMapper.map(entityLocal);
	}


	public List<Long> getLocal(long resFirId) {
		EntityManager em = getEntityManager();
		List<Long> result = new ArrayList<Long>();
		List<Local> resultQuery = null;
		try {
			Query query = em.createNamedQuery("getLocal");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Local>) query.getResultList();
			for (Local entityLocal : resultQuery) {
				result.add(entityLocal.getId());
			}
		} finally {
			em.close();
		}
		return result;
	}

	public List<Long> getLocalClient(long resFirId) {
		EntityManager em = getEntityManager();
		List<Long> result = new ArrayList<Long>();
		List<Local> resultQuery = null;
		try {
			Query query = em.createNamedQuery("getLocalClient");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Local>) query.getResultList();
			for (Local entityLocal : resultQuery) {
				result.add(entityLocal.getId());
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	
	public List<LocalDTO> getLocalList(long resFirId) {
		EntityManager em = getEntityManager();
		List<LocalDTO> result = new ArrayList<LocalDTO>();
		List<Local> resultQuery = null;
		LocalDTO local = null;
		try {
			Query query = em.createNamedQuery("getLocal");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Local>) query.getResultList();
			for (Local entityLocal : resultQuery) {
				local = localMapper.map(entityLocal);
				result.add(local);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public List<LocalDTO> getLocalListClient(long resFirId) {
		EntityManager em = getEntityManager();
		List<LocalDTO> result = new ArrayList<LocalDTO>();
		List<Local> resultQuery = null;
		LocalDTO local = null;
		try {
			Query query = em.createNamedQuery("getLocalClient");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Local>) query.getResultList();
			for (Local entityLocal : resultQuery) {
				local = localMapper.map(entityLocal);
				result.add(local);
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public List<LocalDTO> getLocalAdmin(long resFirId) {
		EntityManager em = getEntityManager();
		List<LocalDTO> result = new ArrayList<LocalDTO>();
		List<Local> resultQuery = null;
		LocalDTO local = null;
		try {
			Query query = em.createNamedQuery("getLocalAdmin");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Local>) query.getResultList();
			for (Local entityLocal : resultQuery) {
				local = localMapper.map(entityLocal);
				result.add(local);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setLocalTransformer(LocalMapper localMapper) {
		this.localMapper = localMapper;
	}
	*/	
	
}