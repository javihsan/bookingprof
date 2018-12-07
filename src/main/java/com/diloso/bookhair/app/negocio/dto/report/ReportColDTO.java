package com.diloso.bookhair.app.negocio.dto.report;

import java.io.Serializable;

public class ReportColDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected String id;
	
	protected String type;
	
	protected String label;
	
	protected String pattern;
	
	protected String p;
	
	public ReportColDTO() {
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	
}