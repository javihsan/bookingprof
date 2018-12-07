class InvoiceNewCtrl extends Monocle.Controller
	
	conserveData: false
		
	events:
		"load #newInvoice" : "loadNew"
		"unload #newInvoice" : "unloadNew"
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"singleTap a[data-action=new]" : "addTask"
		"singleTap a[data-action=newPro]" : "addProduct"
		"singleTap a[data-action=search-client]" : "onSearchClient"
		"change #invoice-form input[data-id=bilRate]" : "changeRate"
		"change #invDate" : "changeDate"
		"change #invClientNew" : "changeInvClientNew"
		"change #invClientEmail" : "changeCliEmail"
		
	elements:
		"#invoice-form": "invoiceForm"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"a[data-action=new]": "buttonNew"
		"a[data-action=newPro]" : "buttonNewProduct"
		
	loadNew: (event) -> 
		if (@invoiceForm.hasClass("active"))		
			#console.log "loadNew", @conserveData
			
			@buttonSave.show()
			
			if @conserveData
				eveClient = __FacadeCore.Cache_get appName+"selectClient"
				if eveClient
					invClientName = $$ "#invClientName"
					invClientSurname = $$ "#invClientSurname"
					invClientEmail = $$ "#invClientEmail"
					invClientName.val eveClient.cliName
					invClientSurname.val eveClient.cliSurname
					invClientEmail.val eveClient.cliEmail
	
					error_invClientName = $$ "#error_invClientName"
					error_invClientSurname = $$ "#error_invClientSurname"
					error_invClientEmail = $$ "#error_invClientEmail"
					error_invClientName.html  ""
					error_invClientSurname.html  ""
					error_invClientEmail.html  ""
				
				selectedTasksBilNum = __FacadeCore.Cache_get appName+"selectedTasksBilNum"
				if selectedTasksBilNum
    				objSelect = Lungo.dom "#invoice-form #bilTaskId"+selectedTasksBilNum
    				selectedTasksBil = __FacadeCore.Cache_get appName+"bilTaskId"+selectedTasksBilNum
    				if selectedTasksBil.proName
    					objSelect.html selectedTasksBil.proName
    					ratValueNew = selectedTasksBil.proRate
    				else
    					objSelect.html selectedTasksBil.lotName
    					ratValueNew = selectedTasksBil.lotTaskRate
    				objRate = Lungo.dom "#invoice-form #bilRate"+selectedTasksBilNum
    				objRate.val ratValueNew.toString()
    				bilTaskIdError = $$ "#error_bilTaskId"+selectedTasksBilNum
    				if bilTaskIdError
    					bilTaskIdError.hide()
					this.changeRate()
				__FacadeCore.Cache_remove appName+"selectedTasksBilNum"
				
				
			else
					
				eventFin = __FacadeCore.Cache_get appName + "eventFin"
								
				simpleLocalTasks = __FacadeCore.Cache_get appName + "simpleLocalTasks"
				if !simpleLocalTasks
					asyn = __FacadeCore.Service_Settings_asyncFalse()
				
					url = "http://" + appHost + "/localTask/manager/listOnlySimpleInv"
					local = __FacadeCore.Cache_get appName + "local"
					data = {localId:local.id}
					simpleLocalTasks = $$.json url, data
					simpleLocalTasks = Lungo.Core.orderByProperty simpleLocalTasks, "lotName", "asc"
					
					__FacadeCore.Cache_remove appName + "simpleLocalTasks"
					__FacadeCore.Cache_set appName + "simpleLocalTasks", simpleLocalTasks
					
					url = "http://" + appHost + "/task/list"
					data = {domain:appFirmDomain}
					tasks = $$.json url, data
					tasks = Lungo.Core.orderByProperty tasks, "tasName", "asc"
			
					__FacadeCore.Cache_remove appName + "tasks"
					__FacadeCore.Cache_set appName + "tasks", tasks
					
					url = "http://"+appHost+"/product/operator/list"
					data = {localId:local.id}
					products = $$.json url, data
					products = Lungo.Core.orderByProperty products, "proName", "asc"
					
					__FacadeCore.Cache_remove appName + "products"
					__FacadeCore.Cache_set appName + "products", products
					
					url = "http://"+appHost+"/calendar/operator/listDiary"
					data = {localId:local.id}
					calendars = $$.json url, data
					calendars = Lungo.Core.orderByProperty calendars, "calName", "asc"
					
					__FacadeCore.Cache_remove appName + "calendars"
					__FacadeCore.Cache_set appName + "calendars", calendars
					
					__FacadeCore.Service_Settings_async(asyn)
				
				@invoiceForm.children().empty()
				
				if eventFin

					asyn = __FacadeCore.Service_Settings_asyncFalse()
				
					url = "http://" + appHost + "/event/operator/listByICS"
					data = {ICS:eventFin.eveICS}
					ICSEvents = $$.json url, data
					ICSEvents = Lungo.Core.orderByProperty ICSEvents,"eveStartTime","asc"
					
					__FacadeCore.Cache_remove appName + "ICSEvents"
					__FacadeCore.Cache_set appName + "ICSEvents", ICSEvents
					
					__FacadeCore.Service_Settings_async(asyn)
					
					invDate = new Date(eventFin.eveStartTime)
					invDay = dateToStringFormat(invDate)
					invHour = dateToStringHour(invDate)
					invClient = eventFin.eveClient.whoName
					if eventFin.eveClient.whoSurname
						invClient += " "+ eventFin.eveClient.whoSurname
					if eventFin.eveClient.whoEmail
						invClient += " - "+ eventFin.eveClient.whoEmail
					
					local = __FacadeCore.Cache_get appName + "local"
					textsTemplates = {currency:local.locWhere.wheCurrency,rate:findLangTextElement("invoice.rate"),date:findLangTextElement("invoice.date"),client:findLangTextElement("invoice.client"),desc:findLangTextElement("invoice.desc")}				
					invoiceMod = new __Model.Invoice
						invId: j,
						invTime: invDay + ' ' + invHour,
						invClient: invClient,
						texts: textsTemplates
					#console.log "invoiceMod",invoiceMod
					view = new __View.InvoiceView model:invoiceMod
					view.append invoiceMod	
					
					textsTemplates = {changePush:findLangTextElement("label.html.apoFor3"),notBilled:findLangTextElement("localTask.notBilled"),currency:local.locWhere.wheCurrency,selectTask:findLangTextElement("billed.selectTask"),selectCalendar:findLangTextElement("billed.selectCalendar"),rate:findLangTextElement("billed.rate"),delete:findLangTextElement("form.delete")}			
					j = 1
					for eventAux in ICSEvents
						billedMod = new __Model.Billed
							bilId: j,
							bilRate: eventAux.eveLocalTask.lotTaskRate,
							texts: textsTemplates
						#console.log "billedMod",billedMod
						view = new __View.BilledView model:billedMod
						view.append billedMod
						j++
						
					bilTaskIdSelect = $$ "#invoice-form span[data-id=bilTaskId]"
					_this = this
					i = 0
					bilTaskIdSelect.each () -> 
						_this.fillTask $$(this), i, true
						i++
						
				else

					local = __FacadeCore.Cache_get appName + "local"
					textsTemplates = {currency:local.locWhere.wheCurrency,rate:findLangTextElement("invoice.rate"),date:findLangTextElement("invoice.date"),new:findLangTextElement("invoice.newClient"),yes:findLangTextElement("general.yes"),no:findLangTextElement("general.no"),search:findLangTextElement("form.search"),name:findLangTextElement("client.name"),surname:findLangTextElement("client.surname"),email:findLangTextElement("client.email"),desc:findLangTextElement("invoice.desc")}
					invDate = newDateTimezone()
					invDay = dateToStringSim(invDate,"-")
					invHour = dateToStringHour(invDate)				
					invoiceMod = new __Model.Invoice
						invId: j,
						invTime:invDay,
						eveHour:invHour,
						texts: textsTemplates
					#console.log "invoiceMod",invoiceMod
					view = new __View.InvoiceNewView model:invoiceMod
					view.append invoiceMod
					
					if local.locNewClientDefault==0
						invClientNew = $$ "#invClientNew"
						invClientNew.val ("0")
						this.changeInvClientNew event

			@conserveData = false
			@buttonNew.show()
			products = __FacadeCore.Cache_get appName + "products"
			if products && products.length>0
				@buttonNewProduct.show()
			else
				@buttonNewProduct.hide()
	
	fillTask: (objSel, ind, sel) ->	
		#console.log "fillTask",objSel,ind
		objCalendarSel = Lungo.dom "#invoice-form #bilCalendarId"+(ind+1)
		if sel
			eventFin = __FacadeCore.Cache_get appName + "eventFin"			
			if eventFin
				ICSEvents = __FacadeCore.Cache_get appName + "ICSEvents"
				localTaskSel = ICSEvents[ind].eveLocalTask
				if localTaskSel.lotTaskRate == 0
					bilTaskIdError = $$ "#error_bilTaskId"+(ind+1)
					if bilTaskIdError
						bilTaskIdError.show() 
				__FacadeCore.Cache_remove appName+"bilTaskId"+(ind+1)
				__FacadeCore.Cache_set appName+"bilTaskId"+(ind+1), localTaskSel
				calendarId = ICSEvents[ind].eveCalendarId

		selectedTasksBil = __FacadeCore.Cache_get appName+"bilTaskId"+(ind+1)

		if selectedTasksBil.proName
			objSel.html selectedTasksBil.proName
			ratValueNew = selectedTasksBil.proRate
		else
			objSel.html selectedTasksBil.lotName
			ratValueNew = selectedTasksBil.lotTaskRate
		
		objRate = Lungo.dom "#invoice-form #bilRate"+(ind+1)
		objRate.val ratValueNew.toString()
		
		calendars = __FacadeCore.Cache_get appName + "calendars"
		i=-1
		taskOption = 0
		for calendar in calendars
			i++
			objCalendarSel[0].options[i] = new Option calendar.calName , calendar.id
			if sel
				if calendarId == calendar.id
					taskOption = i
		objCalendarSel[0].options.selectedIndex = taskOption
		
		this.changeRate()		
	
	addTask: (event) -> 
		#console.log "addTask"
		i = 1
		while (Lungo.dom "#invoice-form #bilTaskId"+i)[0]
			i++
		simpleLocalTasks = __FacadeCore.Cache_get appName + "simpleLocalTasks"
		firstTask = simpleLocalTasks[0] 
		local = __FacadeCore.Cache_get appName + "local"
		textsTemplates = {changePush:findLangTextElement("label.html.apoFor3"),notBilled:findLangTextElement("localTask.notBilled"),currency:local.locWhere.wheCurrency,selectTask:findLangTextElement("billed.selectTask"),selectCalendar:findLangTextElement("billed.selectCalendar"),rate:findLangTextElement("billed.rate"),delete:findLangTextElement("form.delete")}
		billedMod = new __Model.Billed
			bilId: i,
			bilRate: firstTask.lotTaskRate,
			texts: textsTemplates
		#console.log "billedMod",billedMod
		view = new __View.BilledView model:billedMod
		view.append billedMod
	
		__FacadeCore.Cache_remove appName+"bilTaskId"+i
		__FacadeCore.Cache_set appName+"bilTaskId"+i,firstTask
	
		objSelect = Lungo.dom "#invoice-form #bilTaskId"+i
		this.fillTask objSelect, (i-1), false
		
	addProduct: (event) -> 
		#console.log "addProduct"
		i = 1
		while (Lungo.dom "#invoice-form #bilTaskId"+i)[0]
			i++
		products = __FacadeCore.Cache_get appName + "products"
		firstProduct = products[0] 
		local = __FacadeCore.Cache_get appName + "local"
		textsTemplates = {changePush:findLangTextElement("label.html.apoFor3"),notBilled:findLangTextElement("localTask.notBilled"),currency:local.locWhere.wheCurrency,selectTask:findLangTextElement("billed.selectProduct"),selectCalendar:findLangTextElement("billed.selectCalendar"),rate:findLangTextElement("billed.rate"),delete:findLangTextElement("form.delete")}
		billedMod = new __Model.Billed
			bilId: i,
			bilRate: firstProduct.proRate,
			texts: textsTemplates
		#console.log "billedMod",billedMod
		view = new __View.BilledView model:billedMod
		view.append billedMod
	
		__FacadeCore.Cache_remove appName+"bilTaskId"+i
		__FacadeCore.Cache_set appName+"bilTaskId"+i,firstProduct
	
		objSelect = Lungo.dom "#invoice-form #bilTaskId"+i
		objSelect.attr "data-product", "1"
		this.fillTask objSelect,(i-1), false
		
	changeRate: (event) -> 
		#console.log "changeRate"
		rate =  parseFloat(0)
		bilRateId = $$ "#invoice-form input[data-id=bilRate]"
		bilRateId.each () -> 
			valueFloat = parseFloat($$(this).val())
			$$(this).val valueFloat.toFixed(2)
			rate += valueFloat
		objRateTotal = Lungo.dom "#invoice-form #invRate"
		objRateTotal.html parseFloat(rate).toFixed(2)		
	
	changeDate: (event) -> 
		#console.log "changeDate"
		invDate = $$ "#invDate"
		error_invDate = $$ "#error_invDate"
		error_invDate.html ""
		invDate.val formatDate(invDate.val())
		
	onSearchClient: (event) ->
		#console.log "onSearchClient"
		@conserveData = true
		__FacadeCore.Cache_remove appName+"routerSearchClient"
		__FacadeCore.Cache_set appName+"routerSearchClient", "newInvoice"
		__FacadeCore.Router_article "searchClient","search-clients"
	
	changeInvClientNew: (event) -> 
		#console.log "changeInvClientNew"
		invClientNew = $$ "#invClientNew"
		buttonSearch = $$ "#invoice-form #searchLi"
		invClientName = $$ "#invClientName"
		error_invClientName = $$ "#error_invClientName"
		invClientSurname = $$ "#invClientSurname"
		error_invClientSurname = $$ "#error_invClientSurname"
		invClientEmail = $$ "#invClientEmail"
		error_invClientEmail = $$ "#error_invClientEmail"
		if invClientNew.val()=="1"
			invClientName[0].disabled = false
			invClientSurname[0].disabled = false
			invClientEmail[0].disabled = false
			buttonSearch.hide()
		else
			invClientName[0].disabled = true
			invClientSurname[0].disabled = true
			invClientEmail[0].disabled = true
			buttonSearch.show()
		error_invClientName.html  ""
		error_invClientSurname.html  ""
		error_invClientEmail.html  ""	
	
	changeCliEmail: (event) ->
		#console.log "changeCliEmail"
		invTime = $$ "#invTime"
		invClientEmail = $$ "#invClientEmail"
		if (invTime[0] && invClientEmail.val().length>0)
			asyn = __FacadeCore.Service_Settings_asyncFalse()
	
			url = "http://" + appHost + "/client/operator/listByEmail"
			data = {domain:appFirmDomain, email:invClientEmail.val()}
			invClient = $$.json url, data
			
			__FacadeCore.Service_Settings_async(asyn)
			
			invClientNew = $$ "#invClientNew"
			invClientName = $$ "#invClientName"
			invClientSurname = $$ "#invClientSurname"
			
			if invClient && invClientNew.val()=="1"
				invClientNew[0].options[1].selected = true
				Lungo.Notification.success findLangTextElement("label.notification.existsClient.title"), findLangTextElement("label.notification.existsClient.text"), null, 3
				invClient.cliId = invClient.id
				invClientName.val invClient.whoName
				invClientSurname.val invClient.whoSurname
				this.changeInvClientNew(event)
				__FacadeCore.Cache_remove appName+"selectClient"
				__FacadeCore.Cache_set appName+"selectClient", invClient 
				return false
		return true
		
	onSave: (event) -> 
		#console.log "onSave"
		if (this.validateForm() && this.changeCliEmail(event))
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			bilTaskId = $$ "#invoice-form span[data-id=bilTaskId]"
			__FacadeCore.Cache_remove appName + "bilTaskId"
			__FacadeCore.Cache_set appName + "bilTaskId",bilTaskId
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"

			if bilTaskId.length>0
				bilTaskIdError = $$ "#invoice-form div[data-id=bilTaskIdError]"
				billedTaskError = false
				bilTaskIdError.each () -> 
					style = ($$ this).attr "style"
					if !billedTaskError && parseInt(style.indexOf "block") >= 0
						billedTaskError = true
				if billedTaskError
					_this = this
					Lungo.Notification.error findLangTextElement("label.notification.invoiceBillableTask.title"), findLangTextElement("label.notification.invoiceBillableTask.text"), null, 3, () ->
								_this.buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
								_this.buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
				else				
					strBilTaskId = ""
					strBilTypeId = ""
					i=0
					bilTaskId.each () -> 
						if i>0
							strBilTaskId += ","
							strBilTypeId += ","
						bilTask = __FacadeCore.Cache_get appName+$$(this).attr "id"
						if bilTask.id 
							strBilTaskId += bilTask.id
						else
							strBilTaskId += bilTask.lotId		
						if ($$(this).attr("data-product"))
							strBilTypeId += "1"
						else
							strBilTypeId += "0"
						i++		
					bilCalendarId = $$ "#invoice-form select[data-id=bilCalendarId]"
					strBilCalendarId = ""
					i=0
					bilCalendarId.each () -> 
						if i>0
							strBilCalendarId += ","
						strBilCalendarId += $$(this).val()
						i++
					
					bilRateId = $$ "#invoice-form input[data-id=bilRate]"
					strBilRateId = ""
					i=0
					bilRateId.each () -> 
						if i>0
							strBilRateId += ","
						strBilRateId += parseFloat($$(this).val().replace(',','.'))
						i++				
					
					url = "http://"+appHost+"/invoice/operator/new"
					local = __FacadeCore.Cache_get appName + "local"
					invDesc = $$ "#invDesc"
					data = {localId:local.id,invDesc:invDesc.val(),bilTaskId:strBilTaskId.toString(),bilTypeId:strBilTypeId.toString(),bilCalendarId:strBilCalendarId.toString(),bilRateId:strBilRateId.toString()}
					eventFin = __FacadeCore.Cache_get appName + "eventFin"
					#console.log "eventFin",eventFin
					if eventFin
						data.invClientId = eventFin.eveClient.id
						data.invTime = eventFin.eveStartTime
						data.ICS = eventFin.eveICS
					else
			    		invTime = $$ "#invTime"
			    		invDate = $$ "#invDate"
			    		invClientName = $$ "#invClientName"
			    		invClientSurname = $$ "#invClientSurname"
			    		invClientEmail = $$ "#invClientEmail"
			    		a = invTime.val().split(':')
			    		invTimeData = new Date(invDate.val())
			    		invTimeData.setUTCHours a[0]
			    		invTimeData.setUTCMinutes a[1]
			    		invTimeData = invTimeData.getTime()
			    		data.invTime = invTimeData
			    		invClientNew = $$ "#invClientNew"
			    		if invClientNew.val()=="0"
			    			invClient = __FacadeCore.Cache_get appName+"selectClient"
			    		if invClient
			    			data.invClientId = invClient.cliId
			    		else
			    			data.cliName = invClientName.val()
			    			data.cliSurname = invClientSurname.val()
			    			data.cliEmail = invClientEmail.val()
					_this = this
					$$.put url, data, () ->
			    		Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, () ->
			    				__FacadeCore.Router_article "booking", "list-invoices"
			else
				_this = this
				Lungo.Notification.error findLangTextElement("label.notification.invoiceOneTask.title"), findLangTextElement("label.notification.invoiceOneTask.text"), null, 3, () ->
							_this.buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
							_this.buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"

	
	validateForm: -> 
		#console.log "validateForm"
		result = true
		invTime = $$ "#invTime"
		if (invTime[0])
			error_invTime = $$ "#error_invTime"
			invDate = $$ "#invDate"
			error_invDate = $$ "#error_invDate"
			invClientName = $$ "#invClientName"
			error_invClientName = $$ "#error_invClientName"
			invClientSurname = $$ "#invClientSurname"
			error_invClientSurname = $$ "#error_invClientSurname"
			invClientEmail = $$ "#invClientEmail"
			error_invClientEmail = $$ "#error_invClientEmail"
			error_invTime.html  ""
			error_invDate.html  ""
			error_invClientName.html  ""
			error_invClientSurname.html  ""
			error_invClientEmail.html  ""
			if (!checkValidityDate(invDate.val(),invDate.attr("required")))
				error_invDate.html getMessageValidity(invDate[0])
				invDate[0].focus()
				result = false
			else if (!invTime[0].checkValidity())	
				error_invTime.html getMessageValidity(invTime[0])
				invTime[0].focus()
				result = false
			else if ((invClientName[0].disabled && !checkValidity(invClientName.val(),invClientName.attr("pattern"),invClientName.attr("required")))||
					(!invClientName[0].disabled && !invClientName[0].checkValidity()))
				error_invClientName.html getMessageValidity(invClientName[0])
				invClientName[0].focus()
				result = false
			else if (!invClientEmail[0].checkValidity())
				error_invClientEmail.html getMessageValidity(invClientEmail[0])
				invClientEmail[0].focus()
				result = false
		bilRateIdError = $$ "#invoice-form label[data-id=bilRateError]"
		bilRateIdError.each () -> 
			$$(this).html ""
		bilRateId = $$ "#invoice-form input[data-id=bilRate]"
		if result && bilRateId.length>0
			for i in [0..bilRateId.length-1]
				bilRate = bilRateId[i]
				bilRateError = $$ bilRateIdError[i]
				if (!bilRate.checkValidity())
					bilRateError.html getMessageValidity(bilRate)
					bilRate.focus()
					result = false
					break
		result
	
	
	onCancel: (event) -> 
		if (@invoiceForm.hasClass("active"))
			#console.log "onCancel"
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			bilTaskId = $$ "#invoice-form span[data-id=bilTaskId]"
			__FacadeCore.Cache_remove appName + "bilTaskId"
			__FacadeCore.Cache_set appName + "bilTaskId",bilTaskId
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"
			__FacadeCore.Router_section "booking"
					
	resetArticle: () -> 
		#console.log "resetArticle"
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		@invoiceForm.children().empty()
	
	unloadNew: (event) -> 
		if (@invoiceForm.hasClass("active"))
			if !@conserveData
				this.resetArticle()
				__FacadeCore.Cache_remove appName + "eventFin"
				__FacadeCore.Cache_remove appName + "simpleLocalTasks"
				__FacadeCore.Cache_remove appName + "tasks"
				__FacadeCore.Cache_remove appName + "products"
				__FacadeCore.Cache_remove appName + "calendars"
				__FacadeCore.Cache_remove appName + "ICSEvents"
				bilTaskId = __FacadeCore.Cache_get appName + "bilTaskId"
				bilTaskId.each () -> 
					__FacadeCore.Cache_remove appName+$$(this).attr "id"
			__FacadeCore.Cache_remove appName+"selectClient"
				
__Controller.InvoiceNew = new InvoiceNewCtrl "section#newInvoice"
