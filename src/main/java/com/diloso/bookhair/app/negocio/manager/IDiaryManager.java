package com.diloso.bookhair.app.negocio.manager;

import com.diloso.bookhair.app.negocio.dto.DiaryDTO;

public interface IDiaryManager {

	DiaryDTO create(DiaryDTO diaryDTO) throws Exception;

	DiaryDTO remove(long id) throws Exception;

	DiaryDTO update(DiaryDTO diaryDTO) throws Exception;

	DiaryDTO getById(long id);

}