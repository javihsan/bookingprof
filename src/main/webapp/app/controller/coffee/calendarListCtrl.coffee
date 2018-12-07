class CalendarListCtrl extends Monocle.Controller

	events:
		"load article#list-calendar" : "loadCalendar"
		"singleTap a[data-action=new]" : "onNew"

	elements:
		"#list-calendar": "listCalendar"
		"header a[href=\\#]" : "header"
		"footer a" : "footer"
		"a[data-router=aside]": "buttonMenu"
		"a[data-action=new]": "buttonAdd"
					
	onNew: (event) -> 
		if  (@listCalendar.hasClass("active"))
			#console.log "onNewCalendar"
			__FacadeCore.Router_section "newCalendar"
	
	loadCalendar: (event) -> 
		#console.log "loadCalendar"
		
		@header.hide()
		@footer.hide()
		@buttonMenu.show()
		@buttonAdd.show()
		
		Lungo.Element.loading "#list-calendar ul", "black"
		
		url = "http://"+appHost+"/calendar/manager/list"
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
			@listCalendar.children().empty()
			
			texts = {cabText:findLangTextElement("label.aside.places")}
			view = new __View.ListCabView model:texts, container:"section#booking article#list-calendar ul"
			view.append texts
			
			for calendarAux in result
				#console.log "calendarAux", calendarAux
				calEnabled = false
				textsTemplates = {enabled:findLangTextElement("form.closed")}
				if calendarAux.enabled==1
					calEnabled = true
					textsTemplates.enabled = findLangTextElement("form.enabled")
				calendar = new __Model.Calendar 
					enabled: calEnabled,
					calId: calendarAux.id,
					calLocalId: calendarAux.calLocalId, 
					calName: calendarAux.calName,
					calDesc: calendarAux.calDesc,
					texts: textsTemplates,
					calLocalTasks: calendarAux.calLabelLocalTasks,
					calLocalTasksId: calendarAux.calLocalTasksId,
					calDiaryMon: calendarAux.calSemanalDiary.semMonDiary,
					calDiaryTue: calendarAux.calSemanalDiary.semTueDiary,
					calDiaryWed: calendarAux.calSemanalDiary.semWedDiary,
					calDiaryThu: calendarAux.calSemanalDiary.semThuDiary,
					calDiaryFri: calendarAux.calSemanalDiary.semFriDiary,
					calDiarySat: calendarAux.calSemanalDiary.semSatDiary,
					calDiarySun: calendarAux.calSemanalDiary.semSunDiary,
					calSemanalDiary: calendarAux.calSemanalDiary
				view = new __View.CalendarListView model:calendar
				view.append calendar
		else
			@listCalendar.children().empty()
			texts = {cabText:findLangTextElement("label.aside.places")}
			view = new __View.ListCabView model:texts, container:"section#booking article#list-calendar ul"
			view.append texts
			Lungo.Notification.success findLangTextElement("label.notification.noData.title"), findLangTextElement("label.notification.noData.text"), null, 3
					

	
__Controller.CalendarList = new CalendarListCtrl "section#booking"