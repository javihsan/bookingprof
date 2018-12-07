class __View.TaskSelectView extends Monocle.View
    
	container: "section#taskSelect article#task-form ul"
	
	template_url: "/app/templates/taskSelect.mustache"
	
	events:
        "singleTap li": "onTasksPer"
	
	
	onTasksPer: (event) -> 
		if @model.tasMultiple
			#console.log "onTasksPer", @model
					
			__FacadeCore.Cache_remove appName+"selectedTasksPerNum"
			__FacadeCore.Cache_set appName+"selectedTasksPerNum", @model.tasSelId	
					
			__FacadeCore.Router_article "taskSelectPerson","taskPerson-form"
	
