class ReportSalesCtrl extends Monocle.Controller
	
	events:
		"load article#report-sales" : "loadReport"
		"singleTap a[data-action=send]" : "onSend"
		"singleTap #report-sales #report-range" : "loadRange"
			
	elements:
		"header a[href=\\#]" : "header"
		"header a[href=\\#menu]" : "aside"
		"footer a" : "footer"
		"a[data-action=send]": "buttonSend"
		"#report-sales" : "artReport"
		"#report-range" : "reportRangeTimes"
		"#report-sales #all-table" : "reportAllTable"
		"#report-sales #all-pie" : "reportAllPie"
		"#report-sales #task-table" : "reportTaskTable"
		"#report-sales #task-column1" : "reportTaskColumn1"
		"#report-sales #product-table" : "reportProductTable"
		"#report-sales #product-column1" : "reportProductColumn1"
		
	loadRange: (event) -> 
		#console.log "loadRange"
		__FacadeCore.Router_article "rangeSelect","range-form"
			
	loadReport: (event) -> 
		#console.log "loadReport"
		
		@header.show()
		@aside.show()
		@footer.show()
		
		selectedRangeTimes = __FacadeCore.Cache_get appName+"selectedRangeTimes"
		#console.log "selectedRangeTimes",selectedRangeTimes
		if selectedRangeTimes
			strRange = dateToStringFormat(stringToDate(selectedRangeTimes.start)) + " - " + dateToStringFormat(stringToDate(selectedRangeTimes.end))
			strRange += ". "+findLangTextElement("label.html.apoFor3")+"."
			@reportRangeTimes.html strRange
			url = "http://"+appHost+"/report/manager/sales/all"
			local = __FacadeCore.Cache_get appName + "local"
			data = {localId:local.id,selectedDateStart:selectedRangeTimes.start,selectedDateEnd:selectedRangeTimes.end}
			_this = this
			$$.json url, data, (responseAll) -> 
				url = "http://"+appHost+"/report/manager/sales/task"
				$$.json url, data, (responseTask) -> 
					url = "http://"+appHost+"/report/manager/sales/product"
					$$.json url, data, (responseProduct) -> 
						_this.showReport responseAll, responseTask, responseProduct
		else
			@reportRangeTimes.html ""
			Lungo.Notification.success findLangTextElement("report.notification.selectRange.title"), findLangTextElement("report.notification.selectRange.text"), null, 3, (response) ->
					__FacadeCore.Router_article "rangeSelect","range-form"
		
	
	showReport: (responseAll, responseTask, responseProduct) -> 
		#console.log "showReport", responseAll, responseTask, responseProduct
		
		local = __FacadeCore.Cache_get appName + "local"
		
		data = new google.visualization.DataTable responseAll
		formatter = new google.visualization.NumberFormat {suffix: " "+local.locWhere.wheCurrency}
		formatter.format data, 1
		total = responseAll.rows[0].c[1].v
		
		chartTable = new google.visualization.Table @reportAllTable[0]
		chartTable.draw data, {sortColumn: 1, sortAscending: false}

		view = new google.visualization.DataView data
		view.hideRows([0])
		strCab = findLangTextElement "report.sales.totalAll.cab"
		strCab += ": "+total
		chartPie = new google.visualization.PieChart @reportAllPie[0]
		chartPie.draw view, {title:strCab, backgroundColor: "#f4f5f0"}	
		
		data = new google.visualization.DataTable responseTask
		formatter.format data, 1
		data.sort [{column: 1, desc:true}, {column: 0}]
		chartTable = new google.visualization.Table @reportTaskTable[0]
		chartTable.draw data
		total = responseTask.rows[0].c[1].v
				
		view = new google.visualization.DataView data
		view.hideRows([0])
		strCab = findLangTextElement "report.sales.totalTask.cab"
		strCab += ": "+total+" "+local.locWhere.wheCurrency
		chartColumn = new google.visualization.ColumnChart @reportTaskColumn1[0]
		chartColumn.draw view, {title:strCab, backgroundColor: "#f4f5f0", colors: ["#2f886b"]}
		
		data = new google.visualization.DataTable responseProduct
		data.sort [{column: 1, desc:true}, {column: 0}]
		formatter.format data, 1
		chartTable = new google.visualization.Table @reportProductTable[0]
		chartTable.draw data
		total = responseProduct.rows[0].c[1].v
				
		view = new google.visualization.DataView data
		view.hideRows([0])
		strCab = findLangTextElement "report.sales.totalProduct.cab"
		strCab += ": "+total+" "+local.locWhere.wheCurrency
		chartColumn = new google.visualization.ColumnChart @reportProductColumn1[0]
		chartColumn.draw view, {title:strCab, backgroundColor: "#f4f5f0", colors: ["#2f886b"]}
			
	onSend : (event) -> 
		#console.log "onSend"
		url = "http://"+appHost+"/report/manager/send"
		selectedRangeTimes = __FacadeCore.Cache_get appName+"selectedRangeTimes"
		content = @reportTaskTable.html()+@reportProductTable.html()
		local = __FacadeCore.Cache_get appName + "local"
		data = {localId:local.id,selectedDateStart:selectedRangeTimes.start,selectedDateEnd:selectedRangeTimes.end, content: content}
		$$.post url, data, () ->
			Lungo.Notification.success findLangTextElement("report.notification.mail.send.title"), findLangTextElement("report.notification.mail.send.text"), null, 3

							
__Controller.ReportSales = new ReportSalesCtrl "section#booking"