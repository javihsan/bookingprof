package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the Firm entity
 * 
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "getFirm", query = "SELECT t FROM Firm t WHERE t.enabled =1 order by id desc"),
		@NamedQuery(name = "getFirmAdmin", query = "SELECT t FROM Firm t order by id desc"),
		@NamedQuery(name = "getFirmDomain", query = "SELECT t FROM Firm t WHERE t.firDomain=:firDomain and t.enabled =1 order by id desc"),
		@NamedQuery(name = "getFirmDomainAdmin", query = "SELECT t FROM Firm t WHERE t.firDomain=:firDomain order by id desc"),
		@NamedQuery(name = "getDomainServer", query = "SELECT t FROM Firm t WHERE t.firServer=:firServer and t.enabled =1 order by id desc")
})
public class Firm implements Serializable {
	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
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