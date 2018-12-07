class HoursNewCtrl extends Monocle.Controller
	
	events:
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"singleTap a[data-action=new]" : "addTime"
		"load #newHours" : "loadHours"
		
	elements:
		"#hours-form": "listArt"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
					
	loadHours: (event) -> 
		#console.log "loadHours"
		
		@listArt.children().empty()
		
		calendar = __FacadeCore.Cache_get appName + "calendar"
		selectedDateHours = __FacadeCore.Cache_get appName + "selectedDateHours"
		if selectedDateHours
			a = selectedDateHours.split('-')
			newDayAux = new Date(a[0],(a[1]-1),a[2])
			dateFormat = dateToStringFormat(newDayAux)
				
			asyn = __FacadeCore.Service_Settings_asyncFalse()
			
			if calendar
	    		url = "http://" + appHost + "/annual/manager/listCalendarByDay"
	    		data = {id:calendar.calId,selectedDate:selectedDateHours.toString()}
    		else
	    		url = "http://" + appHost + "/annual/manager/listByDay"
	    		local = __FacadeCore.Cache_get appName+"local"
	    		data = {localId:local.id,selectedDate:selectedDateHours.toString()}
			
			diary = $$.json url, data
		
			__FacadeCore.Service_Settings_async asyn
			
		else
			diary = __FacadeCore.Cache_get appName + "diary"
			dateFormat = getSemDay(diary.sem)
		
		#console.log "diary",diary
		
		if calendar
	    	dateFormat += " - "+ calendar.calName
		
		this.addCabTime(dateFormat)
		i=1
		if diary && diary.diaTimes
			timFrom = null
			timUntil = null
			for times in diary.diaTimes
				if !timFrom
					timFrom = times
				else
					timUntil = times
					textsTemplates = {hourFromUntil:findLangTextElement("label.template.hourFromUntil"),delete:findLangTextElement("form.delete")}
					timesMod = new __Model.TimesHours
						timId: i,
						timFrom: timFrom,
						timUntil: timUntil,
						texts: textsTemplates
					#console.log "timesMod",timesMod
					view = new __View.TimesHoursView model:timesMod
					view.append timesMod
					i++
					timFrom = null
		if i==1
			this.addTime()
	
	addCabTime: (dateFormat) ->	
		#console.log "addCabTime"
		textsTemplates = {hoursNewText:findLangTextElement("label.template.hoursNewText")}
		timesMod = new __Model.TimesHours
			timDay: dateFormat,
			texts: textsTemplates
		view = new __View.TimesHoursCabView model:timesMod
		view.append timesMod
	
	addTime: -> 
		#console.log "addTime"
		i = 1
		while (Lungo.dom "article#hours-form #timFrom"+i)[0]
			i++
		
		textsTemplates = {hourFromUntil:findLangTextElement("label.template.hourFromUntil"),delete:findLangTextElement("form.delete")}
		timesMod = new __Model.TimesHours
			timId: i,
			texts: textsTemplates
		view = new __View.TimesHoursView model:timesMod
		view.append timesMod
		
	onSave: (event) -> 
		#console.log "onSave"
		if (this.validateForm())
			selectedTimes = ""
			arraySelectedTimes = new Array()
			listTim = Lungo.dom "article#hours-form ul li"
			
			h = -1
			save = true
			if listTim.length>1
				for i in [1..listTim.length-1]
					timFrom = $$(listTim[i]).find('input')[0]
					timUntil = $$(listTim[i]).find('input')[1]
					if $$(timFrom).val()>=$$(timUntil).val()
						save = false
						timUntil.focus()
						break
					else	
						if i>1
							selectedTimes += ","
						selectedTimes += $$(timFrom).val()+","+$$(timUntil).val()
						h++
						arraySelectedTimes[h] = $$(timFrom).val()
						h++
						arraySelectedTimes[h] = $$(timUntil).val()
			
				if save

					__FacadeCore.Cache_remove appName + "elementSave"
					__FacadeCore.Cache_remove appName + "elementCancel"
					__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
					__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
					Lungo.Element.loading @buttonSave.selector, "black"
					Lungo.Element.loading @buttonCancel.selector, "black"

					selectedDateHours = __FacadeCore.Cache_get appName + "selectedDateHours"
					if selectedDateHours
						a = selectedDateHours.split('-')
						newDayAux = new Date(a[0],(a[1]-1),a[2])
						dateFormat = dateToStringFormat(newDayAux)
						calendar = __FacadeCore.Cache_get appName + "calendar"
						if calendar
							if firm.firConfig.configLocal.configLocRepeat
								url = "http://"+appHost+"/event/operator/listCalendarByDayRepeat"
							else
								url = "http://"+appHost+"/event/operator/listCalendarByDay"
							data = {id:calendar.calId,selectedDate:selectedDateHours.toString()}
						else
							if firm.firConfig.configLocal.configLocRepeat
								url = "http://"+appHost+"/event/operator/listByDayRepeat"
							else
								url = "http://"+appHost+"/event/operator/listByDay"	
							local = __FacadeCore.Cache_get appName + "local"
							data = {localId:local.id,selectedDate:selectedDateHours.toString()}
						_this = this
						$$.json url, data, (response) -> 
							_this.updateForm response,selectedDateHours,selectedTimes
					else
						url = "http://"+appHost+"/diary/manager/update"
						diary = __FacadeCore.Cache_get appName + "diary"
						data = {id:diary.id,selectedTimes:selectedTimes}
						_this = this
						$$.put url, data, () ->
								Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, () ->
									diary.diaTimes = arraySelectedTimes
									calendar = __FacadeCore.Cache_get appName + "calendar"
									if calendar
										eval ("calendar.calDiary"+diary.sem+" = diary")
										index = calendar.calSemanalDiary.closedDiary.indexOf(getSemDayNum diary.sem)
										if index > -1
    										calendar.calSemanalDiary.closedDiary.splice index, 1
										__Controller.CalendarNew.loadNew event
										__FacadeCore.Router_section "newCalendar"
									else	
										local = __FacadeCore.Cache_get appName + "local"
										eval ("local.locSemanalDiary.sem"+diary.sem+"Diary = diary")
										index = local.locSemanalDiary.closedDiary.indexOf(getSemDayNum diary.sem)
										if index > -1
    										local.locSemanalDiary.closedDiary.splice index, 1
										__Controller.Hours.onLoad event
										__FacadeCore.Router_article "booking","local-hours"
									_this.resetArticle()
				else
					Lungo.Notification.error findLangTextElement("label.notification.hourMayor.title"), findLangTextElement("label.notification.hourMayor.text"), null, 3
			else
				Lungo.Notification.error findLangTextElement("label.notification.oneHour.title"), findLangTextElement("label.notification.oneHour.text"), null, 3
							
	updateForm: (response,selectedDateHours,selectedTimes) -> 						
		#console.log "updateForm"
		if response && response.length>0
			_this = this
			dataAccept = {icon: 'checkmark', label: 'Accept', callback: ()-> _this.updateFormConfirm(selectedDateHours,selectedTimes) }
			dataCancel = {icon: 'checkmark', label: 'Cancel', callback: ()-> _this.resetArticle()}
			dataConfirm = {icon: 'user', title: findLangTextElement("label.notification.modifyHourWithApo.title"), description: findLangTextElement("label.notification.modifyHourWithApo.text"), accept: dataAccept, cancel: dataCancel}
			Lungo.Notification.confirm dataConfirm
		else
			this.updateFormConfirm(selectedDateHours,selectedTimes)
	
	
	updateFormConfirm: (selectedDateHours, selectedTimes) -> 
		#console.log "updateFormConfirm"
		calendar = __FacadeCore.Cache_get appName + "calendar"
		if calendar
			url = "http://" + appHost + "/annual/manager/hoursCalendar"
			data = {id:calendar.calId,selectedDate:selectedDateHours.toString(),selectedTimes:selectedTimes}
		else
	    	url = "http://" + appHost + "/annual/manager/hoursLocal"
	    	local = __FacadeCore.Cache_get appName + "local"
	    	data = {localId:local.id,selectedDate:selectedDateHours.toString(),selectedTimes:selectedTimes}
		_this = this
		$$.put url, data, () -> 
				Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, () ->
					__FacadeCore.Router_section "booking-hours"
					_this.resetArticle()
	
	validateForm: -> 
		#console.log "validateForm"
		result = true
		listTim = Lungo.dom "article#hours-form ul li"
		if listTim.length>1
			for i in [1..listTim.length-1]
				timFrom = $$(listTim[i]).find('input')[0]
				timUntil = $$(listTim[i]).find('input')[1]
				timError = $$(listTim[i]).find('label')[0]
				#console.log "validateForm",timFrom,timUntil,timError
				if (!timFrom.checkValidity())
					$$(timError).html getMessageValidity(timFrom)
					timFrom.focus()
					result = false
					break
				else if (!timUntil.checkValidity())
					$$(timError).html getMessageValidity(timUntil)
					timUntil.focus()
					result = false
					break
		result
	
	
	onCancel: (event) -> 
		#console.log "cancel"	
		__FacadeCore.Cache_remove appName + "elementSave"
		__FacadeCore.Cache_remove appName + "elementCancel"
		__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
		__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
		Lungo.Element.loading @buttonSave.selector, "black"
		Lungo.Element.loading @buttonCancel.selector, "black"
		selectedDateHours = __FacadeCore.Cache_get appName + "selectedDateHours"
		if selectedDateHours
			__FacadeCore.Router_section "booking-hours"
		else
    		calendar = __FacadeCore.Cache_get appName + "calendar"
    		if calendar
    			__FacadeCore.Router_section "newCalendar"
    		else
    			__FacadeCore.Router_article "booking","local-hours"
		this.resetArticle()
	
	resetArticle: () -> 
		#console.log "resetArticle"
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		__FacadeCore.Cache_remove appName + "selectedDateHours"
		__FacadeCore.Cache_remove appName + "diary"
		
			
__Controller.HoursNew = new HoursNewCtrl "section#newHours"