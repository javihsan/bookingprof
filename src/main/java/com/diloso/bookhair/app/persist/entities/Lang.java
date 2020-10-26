package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the Lang entity
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="getLang", query = "SELECT t FROM Lang t WHERE t.enabled =1 order by t.lanName asc"),
	@NamedQuery(name="getLangName", query = "SELECT t FROM Lang t WHERE t.lanName=:lanName and t.enabled =1"),
	@NamedQuery(name="getLangCode", query = "SELECT t FROM Lang t WHERE t.lanCode=:lanCode and t.enabled =1")
})
public class Lang implements Serializable {

	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	protected Integer enabled;
	
	protected String lanName;
	
	protected String lanCode;

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
	
	public String getLanName() {
		return lanName;
	}

	public void setLanName(String lanName) {
		this.lanName = lanName;
	}
	
	public String getLanCode() {
		return lanCode;
	}

	public void setLanCode(String lanCode) {
		this.lanCode = lanCode;
	}
		
		
}
