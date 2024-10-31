package com.diloso.bookhair.app.negocio.manager;

import com.diloso.bookhair.app.negocio.dto.SemanalDiaryDTO;

public interface ISemanalDiaryManager {

	SemanalDiaryDTO create(SemanalDiaryDTO semanalDiaryDTO) throws Exception;

	SemanalDiaryDTO remove(long id) throws Exception;

	SemanalDiaryDTO update(SemanalDiaryDTO semanalDiaryDTO) throws Exception;

	SemanalDiaryDTO getById(long id);

}