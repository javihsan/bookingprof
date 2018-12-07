package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.Date;



public class BilledDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected Long id;
	
	protected Integer enabled;

	// Puesto que ha realizado-vendido el producto-servicio
	protected Long bilCalendarId;
	// Cliente que ha consumido el producto-servicio
	protected ClientDTO bilClient;
	// Servicio que se ha comprado (si no se ha comprado un producto)
	protected LocalTaskDTO bilLocalTask;
	// Producto que se ha comprado (si no se ha comprado un servicio)
	protected ProductDTO bilProduct;
	// Fecha de ejecución de la venta
	protected Date bilTime;
	// Tarifa aplicada
	protected Float bilRate;
	// Factura a la que pertenece la venta
	protected Long bilInvoiceId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getEnabled() {
		return enabled;
	}
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	
	public Long getBilCalendarId() {
		return bilCalendarId;
	}
	public void setBilCalendarId(Long bilCalendarId) {
		this.bilCalendarId = bilCalendarId;
	}
	
	public ClientDTO getBilClient() {
		return bilClient;
	}	
	public void setBilClient(ClientDTO bilClient) {
		this.bilClient = bilClient;
	}
	
	public LocalTaskDTO getBilLocalTask() {
		return bilLocalTask;
	}	
	public void setBilLocalTask(LocalTaskDTO bilLocalTask) {
		this.bilLocalTask = bilLocalTask;
	}
	
	public ProductDTO getBilProduct() {
		return bilProduct;
	}	
	public void setBilProduct(ProductDTO bilProduct) {
		this.bilProduct = bilProduct;
	}
	
	public Date getBilTime() {
		return bilTime;
	}
	public void setBilTime(Date bilTime) {
		this.bilTime = bilTime;
	}
	
	public Float getBilRate() {
		return bilRate;
	}
	public void setBilRate(Float bilRate) {
		this.bilRate = bilRate;
	}
	
	public Long getBilInvoiceId() {
		return bilInvoiceId;
	}

	public void setBilInvoiceId(Long bilInvoiceId) {
		this.bilInvoiceId = bilInvoiceId;
	}								
}