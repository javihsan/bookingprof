var annualsBooking = function(){

	this.elementId = null;
    this.annuals = null;
    this.weekDaysClosed = null;
    this.defaults = {
					date	: new Date(),
					openDays: 20,
					calendar: null,
					weekstart: 1,
                    num_days: 7*6
			};
    this.settings = null;
   

	this.create = function(pElementId, pWeekDaysClosed, pOptions){
		this.elementId = pElementId;
	    this.weekDaysClosed = pWeekDaysClosed;

	    this.settings = $$.extend({}, this.defaults, pOptions);
	    
		asyn = __FacadeCore.Service_Settings_asyncFalse();
		
		selectedDate = dateToString(this.settings.date);
		//console.log ("this.settings.calendar", this.settings.calendar);
		if (this.settings.calendar){
			url = "http://"+appHost+"/annual/listCalendarByMonth";
			data = {id:this.settings.calendar,selectedDate:selectedDate.toString()};
		} else {
			url = "http://"+appHost+"/annual/listByMonth";
			local = __FacadeCore.Cache_get(appName + "local");	
			data = {localId:local.id,selectedDate:selectedDate.toString()};
		}
		pAnnuals = Lungo.Core.toArray($$.json(url, data));
		
		__FacadeCore.Service_Settings_async(asyn);
		
	    this.annuals = pAnnuals;
	    
	    this.markup();
	    this.setToday();
	    this.loadAnnuals();
	    this.setBindings();
	}
		
	//PRIVATES
	this.markup = function(){
		this.generateCalendar(this.settings.date.getMonth(), this.settings.date.getFullYear());
	}
	
	this.generateCalendar = function(month, year) {
		//console.log('month-year', month+"-"+year);
		return this.monthMarkup(month, year);
	}
	
	this.monthMarkup= function(month, year) {
	
		var c = new Date();
		c.setDate(1);c.setMonth(month);c.setFullYear(year);
		var x = parseInt(this.settings.weekstart,10);
		var s = (c.getDay()-x)%7;
		if (s<0) { s+=7; }
		var month_m = month+1;
		
		var this_month = this.elementId.find('table');
	    //console.log('this_month',this_month);
		this_month.data('month',month_m);
		this_month.data('year',year);
		
		var mountYearElem = this.elementId.find(".month-year")
		mountYearElem.html(eval(findLangTextElement("general.months"))[month] + ' ' + year);
		
		var dayElem = null;
		var d = 0;
		_this = this;
		this.elementId.find('td').each(

		function() {
			d++;
			
			dayElem = $$(this);
			dayElem.removeClass();
			
			// Add this month
			var this_m = month_m;
			var this_y = year;
			var this_dm = _this.monthLength(this_m,this_y);
			var this_d = d - s;
			var this_f = 1;
	
			if (d <= s) { // Add remaining days from previous month
				this_m = ((month+11) % 12)+1;
				this_y = month - 1 < 0 ? year - 1 : year;
				this_dm = _this.monthLength(this_m,this_y);
				this_d = this_d + this_dm;
				this_f = 0;
			} else if (this_d > this_dm) { // Add start of next month
				this_m = ((month+1) % 12)+1;
				this_y = (month + 1) > 11 ? year + 1 : year;
				this_d = this_d - this_dm;
				this_f = 9;
			}
			this_col =  (d % 7)-1 < 0 ? 6 : (d % 7)-1;
			today = new Date();
			today.setHours(0);
			today.setMinutes(0);
			today.setSeconds(0);
			today.setMilliseconds(0)
			oneDay = 1000 * 60 * 60 * 24;
			maxDate = new Date();
			maxDate.setTime(today.getTime() + ((_this.settings.openDays-1)*oneDay) );
			_this.dayMarkup(dayElem, this_f, this_d, this_m, this_y, this_col,today,maxDate)
		})
		
		return this_month;
	}
	
	this.dayMarkup = function(this_day,format,day,month,year,column,today,maxDate) {

		if ( format == 0 ) {
			this_day.addClass('prevmonth');
		} else if ( format == 9 ) {
			this_day.addClass('nextmonth');
		}

		for (i=0;i<this.weekDaysClosed.length;i++){
			if (column==this.weekDaysClosed[i]) {
				this_day.addClass('date_closed');
			}
		}
		dd = new Date(year, (month-1), day);
		if (dd<today || dd>maxDate){
			this_day.addClass('date_not_enabled');
		}
		
		this_day.attr('datetime',year+'-'+ month+'-'+day);
		this_day.html(day);
		return this_day;
    }
	
	this.monthLength = function(month, year) {
		var dd = new Date(year, month, 0);
		return dd.getDate();
	}
	
	this.setToday = function() {
		var date = new Date();
		var obj = this.elementId.find("td[datetime='"+date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+"']")
		obj.addClass('today');
    }
    
    this.setBindings = function() {
		var _this = this;
				
		// load previous Month
		var prevElem = this.elementId.find("thead tr:first-child th:first-child");
		prevElem.unbind();
		prevElem.bind("singleTap", function() { 
			_this.loadMonthDelta(-1); 
		});
		prevElem = this.elementId.find("thead tr:last-child th:first-child");
		prevElem.unbind();
		prevElem.bind("singleTap", function() { 
			_this.loadMonthDelta(-1); 
		});
		// load next Month
		var nextElem = this.elementId.find("thead tr:first-child th:last-child");
		nextElem.unbind();
		nextElem.bind("singleTap", function() { 
			_this.loadMonthDelta(1); 
		});
		nextElem = this.elementId.find("thead tr:last-child th:last-child");
		nextElem.unbind();
		nextElem.bind("singleTap", function() { 
			_this.loadMonthDelta(1); 
		});
		// load previous-next Month Swipe Down-Top
		var div_month = this.elementId.find('div');
		div_month.unbind();
		div_month.bind("swipeDown", function() { 
		   	_this.loadMonthDelta(-1); 
		});
		div_month.bind("swipeUp", function() { 
			_this.loadMonthDelta(1); 
		});
	}
	
	this.sameDay = function(date1, date2) {
		return (date1.getDate && date2.getDate) && date1.getDate() == date2.getDate() && date1.getMonth() == date2.getMonth() && date1.getFullYear() == date2.getFullYear()
	}
	
	this.loadMonthDelta = function(delta) {
		var day = 1;
        var month = this.elementId.find('table').data('month');
        var year = this.elementId.find('table').data('year');
        var newDay = new Date(year, (month-1)+delta, day);
        create(this.elementId, this.weekDaysClosed, {date:newDay,openDays:this.settings.openDays});
	}
		
	this.loadAnnuals = function() {
		_this = this;
		if (this.annuals.length > 0){
			_this.elementId.find('td').each( 
    	    		function() { 
    	    			__this = this;
    	    			isClosed = 0;
    	    			$$.each(_this.annuals,		
    	    				function(){ 
    	    					day = new Date(this.anuDate);
    	    					strDay = dateToString(day)
    	    					if ($$(__this).attr('datetime')==strDay){ 
    	    						isClosed = this.anuClosed;
        	    					if (isClosed==1){ 
    	    							$$(__this).addClass('date_closed');
    	    						} else if (isClosed==0){ 
    	    							$$(__this).removeClass('date_closed');
    	    							dayDiary = this.anuDayDiary;
    	    							if (dayDiary && dayDiary!=""){
    	    								$$(__this).addClass('date_diary');
    	    							}
    	    						}
    	    					}
    	    				}
    	    			);
            		}
    	    );
	    }
	}
    
   
}();

