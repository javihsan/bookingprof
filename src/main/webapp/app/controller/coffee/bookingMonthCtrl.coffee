class BookingMonthCtrl extends Monocle.Controller

	events:
		"load article#table-month" : "loadCalendarMonth"
		"singleTap a[data-action=today]" : "onToday"
		"singleTap article#table-month td" : "onSelectDay"

	elements:
		"#table-month": "tableMonth"
		"#daySelected": "daySelected"
		"a[data-action=today]" : "today"
		"header a[href=\\#]" : "header"
		"header a[href=\\#menu]" : "aside"
		"footer a" : "footer"
		"footer a[href=\\#list-invoices]" : "footerInvoice"
		"footer a[href=\\#table-day]" : "footerBook"
			
	loadCalendarMonth: (event) -> 
		#console.log "article#table-month"
		
		@header.hide()
		@aside.show()
		@today.show()
		@footer.show()
		
		firm = __FacadeCore.Cache_get appName + "firm"
	
		if firm.firBilledModule==0
			@footerInvoice.hide()
		
		if operatorRead
			@footerBook.hide()
			
		selectedDate = __FacadeCore.Cache_get appName+"selectedDate"
		a = selectedDate.split('-')
		newDayAux = new Date(a[0],(a[1]-1),a[2])
		this.createCalendar(newDayAux)

	createCalendar: (newDayAux) -> 
		#console.log "createCalendar Month: ",newDayAux

		local = __FacadeCore.Cache_get appName + "local"
	
		openDaysAux = local.locOpenDays
		weekDaysClosed = local.locSemanalDiary.closedDiary
			
		create(@tableMonth, weekDaysClosed, {date:newDayAux,openDays:openDaysAux})
			
	onToday: (event) -> 
		#console.log "a[data-action=today] onToday"
				
		if  (@tableMonth.hasClass("active"))
			newDayAux = new Date()
			this.createCalendar(newDayAux)
	
	onSelectDay: (event) ->
		#console.log "article#table-month onSelectDay"
		selectedDate = $$(event.target).attr("datetime")
		if operatorRead
			__FacadeCore.Cache_remove appName+"selectedDateDiary"
			__FacadeCore.Cache_set appName+"selectedDateDiary", new String(selectedDate)
			__FacadeCore.Router_article "booking","list-events"
		else if(!$$(event.target).hasClass('date_closed'))				
			if (adminOption || !$$(event.target).hasClass('date_not_enabled'))
				__FacadeCore.Cache_remove appName+"selectedDate"
				__FacadeCore.Cache_set appName+"selectedDate", new String(selectedDate)
				__FacadeCore.Router_article "booking","table-day"
						
__Controller.BookingMonth = new BookingMonthCtrl "section#booking"