package com.diloso.bookhair.app.persist.mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.negocio.dto.ClientDTO;
import com.diloso.bookhair.app.negocio.dto.InvoiceDTO;
import com.diloso.bookhair.app.negocio.manager.IClientManager;
import com.diloso.bookhair.app.persist.entities.Invoice;
import com.google.appengine.api.datastore.Entity;


@Component
@Scope(value = "singleton")
public class InvoiceMapper {
	
	
	public InvoiceMapper() {

	}	
	
	@Autowired
	protected IClientManager clientManager;
	
	public Invoice map(InvoiceDTO invoice){
		
		Invoice entityInvoice = new Invoice();
		
		try {
			PropertyUtils.copyProperties(entityInvoice, invoice);
		} catch (Exception e) {
		} 
		
		if (invoice.getInvClient()!=null){
			entityInvoice.setInvClientId(invoice.getInvClient().getId());
		}
	
		return entityInvoice;
	}
	
	public InvoiceDTO map(Invoice entityInvoice) {

		InvoiceDTO invoice = new InvoiceDTO();

		try {
			PropertyUtils.copyProperties(invoice, entityInvoice);
			
			// Propiedades de Client
			if (entityInvoice.getInvClientId() != null) {
				ClientDTO client = clientManager.getById(entityInvoice
						.getInvClientId());
				invoice.setInvClient(client);
			}
			
		} catch (Exception e) {
		}
		return invoice;
	}
	
	public InvoiceDTO map(Entity entityInvoice) {

		InvoiceDTO invoice = new InvoiceDTO();

		try {
			invoice.setId(entityInvoice.getKey().getId());
			PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(InvoiceDTO.class);
			Object value;
			for (PropertyDescriptor descriptor : pd) {
				Method writeMethod = PropertyUtils.getWriteMethod(descriptor);
				if (writeMethod!=null){
					value = entityInvoice.getProperty(descriptor.getName());
					if (value instanceof Long){
						if (writeMethod.getParameterTypes()[0].getName().equals("java.lang.Integer")){
							value = new Integer(value.toString());
						}
					} else if (value instanceof Double){
						if (writeMethod.getParameterTypes()[0].getName().equals("java.lang.Float")){
							value = new Float(value.toString());
						}
					}
					if (value!=null) writeMethod.invoke(invoice,value);
				}
			}
			
		} catch (Exception e) {
		}
		
		// Propiedades de Client
		Long invClientId = (Long)entityInvoice.getProperty("invClientId");
		if (invClientId != null) {
			ClientDTO client = clientManager.getById(invClientId);
			invoice.setInvClient(client);
		}
		return invoice;
	}
	
//	public void setClientDAO(IClientManager iClientManager) {
//		this.clientManager = iClientManager;
//	}
	
}
