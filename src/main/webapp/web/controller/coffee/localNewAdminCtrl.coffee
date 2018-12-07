class LocalNewCtrl extends Monocle.Controller

	events:
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
			
	elements:
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"#locName" : "locName"
		"#locAddress" : "locAddress"
		"#locCity" : "locCity"
		"#locState" : "locState"
		"#locCP" : "locCP"
		"#locResponName" : "locResponName"
		"#locResponSurname" : "locResponSurname"
		"#locResponEmail" : "locResponEmail"
		"#locResponTelf1" : "locResponTelf1"
		"#locApoDuration" : "locApoDuration"
		"#locTimeRestricted" : "locTimeRestricted"
		"#locOpenDays" : "locOpenDays"
		"#locNumPersonsApo" : "locNumPersonsApo"
		"#locNumUsuDays" : "locNumUsuDays"
		"#locNewClientDefault" : "locNewClientDefault"
		"#locMulServices" : "locMulServices"
		"#locCacheTasks" : "locCacheTasks"
		"#locSelCalendar" : "locSelCalendar"
		
	onSave: (event) -> 
		#console.log "onSave"
		__FacadeCore.Cache_remove appName + "elementSave"
		__FacadeCore.Cache_remove appName + "elementCancel"
		__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
		__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
		Lungo.Element.loading @buttonSave.selector, "black"
		Lungo.Element.loading @buttonCancel.selector, "black"
		url = "http://"+appHost+"/local/admin/new"
		data = {domain:AppAdmin.firmDomain,locName: @locName.val(),locAddress:@locAddress.val(),locCity:@locCity.val(),locState:@locState.val(),locCP:@locCP.val(),locApoDuration:@locApoDuration.val(),locTimeRestricted: @locTimeRestricted.val(),locOpenDays: @locOpenDays.val(),locNumPersonsApo: @locNumPersonsApo.val(),locNumUsuDays: @locNumUsuDays.val(), locNewClientDefault:@locNewClientDefault.val(),locResponName: @locResponName.val(),locResponSurname: @locResponSurname.val(),locResponEmail: @locResponEmail.val(),locResponTelf1: @locResponTelf1.val(),locMulServices: @locMulServices.val(),locCacheTasks: @locCacheTasks.val(),locSelCalendar: @locSelCalendar.val()}
		_this = this
		$$.post url, data, (response) -> 
				#console.log "onSuccess", response
				Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, (response) ->
					__FacadeCore.Router_article "admin", "list-local"
					_this.resetArticle()
								
	
	onCancel: (event) -> 
		#console.log "cancel"	
		__FacadeCore.Cache_remove appName + "elementSave"
		__FacadeCore.Cache_remove appName + "elementCancel"
		__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
		__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
		Lungo.Element.loading @buttonSave.selector, "black"
		Lungo.Element.loading @buttonCancel.selector, "black"
		this.resetArticle()
		__FacadeCore.Router_back()
		
	resetArticle: () ->
		#console.log "resetArticle"	
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		@locName.val ""
		@locAddress.val ""
		@locCity.val  ""
		@locState.val  ""
		@locCP.val  ""
		@locResponName.val  ""
		@locResponSurname.val  ""
		@locResponEmail.val  ""
		@locResponTelf1.val  ""
		@locApoDuration.val  ""
		@locTimeRestricted.val  ""
		@locOpenDays.val  ""
		@locNumPersonsApo.val  ""
		@locNumUsuDays.val  ""
		@locNewClientDefault.val  ""
		@locMulServices.val  ""
		@locCacheTasks.val  ""
		@locSelCalendar.val  ""
		 
__Controller.LocalNew = new LocalNewCtrl "section#newLocal"
