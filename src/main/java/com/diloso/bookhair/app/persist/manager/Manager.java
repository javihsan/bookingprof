package com.diloso.bookhair.app.persist.manager;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

public class Manager {
	
	@Autowired
	protected IEMF beanEMF;
	
	public Manager() {
	
	}
	 
	protected EntityManager getEntityManager() {
		return beanEMF.get().createEntityManager();
	}
	
	public void setBeanEMF(IEMF beanEMF) {
		this.beanEMF = beanEMF;
	}
	
	
 }