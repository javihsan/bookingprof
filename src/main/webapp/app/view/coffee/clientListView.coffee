class __View.ClientListView extends Monocle.View
    
	container: "section#booking article#list-clients ul"
	
	template_url: "/app/templates/clientList.mustache"
	
	events:
        "singleTap li": "selectClient"
        "swiping li": "onSwiping"
    
   	selectClient: (event) ->
    	#console.log "List.selectClient", @model
    	if __FacadeCore.isSwipeLeft event, true
    		this.onRemove event
    	else
	    	@model.router = "list-clients"
	    	__FacadeCore.Cache_remove appName + "client"
	    	__FacadeCore.Cache_set appName + "client", @model
	    	
	    	__FacadeCore.Router_section "newClient"    
 	    		
    
   	onSwiping: (event) ->
    	#console.log "onSwiping"
    	event.preventDefault()
    	if __FacadeCore.isSwipeLeft event
    		this.onRemove event
    
    onRemove: (event) ->
    	#console.log "onRemove", @model
    	_this = this
	   	dataAccept = {icon: 'checkmark', label: 'Accept', callback: ()-> _this.removeConfirm(event) }
	    dataCancel = {icon: 'checkmark', label: 'Cancel', callback: ()-> {}}
	    dataConfirm = {icon: 'user', title: findLangTextElement("label.notification.deleteClient.title"), description: findLangTextElement("label.notification.deleteClient.text"), accept: dataAccept, cancel: dataCancel}
	    Lungo.Notification.confirm dataConfirm
   
   	removeConfirm: (event) ->
    	#console.log "removeConfirm", @model
    	url = "http://"+appHost+"/client/operator/remove"
    	data = {id:@model.cliId}
    	_this = this
    	$$.put url, data, () ->
    			Lungo.Notification.success findLangTextElement("label.notification.deletedClient.title"), findLangTextElement("label.notification.deletedClient.text"), null, 3
    			cli = Lungo.Core.findByProperty __Controller.ClientList.result, "id", _this.model.cliId
    			index = __Controller.ClientList.result.indexOf cli
    			if index > -1
    				__Controller.ClientList.result.splice index, 1
    			_this.remove() 
	
  