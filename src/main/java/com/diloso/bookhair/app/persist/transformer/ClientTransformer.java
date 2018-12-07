package com.diloso.bookhair.app.persist.transformer;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.WhereDAO;
import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.dto.WhereDTO;
import com.diloso.bookhair.app.persist.entities.Client;


@Component
@Scope(value = "singleton")
public class ClientTransformer {
	
	@Autowired
	protected WhereDAO whereDAO;
	
	public ClientTransformer() {

	}
	
	public Client transformDTOToEntity(ClientDTO client){
		
		Client entityClient = new Client();
		
		try {
			PropertyUtils.copyProperties(entityClient, client);
		} catch (Exception e) {
		}
		
		if (client.getWhoWhere()!=null){
			entityClient.setWhoWhereId(client.getWhoWhere().getId());
		}
		
		return entityClient;
	}
	
	public ClientDTO transformEntityToDTO(Client entityClient){
		
		ClientDTO client = new ClientDTO();
		
		try {
			PropertyUtils.copyProperties(client, entityClient);
		} catch (Exception e) {
		} 
		
		// Propiedades de Where
		if (entityClient.getWhoWhereId() != null) {
			WhereDTO where = whereDAO
					.getById(entityClient.getWhoWhereId());
			client.setWhoWhere(where);
		}
		
		return client;
	}
	

	
}
