class __View.LocalListAdminView extends Monocle.View
    
    container: "section#admin article#list-local ul"
    
    template_url: "/web/templates/localListAdmin.mustache"
   
    events:
        #"singleTap li": "onSingleTap"
        "doubleTap li": "onDoubleTap"
        
    onSingleTap: (event) ->
    	#console.log "onSingleTap"
    	__FacadeCore.Cache_remove appName + "localNew"
    	__FacadeCore.Cache_set appName + "localNew", @model
    	__FacadeCore.Router_section "newLocal"
    
    onDoubleTap: (event) ->
    	#console.log "onDoubleTap"
    	if __FacadeCore.isDoubleTap event
    		this.onEnabled event
    
    
    onEnabled: (event) ->
    	#console.log "onEnabled", @model
    	_this = this
    	dataAccept = {icon: 'checkmark', label: 'Accept', callback: ()-> _this.onEnabledConfirm(event) }
    	dataCancel = {icon: 'checkmark', label: 'Cancel', callback: ()-> {}}
    	dataConfirm = {icon: 'user', title: findLangTextElement("label.notification.enabledLocal.title"), description: findLangTextElement("label.notification.enabledLocal.text"), accept: dataAccept, cancel: dataCancel}
    	Lungo.Notification.confirm dataConfirm
    
    onEnabledConfirm: (event) ->
    	#console.log "onEnabledConfirm", @model
    	url = "http://"+appHost+"/local/admin/enabled"
    	data = {id:@model.locId}
    	$$.put url, data, () ->
	   		__Controller.LocalListAdmin.loadLocal event
  