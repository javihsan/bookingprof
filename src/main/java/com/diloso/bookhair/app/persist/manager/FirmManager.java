package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.config.impl.ConfigFirm;
import com.diloso.bookhair.app.negocio.dao.FirmDAO;
import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.utils.ApplicationContextProvider;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Firm;
import com.diloso.bookhair.app.persist.transformer.FirmTransformer;

@Component
@Scope(value = "singleton")
public class FirmManager extends Manager implements FirmDAO {

	//@Autowired
	protected FirmTransformer firmTransformer;
	
	public FirmManager() {
		if (firmTransformer==null){
			firmTransformer = new FirmTransformer();
		}
	}
	
	public FirmDTO create(FirmDTO firm) throws Exception {
		EntityManager em = getEntityManager();
		Firm entityFirm = firmTransformer.transformDTOToEntity(
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
		return firmTransformer.transformEntityToDTO(entityFirm);
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
		return firmTransformer.transformEntityToDTO(oldEntityFirm);
	}

	public FirmDTO update(FirmDTO firm) throws Exception {
		EntityManager em = getEntityManager();
		Firm entityFirm = firmTransformer.transformDTOToEntity(firm);
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
		return firmTransformer.transformEntityToDTO(entityFirm);
	}

	public FirmDTO getById(long id) {
		Firm entityFirm = null;
		EntityManager em = getEntityManager();
		try {
			entityFirm = (Firm) em.find(Firm.class, id);
		} finally {
			em.close();
		}
		return firmTransformer.transformEntityToDTO(entityFirm);
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
				firm = firmTransformer.transformEntityToDTO(
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
				firm = firmTransformer.transformEntityToDTO(
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
				firm = firmTransformer.transformEntityToDTO(
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
				firm = firmTransformer.transformEntityToDTO(
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

	public void setFirmTransformer(FirmTransformer firmTransformer) {
		this.firmTransformer = firmTransformer;
	}
	
}