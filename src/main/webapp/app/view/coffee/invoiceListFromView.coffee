class __View.InvoiceListFromView extends Monocle.View
    
	container: "section#booking article#list-invoices ul"
	
	template_url: "/app/templates/eventsListFrom.mustache"
	
	events:
        "singleTap li": "onTap"
        
    onTap: (event) ->
    	#console.log "InvoiceListFromView", @model
    	__Controller.InvoiceList.onPrevius(event)
	
	 