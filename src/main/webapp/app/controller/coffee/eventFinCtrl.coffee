class EventFinCtrl extends Monocle.Controller
	
	events:
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"load #finEvent" : "loadFin"
		"unload #finEvent" : "unloadFin"
		
	elements:
		"#eveFinText" : "eveFinText"
		"#eveFinSel" : "eveFinSel"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"#event_hour" : "eventHour"
	
	loadFin: (event) -> 
		#console.log "loadFin"
		@eveFinSel.val "1"
		@eveFinText.val ""
		@eveFinText[0].disabled = false
		eventFin = __FacadeCore.Cache_get appName + "eventFin"
		@eveFinSel[0].options[0].selected = true
		dateHour = new Date(eventFin.eveStartTime)
		hourApo = dateToStringFormat(dateHour)+" "+dateToStringHour(dateHour) + " - " + eventFin.eveClient.whoName
		@eventHour.html hourApo
		
	onSave: (event) -> 
		#console.log "onSave"
		__FacadeCore.Cache_remove appName + "elementSave"
		__FacadeCore.Cache_remove appName + "elementCancel"
		__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
		__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
		Lungo.Element.loading @buttonSave.selector, "black"
		Lungo.Element.loading @buttonCancel.selector, "black"
		eventFin = __FacadeCore.Cache_get appName + "eventFin"
		url = "http://"+appHost+"/event/operator/consumeComent"
		data = {ICS:eventFin.eveICS, sel:@eveFinSel.val(), text:@eveFinText.val()}
		_this = this
		$$.put url, data, () ->
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
		@eveFinText.val ""
		
	unloadFin: (event) -> 
		#console.log "unloadFin"
		this.resetArticle()
		__FacadeCore.Cache_remove appName+"eventFin"
		
				
			 
__Controller.EventFin = new EventFinCtrl "section#finEvent"
