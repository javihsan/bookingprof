package com.diloso.weblogin.aut;

import org.springframework.security.core.GrantedAuthority;


public enum AppRole implements GrantedAuthority {
	ADMIN(0), MANAGER(1), OPERATOR(2), OPERATOR_READ(21), USER(3);

	protected int bit;

	AppRole(int bit) {
		this.bit = bit;
	}

	public String getAuthority() {
		return toString();
	}

	public int getBit() {
		return bit;
	}

	public void setBit(int bit) {
		this.bit = bit;
	}
	
}
