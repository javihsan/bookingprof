package com.diloso.bookhair.app.negocio.config.impl;

import java.util.Map;

import com.diloso.bookhair.app.negocio.config.IConfigDenon;

public class ConfigDenon extends Config implements IConfigDenon {

	Map<String,String> listDenon;

	public Map<String,String> getListDenon() {
		return listDenon;
	}

	public void setListDenon(Map<String,String> listDenon) {
		this.listDenon = listDenon;
	}
	
	
}

