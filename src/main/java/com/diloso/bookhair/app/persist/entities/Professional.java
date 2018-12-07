package com.diloso.bookhair.app.persist.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * The persistent class for the Professional entity
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="getProfessional", query = "SELECT t FROM Professional t WHERE t.resFirId=:resFirId and t.enabled =1 order by id desc"),
	@NamedQuery(name="getProfessionalEmail", query = "SELECT t FROM Professional t WHERE t.resFirId=:resFirId and t.whoEmail =:whoEmail and t.enabled =1 order by id desc")
})
public class Professional extends Who implements Serializable {
	protected static final long serialVersionUID = 1L;

	public Professional() {
    }


}