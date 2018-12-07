package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;

public class WhereDTO extends ResourceDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected String wheAddress;
	
	protected String wheCity;

	protected String wheState;
	
	protected String wheCP;
	
	protected String wheCountry;
	
	protected String wheTimeZone;
	
	protected String wheGoogleHoliday;
	
	protected String wheCurrency;
	
	public WhereDTO() {
    }

	public String getWheAddress() {
		return wheAddress;
	}

	public void setWheAddress(String wheAddress) {
		this.wheAddress = wheAddress;
	}
	
	public String getWheCity() {
		return wheCity;
	}

	public void setWheCity(String wheCity) {
		this.wheCity = wheCity;
	}

	public String getWheState() {
		return wheState;
	}

	public void setWheState(String wheState) {
		this.wheState = wheState;
	}

	public String getWheCP() {
		return wheCP;
	}

	public void setWheCP(String wheCP) {
		this.wheCP = wheCP;
	}

	public String getWheCountry() {
		return wheCountry;
	}

	public void setWheCountry(String wheCountry) {
		this.wheCountry = wheCountry;
	}

	public String getWheTimeZone() {
		return wheTimeZone;
	}

	public void setWheTimeZone(String wheTimeZone) {
		this.wheTimeZone = wheTimeZone;
	}

	public String getWheGoogleHoliday() {
		return wheGoogleHoliday;
	}

	public void setWheGoogleHoliday(String wheGoogleHoliday) {
		this.wheGoogleHoliday = wheGoogleHoliday;
	}
	
	public String getWheCurrency() {
		return wheCurrency;
	}

	public void setWheCurrency(String wheCurrency) {
		this.wheCurrency = wheCurrency;
	}
}