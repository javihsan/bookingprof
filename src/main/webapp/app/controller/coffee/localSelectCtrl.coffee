class LocalSelectCtrl extends Monocle.Controller

	events:
		"load article#list-local" : "loadLocal"

	elements:
		"#list-local": "listLocal"
		"header a[href=\\#]" : "header"
		"footer a" : "footer"
		"header a[href=\\#menu]" : "aside"
		"#header_text" : "headerText"
			
	loadLocal: (event) -> 
		#console.log "loadLocal"
		
		@header.hide()
		@footer.hide()
		
		asyn = __FacadeCore.Service_Settings_asyncFalse()
		
		url = "http://" + appHost + "/firm/get"
		data = {domain:appFirmDomain}
		firm = $$.json url, data
		
		__FacadeCore.Service_Settings_async asyn 
		
		localId = __FacadeCore.Storage_get appName+"localId"
		if !localId
			@aside.hide()
			@headerText.html firm.firName+" - "+ findLangTextElement("local.select.cabText")
			
		Lungo.Element.loading "#list-local ul", "black"
		
		data = {domain:appFirmDomain}
		_this = this
		$$.json urlListLocalAll, data, (response) -> 
			_this.showLocal response
		
		
	showLocal: (response) -> 
		#console.log "showLocal"
		if (response.length>0)
			result = Lungo.Core.toArray response
			result = Lungo.Core.orderByProperty result, "locName", "asc"
			@listLocal.children().empty()
			
			texts = {cabText:findLangTextElement("label.aside.locals")}
			view = new __View.ListCabView model:texts, container:"section#booking article#list-local ul"
			view.append texts
			
			local = __FacadeCore.Cache_get appName + "local"
			
			for localAux in result
				#console.log "localAux", localAux
				selectLocal = false
				if local && localAux.id == local.id
					selectLocal = true
				localMod = new __Model.Local 
					enabled: true,
					locId: localAux.id,
					locSelect: selectLocal,
					locName: localAux.locName,
					locLocation: localAux.locWhere.wheAddress
				view = new __View.LocalSelectView model:localMod
				view.append localMod
		else
			@listLocal.children().empty()
			texts = {cabText:findLangTextElement("label.aside.locals")}
			view = new __View.ListCabView model:texts, container:"section#booking article#list-local ul"
			view.append texts
			Lungo.Notification.success findLangTextElement("label.notification.noData.title"), findLangTextElement("label.notification.noData.text"), null, 3
	
__Controller.LocalSelect = new LocalSelectCtrl "section#booking"