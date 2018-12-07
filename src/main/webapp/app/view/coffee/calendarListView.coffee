class __View.CalendarListView extends Monocle.View
    
    container: "section#booking article#list-calendar ul"
    
    template_url: "/app/templates/calendarList.mustache"
   
    events:
        "singleTap li": "onSelect"
        "doubleTap li": "onEnabled"
    
    onSelect: (event) ->
    	if @model.enabled
    		#console.log "onSelect", @model
    	
    		__FacadeCore.Cache_remove appName + "calendar"
    		__FacadeCore.Cache_set appName + "calendar", @model
    	
    		__FacadeCore.Router_section "newCalendar"
    
    onEnabled: (event) ->
    	#console.log "onEnabled", @model
    	if __FacadeCore.isDoubleTap event
	    	_this = this
	    	dataAccept = {icon: 'checkmark', label: 'Accept', callback: ()-> _this.onEnabledConfirm(event) }
	    	dataCancel = {icon: 'checkmark', label: 'Cancel', callback: ()-> {}}
	    	dataConfirm = {icon: 'user', title: findLangTextElement("label.notification.placeEnabled.title"), description: findLangTextElement("label.notification.placeEnabled.text"), accept: dataAccept, cancel: dataCancel}
	    	Lungo.Notification.confirm dataConfirm
    
    onEnabledConfirm: (event) ->
    	#console.log "onEnabledConfirm", @model
    	url = "http://"+appHost+"/calendar/manager/enabled"
	   	data = {id:@model.calId}
    	$$.put url, data, () ->
	   		__Controller.CalendarList.loadCalendar event	 	
				   