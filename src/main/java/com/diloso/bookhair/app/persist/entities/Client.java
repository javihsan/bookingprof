package com.diloso.bookhair.app.persist.entities;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;

/**
 * The persistent class for the Client entity
 * 
 */
@Entity
@Cache
public class Client extends Who { 
	
	public Client() {
    }

	
	protected Date cliCreationTime;

	public Date getCliCreationTime() {
		return cliCreationTime;
	}

	public void setCliCreationTime(Date cliCreationTime) {
		this.cliCreationTime = cliCreationTime;
	}
	
}