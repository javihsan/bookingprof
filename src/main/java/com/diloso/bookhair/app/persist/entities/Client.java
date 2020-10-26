package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the Client entity
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="getClient", query = "SELECT t FROM Client t WHERE t.resFirId=:resFirId and t.enabled= 1 order by t.id desc"),
	@NamedQuery(name="getClientEmail", query = "SELECT t FROM Client t WHERE t.resFirId=:resFirId and t.whoEmail = :whoEmail and t.enabled= 1 order by t.id desc")
})
public class Client extends Who implements Serializable {
	protected static final long serialVersionUID = 1L;

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