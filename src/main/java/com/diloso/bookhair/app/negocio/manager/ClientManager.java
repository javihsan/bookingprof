package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.ClientDAO;
import com.diloso.bookhair.app.persist.entities.Client;
import com.diloso.bookhair.app.persist.mapper.ClientMapper;

@Component
@Scope(value = "singleton")
public class ClientManager implements IClientManager {

	
	public static final String ENABLED = "enabled";
	public static final String RES_FIR_ID = "resFirId";
	public static final String WHO_EMAIL = "whoEmail";
	public static final String ORDER_KEY_DESC = "-__key__";
	
	@Autowired
	private ClientDAO clientDAO;
	
	@Autowired
	protected ClientMapper mapper;
	
	public ClientManager() {

	}

	@Override
	public ClientDTO create(ClientDTO clientDTO) throws Exception {
		Client client = mapper.map(clientDTO);
		client = clientDAO.create(client);
		return mapper.map(client);
	}

	@Override
	public ClientDTO remove(long id) throws Exception {
		Client client = clientDAO.get(id);
		clientDAO.delete(id);
		if (client == null) {
			return null;
		}
		return mapper.map(client);
	}

	@Override
	public ClientDTO update(ClientDTO clientDTO) throws Exception {
		Client client = mapper.map(clientDTO);
		Client oldClient = clientDAO.get(clientDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(client, oldClient);
		} catch (Exception e) {
		}
		client = clientDAO.update(client);
		if (client == null) {
			return null;
		}
		return mapper.map(client);
	}

	@Override
	public ClientDTO getById(long id) {
		Client client = clientDAO.get(id);
		if (client == null) {
			return null;
		}
		return mapper.map(client);		
	}

	@Override
	public ClientDTO getByEmail(long resFirId, String email) {
		
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(RES_FIR_ID, resFirId);
		filters.put(WHO_EMAIL, email);
		filters.put(ENABLED, 1);
		List<Client> resultQuery = clientDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}

	@Override
	public List<ClientDTO> getClient(long resFirId) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(RES_FIR_ID, resFirId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_DESC);
		List<Client> resultQuery = clientDAO.listOrderFilter(filters, orders);
		List<ClientDTO> result = new ArrayList<ClientDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});
		return result;			
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