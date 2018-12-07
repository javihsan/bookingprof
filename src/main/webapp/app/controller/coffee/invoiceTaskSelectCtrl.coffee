class InvoiceTaskSelectCtrl extends Monocle.Controller

	events:
		"load #invoiceTaskSelect-form" : "loadLocalTaskInv"
		"singleTap a[data-action=cancel]" : "onCancel"
		"change #lotSelect" : "changeSelectTask"

	elements:
		"#invoiceTaskSelect-form": "listArt"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"a[data-action=new]": "buttonNew"
		"a[data-action=newPro]" : "buttonNewProduct"
		
	changeSelectTask: (event) -> 
		console.log "changeSelectTask"
		
		selectTask = Lungo.dom "#invoiceTaskSelect-form #lotSelect"
		this.loadLocalTaskInv event, parseInt(selectTask.val())
			
	loadLocalTaskInv: (event, taskIdSelect) -> 
		#console.log "loadLocalTaskInv", taskIdSelect
		
		@buttonSave.hide()
		@buttonNew.hide()
		@buttonNewProduct.hide()
		
		Lungo.Element.loading "#invoiceTaskSelect-form ul", "black"
		
		simpleLocalTasks = __FacadeCore.Cache_get appName+"simpleLocalTasks"
		products = __FacadeCore.Cache_get appName + "products"

		@listArt.children().empty()
		
		selectedTasksBilNum = __FacadeCore.Cache_get appName+"selectedTasksBilNum"
		selectedTasksBilType = __FacadeCore.Cache_get appName+"selectedTasksBilType"
		selectedTasksBil = __FacadeCore.Cache_get appName+"bilTaskId"+selectedTasksBilNum
		local = __FacadeCore.Cache_get appName + "local"
		if selectedTasksBilType==0
			texts = {cabText:findLangTextElement("billed.selectTask")+" "+selectedTasksBilNum}
		else
			texts = {cabText:findLangTextElement("billed.selectProduct")+" "+selectedTasksBilNum}
		view = new __View.ListCabView model:texts, container:"section#newInvoice article#invoiceTaskSelect-form ul"
		view.append texts
		
		if selectedTasksBilType==0
			textsTemplates = {selectTask:findLangTextElement("localTask.selectSystemTask")}
			view = new __View.LocalTaskInvSelectView model:textsTemplates
			view.append textsTemplates
			tasks = __FacadeCore.Cache_get appName+"tasks"
			i=-1
			taskOption = 0
			selectTask = Lungo.dom "#invoiceTaskSelect-form #lotSelect"
			if !taskIdSelect
			    taskIdSelect = selectedTasksBil.lotTaskId
			for task in tasks
				i++
				selectTask[0].options[i] = new Option task.tasName , task.id
				if taskIdSelect == task.id
					taskOption = i
				selectTask[0].options.selectedIndex = taskOption
			textsTemplates = {currency:local.locWhere.wheCurrency}
			for localTaskAux in simpleLocalTasks
				if parseInt(selectTask.val())==localTaskAux.lotTaskId 
					lotEnabled = false
					if (selectedTasksBil.id==localTaskAux.id || selectedTasksBil.lotId==localTaskAux.id)
						lotEnabled = true
					localTask = new __Model.LocalTask 
						enabled: lotEnabled,
						lotId: localTaskAux.id,
						lotTaskRate: localTaskAux.lotTaskRate,
						lotName: localTaskAux.lotName,
						lotTaskId: localTaskAux.lotTaskId,
						texts: textsTemplates
					#console.log "localTask",localTask	
					view = new __View.LocalTaskInvView model:localTask
					view.append localTask
		else		
			textsTemplates = {currency:local.locWhere.wheCurrency}
			for productAux in products
				lotEnabled = false
				if (selectedTasksBil.id==productAux.id)
					lotEnabled = true
				localTask = new __Model.LocalTask 
					enabled: lotEnabled,
					lotId: productAux.id,
					lotTaskRate: productAux.proRate,
					lotName: productAux.proName,
					texts: textsTemplates
				#console.log "localTask",localTask	
				view = new __View.LocalTaskInvView model:localTask
				view.append localTask
		
	onCancel: (event) -> 
		if  (@listArt.hasClass("active"))
			#console.log "onCancel"
			__FacadeCore.Cache_remove appName+"selectedTasksBilNum"
			__FacadeCore.Cache_remove appName+"selectedTasksBilType"
			__FacadeCore.Router_article "newInvoice","invoice-form"
	
		
	
__Controller.InvoiceTaskSelect = new InvoiceTaskSelectCtrl "section#newInvoice"