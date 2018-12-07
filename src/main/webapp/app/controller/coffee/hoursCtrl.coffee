class HoursCtrl extends Monocle.Controller
	
	events:
		"load article#local-hours" : "onLoad"
		"singleTap #liHours" : "onHours"
		"singleTap #liHoursMon" : "onHoursSem"
		"singleTap #liHoursTue" : "onHoursSem"
		"singleTap #liHoursWed" : "onHoursSem"
		"singleTap #liHoursThu" : "onHoursSem"
		"singleTap #liHoursFri" : "onHoursSem"
		"singleTap #liHoursSat" : "onHoursSem"
		"singleTap #liHoursSun" : "onHoursSem"
		"doubleTap #liHoursMon" : "onCloseSem"
		"doubleTap #liHoursTue" : "onCloseSem"
		"doubleTap #liHoursWed" : "onCloseSem"
		"doubleTap #liHoursThu" : "onCloseSem"
		"doubleTap #liHoursFri" : "onCloseSem"
		"doubleTap #liHoursSat" : "onCloseSem"
		"doubleTap #liHoursSun" : "onCloseSem"
			
	elements:
		"header a[href=\\#]" : "header"
		"footer a" : "footer"
		"#placeTasks": "placeTasks"
		"#spHours": "spHours"
		"#spHoursMon": "spHoursMon"
		"#spHoursTue": "spHoursTue"
		"#spHoursWed": "spHoursWed"
		"#spHoursThu": "spHoursThu"
		"#spHoursFri": "spHoursFri"
		"#spHoursSat": "spHoursSat"
		"#spHoursSun": "spHoursSun"
	
	onLoad: (event) -> 
		#console.log "onLoad"	
		
		@header.hide()
		@footer.hide()
		
		local = __FacadeCore.Cache_get appName + "local"
		#console.log "local", local	
			
		if local
			@spHoursMon.html getStrDiary(local.locSemanalDiary.semMonDiary)
			@spHoursTue.html getStrDiary(local.locSemanalDiary.semTueDiary)
			@spHoursWed.html getStrDiary(local.locSemanalDiary.semWedDiary)
			@spHoursThu.html getStrDiary(local.locSemanalDiary.semThuDiary)
			@spHoursFri.html getStrDiary(local.locSemanalDiary.semFriDiary)
			@spHoursSat.html getStrDiary(local.locSemanalDiary.semSatDiary)
			@spHoursSun.html getStrDiary(local.locSemanalDiary.semSunDiary)
			
			asyn = __FacadeCore.Service_Settings_asyncFalse()
		
			url = "http://"+appHost+"/annual/manager/getAnnualDiaryByDate"
			selectedDate = dateToString new Date()
			data = {localId:local.id,selectedDate:selectedDate.toString()}
			annuals = Lungo.Core.toArray $$.json(url, data)
			annuals = Lungo.Core.orderByProperty annuals, "anuDate", "asc"
		
			__FacadeCore.Service_Settings_async asyn
			
			strHours = "";
			indxHours = -1
			max = 5
			for annual in annuals
				date = new Date annual.anuDate
				indxHours++
				if indxHours <= max
					if indxHours > 0
						strHours += " , "
					if indxHours == max
						strHours += "..."
					else	
						strHours += dateToStringYearLast date
			@spHours.html strHours
		else
			@spHoursMon.html ""
			@spHoursTue.html ""
			@spHoursWed.html ""
			@spHoursThu.html ""
			@spHoursFri.html ""
			@spHoursSat.html ""
			@spHoursSun.html ""
	
	onHoursSem: (event) -> 
		local = __FacadeCore.Cache_get appName + "local"
		if local
			#console.log "onHoursSem"
			sem = event.currentTarget.id.substring event.currentTarget.id.length-3 
			diary = eval ("local.locSemanalDiary.sem"+sem+"Diary")
			diary.sem = sem
			__FacadeCore.Cache_remove appName + "diary"
			__FacadeCore.Cache_set appName + "diary", diary
			__FacadeCore.Router_section "newHours"
	
	onHours: (event) -> 
		local = __FacadeCore.Cache_get appName + "local"
		if local
			#console.log "onHours"
			__FacadeCore.Router_section "booking-hours"		
	 
	 
	onCloseSem: (event) ->
		if __FacadeCore.isDoubleTap event
			local = __FacadeCore.Cache_get appName + "local"
			if local
				#console.log "onCloseSem"
				closed = $$("#"+event.currentTarget.id+ " span div")[0]
				if !closed
					url = "http://"+appHost+"/diary/manager/update"
					sem = event.currentTarget.id.substring event.currentTarget.id.length-3
					diary = eval ("local.locSemanalDiary.sem"+sem+"Diary")
					data = {id:diary.id,selectedTimes:""}
					_this = this
					$$.put url, data, () ->
							diary.diaTimes = null
							eval ("local.locSemanalDiary.sem"+sem+"Diary = diary")
							eval ("_this.spHours"+sem+".html (getStrDiary(diary))")
							local.locSemanalDiary.closedDiary[local.locSemanalDiary.closedDiary.length] = getSemDayNum sem
							Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3


__Controller.Hours = new HoursCtrl "section#booking"