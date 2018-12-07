class __View.LocalSelectView extends Monocle.View
    
    container: "section#booking article#list-local ul"
    
    template_url: "/app/templates/localSelect.mustache"
   
    events:
        "singleTap li": "selectLocal"

    selectLocal: (event) ->
    	#console.log "selectLocal", @model
    	local = __FacadeCore.Cache_get appName+"local"
    	if !local || (local && local.id != @model.locId)
       		__FacadeCore.Storage_set appName+"localId", null
       		__FacadeCore.Storage_set appName+"localId", @model.locId
       	        	       		
       		localReady()