package com.diloso.bookhair.app.persist.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatClientDTO;
import com.diloso.bookhair.app.negocio.dto.RepeatDTO;
import com.diloso.bookhair.app.negocio.manager.IClientManager;
import com.diloso.bookhair.app.negocio.manager.IRepeatManager;
import com.diloso.bookhair.app.persist.entities.RepeatClient;


@Component
@Scope(value = "singleton")
public class RepeatClientMapper {
	
	public RepeatClientMapper() {

	}
	
	@Autowired
	protected IRepeatManager repeatManager;
	
	@Autowired
	protected IClientManager clientManager;

	public RepeatClient map(RepeatClientDTO repeatClient){
		
		RepeatClient entityRepeatClient = new RepeatClient();
		
		try {
			PropertyUtils.copyProperties(entityRepeatClient, repeatClient);
		} catch (Exception e) {
		} 
		
		if (repeatClient.getRecRepeat()!=null){
			entityRepeatClient.setRecRepeatId(repeatClient.getRecRepeat().getId());
		}
		if (repeatClient.getRecClient()!=null){
			entityRepeatClient.setRecClientId(repeatClient.getRecClient().getId());
		}
		
		return entityRepeatClient;
	}
	
	public RepeatClientDTO map(RepeatClient entityRepeatClient) {

		RepeatClientDTO repeatClient = new RepeatClientDTO();

		try {
			PropertyUtils.copyProperties(repeatClient, entityRepeatClient);
			
			// Propiedades de Repeat
			if (entityRepeatClient.getRecRepeatId() != null) {
				RepeatDTO repeat = repeatManager
						.getById(entityRepeatClient.getRecRepeatId());
					repeatClient.setRecRepeat(repeat);
			}
			
			// Propiedades de Client
			if (entityRepeatClient.getRecClientId() != null) {
				ClientDTO client = clientManager
						.getById(entityRepeatClient.getRecClientId());
				repeatClient.setRecClient(client);
			}
			
		} catch (Exception e) {
		}
		return repeatClient;
	}
//
//	public void setRepeatDAO(IRepeatManager iRepeatManager) {
//		this.repeatManager = iRepeatManager;
//	}
//
//	public void setClientDAO(IClientManager iClientManager) {
//		this.clientManager = iClientManager;
//	}
		
		
}
