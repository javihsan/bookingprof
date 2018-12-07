class LocalGeneralCtrl extends Monocle.Controller

	events:
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=reset]" : "onReset"
		"load #local-general" : "loadGeneral"
			
	elements:
		"#local-general": "artLocalGeneral"
		"header a[href=\\#]" : "header"
		"footer a" : "footer"
		"a[data-action=reset]": "buttonReset"
		"a[data-action=save]": "buttonSave"
		"#locBookingClient" : "locBookingClient",
		"#locName" : "locName"
		"#error_locName" : "error_locName"
		"#locAddress" : "locAddress"
		"#error_locAddress" : "error_locAddress"
		"#locCity" : "locCity"
		"#error_locCity" : "error_locCity"
		"#locState" : "locState"
		"#error_locState" : "error_locState"
		"#locCP" : "locCP"
		"#error_locCP" : "error_locCP"
		"#locResponName" : "locResponName"
		"#error_locResponName" : "error_locResponName"
		"#locResponSurname" : "locResponSurname"
		"#error_locResponSurname" : "error_locResponSurname"
		"#locResponEmail" : "locResponEmail"
		"#error_locResponEmail" : "error_locResponEmail"
		"#locResponTelf1" : "locResponTelf1"
		"#error_locResponTelf1" : "error_locResponTelf1"
		"#locMailBookign" : "locMailBookign"
		"#locApoDuration" : "locApoDuration"
		"#error_locApoDuration" : "error_locApoDuration"
		"#locTimeRestricted" : "locTimeRestricted"
		"#error_locTimeRestricted" : "error_locTimeRestricted"
		"#locOpenDays" : "locOpenDays"
		"#error_locOpenDays" : "error_locOpenDays"
		"#locNumPersonsApo" : "locNumPersonsApo"
		"#error_locNumPersonsApo" : "error_locNumPersonsApo"
		"#locNumUsuDays" : "locNumUsuDays"
		"#error_locNumUsuDays" : "error_locNumUsuDays"
		"#locNewClientDefault" : "locNewClientDefault"
		"#locMulServices" : "locMulServices"
		"#locSelCalendar" : "locSelCalendar"
	
	onReset: (event) -> 
		if  (@artLocalGeneral.hasClass("active"))
			#console.log "onReset"
			this.loadGeneral (event)
	
	loadGeneral: (event) -> 
		#console.log "loadGeneral"
		
		@header.hide()
		@footer.hide()
		@buttonReset.show()
		@buttonSave.show()
		
		local = __FacadeCore.Cache_get appName+"local"
		#console.log "local",local

		@locBookingClient[0].options.selectedIndex = local.locBookingClient
		@locName.val local.locName
		@locAddress.val local.locWhere.wheAddress
		@locCity.val local.locWhere.wheCity
		@locState.val local.locWhere.wheState
		@locCP.val local.locWhere.wheCP
		@locResponName.val local.locRespon.whoName
		@locResponSurname.val local.locRespon.whoSurname
		@locResponEmail.val local.locRespon.whoEmail
		@locResponTelf1.val local.locRespon.whoTelf1
		@locApoDuration.val local.locApoDuration.toString()
		@locTimeRestricted.val local.locTimeRestricted.toString()
		@locOpenDays.val local.locOpenDays.toString()
		@locNumPersonsApo.val local.locNumPersonsApo.toString()
		@locNumUsuDays.val local.locNumUsuDays.toString()
		@locNewClientDefault[0].options.selectedIndex = local.locNewClientDefault
		@locMailBookign[0].options.selectedIndex = local.locMailBookign
		@locMulServices[0].options.selectedIndex = local.locMulServices
		@locSelCalendar[0].options.selectedIndex = local.locSelCalendar
		
		firm = __FacadeCore.Cache_get appName + "firm"
		
		if firm.firConfig.configLocal.configLocNumPer==0
			@locNumPersonsApo.parent().hide()
		if firm.firConfig.configLocal.configLocMulSer==0	
			@locMulServices.parent().parent().hide()
		if firm.firConfig.configLocal.configLocSelCal==0	
			@locSelCalendar.parent().parent().hide()	
		
	onSave: (event) -> 
		if  (@artLocalGeneral.hasClass("active"))
			#console.log "onSaveGeneral"
			if (this.validateForm())
				__FacadeCore.Cache_remove appName + "elementSave"
				__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
				Lungo.Element.loading @buttonSave.selector, "black"
				
				url = "http://"+appHost+"/local/manager/update"
				local = __FacadeCore.Cache_get appName+"local"
				data = {localId:local.id,locBookingClient: @locBookingClient.val(),locName: @locName.val(),locAddress:@locAddress.val(),locCity:@locCity.val(),locState:@locState.val(),locApoDuration:@locApoDuration.val(),locCP:@locCP.val(),locTimeRestricted: @locTimeRestricted.val(),locOpenDays: @locOpenDays.val(),locNumPersonsApo: @locNumPersonsApo.val(),locNumUsuDays: @locNumUsuDays.val(), locNewClientDefault:@locNewClientDefault.val(),locResponName: @locResponName.val(),locResponSurname: @locResponSurname.val(),locResponEmail: @locResponEmail.val(),locResponTelf1: @locResponTelf1.val(),locMailBookign:@locMailBookign.val(),locMulServices: @locMulServices.val(),locSelCalendar: @locSelCalendar.val()}
				_this = this
				$$.put url, data, (response) -> 
						#console.log "response",response
						__FacadeCore.Cache_remove appName + "local"
						__FacadeCore.Cache_set appName + "local", response
						_this.buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
						Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3
			
	
	validateForm: -> 
		#console.log "validateForm"
		result = true
		validateForm: -> 
		#console.log "validateForm"
		result = true
		@error_locName.html  ""
		@error_locAddress.html  ""
		@error_locCity.html  ""
		@error_locState.html  ""
		@error_locCP.html  ""
		@error_locResponName.html  ""
		@error_locResponSurname.html  ""
		@error_locResponEmail.html  ""
		@error_locResponTelf1.html  ""
		@error_locApoDuration.html  ""
		@error_locTimeRestricted.html  ""
		@error_locOpenDays.html  ""
		@error_locNumPersonsApo.html  ""
		@error_locNumUsuDays.html  ""

		if (!@locName[0].checkValidity())
			@error_locName.html getMessageValidity(@locName[0])
			@locName[0].focus()
			result = false
		else if (!@locAddress[0].checkValidity())
			@error_locAddress.html getMessageValidity(@locAddress[0])
			@locAddress[0].focus()
			result = false
		else if (!@locCity[0].checkValidity())
			@error_locCity.html getMessageValidity(@locCity[0])
			@locCity[0].focus()
			result = false
		else if (!@locState[0].checkValidity())
			@error_locState.html getMessageValidity(@locState[0])
			@locState[0].focus()
			result = false
		else if (!@locCP[0].checkValidity())
			@error_locCP.html getMessageValidity(@locCP[0])
			@locCP[0].focus()
			result = false
		else if (!@locResponName[0].checkValidity())
			@error_locResponName.html getMessageValidity(@locResponName[0])
			@locResponName[0].focus()
			result = false
		else if (!@locResponSurname[0].checkValidity())
			@error_locResponSurname.html getMessageValidity(@locResponSurname[0])
			@locResponSurname[0].focus()
			result = false
		else if (!@locResponEmail[0].checkValidity())
			@error_locResponEmail.html getMessageValidity(@locResponEmail[0])
			@locResponEmail[0].focus()
			result = false
		else if (!@locResponTelf1[0].checkValidity())
			@error_locResponTelf1.html getMessageValidity(@locResponTelf1[0])
			@locResponTelf1[0].focus()
			result = false							
		else if (!checkValidity(@locApoDuration.val(),@locApoDuration.attr("pattern"),@locApoDuration.attr("required")))
			@error_locApoDuration.html getMessageValidity(@locApoDuration[0])
			@locApoDuration[0].focus()
			result = false
		else if (!checkValidity(@locTimeRestricted.val(),@locTimeRestricted.attr("pattern"),@locTimeRestricted.attr("required")))
			@error_locTimeRestricted.html getMessageValidity(@locTimeRestricted[0])
			@locTimeRestricted[0].focus()
			result = false
		else if (!checkValidity(@locOpenDays.val(),@locOpenDays.attr("pattern"),@locOpenDays.attr("required")))
			@error_locOpenDays.html getMessageValidity(@locOpenDays[0])
			@locOpenDays[0].focus()
			result = false
		else if (!checkValidity(@locNumPersonsApo.val(),@locNumPersonsApo.attr("pattern"),@locNumPersonsApo.attr("required")))
			@error_locNumPersonsApo.html getMessageValidity(@locNumPersonsApo[0])
			@locNumPersonsApo[0].focus()
			result = false
		else if (!checkValidity(@locNumUsuDays.val(),@locNumUsuDays.attr("pattern"),@locNumUsuDays.attr("required")))
			@error_locNumUsuDays.html getMessageValidity(@locNumUsuDays[0])
			@locNumUsuDays[0].focus()
			result = false		
		result	
		 
__Controller.LocalGeneral = new LocalGeneralCtrl "section#booking"
