class LocalTaskCalendarCtrl extends Monocle.Controller

	events:
		"load #localTaskCalendar" : "loadLocalTask"
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"singleTap a[data-action=all]" : "onAll"

	elements:
		"#localTaskCalendar-form": "listArt"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
			
	loadLocalTask: (event) -> 
		#console.log "loadLocalTask"
		
		Lungo.Element.loading "#localTaskCalendar-form ul", "black"
		
		url = "http://"+appHost+"/localTask/manager/listOnlySimple"
		local = __FacadeCore.Cache_get appName + "local"
		data = {localId:local.id}
		_this = this
		$$.json url, data, (response) -> 
			_this.showLocalTask response
		
		
	showLocalTask: (response) -> 
		#console.log "showLocalTask"
		if (response.length>0)
			result = Lungo.Core.toArray response
			result = Lungo.Core.orderByProperty result, "lotName", "asc"		
			@listArt.children().empty()
			
			texts = {cabText:findLangTextElement("localTask.select.cabText")}
			view = new __View.ListCabView model:texts, container:"section#localTaskCalendar article#localTaskCalendar-form ul"
			view.append texts
			
			calendar = __FacadeCore.Cache_get appName + "calendar"

			for localTaskAux in result
				lotEnabled = false
				if calendar && calendar.calLocalTasksId
					for h in [0..calendar.calLocalTasksId.length-1]
						if (calendar.calLocalTasksId[h]==localTaskAux.id)
							lotEnabled = true
							break
				localTask = new __Model.LocalTask 
					enabled: lotEnabled,
					lotId: localTaskAux.id,
					lotTaskDuration: localTaskAux.lotTaskDuration,
					lotName: localTaskAux.lotName
				#console.log "localTask",localTask	
				view = new __View.LocalTaskCalendarView model:localTask
				view.append localTask
		else
			@listArt.children().empty()
			texts = {cabText:findLangTextElement("localTask.select.cabText")}
			view = new __View.ListCabView model:texts, container:"section#localTaskCalendar article#localTaskCalendar-form ul"
			view.append texts
			Lungo.Notification.success findLangTextElement("label.notification.noData.title"), findLangTextElement("label.notification.noData.text"), null, 3
					
	onSave: (event) -> 
		#console.log "onSave"
		
		listLocalTask = $$ "#localTaskCalendar-form ul li"

		selectLocalTask = new Array()
		strSelectLocalTask = ""
		strSelectLocalTaskName = ""
		i = -1
		for h in [1..listLocalTask.length-1]
			liLocalTask = $$ listLocalTask[h]
			if liLocalTask.hasClass("accept")
				i++
				selectLocalTask[i] = parseInt(liLocalTask.attr "lotId")
				if i>0
					strSelectLocalTask += ","
					strSelectLocalTaskName += " , "
				strSelectLocalTask += liLocalTask.attr "lotId"
				strSelectLocalTaskName += liLocalTask.attr "lotName"
		if selectLocalTask.length>0	
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"
			
			url = "http://" + appHost + "/calendar/manager/tasks"
			calendar = __FacadeCore.Cache_get appName + "calendar"
			data = {id:calendar.calId, selectedTasks:strSelectLocalTask}
			_this = this
			$$.put url, data, () -> 
					Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, () ->
						calendar.calLocalTasksId = selectLocalTask
						calendar.calLocalTasks = strSelectLocalTaskName
						__Controller.CalendarNew.loadNew event
						__FacadeCore.Router_section "newCalendar"
						_this.resetArticle()
		else
			Lungo.Notification.error findLangTextElement("label.notification.localTaskSelectOne.title"), findLangTextElement("label.notification.localTaskSelectOne.text"), null, 2
		
	onCancel: (event) -> 
		#console.log "onCancel"
		__FacadeCore.Cache_remove appName + "elementSave"
		__FacadeCore.Cache_remove appName + "elementCancel"
		__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
		__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
		Lungo.Element.loading @buttonSave.selector, "black"
		Lungo.Element.loading @buttonCancel.selector, "black"
		__FacadeCore.Router_section "newCalendar"
		this.resetArticle()
	
	onAll: (event) -> 
		#console.log "onAll"
		listLocalTask = $$ "#localTaskCalendar-form ul li"
		for h in [1..listLocalTask.length-1]
			liLocalTask = $$ listLocalTask[h]
			if !liLocalTask.hasClass("accept")
				liLocalTask.removeClass("cancel")
				liLocalTask.addClass("accept theme")	
		
	
	resetArticle: () -> 
		#console.log "resetArticle"
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"		 
	
__Controller.LocalTaskCalendar = new LocalTaskCalendarCtrl "section#localTaskCalendar"