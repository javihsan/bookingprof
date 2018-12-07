package com.diloso.bookhair.app.negocio.dto.report;

import java.io.Serializable;
import java.util.List;

public class ReportRowDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected List<ReportRowDetailDTO> c;
	
	
	public ReportRowDTO() {
    }


	public List<ReportRowDetailDTO> getC() {
		return c;
	}


	public void setC(List<ReportRowDetailDTO> c) {
		this.c = c;
	}

	
}