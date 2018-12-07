class LocalTaskNewCtrl extends Monocle.Controller
	
	events:
		"load #localTaskCombi-form" : "loadNew"
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"singleTap a[data-action=new]" : "addCombi"
		"singleTap a[data-action=combi]": "onCombi"
		"change select[data-id=lotTaskSelect]" : "changeSelectTask"
		
	elements:
		"#localTaskCombi-form": "localTaskFormCombi"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"a[data-action=combi]": "buttonCombi"
		"a[data-action=single]": "buttonSingle"
		"a[data-action=new]": "buttonNew"

	onCombi: (event) -> 
		#console.log "onCombi"
		__FacadeCore.Router_article "newLocalTask", "localTaskCombi-form"

	loadNew: (event) -> 
		#console.log "loadNew"
		
		@buttonCombi.hide()
		@buttonSingle.show()
		@buttonNew.show()
		
		localTask = __FacadeCore.Cache_get appName + "localTaskNew"
		#console.log "localTask",localTask
		
		simpleLocalTasks = __FacadeCore.Cache_get appName + "simpleLocalTasks"
		if !simpleLocalTasks
			asyn = __FacadeCore.Service_Settings_asyncFalse()
		
			url = "http://" + appHost + "/localTask/manager/listOnlySimple"
			local = __FacadeCore.Cache_get appName + "local"
			data = {localId:local.id}
			simpleLocalTasks = $$.json url, data
			simpleLocalTasks = Lungo.Core.orderByProperty simpleLocalTasks, "lotName", "asc"
		
			__FacadeCore.Cache_remove appName + "simpleLocalTasks"
			__FacadeCore.Cache_set appName + "simpleLocalTasks", simpleLocalTasks
			
			__FacadeCore.Service_Settings_async(asyn)

		if simpleLocalTasks.length==0
			_this = this
			Lungo.Notification.success findLangTextElement("label.notification.localTaskCombiNotSingle.title"), findLangTextElement("label.notification.localTaskCombiNotSingle.text"), null, 3, () ->
		    		__FacadeCore.Router_article "newLocalTask", "localTask-form"
		    		__FacadeCore.Cache_remove appName + "localTaskNew"
		    		__FacadeCore.Cache_remove appName + "simpleLocalTasks"
		    		_this.resetArticleCombi()
		
		@localTaskFormCombi.children().empty()
		
		if localTask
			
			@buttonSingle.hide()
		
			textsTemplates = {selectTask:findLangTextElement("localTask.selectTask"),durationCombiTasks:findLangTextElement("localTask.durationCombiTasks"),delete:findLangTextElement("form.delete"),cabCombiTextNew:findLangTextElement("localTask.cabCombiTextNew"),lotVisible:findLangTextElement("localTask.visible"),yes:findLangTextElement("general.yes"),no:findLangTextElement("general.no")}
			combiTask = localTask.lotTaskCombiId
			combiRes = localTask.lotTaskCombiRes
			j = 1
			lotTaskCombiIdFirst = combiTask[0]
			for i in [0..combiRes.length-1]
				lotTaskCombiIdSecond = combiTask[i+1]
				lotTaskCombiRes = combiRes[i]
							
				localTaskMod = new __Model.LocalTask
					lotId: j,
					lotTaskCombiIdFirst: lotTaskCombiIdFirst,
					lotTaskCombiIdSecond: lotTaskCombiIdSecond,
					lotTaskCombiRes: lotTaskCombiRes.toString()
					texts: textsTemplates
				#console.log "localTaskMod",localTaskMod
				if j==1
					view = new __View.LocalTaskCombiFirstView model:localTaskMod
				else
					view = new __View.LocalTaskCombiView model:localTaskMod
				view.append localTaskMod
				j++
				
			lotTaskIdSelect = $$ "#localTaskCombi-form select[data-id=lotTaskSelect]"
			_this = this
			i = 1
			lotTaskIdSelect.each () -> 
				_this.fillCombiTask $$(this), i, false
				i++
			lotVisible = $$ "#localTaskCombi-form select[data-id=lotVisible]"	
			if localTask.lotVisible==1
				lotVisible[0].options.selectedIndex = 0
			else	
				lotVisible[0].options.selectedIndex = 1	
		else
			this.addCombi()
			lotVisible = $$ "#localTaskCombi-form select[data-id=lotVisible]"
			lotVisible[0].options.selectedIndex = 0
				
	
	fillCombiTask: (objSel, ind, isNew) ->	
		#console.log "fillCombiTask",objSel,ind,isNew
		
		nameObjAnt = "#localTaskCombi-form #lotTaskIdSecond"+(ind-1)
		if objSel[0].id == "lotTaskIdSecond1"
			nameObjAnt = "#localTaskCombi-form #lotTaskIdFirst1"
		objSelAnt = $$(nameObjAnt)
		simpleLocalTasks = __FacadeCore.Cache_get appName + "simpleLocalTasks"
		
		if !isNew
			localTask = __FacadeCore.Cache_get appName + "localTaskNew"
			if localTask
				lotCombiId = localTask.lotTaskCombiId[(ind-1)]		
		i=-1
		taskOption = 0
		for simpleLocalTask in simpleLocalTasks
			i++
			objSel[0].options[i] = new Option simpleLocalTask.lotName , simpleLocalTask.id
			if !isNew
				if lotCombiId == simpleLocalTask.id
					taskOption = i
			if objSelAnt.val() && objSelAnt.val() == simpleLocalTask.id.toString()  
				taskPost = simpleLocalTask.lotTaskPost
		if !taskPost
			taskPost = "0"

		objSel[0].options.selectedIndex = taskOption
		objTaskDuration = $$("#localTaskCombi-form #lotTaskDuration"+ind)
		if objSelAnt[0] && objTaskDuration[0] && objTaskDuration.val() == ''
			objTaskDuration.val taskPost.toString()
				
	
	changeSelectTask: (event) -> 
		#console.log "changeSelectTask", event.currentTarget
		tasksSelect = $$ event.currentTarget
		simpleLocalTasks = __FacadeCore.Cache_get appName + "simpleLocalTasks"
		for simpleLocalTask in simpleLocalTasks
			if tasksSelect.val() && tasksSelect.val() == simpleLocalTask.id.toString()  
				taskPost = simpleLocalTask.lotTaskPost
				break
		if !taskPost
			taskPost = "0"
		
		i = 0
		for select in $$("select[data-id=lotTaskSelect]")
			i++
			if select.id == event.currentTarget.id
				break
		objTaskDuration = $$("#localTaskCombi-form #lotTaskDuration"+i)
		if objTaskDuration[0]
			objTaskDuration.val taskPost.toString()
	
	addCombi: () -> 
		#console.log "addCombi"
		i = 1
		while (Lungo.dom "#localTaskCombi-form #lotTaskDuration"+i)[0]
			i++

		textsTemplates = {selectTask:findLangTextElement("localTask.selectTask"),durationCombiTasks:findLangTextElement("localTask.durationCombiTasks"),delete:findLangTextElement("form.delete"),cabCombiTextNew:findLangTextElement("localTask.cabCombiTextNew"),lotVisible:findLangTextElement("localTask.visible"),yes:findLangTextElement("general.yes"),no:findLangTextElement("general.no")}
		localTaskMod = new __Model.LocalTask
			lotId: i,
			texts: textsTemplates
		if i==1
			view = new __View.LocalTaskCombiFirstView model:localTaskMod
		else
			view = new __View.LocalTaskCombiView model:localTaskMod
		view.append localTaskMod
		
		if i==1
			objFirst = Lungo.dom "#localTaskCombi-form #lotTaskIdFirst"+i
			this.fillCombiTask objFirst , i, true
		objSecond = Lungo.dom "#localTaskCombi-form #lotTaskIdSecond"+i
		this.fillCombiTask objSecond, i, true
		
	onSave: (event) -> 
		if  (@localTaskFormCombi.hasClass("active"))
			#console.log "onSave"
			if (this.validateFormCombi())
				__FacadeCore.Cache_remove appName + "elementSave"
				__FacadeCore.Cache_remove appName + "elementCancel"
				__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
				__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
				Lungo.Element.loading @buttonSave.selector, "black"
				Lungo.Element.loading @buttonCancel.selector, "black"
	
				localTask = __FacadeCore.Cache_get appName + "localTaskNew"
				simpleLocalTasks = __FacadeCore.Cache_get appName + "simpleLocalTasks"
	
				selectLotTaskCombiId = ""
				selectLotTaskCombiRes = ""
	
				listCombi = Lungo.dom "#localTaskCombi-form ul li"
				
				if listCombi.length>1
					lotTaskCombiId = $$ "#localTaskCombi-form select[data-id=lotTaskSelect]"
					lotTaskCombiIdAux = new Array()
					i = 0
					lotTaskCombiId.each () ->
						lotTaskCombiIdAux[i] = $$(this).val()
						i++
					duplicate = arrHasDupes lotTaskCombiIdAux
					if duplicate
						duplicateText = (Lungo.Core.findByProperty(simpleLocalTasks, "id", duplicate)).lotName
						_this = this
						Lungo.Notification.error findLangTextElement("label.notification.localTaskCombiDiferent.title"), findLangTextElement("label.notification.localTaskCombiDiferent.text")+" "+duplicateText+" "+findLangTextElement("label.notification.localTaskCombiDiferent.text2"), null, 3, () ->
								_this.resetArticleCombi()
					else		
						i=0
						lotTaskCombiId.each () ->
							if i>0
								selectLotTaskCombiId += ","
							selectLotTaskCombiId += $$(this).val()
							i++
						lotTaskCombiRes = $$ "#localTaskCombi-form input"
						i=0
						lotTaskCombiRes.each () ->
							if i>0
								selectLotTaskCombiRes += ","
							selectLotTaskCombiRes += $$(this).val()
							i++
						lotVisible = $$ "#localTaskCombi-form select[data-id=lotVisible]"	
						url = "http://"+appHost+"/localTask/manager/newCombi"
						local = __FacadeCore.Cache_get appName + "local"
						data = {localId:local.id,lotTaskCombiId:selectLotTaskCombiId.toString(),lotTaskCombiRes:selectLotTaskCombiRes.toString(),lotVisible:lotVisible.val()}
						if localTask
		    				data.id = localTask.lotId
						_this = this
						$$.put url, data, () ->
		    				Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, () ->
		    						__FacadeCore.Router_article "booking", "local-tasks"
		    						__FacadeCore.Cache_remove appName + "localTaskNew"
		    						__FacadeCore.Cache_remove appName + "simpleLocalTasks"
		    						_this.resetArticleCombi()

	
	
	validateFormCombi: -> 
		#console.log "validateFormCombi"
		result = true
		lotTaskDurationErrorId = $$ "#localTaskCombi-form label[data-id=lotTaskDurationError]"
		lotTaskDurationErrorId.each () -> 
			$$(this).html ""
		listCombi = Lungo.dom "#localTaskCombi-form ul li"
		if listCombi.length>1
			for i in [1..listCombi.length-1]
				lotTaskCombiRes = $$(listCombi[i]).find('input')[0]
				lotTaskDurationError = $$ "#localTaskCombi-form [id=lotTaskDurationError"+i+"]"
				if (!lotTaskCombiRes.checkValidity())
					lotTaskDurationError.html getMessageValidity(lotTaskCombiRes)
					lotTaskCombiRes.focus()
					result = false
					break
		result
	
	
	onCancel: (event) -> 
		if  (@localTaskFormCombi.hasClass("active"))
			#console.log "onCancel"
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"
			this.resetArticleCombi()
			__FacadeCore.Cache_remove appName + "localTaskNew"
			__FacadeCore.Cache_remove appName + "simpleLocalTasks"
			__FacadeCore.Router_back()
					
	resetArticleCombi: () -> 
		#console.log "resetArticleCombi"
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		lotTaskDurationError = $$ "#localTaskCombi-form [data-id=lotTaskDurationError]"
		lotTaskDurationError.each () ->
			$$(this).html ""
		lotVisible = $$ "#localTaskCombi-form select[data-id=lotVisible]"
		lotVisible[0].options.selectedIndex = 0	
				
__Controller.LocalTaskNew = new LocalTaskNewCtrl "section#newLocalTask"
