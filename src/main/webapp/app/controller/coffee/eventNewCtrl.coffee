class EventNewCtrl extends Monocle.Controller
	
	conserveData = false
	
	events:
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"singleTap a[data-action=search-client]" : "onSearchClient"
		"load #newEvent" : "loadNew"
		"unload #newEvent" : "unloadNew"
		"change #eveClientNew" : "changeEveClientNew"
		"change #eveClientEmail" : "changeCliEmail"
		
	elements:
		"#event_hour" : "eventHour"
		"#eveTime" : "eveTime"
		"#error_eveTime" : "error_eveTime"
		"#eveClientEmail" : "eveClientEmail"
		"#error_eveClientEmail" : "error_eveClientEmail"
		"#eveClientName" : "eveClientName"
		"#error_eveClientName" : "error_eveClientName"
		"#eveClientTelf" : "eveClientTelf"
		"#error_eveClientTelf" : "error_eveClientTelf"
		"#eveDescAlega" : "eveDescAlega"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"#eveClientNew" : "eveClientNew"
		"#searchLi": "buttonSearch"
		
	
	changeCliEmail: (event) ->
		#console.log "changeCliEmail"
		if (@eveTime[0] && @eveClientEmail.val().length>0)
			asyn = __FacadeCore.Service_Settings_asyncFalse()
	
			url = "http://" + appHost + "/client/operator/listByEmail"
			data = {domain:appFirmDomain, email:@eveClientEmail.val()}
			eveClient = $$.json url, data
			
			__FacadeCore.Service_Settings_async(asyn)
			#console.log  "@eveClientNew.val()", @eveClientNew.val()
			if eveClient && @eveClientNew.val()=="1"
				@eveClientNew[0].options[1].selected = true
				Lungo.Notification.success findLangTextElement("label.notification.existsClient.title"), findLangTextElement("label.notification.existsClient.text"), null, 3
				eveClient.cliId = eveClient.id
				@eveClientName.val eveClient.whoName
				if eveClient.whoTelf1
					@eveClientTelf.val eveClient.whoTelf1
				else if eveClient.whoTelf2
					@eveClientTelf.val eveClient.whoTelf2
				this.changeEveClientNew event
				__FacadeCore.Cache_remove appName+"selectClient"
				__FacadeCore.Cache_set appName+"selectClient", eveClient 
				return false
		return true
	
			
	onSearchClient: (event) ->
		#console.log "onSearchClient"
		conserveData = true
		__FacadeCore.Cache_remove appName+"routerSearchClient"
		__FacadeCore.Cache_set appName+"routerSearchClient", "newEvent" 
		__FacadeCore.Router_article "searchClient","search-clients"
	
	changeEveClientNew: (event) -> 
		#console.log "changeEveClientNew"
		if @eveClientNew.val()=="1"
			@eveClientName[0].disabled = false
			@eveClientEmail[0].disabled = false
			@eveClientTelf[0].disabled = false
			@buttonSearch.hide()
		else
			@eveClientName[0].disabled = true
			@eveClientEmail[0].disabled = true
			@eveClientTelf[0].disabled = true
			@buttonSearch.show()
		if @eveTime[0]
			@error_eveTime.html  ""
		@error_eveClientEmail.html  ""
		@error_eveClientName.html  ""
		@error_eveClientTelf.html  ""
			
			
	onSave: (event) -> 
		#console.log "onSave"
		if (@buttonSave.html().indexOf("Ok")!=-1 && this.validateForm() && this.changeCliEmail(event))
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"
			local = __FacadeCore.Cache_get appName + "local"
			appointment = __FacadeCore.Cache_get appName+"newApo"
			startTime = appointment.apoStartTime 
			if @eveTime[0]
				a = @eveTime.val().split(':')
				startTime = new Date(startTime)
				startTime.setUTCHours a[0]
				startTime.setUTCMinutes a[1]
				startTime = startTime.getTime()
				# si estamos en admin y no es nuevo
				if @eveClientNew.val()=="0"
					eveClient = __FacadeCore.Cache_get appName+"selectClient"

			data = {eveDescAlega: @eveDescAlega.val(), localId:local.id, eveStartTime:startTime}
			if eveClient
				data.cliId = eveClient.cliId
			else
				data.cliName = @eveClientName.val()
				data.cliEmail = @eveClientEmail.val()
				data.cliTelf = @eveClientTelf.val()
	
			selectedTasks = __FacadeCore.Cache_get appName+"selectedTasks"
			#console.log "selectedTasks",selectedTasks
			selectTaskParam = new Array()
			h=0
			for taskSel in selectedTasks
				selectTaskParam[h] = taskSel.id
				h++
			data.selectedTasks = selectTaskParam	
			
			selectedTasksCount = __FacadeCore.Cache_get appName+"selectedTasksCount"
			data.selectedTasksCount = selectedTasksCount
			
			selectedCalendars = __FacadeCore.Cache_get appName+"selectedCalendars"
			#console.log "selectedCalendars",selectedCalendars
			selectCalendarsParam = new Array()
			if selectedCalendars
				h=0
				for calendarSel in selectedCalendars
					selectCalendarsParam[h] = calendarSel.id
					h++
			data.selectedCalendars = selectCalendarsParam
			if selectedTasks[0].numLines
				data.numLines = selectedTasks[0].numLines
				data.numPallets = selectedTasks[0].numPallets
			_this = this
			$$.post urlEventNew, data, (response) -> 
					#console.log "onSuccess", response
					if (_this.eveTime[0])
						Lungo.Notification.success findLangTextElement("label.notification.bookedApoAdmin.title"), findLangTextElement("label.notification.bookedApoAdmin.text"), null, 3, (response) ->
							_this.resetArticle()
							__FacadeCore.Router_article "booking","table-month"
					else
						__FacadeCore.Storage_set appName+"eveClient", null
						__FacadeCore.Storage_set appName+"eveClient", data
						Lungo.Notification.success findLangTextElement("label.notification.bookedApo.title"), findLangTextElement("label.notification.bookedApo.text"), null, 3, (response) ->
							_this.resetArticle()
							__FacadeCore.Router_article "booking","table-month"	

	validateForm: -> 
		#console.log "validateForm"
		result = true
		if @eveTime[0]
			@error_eveTime.html  ""
		@error_eveClientEmail.html  ""
		@error_eveClientName.html  ""
		@error_eveClientTelf.html  ""
		if (@eveTime[0] && !@eveTime[0].checkValidity())
			@error_eveTime.html getMessageValidity(@eveTime[0])
			@eveTime[0].focus()
			result = false
		else if ((@eveClientName[0].disabled && !checkValidity(@eveClientName.val(),@eveClientName.attr("pattern"),@eveClientName.attr("required")))||
				(!@eveClientName[0].disabled && !@eveClientName[0].checkValidity()))
			@error_eveClientName.html getMessageValidity(@eveClientName[0])
			@eveClientName[0].focus()
			result = false
		else if (!@eveClientEmail[0].checkValidity())
			@error_eveClientEmail.html getMessageValidity(@eveClientEmail[0])
			@eveClientEmail[0].focus()
			result = false
		else if (!checkValidity(@eveClientTelf.val(),@eveClientTelf.attr("pattern"),@eveClientTelf.attr("required")))	
			@error_eveClientTelf.html getMessageValidity(@eveClientTelf[0])
			@eveClientTelf[0].focus()
			result = false
		result
		
	onCancel: (event) -> 
		#console.log "cancel"	
		__FacadeCore.Cache_remove appName + "elementSave"
		__FacadeCore.Cache_remove appName + "elementCancel"
		__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
		__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
		Lungo.Element.loading @buttonSave.selector, "black"
		Lungo.Element.loading @buttonCancel.selector, "black"
		this.resetArticle()
		if @eveTime[0]
			__FacadeCore.Router_article "taskSelect","task-form"
		else
			__FacadeCore.Router_article "booking","table-day"		
	
	resetArticle: () -> 
		#console.log "resetArticle"	
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		@eveClientName.val ""
		@eveClientEmail.val ""
		@eveClientTelf.val ""
		@eveDescAlega.val ""
		if @eveTime[0]
			@eveTime.val ""
			@error_eveTime.html  ""
			@eveClientNew[0].options[0].selected = true
			@eveClientName[0].disabled = false
			@eveClientEmail[0].disabled = false
			@eveClientTelf[0].disabled = false
			@buttonSearch.hide()
		@error_eveClientEmail.html  ""
		@error_eveClientName.html  ""
		@error_eveClientTelf.html  ""
	
	unloadNew: (event) -> 
		#console.log "unloadNew"
		if !conserveData
			__FacadeCore.Cache_remove appName+"newApo"
			local = __FacadeCore.Cache_get appName + "local"
			if local.locCacheTasks != 1
				__FacadeCore.Cache_remove appName+"selectedTasks"
				__FacadeCore.Cache_remove appName+"selectedTasksCount"
				__FacadeCore.Cache_remove appName+"selectedCalendars"
		__FacadeCore.Cache_remove appName+"selectClient"
		
	loadNew: (event) -> 
		#console.log "loadNew"
		conserveData = false
		appointment = __FacadeCore.Cache_get appName+"newApo"
		local = __FacadeCore.Cache_get appName + "local"
		selectedCalendars = __FacadeCore.Cache_get appName+"selectedCalendars"
		
		dateHour = new Date(appointment.apoStartTime)
		strDateHour = dateToStringHour(dateHour)
		hourApo = dateToStringFormat(dateHour)+" "+strDateHour
		selectedTasks = __FacadeCore.Cache_get appName+"selectedTasks"
		selectedTasksCount = __FacadeCore.Cache_get appName+"selectedTasksCount"
		strTasks = " . "
		if selectedTasks[0].numLines
			strTasks += findLangTextElement("label.template.numLines")+": "+selectedTasks[0].numLines
			strTasks += " - "+findLangTextElement("label.template.numPallets")+": "+selectedTasks[0].numPallets
		else	
			if local.locNumPersonsApo > 1
				strTasks += selectedTasksCount.length + " "+findLangTextElement("label.html.apoFor2") 
				strTasks += ": "
			for taskSel in selectedTasks
				if local.locMulServices == 1
					strTasks += " 1 "
				strTasks += taskSel.tasName
		if selectedCalendars	
			strTasks += "."
			for calendarSel in selectedCalendars
				strTasks += " " + calendarSel.name
				
		@eventHour.html hourApo + strTasks
		if @eveTime[0]
			if (@eveTime.val().length==0)
				@eveTime.val strDateHour
			eveClient = __FacadeCore.Cache_get appName+"selectClient"
		else
			eveClient = __FacadeCore.Cache_get appName+"clientSession"
			if !eveClient
				eveClient = __FacadeCore.Storage_get appName+"eveClient"
		if eveClient
			@eveClientName.val eveClient.cliName
			@eveClientEmail.val eveClient.cliEmail
			if eveClient.cliTelf
				@eveClientTelf.val eveClient.cliTelf
			else if eveClient.cliTelf1
				@eveClientTelf.val eveClient.cliTelf1
			else if eveClient.cliTelf2
				@eveClientTelf.val eveClient.cliTelf2
		
		@error_eveClientEmail.html  ""
		@error_eveClientName.html  ""
		@error_eveClientTelf.html  ""

		if appFirmDomain == 'adveo'
			@eveClientNew.val ("0")
			if @eveTime[0]
				@eveClientNew.parent().parent().hide()
			this.changeEveClientNew event	
		else if @eveTime[0] && local.locNewClientDefault==0
			@eveClientNew.val ("0")
			this.changeEveClientNew event
			 
__Controller.EventNew = new EventNewCtrl "section#newEvent"
