package com.diloso.bookhair.app.persist.mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.BilledDTO;
import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.ProductDTO;
import com.diloso.bookhair.app.negocio.manager.IClientManager;
import com.diloso.bookhair.app.negocio.manager.ILocalTaskManager;
import com.diloso.bookhair.app.negocio.manager.IProductManager;
import com.diloso.bookhair.app.persist.entities.Billed;
import com.google.appengine.api.datastore.Entity;


@Component
@Scope(value = "singleton")
public class BilledMapper {
	
	public BilledMapper() {

	}	
	
	@Autowired
	protected IClientManager clientManager;
	
	@Autowired
	protected ILocalTaskManager localTaskManager;
	
	@Autowired
	protected IProductManager productManager;
	
	public Billed map(BilledDTO billed){
		
		Billed entityBilled = new Billed();
		
		try {
			PropertyUtils.copyProperties(entityBilled, billed);
		} catch (Exception e) {
		} 
		
		if (billed.getBilClient()!=null){
			entityBilled.setBilClientId(billed.getBilClient().getId());
		}
	
		if (billed.getBilLocalTask()!=null){
			entityBilled.setBilLocalTaskId(billed.getBilLocalTask().getId());
		}
		
		if (billed.getBilProduct()!=null){
			entityBilled.setBilProductId(billed.getBilProduct().getId());
		}
	
		
		return entityBilled;
	}
	
	public BilledDTO map(Billed entityBilled) {

		BilledDTO billed = new BilledDTO();

		try {
			PropertyUtils.copyProperties(billed, entityBilled);
			
			// Propiedades de Client
			if (entityBilled.getBilClientId() != null) {
				ClientDTO client = clientManager.getById(entityBilled
						.getBilClientId());
				billed.setBilClient(client);
			}
			
			// Propiedades de LocalTask
			Long bilLocalTaskId = entityBilled.getBilLocalTaskId();
			if (bilLocalTaskId != null) {
				LocalTaskDTO localbilled = localTaskManager.getById(bilLocalTaskId);
				billed.setBilLocalTask(localbilled);
			}
			
			
			// Propiedades de Product
			if (entityBilled.getBilProductId() != null) {
				ProductDTO product = productManager.getById(entityBilled
						.getBilProductId());
				billed.setBilProduct(product);
			}
			
	
		} catch (Exception e) {
		}
		return billed;
	}
	
	public BilledDTO map(Entity entityBilled) {

		BilledDTO billed = new BilledDTO();

		try {
			billed.setId(entityBilled.getKey().getId());
			PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(BilledDTO.class);
			Object value;
			for (PropertyDescriptor descriptor : pd) {
				Method writeMethod = PropertyUtils.getWriteMethod(descriptor);
				if (writeMethod!=null){
					value = entityBilled.getProperty(descriptor.getName());
					if (value instanceof Long){
						if (writeMethod.getParameterTypes()[0].getName().equals("java.lang.Integer")){
							value = new Integer(value.toString());
						}
					} else if (value instanceof Double){
						if (writeMethod.getParameterTypes()[0].getName().equals("java.lang.Float")){
							value = new Float(value.toString());
						}
					} 
					if (value!=null) writeMethod.invoke(billed,value);
				}
			}
			
		} catch (Exception e) {
		}
		
		// Propiedades de Client
		Long bilClientId = (Long)entityBilled.getProperty("bilClientId");
		if (bilClientId != null) {
			ClientDTO client = clientManager.getById(bilClientId);
			billed.setBilClient(client);
		}
		
		// Propiedades de LocalTask
		Long bilLocalTaskId =  (Long)entityBilled.getProperty("bilLocalTaskId");
		if (bilLocalTaskId != null) {
			LocalTaskDTO localbilled = localTaskManager.getById(bilLocalTaskId);
			billed.setBilLocalTask(localbilled);
		}	
		
		// Propiedades de Product
		Long bilProductId = (Long)entityBilled.getProperty("bilProductId");
		if (bilProductId != null) {
			ProductDTO product = productManager.getById(bilProductId);
			billed.setBilProduct(product);
		}

		
		return billed;
	}

//	public void setClientDAO(IClientManager iClientManager) {
//		this.iClientManager = iClientManager;
//	}
//
//	public void setLocalTaskDAO(ILocalTaskManager iLocalTaskManager) {
//		this.iLocalTaskManager = iLocalTaskManager;
//	}
//
//	public void setProductDAO(IProductManager iProductManager) {
//		this.iProductManager = iProductManager;
//	}
		
	
}
