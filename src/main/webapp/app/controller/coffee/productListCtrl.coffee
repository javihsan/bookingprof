class ProductListCtrl extends Monocle.Controller

	events:
		"load article#list-product" : "loadListProduct"
		"singleTap a[data-action=new]" : "newProduct"
		
	elements:
		"#list-product": "listProduct"
		"header a[href=\\#]" : "header"
		"header a[href=\\#menu]" : "aside"
		"a[data-action=new]": "buttonAdd"
		"footer a" : "footer"
	
	newProduct: (event) -> 
		if  (@listProduct.hasClass("active"))
			#console.log "newProduct"
			__FacadeCore.Router_article "newProduct", "product-form"
	
	loadListProduct: (event) -> 
		#console.log "loadListProduct"
		
		@header.hide()
		@aside.show()
		@buttonAdd.show()
		@footer.hide()
		
		Lungo.Element.loading "#list-product ul", "black"
		
		url = "http://"+appHost+"/product/operator/list"
		local = __FacadeCore.Cache_get appName + "local"
		data = {localId:local.id}
		_this = this
		$$.json url, data, (response) -> 
			_this.showProduct response
		
		
	showProduct: (response) -> 
		#console.log "showProduct"
		if (response.length>0)
			result = Lungo.Core.toArray response
			result = Lungo.Core.orderByProperty result, "proName", "asc"
			@listProduct.children().empty()
			
			texts = {cabText:findLangTextElement("label.aside.products")}
			view = new __View.ListCabView model:texts, container:"section#booking article#list-product ul"
			view.append texts
			
			local = __FacadeCore.Cache_get appName + "local"
			textsTemplates = {currency:local.locWhere.wheCurrency}
			for productAux in result
				#console.log "productAux",productAux
				proRate = productAux.proRate
				product = new __Model.Product
					enabled: true,
					proId: productAux.id,
					proLocalId: productAux.proLocalId,
					proNameMulti: productAux.proNameMulti,
					proRate: parseFloat(proRate).toFixed(2),
					proName: productAux.proName,
					texts: textsTemplates
				#console.log "product",product	
				view = new __View.ProductListView model:product
				view.append product
		else
			@listProduct.children().empty()
			texts = {cabText:findLangTextElement("label.aside.products")}
			view = new __View.ListCabView model:texts, container:"section#booking article#list-product ul"
			view.append texts
			Lungo.Notification.success findLangTextElement("label.notification.noData.title"), findLangTextElement("label.notification.noData.text"), null, 3
	
__Controller.ProductList = new ProductListCtrl "section#booking"