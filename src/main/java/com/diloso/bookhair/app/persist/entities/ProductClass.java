package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the ProductClass entity
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="getProductClass", query = "SELECT t FROM ProductClass t WHERE t.enabled =1 order by id asc")
})
public class ProductClass implements Serializable {

	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	protected Integer enabled;
	
	protected String pclNameMulti;
	
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
	
	public String getPclNameMulti() {
		return pclNameMulti;
	}

	public void setPclNameMulti(String pclNameMulti) {
		this.pclNameMulti = pclNameMulti;
	}
	
	
}