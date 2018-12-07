class __View.LocalTaskListView extends Monocle.View
    
    container: "section#booking article#local-tasks ul"
    
    template_url: "/app/templates/localTaskList.mustache"
    
    events:
        "singleTap li": "loadLocalTask"
        "doubleTap li": "defaultLocalTask"
        "swiping li": "onSwiping"
    
    loadLocalTask: (event) ->
    	#console.log "loadLocalTask", @model
    	if __FacadeCore.isSwipeLeft event, true
    		this.onRemove event
    	else
	    	__FacadeCore.Cache_remove appName + "localTaskNew"
	    	__FacadeCore.Cache_set appName + "localTaskNew", @model
	    	if @model.lotTaskCombiRes && @model.lotTaskCombiRes.length>0
	    		__FacadeCore.Router_article "newLocalTask", "localTaskCombi-form"
	    	else
	    		__FacadeCore.Router_article "newLocalTask", "localTask-form"
   
    onSwiping: (event) ->
    	#console.log "onSwiping"
    	event.preventDefault()
    	if __FacadeCore.isSwipeLeft event
    		this.onRemove event
    
    onRemove: (event) ->
    	#console.log "onRemove", @model
    	if !@model.lotDefault
	    	_this = this
		   	dataAccept = {icon: 'checkmark', label: 'Accept', callback: ()-> _this.removeConfirm(event) }
		    dataCancel = {icon: 'checkmark', label: 'Cancel', callback: ()-> {}}
		    dataConfirm = {icon: 'user', title: findLangTextElement("label.notification.deleteLocalTask.title"), description: findLangTextElement("label.notification.deleteLocalTask.text"), accept: dataAccept, cancel: dataCancel}
		    Lungo.Notification.confirm dataConfirm
   
   	removeConfirm: (event) ->
    	#console.log "removeConfirm", @model
    	url = "http://"+appHost+"/localTask/manager/remove"
    	data = {localId:@model.lotLocalId, id:@model.lotId}
    	_this = this
    	$$.put url, data, () ->
    			Lungo.Notification.success findLangTextElement("label.notification.deletedLocalTask.title"), findLangTextElement("label.notification.deletedLocalTask.text"), null, 3
    			_this.remove() 		
  	 
    defaultLocalTask: (event) ->
    	if __FacadeCore.isDoubleTap event
	    	#console.log "defaultLocalTask", @model
	    	if !@model.lotDefault && (@model.lotTaskDuration>0 || (@model.lotTaskCombiId && @model.lotTaskCombiId.length>0))
		    	url = "http://"+appHost+"/local/manager/defaultLocalTask"
		   		data = {localId:@model.lotLocalId, idLocalTask:@model.lotId}
		   		_this = @model
		   		$$.put url, data, () ->
		   			local = __FacadeCore.Cache_get appName + "local"
		   			local.locTaskDefaultId = _this.lotId
		   			__FacadeCore.Cache_remove appName + "local"
		   			__FacadeCore.Cache_set appName + "local", local
		   			__Controller.LocalTaskList.loadListLocalTask event
	
    