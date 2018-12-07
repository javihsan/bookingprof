class ReportApoCtrl extends Monocle.Controller
	
	events:
		"load article#report-apo" : "loadReport"
		"singleTap a[data-action=send]" : "onSend"
		"singleTap #report-apo #report-range" : "loadRange"
			
	elements:
		"header a[href=\\#]" : "header"
		"header a[href=\\#menu]" : "aside"
		"footer a" : "footer"
		"a[data-action=send]": "buttonSend"
		"#report-apo" : "artReport"
		"#report-range" : "reportRangeTimes"
		"#report-apo #fin-table" : "reportFinTable"
		"#report-apo #fin-pie" : "reportFinPie"
		"#report-apo #booking-table" : "reportBookingTable"
		"#report-apo #booking-pie" : "reportBookingPie"
		"#report-apo #task-table" : "reportTaskTable"
		"#report-apo #task-column1" : "reportTaskColumn1"
		
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
			url = "http://"+appHost+"/report/manager/apo/all"
			local = __FacadeCore.Cache_get appName + "local"
			data = {localId:local.id,selectedDateStart:selectedRangeTimes.start,selectedDateEnd:selectedRangeTimes.end}
			_this = this
			$$.json url, data, (responseAll) ->
				url = "http://"+appHost+"/report/manager/apo/booking"
				$$.json url, data, (responseBooking) -> 
					url = "http://"+appHost+"/report/manager/apo/task"
					$$.json url, data, (responseTask) ->
						_this.showReport responseAll, responseBooking, responseTask
		else
			@reportRangeTimes.html ""
			Lungo.Notification.success findLangTextElement("report.notification.selectRange.title"), findLangTextElement("report.notification.selectRange.text"), null, 3, (response) ->
					__FacadeCore.Router_article "rangeSelect","range-form"
		
	
	showReport: (responseAll, responseBooking, responseTask) -> 
		#console.log "showReport",responseAll, responseBooking, responseTask
		
		local = __FacadeCore.Cache_get appName + "local"
		
		data = new google.visualization.DataTable responseAll
		total = responseAll.rows[0].c[1].v
		
		chartTable = new google.visualization.Table @reportFinTable[0]
		chartTable.draw data, {sortColumn: 1, sortAscending: false}

		view = new google.visualization.DataView data
		view.hideRows([0])
		strCab = findLangTextElement "report.apo.allPie.cab"
		strCab += ": "+total
		chartPie = new google.visualization.PieChart @reportFinPie[0]
		chartPie.draw view, {title:strCab, backgroundColor: "#f4f5f0"}		
				
		data = new google.visualization.DataTable responseBooking
		data.sort [{column: 1, desc:true}, {column: 0}]
		total = responseBooking.rows[0].c[1].v
		
		chartTable = new google.visualization.Table @reportBookingTable[0]
		chartTable.draw data

		view = new google.visualization.DataView data
		view.hideRows([0])
		strCab = findLangTextElement "report.apo.allPie.cab"
		strCab += ": "+total
		chartPie = new google.visualization.PieChart @reportBookingPie[0]
		chartPie.draw view, {title:strCab, backgroundColor: "#f4f5f0"}	
		
		data = new google.visualization.DataTable responseTask
		chartTable = new google.visualization.Table @reportTaskTable[0]
		chartTable.draw data, {sortColumn: 1, sortAscending: false}
		total = responseTask.rows[0].c[1].v
		
		view = new google.visualization.DataView data
		view.hideRows([0])
		strCab = findLangTextElement "report.apo.workedPie"
		strCab += ": "+total
		chartColumn = new google.visualization.ColumnChart @reportTaskColumn1[0]
		chartColumn.draw view, {title:strCab, backgroundColor: "#f4f5f0", colors: ["#2f886b"]}
			
	onSend : (event) -> 
		#console.log "onSend"
		url = "http://"+appHost+"/report/manager/send"
		selectedRangeTimes = __FacadeCore.Cache_get appName+"selectedRangeTimes"
		content = @reportFinTable.html()+@reportBookingTable.html()+reportTaskTable.html()
		local = __FacadeCore.Cache_get appName + "local"
		data = {localId:local.id,selectedDateStart:selectedRangeTimes.start,selectedDateEnd:selectedRangeTimes.end, content: content}
		$$.post url, data, () ->
			Lungo.Notification.success findLangTextElement("report.notification.mail.send.title"), findLangTextElement("report.notification.mail.send.text"), null, 3

							
__Controller.ReportApo = new ReportApoCtrl "section#booking"