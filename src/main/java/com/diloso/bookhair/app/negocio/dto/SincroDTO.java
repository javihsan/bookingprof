package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;

public class SincroDTO extends ResourceDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	// 0 Google Calendar - 1 MailChimp
	protected Integer sinType;
	// Key de la api de la app
	protected String sinApiKey;
	// Secret de la api de la app
	protected String sinApiSecret;
	// Key del consumidor
	protected String sinConKey;
	// Secret del consumidor
	protected String sinConSecret;
	// Cuenta de email del consumidor
	protected String sinConEmail;
	// Telefono del consumidor
	protected String sinConTelf;
	
		
	public SincroDTO() {
    }


	public Integer getSinType() {
		return sinType;
	}


	public void setSinType(Integer sinType) {
		this.sinType = sinType;
	}


	public String getSinApiKey() {
		return sinApiKey;
	}


	public void setSinApiKey(String sinApiKey) {
		this.sinApiKey = sinApiKey;
	}


	public String getSinApiSecret() {
		return sinApiSecret;
	}


	public void setSinApiSecret(String sinApiSecret) {
		this.sinApiSecret = sinApiSecret;
	}


	public String getSinConKey() {
		return sinConKey;
	}


	public void setSinConKey(String sinConKey) {
		this.sinConKey = sinConKey;
	}


	public String getSinConSecret() {
		return sinConSecret;
	}


	public void setSinConSecret(String sinConSecret) {
		this.sinConSecret = sinConSecret;
	}


	public String getSinConEmail() {
		return sinConEmail;
	}


	public void setSinConEmail(String sinConEmail) {
		this.sinConEmail = sinConEmail;
	}


	public String getSinConTelf() {
		return sinConTelf;
	}


	public void setSinConTelf(String sinConTelf) {
		this.sinConTelf = sinConTelf;
	}
	
		
}