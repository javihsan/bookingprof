class __View.LocalTaskCombiView extends Monocle.View
    
	container: "section#newLocalTask article#localTaskCombi-form ul"
	
	template_url: "/app/templates/localTaskCombi.mustache"
	
	events:
        "singleTap button": "onRemove"
        
    onRemove: (event) ->
    	#console.log "onRemove", @model
    	@remove()
  	  	
	
	 