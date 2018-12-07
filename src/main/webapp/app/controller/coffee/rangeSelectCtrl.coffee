class RangeSelectCtrl extends Monocle.Controller
	
	events:
		"load article#range-form" : "onLoad"
		"singleTap a[data-action=save]" : "onSave"
		"singleTap a[data-action=cancel]" : "onCancel"
		"change #yearDate" : "changeYear"
		"change #monthMonthDate" : "changeMonth"
		"change #monthYearDate" : "changeMonth"
		"change #startDate" : "changeDate"
		"change #endDate" : "changeDate"		
			
	elements:
		"a[data-action=save]": "buttonSave"
		"a[data-action=cancel]": "buttonCancel"
		"#yearDate" : "yearDate"
		"#monthMonthDate" : "monthMonthDate"
		"#monthYearDate" : "monthYearDate"
		"#startDate" : "startDate"
		"#error_startDate" : "startDateError"
		"#endDate" : "endDate"
		"#error_endDate" : "endDateError"
	
	changeYear: (event) -> 
		#console.log "changeYear",@yearDate.val()
		@startDateError.html ""
		@endDateError.html ""
		if @yearDate.val() && @yearDate.val().length>0
			@monthMonthDate.val ""
			@monthYearDate.val ""
			@startDate.val @yearDate.val()+"-01-01"
			@endDate.val @yearDate.val()+"-12-31"
		else
			@startDate.val ""
			@endDate.val ""
	
	changeMonth: (event) -> 
		#console.log "changeMonth", @monthMonthDate.val(), @monthYearDate.val()
		@startDateError.html ""
		@endDateError.html ""
		if @monthMonthDate.val() && @monthYearDate.val().length>0 && @monthYearDate.val() && @monthYearDate.val().length>0
			strMonth = @monthMonthDate.val()
			if (parseInt(@monthMonthDate.val()) <= 9)
     			strMonth = "0" + strMonth
    		newDayAux = new Date @monthYearDate.val(), @monthMonthDate.val(), 0
			dayLength = newDayAux.getDate()
			@yearDate.val ""
			@startDate.val @monthYearDate.val()+"-"+strMonth+"-01"
			@endDate.val @monthYearDate.val()+"-"+strMonth+"-"+dayLength
		else
			@startDate.val ""
			@endDate.val ""
	
	changeDate: (event) -> 
		#console.log "changeDate"
		@startDateError.html ""
		@endDateError.html ""
		@yearDate.val ""
		@monthMonthDate.val ""
		@monthYearDate.val ""
		@startDate.val formatDate(@startDate.val())
		@endDate.val formatDate(@endDate.val())
		
	onLoad: (event) -> 
		#console.log "onLoad"

		selectedRangeTimes = __FacadeCore.Cache_get appName+"selectedRangeTimes"
		#console.log "selectedRangeTimes",selectedRangeTimes
		if selectedRangeTimes
			@startDate.val selectedRangeTimes.start
			@endDate.val selectedRangeTimes.end
		else
			@startDate.val ""
			@endDate.val ""
		
		today = new Date()
		year = today.getFullYear()
		
		i=1
		for y in [2013..year]
			@yearDate[0].options[i] = new Option y, y
			@monthYearDate[0].options[i] = new Option y, y
			i++
		
		for m in [1..12]
		 	strMonth = eval(findLangTextElement("general.months"))[m-1]
		 	@monthMonthDate[0].options[m] = new Option strMonth, m
			
	onSave: (event) -> 
		#console.log "onSave"		
		
		if (this.validateForm())

			selectedRangeTimes = {start:@startDate.val(),end:@endDate.val()}
 			
			__FacadeCore.Cache_remove appName+"selectedRangeTimes"
			__FacadeCore.Cache_set appName+"selectedRangeTimes", selectedRangeTimes
			
			firm = __FacadeCore.Cache_get appName + "firm"
			
			if firm.firBilledModule==0
			    __FacadeCore.Router_article "booking", "report-apo"
			else
			   __FacadeCore.Router_article "booking", "report-sales"
					
		
	validateForm: () -> 
		#console.log "validateForm"
		result = true
		@startDateError.html ""
		@endDateError.html ""
		if (!checkValidityDate(@startDate.val(),@startDate.attr("required")))
			@startDateError.html getMessageValidity(@startDate[0])
			@startDate[0].focus()
			result = false
		else if (!checkValidityDate(@endDate.val(),@endDate.attr("required")))
			@endDateError.html getMessageValidity(@endDate[0])
			@endDate[0].focus()
			result = false
		result
	
	onCancel: (event) -> 
		#console.log "cancel"	
		__FacadeCore.Router_article "booking", "report-sales"
		
		 
__Controller.RangeSelect = new RangeSelectCtrl "section#rangeSelect"
