class EventListCtrl extends Monocle.Controller
	
	result = null
	
	events:
		"load article#list-events" : "loadListEvents"
		"singleTap a[data-action=places]" : "onPlaces"
			
	elements:
		"#list-events": "listEvents"
		"header a[href=\\#]" : "header"
		"a[data-action=places]" : "places"
		
	setResult: (res) -> 
		#console.log "setResult", result, res
		result = null
	
	onPrevius: (event) -> 
		#console.log "onPrevius"
		this.change(event,-1)	
	
	onNext: (event) -> 
		#console.log "onNext"
		this.change(event,1)
	
	change: (event,delta) -> 
		#console.log "change"
		
		selectedDate = __FacadeCore.Cache_get appName+"selectedDateDiary"
		a = selectedDate.split('-')
		newDayAux = new Date(a[0],(a[1]-1),a[2])
			
		oneWeek = 1000 * 60 * 60 * 24 * 7
		newDayAux.setTime newDayAux.getTime() + (delta*oneWeek)
		
		selectedDate = dateToString(newDayAux)
		
		__FacadeCore.Cache_remove appName+"selectedDateDiary"
		__FacadeCore.Cache_set appName+"selectedDateDiary", selectedDate
		
		result = null
		
		this.loadListEventsWeek(event)	
	
	loadListEvents: (event) ->
		#console.log "loadListEvents"
		
		@header.hide()
		@places.show()
		
		selectedDate = __FacadeCore.Cache_get appName+"selectedDateDiary"
		if !selectedDate
			newDayAux = new Date()
			selectedDate =  dateToString(newDayAux)
			__FacadeCore.Cache_remove appName+"selectedDateDiary"
			__FacadeCore.Cache_set appName+"selectedDateDiary", selectedDate
		
		result = null
		
		this.loadListEventsWeek(event)
			
	loadListEventsWeek: (event) -> 
		#console.log "loadListEventsWeek"
		Lungo.Element.loading "#list-events ul", "black"
		
		selectedDate = __FacadeCore.Cache_get appName+"selectedDateDiary"
		a = selectedDate.split('-')
		newDay = new Date(a[0],(a[1]-1),a[2])
		# Calculamos el lunes de la semana a la que pertenece selectedDate
		dayWeek = newDay.getDay()
		if (dayWeek==0)
			dayWeek = 6
		else
			dayWeek = dayWeek-1
		oneDay = 1000 * 60 * 60 * 24
		dayFirstWeek = new Date()
		dayFirstWeek.setTime(newDay.getTime() - (dayWeek*oneDay));
		if !result
			
			selectedDateFirstWeek =  dateToString(dayFirstWeek)
							
			local = __FacadeCore.Cache_get appName+"local"
			data = {localId:local.id,selectedDate:selectedDateFirstWeek.toString()}
			_this = this
			$$.json urlListByDiary, data, (response) -> 
				_this.showListEvents response,newDay,dayFirstWeek
		else
			this.showListEvents null,newDay,dayFirstWeek
			
			
	showListEvents: (response,daySelect,dayFirstWeek) -> 
		#console.log "showListEvents"
		if !result
			result = Lungo.Core.toArray response
			result = Lungo.Core.orderByProperty result,"eveStartTime","asc"
		#console.log "result", result
		@listEvents.children().empty()
		textsTemplates = {displayUntil:findLangTextElement("label.template.displayUntil"),pressDisplayMore:findLangTextElement("label.template.pressDisplayMore")}
		eventMov = new __Model.Event
			eveDay: dateToStringFormat(dayFirstWeek),
			texts: textsTemplates
		view = new __View.EventListFromView model:eventMov
		view.append eventMov
		
		dateDaySelect = dateToStringFormat(daySelect)
		dateDayAnt = ""
		textsTemplates = {pressDisplay:findLangTextElement("label.template.pressDisplay"),finished:findLangTextElement("label.template.finished"),rejected:findLangTextElement("label.template.rejected")}
		for eventAux in result
			#console.log "eventAux", eventAux
			selectCalendar = __FacadeCore.Cache_get appName+"selectCalendarDiary"
			calEnabled = false
			if selectCalendar
				for h in [0..selectCalendar.length-1]
					if (selectCalendar[h]==eventAux.eveCalendarId)
						calEnabled = true
						break
			else 
				calEnabled = true
			if calEnabled
				dateHour = new Date(eventAux.eveStartTime)
				dateHourEnd = new Date(eventAux.eveEndTime)
				dateDay = dateToStringFormat(dateHour)
				rate = 0
				consumed = false
				if eventAux.eveConsumed==1
					consumed = true
				rejected = false
				if eventAux.eveConsumed==2
					rejected = true	
				descrip = eventAux.eveDesc
				if (!eventAux.eveClient)
					descrip = eventAux.repName	
				event = new __Model.Event 
					eveId: eventAux.id,
					eveStartTime: eventAux.eveStartTime,
					eveDay: dateDay,
					eveHour: dateToStringHour(dateHour),
					eveHourEnd: dateToStringHour(dateHourEnd),
					eveName: eventAux.eveName,
					eveClient: eventAux.eveClient, 
					eveDesc: descrip,
					eveCalendarName: eventAux.eveCalendarName,
					eveLocalTask: eventAux.eveLocalTask,
					eveICS: eventAux.eveICS,
					eveConsumed: consumed,
					eveRejected: rejected,
					enabled: true,
					texts: textsTemplates
				#console.log "event", event
				if dateDay!=dateDayAnt
					view = new __View.EventListDayView model:event
					view.append event
				if dateDay==dateDaySelect
					if (eventAux.eveClient)
						if operatorRead 
							view = new __View.EventListReadView model:event
						else	
							view = new __View.EventListView model:event
					else	
						view = new __View.EventListSPView model:event
					view.append event
				dateDayAnt = dateDay
		
		oneDay = 1000 * 60 * 60 * 24;
		dayFirstWeek.setTime(dayFirstWeek.getTime() + (6*oneDay))
		textsTemplates = {displayFrom:findLangTextElement("label.template.displayFrom"),pressDisplayMore:findLangTextElement("label.template.pressDisplayMore")}
		eventMov = new __Model.Event
			eveDay: dateToStringFormat(dayFirstWeek),
			texts: textsTemplates
		view = new __View.EventListUntilView model:eventMov
		view.append eventMov
			
		
	onPlaces: (event) -> 
		#console.log "onPlaces"
		__FacadeCore.Router_section "calendarDiarySelect"		
			
						
__Controller.EventList = new EventListCtrl "section#booking"