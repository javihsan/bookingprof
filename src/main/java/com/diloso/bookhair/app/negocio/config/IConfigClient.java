package com.diloso.bookhair.app.negocio.config;

import java.util.Map;

import com.diloso.bookhair.app.negocio.config.impl.ConfigClientField;

public interface IConfigClient extends IConfig {
	
	Map<String,ConfigClientField> getExtraTable();
	
	Map<String,ConfigClientField> getExtraBook();
		
}
