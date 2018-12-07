class CalendarNewCtrl extends Monocle.Controller
	
	conserveCal = false
	
	events:
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"load #newCalendar" : "loadNew"
		"unload #newCalendar" : "unloadNew"
		"singleTap #liPlaceTasks" : "onTasks"
		"singleTap #liPlaceHours" : "onHours"
		"singleTap #liPlaceHoursMon" : "onHoursSem"
		"singleTap #liPlaceHoursTue" : "onHoursSem"
		"singleTap #liPlaceHoursWed" : "onHoursSem"
		"singleTap #liPlaceHoursThu" : "onHoursSem"
		"singleTap #liPlaceHoursFri" : "onHoursSem"
		"singleTap #liPlaceHoursSat" : "onHoursSem"
		"singleTap #liPlaceHoursSun" : "onHoursSem"
		"doubleTap #liPlaceHoursMon" : "onCloseSem"
		"doubleTap #liPlaceHoursTue" : "onCloseSem"
		"doubleTap #liPlaceHoursWed" : "onCloseSem"
		"doubleTap #liPlaceHoursThu" : "onCloseSem"
		"doubleTap #liPlaceHoursFri" : "onCloseSem"
		"doubleTap #liPlaceHoursSat" : "onCloseSem"
		"doubleTap #liPlaceHoursSun" : "onCloseSem"
			
	elements:
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"#calName" : "calName"
		"#error_calName" : "calNameError"
		"#calDesc" : "calDesc"
		"#error_calDesc" : "calDescError"
		"#placeTasks": "placeTasks"
		"#placeHours": "placeHours"
		"#placeHoursMon": "placeHoursMon"
		"#placeHoursTue": "placeHoursTue"
		"#placeHoursWed": "placeHoursWed"
		"#placeHoursThu": "placeHoursThu"
		"#placeHoursFri": "placeHoursFri"
		"#placeHoursSat": "placeHoursSat"
		"#placeHoursSun": "placeHoursSun"

			
	loadNew: (event) -> 
		#console.log "loadNew"	
		
		conserveCal = false
		
		calendar = __FacadeCore.Cache_get appName + "calendar"
		#console.log "calendar", calendar	
			
		if calendar
			@calName.val calendar.calName
			@calDesc.val calendar.calDesc
			@placeTasks.parent().show()
			@placeHours.parent().show()
			@placeHoursMon.parent().show()
			@placeHoursTue.parent().show()
			@placeHoursWed.parent().show()
			@placeHoursThu.parent().show()
			@placeHoursFri.parent().show()
			@placeHoursSat.parent().show()
			@placeHoursSun.parent().show()
			@placeTasks.html calendar.calLocalTasks
			@placeHoursMon.html getStrDiary(calendar.calDiaryMon)
			@placeHoursTue.html getStrDiary(calendar.calDiaryTue)
			@placeHoursWed.html getStrDiary(calendar.calDiaryWed)
			@placeHoursThu.html getStrDiary(calendar.calDiaryThu)
			@placeHoursFri.html getStrDiary(calendar.calDiaryFri)
			@placeHoursSat.html getStrDiary(calendar.calDiarySat)
			@placeHoursSun.html getStrDiary(calendar.calDiarySun)
			
			asyn = __FacadeCore.Service_Settings_asyncFalse()
		
			url = "http://"+appHost+"/annual/manager/getAnnualDiaryCalendarByDate"
			local = __FacadeCore.Cache_get appName + "local"	
			selectedDate = dateToString new Date()
			data = {id:calendar.calId,localId:local.id,selectedDate:selectedDate.toString()}
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
			@placeHours.html strHours
		else	
			@placeTasks.parent().hide()
			@placeHours.parent().hide()
			@placeHoursMon.parent().hide()
			@placeHoursTue.parent().hide()
			@placeHoursWed.parent().hide()
			@placeHoursThu.parent().hide()
			@placeHoursFri.parent().hide()
			@placeHoursSat.parent().hide()
			@placeHoursSun.parent().hide()
	
	onSave: (event) -> 
		#console.log "onSave"
		if (this.validateForm())
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"
			
			local = __FacadeCore.Cache_get appName + "local"
			calendar = __FacadeCore.Cache_get appName + "calendar"
			
			url = "http://"+appHost+"/calendar/manager/new"
			data = {localId:local.id, calName: @calName.val(), calDesc: @calDesc.val()}
			if calendar
				data.id = calendar.calId
			_this = this
			$$.post url, data, (response) ->
					Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, (response) ->
						__FacadeCore.Router_article "booking", "list-calendar"
			
	
	validateForm: -> 
		#console.log "validateForm"
		result = true
		@calNameError.html  ""
		@calDescError.html  ""
		if (!@calName[0].checkValidity())
			@calNameError.html getMessageValidity(@calName[0])
			@calName[0].focus()
			result = false
		else if (!@calDesc[0].checkValidity())
			@calDescError.html getMessageValidity(@calDesc[0])
			@calDesc[0].focus()
			result = false
		result
	
	onCancel: (event) -> 
		#console.log "cancel"	
		__FacadeCore.Cache_remove appName + "elementSave"
		__FacadeCore.Cache_remove appName + "elementCancel"
		__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
		__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
		Lungo.Element.loading @buttonSave.selector, "black"
		Lungo.Element.loading @buttonCancel.selector, "black"
		__FacadeCore.Router_article "booking", "list-calendar"
		
	resetArticle: () ->
		#console.log "resetArticle"	
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		@calName.val ""
		@calDesc.val ""
		@calNameError.html  ""
		@calDescError.html  ""
		@placeTasks.html ""
		@placeHoursMon.html ""
		@placeHoursTue.html ""
		@placeHoursWed.html ""
		@placeHoursThu.html ""
		@placeHoursFri.html ""
		@placeHoursSat.html ""
		@placeHoursSun.html ""
		@placeHours.html ""
			
	unloadNew: (event) -> 
		#console.log "unloadNew"	
		this.resetArticle()
		if !conserveCal
			__FacadeCore.Cache_remove appName + "calendar"
	
	onTasks: (event) -> 
		calendar = __FacadeCore.Cache_get appName + "calendar"
		if calendar && calendar.calId
			#console.log "onTasks"
			conserveCal = true
			__FacadeCore.Router_section "localTaskCalendar"
	
	onHoursSem: (event) -> 
		calendar = __FacadeCore.Cache_get appName + "calendar"
		if calendar && calendar.calId
			#console.log "onHoursSem"
			sem = event.currentTarget.id.substring event.currentTarget.id.length-3 
			diary = eval ("calendar.calDiary"+sem)
			diary.sem = sem
			__FacadeCore.Cache_remove appName + "diary"
			__FacadeCore.Cache_set appName + "diary", diary
			conserveCal = true
			__FacadeCore.Router_section "newHours"
	
	onHours: (event) -> 
		calendar = __FacadeCore.Cache_get appName + "calendar"
		if calendar && calendar.calId
			#console.log "onHours"
			conserveCal = true
			__FacadeCore.Router_section "booking-hours"		
	 
	onCloseSem: (event) ->
		if __FacadeCore.isDoubleTap event
			calendar = __FacadeCore.Cache_get appName + "calendar"
			if calendar && calendar.calId
				#console.log "onCloseSem"
				closed = $$("#"+event.currentTarget.id+ " span div")[0]
				if !closed
					url = "http://"+appHost+"/diary/manager/update"
					sem = event.currentTarget.id.substring event.currentTarget.id.length-3
					diary = eval ("calendar.calDiary"+sem)
					data = {id:diary.id,selectedTimes:""}
					_this = this
					$$.put url, data, () ->
							diary.diaTimes = null
							eval ("calendar.calDiary"+sem+" = diary")
							eval ("_this.placeHours"+sem+".html (getStrDiary(diary))")
							calendar.calSemanalDiary.closedDiary[calendar.calSemanalDiary.closedDiary.length] = getSemDayNum sem
							Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3
		 
	 
	 
__Controller.CalendarNew = new CalendarNewCtrl "section#newCalendar"
