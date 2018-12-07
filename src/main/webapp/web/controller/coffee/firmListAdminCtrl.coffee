class FirmListAdminCtrl extends Monocle.Controller

	events:
		"load article#list-firm" : "loadFirm"
		"singleTap a[data-action=new]" : "onNewFirm"

	elements:
		"#list-firm": "listFirm"
		"#header_list_text": "header"
	
	onNewFirm: (event) -> 
		#console.log "onNewFirm"
		if  (@listFirm.hasClass("active"))
			__FacadeCore.Router_section "newFirm"

			
	loadFirm: (event) -> 
		#console.log "loadFirm"
		
		@header.html AppAdmin.firmDomain + " - "+findLangTextElement ("label.aside.firms")
		
		Lungo.Element.loading "#list-firm ul", "black"
		
		url = "http://"+appHost+"/firm/admin/list"
		_this = this
		$$.json url, null, (response) -> 
			_this.showFirm response
		
		
	showFirm: (response) -> 
		#console.log "showFirm"
		if (response.length>0)
			result = Lungo.Core.toArray response
			result = Lungo.Core.orderByProperty result, "firName", "asc"
			@listFirm.children().empty()
			
			#texts = {cabText:findLangTextElement("place.select.cabText")}
			#view = new __View.ListCabView model:texts, container:"section#booking article#list-firm ul"
			#view.append texts
			textsTemplates = {enabled:findLangTextElement("form.enabled")}
			for firmAux in result
				#console.log "firmAux", firmAux
				firEnabled = false
				textsTemplates = {enabled:findLangTextElement("form.closed")}
				if firmAux.enabled==1
					firEnabled = true
					textsTemplates.enabled = findLangTextElement("form.enabled")
				firm = new __Model.Firm 
					enabled: firEnabled,
					firId: firmAux.id,
					firName: firmAux.firName,
					firDomain: firmAux.firDomain,
					firServer: firmAux.firServer,
					firResponName: firmAux.firRespon.whoName,
					firResponSurname: firmAux.firRespon.whoSurname,
					firResponEmail: firmAux.firRespon.whoEmail,
					firResponTelf1: firmAux.firRespon.whoTelf1,
					firAddress: firmAux.firWhere.wheAddress
					firCity: firmAux.firWhere.wheCity
					firState: firmAux.firWhere.wheState
					firCP: firmAux.firWhere.wheCP
					firCountry: firmAux.firWhere.wheCountry
					firClassTasks: firmAux.firClassTasks,
					firGwtUsers: firmAux.firGwtUsers,
					firBilledModule: firmAux.firBilledModule,
					firConfig: firmAux.firConfig.numConfig,
					texts: textsTemplates
				view = new __View.FirmListAdminView model:firm
				view.append firm
		else
			@listFirm.children().empty()
			Lungo.Notification.success findLangTextElement("label.notification.noData.title"), findLangTextElement("label.notification.noData.text"), null, 3
	
__Controller.FirmListAdmin = new FirmListAdminCtrl "section#admin"