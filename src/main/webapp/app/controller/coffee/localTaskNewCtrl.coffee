class LocalTaskNewCtrl extends Monocle.Controller
	
	events:
		"load #localTask-form" : "loadNew"
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"singleTap a[data-action=single]": "onSingle"
		"change #lotTaskId" : "changeSelectTask"
		"change #lotTaskRate" : "changeRate"
		
	elements:
		"#localTask-form": "localTaskForm"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"a[data-action=combi]": "buttonCombi"
		"a[data-action=single]": "buttonSingle"
		"a[data-action=new]": "buttonNew"
		"#lotTaskId" : "selectTask"
		"#localTask-name" : "lotNameInputContainer"
		"#lotNameError" : "lotNameError"
		"#lotTaskDuration" : "lotTaskDuration"
		"#lotTaskDurationError" : "lotTaskDurationError"
		"#lotTaskPost" : "lotTaskPost"
		"#lotTaskPostError" : "lotTaskPostError"
		"#lotTaskRate" : "lotTaskRate"
		"#lotTaskRateError" : "lotTaskRateError"
		"#symbol_currency" : "symbolCurrency"
		"#lotVisible" : "lotVisible"
	
	changeRate: (event) -> 
		#console.log "changeRate", event
		@lotTaskRate.val parseFloat(@lotTaskRate.val()).toFixed(2)
	
	onSingle: (event) -> 
		#console.log "onSingle"
		__FacadeCore.Router_article "newLocalTask", "localTask-form"
	
	changeSelectTask: (event) -> 
		#console.log "changeSelectTask"
		
		tasks = __FacadeCore.Cache_get appName + "tasks"
		task = Lungo.Core.findByProperty tasks, "id", @selectTask.val()
		asyn = __FacadeCore.Service_Settings_asyncFalse()
					
		url = "http://" + appHost + "/multiText/listByKey"
		data =  {key:task.tasNameMulti}
		nameMultiResult = $$.json url, data

		__FacadeCore.Service_Settings_async(asyn)
				
		nameMulti = Lungo.Core.toArray nameMultiResult
		
		lotNameInput = $$ "#localTask-name input[type=text]"
		lotNameInput.each () -> 
	    		nameMultiText = Lungo.Core.findByProperty nameMulti, "mulLanCode", $$(this).attr "data-lang"
	    		$$(this).val nameMultiText.mulText

	loadNew: (event) -> 
		#console.log "loadNew"
		
		@buttonCombi.show()
		@buttonSingle.hide()
		@buttonNew.hide()
		
		localTask = __FacadeCore.Cache_get appName + "localTaskNew"
		#console.log "localTask",localTask
		local = __FacadeCore.Cache_get appName + "local"
		
		@symbolCurrency.html local.locWhere.wheCurrency
		
		localLangs = Lungo.Core.toArray local.locLangs
		localLangs = Lungo.Core.orderByProperty localLangs, "lanName", "asc"
		lang = Lungo.Core.findByProperty localLangs, "lanCode", langApp
		localLangs = changeArrayToFirst localLangs, lang
		
		if localTask
			
			@buttonCombi.hide()
			
			taskId = localTask.lotTaskId
			
			asyn = __FacadeCore.Service_Settings_asyncFalse()
					
			url = "http://" + appHost + "/multiText/listByKey"
			data =  {key:localTask.lotNameMulti}
			nameMultiResult = $$.json url, data

			__FacadeCore.Service_Settings_async(asyn)
					
			nameMulti = Lungo.Core.toArray nameMultiResult
			nameMulti = Lungo.Core.orderByProperty nameMulti, "mulLanName", "asc"
			lang = Lungo.Core.findByProperty nameMulti, "mulLanCode", langApp
			nameMulti = changeArrayToFirst nameMulti, lang
			@lotTaskDuration.val localTask.lotTaskDuration.toString()
			@lotTaskPost.val localTask.lotTaskPost.toString()
			@lotTaskRate.val  parseFloat(localTask.lotTaskRate).toFixed(2)
			if localTask.lotVisible==1
				@lotVisible[0].options.selectedIndex = 0
			else	
				@lotVisible[0].options.selectedIndex = 1
			
		else
			
			nameMulti = new Array()
			i = -1
			for localLang in localLangs
				i++
				nameMultiText = {mulLanName:localLang.lanName,mulLanCode:localLang.lanCode}
				nameMulti[i] = nameMultiText
			@lotTaskDuration.val ""
			@lotTaskPost.val ""
			@lotTaskRate.val ""
			@lotVisible[0].options.selectedIndex = 0
		
		firm = __FacadeCore.Cache_get appName + "firm"
		if firm.firBilledModule==0
			@lotTaskRate.parent().hide()
			@lotTaskRate.val "0"
								
		@lotNameInputContainer.empty()
		for nameMultiText in nameMulti
			if !localTask || (localTask && Lungo.Core.findByProperty localLangs, "lanCode", nameMultiText.mulLanCode)
				view = new __View.LocalTaskNameView model:nameMultiText
				view.append nameMultiText
		
		tasks = __FacadeCore.Cache_get appName + "tasks"
		if !tasks
			asyn = __FacadeCore.Service_Settings_asyncFalse()
		
			url = "http://" + appHost + "/task/list"
			data = {domain:appFirmDomain}
			tasks = $$.json url, data
			tasks = Lungo.Core.orderByProperty tasks, "tasName", "asc"
			
			__FacadeCore.Cache_remove appName + "tasks"
			__FacadeCore.Cache_set appName + "tasks", tasks
			
			__FacadeCore.Service_Settings_async(asyn)
		
		i=-1
		taskOption = 0
		for task in tasks
			i++
			@selectTask[0].options[i] = new Option task.tasName , task.id
			if taskId && taskId == task.id
				taskOption = i
		@selectTask[0].options.selectedIndex = taskOption
		if !localTask
			this.changeSelectTask(event)
	
	onSave: (event) -> 
		if  (@localTaskForm.hasClass("active"))
			#console.log "onSave"
			if (this.validateForm())
				__FacadeCore.Cache_remove appName + "elementSave"
				__FacadeCore.Cache_remove appName + "elementCancel"
				__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
				__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
				Lungo.Element.loading @buttonSave.selector, "black"
				Lungo.Element.loading @buttonCancel.selector, "black"
				localTask = __FacadeCore.Cache_get appName + "localTaskNew"
				local = __FacadeCore.Cache_get appName + "local"
				data = {localId:local.id,lotTaskId:@selectTask.val(),lotTaskDuration:@lotTaskDuration.val(),lotTaskPost:@lotTaskPost.val(),lotTaskRate:@lotTaskRate.val(),lotVisible:@lotVisible.val()}
				lotNameInput = $$ "#localTask-name input[type=text]"
				lotNameInput.each () -> 
						eval("data."+$$(this)[0].id+" = '"+$$(this).val()+"'")
				if localTask
		    		data.id = localTask.lotId
		    	url = "http://"+appHost+"/localTask/manager/new"
		    	_this = this
		    	$$.put url, data, (response) ->
		    			if response.lotDefault
		    				local.locTaskDefaultId = response.id
		    			Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, () ->
		    					__FacadeCore.Router_article "booking", "local-tasks"
		    					_this.resetArticle()
	    					

	validateForm: () -> 
		#console.log "validateForm"
		result = true
		@lotNameError.html ""
		@lotTaskDurationError.html ""
		@lotTaskPostError.html ""
		@lotTaskRateError.html ""
		lotNameInput = $$ "#localTask-name input[type=text]"
		_this = this
		lotNameInput.each () -> 
				if (result && !$$(this)[0].checkValidity())
					_this.lotNameError.html getMessageValidity($$(this)[0])
					$$(this)[0].focus()
					result = false
		if (result && !checkValidity(@lotTaskDuration.val(),@lotTaskDuration.attr("pattern"),@lotTaskDuration.attr("required")))
			@lotTaskDurationError.html getMessageValidity(@lotTaskDuration[0])
			@lotTaskDuration[0].focus()
			result = false
		if (result && !checkValidity(@lotTaskPost.val(),@lotTaskPost.attr("pattern"),@lotTaskPost.attr("required")))
			@lotTaskPostError.html getMessageValidity(@lotTaskPost[0])
			@lotTaskPost[0].focus()
			result = false	
		if (result && !checkValidity(@lotTaskRate.val(),@lotTaskRate.attr("pattern"),@lotTaskRate.attr("required")))
			@lotTaskRateError.html getMessageValidity(@lotTaskRate[0])
			@lotTaskRate[0].focus()
			result = false	
		result

	onCancel: (event) -> 
		if  (@localTaskForm.hasClass("active"))
			#console.log "onCancel"
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"
			__FacadeCore.Router_article "booking", "local-tasks"
			this.resetArticle()
		
	resetArticle: () -> 
		#console.log "resetArticle"
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		@lotNameError.html ""
		@lotTaskDurationError.html ""
		@lotTaskPostError.html ""
		@lotTaskRateError.html ""
		@lotVisible[0].options.selectedIndex = 0
		__FacadeCore.Cache_remove appName+"localTaskNew"
		
		
__Controller.LocalTaskNew = new LocalTaskNewCtrl "section#newLocalTask"
