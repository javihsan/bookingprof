class __View.LocalTaskCalendarView extends Monocle.View
    
    container: "section#localTaskCalendar article#localTaskCalendar-form ul"
    
    template_url: "/app/templates/localTaskCalendar.mustache"
   
    events:
        "singleTap li": "onSelect"
    
    onSelect: (event) ->
    	#console.log "onSelect", @model
    	@model.updateAttributes enabled: if @model.enabled then false else true
    	@refresh()
   			   