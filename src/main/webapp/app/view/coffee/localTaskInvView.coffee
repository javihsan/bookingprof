class __View.LocalTaskInvView extends Monocle.View
    
    container: "section#newInvoice article#invoiceTaskSelect-form ul"
    
    template_url: "/app/templates/localTaskInv.mustache"
   
    events:
        "singleTap li": "onSelect"
    
    onSelect: (event) ->
    	#console.log "onSelect", @model
    	
    	@model.updateAttributes enabled: if @model.enabled then false else true
    	@refresh()
    	
    	selectedTasksBilNum = __FacadeCore.Cache_get appName+"selectedTasksBilNum"
    	__FacadeCore.Cache_remove appName+"bilTaskId"+selectedTasksBilNum
    	__FacadeCore.Cache_set appName+"bilTaskId"+selectedTasksBilNum, @model

    	__FacadeCore.Cache_remove appName+"selectedTasksBilType"
    	__FacadeCore.Router_article "newInvoice","invoice-form"
		

   	