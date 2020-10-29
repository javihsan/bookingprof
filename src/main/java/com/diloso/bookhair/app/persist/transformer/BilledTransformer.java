package com.diloso.bookhair.app.persist.transformer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dao.ClientDAO;
import com.diloso.bookhair.app.negocio.dao.LocalTaskDAO;
import com.diloso.bookhair.app.negocio.dao.ProductDAO;
import com.diloso.bookhair.app.negocio.dto.BilledDTO;
import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.dto.LocalTaskDTO;
import com.diloso.bookhair.app.negocio.dto.ProductDTO;
import com.diloso.bookhair.app.persist.entities.Billed;
import com.google.appengine.api.datastore.Entity;


@Component
@Scope(value = "singleton")
public class BilledTransformer {
	
	public BilledTransformer() {

	}	
	
	//@Autowired
	protected ClientDAO clientDAO;
	
	//@Autowired
	protected LocalTaskDAO localTaskDAO;
	
	//@Autowired
	protected ProductDAO productDAO;
	
	public Billed transformDTOToEntity(BilledDTO billed){
		
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
	
	public BilledDTO transformEntityToDTO(Billed entityBilled) {

		BilledDTO billed = new BilledDTO();

		try {
			PropertyUtils.copyProperties(billed, entityBilled);
			
			// Propiedades de Client
			if (entityBilled.getBilClientId() != null) {
				ClientDTO client = clientDAO.getById(entityBilled
						.getBilClientId());
				billed.setBilClient(client);
			}
			
			// Propiedades de LocalTask
			Long bilLocalTaskId = entityBilled.getBilLocalTaskId();
			if (bilLocalTaskId != null) {
				LocalTaskDTO localbilled = localTaskDAO.getById(bilLocalTaskId);
				billed.setBilLocalTask(localbilled);
			}
			
			
			// Propiedades de Product
			if (entityBilled.getBilProductId() != null) {
				ProductDTO product = productDAO.getById(entityBilled
						.getBilProductId());
				billed.setBilProduct(product);
			}
			
	
		} catch (Exception e) {
		}
		return billed;
	}
	
	public BilledDTO transformEntityToDTO(Entity entityBilled) {

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
			ClientDTO client = clientDAO.getById(bilClientId);
			billed.setBilClient(client);
		}
		
		// Propiedades de LocalTask
		Long bilLocalTaskId =  (Long)entityBilled.getProperty("bilLocalTaskId");
		if (bilLocalTaskId != null) {
			LocalTaskDTO localbilled = localTaskDAO.getById(bilLocalTaskId);
			billed.setBilLocalTask(localbilled);
		}	
		
		// Propiedades de Product
		Long bilProductId = (Long)entityBilled.getProperty("bilProductId");
		if (bilProductId != null) {
			ProductDTO product = productDAO.getById(bilProductId);
			billed.setBilProduct(product);
		}

		
		return billed;
	}

	public void setClientDAO(ClientDAO clientDAO) {
		this.clientDAO = clientDAO;
	}

	public void setLocalTaskDAO(LocalTaskDAO localTaskDAO) {
		this.localTaskDAO = localTaskDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}
	
	
	
}
