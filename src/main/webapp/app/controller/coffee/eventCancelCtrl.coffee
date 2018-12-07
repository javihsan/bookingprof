class EventCancelCtrl extends Monocle.Controller
	
	events:
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"load #cancelEvent" : "loadCancel"
		"unload #cancelEvent" : "unloadCancel"
		"change #eveCancelSend" : "changeEveCancelSend"
		
	elements:
		"#eveCancelText" : "eveCancelText"
		"#eveCancelSend" : "eveCancelSend"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"#event_hour" : "eventHour"
		"#error_eveClientEmail" : "errorEveClientEmail"
	
	loadCancel: (event) -> 
		#console.log "loadCancel"
		@eveCancelSend.val "1"
		@eveCancelText.val ""
		@eveCancelText[0].disabled = false
		eventCancel = __FacadeCore.Cache_get appName + "eventCancel"
		mail = eventCancel.eveClient.whoEmail
		if !mail || mail.length==0
			@errorEveClientEmail.html findLangTextElement("event.cancelNotEmail")
			@eveCancelSend[0].options[1].selected = true
			@eveCancelSend[0].disabled = true
			@eveCancelText[0].disabled = true
		else
			@errorEveClientEmail.html ""
			@eveCancelSend[0].options[0].selected = true
			@eveCancelSend[0].disabled = false
			@eveCancelText[0].disabled = false
		dateHour = new Date(eventCancel.eveStartTime)
		hourApo = dateToStringFormat(dateHour)+" "+dateToStringHour(dateHour) + " - " + eventCancel.eveClient.whoName
		@eventHour.html hourApo
	
	changeEveCancelSend: (event) -> 
		#console.log "changeEveCancelSend"
		if @eveCancelSend.val()=="1"
			@eveCancelText[0].disabled = false
		else
			@eveCancelText[0].disabled = true
	
	onSave: (event) -> 
		#console.log "onSave"
		__FacadeCore.Cache_remove appName + "elementSave"
		__FacadeCore.Cache_remove appName + "elementCancel"
		__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
		__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
		Lungo.Element.loading @buttonSave.selector, "black"
		Lungo.Element.loading @buttonCancel.selector, "black"
		eventCancel = __FacadeCore.Cache_get appName + "eventCancel"
		url = "http://"+appHost+"/event/operator/cancel"
		local = __FacadeCore.Cache_get appName+"local"
		data = {localId:local.id,id:eventCancel.eveId, send:@eveCancelSend.val(), text:@eveCancelText.val()}
		_this = this
		$$.put url, data, () ->
    		Lungo.Notification.success findLangTextElement("label.notification.canceledApo.title"), findLangTextElement("label.notification.canceledApo.text"), null, 3,  (response) ->
    			__Controller.EventList.setResult null
    			__FacadeCore.Router_article "booking", "list-events"
	
	onCancel: (event) -> 
		#console.log "cancel"
		__FacadeCore.Cache_remove appName + "elementSave"
		__FacadeCore.Cache_remove appName + "elementCancel"
		__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
		__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
		Lungo.Element.loading @buttonSave.selector, "black"
		Lungo.Element.loading @buttonCancel.selector, "black"
		__FacadeCore.Router_back()
	
	resetArticle: () -> 
		#console.log "resetArticle"
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		@eveCancelText.val ""
		
	unloadCancel: (event) -> 
		#console.log "unloadCancel"
		this.resetArticle()
		__FacadeCore.Cache_remove appName+"eventCancel"
		
				
			 
__Controller.EventCancel = new EventCancelCtrl "section#cancelEvent"
