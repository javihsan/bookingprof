package com.diloso.bookhair.app.negocio.config.impl;

import com.diloso.bookhair.app.negocio.config.IConfigAut;
import com.diloso.bookhair.app.negocio.config.IConfigClient;
import com.diloso.bookhair.app.negocio.config.IConfigDenon;
import com.diloso.bookhair.app.negocio.config.IConfigEvent;
import com.diloso.bookhair.app.negocio.config.IConfigFirm;
import com.diloso.bookhair.app.negocio.config.IConfigLocal;
import com.diloso.bookhair.app.negocio.config.IConfigPay;

public class ConfigFirm extends Config implements IConfigFirm {

	public static final String PRE_IDENT_FIRM = "CF";
	public static final String IDENT_DEFAULT = "01001";
	
	String numConfig;
	
	IConfigLocal configLocal;
	
	IConfigAut configAut;
	
	IConfigDenon configDenon;
	
	IConfigClient configClient;
	
	IConfigPay configPay;
	
	IConfigEvent configEvent;
	
	public ConfigFirm(){
	}
	
	public ConfigFirm(String numConfig){
		this.numConfig = numConfig;
	}
	
	public String getNumConfig() {
		return numConfig;
	}

	public void setNumConfig(String numConfig) {
		this.numConfig = numConfig;
	}
	
	public IConfigLocal getConfigLocal() {
		return configLocal;
	}

	public void setConfigLocal(IConfigLocal configLocal) {
		this.configLocal = configLocal;
	}

	public IConfigAut getConfigAut() {
		return configAut;
	}

	public void setConfigAut(IConfigAut configAut) {
		this.configAut = configAut;
	}

	public IConfigDenon getConfigDenon() {
		return configDenon;
	}

	public void setConfigDenon(IConfigDenon configDenon) {
		this.configDenon = configDenon;
	}

	public IConfigClient getConfigClient() {
		return configClient;
	}

	public void setConfigClient(IConfigClient configClient) {
		this.configClient = configClient;
	}

	public IConfigPay getConfigPay() {
		return configPay;
	}

	public void setConfigPay(IConfigPay configPay) {
		this.configPay = configPay;
	}

	public IConfigEvent getConfigEvent() {
		return configEvent;
	}

	public void setConfigEvent(IConfigEvent configEvent) {
		this.configEvent = configEvent;
	}
	
}
