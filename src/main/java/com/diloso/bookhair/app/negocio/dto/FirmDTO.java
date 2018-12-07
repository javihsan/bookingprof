package com.diloso.bookhair.app.negocio.dto;

import java.io.Serializable;
import java.util.List;

import com.diloso.bookhair.app.negocio.config.impl.ConfigFirm;


public class FirmDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected Long id;
	
	protected Integer enabled;
	
	protected String firName;
	
	// Iidentificativo en bookingProf
	protected String firDomain;
	
	// Dominio propio
	protected String firServer;
	
	protected List<String> firGwtUsers;
	
	protected ProfessionalDTO firRespon;
	
	protected WhereDTO firWhere;
	
	// Firma que usa el módulo de facturacion
	protected Integer firBilledModule;
	
	// Identificativo de config 01-Pelu 02-Moda 03-Club 04-Gym -3 digitos para variedades
	protected ConfigFirm firConfig;
	
	// Lista de clases de tareas del negocio
	protected List<Long> firClassTasks;
	
	public FirmDTO() {
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

	
	public ProfessionalDTO getFirRespon() {
		return firRespon;
	}

	public void setFirRespon(ProfessionalDTO firRespon) {
		this.firRespon = firRespon;
	}

	public WhereDTO getFirWhere() {
		return firWhere;
	}

	public void setFirWhere(WhereDTO firWhere) {
		this.firWhere = firWhere;
	}

	public Integer getFirBilledModule() {
		return firBilledModule;
	}

	public void setFirBilledModule(Integer firBilledModule) {
		this.firBilledModule = firBilledModule;
	}

	public ConfigFirm getFirConfig() {
		return firConfig;
	}

	public void setFirConfig(ConfigFirm firConfig) {
		this.firConfig = firConfig;
	}


	public List<Long> getFirClassTasks() {
		return firClassTasks;
	}

	public void setFirClassTasks(List<Long> firClassTasks) {
		this.firClassTasks = firClassTasks;
	}

	
}