class __View.BilledView extends Monocle.View
    
	container: "section#newInvoice article#invoice-form ul"
	
	template_url: "/app/templates/billed.mustache"
	
	events:
        "singleTap div[data-id=divBilTaskId]": "onTasksInv"
        "singleTap button": "onRemove"
    
    
    onTasksInv: (event) ->
    	#console.log "onTasksInv", @model
    	
    	__Controller.InvoiceNew.conserveData = true
    	
    	__FacadeCore.Cache_remove appName+"selectedTasksBilNum"
    	__FacadeCore.Cache_set appName+"selectedTasksBilNum", @model.bilId
    	
    	objSelect = Lungo.dom "#invoice-form #bilTaskId"+@model.bilId
    	type = 0
    	if objSelect.attr "data-product"
    		type = 1
    	__FacadeCore.Cache_remove appName+"selectedTasksBilType"
    	__FacadeCore.Cache_set appName+"selectedTasksBilType", type
    	
    	__FacadeCore.Router_article "newInvoice","invoiceTaskSelect-form"
	
	onRemove: (event) ->
    	#console.log "onRemove", @model
    	@remove()
    	__FacadeCore.Cache_remove appName+"bilTaskId"+@model.bilId
    	__Controller.InvoiceNew.changeRate event
  	  	
	
	 