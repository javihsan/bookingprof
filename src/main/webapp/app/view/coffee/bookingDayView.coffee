class __View.BookingDayView extends Monocle.View
    
	container: "section#booking article#table-day div#table-day-container"
	
	template_url: "/app/templates/bookingDay.mustache"
	
	events:
        "singleTap div": "onTap"
        
    onTap: (event) ->
    	#console.log "onTap", @model
    	__FacadeCore.Cache_remove appName+"newApo"
    	__FacadeCore.Cache_set appName+"newApo", @model
    	
    	if @model.apoCalendarId!=null
	    	selectedCalendars = new Array()
	    	calendarSel = {id:@model.apoCalendarId,name:@model.apoCalendarName}
	    	selectedCalendars[0] = calendarSel
	    	__FacadeCore.Cache_remove appName+"selectedCalendars"
	    	__FacadeCore.Cache_set appName+"selectedCalendars", selectedCalendars
    	
    	__FacadeCore.Router_section "#newEvent"
    	
    	
    
      