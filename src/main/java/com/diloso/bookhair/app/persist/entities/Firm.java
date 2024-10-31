package com.diloso.bookhair.app.persist.entities;

import java.util.List;

import com.diloso.bookhair.app.datastore.data.StorableWithModificationTimestamp;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The persistent class for the Firm entity
 * 
 */
@Entity
@Cache
public class Firm extends StorableWithModificationTimestamp<Long> { 
	
	@Id
	protected Long id;
	
	@Index
	protected Integer enabled;
	
	protected String firName;
	
	protected String firDomain;
	
	protected String firServer;
	
	protected List<String> firGwtUsers;
	
	protected Long firResponId;
	
	protected Long firWhereId;
	
	protected Integer firBilledModule;
	
	protected String firConfigNum;
	
	protected List<Long> firClassTasks;
		
	public Firm() {
    }


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Integer getEnabled() {
		return enabled;
	}


	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}


	public String getFirName() {
		return firName;
	}


	public void setFirName(String firName) {
		this.firName = firName;
	}


	public String getFirDomain() {
		return firDomain;
	}


	public void setFirDomain(String firDomain) {
		this.firDomain = firDomain;
	}

	public String getFirServer() {
		return firServer;
	}

	public void setFirServer(String firServer) {
		this.firServer = firServer;
	}

	
	public List<String> getFirGwtUsers() {
		return firGwtUsers;
	}

	public void setFirGwtUsers(List<String> firGwtUsers) {
		this.firGwtUsers = firGwtUsers;
	}

	public Long getFirResponId() {
		return firResponId;
	}


	public void setFirResponId(Long firResponId) {
		this.firResponId = firResponId;
	}


	public Long getFirWhereId() {
		return firWhereId;
	}


	public void setFirWhereId(Long firWhereId) {
		this.firWhereId = firWhereId;
	}

	public Integer getFirBilledModule() {
		return firBilledModule;
	}

	public void setFirBilledModule(Integer firBilledModule) {
		this.firBilledModule = firBilledModule;
	}

	public String getFirConfigNum() {
		return firConfigNum;
	}

	public void setFirConfigNum(String firConfigNum) {
		this.firConfigNum = firConfigNum;
	}
	
	public List<Long> getFirClassTasks() {
		return firClassTasks;
	}

	public void setFirClassTasks(List<Long> firClassTasks) {
		this.firClassTasks = firClassTasks;
	}
	
}