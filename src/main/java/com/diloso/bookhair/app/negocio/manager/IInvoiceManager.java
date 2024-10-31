package com.diloso.bookhair.app.negocio.manager;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.InvoiceDTO;

public interface IInvoiceManager {

	InvoiceDTO create(InvoiceDTO taskDTO) throws Exception;

	InvoiceDTO remove(long id) throws Exception;

	InvoiceDTO update(InvoiceDTO taskDTO) throws Exception;

	InvoiceDTO getById(long id);

	List<InvoiceDTO> getInvoiceByWeek(long invLocalId, String selectedDate);
	
	List<InvoiceDTO> getInvoiceByDay(long invLocalId, String selectedDate);
	
	List<InvoiceDTO> getInvoiceByClientAgo(long invLocalId, long clientId);
}