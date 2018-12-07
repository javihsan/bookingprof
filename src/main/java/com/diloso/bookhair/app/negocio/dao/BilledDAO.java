package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.BilledDTO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;

public interface BilledDAO {

	BilledDTO create(BilledDTO task) throws Exception;

	BilledDTO remove(long id) throws Exception;

	BilledDTO update(BilledDTO task) throws Exception;

	BilledDTO getById(long id);
	
	List<BilledDTO> getBilledByInvoice(long bilInvoiceId, String lang);
	
	Float getBilledSales(CalendarDTO calendar, String startDate, String endDate);
	
	Float getBilledSalesTask(CalendarDTO calendar, String startDate, String endDate, Long localTaskId);
	
	Float getBilledSalesProduct(CalendarDTO calendar, String startDate, String endDate, Long productId);
	
}