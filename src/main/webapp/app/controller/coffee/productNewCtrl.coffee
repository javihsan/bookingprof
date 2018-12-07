class ProductNewCtrl extends Monocle.Controller
	
	events:
		"load #product-form" : "loadNew"
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"change #proRate" : "changeRate"
		
	elements:
		"#product-form": "productForm"
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"a[data-action=new]": "buttonNew"
		"#product-name" : "proNameInputContainer"
		"#proNameError" : "proNameError"
		"#proRate" : "proRate"
		"#proRateError" : "proRateError"
		"#symbol_currency" : "symbolCurrency"


	changeRate: (event) -> 
		#console.log "changeRate", event
		@proRate.val parseFloat(@proRate.val()).toFixed(2)

		
	loadNew: (event) -> 
		#console.log "loadNew"
		
		@buttonNew.hide()
		
		product = __FacadeCore.Cache_get appName + "productNew"

		local = __FacadeCore.Cache_get appName + "local"
		
		@symbolCurrency.html local.locWhere.wheCurrency
		
		localLangs = Lungo.Core.toArray local.locLangs
		localLangs = Lungo.Core.orderByProperty localLangs, "lanName", "asc"
		lang = Lungo.Core.findByProperty localLangs, "lanCode", langApp
		localLangs = changeArrayToFirst localLangs, lang
		
		if product
			
			asyn = __FacadeCore.Service_Settings_asyncFalse()
					
			url = "http://" + appHost + "/multiText/listByKey"
			data =  {key:product.proNameMulti}
			nameMultiResult = $$.json url, data

			__FacadeCore.Service_Settings_async(asyn)
					
			nameMulti = Lungo.Core.toArray nameMultiResult
			nameMulti = Lungo.Core.orderByProperty nameMulti, "mulLanName", "asc"
			lang = Lungo.Core.findByProperty nameMulti, "mulLanCode", langApp
			nameMulti = changeArrayToFirst nameMulti, lang

			@proRate.val parseFloat(product.proRate).toFixed(2)
			
		else
			
			nameMulti = new Array()
			i = -1
			for localLang in localLangs
				i++
				nameMultiText = {mulLanName:localLang.lanName,mulLanCode:localLang.lanCode}
				nameMulti[i] = nameMultiText
			@proRate.val ""
							
		@proNameInputContainer.empty()
		for nameMultiText in nameMulti
			if !product || (product && Lungo.Core.findByProperty localLangs, "lanCode", nameMultiText.mulLanCode)
				view = new __View.ProductNameView model:nameMultiText
				view.append nameMultiText
		
	
	onSave: (event) -> 
		#console.log "onSave"
		if (this.validateForm())
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"
			product = __FacadeCore.Cache_get appName + "productNew"
			local = __FacadeCore.Cache_get appName + "local"
			data = {localId:local.id,proRate:@proRate.val()}
			proNameInput = $$ "#product-name input[type=text]"
			proNameInput.each () -> 
					eval("data."+$$(this)[0].id+" = '"+$$(this).val()+"'")
			if product
	    		data.id = product.proId
	    	url = "http://"+appHost+"/product/manager/new"
	    	_this = this
	    	$$.put url, data, (response) ->
	    			Lungo.Notification.success findLangTextElement("label.notification.salvedData.title"), findLangTextElement("label.notification.salvedData.text"), null, 3, () ->
	    					__FacadeCore.Router_article "booking", "list-product"
	    					_this.resetArticle()
	    					

	validateForm: () -> 
		#console.log "validateForm"
		result = true
		@proNameError.html ""
		@proRateError.html ""
		proNameInput = $$ "#product-name input[type=text]"
		_this = this
		proNameInput.each () -> 
				if (result && !$$(this)[0].checkValidity())
					_this.proNameError.html getMessageValidity($$(this)[0])
					$$(this)[0].focus()
					result = false
		if (result && !checkValidity(@proRate.val(),@proRate.attr("pattern"),@proRate.attr("required")))
			@proRateError.html getMessageValidity(@proRate[0])
			@proRate[0].focus()
			result = false	
		result

	onCancel: (event) -> 
		if  (@productForm.hasClass("active"))
			#console.log "onCancel"
			__FacadeCore.Cache_remove appName + "elementSave"
			__FacadeCore.Cache_remove appName + "elementCancel"
			__FacadeCore.Cache_set appName + "elementSave",@buttonSave.html()
			__FacadeCore.Cache_set appName + "elementCancel",@buttonCancel.html()
			Lungo.Element.loading @buttonSave.selector, "black"
			Lungo.Element.loading @buttonCancel.selector, "black"
			__FacadeCore.Router_article "booking", "list-product"
			this.resetArticle()
		
	resetArticle: () -> 
		#console.log "resetArticle"
		@buttonSave.html __FacadeCore.Cache_get appName + "elementSave"
		@buttonCancel.html __FacadeCore.Cache_get appName + "elementCancel"
		@proNameError.html ""
		@proRateError.html ""
		__FacadeCore.Cache_remove appName+"productNew"
		
		
__Controller.ProductNew = new ProductNewCtrl "section#newProduct"
