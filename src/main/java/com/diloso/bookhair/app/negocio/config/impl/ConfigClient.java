package com.diloso.bookhair.app.negocio.config.impl;

import java.util.Map;

import com.diloso.bookhair.app.negocio.config.IConfigClient;

public class ConfigClient extends Config implements IConfigClient {

	Map<String,ConfigClientField> extraTable;
	
	Map<String,ConfigClientField> extraBook;

	public Map<String,ConfigClientField> getExtraTable() {
		return extraTable;
	}

	public void setExtraTable(Map<String,ConfigClientField> extraTable) {
		this.extraTable = extraTable;
	}

	public Map<String,ConfigClientField> getExtraBook() {
		return extraBook;
	}

	public void setExtraBook(Map<String,ConfigClientField> extraBook) {
		this.extraBook = extraBook;
	}
	
	
		
}
