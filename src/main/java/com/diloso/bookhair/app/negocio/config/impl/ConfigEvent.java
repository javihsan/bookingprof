package com.diloso.bookhair.app.negocio.config.impl;

import com.diloso.bookhair.app.negocio.config.IConfigEvent;

public class ConfigEvent extends Config implements IConfigEvent {

	Integer configEventPerformer;

	public Integer getConfigEventPerformer() {
		return configEventPerformer;
	}

	public void setConfigEventPerformer(Integer configEventPerformer) {
		this.configEventPerformer = configEventPerformer;
	}
		
}
