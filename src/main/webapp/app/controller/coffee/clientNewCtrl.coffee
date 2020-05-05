class ClientNewCtrl extends Monocle.Controller

	events:
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"load #newClient" : "loadNew"
		"unload #newClient" : "unloadNew"
		"change #cliBirthday" : "changeBirthday"
			
	elements:
		"#cliEmail" : "cliEmail"
		"#error_cliEmail" : "error_cliEmail"
		"#cliGender" : "cliGender"
		"#error_cliGender" : "error_cliGender"
		"#cliBirthday" : "cliBirthday"
		"#error_cliBirthday" : "error_cliBirthday"
		"#cliName" : "cliName"
		"#error_cliName" : "error_cliName"
		"#cliSurname" : "cliSurname"
		"#cliTelf1" : "cliTelf1"
		"#error_cliTelf1" : "error_cliTelf1"
		"#cliTelf2" : "cliTelf2"
		"#error_cliTelf2" : "error_cliTelf2"
		"#cliDesc" : "cliDesc"
		"ul:last-child" : "cliInvoices"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
	
	onSave: (event) -> 
		#console.log "onSave"
		if (this.validateForm())
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"
			client = __FacadeCore.Cache_get appName + "client"
			url = "http://"+appHost+"/client/operator/update" 
			data = {id:client.cliId,cliEmail: @cliEmail.val(), cliGender: @cliGender.val(), cliBirthday: @cliBirthday.val(), cliName: @cliName.val(), cliSurname: @cliSurname.val(), cliTelf1: @cliTelf1.val(), cliTelf2: @cliTelf2.val(), cliDesc: @cliDesc.val()}
			_this = this
			$$.put url, data, () -> 
					#console.log "onSuccess"
					Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, () ->
						#_this.resetArticle()
						__FacadeCore.Router_article "booking", client.router
						_this.resetArticle()


	validateForm: -> 
		#console.log "validateForm"
		result = true
		@error_cliEmail.html  ""
		@error_cliGender.html  ""
		@error_cliBirthday.html  ""
		@error_cliName.html  ""
		@error_cliTelf1.html  ""
		@error_cliTelf2.html  ""
		if (!@cliName[0].checkValidity())
			@error_cliName.html getMessageValidity(@cliName[0])
			@cliName[0].focus()
			result = false
		else if (!@cliEmail[0].checkValidity())
			@error_cliEmail.html getMessageValidity(@cliEmail[0])
			@cliEmail[0].focus()
			result = false
		else if (!checkValidityDate(@cliBirthday.val(),@cliBirthday.attr("required")))
			@error_cliBirthday.html getMessageValidity(@cliBirthday[0])
			@cliBirthday[0].focus()
			result = false
		#else if (@cliGender[0].options.selectedIndex == 0)
			#@error_cliGender.html getMessageValidity(@cliGender[0])
			#@cliGender[0].focus()
			#result = false	
		else if (!checkValidity(@cliTelf1.val(),@cliTelf1.attr("pattern"),@cliTelf1.attr("required")))
			@error_cliTelf1.html getMessageValidity(@cliTelf1[0])
			@cliTelf1[0].focus()
			result = false
		else if (!checkValidity(@cliTelf2.val(),@cliTelf2.attr("pattern"),@cliTelf2.attr("required")))
			@error_cliTelf2.html getMessageValidity(@cliTelf2[0])
			@cliTelf2[0].focus()
			result = false 
		result
	
	changeBirthday: (event) -> 
		#console.log "changeBirthday"
		@error_cliBirthday.html ""
		@cliBirthday.val formatDate(@cliBirthday.val())
			
	onCancel: (event) -> 
		#console.log "cancel"	
		__FacadeCore.Cache_remove appName + "elementSave"
		__FacadeCore.Cache_remove appName + "elementCancel"
		__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
		__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
		Lungo.Element.loading @buttonSave.selector, "black"
		Lungo.Element.loading @buttonCancel.selector, "black"
		__FacadeCore.Router_back()
		this.resetArticle()
		
	
	resetArticle: () -> 
		#console.log "resetArticle"
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		@cliName.val ""
		@cliEmail.val ""
		@cliGender[0].options.selectedIndex = 0
		@cliBirthday.val ""
		@cliSurname.val ""
		@cliTelf1.val ""
		@cliTelf2.val ""
		@error_cliEmail.html  ""
		@error_cliGender.html  ""
		@error_cliBirthday.html  ""
		@error_cliName.html  ""
		@error_cliTelf1.html  ""
		@error_cliTelf2.html  ""
		@cliDesc.val ""
		@cliInvoices.html ""
	
	unloadNew: (event) -> 
		#console.log "unloadNew"	
		__FacadeCore.Cache_remove appName + "client"
		
	loadNew: (event) -> 
		#console.log "loadNew"	
		client = __FacadeCore.Cache_get appName + "client"

		asyn = __FacadeCore.Service_Settings_asyncFalse()
	
		local = __FacadeCore.Cache_get appName + "local"
		data = {localId:local.id,id:client.cliId}
		url = "http://" + appHost + "/event/operator/listByClientAgo"
		listEvents = $$.json url, data
		
		__FacadeCore.Service_Settings_async(asyn)
		
		strTasks = ""
		listEvents = Lungo.Core.orderByProperty listEvents,"eveStartTime","desc"
		for event in listEvents
			date = new Date event.eveStartTime
			strDate = dateToStringFormat(date) + ", " + dateToStringHour(date)
			strTasks += "<li>"+strDate+" - "+event.eveLocalTask.lotName+"</li>"
			
		@cliEmail.val client.cliEmail
		if client.cliGender==null
			client.cliGender = -1
		@cliGender[0].options.selectedIndex = (client.cliGender + 1)
		cliBirthdayTime = ""
		if client.cliBirthday
			cliBirthdayDate = new Date client.cliBirthday
			cliBirthdayTime = dateToStringSim cliBirthdayDate,"-"
		@cliBirthday.val cliBirthdayTime
		@cliName.val client.cliName
		@cliSurname.val client.cliSurname
		@cliTelf1.val client.cliTelf1
		@cliTelf2.val client.cliTelf2
		@cliDesc.val client.cliDesc
		@cliInvoices.append strTasks
		
		 
__Controller.ClientNew = new ClientNewCtrl "section#newClient"
