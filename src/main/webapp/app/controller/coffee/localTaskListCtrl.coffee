class LocalTaskListCtrl extends Monocle.Controller
	
	result = {}
	
	events:
		"load article#local-tasks" : "loadListLocalTask"
		"unload article#local-tasks" : "onUnload"
		"singleTap a[data-action=new]" : "newLocalTask"
		"singleTap a[data-action=all]" : "onAll"
		"singleTap a[data-action=bookable]" : "onBookable"
		"singleTap a[data-action=billed]" : "onBilled"
		
	elements:
		"#local-tasks": "listLocalTask"
		"header a[href=\\#]" : "header"
		"header a[href=\\#menu]" : "aside"
		"a[data-action=new]": "buttonAdd"
		"a[data-action=all]": "buttonAll"
		"a[data-action=bookable]": "buttonBookable"
		"a[data-action=billed]": "buttonBilled"
		"footer a" : "footer"
	
	newLocalTask: (event) -> 
		if  (@listLocalTask.hasClass("active"))
			#console.log "newLocalTask"
			__FacadeCore.Router_article "newLocalTask", "localTask-form"
	
	
	onAll: (event) -> 
		#console.log "onAll"
		this.showLocalTask "all"

	onBookable: (event) -> 
		#console.log "onBookable"
		this.showLocalTask "bookable"

	onBilled: (event) -> 
		#console.log "onBilled"
		this.showLocalTask "billed"

	
	loadListLocalTask: (event) -> 
		#console.log "loadListLocalTask"
		
		@header.hide()
		@aside.show()
		@buttonAdd.show()
		@footer.hide()
				
		firm = __FacadeCore.Cache_get appName + "firm"
	
		if firm.firBilledModule==1
			@buttonBookable.show()
			@buttonAll.show()
			@buttonBilled.show()
				
		Lungo.Element.loading "#local-tasks ul", "black"
		
		url = "http://"+appHost+"/localTask/manager/listCombi"
		local = __FacadeCore.Cache_get appName + "local"
		data = {localId:local.id}
		_this = this
		$$.json url, data, (response) -> 
			_this.showLocalTaskList response
		
		
	showLocalTaskList: (response) -> 
		#console.log "showLocalTaskList"
		if (response.length>0)
			result = Lungo.Core.toArray response
			result = Lungo.Core.orderByProperty result, "lotName", "asc"
			firm = __FacadeCore.Cache_get appName + "firm"
			if firm.firBilledModule==1
				this.showLocalTask "bookable"
			else	
				this.showLocalTask ""
		else
			@listLocalTask.children().empty()
			texts = {cabText:findLangTextElement("label.aside.localTasks")}
			view = new __View.ListCabView model:texts, container:"section#booking article#local-tasks ul"
			view.append texts
			Lungo.Notification.success findLangTextElement("label.notification.noData.title"), findLangTextElement("label.notification.noData.text"), null, 3


	showLocalTask: (type) -> 
		#console.log "showLocalTask", type
		
		@listLocalTask.children().empty()
		
		strType = ""
		if type == "bookable"
			strType =  " - " + findLangTextElement("localTask.bookable")
		else if type == "billed"
			strType = " - " + findLangTextElement("localTask.billed")
		else if type == "all"
			strType = " - " + findLangTextElement("form.all")
		else
			type = "all"	
			
		texts = {cabText:findLangTextElement("label.aside.localTasks")+" "+ strType}
		view = new __View.ListCabView model:texts, container:"section#booking article#local-tasks ul"
		view.append texts
		
		local = __FacadeCore.Cache_get appName + "local"
		textsTemplates = {default:findLangTextElement("label.template.localTaskdefault"), currency:local.locWhere.wheCurrency}
		for localTaskAux in result
			#console.log "localTaskAux",localTaskAux
			if (type == "bookable" && (localTaskAux.lotTaskDuration>0 || (localTaskAux.lotTaskCombiId && localTaskAux.lotTaskCombiId.length>0))) || (type == "billed" && localTaskAux.lotTaskRate>0) || (type == "all")
				defaultAux = false
				if local.locTaskDefaultId == localTaskAux.id
					defaultAux = true
				lotTaskDuration = localTaskAux.lotTaskDuration
				lotTaskPost = "0"
				if localTaskAux.lotTaskPost != null
					lotTaskPost = localTaskAux.lotTaskPost
				lotTaskRate = localTaskAux.lotTaskRate
				hasLotTaskRate = true
				firm = __FacadeCore.Cache_get appName + "firm"
				if firm.firBilledModule==0
					hasLotTaskRate = false
				if 	localTaskAux.lotTaskCombiRes && localTaskAux.lotTaskCombiRes.length>0 
					lotTaskDuration = ""
					for res in localTaskAux.lotTaskCombiRes
						if lotTaskDuration!=""
							lotTaskDuration += ","
						lotTaskDuration += res
					hasLotTaskRate = false	
							
				localTask = new __Model.LocalTask
					enabled: true,
					lotDefault: defaultAux,
					lotId: localTaskAux.id,
					lotLocalId: localTaskAux.lotLocalId,
					lotTaskId: localTaskAux.lotTaskId,
					lotNameMulti: localTaskAux.lotNameMulti,
					lotTaskDuration: lotTaskDuration.toString(),
					lotTaskPost: lotTaskPost.toString(),
					lotTaskRate: parseFloat(lotTaskRate).toFixed(2),
					hasLotTaskRate: hasLotTaskRate,
					lotName: localTaskAux.lotName,
					lotTaskCombiId: localTaskAux.lotTaskCombiId,
					lotTaskCombiRes: localTaskAux.lotTaskCombiRes,
					lotVisible: localTaskAux.lotVisible,
					texts: textsTemplates
				#console.log "localTask",localTask	
				view = new __View.LocalTaskListView model:localTask
				view.append localTask
		
	
	onUnload: (event) -> 
		#console.log "onUnload"	
		result = {}
	
__Controller.LocalTaskList = new LocalTaskListCtrl "section#booking"