class __View.EventListReadView extends Monocle.View
    
	container: "section#booking article#list-events ul"
	
	template_url: "/app/templates/eventsList.mustache"
	
	elements:
		"div:first-child": "divConsumed"
	
	events:
        "doubleTap li": "consumEvent"

    consumEvent: (event) ->
    	#console.log "consumEvent", @model
    	view = new __View.EventListView model:@model
    	view.consumEvent event
    	
	  