class __View.EventListView extends Monocle.View
    
	container: "section#booking article#list-events ul"
	
	template_url: "/app/templates/eventsList.mustache"
	
	elements:
		"div:first-child": "divConsumed"
	
	events:
        "singleTap li": "loadClient"
        "doubleTap li": "consumEvent"
        "swiping li": "onSwiping"

 	loadClient: (event) ->
    	#console.log "loadClient", @model
    	if __FacadeCore.isSwipeLeft event, true
    		this.removeEvent event
    	else	
	    	__FacadeCore.Cache_remove appName + "client"
	    	client = new __Model.Client
	    		enabled: true,
	    		cliId: @model.eveClient.id,
	    		cliName: @model.eveClient.whoName,
	    		cliSurname: @model.eveClient.whoSurname,
	    		cliEmail: @model.eveClient.whoEmail,
	    		cliGender: @model.eveClient.whoGender,
	    		cliBirthday: @model.eveClient.whoBirthday,
	    		cliTelf1: @model.eveClient.whoTelf1,
	    		cliTelf2: @model.eveClient.whoTelf2,
	    		cliDesc: @model.eveClient.whoDesc,
	    		router: "list-events"
	    	
	    	__FacadeCore.Cache_set appName + "client", client
	    	__FacadeCore.Router_section "newClient"	


 	onSwiping: (event) ->
    	#console.log "onSwiping"
    	event.preventDefault()
    	if __FacadeCore.isSwipeLeft event
    		this.removeEvent event
 	
 	removeEvent: (event) ->
    	#console.log "removeEvent", @model
    	__FacadeCore.Cache_remove appName + "eventCancel"
    	__FacadeCore.Cache_set appName + "eventCancel", @model
    	__FacadeCore.Router_section "cancelEvent"

    consumEvent: (event) ->
    	#console.log "consumEvent", @model
    	if __FacadeCore.isDoubleTap event
    		if appFirmDomain == 'adveo'
    			if !@model.eveConsumed && !@model.eveRejected
    				__FacadeCore.Cache_remove appName + "eventFin"
    				__FacadeCore.Cache_set appName + "eventFin", @model
    				__FacadeCore.Router_section "finEvent"
	    		else
	    			_this = this
	    			dataAccept = {icon: 'checkmark', label: 'Accept', callback: ()-> _this.consumEventConfirm(event) }
		    		dataCancel = {icon: 'checkmark', label: 'Cancel', callback: ()-> {}}
	    			dataConfirm = {icon: 'user', title: findLangTextElement("label.notification.openApo.title"), description: findLangTextElement("label.notification.openApo.text"), accept: dataAccept, cancel: dataCancel}
	    			Lungo.Notification.confirm dataConfirm
	    	else
	    		firm = __FacadeCore.Cache_get appName + "firm"
	    		if firm.firBilledModule==1
	    			if !@model.eveConsumed
	    				__FacadeCore.Cache_remove appName + "eventFin"
	    				__FacadeCore.Cache_set appName + "eventFin", @model
	    				__FacadeCore.Router_section "newInvoice"
		    	else
		    		_this = this
		    		dataAccept = {icon: 'checkmark', label: 'Accept', callback: ()-> _this.consumEventConfirm(event) }
		    		dataCancel = {icon: 'checkmark', label: 'Cancel', callback: ()-> {}}
		    		if !@model.eveConsumed
		    			dataConfirm = {icon: 'user', title: findLangTextElement("label.notification.closeApo.title"), description: findLangTextElement("label.notification.closeApo.text"), accept: dataAccept, cancel: dataCancel}
		    		else
		    			dataConfirm = {icon: 'user', title: findLangTextElement("label.notification.openApo.title"), description: findLangTextElement("label.notification.openApo.text"), accept: dataAccept, cancel: dataCancel}
		    		Lungo.Notification.confirm dataConfirm   
	
	consumEventConfirm: (event) ->
    	#console.log "consumEventConfirm"
    	if appFirmDomain == 'adveo'
   			url = "http://"+appHost+"/event/operator/consumeComent"
   			data = {ICS:@model.eveICS, sel:-1, text:''}
   		else	
   			url = "http://"+appHost+"/event/operator/consume"
   			data = {ICS:@model.eveICS}
   		_this = this
   		$$.put url, data, (response) ->
   			_this.showConsumEvent response
	
	showConsumEvent: (response) -> 
		#console.log "showConsumEvent"
		consumed = false
		if response>0
			consumed = true
		# Obligamos a refrescar la lista 
		__Controller.EventList.setResult null
		__Controller.EventList.loadListEvents()
	  