package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.persist.dao.ClientDAO;
import com.diloso.bookhair.app.persist.mapper.ClientMapper;

@Component
@Scope(value = "singleton")
public class ClientManager implements IClientManager {

	@Autowired
	private ClientDAO clientDAO;
	
	@Autowired
	protected ClientMapper clientMapper;
	
	public ClientManager() {

	}

	@Override
	public ClientDTO create(ClientDTO client) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDTO remove(long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDTO update(ClientDTO client) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDTO getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientDTO getByEmail(long resFirId, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ClientDTO> getClient(long resFirId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
/*
	public ClientDTO create(ClientDTO client) throws Exception {
		EntityManager em = getEntityManager();
		Client entityClient = clientMapper
				.map(client);
		try {
			em.getTransaction().begin();
			em.persist(entityClient);
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
		return clientMapper.map(
				entityClient);
	}

	public ClientDTO remove(long id) throws Exception {
		EntityManager em = getEntityManager();
		Client oldEntityClient = new Client();
		try {
			em.getTransaction().begin();
			Client entityClient = (Client) em.find(Client.class, id);
			PropertyUtils.copyProperties(oldEntityClient, entityClient);
			em.remove(em.merge(entityClient));
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
		return clientMapper.map(
				oldEntityClient);
	}

	public ClientDTO update(ClientDTO client) throws Exception {
		EntityManager em = getEntityManager();
		Client entityClient = clientMapper
				.map(client);
		Client oldEntityClient = null;
		try {
			em.getTransaction().begin();
			oldEntityClient = (Client) em.find(Client.class,
					entityClient.getId());
			new NullAwareBeanUtilsBean().copyProperties(entityClient,
					oldEntityClient);
			entityClient = em.merge(entityClient);
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
		return clientMapper.map(
				entityClient);
	}

	public ClientDTO getById(long id) {
		Client entityClient = null;
		EntityManager em = getEntityManager();
		try {
			entityClient = (Client) em.find(Client.class, id);
		} finally {
			em.close();
		}
		return clientMapper.map(
				entityClient);
	}

	public ClientDTO getByEmail(long resFirId, String email) {
		EntityManager em = getEntityManager();
		List<Client> resultQuery = null;
		ClientDTO client = null;
		try {
			Query query = em.createNamedQuery("getClientEmail");
			query.setParameter("resFirId", resFirId);
			query.setParameter("whoEmail", email);
			resultQuery = (List<Client>) query.getResultList();
			if (resultQuery.size() == 1) {
				client = clientMapper.map(
						resultQuery.get(0));
			}
		} finally {
			em.close();
		}
		return client;
	}

	public List<ClientDTO> getClient(long resFirId) {
		EntityManager em = getEntityManager();
		List<ClientDTO> result = new ArrayList<ClientDTO>();
		List<Client> resultQuery = null;
		ClientDTO client = null;
		try {

			javax.persistence.Query query = em.createNamedQuery("getClient");
			query.setParameter("resFirId", resFirId);
			resultQuery = (List<Client>) query.getResultList();
			for (Client entityClient : resultQuery) {
				client = clientMapper.map(
						entityClient);
				result.add(client);
			}

		} finally {
			em.close();
		}
		return result;
	}

	public void setClientTransformer(ClientMapper clientMapper) {
		this.clientMapper = clientMapper;
	}
	*/
	

}