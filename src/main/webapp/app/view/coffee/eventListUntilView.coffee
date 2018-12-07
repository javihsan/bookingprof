class __View.EventListUntilView extends Monocle.View
    
	container: "section#booking article#list-events ul"
	
	template_url: "/app/templates/eventsListUntil.mustache"
	
	
	events:
        "singleTap li": "onTap"
        
    onTap: (event) ->
    	#console.log "EventListUntilView", @model
    	__Controller.EventList.onNext(event) 