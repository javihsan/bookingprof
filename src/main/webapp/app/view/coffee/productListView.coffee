class __View.ProductListView extends Monocle.View
    
    container: "section#booking article#list-product ul"
    
    template_url: "/app/templates/productList.mustache"
    
    events:
        "singleTap li": "loadProduct"
        "swiping li": "onSwiping"
    
    loadProduct: (event) ->
    	#console.log "loadProduct", @model
    	if __FacadeCore.isSwipeLeft event, true
    		this.onRemove event
    	else
	    	__FacadeCore.Cache_remove appName + "productNew"
	    	__FacadeCore.Cache_set appName + "productNew", @model
	    	__FacadeCore.Router_article "newProduct", "product-form"
   
    onSwiping: (event) ->
    	#console.log "onSwiping"
    	event.preventDefault()
    	if __FacadeCore.isSwipeLeft event
    		this.onRemove event
    
    onRemove: (event) ->
    	#console.log "onRemove", @model
    	if !@model.proDefault
	    	_this = this
		   	dataAccept = {icon: 'checkmark', label: 'Accept', callback: ()-> _this.removeConfirm(event) }
		    dataCancel = {icon: 'checkmark', label: 'Cancel', callback: ()-> {}}
		    dataConfirm = {icon: 'user', title: findLangTextElement("label.notification.deleteProduct.title"), description: findLangTextElement("label.notification.deleteProduct.text"), accept: dataAccept, cancel: dataCancel}
		    Lungo.Notification.confirm dataConfirm
   
   	removeConfirm: (event) ->
    	#console.log "removeConfirm", @model
    	url = "http://"+appHost+"/product/manager/remove"
    	data = {localId:@model.proLocalId, id:@model.proId}
    	_this = this
    	$$.put url, data, () ->
    			Lungo.Notification.success findLangTextElement("label.notification.deletedProduct.title"), findLangTextElement("label.notification.deletedProduct.text"), null, 3
    			_this.remove() 		
  	 
  
    