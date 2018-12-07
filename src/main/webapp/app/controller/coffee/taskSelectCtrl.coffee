class TaskSelectCtrl extends Monocle.Controller
	
	antNumPersonsVal = null
	
	events:
		"load article#task-form" : "onLoad"
		"singleTap button[data-action=save]" : "onSave"
		"singleTap button[data-action=cancel]" : "onCancel"
		"change #numPersons" : "changeNumPersons"
		"change select[data-id=taskSelect]" : "changeTaskSelect"
		"change input[data-id=taskGoodsSelect]" : "changeTaskGoodsSelect"
		"change #task-form #calendarId" : "changeCalendarSelect"
			
	elements:
		"#task-form": "listArt"
		"#numPersons" : "numPersons"
	
	changeTaskSelect: (event) -> 
		#console.log "changeTaskSelect", event.currentTarget
		option = event.currentTarget.options[event.currentTarget.selectedIndex]
		tasksSelect = $$ event.currentTarget
		taskNumPerson = tasksSelect.attr "data-numPerson"
		taskSelPer = {id:parseInt(option.value),tasName:option.text}
		selectedTasksPer = new Array()
		selectedTasksPer[0] = taskSelPer 
		__FacadeCore.Cache_remove appName+"selectedTasksPer"+taskNumPerson
		__FacadeCore.Cache_set appName+"selectedTasksPer"+taskNumPerson, selectedTasksPer
		if local.locSelCalendar==1
			this.fillSelCalendar()
		
	changeCalendarSelect: (event) -> 
		#console.log "changeCalendarSelect", event.currentTarget
		option = event.currentTarget.options[event.currentTarget.selectedIndex]
		if event.currentTarget.selectedIndex>0
			selectedCalendars = new Array()
			calendarSel = {id:option.value,name:option.text}
			selectedCalendars[0] = calendarSel 
			__FacadeCore.Cache_remove appName+"selectedCalendars"
			__FacadeCore.Cache_set appName+"selectedCalendars", selectedCalendars
		else		
			__FacadeCore.Cache_remove appName+"selectedCalendars"
		
	showTasks: (event) -> 
		#console.log "showTasks",@numPersons.val()
		local = __FacadeCore.Cache_get appName + "local"
		textsTemplates = {jobForPerson:findLangTextElement("label.template.job"),changePush:findLangTextElement("label.html.apoFor3")}
		combiTasks = __FacadeCore.Cache_get appName+"combiTasks"
		for h in [0..@numPersons.val()-1]
			tasSelName = findLangTextElement("label.template.jobForPerson") + " " + @numPersons[0].options[h].text
			if local.locNumPersonsApo==1
				tasSelName = ""
			tasMultiple = true
			if local.locMulServices==0
				tasMultiple = false
			taskSel = new __Model.TaskSel
				enabled: true,
				tasSelId: @numPersons[0].options[h].value,
				tasSelName: tasSelName,
				tasMultiple: tasMultiple,
				texts: textsTemplates
			#console.log "taskSel", taskSel
			view = new __View.TaskSelectView model:taskSel
			view.append taskSel
			
			selectedTasksPer = __FacadeCore.Cache_get appName+"selectedTasksPer"+(h+1)
			if tasMultiple
				strTask = ""
				for i in [0..selectedTasksPer.length-1]
					if i>0
						strTask += " , "
					strTask += selectedTasksPer[i].tasName
				tasksSelect = Lungo.dom "article#task-form #task_"+(taskSel.tasSelId)
				tasksSelect.html strTask
			else	
				tasksSelect = Lungo.dom "article#task-form #task_"+(taskSel.tasSelId)	
				i=-1	
				taskOption = 0
				for task in combiTasks
					i++
					tasksSelect[0].options[i] = new Option task.lotName , task.id
					if selectedTasksPer[0].id.toString() == task.id.toString()
						tasksSelect[0].options.selectedIndex = i
		
		if local.locSelCalendar==1
			textsTemplates = {selectCalendar:findLangTextElement("place.select.cabText")}
			view = new __View.TaskSelectCalendarView model:textsTemplates
			view.append textsTemplates
			this.fillSelCalendar()
		else
			__FacadeCore.Cache_remove appName+"selectedCalendars"	
					
		textsTemplates = {ok:findLangTextElement("event.searchHours"),cancel:findLangTextElement("form.cancel")}			
		view = new __View.TaskSelectButtonView model:textsTemplates
		view.append textsTemplates
		
											
	fillSelCalendar: () ->
		#console.log "fillSelCalendar"
		asyn = __FacadeCore.Service_Settings_asyncFalse()
		
		url = "http://"+appHost+"/calendar/listCandidate"
		local = __FacadeCore.Cache_get appName + "local"
		selectTaskParam = new Array()
		i=0
		for h in [0..antNumPersonsVal-1]
			selectedTasksPer = __FacadeCore.Cache_get appName+"selectedTasksPer"+(h+1)
			for taskSel in selectedTasksPer  
				if selectTaskParam.indexOf(taskSel.id)==-1
					selectTaskParam[i] = taskSel.id
					i++
		data = {localId:local.id,selectedTasks:selectTaskParam}			
		calendars = $$.json url, data
		calendars = Lungo.Core.orderByProperty calendars, "calName", "asc"
		
		__FacadeCore.Service_Settings_async(asyn)
		
		objCalendarSel = Lungo.dom "#task-form #calendarId"
		objCalendarSel.empty();
		objCalendarSel[0].options[0] = new Option findLangTextElement("general.anyone") , 0
		selectedCalendars = __FacadeCore.Cache_get appName+"selectedCalendars"
		i=0
		selOption = 0
		for calendar in calendars
			i++
			objCalendarSel[0].options[i] = new Option calendar.calName , calendar.id
			if selectedCalendars && selectedCalendars[0].id.toString() == calendar.id.toString()
				selOption = i
			else if selOption==0 && calendar.calName.toLowerCase().indexOf("pere")!=-1
				selOption = i
				selectedCalendars = new Array()
				calendarSel = {id:calendar.id,name:calendar.calName}
				selectedCalendars[0] = calendarSel 
				__FacadeCore.Cache_remove appName+"selectedCalendars"
				__FacadeCore.Cache_set appName+"selectedCalendars", selectedCalendars
		objCalendarSel[0].options.selectedIndex = selOption
					
					
	changeNumPersons: (event) -> 
		#console.log "changeNumPersons"
		if antNumPersonsVal
			# Borramos los botones
			delLi = Lungo.dom "article#task-form li:last-child"
			delLi.remove()
			# Borramos selCalendar si hay
			delLi = Lungo.dom "article#task-form #selectCalendarLi"
			delLi.remove()
			# Borramos selTask por cada persona			
			for h in [0..antNumPersonsVal-1]
				delLi = Lungo.dom "article#task-form li:last-child"
				delLi.remove()
		antNumPersonsVal = @numPersons.val()
		
		__FacadeCore.Cache_remove appName+"selectedTasksCountProv"
		__FacadeCore.Cache_set appName+"selectedTasksCountProv", antNumPersonsVal
		
		selectedTasks = __FacadeCore.Cache_get appName+"selectedTasks"
		selectedTasksCount = __FacadeCore.Cache_get appName+"selectedTasksCount"
		combiTasks = __FacadeCore.Cache_get appName+"combiTasks"
		local = __FacadeCore.Cache_get appName + "local"
		defaultTask = Lungo.Core.findByProperty(combiTasks, "id", local.locTaskDefaultId)
		taskSelPer = {id:defaultTask.id,tasName:defaultTask.lotName}
		if appFirmDomain == 'adveo'
			taskSelPer.numLines = 1
			taskSelPer.numPallets = 1
		
		ind = 0
		for h in [0..@numPersons.val()-1]
			selectedTasksPer = __FacadeCore.Cache_get appName+"selectedTasksPer"+(h+1)
			if !selectedTasksPer
				if selectedTasksCount && selectedTasksCount[h]
					selectedTasksPer = new Array()
					for i in [0..selectedTasksCount[h]-1]
						selectedTasksPer[i] = selectedTasks[ind]
						i++	
						ind++
				else
				    selectedTasksPer = new Array()
				    selectedTasksPer[0] = taskSelPer

				__FacadeCore.Cache_remove appName+"selectedTasksPer"+(h+1)
				__FacadeCore.Cache_set appName+"selectedTasksPer"+(h+1), selectedTasksPer
				
		if appFirmDomain == 'adveo'
			this.showTasksGoods(event)
		else	
			this.showTasks(event)
		
	onLoad: (event) -> 
		#console.log "onLoad"
		
		local = __FacadeCore.Cache_get appName + "local"
		 
		for i in [1..local.locNumPersonsApo]
			@numPersons[0].options[i-1] = new Option i, i
		
		selectedTasksCountProv = __FacadeCore.Cache_get appName+"selectedTasksCountProv"
		if !selectedTasksCountProv
			selectedTasksCount = __FacadeCore.Cache_get appName+"selectedTasksCount"
			if selectedTasksCount
				selectedTasksCountProv = selectedTasksCount.length

		if selectedTasksCountProv
			@numPersons[0].options.selectedIndex = selectedTasksCountProv-1
		else
			@numPersons[0].options.selectedIndex = 0
		
		if local.locNumPersonsApo==1
			@numPersons.parent().parent().hide()
			@numPersons[0].options.selectedIndex = 0
			
		this.changeNumPersons(event)
		   		
	onSave: (event) -> 
		if  (@listArt.hasClass("active") && this.validateForm())
			#console.log "onSave"
			selectedTasks = new Array()
			selectedTasksCount = new Array()
			i=0
			for h in [0..antNumPersonsVal-1]
				selectedTasksPer = __FacadeCore.Cache_get appName+"selectedTasksPer"+(h+1)
				for taskSel in selectedTasksPer  
					selectedTasks[i] = taskSel
					i++
				selectedTasksCount[h] = selectedTasksPer.length
				
			__FacadeCore.Cache_remove appName+"selectedTasks"
			__FacadeCore.Cache_set appName+"selectedTasks", selectedTasks
			
			__FacadeCore.Cache_remove appName+"selectedTasksCount"
			__FacadeCore.Cache_set appName+"selectedTasksCount", selectedTasksCount
	
			for i in [1..local.locNumPersonsApo]
				__FacadeCore.Cache_remove appName+"selectedTasksPer"+i
			__FacadeCore.Cache_remove appName+"selectedTasksCountProv"
	
			__FacadeCore.Router_article "booking", "table-day"		
	
	
	onCancel: (event) -> 
		if  (@listArt.hasClass("active")) 
			#console.log "cancel"
			local = __FacadeCore.Cache_get appName + "local"
			for i in [1..local.locNumPersonsApo]
				__FacadeCore.Cache_remove appName+"selectedTasksPer"+i
			__FacadeCore.Cache_remove appName+"selectedTasksCountProv"	
			
			__FacadeCore.Router_article "booking", "table-month"
		
	
	showTasksGoods: (event) -> 
		#console.log "showTasksGoods",@numPersons.val()
		selectedTasksPer = __FacadeCore.Cache_get appName+"selectedTasksPer"+1
		taskSelPer = selectedTasksPer[0]
		textsTemplates = {numLines:findLangTextElement("label.template.selNumLines"),numPallets:findLangTextElement("label.template.selNumPallets")}
		taskSel = new __Model.TaskSel
			enabled: true,
			tasGoodsNumLines: taskSelPer.numLines,
			tasGoodsNumPallets: taskSelPer.numPallets,
			texts: textsTemplates
		#console.log "taskSel", taskSel
		view = new __View.TaskSelectGoodsView model:taskSel
		view.append taskSel
				
		textsTemplates = {ok:findLangTextElement("event.searchHours"),cancel:findLangTextElement("form.cancel")}			
		view = new __View.TaskSelectButtonView model:textsTemplates
		view.append textsTemplates
		
	
	changeTaskGoodsSelect: (event) -> 
		#console.log "changeTaskGoodsSelect", event.currentTarget
		tasksSelect = $$ event.currentTarget
		selectedTasksPer = __FacadeCore.Cache_get appName+"selectedTasksPer"+1
		taskSelPer = selectedTasksPer[0]
		if (event.currentTarget.checkValidity())	
			eval("taskSelPer."+event.currentTarget.id+" = parseInt(tasksSelect.val())")
			selectedTasksPer = new Array()
			selectedTasksPer[0] = taskSelPer 
			__FacadeCore.Cache_remove appName+"selectedTasksPer"+1
			__FacadeCore.Cache_set appName+"selectedTasksPer"+1, selectedTasksPer
	
	
	validateForm: -> 
		#console.log "validateForm"
		result = true
		numLinesSel = $$ "#numLines"
		error_numLines = $$ "#error_numLines"
		error_numLines.html ""
		numPalletsSel = $$ "#numPallets"
		error_numPallets = $$ "#error_numPallets"
		error_numPallets.html ""
		if (numLinesSel[0] && !numLinesSel[0].checkValidity())	
			error_numLines.html getMessageValidity(numLinesSel[0])
			numLinesSel[0].focus()
			result = false
		else if (numPalletsSel[0] && !numPalletsSel[0].checkValidity())	
			error_numPallets.html getMessageValidity(numPalletsSel[0])
			numPalletsSel[0].focus()
			result = false
		result	
			
				 
__Controller.TaskSelect = new TaskSelectCtrl "section#taskSelect"
