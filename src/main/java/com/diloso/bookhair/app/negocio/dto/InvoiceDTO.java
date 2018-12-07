package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.Date;



public class InvoiceDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected Long id;
	
	protected Integer enabled;

	// Local
	protected Long invLocalId;
	
	// Descripcion de la factura
	protected String invDesc;
	// Cliente al que se le pasa la factura
	protected ClientDTO invClient;
	// Fecha de emision de la factura
	protected Date invIssueTime;
	// Fecha a facturar
	protected Date invTime;
	// Tarifa aplicada
	protected Float invRate;
	
	// Servicios / productos de la factura, solo para mostrar, no se guarda
	protected String invBilleds;
	
    public InvoiceDTO() {
    }

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
	
	public Long getInvLocalId() {
		return invLocalId;
	}

	public void setInvLocalId(Long invLocalId) {
		this.invLocalId = invLocalId;
	}

	public String getInvDesc() {
		return invDesc;
	}

	public void setInvDesc(String invDesc) {
		this.invDesc = invDesc;
	}

	public ClientDTO getInvClient() {
		return invClient;
	}

	public void setInvClient(ClientDTO invClient) {
		this.invClient = invClient;
	}

	public Date getInvIssueTime() {
		return invIssueTime;
	}

	public void setInvIssueTime(Date invIssueTime) {
		this.invIssueTime = invIssueTime;
	}

	public Date getInvTime() {
		return invTime;
	}

	public void setInvTime(Date invTime) {
		this.invTime = invTime;
	}

	public Float getInvRate() {
		return invRate;
	}

	public void setInvRate(Float invRate) {
		this.invRate = invRate;
	}

	public String getInvBilleds() {
		return invBilleds;
	}

	public void setInvBilleds(String invBilleds) {
		this.invBilleds = invBilleds;
	}

    						
}