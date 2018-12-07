package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.Date;

public class ClientDTO extends WhoDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	public ClientDTO() {
    }

	protected Date cliCreationTime;

	public Date getCliCreationTime() {
		return cliCreationTime;
	}

	public void setCliCreationTime(Date cliCreationTime) {
		this.cliCreationTime = cliCreationTime;
	}
	
	
	
}