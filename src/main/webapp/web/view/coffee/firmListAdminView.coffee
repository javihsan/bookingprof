class __View.FirmListAdminView extends Monocle.View
    
    container: "section#admin article#list-firm ul"
    
    template_url: "/web/templates/firmListAdmin.mustache"
   
    events:
        "singleTap li": "onSingleTap"
        "doubleTap li": "onDoubleTap"
        
    onSingleTap: (event) ->
    	#console.log "onSingleTap"
    	__FacadeCore.Cache_remove appName + "firmNew"
    	__FacadeCore.Cache_set appName + "firmNew", @model
    	AppAdmin.firmDomain = @model.firDomain
    	__FacadeCore.Router_section "newFirm"

    onDoubleTap: (event) ->
    	#console.log "onDoubleTap"
    	if __FacadeCore.isDoubleTap event
    		this.onEnabled event
    
    onEnabled: (event) ->
    	#console.log "onEnabled", @model
    	_this = this
    	dataAccept = {icon: 'checkmark', label: 'Accept', callback: ()-> _this.onEnabledConfirm(event) }
    	dataCancel = {icon: 'checkmark', label: 'Cancel', callback: ()-> {}}
    	dataConfirm = {icon: 'user', title: findLangTextElement("label.notification.enabledFirm.title"), description: findLangTextElement("label.notification.enabledFirm.text"), accept: dataAccept, cancel: dataCancel}
    	Lungo.Notification.confirm dataConfirm
    
    onEnabledConfirm: (event) ->
    	#console.log "onEnabledConfirm", @model
    	url = "http://"+appHost+"/firm/admin/enabled"
    	data = {id:@model.firId}
    	$$.put url, data, () ->
	   		__Controller.FirmListAdmin.loadFirm event
