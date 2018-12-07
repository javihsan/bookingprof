class ClientListCtrl extends Monocle.Controller
	
	result: {}
	keywordType = null
	
	events:
		"load article#list-clients" : "loadClientList"
		"unload article#list-clients" : "onUnload"
		"keyup article#list-clients #keyword-type" : "onKeyWordType"
	
	elements:
		"header a[href=\\#]" : "header"
		"footer a" : "footer"
	
	onKeyWordType: (event) -> 
		#console.log "onKeyWordType"
		this.showListClients()
	
	loadClientList: (event) -> 
		#console.log "loadClientList"
	
		@header.hide()
		@footer.hide()
	
		Lungo.Element.loading "#list-clients ul", "black"
		
		clients = __FacadeCore.Cache_get appName + "clients"
		this.showClientList clients
				
		
	showClientList: (response) -> 
		#console.log "showClientList"
		if (response.length>0)
			@result = Lungo.Core.toArray response
			@result = Lungo.Core.orderByProperty @result, "whoName", "asc"
			this.showListClients()
		else 
			clientSearch = Lungo.dom "#list-clients ul"
			this.cleanList(clientSearch)
			Lungo.Notification.success findLangTextElement("label.notification.noData.title"), findLangTextElement("label.notification.noData.text"), null, 3, (response) ->
					__FacadeCore.Router_article "booking", "table-month"
		
	
	showListClients: ->
		#console.log "showListClients"
		clientSearch = Lungo.dom "#list-clients ul"
		this.cleanList(clientSearch)
		if !keywordType
			texts = {search:findLangTextElement("form.search")}
			view = new __View.SearchListView
			view.append texts
			keywordType = Lungo.dom "article#list-clients input#keyword-type"
		valSearch = keywordType.val().toString().toLowerCase()
		local = __FacadeCore.Cache_get appName + "local"
		if valSearch!="" || local.locNewClientDefault==1
			for clientAux in @result
				if clientAux.whoName.toLowerCase().indexOf(valSearch) != -1
					client = new __Model.Client
						enabled: true,
						cliId: clientAux.id,
						cliName: clientAux.whoName,
						cliSurname: clientAux.whoSurname,
						cliEmail: clientAux.whoEmail,
						cliGender: clientAux.whoGender,
						cliBirthday: clientAux.whoBirthday,
						cliTelf1: clientAux.whoTelf1,
						cliTelf2: clientAux.whoTelf2,
						cliDesc: clientAux.whoDesc
					#console.log "client", client
					view = new __View.ClientListView model:client
					view.append client
			
	
	resetArticle: () -> 
		#console.log "resetArticle"
		if keywordType
			keywordType.val ""
		keywordType = null
			
	onUnload: (event) -> 
		#console.log "onUnload"	
		this.resetArticle()
		__FacadeCore.Cache_remove appName + "selectClient"
	
	cleanList: (obj) ->
		for objChildren in obj.children()
			if !$$(objChildren).hasClass "dark"
				$$(objChildren).remove()
	
				
__Controller.ClientList = new ClientListCtrl "section#booking"