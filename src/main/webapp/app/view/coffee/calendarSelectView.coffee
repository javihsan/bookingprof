class __View.CalendarSelectView extends Monocle.View
    
    container: "section#calendarDiarySelect article#calendarDiary-form ul"
    
    template_url: "/app/templates/calendarSelect.mustache"
   
    events:
        "singleTap li": "onSelect"
    
    onSelect: (event) ->
    	#console.log "onSelect", @model
    	@model.updateAttributes enabled: if @model.enabled then false else true
    	@refresh()
   			   