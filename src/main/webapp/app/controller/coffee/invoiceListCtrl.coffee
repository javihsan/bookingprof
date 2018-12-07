class InvoiceListCtrl extends Monocle.Controller
	
	result = null
	
	events:
		"load article#list-invoices" : "loadListInvoices"
		"singleTap a[data-action=new]" : "addInvoice"
			
	elements:
		"#list-invoices": "listInvoices"
		"header a[href=\\#]" : "header"
		"a[data-action=new]" : "new"
		
	setResult: (res) -> 
		#console.log "setResult", result, res
		result = null
	
	onPrevius: (event) -> 
		#console.log "onPrevius"
		this.change(event,-1)	
	
	onNext: (event) -> 
		#console.log "onNext"
		this.change(event,1)
	
	change: (event,delta) -> 
		#console.log "change"
		
		selectedDate = __FacadeCore.Cache_get appName+"selectedDateDiary"
		a = selectedDate.split('-')
		newDayAux = new Date(a[0],(a[1]-1),a[2])
			
		oneWeek = 1000 * 60 * 60 * 24 * 7
		newDayAux.setTime newDayAux.getTime() + (delta*oneWeek)
		
		selectedDate = dateToString(newDayAux)
		
		__FacadeCore.Cache_remove appName+"selectedDateDiary"
		__FacadeCore.Cache_set appName+"selectedDateDiary", selectedDate
		
		result = null
		
		this.loadListInvoicesWeek(event)	
	
	loadListInvoices: (event) ->
		#console.log "loadListInvoices"
		
		@header.hide()
		@new.show()
		
		selectedDate = __FacadeCore.Cache_get appName+"selectedDateDiary"
		if !selectedDate
			newDayAux = new Date()
			selectedDate =  dateToString(newDayAux)
			__FacadeCore.Cache_remove appName+"selectedDateDiary"
			__FacadeCore.Cache_set appName+"selectedDateDiary", selectedDate
		
		result = null
		
		this.loadListInvoicesWeek(event)
			
	loadListInvoicesWeek: (event) -> 
		#console.log "loadListInvoicesWeek"
		Lungo.Element.loading "#list-invoices ul", "black"
		
		selectedDate = __FacadeCore.Cache_get appName+"selectedDateDiary"
		a = selectedDate.split('-')
		newDay = new Date(a[0],(a[1]-1),a[2])
		# Calculamos el lunes de la semana a la que pertenece selectedDate
		dayWeek = newDay.getDay()
		if (dayWeek==0)
			dayWeek = 6
		else
			dayWeek = dayWeek-1
		oneDay = 1000 * 60 * 60 * 24
		dayFirstWeek = new Date()
		dayFirstWeek.setTime(newDay.getTime() - (dayWeek*oneDay));
		if !result
			
			selectedDateFirstWeek =  dateToString(dayFirstWeek)
							
			url = "http://"+appHost+"/invoice/operator/listByDiary"
			local = __FacadeCore.Cache_get appName+"local"
			data = {localId:local.id,selectedDate:selectedDateFirstWeek.toString()}
			_this = this
			$$.json url, data, (response) -> 
				_this.showListInvoices response,newDay,dayFirstWeek
		else
			this.showListInvoices null,newDay,dayFirstWeek
			
			
	showListInvoices: (response,daySelect,dayFirstWeek) -> 
		#console.log "showListInvoices"
		local = __FacadeCore.Cache_get appName + "local"
		if !result
			result = Lungo.Core.toArray response
			result = Lungo.Core.orderByProperty result,"invTime","asc"
		#console.log "result", result
		
		# Calculamos las tarifas diarias
		dateDayAnt = ""
		invRateDay = 0
		ratesListDay = []
		i=0
		for invoiceAux in result
			dateHour = new Date(invoiceAux.invTime)
			dateDay = dateToStringFormat(dateHour)
			if (dateDay!=dateDayAnt && dateDayAnt!="")
				ratesListDay[i] = invRateDay
				invRateDay = 0
				i++
			invRateDay += invoiceAux.invRate
			dateDayAnt = dateDay
		ratesListDay[i] = invRateDay	
		
		@listInvoices.children().empty()
		textsTemplates = {displayUntil:findLangTextElement("label.template.displayUntil"),pressDisplayMore:findLangTextElement("label.template.pressDisplayMore")}
		invoiceMov = new __Model.Invoice
			eveDay: dateToStringFormat(dayFirstWeek),
			texts: textsTemplates
		view = new __View.InvoiceListFromView model:invoiceMov
		view.append invoiceMov
		
		dateDaySelect = dateToStringFormat(daySelect)
		dateDayAnt = ""
		textsTemplates = {pressDisplay:findLangTextElement("label.template.pressDisplay"),finished:findLangTextElement("label.template.finished"), currency:local.locWhere.wheCurrency}
		i=0
		for invoiceAux in result
			#console.log "invoiceAux", invoiceAux
			dateHour = new Date(invoiceAux.invTime)
			dateDay = dateToStringFormat(dateHour)
			invoice = new __Model.Invoice 
				invId: invoiceAux.id,
				invTime: invoiceAux.invTime,
				eveDay: dateDay,
				eveHour: dateToStringHour(dateHour),
				invClient: invoiceAux.invClient, 
				invDesc: invoiceAux.invDesc,
				enabled: true,
				invRate: parseFloat(invoiceAux.invRate).toFixed(2),
				invBilleds: invoiceAux.invBilleds,
				texts: textsTemplates
			#console.log "invoice", invoice
			if (dateDay!=dateDayAnt)
				invoice.invRateDay = parseFloat(ratesListDay[i]).toFixed(2)
				view = new __View.InvoiceListDayView model:invoice
				view.append invoice
				i++
			if (dateDay==dateDaySelect)
				invoice.texts = textsTemplates
				view = new __View.InvoiceListView model:invoice
				view.append invoice
			dateDayAnt = dateDay
		
		oneDay = 1000 * 60 * 60 * 24;
		dayFirstWeek.setTime(dayFirstWeek.getTime() + (6*oneDay))
		textsTemplates = {displayFrom:findLangTextElement("label.template.displayFrom"),pressDisplayMore:findLangTextElement("label.template.pressDisplayMore")}
		invoiceMov = new __Model.Invoice
			eveDay: dateToStringFormat(dayFirstWeek),
			texts: textsTemplates
		view = new __View.InvoiceListUntilView model:invoiceMov
		view.append invoiceMov
	
	
	addInvoice: (event) ->
    	#console.log "addInvoice"
    	__FacadeCore.Router_section "newInvoice"	
	
						
__Controller.InvoiceList = new InvoiceListCtrl "section#booking"