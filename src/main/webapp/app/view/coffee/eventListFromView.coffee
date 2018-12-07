class __View.EventListFromView extends Monocle.View
    
	container: "section#booking article#list-events ul"
	
	template_url: "/app/templates/eventsListFrom.mustache"
	
	events:
        "singleTap li": "onTap"
        
    onTap: (event) ->
    	#console.log "EventListFromView", @model
    	__Controller.EventList.onPrevius(event)
	
	 