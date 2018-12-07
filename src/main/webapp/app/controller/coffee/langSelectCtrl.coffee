class LangSelectCtrl extends Monocle.Controller
	
	events:
		"load article#langs" : "loadLangSelect"
		"singleTap article#langs li" : "onLangSelect"
			
	elements:
		"#langs": "listLangs"
		"header a[href=\\#]" : "header"
		"footer a" : "footer"
		
	loadLangSelect: (event) -> 
		#console.log "loadLangSelect"
	
		@header.hide()
		@footer.hide()
		
		Lungo.Element.loading "#langs ul", "black"
		
		url = "http://"+appHost+"/lang/list"
		_this = this
		$$.json url, null, (response) -> 
			_this.showLoadLangSelect response
		
	showLoadLangSelect: (response) -> 
		#console.log "showLoadLangSelect"	
		
		result = Lungo.Core.toArray response
		@listLangs.children().empty()
		for langAux in result
			lang = new __Model.Lang
				lanId: langAux.lanId,
				lanCode: langAux.lanCode,
				lanName: langAux.lanName
			#console.log "lang", lang
			view = new __View.LangSelectView model:lang
			view.append lang
			
		this.selectLang langApp    			

	selectLang: (langSel) -> 
		#console.log "selectLang", langSel
	
		local = __FacadeCore.Cache_get appName + "local"
		localLangs = local.locLangs
		$$("article#langs *[data-lang]").each () ->
				$$(this).show()
				lang = $$(this).attr "data-lang"
				if (localLangs && lang && Lungo.Core.findByProperty(localLangs, "lanCode", lang))
	    			$$(this).removeClass()
    				$$(this).addClass "selectable"
    				if (lang && lang==langSel)
    					$$(this).addClass "current active"
    			else
    				$$(this).hide()

	onLangSelect: (event) -> 
		#console.log "onLangSelect"
		
		lang = $$(event.target).attr "data-lang"
				
		if (lang && lang!=langApp)
			this.selectLang lang
			changeLang lang
			if $$("#table-month")[0]
				setCurrentAside "langs","table-month"
				__FacadeCore.Router_article "booking", "table-month"
			else if $$("#local-tasks")[0]
				setCurrentAside "langs","local-tasks"
				__FacadeCore.Router_article "booking", "local-tasks"	
			else if $$("#report-sales")[0]
				setCurrentAside "langs","report-sales"
				__FacadeCore.Router_article "booking", "report-sales"
		
					
							
__Controller.LangSelect = new LangSelectCtrl "section#booking"