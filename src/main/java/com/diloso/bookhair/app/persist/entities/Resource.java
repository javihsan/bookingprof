package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
/**
 * The persistent class for the Resource entity
 * 
 */
@Entity
@MappedSuperclass
public class Resource implements Serializable {
	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	protected Integer enabled;

	protected Long resFirId;
	
	public Resource() {
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

	public Long getResFirId() {
		return resFirId;
	}

	public void setResFirId(Long resFirId) {
		this.resFirId = resFirId;
	}
	
   
}