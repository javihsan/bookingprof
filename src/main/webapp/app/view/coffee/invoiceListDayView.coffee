class __View.InvoiceListDayView extends Monocle.View
    
	container: "section#booking article#list-invoices ul"
	
	template_url: "/app/templates/invoicesListDay.mustache"
	
	events:
        "singleTap li": "onTap"
        
    onTap: (event) ->
    	#console.log "InvoiceListDayView", @model
    	
    	selectedDate = dateToString(new Date(@model.invTime))
    	
    	__FacadeCore.Cache_remove appName+"selectedDateDiary"
    	__FacadeCore.Cache_set appName+"selectedDateDiary", selectedDate
    	
    	__Controller.InvoiceList.loadListInvoicesWeek(event)
	
	 