class __View.InvoiceListUntilView extends Monocle.View
    
	container: "section#booking article#list-invoices ul"
	
	template_url: "/app/templates/eventsListUntil.mustache"
	
	
	events:
        "singleTap li": "onTap"
        
    onTap: (event) ->
    	#console.log "InvoiceListUntilView", @model
    	__Controller.InvoiceList.onNext(event) 