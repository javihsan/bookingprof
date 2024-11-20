package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.FirmDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.FirmDAO;
import com.diloso.bookhair.app.persist.entities.Firm;
import com.diloso.bookhair.app.persist.mapper.FirmMapper;

@Component
@Scope(value = "singleton")
public class FirmManager implements IFirmManager {

	public static final String ENABLED = "enabled";
	public static final String FIR_SERVER = "firServer";
	public static final String FIR_DOMAIN = "firDomain";
	public static final String ORDER_KEY_DESC = "-__key__";
	
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
		Firm firm = firmDAO.get(id);
		firmDAO.delete(id);
		if (firm == null) {
			return null;
		}
		return mapper.map(firm);
	}

	@Override
	public FirmDTO update(FirmDTO firmDTO) throws Exception {
		Firm firm = mapper.map(firmDTO);
		Firm oldFirm = firmDAO.get(firmDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(firm, oldFirm);
		} catch (Exception e) {
		}
		firm = firmDAO.update(firm);
		if (firm == null) {
			return null;
		}
		return mapper.map(firm);
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
		List<Firm> resultQuery = firmDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			return mapper.map(resultQuery.get(0));
		}
		return null;
	}

	@Override
	public FirmDTO getFirmDomainAdmin(String domain) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(FIR_DOMAIN, domain);
		List<Firm> resultQuery = firmDAO.listFilter(filters);
		FirmDTO firmDTO = null;
		if (resultQuery.size() == 1) {
			firmDTO = mapper.map(resultQuery.get(0));
		}
		return firmDTO;
	}

	public String getDomainServer(String server) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(FIR_SERVER, server);
		filters.put(ENABLED, 1);
		List<Firm> resultQuery = firmDAO.listFilter(filters);
		String result = null;
		if (resultQuery.size() == 1) {
			result = resultQuery.get(0).getFirDomain();
		}
		return result;
	}

	@Override
	public List<FirmDTO> getFirm() {
		List<FirmDTO> result = new ArrayList<FirmDTO>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_DESC);
		List<Firm> resultQuery = firmDAO.listOrderFilter(filters, orders);
		for (Firm entityFirm : resultQuery) {
			result.add(mapper.map(entityFirm));
		}
		return result;
	}

	@Override
	public List<FirmDTO> getFirmAdmin() {
		List<FirmDTO> result = new ArrayList<FirmDTO>();
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_KEY_DESC);
		List<Firm> resultQuery = firmDAO.listOrder(orders);
		for (Firm entityFirm : resultQuery) {
			result.add(mapper.map(entityFirm));
		}
		return result;
	}

	@Override
	public List<String> findUsers(String domain) {
		List<String> result = new ArrayList<String>();
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(FIR_DOMAIN, domain);
		filters.put(ENABLED, 1);
		List<Firm> resultQuery = firmDAO.listFilter(filters);
		if (resultQuery.size() == 1) {
			result = resultQuery.get(0).getFirGwtUsers();
		}
		return result;
	}

}