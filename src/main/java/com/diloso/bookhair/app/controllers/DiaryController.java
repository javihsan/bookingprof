package com.diloso.bookhair.app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.diloso.bookhair.app.negocio.dto.DiaryDTO;
import com.diloso.bookhair.app.negocio.manager.IDiaryManager;

@Controller
@RequestMapping(value={"/*/diary", "/diary"})
public class DiaryController {
	
	@Autowired
	protected IDiaryManager diaryManager;
	
	@RequestMapping(method = RequestMethod.PUT, value = "/manager/update")
	@ResponseStatus(HttpStatus.OK)
	protected void update(@RequestParam("id") Long id, @RequestParam("selectedTimes") String selectedTimes)
			throws Exception {
					
		List<String> diaTimes = new ArrayList<String>();
		if (selectedTimes!=null && selectedTimes.length()>0){
			String[] a = selectedTimes.split(",");
			for (String strTime : a) {
				diaTimes.add(strTime);
			}
		}	
		DiaryDTO diary = diaryManager.getById(id);
		if (diary!=null){
			diary.setDiaTimes(diaTimes);
			diaryManager.update(diary);
		} 
	}

	public void setDiaryDAO(IDiaryManager iDiaryManager) {
		this.diaryManager = iDiaryManager;
	}
	
		
}

