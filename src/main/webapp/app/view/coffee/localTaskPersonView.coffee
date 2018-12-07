class __View.LocalTaskPersonView extends Monocle.View
    
    container: "section#taskSelectPerson article#taskPerson-form ul"
    
    template_url: "/app/templates/localTaskPerson.mustache"
   
    events:
        "singleTap li": "onSelect"
    
    onSelect: (event) ->
    	#console.log "onSelect",  @model
    	#@model.updateAttributes enabled: if @model.enabled then false else true
    	liTarget = $$ event.currentTarget
    	if liTarget.hasClass ("cancel")
    		liTarget.removeClass("cancel")
    		liTarget.addClass("accept")
    		liTarget.addClass("theme")
    		__Controller.TaskSelectPerson.selectCount++
    	else
    		liTarget.removeClass("accept")
    		liTarget.removeClass("theme")
    		liTarget.addClass("cancel")
    		__Controller.TaskSelectPerson.selectCount--
    	FacadeCore.prototype.count "a[data-action=reset]", __Controller.TaskSelectPerson.selectCount
    	#@refresh()
   	