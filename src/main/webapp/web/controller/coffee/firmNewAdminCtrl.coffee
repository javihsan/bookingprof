class FirmNewCtrl extends Monocle.Controller

	events:
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"load #newFirm" : "loadFirm"
		"unload #newFirm" : "unloadFirm"
		"change #firDomain" : "changeFirDomain"
			
	elements:
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"#firName" : "firName"
		"#firDomain" : "firDomain"
		"#firServer" : "firServer"
		"#firAddress" : "firAddress"
		"#firCity" : "firCity"
		"#firState" : "firState"
		"#firCP" : "firCP"
		"#firCountry" : "firCountry"
		"#firResponName" : "firResponName"
		"#firResponSurname" : "firResponSurname"
		"#firResponEmail" : "firResponEmail"
		"#firResponTelf1" : "firResponTelf1"
		"#firBilledModule" : "firBilledModule",
		"#firConfig" : "firConfig",
		"#firClassTasks" : "firClassTasks",
		"#firGwtUsers" : "firGwtUsers"
	
	changeFirDomain: (event) ->
		#console.log "changeFirDomain"
		firm = __FacadeCore.Cache_get appName+"firmNew"
		firDom = ""
		if firm
			firDom = firm.firDomain
		if (@firDomain.val().length>0 && (firDom.length==0 || (firDom.length>0 && firDom != @firDomain.val())))
			asyn = __FacadeCore.Service_Settings_asyncFalse()
	
			url = "http://" + appHost + "/firm/admin/get"
			data = {domain: @firDomain.val()}
			firmDom = $$.json url, data
			
			__FacadeCore.Service_Settings_async(asyn)
			
			if firmDom
				@firDomain[0].focus()
				Lungo.Notification.success findLangTextElement("label.notification.existsDomain.title"), findLangTextElement("label.notification.existsDomain.text"), null, 3
				return false
		return true
	
	loadFirm: (event) -> 
		#console.log "loadFirm"
		
		firm = __FacadeCore.Cache_get appName+"firmNew"
		#console.log "firm",firm
		
		asyn = __FacadeCore.Service_Settings_asyncFalse()
				
		url = "http://" + appHost + "/taskClass/operator/list"
		tasksClass = $$.json url, null
		tasksClass = Lungo.Core.orderByProperty tasksClass, "tclName", "asc"
		
		__FacadeCore.Service_Settings_async(asyn)			
		
		objSelectMul = Lungo.dom "#firm-form #firClassTasks"
		i=-1
		for taskClass in tasksClass
			i++;
			objSelectMul[0].options[i] = new Option taskClass.tclName , taskClass.id
		
		if firm
			@firName.val firm.firName
			@firDomain.val firm.firDomain
			@firServer.val firm.firServer
			@firAddress.val firm.firAddress
			@firCity.val firm.firCity
			@firState.val firm.firState
			@firCP.val firm.firCP
			@firCountry.val firm.firCountry
			@firResponName.val firm.firResponName
			@firResponSurname.val firm.firResponSurname
			@firResponEmail.val firm.firResponEmail
			@firResponTelf1.val firm.firResponTelf1
			@firBilledModule[0].options.selectedIndex = firm.firBilledModule
			@firConfig.val firm.firConfig
			@firGwtUsers.val firm.firGwtUsers.toString()
			strTaskClass = firm.firClassTasks.toString()
			a = strTaskClass.split(",")
			for option in objSelectMul[0].options
				if (a.indexOf(option.value)) != -1
					option.selected = true
		else		
			@firGwtUsers[0].disabled = true

	onSave: (event) -> 
		#console.log "onSave"
		if (this.changeFirDomain(event))
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"
			
			url = "http://"+appHost+"/firm/admin/new"
			data = {firName: @firName.val(),firDomain: @firDomain.val(),firServer: @firServer.val(),firAddress: @firAddress.val(),firCity: @firCity.val(),firState: @firState.val(),firCP: @firCP.val(),firCountry: @firCountry.val(),firResponName: @firResponName.val(),firResponSurname: @firResponSurname.val(),firResponEmail: @firResponEmail.val(),firResponTelf1: @firResponTelf1.val(),firBilledModule: @firBilledModule.val(),firConfig: @firConfig.val(),firGwtUsers: @firGwtUsers.val()}
			objSelectMul = Lungo.dom "#firm-form #firClassTasks"
			strFirClass = ""
			i=0
			for option in objSelectMul[0].options
				if option.selected
					if i>0
						strFirClass += ","
					strFirClass += 	option.value
					i++
			data.firClassTasks = strFirClass
			firm = __FacadeCore.Cache_get appName+"firmNew"
			if firm
		    	data.id = firm.firId
			_this = this
			$$.post url, data, (response) -> 
					#console.log "onSuccess", response
					Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, (response) ->
						__FacadeCore.Router_article "admin", "list-firm"
						AppAdmin.firmDomain = _this.firDomain.val()
						_this.resetArticle()
			
	
	onCancel: (event) -> 
		#console.log "cancel"	
		__FacadeCore.Cache_remove appName + "elementSave"
		__FacadeCore.Cache_remove appName + "elementCancel"
		__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
		__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
		Lungo.Element.loading @buttonSave.selector, "black"
		Lungo.Element.loading @buttonCancel.selector, "black"
		__FacadeCore.Router_article "admin", "list-firm"
		this.resetArticle()
		
	resetArticle: () ->
		#console.log "resetArticle"	
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		@firName.val ""
		@firDomain.val ""
		@firServer.val ""
		@firAddress.val ""
		@firCity.val ""
		@firState.val ""
		@firCP.val ""
		@firCountry.val ""
		@firResponName.val ""
		@firResponSurname.val ""
		@firResponEmail.val ""
		@firResponTelf1.val ""
		@firBilledModule.val ""
		@firConfig.val ""
		@firGwtUsers.val ""
		@firGwtUsers[0].disabled = false
		
	unloadFirm: (event) -> 
		#console.log "unloadFirm"	
		__FacadeCore.Cache_remove appName + "firmNew"	
		 
__Controller.FirmNew = new FirmNewCtrl "section#newFirm"
