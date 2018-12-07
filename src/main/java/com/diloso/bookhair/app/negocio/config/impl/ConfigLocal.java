package com.diloso.bookhair.app.negocio.config.impl;

import com.diloso.bookhair.app.negocio.config.IConfigLocal;

public class ConfigLocal extends Config implements IConfigLocal {

	Integer configLocNumPer;
	
	Integer configLocMulSer;
	
	Integer configLocSelCal;
	
	Integer configLocSelCalAfter;
	
	Integer configLocRepeat;

	public Integer getConfigLocSelCalAfter() {
		return configLocSelCalAfter;
	}

	public void setConfigLocSelCalAfter(Integer configLocSelCalAfter) {
		this.configLocSelCalAfter = configLocSelCalAfter;
	}

	public Integer getConfigLocNumPer() {
		return configLocNumPer;
	}

	public void setConfigLocNumPer(Integer configLocNumPer) {
		this.configLocNumPer = configLocNumPer;
	}

	public Integer getConfigLocMulSer() {
		return configLocMulSer;
	}

	public void setConfigLocMulSer(Integer configLocMulSer) {
		this.configLocMulSer = configLocMulSer;
	}

	public Integer getConfigLocSelCal() {
		return configLocSelCal;
	}

	public void setConfigLocSelCal(Integer configLocSelCal) {
		this.configLocSelCal = configLocSelCal;
	}
	
	public Integer getConfigLocRepeat() {
		return configLocRepeat;
	}

	public void setConfigLocRepeat(Integer configLocRepeat) {
		this.configLocRepeat = configLocRepeat;
	}

	/* 
	 Si no permites más de 1 servicio: 
	  - no permites configurar "más de una persona" (luego solo hay 1)
	  - no permites configurar "más de 1 servicio x persona" (luego solo hay 1)
	 */
	public boolean getConfigLocSP() {
		return getConfigLocNumPer()==0 && getConfigLocMulSer() == 0;
	}
	
		
}
