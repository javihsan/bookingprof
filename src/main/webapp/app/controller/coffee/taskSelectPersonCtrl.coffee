class TaskSelectPersonCtrl extends Monocle.Controller
	
	selectCount: 0
	
	events:
		"load #taskPerson-form" : "loadLocalTaskPerson"
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"singleTap a[data-action=reset]" : "onReset"

	elements:
		"#taskPerson-form": "listArt"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
			
	loadLocalTaskPerson: (event) -> 
		#console.log "loadLocalTaskPerson"
		
		Lungo.Element.loading "#taskPerson-form ul", "black"
		
		combiTasks = __FacadeCore.Cache_get appName+"combiTasks"
		
		@listArt.children().empty()
		
		selectedTasksPerNum = __FacadeCore.Cache_get appName+"selectedTasksPerNum"
		selectedTasksPer = __FacadeCore.Cache_get appName+"selectedTasksPer"+selectedTasksPerNum
		
		tasSelName = findLangTextElement("label.template.job")
		if local.locNumPersonsApo>1
			tasSelName += " "+findLangTextElement("label.template.jobForPerson")+" "+selectedTasksPerNum
		texts = {cabText:tasSelName}
		view = new __View.ListCabView model:texts, container:"section#taskSelect article#taskPerson-form ul"
		view.append texts
		@selectCount = 0
		for localTaskAux in combiTasks
			lotEnabled = false
			for taskSelPer in selectedTasksPer
				if (taskSelPer.id==localTaskAux.id)
					lotEnabled = true
					@selectCount++
					break
			localTask = new __Model.LocalTask 
				enabled: lotEnabled,
				lotId: localTaskAux.id,
				lotTaskDuration: localTaskAux.lotTaskDuration,
				lotName: localTaskAux.lotName
			#console.log "localTask",localTask
			view = new __View.LocalTaskPersonView model:localTask
			view.append localTask
		
		FacadeCore.prototype.count "a[data-action=reset]", @selectCount	

					
	onSave: (event) -> 
		if  (@listArt.hasClass("active"))
			#console.log "onSave"
			
			combiTasks = __FacadeCore.Cache_get appName+"combiTasks"
						
			listLocalTask = $$ "#taskPerson-form ul li"
	
			selectedTasksPer = new Array()
			selectedTasksPerId = new Array()
			i = -1
			c = 0
			for h in [0..listLocalTask.length-1]
				liLocalTask = $$ listLocalTask[h]
				if liLocalTask.hasClass("accept")
					i++
					taskSelPer = {id:parseInt(liLocalTask.attr "lotId"),tasName:liLocalTask.attr "lotName"}
					selectedTasksPer[i] = taskSelPer
					taskAux = Lungo.Core.findByProperty(combiTasks, "id", taskSelPer.id)
					if taskAux.lotTaskCombiId && taskAux.lotTaskCombiId.length>0
						for taskAuxCombi in taskAux.lotTaskCombiId  
							selectedTasksPerId[c] = taskAuxCombi
							c++
					else
						selectedTasksPerId[c] = taskSelPer.id
						c++
			if selectedTasksPer.length>0
				duplicate = arrHasDupes selectedTasksPerId
				if duplicate
					duplicateText = (Lungo.Core.findByProperty(combiTasks, "id", duplicate)).lotName
					_this = this
					Lungo.Notification.error findLangTextElement("label.notification.localTaskCombiDiferent.title"), findLangTextElement("label.notification.localTaskCombiDiferent.text")+" "+duplicateText+" "+findLangTextElement("label.notification.localTaskCombiDiferent.text2"), null, 3
				else	
					__FacadeCore.Cache_remove appName + "elementSave"
					__FacadeCore.Cache_remove appName + "elementCancel"
					__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
					__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
					Lungo.Element.loading @buttonSave.selector, "black"
					Lungo.Element.loading @buttonCancel.selector, "black"
					
					selectedTasksPerNum = __FacadeCore.Cache_get appName+"selectedTasksPerNum"
					__FacadeCore.Cache_remove appName+"selectedTasksPer"+selectedTasksPerNum
					__FacadeCore.Cache_set appName+"selectedTasksPer"+selectedTasksPerNum,selectedTasksPer
					
					__FacadeCore.Router_article "taskSelect","task-form"
					this.resetArticle()
			else
				Lungo.Notification.error findLangTextElement("label.notification.localTaskSelectOne.title"), findLangTextElement("label.notification.localTaskSelectOne.text"), null, 2
 
	
	onReset: (event) -> 
		#console.log "onReset"
		listLocalTask = $$ "#taskPerson-form ul li"
		for h in [0..listLocalTask.length-1]
			liLocalTask = $$ listLocalTask[h]
			if !liLocalTask.hasClass("cancel")
				liLocalTask.removeClass("accept")
				liLocalTask.removeClass("theme")
				liLocalTask.addClass("cancel")
		@selectCount = 0		
		FacadeCore.prototype.count "a[data-action=reset]", @selectCount		
	
		
	onCancel: (event) -> 
		if  (@listArt.hasClass("active"))
			#console.log "onCancel"
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"
			__FacadeCore.Router_article "taskSelect","task-form"
			this.resetArticle()
	
		
	resetArticle: () -> 
		#console.log "resetArticle"
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		__FacadeCore.Cache_remove appName+"selectedTasksPerNum"		 
	
__Controller.TaskSelectPerson = new TaskSelectPersonCtrl "section#taskSelectPerson"