class BookingHoursCtrl extends Monocle.Controller

	events:
		"load section#booking-hours" : "loadHours"
		"singleTap a[data-action=today]" : "onToday"
		"singleTap article#hours td" : "onSelectDay"
		"doubleTap article#hours td" : "onCloseDay"
		#"singleTap a[data-action=festive]" : "loadFestive"
		"singleTap a[data-action=return]" : "onReturn"
		
	elements:
		"#hours": "tableHours"
		#"a[data-action=festive]" : "festive"
		
	loadHours: (event) -> 
		#console.log "loadHours"
		
		#calendar = __FacadeCore.Cache_get appName + "calendar"
		#if calendar
			#@festive.hide()
		#else	
			#@festive.show()
			
		selectedDate = __FacadeCore.Cache_get appName+"selectedDate"
		a = selectedDate.split('-')
		newDayAux = new Date(a[0],(a[1]-1),a[2])
		this.createCalendar(newDayAux)
			
	
	createCalendar: (newDayAux) -> 
		#console.log "createCalendar Hours", newDayAux

		local = __FacadeCore.Cache_get appName + "local"
	
		weekDaysClosed = local.locSemanalDiary.closedDiary
		options = {date:newDayAux}
		calendar = __FacadeCore.Cache_get appName + "calendar"
		if calendar
			options.calendar = calendar.calId
			weekDaysClosed = calendar.calSemanalDiary.closedDiary
		create(@tableHours, weekDaysClosed, options)
		
		
	onToday: (event) -> 
		#console.log "onToday Hours"
	
		newDayAux = new Date()
		this.createCalendar(newDayAux)
	
	loadFestive: (event) -> 
		#console.log "loadFestive"
		
		elementFestive = @festive.html()
		
		Lungo.Element.loading "a[data-action=festive]", "black"

		local = __FacadeCore.Cache_get appName+"local"

		selectedDate = __FacadeCore.Cache_get appName+"selectedDate"
			
		url = "http://"+appHost+"/annual/manager/closeGoogleLocal"
		data = {id:local.id,selectedDate:selectedDate.toString()}
		_this = this
		$$.put url, data, (response) ->
			_this.festive.html elementFestive
			_this.loadHours event
			Lungo.Notification.success findLangTextElement("label.notification.closeDateHolidays.title"), findLangTextElement("label.notification.closeDateHolidays.text"), null, 3
			
	
	onSelectDay: (event) ->
		#console.log "onSelectDay"
		closed = $$(event.target).hasClass("date_closed")
		if !closed
			selectedDateHours = $$(event.target).attr("datetime")
			__FacadeCore.Cache_remove appName + "selectedDateHours"
			__FacadeCore.Cache_set appName + "selectedDateHours", selectedDateHours
			__FacadeCore.Router_section "newHours"
	
	onCloseDay: (event) ->
		#console.log "onCloseDay"
		if __FacadeCore.isDoubleTap event
			selectedDate = $$(event.target).attr("datetime")
			closed = $$(event.target).hasClass("date_closed")
			firm = __FacadeCore.Cache_get appName + "firm"
			if !closed
				calendar = __FacadeCore.Cache_get appName + "calendar"
				if calendar
					if firm.firConfig.configLocal.configLocRepeat
						url = "http://"+appHost+"/event/operator/listCalendarByDayRepeat"
					else
						url = "http://"+appHost+"/event/operator/listCalendarByDay"
					data = {id:calendar.calId,selectedDate:selectedDate.toString()}
				else
					if firm.firConfig.configLocal.configLocRepeat
						url = "http://"+appHost+"/event/operator/listByDayRepeat"
					else
						url = "http://"+appHost+"/event/operator/listByDay"	
					local = __FacadeCore.Cache_get appName + "local"
					data = {localId:local.id,selectedDate:selectedDate.toString()}
				_this = this
				$$.json url, data, (response) -> 
						_this.closeDate response,event,selectedDate
			else
				this.closeDateConfirm event,selectedDate			
		
	closeDate: (response,event,selectedDate) -> 
		#console.log "closeDate"
		if response && response.length>0
			_this = this
			dataAccept = {icon: 'checkmark', label: 'Accept', callback: ()-> _this.closeDateConfirm(event,selectedDate) }
			dataCancel = {icon: 'checkmark', label: 'Cancel', callback: ()-> {}}
			dataConfirm = {icon: 'user', title: findLangTextElement("label.notification.closeDateWithApo.title"), description: findLangTextElement("label.notification.closeDateWithApo.text"), accept: dataAccept, cancel: dataCancel}
			Lungo.Notification.confirm dataConfirm
		else
			this.closeDateConfirm(event,selectedDate)

	closeDateConfirm: (event,selectedDate) ->
    	#console.log "closeDateConfirm"
    	
    	calendar = __FacadeCore.Cache_get appName + "calendar"
    	if calendar
	    	url = "http://"+appHost+"/annual/manager/closeCalendar"
	    	data = {id:calendar.calId,selectedDate:selectedDate}
    	else
	    	url = "http://"+appHost+"/annual/manager/closeLocal"
	    	local = __FacadeCore.Cache_get appName+"local"
	    	data = {localId:local.id,selectedDate:selectedDate}
    	_this = this
    	$$.put url, data, () ->
    		_this.loadHours event
    		Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 2

    
    onReturn: (event) ->
    	#console.log "onReturn"
    	calendar = __FacadeCore.Cache_get appName + "calendar"
    	if calendar
    		__FacadeCore.Router_section "newCalendar"
    	else
    		__FacadeCore.Router_article "booking", "local-hours"
    	
__Controller.BookingHours = new BookingHoursCtrl "section#booking-hours"