package com.diloso.bookhair.app.negocio.dto.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReportDTO implements Serializable {
	protected static final long serialVersionUID = 1L;

	protected List<ReportColDTO> cols;
	
	protected List<ReportRowDTO> rows;
	
	
	public ReportDTO() {
		cols = new ArrayList<ReportColDTO>();
		rows = new ArrayList<ReportRowDTO>();
    }


	public List<ReportColDTO> getCols() {
		return cols;
	}


	public void setCols(List<ReportColDTO> cols) {
		this.cols = cols;
	}


	public List<ReportRowDTO> getRows() {
		return rows;
	}


	public void setRows(List<ReportRowDTO> rows) {
		this.rows = rows;
	}

	public void addColumn(String id, String type, String label){
		ReportColDTO reportColDTO = new ReportColDTO();
		reportColDTO.setId(id);
		reportColDTO.setType(type);
		reportColDTO.setLabel(label);
		cols.add(reportColDTO);
	}
	
	
	public void addRow(List<Object> listColumnValues){
		ReportRowDTO reportRowDTO = new ReportRowDTO();
		List<ReportRowDetailDTO> listReportRowDetailDTO = new ArrayList<ReportRowDetailDTO>();
		for (Object value : listColumnValues) {
			ReportRowDetailDTO reportRowDetailDTO = new ReportRowDetailDTO();
			reportRowDetailDTO.setV(value);
			listReportRowDetailDTO.add(reportRowDetailDTO);
		}
		reportRowDTO.setC(listReportRowDetailDTO);
		rows.add(reportRowDTO);
	}
}