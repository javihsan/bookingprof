class ClientSearchCtrl extends Monocle.Controller
	
	result = {}
	keywordType = null
	
	events:
		"singleTap a[data-action=cancel]" : "onCancel"
		"load article#search-clients" : "loadClientSearch"
		"unload article#search-clients" : "onUnload"
		"keyup article#search-clients #keyword-type" : "onKeyWordType"
	
	onKeyWordType: (event) -> 
		#console.log "onKeyWordType"
		this.showListClients()
	
	loadClientSearch: (event) -> 
		#console.log "loadClientSearch"
	
		Lungo.Element.loading "#search-clients ul", "black"
		
		clients = __FacadeCore.Cache_get appName + "clients"
		this.showClientSearch clients
		
		
	showClientSearch: (response) -> 
		#console.log "showClientSearch"
		if (response.length>0)
			result = Lungo.Core.toArray response
			result = Lungo.Core.orderByProperty result, "whoName", "asc"
			this.showListClients()
		else 
			clientSearch = Lungo.dom "#search-clients ul"
			this.cleanList(clientSearch)
			Lungo.Notification.success findLangTextElement("label.notification.noData.title"), findLangTextElement("label.notification.noData.text"), null, 3, (response) ->
					__FacadeCore.Router_back()
		
	
	showListClients: ->
		#console.log "showListClients"
		clientSearch = Lungo.dom "#search-clients ul"
		this.cleanList(clientSearch)
		if !keywordType
			texts = {search:findLangTextElement("form.search")}
			view = new __View.SearchTypeView
			view.append texts
			keywordType = Lungo.dom "article#search-clients input#keyword-type"
		valSearch = keywordType.val().toString().toLowerCase()
		local = __FacadeCore.Cache_get appName + "local"
		if valSearch!="" || local.locNewClientDefault==1
			for clientAux in result
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
						cliTelf2: clientAux.whoTelf2
					#console.log "client", client
					view = new __View.ClientSearchListView model:client
					view.append client
			
	onCancel: (event) -> 
		#console.log "cancel"
		router = __FacadeCore.Cache_get appName + "routerSearchClient"
		__FacadeCore.Cache_remove appName + "routerSearchClient"
		__FacadeCore.Router_section router
	
	resetArticle: () -> 
		#console.log "resetArticle"
		if keywordType
			keywordType.val ""
		keywordType = null
			
	onUnload: (event) -> 
		#console.log "unloadNew"	
		this.resetArticle()
		__FacadeCore.Cache_remove appName + "selectClient"
	
	cleanList: (obj) ->
		for objChildren in obj.children()
			if !$$(objChildren).hasClass "dark"
				$$(objChildren).remove()
	
				
__Controller.ClientSearch = new ClientSearchCtrl "section#searchClient"