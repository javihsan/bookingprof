package com.diloso.bookhair.app.negocio.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.diloso.bookhair.app.controllers.CalendarController;
import com.diloso.bookhair.app.negocio.dto.InvoiceDTO;
import com.diloso.bookhair.app.negocio.utils.NullAwareBeanUtilsBean;
import com.diloso.bookhair.app.persist.dao.InvoiceDAO;
import com.diloso.bookhair.app.persist.entities.Invoice;
import com.diloso.bookhair.app.persist.mapper.InvoiceMapper;

@Component
@Scope(value = "singleton")
public class InvoiceManager implements IInvoiceManager {
	
	public static final String INV_LOCAL_ID = "invLocalId";
	public static final String INV_CLIENT_ID = "invClientId";
	public static final String ENABLED = "enabled";
	public static final String ORDER_INV_DATE_ASC = "invTime";
	public static final String ORDER_INV_DATE_MAYQ = "invTime >=";
	public static final String ORDER_INV_DATE_MENQ = "invTime <="; 

	@Autowired
	private InvoiceDAO invoiceDAO;
	
	@Autowired
	protected IMultiTextManager multiTextManager;
	
	@Autowired
	protected ITaskManager taskManager;
	
	@Autowired
	protected InvoiceMapper mapper;
	
	public InvoiceManager() {

	}

	@Override
	public InvoiceDTO create(InvoiceDTO invoiceDTO) throws Exception {
		Invoice invoice = mapper.map(invoiceDTO);
		invoice = invoiceDAO.create(invoice);
		return mapper.map(invoice);
	}

	@Override
	public InvoiceDTO remove(long id) throws Exception {
		Invoice invoice = invoiceDAO.get(id);
		invoiceDAO.delete(id);
		if (invoice == null) {
			return null;
		}
		return mapper.map(invoice);
	}

	@Override
	public InvoiceDTO update(InvoiceDTO invoiceDTO) throws Exception {
		Invoice invoice = mapper.map(invoiceDTO);
		Invoice oldInvoice = invoiceDAO.get(invoiceDTO.getId());
		try {
			new NullAwareBeanUtilsBean().copyProperties(invoice, oldInvoice);
		} catch (Exception e) {
		}
		invoice = invoiceDAO.update(invoice);
		if (invoice == null) {
			return null;
		}
		return mapper.map(invoice);
	}

	@Override
	public InvoiceDTO getById(long id) {
		Invoice invoice = invoiceDAO.get(id);
		if (invoice == null) {
			return null;
		}
		return mapper.map(invoice);		
	}

	@Override
	public List<InvoiceDTO> getInvoiceByWeek(long invLocalId, String selectedDate) {
		
		String[] dates = selectedDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND, 0);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		Date startTime = calendarGreg.getTime();

		calendarGreg.add(Calendar.DAY_OF_MONTH, 7);
		Date endTime = calendarGreg.getTime();

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(INV_LOCAL_ID, invLocalId);
		filters.put(ENABLED, 1);
		filters.put(ORDER_INV_DATE_MAYQ, startTime);
		filters.put(ORDER_INV_DATE_MENQ, endTime);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_INV_DATE_ASC);
		List<Invoice> resultQuery = invoiceDAO.listOrderFilter(filters, orders);
		List<InvoiceDTO> result = new ArrayList<InvoiceDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});

		return result;

	}

	@Override
	public List<InvoiceDTO> getInvoiceByDay(long invLocalId, String selectedDate) {

		String[] dates = selectedDate.split(CalendarController.CHAR_SEP_DATE);
		String year = dates[0];
		String month = dates[1];
		String day = dates[2];

		Calendar calendarGreg = new GregorianCalendar();
		calendarGreg.set(Calendar.YEAR, new Integer(year));
		calendarGreg.set(Calendar.MONTH, new Integer(month) - 1);
		calendarGreg.set(Calendar.DAY_OF_MONTH, new Integer(day));
		calendarGreg.set(Calendar.HOUR_OF_DAY, 0);
		calendarGreg.set(Calendar.MINUTE, 0);
		calendarGreg.set(Calendar.SECOND, 0);
		calendarGreg.set(Calendar.MILLISECOND, 0);
		Date startTime = calendarGreg.getTime();

		calendarGreg.add(Calendar.HOUR, 24);
		Date endTime = calendarGreg.getTime();

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(INV_LOCAL_ID, invLocalId);
		filters.put(ENABLED, 1);
		filters.put(ORDER_INV_DATE_MAYQ, startTime);
		filters.put(ORDER_INV_DATE_MENQ, endTime);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_INV_DATE_ASC);
		List<Invoice> resultQuery = invoiceDAO.listOrderFilter(filters, orders);
		List<InvoiceDTO> result = new ArrayList<InvoiceDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});

		return result;
	}

	@Override
	public List<InvoiceDTO> getInvoiceByClientAgo(long invLocalId, long clientId) {

		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(INV_CLIENT_ID, clientId);
		filters.put(INV_LOCAL_ID, invLocalId);
		filters.put(ENABLED, 1);
		List<String> orders = new ArrayList<String>();
		orders.add(ORDER_INV_DATE_ASC);
		List<Invoice> resultQuery = invoiceDAO.listOrderFilter(filters, orders);
		List<InvoiceDTO> result = new ArrayList<InvoiceDTO>();
		resultQuery.stream().forEach(entity -> {
			result.add(mapper.map(entity));
		});

		return result;
	}

}