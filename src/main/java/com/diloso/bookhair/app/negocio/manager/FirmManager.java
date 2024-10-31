package com.diloso.bookhair.app.negocio.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.persist.dao.FirmDAO;
import com.diloso.bookhair.app.persist.entities.Firm;
import com.diloso.bookhair.app.persist.mapper.FirmMapper;

@Component
@Scope(value = "singleton")
public class FirmManager implements IFirmManager {

	public static final String ENABLED = "enabled";
	public static final String FIR_SERVER = "firServer";
	public static final String FIR_DOMAIN = "firDomain";
	
	
	@Autowired
	private FirmDAO firmDAO;
	
	@Autowired
	protected FirmMapper mapper;
	
	public FirmManager() {
	}
	
	@Override
	public FirmDTO create(FirmDTO firmDTO) throws Exception {
		Firm firm = mapper.map(firmDTO);
		firm = firmDAO.create(firm);
		return mapper.map(firm);
	}

	@Override
	public FirmDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmDTO update(FirmDTO firm) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public FirmDTO getById(long id) {
		Firm firm = firmDAO.get(id);
		if (firm == null) {
			return null;
		}
		return mapper.map(firm);
	}

	@Override
	public FirmDTO getFirmDomain(String domain) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(FIR_DOMAIN, domain);
		filters.put(ENABLED, 1);
		List<Firm> listFirm = firmDAO.listFilter(filters);
		
