package com.diloso.bookhair.app.persist.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.LangDAO;
import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.entities.Lang;
import com.diloso.bookhair.app.persist.transformer.LangTransformer;

@Component
@Scope(value = "singleton")
public class LangManager extends Manager implements LangDAO {
	
	//@Autowired
	protected LangTransformer langTransformer;
	
	public LangManager() {
		if (langTransformer==null){
			langTransformer = new LangTransformer();
		}
	}

	public LangDTO create(LangDTO lang) throws Exception {
		EntityManager em = getEntityManager();
		Lang entityLang =langTransformer.transformDTOToEntity(
				lang);
		try {
			em.getTransaction().begin();
			em.persist(entityLang);
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
		return langTransformer.transformEntityToDTO(entityLang);
	}

	public LangDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Lang oldEntityLang = new Lang();
		try {
			em.getTransaction().begin();
			Lang entityLang = (Lang) em.find(Lang.class, id);
			PropertyUtils.copyProperties(oldEntityLang, entityLang);
			em.remove(em.merge(entityLang));
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
		return langTransformer
				.transformEntityToDTO(oldEntityLang);
	}

	public LangDTO update(LangDTO lang) throws Exception {
		EntityManager em = getEntityManager();
		Lang entityLang =langTransformer.transformDTOToEntity(
				lang);
		Lang oldEntityLang = null;
		try {
			em.getTransaction().begin();
			oldEntityLang = (Lang) em.find(Lang.class, entityLang.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityLang,
					oldEntityLang);
			entityLang = em.merge(entityLang);
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
		return langTransformer.transformEntityToDTO(entityLang);
	}

	public LangDTO getById(long id) {
		Lang entityLang = null;
		EntityManager em = getEntityManager();
		try {
			entityLang = (Lang) em.find(Lang.class, id);
		} finally {
			em.close();
		}
		return langTransformer.transformEntityToDTO(entityLang);
	}

	public LangDTO getByName(String name) {
		EntityManager em = getEntityManager();
		List<Lang> resultQuery = null;
		LangDTO lang = null;
		try {
			Query query = em.createNamedQuery("getLangName");
			query.setParameter("lanName", name);
			resultQuery = (List<Lang>) query.getResultList();
			if (resultQuery.size() == 1) {
				lang =langTransformer.transformEntityToDTO(
						resultQuery.get(0));
			}
		} finally {
			em.close();
		}
		return lang;
	}

	public LangDTO getByCode(String lanCode) {
		EntityManager em = getEntityManager();
		List<Lang> resultQuery = null;
		LangDTO lang = null;
		try {
			Query query = em.createNamedQuery("getLangCode");
			query.setParameter("lanCode", lanCode);
			resultQuery = (List<Lang>) query.getResultList();
			if (resultQuery.size() == 1) {
				lang =langTransformer.transformEntityToDTO(
						resultQuery.get(0));
			}
		} finally {
			em.close();
		}
		return lang;
	}

	public List<LangDTO> getLang() {
		EntityManager em = getEntityManager();
		List<LangDTO> result = new ArrayList<LangDTO>();
		List<Lang> resultQuery = null;
		LangDTO lang = null;
		try {
			Query query = em.createNamedQuery("getLang");
			resultQuery = (List<Lang>) query.getResultList();
			for (Lang entityLang : resultQuery) {
				lang = langTransformer.transformEntityToDTO(
						entityLang);
				result.add(lang);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setLangTransformer(LangTransformer langTransformer) {
		this.langTransformer = langTransformer;
	}
	
	
}