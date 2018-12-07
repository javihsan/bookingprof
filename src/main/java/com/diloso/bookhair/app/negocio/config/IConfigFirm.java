package com.diloso.bookhair.app.negocio.config;

public interface IConfigFirm extends IConfig {
	
	IConfigLocal getConfigLocal();
	
	IConfigAut getConfigAut();
	
	IConfigDenon getConfigDenon();

	IConfigClient getConfigClient();

	IConfigPay getConfigPay();

	IConfigEvent getConfigEvent();
	
}
