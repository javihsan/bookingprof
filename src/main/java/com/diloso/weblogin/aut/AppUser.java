package com.diloso.weblogin.aut;

import java.io.Serializable;
import java.util.Set;

public class AppUser implements Serializable {

	protected String email;
	protected Set<AppRole> authorities;
	protected boolean enabled;
	protected Long firmId;	
	
	public AppUser(String email, Set<AppRole> authorities, boolean enabled, Long firmId) {
		super();
		this.email = email;
		this.authorities = authorities;
		this.enabled = enabled;
		this.firmId = firmId;
	}


	public String getEmail() {
		return email;
	}

	public Set<AppRole> getAuthorities() {
		return authorities;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Long getFirmId() {
		return firmId;
	}
	
}
