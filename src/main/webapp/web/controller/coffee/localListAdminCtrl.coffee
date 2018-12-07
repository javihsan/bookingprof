class LocalListAdminCtrl extends Monocle.Controller

	events:
		"load article#list-local" : "loadLocal"
		"singleTap a[data-action=new]" : "onNewLocal"

	elements:
		"#list-local": "listLocal"
		"#header_list_text": "header"
	
	onNewLocal: (event) -> 
		#console.log "onNewLocal"
		if  (@listLocal.hasClass("active"))
			__FacadeCore.Router_section "newLocal"
			
	loadLocal: (event) -> 
		#console.log "loadLocal"
		
		@header.html AppAdmin.firmDomain + " - "+findLangTextElement ("label.aside.locals")
		
		Lungo.Element.loading "#list-local ul", "black"
		
		url = "http://"+appHost+"/local/admin/list"
		data = {domain:AppAdmin.firmDomain}
		_this = this
		$$.json url, data, (response) -> 
			_this.showLocal response
		
		
	showLocal: (response) -> 
		#console.log "showLocal"
		if (response.length>0)
			result = Lungo.Core.toArray response
			result = Lungo.Core.orderByProperty result, "locName", "asc"
			@listLocal.children().empty()
			
			#texts = {cabText:findLangTextElement("place.select.cabText")}
			#view = new __View.ListCabView model:texts, container:"section#booking article#list-local ul"
			#view.append texts
			
			textsTemplates = {enabled:findLangTextElement("form.enabled")}
			for localAux in result
				#console.log "localAux", localAux
				locEnabled = false
				textsTemplates = {enabled:findLangTextElement("form.closed")}
				if localAux.enabled==1
					locEnabled = true
					textsTemplates.enabled = findLangTextElement("form.enabled")
				local = new __Model.Local 
					enabled: locEnabled,
					locId: localAux.id,
					locName: localAux.locName,
					locLocation: localAux.locLocation,
					texts: textsTemplates
				view = new __View.LocalListAdminView model:local
				view.append local
		else
			@listLocal.children().empty()
			Lungo.Notification.success findLangTextElement("label.notification.noData.title"), findLangTextElement("label.notification.noData.text"), null, 3
	
__Controller.LocalListAdmin = new LocalListAdminCtrl "section#admin"