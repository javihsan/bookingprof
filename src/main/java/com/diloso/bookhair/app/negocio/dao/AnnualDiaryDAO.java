package com.diloso.bookhair.app.negocio.dao;

import java.util.List;

import com.diloso.bookhair.app.negocio.dto.AnnualDiaryDTO;

public interface AnnualDiaryDAO {

	AnnualDiaryDTO create(AnnualDiaryDTO annualDiary) throws Exception;

	AnnualDiaryDTO remove(long id) throws Exception;

	AnnualDiaryDTO update(AnnualDiaryDTO annualDiary) throws Exception;

	AnnualDiaryDTO getById(long id);
	
	List<AnnualDiaryDTO> getAnnualDiary(long localId);
	
	AnnualDiaryDTO getAnnualDiaryByDay(long localId, String selectedDate);
	
	List<AnnualDiaryDTO> getAnnualDiaryByMonth(long localId, String selectedDate);
	
	List<AnnualDiaryDTO> getAnnualDiaryByDate(long localId, String selectedDate, int numDays);
	
	AnnualDiaryDTO getAnnualDiaryCalendarByDay(long calId, String selectedDate);
	
	List<AnnualDiaryDTO> getAnnualDiaryCalendarByMonth(long calId, String selectedDate);
	
	List<AnnualDiaryDTO> getAnnualDiaryCalendarByDate(long calId, String selectedDate, int numDays);
	
	AnnualDiaryDTO getAnnualDiaryRepatByDay(long repeatId, String selectedDate);
	
}