		FirmDTO firmDTO = null;
		if (listFirm.size() == 1) {
			firmDTO = mapper.map(
					listFirm.get(0));
		}
		return firmDTO;
	}

	@Override
	public FirmDTO getFirmDomainAdmin(String domain) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(FIR_DOMAIN, domain);
		List<Firm> listFirm = firmDAO.listFilter(filters);
		
		FirmDTO firmDTO = null;
		if (listFirm.size() == 1) {
			firmDTO = mapper.map(
					listFirm.get(0));
		}
		return firmDTO;
	}

	public String getDomainServer(String server) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(FIR_SERVER, server);
		filters.put(ENABLED, 1);
		List<Firm> listFirm = firmDAO.listFilter(filters);
		String result = null;
		if (listFirm.size() == 1) {
			result = listFirm.get(0).getFirDomain();
		}
		return result;
	}

	@Override
	public List<FirmDTO> getFirm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FirmDTO> getFirmAdmin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> findUsers(String domain) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	public Long findId(String domain) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(FIR_DOMAIN, domain);
		filters.put(ENABLED, 1);
		List<Firm> listFirm = firmDAO.listFilter(filters);
		Long result = null;
		if (listFirm.size() == 1) {
			result = listFirm.get(0).getId();
		}
		return result;
	}

	public boolean isRestrictedNivelUser(String domain) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(FIR_DOMAIN, domain);
		filters.put(ENABLED, 1);
		List<Firm> listFirm = firmDAO.listFilter(filters);
		boolean result = false;
		if (listFirm.size() == 1) {
			String numConfig = ConfigFirm.IDENT_DEFAULT;
			if (listFirm.get(0).getFirConfigNum() != null && !listFirm.get(0).getFirConfigNum().equals("")) {
				numConfig = listFirm.get(0).getFirConfigNum();
			} 
			ConfigFirm configFirm = (ConfigFirm) ApplicationContextProvider.getApplicationContext().getBean(ConfigFirm.PRE_IDENT_FIRM+numConfig);
			result = configFirm.getConfigAut().getConfigAutNivelUser()==1;
		}
		return result;		
	}*/
	
	
	
	/*
	public FirmDTO create(FirmDTO firm) throws Exception {
		EntityManager em = getEntityManager();
		Firm entityFirm = firmMapper.map(
				firm);
		try {
			em.getTransaction().begin();
			em.persist(entityFirm);
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
		return firmMapper.map(entityFirm);
	}

	public FirmDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Firm oldEntityFirm = new Firm();
		try {
			em.getTransaction().begin();
			Firm entityFirm = (Firm) em.find(Firm.class, id);
			PropertyUtils.copyProperties(oldEntityFirm, entityFirm);
			em.remove(em.merge(entityFirm));
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
		return firmMapper.map(oldEntityFirm);
	}

	public FirmDTO update(FirmDTO firm) throws Exception {
		EntityManager em = getEntityManager();
		Firm entityFirm = firmMapper.map(firm);
		Firm oldEntityFirm = null;
		try {
			em.getTransaction().begin();
			oldEntityFirm = (Firm) em.find(Firm.class, entityFirm.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityFirm,
					oldEntityFirm);
			entityFirm = em.merge(entityFirm);
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
		return firmMapper.map(entityFirm);
	}

	public FirmDTO getById(long id) {
		Firm entityFirm = null;
		EntityManager em = getEntityManager();
		try {
			entityFirm = (Firm) em.find(Firm.class, id);
		} finally {
			em.close();
		}
		return firmMapper.map(entityFirm);
	}

	public FirmDTO getFirmDomain(String domain) {
		EntityManager em = getEntityManager();
		List<Firm> resultQuery = null;
		FirmDTO firm = null;
		try {
			Query query = em.createNamedQuery("getFirmDomain");
			query.setParameter("firDomain", domain);
			resultQuery = (List<Firm>) query.getResultList();
			if (resultQuery.size() == 1) {
				firm = firmMapper.map(
						resultQuery.get(0));
			}
		} finally {
			em.close();
		}
		return firm;
	}

	public FirmDTO getFirmDomainAdmin(String domain) {
		EntityManager em = getEntityManager();
		List<Firm> resultQuery = null;
		FirmDTO firm = null;
		try {
			Query query = em.createNamedQuery("getFirmDomainAdmin");
			query.setParameter("firDomain", domain);
			resultQuery = (List<Firm>) query.getResultList();
			if (resultQuery.size() == 1) {
				firm = firmMapper.map(
						resultQuery.get(0));
			}
		} finally {
			em.close();
		}
		return firm;
	}
	
	public String getDomainServer(String server) {
		EntityManager em = getEntityManager();
		List<Firm> resultQuery = null;
		String result = null;
		try {
			Query query = em.createNamedQuery("getDomainServer");
			query.setParameter("firServer", server);
			resultQuery = (List<Firm>) query.getResultList();
			if (resultQuery.size() == 1) {
				result = resultQuery.get(0).getFirDomain();
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public List<FirmDTO> getFirm() {
		EntityManager em = getEntityManager();
		List<FirmDTO> result = new ArrayList<FirmDTO>();
		List<Firm> resultQuery = null;
		FirmDTO firm = null;
		try {
			Query query = em.createNamedQuery("getFirm");
			resultQuery = (List<Firm>) query.getResultList();
			for (Firm entityFirm : resultQuery) {
				firm = firmMapper.map(
						entityFirm);
				result.add(firm);
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public List<FirmDTO> getFirmAdmin() {
		EntityManager em = getEntityManager();
		List<FirmDTO> result = new ArrayList<FirmDTO>();
		List<Firm> resultQuery = null;
		FirmDTO firm = null;
		try {
			Query query = em.createNamedQuery("getFirmAdmin");
			resultQuery = (List<Firm>) query.getResultList();
			for (Firm entityFirm : resultQuery) {
				firm = firmMapper.map(
						entityFirm);
				result.add(firm);
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public List<String> findUsers(String domain) {
		EntityManager em = getEntityManager();
		List<String> result = new ArrayList<String>();
		List<Firm> resultQuery = null;
		try {
			Query query = em.createNamedQuery("getFirmDomain");
			query.setParameter("firDomain", domain);
			resultQuery = (List<Firm>) query.getResultList();
			if (resultQuery.size() == 1) {
				result = resultQuery.get(0).getFirGwtUsers();
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public Long findId(String domain) {
		EntityManager em = getEntityManager();
		Long result = null;
		List<Firm> resultQuery = null;
		try {
			Query query = em.createNamedQuery("getFirmDomain");
			query.setParameter("firDomain", domain);
			resultQuery = (List<Firm>) query.getResultList();
			if (resultQuery.size() == 1) {
				result = resultQuery.get(0).getId();
			}
		} finally {
			em.close();
		}
		return result;
	}
	
	public boolean isRestrictedNivelUser(String domain) {
		EntityManager em = getEntityManager();
		boolean result = false;
		List<Firm> resultQuery = null;
		try {
			Query query = em.createNamedQuery("getFirmDomain");
			query.setParameter("firDomain", domain);
			resultQuery = (List<Firm>) query.getResultList();
			if (resultQuery.size() == 1) {
				String numConfig = ConfigFirm.IDENT_DEFAULT;
				if (resultQuery.get(0).getFirConfigNum() != null && !resultQuery.get(0).getFirConfigNum().equals("")) {
					numConfig = resultQuery.get(0).getFirConfigNum();
				} 
				ConfigFirm configFirm = (ConfigFirm) ApplicationContextProvider.getApplicationContext().getBean(ConfigFirm.PRE_IDENT_FIRM+numConfig);
				result = configFirm.getConfigAut().getConfigAutNivelUser()==1;
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setFirmTransformer(FirmMapper firmMapper) {
		this.firmMapper = firmMapper;
	}
	*/
}