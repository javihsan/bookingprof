package com.diloso.bookhair.app.negocio.dto.report;

import java.io.Serializable;

public class ReportRowDetailDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected Object v;
	
	protected String f;
	
	protected String p;
	
	public ReportRowDetailDTO() {
    }

	public Object getV() {
		return v;
	}

	public void setV(Object v) {
		this.v = v;
	}

	public String getF() {
		return f;
	}

	public void setF(String f) {
		this.f = f;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}
	
	

}