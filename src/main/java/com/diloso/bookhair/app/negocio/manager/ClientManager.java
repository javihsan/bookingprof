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
	
}