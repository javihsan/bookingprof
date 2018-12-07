class CalendarSelectCtrl extends Monocle.Controller

	events:
		"load #calendarDiarySelect" : "loadCalendar"
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"singleTap a[data-action=all]" : "onAll"

	elements:
		"#calendarDiary-form": "listArt"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
			
	loadCalendar: (event) -> 
		#console.log "loadCalendar"
		
		Lungo.Element.loading "#calendarDiary-form ul", "black"
		
		url = "http://"+appHost+"/calendar/operator/listDiary"
		local = __FacadeCore.Cache_get appName + "local"
		data = {localId:local.id}
		_this = this
		$$.json url, data, (response) -> 
			_this.showCalendar response
		
		
	showCalendar: (response) -> 
		#console.log "showCalendar"
		if (response.length>0)
			result = Lungo.Core.toArray response
			result = Lungo.Core.orderByProperty result, "calName", "asc"		
			@listArt.children().empty()
			
			texts = {cabText:findLangTextElement("place.select.cabText")}
			view = new __View.ListCabView model:texts, container:"section#calendarDiarySelect article#calendarDiary-form ul"
			view.append texts
			
			selectCalendar = __FacadeCore.Cache_get appName+"selectCalendarDiary"

			for calendarAux in result
				#console.log "calendarAux", calendarAux
				calEnabled = false
				if selectCalendar
					for h in [0..selectCalendar.length-1]
						if (selectCalendar[h]==calendarAux.id)
							calEnabled = true
							break
				else 
					calEnabled = true
				calendar = new __Model.Calendar 
					enabled: calEnabled,
					calId: calendarAux.id,
					calLocalId: calendarAux.calLocalId, 
					calName: calendarAux.calName,
					calDesc: calendarAux.calDesc
				view = new __View.CalendarSelectView model:calendar
				view.append calendar
		else
			@listArt.children().empty()
			texts = {cabText:findLangTextElement("place.select.cabText")}
			view = new __View.ListCabView model:texts, container:"section#calendarDiarySelect article#calendarDiary-form ul"
			view.append texts
			Lungo.Notification.success findLangTextElement("label.notification.noData.title"), findLangTextElement("label.notification.noData.text"), null, 3
					
	onSave: (event) -> 
		#console.log "onSave"
		listCalendar = $$ "#calendarDiary-form ul li"

		selectCalendar = new Array()
		i = -1
		for h in [1..listCalendar.length-1]
			liCalendar = $$ listCalendar[h]
			if liCalendar.hasClass("accept")
				i++
				selectCalendar[i] = parseInt(liCalendar.attr "calId")
		if selectCalendar.length>0	
			__FacadeCore.Cache_remove appName+"selectCalendarDiary"
			__FacadeCore.Cache_set appName+"selectCalendarDiary", selectCalendar
			__FacadeCore.Router_article "booking", "list-events"
		else
			Lungo.Notification.error findLangTextElement("label.notification.placeSelectOne.title"), findLangTextElement("label.notification.placeSelectOne.text"), null, 2
		
	onCancel: (event) -> 
		#console.log "onCancel"
		__FacadeCore.Router_back()
	
	onAll: (event) -> 
		#console.log "onAll"
		listCalendar = $$ "#calendarDiary-form ul li"
		for h in [1..listCalendar.length-1]
			liCalendar = $$ listCalendar[h]
			if !liCalendar.hasClass("accept")
				liCalendar.removeClass("cancel")
				liCalendar.addClass("accept theme")	
		
	
__Controller.CalendarSelect = new CalendarSelectCtrl "section#calendarDiarySelect"