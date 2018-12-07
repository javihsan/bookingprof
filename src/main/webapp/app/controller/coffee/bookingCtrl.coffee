class BookingCtrl extends Monocle.Controller

	events:
		"load article#table-day" : "loadCalendarDay"
		"singleTap a[data-action=today]" : "onToday"
		"singleTap article#table-day thead tr:first-child" : "onSelectedTasks"
		"singleTap article#table-day thead tr:last-child th:first-child" : "onPreviusDay"
		"singleTap article#table-day thead tr:last-child th:last-child" : "onNextDay"

	elements:
		"#table-day": "tableDay"
		"#table-day-container": "tableDayContainer"
		"#daySelected": "daySelected"
		"#tasksSelected": "tasksSelected"
		"article#table-day thead tr:last-child th:first-child" : "previusDay"
		"article#table-day thead tr:last-child th:last-child" : "nextDay"
		"header a[href=\\#]" : "header"
		"a[data-action=today]" : "today"
			
	onSelectedTasks: (event) -> 
		#console.log "onSelectedTasks"
		__FacadeCore.Router_article "taskSelect","task-form"
					
	loadCalendarDay: (event) -> 
		#console.log "article table-day load"
				
		selectedTasks = __FacadeCore.Cache_get appName+"selectedTasks"
		if selectedTasks
			selectedTasksCount = __FacadeCore.Cache_get appName+"selectedTasksCount"
			
			@header.hide()
			@today.show()
				
			Lungo.Element.loading "#table-day-container", "black"
			
			selectedDate = __FacadeCore.Cache_get appName+"selectedDate"
			
			local = __FacadeCore.Cache_get appName + "local"
			openDaysAux = local.locOpenDays
	
			selectedCalendars = __FacadeCore.Cache_get appName+"selectedCalendars"
			if !selectedCalendars
				selectedCalendarsParam = ""
				strSelectedCalendars = ""
			else
				selectedCalendarsParam = new Array()
				strSelectedCalendars = "<br> "+findLangTextElement("label.header.places")+ ":"
				h=0
				for calendarSel in selectedCalendars
					strSelectedCalendars += " " + calendarSel.name
					selectedCalendarsParam[h] = calendarSel.id
					h++
	
			a = selectedDate.split('-')
			newDayAux = new Date(a[0],(a[1]-1),a[2])
	
			oneDay = 1000 * 60 * 60 * 24;
			today = new Date();
			today.setHours(0);
			today.setMinutes(0);
			today.setSeconds(0);
			today.setMilliseconds(0)	
			maxDate = new Date();
			maxDate.setTime(today.getTime() + (openDaysAux*oneDay))
	
			@previusDay.removeClass "not_enabled"
			@nextDay.removeClass "not_enabled"
			if !adminOption
				if (newDayAux<=today)
					@previusDay.addClass "not_enabled"
				if (newDayAux>=maxDate)
					@nextDay.addClass "not_enabled"
			
			@daySelected.html dateToStringFormat(newDayAux)
			h=0
			selectTaskParam = new Array()
			if selectedTasks[0].numLines
				strTasks = findLangTextElement("label.template.numLines")+": "+selectedTasks[0].numLines
				strTasks += " - "+findLangTextElement("label.template.numPallets")+": "+selectedTasks[0].numPallets
			else
				strTasks = findLangTextElement("label.html.apoFor1")
				if local.locNumPersonsApo > 1
					strTasks += " "+ selectedTasksCount.length + " "+findLangTextElement("label.html.apoFor2") 
				strTasks += ": "
				for taskSel in selectedTasks
					if local.locMulServices == 1
						strTasks += " 1 "
					strTasks += taskSel.tasName
					selectTaskParam[h] = taskSel.id
					h++
			strTasks += strSelectedCalendars
			strTasks += "<br>"+findLangTextElement("label.html.apoFor3")
			@tasksSelected.html strTasks
			
			data = {localId:local.id,selectedDate:selectedDate.toString(),selectedTasks:selectTaskParam,selectedTasksCount:selectedTasksCount,selectedCalendars:selectedCalendarsParam}
			if selectedTasks[0].numLines
				data.numLines = selectedTasks[0].numLines
				data.numPallets = selectedTasks[0].numPallets
			_this = this
			$$.json urlListApoByDay, data, (response) -> 
				_this.showCalendarDay response,data
		else
			@tasksSelected.html ""
			timeNot = 3
			if adminOption
				timeNot = 1
			Lungo.Notification.success findLangTextElement("label.notification.selectTask.title"), findLangTextElement("label.notification.selectTask.text"), null, timeNot, (response) ->
					__FacadeCore.Router_article "taskSelect","task-form"
			
		
	showCalendarDay: (response,data) ->	
		#console.log "showCalendarDay", response
		
		resultApos = Lungo.Core.toArray response
		resultApos = Lungo.Core.orderByProperty resultApos, "apoName", "asc"
					
		margin_top_first = 15
		margin_left_first = 3
		margin_top_row = 100
		margin_left_row = 16
		
		@tableDayContainer.empty()
		
		if resultApos.length>0				
					
			hours = []
			for h in [0..23]
				hours[h] = [0,0]
			
			# Buscamos las horas que tienen citas
			for appointmentAux in resultApos
				hourAux = parseInt(appointmentAux.apoName.split(":")[0])
				hours[hourAux][1] = 1

			# Reordenamos el orden de las horas que tienen citas
			x = -1
			for h in [0..23]
				if hours[h][1] == 1
					x++
					hours[h][1] = x
			firm = __FacadeCore.Cache_get appName + "firm"
			calendars = __FacadeCore.Cache_get appName + "calCandidates"
			numCals = __FacadeCore.Cache_get appName + "numCals"
			bgColor = 0
			# Pintamos las citas
			for appointmentAux in resultApos
				#console.log "appointmentAux", appointmentAux
				hourAux = parseInt(appointmentAux.apoName.split(":")[0])
				num_apo = hours[hourAux][0]
				hours[hourAux][0] = hours[hourAux][0] + 1
				top_hour = hours[hourAux][1]
				if numCals>1 && firm.firConfig.configLocal.configLocSelCalAfter
					margin_left_row = 21
					bgColor = 1
					for cal in calendars
						if cal.id != appointmentAux.apoCalendarId
							bgColor++
						else
							break	
				appointment = new __Model.Appointment
					enabled: true,
					apoName: appointmentAux.apoName,
					apoStartTime: appointmentAux.apoStartTime,
					apoCalendarId: appointmentAux.apoCalendarId,
					apoCalendarName: appointmentAux.apoCalendarName,
					apoX: (top_hour * margin_top_row) + margin_top_first,
					apoY: (num_apo * margin_left_row) + margin_left_first,
					bgColor: bgColor
				#console.log "appointment", appointment
				if bgColor>0
					view = new __View.BookingDaySPView model:appointment
				else	
					view = new __View.BookingDayView model:appointment
				view.append appointment	
		
		else
			if !adminOption
				Lungo.Notification.success findLangTextElement("label.notification.notavailable.title"), findLangTextElement("label.notification.notavailable.text"), null, 5, (response) ->
					__FacadeCore.Router_article "taskSelect","task-form"
			else
				Lungo.Notification.success findLangTextElement("label.notification.notavailableAdmin.title"), findLangTextElement("label.notification.notavailableAdmin.text"), null, 5
				a = data.selectedDate.split('-')
				newDayAux = new Date(a[0],(a[1]-1),a[2])
				todayAux = new Date()
				newDayAux.setHours (todayAux.getHours() - (todayAux.getTimezoneOffset() / 60))
				newDayAux.setMinutes todayAux.getMinutes()
				appointment = new __Model.Appointment
					enabled: true,
					apoStartTime: newDayAux
				#console.log "appointment", appointment
				__FacadeCore.Cache_remove appName+"newApo"
				__FacadeCore.Cache_set appName+"newApo", appointment
				__FacadeCore.Router_section "#newEvent"
			
	onToday: (event) -> 
		#console.log "a[data-action=today] onToday"
				
		if  (@tableDay.hasClass("active"))
			newDayAux = new Date()
			selectedDate =  dateToString(newDayAux)
			__FacadeCore.Cache_remove appName+"selectedDate"
			__FacadeCore.Cache_set appName+"selectedDate", selectedDate
			this.loadCalendarDay(event)	
			
	onPreviusDay: (event) -> 
		#console.log "onPreviusDay"
		if (!@previusDay.hasClass("not_enabled"))
			this.changeDay(event,-1)	
	
	onNextDay: (event) -> 
		#console.log "onNextDay"
		if (!@nextDay.hasClass("not_enabled"))
			this.changeDay(event,1)
		
	changeDay: (event,delta) -> 
		#console.log "changeDay"
		
		selectedDate = __FacadeCore.Cache_get appName+"selectedDate"
		a = selectedDate.split('-')
		newDayAux = new Date(a[0],(a[1]-1),a[2])
			
		oneDay = 1000 * 60 * 60 * 24;
		newDayAux.setTime(newDayAux.getTime() + (delta*oneDay));
		
		selectedDate =  dateToString(newDayAux)
		
		__FacadeCore.Cache_remove appName+"selectedDate"
		__FacadeCore.Cache_set appName+"selectedDate", selectedDate

		this.loadCalendarDay(event)		


					
__Controller.Booking = new BookingCtrl "section#booking"