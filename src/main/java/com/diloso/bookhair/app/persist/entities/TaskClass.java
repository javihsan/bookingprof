package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the TaskClass entity
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="getTaskClass", query = "SELECT t FROM TaskClass t WHERE t.enabled =1 order by id asc"),
	@NamedQuery(name="getTaskClassMultiKey", query = "SELECT t FROM TaskClass t WHERE t.tclNameMulti=tclNameMulti and t.enabled =1 order by id asc")
})
public class TaskClass implements Serializable {

	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	protected Integer enabled;
	
	protected String tclNameMulti;
	
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
	
	public String getTclNameMulti() {
		return tclNameMulti;
	}

	public void setTclNameMulti(String tclNameMulti) {
		this.tclNameMulti = tclNameMulti;
	}
	
	
}
