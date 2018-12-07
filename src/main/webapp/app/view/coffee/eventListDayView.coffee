class __View.EventListDayView extends Monocle.View
    
	container: "section#booking article#list-events ul"
	
	template_url: "/app/templates/eventsListDay.mustache"
	
	events:
        "singleTap li": "onTap"
        
    onTap: (event) ->
    	#console.log "EventListDayView", @model
    	
    	selectedDate = dateToString(new Date(@model.eveStartTime))
    	
    	__FacadeCore.Cache_remove appName+"selectedDateDiary"
    	__FacadeCore.Cache_set appName+"selectedDateDiary", selectedDate
    	
    	__Controller.EventList.loadListEventsWeek(event)
	
	 