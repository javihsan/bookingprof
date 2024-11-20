package com.diloso.bookhair.app.negocio.manager;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.CalendarDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatClientDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.RepeatClientDAO;
import com.diloso.bookhair.app.persist.entities.RepeatClient;
import com.diloso.bookhair.app.persist.mapper.RepeatClientMapper;

@Component
@Scope(value = "singleton")
public class RepeatClientManager implements IRepeatClientManager {

	@Autowired
	private RepeatClientDAO repeatClientDAO;
	
	@Autowired
	protected RepeatClientMapper mapper;
	
	public RepeatClientManager() {

	}

	@Override
	public RepeatClientDTO create(RepeatClientDTO repeatClientDTO) throws Exception {
		RepeatClient repeatClient = mapper.map(repeatClientDTO);
		repeatClient = repeatClientDAO.create(repeatClient);
		return mapper.map(repeatClient);
	}

	@Override
	public RepeatClientDTO remove(long id) throws Exception {
		RepeatClient repeatClient = repeatClientDAO.get(id);
		repeatClientDAO.delete(id);
		if (repeatClient == null) {
			return null;
		}
		return mapper.map(repeatClient);
	}

	@Override
	public RepeatClientDTO update(RepeatClientDTO repeatClientDTO) throws Exception {
		RepeatClient repeatClient = mapper.map(repeatClientDTO);
		RepeatClient oldRepeatClient = repeatClientDAO.get(repeatClientDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(repeatClient, oldRepeatClient);
		} catch (Exception e) {
		}
		repeatClient = repeatClientDAO.update(repeatClient);
		if (repeatClient == null) {
			return null;
		}
		return mapper.map(repeatClient);
	}

	@Override
	public RepeatClientDTO getById(long id) {
		RepeatClient repeatClient = repeatClientDAO.get(id);
		if (repeatClient == null) {
			return null;
		}
		return mapper.map(repeatClient);		
	}

	@Override
	public List<RepeatClientDTO> getRepeatClientByClientAgo(CalendarDTO calendarDTO, Long clientId, Date selectedDate,
			int numDays) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/*
	public List<RepeatClientDTO> getRepeatClient(long calLocalId) {
		EntityManager em = getEntityManager();
		List<RepeatClientDTO> result = new ArrayList<RepeatClientDTO>();
		List<RepeatClient> resultQuery = null;
		RepeatClientDTO repeatClient = null;
		try {
			Query query = em.createNamedQuery("getRepeatClient");
			query.setParameter("calLocalId", calLocalId);
			resultQuery = (List<RepeatClient>) query.getResultList();
			for (RepeatClient entityRepeatClient : resultQuery) {
				repeatClient = repeatClientTransformer
						.map(entityRepeatClient);
				result.add(repeatClient);
			}
		} finally {
			em.close();
		}
		return result;
	}
*/
}