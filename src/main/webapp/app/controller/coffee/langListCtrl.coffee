class LangListCtrl extends Monocle.Controller
	
	events:
		"load article#langs-admin" : "loadLangList"
		"singleTap article#langs-admin li" : "onLangEnabled"
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=reset]" : "onReset"
			
	elements:
		"#langs-admin": "artListLang"
		"header a[href=\\#]" : "header"
		"footer a" : "footer"
		"a[data-action=reset]": "buttonReset"
		"a[data-action=save]": "buttonSave"
	
	onSave: (event) -> 
		if  (@artListLang.hasClass("active"))
			#console.log "onSaveLang"
			
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			Lungo.Element.loading @buttonSave.selector, "black"
			
			selectedLangs = new Array()
			h = -1
			$$("article#langs-admin *[data-lang]").each () ->
	    			lang = $$(this).attr "data-lang"
	    			langEnabledSelect = Lungo.dom "article#langs-admin #langEnabled_"+lang
	    			if (langEnabledSelect.hasClass "accept")
	    				h++
	    				selectedLangs[h] = lang
	
			url = "http://" + appHost + "/local/manager/changeLangs"
			local = __FacadeCore.Cache_get appName + "local"
			data = {localId:local.id,selectedLangs:selectedLangs}
			_this = this
			$$.put url, data, (response) ->
					#console.log "response",response
					__FacadeCore.Cache_remove appName + "local"
					__FacadeCore.Cache_set appName + "local", response
					_this.buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
					Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3
	
	loadLangList: (event) -> 
		#console.log "loadLangList"
	
		@header.hide()
		@footer.hide()
		@buttonReset.show()
		@buttonSave.show()
		
		Lungo.Element.loading "#langs-admin ul", "black"
		
		url = "http://"+appHost+"/lang/list"
		_this = this
		$$.json url, null, (response) -> 
			_this.showLoadLangList response
		
	showLoadLangList: (response) -> 
		#console.log "showLoadLangList"
		
		result = Lungo.Core.toArray response
		@artListLang.children().empty()
		
		texts = {cabText:findLangTextElement("label.aside.langsAdmin")}
		view = new __View.ListCabView model:texts, container:"section#booking article#langs-admin ul"
		view.append texts
		
		for langAux in result
			lang = new __Model.Lang
				lanId: langAux.lanId,
				lanCode: langAux.lanCode,
				lanName: langAux.lanName
			#console.log "lang", lang
			view = new __View.LangListView model:lang
			view.append lang
		
		local = __FacadeCore.Cache_get appName + "local"
		localLangs = local.locLangs
	
		$$("article#langs-admin *[data-lang]").each () ->
    			lang = $$(this).attr "data-lang"
    			$$(this).removeClass()
    			$$(this).addClass "selectable"
    			if (lang && lang==langApp)
    				$$(this).addClass "current active"
    			langEnabledSelect = Lungo.dom "article#langs-admin #langEnabled_"+lang
    			langEnabledSelect.removeClass()
    			langEnabledSelect.addClass "right tag"
    			if (localLangs && lang && Lungo.Core.findByProperty(localLangs, "lanCode", lang))
    				langEnabledSelect.addClass "accept"
    				langEnabledSelect.html findLangTextElement("local.langEnabled")
    			else		
    				langEnabledSelect.addClass "cancel"
    				langEnabledSelect.html findLangTextElement("local.langDisabled")	

	selectLang: (langSel) -> 
		#console.log "selectLang", langSel
		if (langSel!=langApp)
			local = __FacadeCore.Cache_get appName + "local"
			localLangs = local.locLangs
		
			langEnabledSelect = Lungo.dom "article#langs-admin #langEnabled_"+langSel
			if (langEnabledSelect.hasClass "accept")
				langEnabledSelect.removeClass "accept"
				langEnabledSelect.addClass "cancel"
				langEnabledSelect.html findLangTextElement("local.langDisabled")
			else
				langEnabledSelect.removeClass "cancel"
				langEnabledSelect.addClass "accept"
				langEnabledSelect.html findLangTextElement("local.langEnabled")
			

	onLangEnabled: (event) -> 
		#console.log "onLangEnabled"
		
		lang = $$(event.target).attr "data-lang"
		this.selectLang lang
				
	onReset: (event) -> 
		if  (@artListLang.hasClass("active"))
			#console.log "onReset"
			this.loadLangList (event)
				
__Controller.LangList = new LangListCtrl "section#booking"