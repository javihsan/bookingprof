package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.LangDTO;
import com.diloso.bookhair.app.persist.dao.LangDAO;
import com.diloso.bookhair.app.persist.mapper.LangMapper;

@Component
@Scope(value = "singleton")
public class LangManager implements ILangManager {
	
	@Autowired
	private LangDAO langDAO;
	
	@Autowired
	protected LangMapper langMapper;
	
	public LangManager() {
		if (langMapper==null){
			langMapper = new LangMapper();
		}
	}

	@Override
	public LangDTO create(LangDTO lang) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LangDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LangDTO update(LangDTO lang) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LangDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LangDTO getByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LangDTO getByCode(String lanCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LangDTO> getLang() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	/*
	public LangDTO create(LangDTO lang) throws Exception {
		EntityManager em = getEntityManager();
		Lang entityLang =langMapper.map(
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
		return langMapper.map(entityLang);
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
		return langMapper
				.map(oldEntityLang);
	}

	public LangDTO update(LangDTO lang) throws Exception {
		EntityManager em = getEntityManager();
		Lang entityLang =langMapper.map(
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
		return langMapper.map(entityLang);
	}

	public LangDTO getById(long id) {
		Lang entityLang = null;
		EntityManager em = getEntityManager();
		try {
			entityLang = (Lang) em.find(Lang.class, id);
		} finally {
			em.close();
		}
		return langMapper.map(entityLang);
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
				lang =langMapper.map(
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
				lang =langMapper.map(
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
				lang = langMapper.map(
						entityLang);
				result.add(lang);
			}
		} finally {
			em.close();
		}
		return result;
	}

	public void setLangTransformer(LangMapper langMapper) {
		this.langMapper = langMapper;
	}
	*/
	
}