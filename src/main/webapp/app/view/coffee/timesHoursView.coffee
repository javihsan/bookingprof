class __View.TimesHoursView extends Monocle.View
    
	container: "section#newHours article#hours-form ul"
	
	template_url: "/app/templates/timesHours.mustache"
	
	events:
        "singleTap button": "onRemove"
        
    onRemove: (event) ->
    	#console.log "onRemove", @model
    	@remove()
  	  	
	
	 