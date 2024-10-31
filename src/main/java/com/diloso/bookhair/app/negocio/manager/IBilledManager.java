package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.BilledDTO;
import com.diloso.bookhair.app.negocio.dto.CalendarDTO;

public interface IBilledManager {

	BilledDTO create(BilledDTO taskDTO) throws Exception;

	BilledDTO remove(long id) throws Exception;

	BilledDTO update(BilledDTO taskDTO) throws Exception;

	BilledDTO getById(long id);
	
	List<BilledDTO> getBilledByInvoice(long bilInvoiceId, String lang);
	
	Float getBilledSales(CalendarDTO calendaDTOr, String startDate, String endDate);
	
	Float getBilledSalesTask(CalendarDTO calendarDTO, String startDate, String endDate, Long localTaskId);
	
	Float getBilledSalesProduct(CalendarDTO calendarDTO, String startDate, String endDate, Long productId);
	
}