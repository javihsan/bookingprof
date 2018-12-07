package com.diloso.bookhair.app.negocio.config.impl;

import com.diloso.bookhair.app.negocio.config.IConfigClientField;

public class ConfigClientField extends Config implements IConfigClientField {

	String name;
	
	String type;
	
	String textLang;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTextLang() {
		return textLang;
	}

	public void setTextLang(String textLang) {
		this.textLang = textLang;
	}
	
	
}
