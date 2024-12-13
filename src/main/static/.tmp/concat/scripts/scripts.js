
  var FacadeCore = function() {
    
	var cacheService = undefined;
	  
	function FacadeCore(cacheS) {
		cacheService = cacheS;
	}
	
	FacadeCore.prototype.Storage_set = function(name,value) {
		try{
			return window.localStorage.setItem(name,JSON.stringify(value));
		}catch(err)
		  {
			return this.Cache_set(name,value);
		  }
	};
	
	FacadeCore.prototype.Storage_get = function(name) {
		try{
			return JSON.parse(window.localStorage.getItem(name));
		} catch(err)
		  {
			return this.Cache_get(name);
		  }
	};
	
	FacadeCore.prototype.Storage_remove = function(name) {
		try{
			return window.localStorage.removeItem(name);
		} catch(err)
		  {
			return this.Cache_remove(name);
		  }
	};
	
	FacadeCore.prototype.Cache_set = function(name,value) {
		return cacheService.put(name,value);
	};
	
	FacadeCore.prototype.Cache_get = function(name) {
		return cacheService.get(name);
	};
	
	FacadeCore.prototype.Cache_remove = function(name) {
		return cacheService.remove(name);
	};
	
	FacadeCore.prototype.Router_section = function(section) {
      return Lungo.Router.section(section);
    };
    
	FacadeCore.prototype.Router_article = function(section,article) {
	   return Lungo.Router.article(section,article);
	};
	
	FacadeCore.prototype.Router_back = function() {
	   return Lungo.Router.back();
	};
    
	FacadeCore.prototype.Service_Settings_async = function(value) {
		return Lungo.Service.Settings.async = value;
	};
	
	FacadeCore.prototype.Service_Settings_asyncFalse = function() {
		asyn = Lungo.Service.Settings.async;
		this.Service_Settings_async(false);
		return asyn; 
	};
	
	FacadeCore.prototype.Service_Settings_timeout = function(value) {
		return Lungo.Service.Settings.timeout = value;
	};
	
	FacadeCore.prototype.isSwipeLeft = function(event, ignoreRange) {
		if (ignoreRange){
			return event.iniTouch && event.currentTouch && event.iniTouch.x > event.currentTouch.x;
		} else{
			return Math.abs(event.iniTouch.x - event.currentTouch.x)>30 && event.iniTouch.x > event.currentTouch.x;
		}	
	};
	
	FacadeCore.prototype.isSwipeRight = function(event, ignoreRange) {
		if (ignoreRange){
			return event.iniTouch && event.currentTouch && event.iniTouch.x < event.currentTouch.x;
		} else{
			return Math.abs(event.iniTouch.x - event.currentTouch.x)>30 && event.iniTouch.x < event.currentTouch.x;
		}
	};
	
	FacadeCore.prototype.isDoubleTap = function(event) {
		return !event.iniTouch || Math.abs(event.iniTouch.x - event.currentTouch.x)<30
	};
	
	FacadeCore.prototype.count = function(selector,count) {
		var element = Lungo.dom(selector);
		if (element){
			element.children(".tag.count").remove();
			if (count){
				var binding = Lungo.Constants.BINDING.SELECTOR;
				var html = Lungo.Attributes.count.html.replace(binding, count);
				element.append (html);
			}
		}	
	};
	
	
	return FacadeCore;

}();

__FacadeCore = undefined;


var Utils = function() {
	
	var scope = undefined;
	
	var daysWeekAbbr = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
	
	function Utils(scopeS) {
		scope = scopeS;
	}
	
	Utils.prototype.newDateTimezone = function() {
		newDate = new Date()
		newDate.setHours((newDate.getHours() - (newDate.getTimezoneOffset() / 60)));
		newDate.setMinutes(newDate.getMinutes());
	    return newDate;
	};
	
	Utils.prototype.dateToString = function(date) {
	  var day, month, year;
	
	  year = date.getFullYear();
	  month = date.getMonth() + 1;
	  day = date.getDate();
	  return year + "-" + month + "-" + day;
	};
	
	Utils.prototype.dateToStringFormat = function(date) {
	    var day, month, year;
	    year = date.getFullYear();
	    month = eval(scope.findLangTextElement("general.months"))[date.getMonth()]
	    day = (date.getDate()).toString();
	    if (parseInt(day) <= 9) {
	      day = "0" + day;
	    }
		dayWeek = date.getDay();
		if (dayWeek==0){
			dayWeek = 6;
		} else {
			dayWeek = dayWeek-1;
		}
	    strDayWeek = eval(scope.findLangTextElement("general.daysWeek"))[dayWeek]
	    return strDayWeek+ ", " + day + "-" + month + "-" + year;
	};
	
	Utils.prototype.dateToDayWeekMonthFormat = function(date) {
	    var day, month;
	    month = eval(scope.findLangTextElement("general.months"))[date.getMonth()]
	    day = (date.getDate()).toString();
	    if (parseInt(day) <= 9) {
	      day = "0" + day;
	    }
		dayWeek = date.getDay();
		if (dayWeek==0){
			dayWeek = 6;
		} else {
			dayWeek = dayWeek-1;
		}
	    strDayWeek = scope.findLangTextElement("general.daysWeek"+daysWeekAbbr[dayWeek]);
	    return strDayWeek+ ", " + day + " " + month;
	};
	
	Utils.prototype.dateToDayWeekDayMonthFormat = function(date) {
	    var day;
	    day = (date.getDate()).toString();
	    if (parseInt(day) <= 9) {
	      day = "0" + day;
	    }
		dayWeek = date.getDay();
		if (dayWeek==0){
			dayWeek = 6;
		} else {
			dayWeek = dayWeek-1;
		}
	    strDayWeek = scope.findLangTextElement("general.daysWeek"+daysWeekAbbr[dayWeek]);
	    return strDayWeek+ ", " + day;
	};
	
	Utils.prototype.dateToDayWeekFormat = function(date) {
		dayWeek = date.getDay();
		if (dayWeek==0){
			dayWeek = 6;
		} else {
			dayWeek = dayWeek-1;
		}
	    return scope.findLangTextElement("general.daysWeek"+daysWeekAbbr[dayWeek]);
	};
	
	Utils.prototype.dateToDayMonthFormat = function(date) {
	    var day, month;
	    month = eval(scope.findLangTextElement("general.months"))[date.getMonth()]
	    day = (date.getDate()).toString();
	    if (parseInt(day) <= 9) {
	      day = "0" + day;
	    }
	    return day + " " + month;
	};
	
	Utils.prototype.dateToStringSim = function(date, sym) {
	    var day, month, year;
	
	    year = date.getFullYear();
	    month = (date.getMonth() + 1).toString();
	    if (parseInt(month) <= 9) {
	    	month = "0" + month;
	    }
	    day = (date.getDate()).toString();
	    if (parseInt(day) <= 9) {
	        day = "0" + day;
	    }
	    return year + sym + month + sym + day;
	};
	
	Utils.prototype.dateToStringYearLast = function(date) {
	    var day, month, year;
	
	    year = date.getFullYear();
	    month = (date.getMonth() + 1).toString();
	    if (parseInt(month) <= 9) {
	    	month = "0" + month;
	    }
	    day = (date.getDate()).toString();
	    if (parseInt(day) <= 9) {
	        day = "0" + day;
	    }
	    return day + '-' + month + '-' + year;
	};
	
	Utils.prototype.stringToDate = function(strDate) {
		var a = strDate.split('-');
		var b = a[0].split(',');
		if (b.length==1){ // Formato 2024-11-28
			return new Date(a[0],(a[1]-1),a[2]);
		} else { // Formato Martes, 12-Noviembre-2024
			year = a[2];
			aMonth = eval(scope.findLangTextElement("general.months"));
			month = aMonth.indexOf (a[1]);
			day = b[1].trim();
			return new Date(year,month,day);
		}	
	}
	
	Utils.prototype.formatDate = function(strDate) {
		while (strDate.indexOf("/") != -1){
			strDate = strDate.replace("/", "-");
		}
		a = strDate.split('-');
		if (a.length==3){
			if (a[2].length==4){
				strDate = a[2]+"-"+a[1]+"-"+a[0];
			}
			a = strDate.split('-');	
			if (a[1].length==1){
				a[1] = "0"+a[1];
			}
			if (a[2].length==1){
				a[2] = "0"+a[2];
			}
			return strDate = a[0]+"-"+a[1]+"-"+a[2];
		}	
	};
	
	Utils.prototype.dateToStringHour = function(date) {
	    var hourStr, minStr;
	
	    hourStr = date.getUTCHours();
	    if (parseInt(hourStr) <= 9) {
	      hourStr = "0" + hourStr;
	    }
	    minStr = date.getUTCMinutes();
	    if (parseInt(minStr) <= 9) {
	      minStr = "0" + minStr;
	    }
	    return hourStr + ":" + minStr;
	};
	
	Utils.prototype.getSemDay = function(sem) {
		dayWeek = daysWeekAbbr.indexOf(sem);
	    return eval(scope.findLangTextElement("general.daysWeek"))[dayWeek]
	};
		
	Utils.prototype.checkValidity = function(str,pattern,required) {
		if (str==""){
			if (required!== null){
				return false;
			} else {
				return true;
			}
		}
		if (pattern== null){
			return true;
		}
		var result = false;
		checkMatch = str.match (pattern);
		if (checkMatch){
			if (checkMatch.indexOf(str)>-1){
				result = true;
			}
		}
		return result;
	};
	
	Utils.prototype.checkValidityDate = function(strDate,required) {
		if (strDate==""){
			if (required!== null){
				return false;
			} else {
				return true;
			}
		}
		var result = false;
		var timestamp=Date.parse(strDate);
		if (isNaN(timestamp)==false){
			result = true;
		}
		return result;
	};
	
	Utils.prototype.changeArrayToFirst = function(array,obj) {
		posObj = array.indexOf (obj);
		array.splice (posObj, 1);
		array.unshift (obj);
		return array;
	};
	
	Utils.prototype.arrHasDupes = function ( A ) {                      
		var i, j, n;
		n=A.length;
	                                                    
		for (i=0; i<n; i++) {                       
			for (j=i+1; j<n; j++) {
				if (A[i]==A[j]) return A[i];
		}	}
		return null;
	};
	
	
	Utils.prototype.getStrDiary = function (diary) {                      
		if (diary==null || diary.diaTimes==null){
			return '<div class="tag cancel">'+scope.findLangTextElement("form.closed")+'</div>';
		}
		result = "";
		diaTimes = diary.diaTimes;
		startTime = true;
		resultTime = null;
		for (h=0;h<diaTimes.length;h++){	
			date = diaTimes[h];
			if (startTime){
				resultTime = date +" - ";
				startTime = false;
			} else {
				if (result.length>0){
					result += " , ";
				}
				resultTime += date;	
				startTime = true;
				result += resultTime;
			}
		}
		return result;
	};
	
	Utils.prototype.sortByPropChar = function (array, prop, asc){
	
		var sortFun = function (a, b){
		  var aName = eval("a."+prop+".toLowerCase()");
		  var bName = eval("b."+prop+".toLowerCase()");
		  if (asc){
			  return ((aName > bName) ? -1 : ((aName < bName) ? 1 : 0));
		  } else {
			  return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
		  }		  
		}
		return array.sort(sortFun);
	};	
	
	Utils.prototype.findByProp = function (array, prop, key){
		//console.log ("findByProp", array, prop, key);
		var matches = $.grep(array, function(elem) {
		    return(eval("elem."+prop) == key);
		});
		if (matches.length>0) {
			return matches[0];
		} else return null;
	};
	
	return Utils;
	
}();

__Utils = undefined;

var annualsBooking = function(){

	this.httpService  = null;
	this.elementId = null;
    this.weekDaysClosed = null;
    this.defaults = {
					date	: new Date(),
					openDays: 20,
					calendar: null,
					weekstart: 1,
                    num_days: 7*6
			};
    this.settings = null;
   

	this.create = function(scope, httpService, pElementId, pWeekDaysClosed, pOptions){
		this.scope  = scope;
		this.httpService  = httpService;
		this.elementId = pElementId;
	    this.weekDaysClosed = pWeekDaysClosed;

	    this.settings = $.extend({}, this.defaults, pOptions);
		
		selectedDate = __Utils.dateToString(this.settings.date);
		//console.log ("this.settings.calendar", this.settings.calendar);
		if (this.settings.calendar){
			url = protocol_url+appHost+"/annual/listCalendarByMonth";
			data = {id:this.settings.calendar,selectedDate:selectedDate.toString()};
		} else {
			url = protocol_url+appHost+"/annual/listByMonth";
			data = {localId:scope.local.id,selectedDate:selectedDate.toString()};
		}
		var promiseAnnuals = httpService.GET(url,data);
		promiseAnnuals.then(function (response) {
			this.scope.annuals = response.data;
			this.markup();
		    this.setsaveSearch();
		    this.loadAnnuals();
     	});
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
		
		var this_month = this.elementId.find('#tableDates');
	    this_month.data('month',month_m);
		this_month.data('year',year);

		var mountYearElem = this.elementId.find(".month-year");
		mountYearElem.html(eval(scope.findLangTextElement("general.months"))[month] + ' ' + year);
		
		var dayElem = null;
		var d = 0;
		_this = this;
		this.elementId.find('md-grid-tile span#date').each(

		function() {
			d++;
			
			dayElem = $(this);
			dayElem.removeClass();
			dayElem.parent().removeClass();
			
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
			saveSearch = new Date();
			saveSearch.setHours(0);
			saveSearch.setMinutes(0);
			saveSearch.setSeconds(0);
			saveSearch.setMilliseconds(0)
			oneDay = 1000 * 60 * 60 * 24;
			maxDate = new Date();
			maxDate.setTime(saveSearch.getTime() + ((_this.settings.openDays-1)*oneDay) );
			_this.dayMarkup(dayElem, this_f, this_d, this_m, this_y, this_col,saveSearch,maxDate)
		})

		return this_month;
	}
	
	this.dayMarkup = function(this_day,format,day,month,year,column,saveSearch,maxDate) {
		
		if ( format == 0 ) {
			//this_day.addClass('prevmonth');
			this_day.parent().addClass('prevmonth');
		} else if ( format == 9 ) {
			//this_day.addClass('nextmonth');
			this_day.parent().addClass('nextmonth');
		}

		for (i=0;i<this.weekDaysClosed.length;i++){
			if (column==this.weekDaysClosed[i]) {
				//this_day.addClass('date_closed');
				this_day.parent().addClass('date_closed');
			}
		}
		dd = new Date(year, (month-1), day);
		if (dd<saveSearch || dd>maxDate){
			//this_day.addClass('date_not_enabled');
			this_day.parent().addClass('date_not_enabled');
		}
		
		this_day.attr('datetime',year+'-'+ month+'-'+day);
		this_day.html(day);
		return this_day;
    }
	
	this.monthLength = function(month, year) {
		var dd = new Date(year, month, 0);
		return dd.getDate();
	}
	
	this.setsaveSearch = function() {
		var date = new Date();
		var obj = this.elementId.find("span[datetime='"+date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+"']")
		obj.addClass('saveSearch');
    }
   	
	this.sameDay = function(date1, date2) {
		return (date1.getDate && date2.getDate) && date1.getDate() == date2.getDate() && date1.getMonth() == date2.getMonth() && date1.getFullYear() == date2.getFullYear()
	}
	
	this.loadMonthDelta = function(delta) {
		var day = 1;
        var month = this.elementId.find('#tableDates').data('month');
        var year = this.elementId.find('#tableDates').data('year');
        var newDay = new Date(year, (month-1)+delta, day);
        create(this.scope, this.httpService, this.elementId, this.weekDaysClosed, {date:newDay,openDays:this.settings.openDays});
	}
		
	this.loadAnnuals = function() {
		_this = this;
		if (this.scope.annuals.length > 0){
			_this.elementId.find('md-grid-tile span#date').each( 
    	    		function() { 
    	    			__this = this;
    	    			isClosed = 0;
    	    			$.each(_this.scope.annuals,		
    	    				function(){ 
    	    			    	day = new Date(this.anuDate);
    	    					strDay = __Utils.dateToString(day)
    	    					if ($(__this).attr('datetime')==strDay){
    	    						isClosed = this.anuClosed;
        	    					if (isClosed==1){ 
    	    							$(__this).parent().addClass('date_closed');
    	    						} else if (isClosed==0){ 
    	    							$(__this).parent().removeClass('date_closed');
    	    							dayDiary = this.anuDayDiary;
    	    							if (dayDiary && dayDiary!=""){
    	    								$(__this).parent().addClass('date_diary');
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


app
	.controller("MenuBehaviour", [
  		"$scope", "$location", "$rootScope", 
		function($scope, $location, $rootScope) {
  			  			
  			$scope.isActive = function(viewLocation) {
				var pattern = '/' + viewLocation, re = new RegExp(pattern);
				var path = $location.path();
  				return re.test(path);
			};
			

			// Obtenemos el previusPath cuando se ha pulsado sobre el menu
		    $scope.$on('$locationChangeStart', function(evt, absNewUrl, absOldUrl) {
		    	//console.log('absNewUrl absOldUrl: ', absNewUrl, absOldUrl);
		    	var hashIndex = absNewUrl.indexOf('#');
		    	var newRoute = absNewUrl.substr(hashIndex + 1);
		    	hashIndex = absOldUrl.indexOf('#');
		    	var oldRoute = absOldUrl.substr(hashIndex + 1);
		    	if (newRoute!=oldRoute){
		    		if (oldRoute!=$scope.previusPath){
		    			$scope.previusPath = oldRoute;
		    			//console.log('previusPath: ', $scope.previusPath);
		    		}
		    	}
		    });
		   			
	} ]);
app
	.controller(
				"LangsController",
				[
						"$scope", "$state", "$location", "httpService", "$rootScope", "$mdDialog", "$mdMedia", "$mdDateLocale",
						function($scope, $state, $location, httpService, $rootScope, $mdDialog, $mdMedia, $mdDateLocale) {
							
							// Callback de changeLang para selectLang
							$scope.callSelectLang = function() {
								if ($state.get("booking.home")) {
								    //console.log("estamos en booking o operator");
									urlLocalTask = protocol_url + appHost + "/localTask/listCombi";
							     	data = {localId:$rootScope.local.id, lanCode:$rootScope.langApp};
							     	var promiseLocalTask = httpService.GET(urlLocalTask,data);
							     	promiseLocalTask.then(function (response) {
									 	var combiTasks = __Utils.sortByPropChar(response.data, "lotName", true);
									 	$rootScope.combiTasks = combiTasks;
									 	var path = $location.path();
										if (path != "/booking") {
											return $state.go("booking.home");
										} else {
											return $rootScope.currentScope.initBook(1);
										}
							     	});
							     	
							   		moment.locale($rootScope.langApp);
								
								    $mdDateLocale.shortDays = eval($rootScope.findLangTextElement("general.daysWeekShort"));
								    $mdDateLocale.shortMonths = eval($rootScope.findLangTextElement("general.monthsShort"));
							     	
								} else if ($state.get("localTasks.home")) {
									//console.log("estamos en manager");
									return $state.go('localTasks.home');
								} else if ($state.get("reportSales.home")) {
									//console.log("estamos en report");
									return $state.go('reportSales.home');
								} else if ($state.get("search.home")) {
									//console.log("estamos en search");
									var path = $location.path();
									if (path != "/search") {
										return $state.go("search.home");
									} else {
										return $rootScope.currentScope.initBook(1);
									}									
							     	
							   		moment.locale($rootScope.langApp);
								
								    $mdDateLocale.shortDays = eval($rootScope.findLangTextElement("general.daysWeekShort"));
								    $mdDateLocale.shortMonths = eval($rootScope.findLangTextElement("general.monthsShort"));
							     	
								}
							};
							
							// Cambiamos el idioma
							$scope.selectLang = function(lang) {
								//console.log("selectLang", lang, $rootScope.langApp);
								if (lang && lang !== $rootScope.langApp) {
									if (!$scope.openModelLangs){
										$rootScope.showLang = !$rootScope.showLang;
									}	
									$rootScope.isViewLoading = true;
									$rootScope.changeLang(lang,$scope.callSelectLang);
									$rootScope.openNotif($rootScope.findLangTextElement("lang.selected.text") + " " + lang, 2, null);
								}
							};
															
							$scope.toggleLangTools = function() {
								//console.log('toggleLangTools',$rootScope.showLang);
								$scope.openModelLangs = !$mdMedia('gt-sm') && $rootScope.langs.length>3;
								if ($scope.openModelLangs){
									$scope.showLangs($rootScope.findLangTextElement("label.aside.langs"), $rootScope.findLangTextElement("general.select"));
								} else {
									$rootScope.showLang = !$rootScope.showLang;
								}	
							};
														
							$scope.showLangs = function(titleDialog, titleContent) {
								//console.log ("titleDialog, titleContent",titleDialog, titleContent);
							    $mdDialog.show({
							      controller: DialogController,
							      templateUrl: 'views/modalDialogLangs.html',
							      parent: angular.element(document.body),
							      clickOutsideToClose:true,
						          locals: { titleDialog: titleDialog, titleContent: titleContent }
							    })
							    .then(function(obj) {
							    	$scope.selectLang(obj);
							    });
							};
							
							var DialogController = function ($scope, $mdDialog, titleDialog, titleContent) {
								
								$scope.acceptText = $rootScope.findLangTextElement("form.accept");
								$scope.cancelText = $rootScope.findLangTextElement("form.cancel");

								$scope.titleDialog = titleDialog;
							    $scope.titleContent = titleContent;

							    $scope.returnObj = $rootScope.langApp;
							    
							    $scope.selectObj = function(obj) {
							    	$scope.returnObj = obj;
							    }
							    
								$scope.hide = function() {
									$mdDialog.hide();
								};
								  
								$scope.cancel = function() {
								    $mdDialog.cancel();
								};
								  
								$scope.answer = function() {
								    $mdDialog.hide($scope.returnObj);
								};

							}
							DialogController.$inject = ["$scope", "$mdDialog", "titleDialog", "titleContent"];

							
							
						} ]);

app.directive('showLangs', function() {
	return {
		restrict : 'E',
		templateUrl : 'views/langs.html',
		controller: 'LangsController',
	};
});
app
		.controller(
				"LegalController",
				[
						"$scope", "$state", "$location", "httpService", "$rootScope", "$mdDialog", "$mdMedia",
						function($scope, $state, $location, httpService, $rootScope, $mdDialog, $mdMedia) {
													
							// Al iniciar la pantalla de Legal
							$scope.initLegal = function() {
								//console.log("initLegal");
							}	
							
							
						} ]);


angular.module('app').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('views/bookingAposDay.html',
    "<md-content> <md-content ng-show=\"!errorApoDay\"> <loading></loading> <div> <md-grid-list md-cols=\"5\" md-gutter=\"0px\" md-row-height=\"16px\"> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(-2)\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(-1)\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay()\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(1)\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(2)\"></span> </md-grid-tile> </md-grid-list> <md-divider> <md-grid-list id=\"tableDatesAux\" md-cols=\"5\" md-gutter=\"0px\" md-row-height=\"56px\"> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(-2)}\" ng-click=\"initDayAppos(-2, $event)\"> <span ng-bind=\"extractDayMonth(-2)\"></span> </md-grid-tile> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(-1)}\" ng-click=\"initDayAppos(-1, $event)\"> <span ng-bind=\"extractDayMonth(-1)\"></span> </md-grid-tile> <md-grid-tile class=\"date_not_enabled\"> <span class=\"today\" ng-bind=\"extractDayMonth()\"></span> </md-grid-tile> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(1)}\" ng-click=\"initDayAppos(1, $event)\"> <span ng-bind=\"extractDayMonth(1)\"></span> </md-grid-tile> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(2)}\" ng-click=\"initDayAppos(2, $event)\"> <span ng-bind=\"extractDayMonth(2)\"></span> </md-grid-tile> </md-grid-list> </md-divider></div> <md-divider class=\"clear\"> <div> <md-grid-list id=\"tableDays\" md-cols=\"{{appo.cols}}\" md-gutter-sm=\"4px\" md-row-height=\"fit\" style=\"height:{{appo.height}}px !important\"> <md-grid-tile ng-repeat=\"appointment in appo.appointments\" ng-click=\"onSelectDayAppo(appointment)\"> <span ng-hidess=\"appointment.bgColor>0\" class=\"calendarDayText\"> {{appointment.apoName}} <span ng-show=\"appointment.bgColor>0\" class=\"calendarDayText\"> {{appointment.apoCalendarName}} </span> <md-icon md-font-library=\"material-icons\" class=\"md-warn\">check</md-icon> </span> <!-- \t\t<div ng-show=\"appointment.bgColor>0\" class=\"calendarDaySP bg-color{{bgColor}}\" style=\"margin-top: {{appointment.apoX}}px; left: {{appointment.apoY}}%;\">\r" +
    "\n" +
    "\t\t\t\t\t\t\t\t\t\t\t<p class=\"calendarDayText\">{{appointment.apoName}}</p>\r" +
    "\n" +
    "\t\t\t\t\t\t\t\t\t\t\t<p class=\"calendarDayText special\">{{appointment.apoCalendarName}}</p>\r" +
    "\n" +
    "\t\t\t\t\t\t\t\t\t\t</div> --> </md-grid-tile> </md-grid-list> </div> </md-divider></md-content> <md-content ng-show=\"errorApoDay\"> <md-toolbar> <div class=\"md-toolbar-tools notif cab\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools notif notsave\"> <span flex></span> <span flex>{{findLangTextElement(\"label.notification.notavailable.title\")}}</span> <span flex></span> </div> </md-toolbar> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item class=\"md-1-line\"> <p>{{findLangTextElement(\"label.notification.notavailable.text\")}}</p> </md-list-item> <md-list-item ng-show=\"appo.nextDays.length>0\" class=\"md-1-line\"> <p ng-show=\"!isSubViewLoading\">{{findLangTextElement(\"localTask.notavailablesearchresult\")}} {{formatDateSelected()}}</p> </md-list-item> </md-list> </div> <div ng-hide=\"appo.nextDays.length==0\"> <loading_sub></loading_sub> <md-grid-list ng-show=\"!isSubViewLoading\" id=\"tableDatesNext\" md-cols-xs=\"2\" md-cols=\"4\" md-gutter-sm=\"10px\" md-gutter=\"0px\" md-row-height=\"76px\"> <md-grid-tile ng-repeat=\"nextDay in appo.nextDays\" ng-click=\"initDayAppos(null, $event, nextDay)\"> <span ng-bind=\"extractDayWeek(nextDay)\"></span> </md-grid-tile> </md-grid-list> </div> <div ng-show=\"!isSubViewLoading && appo.nextDays.length==0\"> <md-divider class=\"clear-min\"> <div layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"initBook(1)\" class=\"md-primary md-hue-2\"> <span lnt-id=\"form.accept\">Aceptar</span> </md-button> <span flex></span> </div> </md-divider></div> </md-divider></md-content> </md-content>"
  );


  $templateCache.put('views/bookingAposEnd.html',
    "<md-content> <md-content ng-show=\"!errorSave\"> <md-toolbar> <div class=\"md-toolbar-tools notif cab\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools notif\"> <span flex></span> <span ng-show=\"!adminOption\" flex>{{findLangTextElement(\"label.notification.bookedApo.title\")}}</span> <span ng-show=\"adminOption\" flex>{{findLangTextElement(\"label.notification.bookedApoAdmin.title\")}}</span> <span flex></span> </div> </md-toolbar> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item class=\"md-1-line\"> <p ng-show=\"!adminOption\">{{findLangTextElement(\"label.notification.bookedApo.text\")}}</p> <p ng-show=\"adminOption\">{{findLangTextElement(\"label.notification.bookedApoAdmin.text\")}}</p> </md-list-item> <md-divider> <md-list-item> <div layout=\"column\" layout-align=\"center start\" layout-gt-sm=\"row\" layout-align-gt-sm=\"start center\"> <div layout=\"row\" style=\"margin-right: 32px\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">today</md-icon> <p>{{formatDateSelected()}}</p> </div> <div layout=\"row\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">schedule</md-icon> <p>{{appo.appoSel.apoName}}</p> </div> </div> </md-list-item> <md-divider> <md-list-item ng-show=\"local.locNumPersonsApo > 1\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person_add</md-icon> <p>{{findLangTextElement(\"label.html.apoFor1\")}} {{personscope.numPersons}} {{findLangTextElement(\"label.html.apoFor2\")}}</p> </md-list-item> <md-list-item ng-show=\"!isAdveo\" ng-repeat=\"numPerson in personscope.persons | limitTo:personscope.numPersons\"> <md-icon ng-if=\"local.locNumPersonsApo == 1\" md-font-library=\"material-icons\" class=\"md-24\">build</md-icon> <md-icon ng-if=\"local.locNumPersonsApo > 1\" md-font-library=\"material-icons\" class=\"md-24\">looks_{{icons_num($index)}}</md-icon> <p ng-show=\"!tasMultiple\">{{personscope.selectedTasksPersons[$index][0].tasName}}</p> <p ng-show=\"tasMultiple\">{{personscope.selectedTasksPersonsStr[$index]}}</p> </md-list-item> <md-list-item ng-show=\"isAdveo\"> <p>{{findLangTextElement(\"label.template.numLines\")}}: {{personscope.selectedTasksPersons[0][0].numLines}}</p> <p>{{findLangTextElement(\"label.template.numPallets\")}}: {{personscope.selectedTasksPersons[0][0].numPallets}}</p> </md-list-item> <md-list-item ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 fmd-hue-3\">perm_contact_calendar</md-icon> <p>{{findLangTextElement(\"label.header.places\")}}: {{selCalendar.selectedCalendar[0].calName}}</p> </md-list-item> <md-divider> <md-list-item> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person</md-icon> <p>{{client.name}}</p> </md-list-item> <md-list-item> <div layout=\"column\" layout-align=\"center start\" layout-gt-sm=\"row\" layout-align-gt-sm=\"start center\"> <div layout=\"row\" style=\"margin-right: 32px\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">email</md-icon> <p>{{client.email}}</p> </div> <div layout=\"row\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">phone</md-icon> <p>{{client.telf}}</p> </div> </div> </md-list-item> <md-list-item ng-show=\"celebration.isCelebration\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">today</md-icon> <p>{{formatDateCelebration()}}</p> </md-list-item> <md-list-item ng-show=\"client.observ!=''\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">insert_comment</md-icon> <p>{{client.observ}}</p> </md-list-item> </md-divider></md-divider></md-divider></md-list> </div> </md-divider></md-content> <md-content ng-show=\"errorSave\"> <md-toolbar> <div class=\"md-toolbar-tools notif cab\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools notif notsave\"> <span flex></span> <span flex>{{findLangTextElement(\"label.notification.errorBase.title\")}}</span> <span flex></span> </div> </md-toolbar> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item class=\"md-1-line\"> <p>{{errorSave}}</p> </md-list-item> </md-list> </div> </md-divider></md-content> <md-divider class=\"clear-min\"> <div layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"initBook(1)\" class=\"md-primary md-hue-2\"> <span lnt-id=\"form.accept\">Aceptar</span> </md-button> <span flex></span> </div> </md-divider></md-content>"
  );


  $templateCache.put('views/bookingAposNew.html',
    "<form name=\"apoNewForm\"> <md-content> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item> <div layout=\"column\" layout-align=\"center start\" layout-gt-sm=\"row\" layout-align-gt-sm=\"start center\"> <div layout=\"row\" style=\"margin-right: 32px\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">today</md-icon> <p>{{formatDateSelected()}}</p> </div> <div layout=\"row\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">schedule</md-icon> <p>{{appo.appoSel.apoName}}</p> </div> </div> </md-list-item> <md-divider> <md-list-item ng-show=\"local.locNumPersonsApo > 1\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person_add</md-icon> <p>{{findLangTextElement(\"label.html.apoFor1\")}} {{personscope.numPersons}} {{findLangTextElement(\"label.html.apoFor2\")}}</p> </md-list-item> <md-divider> <md-list-item ng-show=\"!isAdveo\" ng-repeat=\"numPerson in personscope.persons | limitTo:personscope.numPersons\"> <md-icon ng-if=\"local.locNumPersonsApo == 1\" md-font-library=\"material-icons\" class=\"md-24\">build</md-icon> <md-icon ng-if=\"local.locNumPersonsApo > 1\" md-font-library=\"material-icons\" class=\"md-24\">looks_{{icons_num($index)}}</md-icon> <p ng-show=\"!tasMultiple\">{{personscope.selectedTasksPersons[$index][0].tasName}}</p> <p ng-show=\"tasMultiple\">{{personscope.selectedTasksPersonsStr[$index]}}</p> </md-list-item> <md-list-item ng-show=\"isAdveo\"> <p>{{findLangTextElement(\"label.template.numLines\")}}: {{personscope.selectedTasksPersons[0][0].numLines}}</p> <p>{{findLangTextElement(\"label.template.numPallets\")}}: {{personscope.selectedTasksPersons[0][0].numPallets}}</p> </md-list-item> <md-divider ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-list-item ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 fmd-hue-3\">perm_contact_calendar</md-icon> <p>{{findLangTextElement(\"label.header.places\")}}: {{selCalendar.selectedCalendar[0].calName}}</p> </md-list-item> </md-divider></md-divider></md-divider></md-list> </div> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\"> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person</md-icon> <label>{{findLangTextElement(\"client.name\")}}</label> <input name=\"cliName\" ng-model=\"client.name\" ng-maxlength=\"100\" required> <div ng-messages=\"apoNewForm.cliName.$error\"> <div ng-message=\"required\">{{findLangTextElement(\"label.requiredData\")}}</div> <div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div> </div> </md-input-container> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">email</md-icon> <label>{{findLangTextElement(\"client.email\")}}</label> <input type=\"email\" name=\"cliEmail\" ng-model=\"client.email\" required ng-pattern=\"/^.+@.+\\..+$/\"> <div ng-messages=\"apoNewForm.cliEmail.$error\"> <div ng-message-exp=\"['required', 'pattern']\">{{findLangTextElement(\"label.requiredData\")}} / {{findLangTextElement(\"label.typePatternData\")}}</div> </div> </md-input-container> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">phone</md-icon> <label>{{findLangTextElement(\"client.telf\")}}</label> <input type=\"tel\" name=\"cliTelf\" ng-model=\"client.telf\" required placeholder=\"9.. / 6..\"> <div ng-messages=\"apoNewForm.cliTelf.$error\"> <div ng-message=\"required\">{{findLangTextElement(\"label.requiredData\")}}</div> </div> </md-input-container> </div> <md-divider class=\"clear-min\"> <div layout=\"column\" ng-show=\"celebration.isCelebration\"> <md-input-container class=\"md-block\" flex-gt-sm> <label>{{findLangTextElement(\"client.celebrationDate\")}}</label> <md-datepicker ng-model=\"celebration.date\" md-min-date=\"celebration.dateMin\"> </md-input-container> </div> <md-divider class=\"clear-min\"> <div layout=\"column\"> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">insert_comment</md-icon> <label>{{findLangTextElement(\"event.com\")}}</label> <textarea name=\"cliObserv\" ng-model=\"client.observ\" ng-maxlength=\"300\"></textarea> <div ng-messages=\"apoNewForm.cliObserv.$error\"> <div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div> </div> </md-input-container> </div> <!-- <md-divider class=\"clear-min\"> --> <!-- \r" +
    "\n" +
    "\t\t<div layout=\"column\">\t\r" +
    "\n" +
    "\t\t\t<md-input-container class=\"md-block\" flex-gt-sm>\r" +
    "\n" +
    "\t\t\t\t<md-icon md-font-library=\"material-icons\" class=\"md-24\">report</md-icon>\r" +
    "\n" +
    "\t\t\t\t<label>{{findLangTextElement(\"event.legal\")}}</label> \r" +
    "\n" +
    "\t\t\t\t <!-- <md-checkbox ng-model=\"client.legal\"> Checkbox 1: {{ data.cb1 }}</md-checkbox>--> <!-- <div ng-messages=\"apoNewForm.cliLegal.$error\">\r" +
    "\n" +
    "\t\t\t\t\t<div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div>\r" +
    "\n" +
    "\t\t\t\t</div> --> <!-- \t</md-input-container>\r" +
    "\n" +
    "\t\t</div> --> <md-divider class=\"clear-min\"> <div layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"(apoNewForm.$invalid=true) && sendNewAppo()\" class=\"md-primary md-hue-2\" ng-disabled=\"apoNewForm.$invalid\"> <span lnt-id=\"form.send\">Enviar</span> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-accents\">send</md-icon> </md-button> <span flex></span> </div> </md-divider></md-divider></md-divider></md-divider></md-content> </form>"
  );


  $templateCache.put('views/bookingAposNewAdmin.html',
    "<form name=\"apoNewAdminForm\"> <md-content> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item> <div layout=\"column\" layout-align=\"center start\" layout-gt-sm=\"row\" layout-align-gt-sm=\"start center\"> <div layout=\"row\" style=\"margin-right: 32px\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">today</md-icon> <p>{{formatDateSelected()}}</p> </div> <div layout=\"row\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">schedule</md-icon> <p>{{appo.appoSel.apoName}}</p> </div> </div> </md-list-item> <md-divider> <md-list-item ng-show=\"local.locNumPersonsApo > 1\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person_add</md-icon> <p>{{findLangTextElement(\"label.html.apoFor1\")}} {{personscope.numPersons}} {{findLangTextElement(\"label.html.apoFor2\")}}</p> </md-list-item> <md-divider> <md-list-item ng-show=\"!isAdveo\" ng-repeat=\"numPerson in personscope.persons | limitTo:personscope.numPersons\"> <md-icon ng-if=\"local.locNumPersonsApo == 1\" md-font-library=\"material-icons\" class=\"md-24\">build</md-icon> <md-icon ng-if=\"local.locNumPersonsApo > 1\" md-font-library=\"material-icons\" class=\"md-24\">looks_{{icons_num($index)}}</md-icon> <p ng-show=\"!tasMultiple\">{{personscope.selectedTasksPersons[$index][0].tasName}}</p> <p ng-show=\"tasMultiple\">{{personscope.selectedTasksPersonsStr[$index]}}</p> </md-list-item> <md-list-item ng-show=\"isAdveo\"> <p>{{findLangTextElement(\"label.template.numLines\")}}: {{personscope.selectedTasksPersons[0][0].numLines}}</p> <p>{{findLangTextElement(\"label.template.numPallets\")}}: {{personscope.selectedTasksPersons[0][0].numPallets}}</p> </md-list-item> <md-divider ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-list-item ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 fmd-hue-3\">perm_contact_calendar</md-icon> <p>{{findLangTextElement(\"label.header.places\")}}: {{selCalendar.selectedCalendar[0].calName}}</p> </md-list-item> </md-divider></md-divider></md-divider></md-list> </div> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\" ng-controller=\"BookControllerAuto as ctrl\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 person-autocomplete\">person</md-icon> <md-autocomplete required md-floating-label=\"{{findLangTextElement('client.name')}}\" md-min-length=\"3\" md-input-name=\"cliName\" md-input-maxlength=\"100\" md-no-cache=\"true\" md-items=\"item in ctrl.querySearch(ctrl.searchText)\" md-item-text=\"item.whoName\" md-selected-item=\"ctrl.selectedItem\" md-search-text=\"ctrl.searchText\" md-selected-item-change=\"ctrl.selectedItemChange(item)\" md-search-text-change=\"ctrl.actionNewClient(ctrl.searchText)\" md-escape-options=\"clear\" md-clear-button=\"true\"> <md-item-template> <span md-highlight-text=\"ctrl.searchText\">{{item.whoName}}</span> </md-item-template> <div ng-messages=\"apoNewAdminForm.cliName.$error\"> <div ng-message=\"required\">{{findLangTextElement(\"label.requiredData\")}}</div> <div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div> </div> </md-autocomplete> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">email</md-icon> <label>{{findLangTextElement(\"client.email\")}}</label> <input ng-disabled=\"!ctrl.isNewClient()\" type=\"email\" name=\"cliEmail\" ng-model=\"client.email\" ng-pattern=\"/^.+@.+\\..+$/\" ng-change=\"changeCliEmail()\"> <div ng-messages=\"apoNewAdminForm.cliEmail.$error\"> <div ng-message-exp=\"['pattern']\">{{findLangTextElement(\"label.requiredData\")}} / {{findLangTextElement(\"label.typePatternData\")}}</div> </div> </md-input-container> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">phone</md-icon> <label>{{findLangTextElement(\"client.telf\")}}</label> <input ng-disabled=\"!ctrl.isNewClient()\" type=\"tel\" name=\"cliTelf\" ng-model=\"client.telf\" placeholder=\"9.. / 6..\"> </md-input-container> </div> <md-divider class=\"clear-min\"> <div layout=\"column\" ng-show=\"celebration.isCelebration\"> <md-input-container class=\"md-block\" flex-gt-sm> <label>{{findLangTextElement(\"client.celebrationDate\")}}</label> <md-datepicker ng-model=\"celebration.date\" md-min-date=\"celebration.dateMin\"> </md-input-container> </div> <md-divider class=\"clear-min\"> <div layout=\"column\"> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">insert_comment</md-icon> <label>{{findLangTextElement(\"event.com\")}}</label> <textarea name=\"cliObserv\" ng-model=\"client.observ\" ng-maxlength=\"300\"></textarea> <div ng-messages=\"apoNewAdminForm.cliObserv.$error\"> <div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div> </div> </md-input-container> </div> <!-- <md-divider class=\"clear-min\"> --> <!-- \r" +
    "\n" +
    "\t\t<div layout=\"column\">\t\r" +
    "\n" +
    "\t\t\t<md-input-container class=\"md-block\" flex-gt-sm>\r" +
    "\n" +
    "\t\t\t\t<md-icon md-font-library=\"material-icons\" class=\"md-24\">report</md-icon>\r" +
    "\n" +
    "\t\t\t\t<label>{{findLangTextElement(\"event.legal\")}}</label> \r" +
    "\n" +
    "\t\t\t\t <!-- <md-checkbox ng-model=\"client.legal\"> Checkbox 1: {{ data.cb1 }}</md-checkbox>--> <!-- <div ng-messages=\"apoNewAdminForm.cliLegal.$error\">\r" +
    "\n" +
    "\t\t\t\t\t<div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div>\r" +
    "\n" +
    "\t\t\t\t</div> --> <!-- \t</md-input-container>\r" +
    "\n" +
    "\t\t</div> --> <md-divider class=\"clear-min\"> <div layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"(apoNewAdminForm.$invalid=true) && sendNewAppo()\" class=\"md-primary md-hue-2\" ng-disabled=\"apoNewAdminForm.$invalid\"> <span lnt-id=\"form.send\">Enviar</span> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-accents\">send</md-icon> </md-button> <span flex></span> </div> </md-divider></md-divider></md-divider></md-divider></md-content> </form>"
  );


  $templateCache.put('views/bookingHome.html',
    "<article class=\"booking_calendar\"> <md-tabs id=\"tabsbook\" md-selected=\"$parent.selectedTabIndex\" md-dynamic-height md-border-bottom md-autoselect> <md-tab label=\"{{tabsBook[0].title}}\" ng-disabled=\"tabsBook[0].disabled\" ng-click=\"$parent.selectedTabIndex = 0;\"> <loading></loading> <md-toolbar ng-show=\"!isViewLoading && selectedDate\"> <div class=\"md-toolbar-tools date\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools date year\" layout-align=\"start center\"> <span flex ng-bind=\"extractYear()\"></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools date\" layout-align=\"start center\"> <span flex=\"70\" ng-bind=\"extractDayWeekMonth()\"></span> <span flex></span> <md-button ng-click=\"toggleSelectDate()\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-light\">arrow_drop_down_circle</md-icon> </md-button> </div> </md-toolbar> <show-month id=\"table-month\" ng-show=\"!isViewLoading && showSelectedDate\"> </show-month> <show-tasks ng-show=\"!isViewLoading && selectedDate\"> </show-tasks> <md-divider class=\"clear\"> <div ng-show=\"!isViewLoading && selectedDate\" layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"saveTaskSelect()\" class=\"md-primary md-hue-2\"> <span lnt-id=\"event.searchHours\">Ver resultados</span> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-accents\">search</md-icon> </md-button> <span flex></span> </div> </md-divider></md-tab> <md-tab label=\"{{tabsBook[1].title}}\" ng-disabled=\"tabsBook[1].disabled\" ng-click=\"$parent.selectedTabIndex = 1;\"> <loading></loading> <show-apos-day ng-show=\"!isViewLoading\"> </show-apos-day> </md-tab> <md-tab label=\"{{tabsBook[2].title}}\" ng-disabled=\"tabsBook[2].disabled\" ng-click=\"$parent.selectedTabIndex = 2;\"> <loading></loading> <show-apos-new ng-show=\"!isViewLoading && !adminOption\"> </show-apos-new> <show-apos-new-admin ng-show=\"!isViewLoading && adminOption\"> </show-apos-new-admin> </md-tab> <md-tab label=\"{{tabsBook[3].title}}\" ng-disabled=\"tabsBook[3].disabled\" ng-click=\"$parent.selectedTabIndex = 3;\"> <loading></loading> <show-apos-end ng-show=\"!isViewLoading\"> </md-tab> </md-tabs> </article>"
  );


  $templateCache.put('views/bookingSelectTasks.html',
    "<form> <md-content> <div layout=\"column\"> <md-list> <div ng-show=\"local.locNumPersonsApo > 1\" class=\"block-sep\"> <md-list-item class=\"md-2-line\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person_add</md-icon> <div class=\"md-list-item-text\"> <p lnt-id=\"task.select.people\"></p> </div> </md-list-item> <md-list-item class=\"md-2-line\"> <md-icon></md-icon> <div class=\"md-list-item-text\"> <md-input-container> <md-select ng-model=\"personscope.numPersons\" ng-change=\"disabledNextTabs();changeNumPersons()\" aria-label=\"{{findLangTextElement('task.select.people')}}\"> <md-option ng-repeat=\"numPerson in personscope.persons\" value=\"{{numPerson.id}}\"> {{numPerson.name}} </md-option> </md-select> </md-input-container> </div> </md-list-item> </div> <div ng-show=\"!isAdveo\" ng-repeat=\"numPerson in personscope.persons | limitTo:personscope.numPersons\" class=\"block-sep\"> <md-list-item class=\"md-2-line\"> <md-icon ng-if=\"local.locNumPersonsApo == 1\" md-font-library=\"material-icons\" class=\"md-24\">build</md-icon> <md-icon ng-if=\"local.locNumPersonsApo > 1\" md-font-library=\"material-icons\" class=\"md-24\">looks_{{icons_num($index)}}</md-icon> <div class=\"md-list-item-text\"> <p lnt-id=\"label.template.job\"></p> </div> </md-list-item> <md-list-item class=\"md-2-line\"> <md-icon></md-icon> <div class=\"md-list-item-text taskSelect\"> <a ng-href=\"\" ng-click=\"goToSelectTaskPerson(numPerson.id)\"> <font>{{personscope.selectedTasksPersonsStr[$index]}}</font> </a> <md-button ng-show=\"sectionBack==null && existsMenu\" ng-click=\"goToSelectTaskPerson(numPerson.id)\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">arrow_drop_down</md-icon> </md-button> </div> </md-list-item> </div> <md-list-item ng-show=\"isAdveo\" class=\"md-2-line\"> <label id=\"error_numLines\" style=\"color:red\"></label> <label>{{findLangTextElement(\"label.template.selNumLines\")}}: </label> <input id=\"numLines\" size=\"3\" type=\"number\" ng-model=\"personscope.selectedTasksPersons[0][0].numLines\" max=\"999\" min=\"1\" required> <label id=\"error_numPallets\" style=\"color:red\"></label> <label>{{findLangTextElement(\"label.template.selNumPallets\")}}: </label> <input id=\"numPallets\" size=\"3\" type=\"number\" ng-model=\"personscope.selectedTasksPersons[0][0].numPallets\" max=\"999\" min=\"1\" required> </md-list-item> <div ng-show=\"local.locSelCalendar == 1\" class=\"block-sep\"> <md-list-item class=\"md-2-line\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-hue-3\">perm_contact_calendar</md-icon> <div class=\"md-list-item-text\"> <p lnt-id=\"place.select.cabText\"></p> </div> </md-list-item> <md-list-item class=\"md-2-line\"> <md-icon></md-icon> <div class=\"md-list-item-text\"> <md-input-container> <md-select ng-model=\"selCalendar.selectedCalendar[0].id\" ng-change=\"disabledNextTabs()\" aria-label=\"{{findLangTextElement('place.select.cabText')}}\"> <md-option value=\"-1\">{{findLangTextElement(\"general.anyone\")}}</md-option> <md-option ng-repeat=\"calendar in selCalendar.calendars\" value=\"{{calendar.id}}\"> {{calendar.calName}} </md-option> </md-select> </md-input-container> </div> </md-list-item> </div> </md-list> </div> </md-content> </form>"
  );


  $templateCache.put('views/flyCalendarTable.html',
    "<md-subheader class=\"md-no-sticky md-primary\"> <div layout=\"row\" layout-align=\"space-around center\"> <div flex=\"70\" layout=\"row\" layout-align=\"start center\"> <md-button ng-click=\"loadMonth(-1)\"> <md-icon md-font-library=\"material-icons\" class=\"md-primary md-24\">navigate_before</md-icon> </md-button> <span flex=\"none\" class=\"month-year\"></span> <md-button ng-click=\"loadMonth(+1)\"> <md-icon md-font-library=\"material-icons\" class=\"md-primary md-24\">navigate_next</md-icon> </md-button> </div> <md-button ng-click=\"onToday()\"> <span lnt-id=\"label.header.today\">Today</span> <md-icon md-font-library=\"material-icons\" class=\"md-primary md-24\">today</md-icon> </md-button> <span flex></span> </div> </md-subheader> <md-divider> <md-grid-list md-cols=\"7\" md-gutter=\"0px\" md-row-height=\"16px\"> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekMon\">Mon</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekTue\">Tue</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekWed\">Wed</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekThu\">Thu</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekFri\">Fri</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekSat\">Sat</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekSun\">Sun</span> </md-grid-tile> </md-grid-list> <md-divider> <md-grid-list id=\"tableDates\" md-cols=\"7\" md-gutter=\"0px\" md-row-height=\"fit\"> <md-grid-tile ng-repeat=\"a in range(42) track by $index\" ng-click=\"disabledNextTabs(); onSelectDate($event)\"> <span id=\"date\"></span> </md-grid-tile> </md-grid-list></md-divider></md-divider>"
  );


  $templateCache.put('views/flyHome.html',
    "<article class=\"booking_calendar\"> <md-tabs id=\"tabsbook\" md-selected=\"$parent.selectedTabIndex\" md-dynamic-height md-border-bottom md-autoselect> <md-tab label=\"{{tabsBook[0].title}}\" ng-disabled=\"tabsBook[0].disabled\" ng-click=\"$parent.selectedTabIndex = 0;\"> <loading></loading> <show-calendar-table id=\"table-month\" ng-show=\"!isViewLoading && showCalendar\"> </show-calendar-table> <show-search ng-show=\"!isViewLoading\"> </show-search> <md-divider class=\"clear\"> <div ng-show=\"!isViewLoading && searchInputScope.dateOrigin\" layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"saveSearch()\" class=\"md-primary md-hue-2\"> <span lnt-id=\"fly.action.searchOffers\">Ver resultados</span> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-accents\">search</md-icon> </md-button> <span flex></span> </div> </md-divider></md-tab> <md-tab label=\"{{tabsBook[1].title}}\" ng-disabled=\"tabsBook[1].disabled\" ng-click=\"$parent.selectedTabIndex = 1;\"> <loading></loading> <show-list-offers ng-show=\"!isViewLoading\"> </show-list-offers> </md-tab> <!--<md-tab label=\"{{tabsBook[2].title}}\" ng-disabled=\"tabsBook[2].disabled\" ng-click=\"$parent.selectedTabIndex = 2;\">\r" +
    "\n" +
    "      \t\t\t<loading></loading>\r" +
    "\n" +
    "\t\t\t\t<show-save-search ng-show=\"!isViewLoading && !adminOption\">\r" +
    "\n" +
    "\t\t\t\t</show-save-search>\r" +
    "\n" +
    "      \t\t</md-tab>\r" +
    "\n" +
    "\r" +
    "\n" +
    "\t\t\t<md-tab label=\"{{tabsBook[3].title}}\" ng-disabled=\"tabsBook[3].disabled\" ng-click=\"$parent.selectedTabIndex = 3;\">\r" +
    "\n" +
    "      \t\t\t<loading></loading>\r" +
    "\n" +
    "\t\t\t\t<show-save-search-end ng-show=\"!isViewLoading\">\r" +
    "\n" +
    "\t\t\t\t</show-save-search-end>\r" +
    "\n" +
    "      \t\t</md-tab>--> </md-tabs> </article>"
  );


  $templateCache.put('views/flyListOffers.html',
    "<md-content> <md-content ng-show=\"!errorApoDay\"> <loading></loading> <div> <md-grid-list md-cols=\"5\" md-gutter=\"0px\" md-row-height=\"16px\"> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(-2)\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(-1)\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay()\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(1)\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(2)\"></span> </md-grid-tile> </md-grid-list> <md-divider> <md-grid-list id=\"tableDatesAux\" md-cols=\"5\" md-gutter=\"0px\" md-row-height=\"56px\"> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(-2)}\" ng-click=\"initDayAppos(-2, $event)\"> <span ng-bind=\"extractDayMonth(-2)\"></span> </md-grid-tile> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(-1)}\" ng-click=\"initDayAppos(-1, $event)\"> <span ng-bind=\"extractDayMonth(-1)\"></span> </md-grid-tile> <md-grid-tile class=\"date_not_enabled\"> <span class=\"today\" ng-bind=\"extractDayMonth()\"></span> </md-grid-tile> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(1)}\" ng-click=\"initDayAppos(1, $event)\"> <span ng-bind=\"extractDayMonth(1)\"></span> </md-grid-tile> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(2)}\" ng-click=\"initDayAppos(2, $event)\"> <span ng-bind=\"extractDayMonth(2)\"></span> </md-grid-tile> </md-grid-list> </md-divider></div> <md-divider class=\"clear\"> <div> <md-grid-list id=\"tableDays\" md-cols=\"{{appo.cols}}\" md-gutter-sm=\"4px\" md-row-height=\"fit\" style=\"height:{{appo.height}}px !important\"> <md-grid-tile ng-repeat=\"appointment in appo.appointments\" ng-click=\"onSelectDayAppo(appointment)\"> <span ng-hidess=\"appointment.bgColor>0\" class=\"calendarDayText\"> {{appointment.apoName}} <span ng-show=\"appointment.bgColor>0\" class=\"calendarDayText\"> {{appointment.apoCalendarName}} </span> <md-icon md-font-library=\"material-icons\" class=\"md-warn\">check</md-icon> </span> <!-- \t\t<div ng-show=\"appointment.bgColor>0\" class=\"calendarDaySP bg-color{{bgColor}}\" style=\"margin-top: {{appointment.apoX}}px; left: {{appointment.apoY}}%;\">\r" +
    "\n" +
    "\t\t\t\t\t\t\t\t\t\t\t<p class=\"calendarDayText\">{{appointment.apoName}}</p>\r" +
    "\n" +
    "\t\t\t\t\t\t\t\t\t\t\t<p class=\"calendarDayText special\">{{appointment.apoCalendarName}}</p>\r" +
    "\n" +
    "\t\t\t\t\t\t\t\t\t\t</div> --> </md-grid-tile> </md-grid-list> </div> </md-divider></md-content> <md-content ng-show=\"errorApoDay\"> <md-toolbar> <div class=\"md-toolbar-tools notif cab\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools notif notsave\"> <span flex></span> <span flex>{{findLangTextElement(\"label.notification.notavailable.title\")}}</span> <span flex></span> </div> </md-toolbar> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item class=\"md-1-line\"> <p>{{findLangTextElement(\"label.notification.notavailable.text\")}}</p> </md-list-item> <md-list-item ng-show=\"appo.nextDays.length>0\" class=\"md-1-line\"> <p ng-show=\"!isSubViewLoading\">{{findLangTextElement(\"localTask.notavailablesearchresult\")}} {{formatDateSelected()}}</p> </md-list-item> </md-list> </div> <div ng-hide=\"appo.nextDays.length==0\"> <loading_sub></loading_sub> <md-grid-list ng-show=\"!isSubViewLoading\" id=\"tableDatesNext\" md-cols-xs=\"2\" md-cols=\"4\" md-gutter-sm=\"10px\" md-gutter=\"0px\" md-row-height=\"76px\"> <md-grid-tile ng-repeat=\"nextDay in appo.nextDays\" ng-click=\"initDayAppos(null, $event, nextDay)\"> <span ng-bind=\"extractDayWeek(nextDay)\"></span> </md-grid-tile> </md-grid-list> </div> <div ng-show=\"!isSubViewLoading && appo.nextDays.length==0\"> <md-divider class=\"clear-min\"> <div layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"initSearch(1)\" class=\"md-primary md-hue-2\"> <span lnt-id=\"form.accept\">Aceptar</span> </md-button> <span flex></span> </div> </md-divider></div> </md-divider></md-content> </md-content>"
  );


  $templateCache.put('views/flySaveSearch.html',
    "<form name=\"apoNewForm\"> <md-content> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item> <div layout=\"column\" layout-align=\"center start\" layout-gt-sm=\"row\" layout-align-gt-sm=\"start center\"> <div layout=\"row\" style=\"margin-right: 32px\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">today</md-icon> <p>{{formatDateSelected()}}</p> </div> <div layout=\"row\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">schedule</md-icon> <p>{{appo.appoSel.apoName}}</p> </div> </div> </md-list-item> <md-divider> <md-list-item ng-show=\"local.locNumPersonsApo > 1\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person_add</md-icon> <p>{{findLangTextElement(\"label.html.apoFor1\")}} {{personscope.numPersons}} {{findLangTextElement(\"label.html.apoFor2\")}}</p> </md-list-item> <md-divider> <md-list-item ng-show=\"!isAdveo\" ng-repeat=\"numPerson in personscope.persons | limitTo:personscope.numPersons\"> <md-icon ng-if=\"local.locNumPersonsApo == 1\" md-font-library=\"material-icons\" class=\"md-24\">build</md-icon> <md-icon ng-if=\"local.locNumPersonsApo > 1\" md-font-library=\"material-icons\" class=\"md-24\">looks_{{icons_num($index)}}</md-icon> <p ng-show=\"!tasMultiple\">{{personscope.selectedTasksPersons[$index][0].tasName}}</p> <p ng-show=\"tasMultiple\">{{personscope.selectedTasksPersonsStr[$index]}}</p> </md-list-item> <md-list-item ng-show=\"isAdveo\"> <p>{{findLangTextElement(\"label.template.numLines\")}}: {{personscope.selectedTasksPersons[0][0].numLines}}</p> <p>{{findLangTextElement(\"label.template.numPallets\")}}: {{personscope.selectedTasksPersons[0][0].numPallets}}</p> </md-list-item> <md-divider ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-list-item ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 fmd-hue-3\">perm_contact_calendar</md-icon> <p>{{findLangTextElement(\"label.header.places\")}}: {{selCalendar.selectedCalendar[0].calName}}</p> </md-list-item> </md-divider></md-divider></md-divider></md-list> </div> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\"> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person</md-icon> <label>{{findLangTextElement(\"client.name\")}}</label> <input name=\"cliName\" ng-model=\"client.name\" ng-maxlength=\"100\" required> <div ng-messages=\"apoNewForm.cliName.$error\"> <div ng-message=\"required\">{{findLangTextElement(\"label.requiredData\")}}</div> <div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div> </div> </md-input-container> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">email</md-icon> <label>{{findLangTextElement(\"client.email\")}}</label> <input type=\"email\" name=\"cliEmail\" ng-model=\"client.email\" required ng-pattern=\"/^.+@.+\\..+$/\"> <div ng-messages=\"apoNewForm.cliEmail.$error\"> <div ng-message-exp=\"['required', 'pattern']\">{{findLangTextElement(\"label.requiredData\")}} / {{findLangTextElement(\"label.typePatternData\")}}</div> </div> </md-input-container> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">phone</md-icon> <label>{{findLangTextElement(\"client.telf\")}}</label> <input type=\"tel\" name=\"cliTelf\" ng-model=\"client.telf\" required placeholder=\"9.. / 6..\"> <div ng-messages=\"apoNewForm.cliTelf.$error\"> <div ng-message=\"required\">{{findLangTextElement(\"label.requiredData\")}}</div> </div> </md-input-container> </div> <md-divider class=\"clear-min\"> <div layout=\"column\" ng-show=\"celebration.isCelebration\"> <md-input-container class=\"md-block\" flex-gt-sm> <label>{{findLangTextElement(\"client.celebrationDate\")}}</label> <md-datepicker ng-model=\"celebration.date\" md-min-date=\"celebration.dateMin\"> </md-input-container> </div> <md-divider class=\"clear-min\"> <div layout=\"column\"> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">insert_comment</md-icon> <label>{{findLangTextElement(\"event.com\")}}</label> <textarea name=\"cliObserv\" ng-model=\"client.observ\" ng-maxlength=\"300\"></textarea> <div ng-messages=\"apoNewForm.cliObserv.$error\"> <div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div> </div> </md-input-container> </div> <md-divider class=\"clear-min\"> <div layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"(apoNewForm.$invalid=true) && sendNewAppo()\" class=\"md-primary md-hue-2\" ng-disabled=\"apoNewForm.$invalid\"> <span lnt-id=\"form.send\">Enviar</span> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-accents\">send</md-icon> </md-button> <span flex></span> </div> </md-divider></md-divider></md-divider></md-divider></md-content> </form>"
  );


  $templateCache.put('views/flySaveSearchEnd.html',
    "<md-content> <md-content ng-show=\"!errorSave\"> <md-toolbar> <div class=\"md-toolbar-tools notif cab\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools notif\"> <span flex></span> <span ng-show=\"!adminOption\" flex>{{findLangTextElement(\"label.notification.bookedApo.title\")}}</span> <span ng-show=\"adminOption\" flex>{{findLangTextElement(\"label.notification.bookedApoAdmin.title\")}}</span> <span flex></span> </div> </md-toolbar> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item class=\"md-1-line\"> <p ng-show=\"!adminOption\">{{findLangTextElement(\"label.notification.bookedApo.text\")}}</p> <p ng-show=\"adminOption\">{{findLangTextElement(\"label.notification.bookedApoAdmin.text\")}}</p> </md-list-item> <md-divider> <md-list-item> <div layout=\"column\" layout-align=\"center start\" layout-gt-sm=\"row\" layout-align-gt-sm=\"start center\"> <div layout=\"row\" style=\"margin-right: 32px\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">today</md-icon> <p>{{formatDateSelected()}}</p> </div> <div layout=\"row\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">schedule</md-icon> <p>{{appo.appoSel.apoName}}</p> </div> </div> </md-list-item> <md-divider> <md-list-item ng-show=\"local.locNumPersonsApo > 1\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person_add</md-icon> <p>{{findLangTextElement(\"label.html.apoFor1\")}} {{personscope.numPersons}} {{findLangTextElement(\"label.html.apoFor2\")}}</p> </md-list-item> <md-list-item ng-show=\"!isAdveo\" ng-repeat=\"numPerson in personscope.persons | limitTo:personscope.numPersons\"> <md-icon ng-if=\"local.locNumPersonsApo == 1\" md-font-library=\"material-icons\" class=\"md-24\">build</md-icon> <md-icon ng-if=\"local.locNumPersonsApo > 1\" md-font-library=\"material-icons\" class=\"md-24\">looks_{{icons_num($index)}}</md-icon> <p ng-show=\"!tasMultiple\">{{personscope.selectedTasksPersons[$index][0].tasName}}</p> <p ng-show=\"tasMultiple\">{{personscope.selectedTasksPersonsStr[$index]}}</p> </md-list-item> <md-list-item ng-show=\"isAdveo\"> <p>{{findLangTextElement(\"label.template.numLines\")}}: {{personscope.selectedTasksPersons[0][0].numLines}}</p> <p>{{findLangTextElement(\"label.template.numPallets\")}}: {{personscope.selectedTasksPersons[0][0].numPallets}}</p> </md-list-item> <md-list-item ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 fmd-hue-3\">perm_contact_calendar</md-icon> <p>{{findLangTextElement(\"label.header.places\")}}: {{selCalendar.selectedCalendar[0].calName}}</p> </md-list-item> <md-divider> <md-list-item> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person</md-icon> <p>{{client.name}}</p> </md-list-item> <md-list-item> <div layout=\"column\" layout-align=\"center start\" layout-gt-sm=\"row\" layout-align-gt-sm=\"start center\"> <div layout=\"row\" style=\"margin-right: 32px\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">email</md-icon> <p>{{client.email}}</p> </div> <div layout=\"row\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">phone</md-icon> <p>{{client.telf}}</p> </div> </div> </md-list-item> <md-list-item ng-show=\"celebration.isCelebration\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">today</md-icon> <p>{{formatDateCelebration()}}</p> </md-list-item> <md-list-item ng-show=\"client.observ!=''\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">insert_comment</md-icon> <p>{{client.observ}}</p> </md-list-item> </md-divider></md-divider></md-divider></md-list> </div> </md-divider></md-content> <md-content ng-show=\"errorSave\"> <md-toolbar> <div class=\"md-toolbar-tools notif cab\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools notif notsave\"> <span flex></span> <span flex>{{findLangTextElement(\"label.notification.errorBase.title\")}}</span> <span flex></span> </div> </md-toolbar> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item class=\"md-1-line\"> <p>{{errorSave}}</p> </md-list-item> </md-list> </div> </md-divider></md-content> <md-divider class=\"clear-min\"> <div layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"initSearch(1)\" class=\"md-primary md-hue-2\"> <span lnt-id=\"form.accept\">Aceptar</span> </md-button> <span flex></span> </div> </md-divider></md-content>"
  );


  $templateCache.put('views/flySearch.html',
    "<form> <md-content> <div layout=\"column\"> <md-list> <div class=\"block-sep\"> <div layout=\"column\" layout-gt-sm=\"row\"> <md-list-item class=\"md-2-line\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">email</md-icon> <div class=\"md-list-item-text taskSelect\" ng-controller=\"LocOriginContrAuto as ctrl\"> <md-autocomplete class=\"md-2-line\" md-floating-label=\"{{findLangTextElement('fly.location.origin')}}\" md-min-length=\"1\" md-input-name=\"locOrigin\" md-input-maxlength=\"100\" md-no-cache=\"true\" md-items=\"location in ctrl.querySearch(ctrl.searchText)\" md-item-text=\"location.name\" md-selected-item=\"ctrl.selectedItem\" md-search-text=\"ctrl.searchText\" md-escape-options=\"clear\" md-clear-button=\"true\"> <md-item-template> <md-icon ng-show=\"location.type=='CITY'\" md-font-library=\"material-icons\" class=\"md-24\">location_city</md-icon> <span ng-show=\"location.type=='CITY'\" style=\"font-weight: bold\">{{location.name}}</span> <md-icon ng-show=\"location.type=='AIRPORT'\" md-font-library=\"material-icons\" class=\"md-24\">local_airport</md-icon> <span ng-show=\"location.type=='AIRPORT'\">{{location.name}}</span> </md-item-template> </md-autocomplete> </div> <md-icon md-font-library=\"material-icons\" class=\"md-24\">email</md-icon> <div class=\"md-list-item-text taskSelect\" ng-controller=\"LocDestContrAuto as ctrl\"> <md-autocomplete class=\"md-2-line\" md-floating-label=\"{{findLangTextElement('fly.location.dest')}}\" md-min-length=\"1\" md-input-name=\"locOrigin\" md-input-maxlength=\"100\" md-no-cache=\"true\" md-items=\"location in ctrl.querySearch(ctrl.searchText)\" md-item-text=\"location.name\" md-selected-item=\"ctrl.selectedItem\" md-search-text=\"ctrl.searchText\" md-escape-options=\"clear\" md-clear-button=\"true\"> <md-item-template> <md-icon ng-show=\"location.type=='CITY'\" md-font-library=\"material-icons\" class=\"md-24\">location_city</md-icon> <span ng-show=\"location.type=='CITY'\" style=\"font-weight: bold\">{{location.name}}</span> <md-icon ng-show=\"location.type=='AIRPORT'\" md-font-library=\"material-icons\" class=\"md-24\">local_airport</md-icon> <span ng-show=\"location.type=='AIRPORT'\">{{location.name}}</span> </md-item-template> </md-autocomplete> </div> </md-list-item> </div> </div> <div class=\"block-sep\"> <md-toolbar ng-show=\"!isViewLoading && searchInputScope.dateOrigin\"> <div class=\"md-toolbar-tools date\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools date year\" layout-align=\"start center\"> <span flex ng-bind=\"extractYear()\"></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools date\" layout-align=\"start center\"> <span flex=\"70\" ng-bind=\"extractDayWeekMonth()\"></span> <span flex></span> <md-button ng-click=\"toggleShowCalendar()\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-light\">arrow_drop_down_circle</md-icon> </md-button> </div> </md-toolbar> </div> <div class=\"block-sep\"> <md-list-item class=\"md-2-line\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person</md-icon> <div class=\"md-list-item-text\"> <p lnt-id=\"fly.travelers.text\"></p> </div> </md-list-item> <md-list-item class=\"md-2-line\"> <md-icon></md-icon> <div class=\"md-list-item-text taskSelect\"> <a ng-href=\"\" ng-click=\"goToSelectOriginLocation()\"> <font>{{searchInputScope.selectedOriginLocationStr}}</font> </a> <md-button ng-show=\"sectionBack==null && existsMenu\" ng-click=\"goToSelectOriginLocation()\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">arrow_drop_down</md-icon> </md-button> </div> </md-list-item> </div> </md-list> </div> </md-content> </form>"
  );


  $templateCache.put('views/langs.html',
    "<div class=\"md-toolbar-tools\"> <md-radio-group ng-model=\"langApp\" class=\"green md-hue-1\" layout=\"row\" layout-align=\"space-between center\"> <md-radio-button ng-value=\"lang.lanCode\" class=\"md-primary md-hue-2\" ng-repeat=\"lang in langs\" ng-click=\"selectLang(lang.lanCode)\"> <span>{{lang.lanName}}</span> </md-radio-button> </md-radio-group> </div>"
  );


  $templateCache.put('views/legal.html',
    "<md-content> LEGALLLLLLLLL </md-content>"
  );


  $templateCache.put('views/loading.html',
    "<div layout=\"column\" layout-align=\"center center\" class=\"loading\" ng-show=\"isViewLoading && local\"> <md-progress-circular md-mode=\"indeterminate\" md-diameter=\"75\" class=\"md-hue-2\"></md-progress-circular> </div>"
  );


  $templateCache.put('views/loadingSub.html',
    "<div layout=\"column\" layout-align=\"center center\" class=\"loading\" ng-show=\"isSubViewLoading && local\"> <md-progress-circular md-mode=\"indeterminate\" md-diameter=\"55\" class=\"md-hue-2\"></md-progress-circular> </div>"
  );


  $templateCache.put('views/modalDialogLangs.html',
    "<md-dialog aria-label=\"{{titleDialog}}\" ng-cloak> <form> <md-toolbar> <div class=\"md-toolbar-tools title\"> <span flex></span> <span flex></span> <md-button class=\"md-icon-button\" ng-click=\"cancel()\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-light\">close</md-icon> </md-button> </div> <div class=\"md-toolbar-tools\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools title\"> <span flex></span> <span flex>{{titleDialog}}</span> <span flex></span> </div> </md-toolbar> <md-dialog-content> <div class=\"md-dialog-content\"> <div layout=\"column\" layout-margin> <md-subheader class=\"md-no-sticky\">{{titleContent}}</md-subheader> <md-radio-group ng-model=\"returnObj\" layout=\"column\" layout-align=\"space-between start\"> <md-radio-button ng-value=\"lang.lanCode\" class=\"md-primary md-hue-2\" ng-repeat=\"lang in $parent.langs\" ng-click=\"selectObj(lang.lanCode)\"> {{ lang.lanName }} </md-radio-button> </md-radio-group> </div> </div> </md-dialog-content> <md-dialog-actions layout=\"row\" layout-align=\"space-around center\"> <span flex></span> <md-button ng-click=\"cancel()\" class=\"md-primary md-hue-2\"> {{cancelText}} </md-button> <md-button ng-click=\"answer()\" class=\"md-primary md-hue-2\"> {{acceptText}} </md-button> <span flex></span> </md-dialog-actions> </form> </md-dialog>"
  );


  $templateCache.put('views/modalDialogLocals.html',
    "<md-dialog aria-label=\"{{titleDialog}}\" ng-cloak> <form> <md-toolbar md-scroll-shrink> <div class=\"md-toolbar-tools title\"> <span flex></span> <span flex></span> <md-button ng-show=\"$parent.local!=null\" class=\"md-icon-button\" ng-click=\"cancel()\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-light\">close</md-icon> </md-button> </div> <div class=\"md-toolbar-tools\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools title\"> <span flex></span> <span ng-show=\"returnObj==null\" flex>{{titleDialog}}</span> <span ng-show=\"returnObj!=null\" flex>{{returnObj.locName}}</span> <span flex></span> </div> </md-toolbar> <md-dialog-content class=\"detail\"> <div ng-show=\"$parent.listLocal.length>1\" class=\"md-dialog-content\"> <div layout=\"column\" layout-margin> <md-subheader class=\"md-no-sticky\">{{titleContent}}</md-subheader> <md-radio-group ng-model=\"returnObj\" layout=\"column\" layout-align=\"space-between start\"> <md-radio-button ng-value=\"localElem\" class=\"md-primary md-hue-2\" ng-repeat=\"localElem in $parent.listLocal\" ng-click=\"selectObj(localElem)\"> {{ localElem.locName }} </md-radio-button> </md-radio-group> </div> </div> <div ng-show=\"returnObj!=null\" class=\"md-dialog-content\"> <div layout=\"column\" layout-margin> <md-list> <md-list-item class=\"md-2-line\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">phone</md-icon> <div class=\"md-list-item-text\"> <p> {{ returnObj.locRespon.whoTelf1 }} </p> </div> </md-list-item> <md-divider> <md-list-item class=\"md-2-line\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">email</md-icon> <div class=\"md-list-item-text\"> <p> {{ returnObj.locRespon.whoEmail }} </p> </div> </md-list-item> <md-divider> <md-list-item class=\"md-2-line\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">home</md-icon> <div class=\"md-list-item-text\"> <p>{{ returnObj.locWhere.wheAddress }} {{ returnObj.locWhere.wheCity }}</p> </div> </md-list-item> </md-divider></md-divider></md-list> </div> </div> </md-dialog-content> <md-dialog-actions ng-show=\"$parent.listLocal.length>1\" layout=\"row\" layout-align=\"space-around center\"> <span flex></span> <md-button ng-show=\"$parent.local!=null\" ng-click=\"cancel()\" class=\"md-primary md-hue-2\"> {{cancelText}} </md-button> <md-button ng-disabled=\"returnObj==null\" ng-click=\"answer()\" class=\"md-primary md-hue-2\"> {{acceptText}} </md-button> <span flex></span> </md-dialog-actions> </form> </md-dialog>"
  );


  $templateCache.put('views/modalDialogLocations.html',
    "<md-dialog aria-label=\"{{titleDialog}}\" ng-cloak> <form> <md-toolbar> <div class=\"md-toolbar-tools title\"> <span flex></span> <span flex></span> <md-button class=\"md-icon-button\" ng-click=\"cancel()\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-light\">close</md-icon> </md-button> </div> <div class=\"md-toolbar-tools title\"> <span flex></span> <span flex=\"70\">{{titleDialog}}</span> <span flex></span> </div> </md-toolbar> <md-dialog-content> <div class=\"md-dialog-content\"> <div layout=\"column\" layout-margin> <md-subheader class=\"md-no-sticky\">{{titleContent}}</md-subheader> <div ng-cloak ng-repeat=\"task in $parent.combiTasks\"> <md-checkbox aria-label=\"task.lotName\" class=\"md-primary md-hue-2\" ng-checked=\"selectedLocationChecks.indexOf(task.id) > -1\" ng-click=\"selectObj(task.id)\"> {{ task.lotName }} </md-checkbox> </div> </div> </div> </md-dialog-content> <md-dialog-actions layout=\"row\" layout-align=\"space-around center\"> <span flex></span> <md-button ng-click=\"cancel()\" class=\"md-primary md-hue-2\"> {{cancelText}} </md-button> <md-button ng-click=\"answer()\" class=\"md-primary md-hue-2\" ng-disabled=\"selectedLocationChecks.length==0\"> {{acceptText}} </md-button> <span flex></span> </md-dialog-actions> </form> </md-dialog>"
  );


  $templateCache.put('views/modalDialogTasks.html',
    "<md-dialog aria-label=\"{{titleDialog}}\" ng-cloak> <form> <md-toolbar> <div class=\"md-toolbar-tools title\"> <span flex></span> <span flex></span> <md-button class=\"md-icon-button\" ng-click=\"cancel()\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-light\">close</md-icon> </md-button> </div> <div class=\"md-toolbar-tools title\"> <span flex></span> <span flex=\"70\">{{titleDialog}}</span> <span flex></span> </div> </md-toolbar> <md-dialog-content> <div class=\"md-dialog-content\"> <div layout=\"column\" layout-margin> <md-subheader class=\"md-no-sticky\">{{titleContent}}</md-subheader> <div ng-cloak ng-repeat=\"task in $parent.combiTasks\"> <md-checkbox aria-label=\"task.lotName\" class=\"md-primary md-hue-2\" ng-checked=\"selectedTasksPersonsChecks.indexOf(task.id) > -1\" ng-click=\"selectObj(task.id)\"> {{ task.lotName }} </md-checkbox> </div> </div> </div> </md-dialog-content> <md-dialog-actions layout=\"row\" layout-align=\"space-around center\"> <span flex ng-show=\"!tasMultiple\"></span> <md-button ng-show=\"tasMultiple\" class=\"md-fab md-mini md-primary md-hue-2\"> <span>{{selectedTasksPersonsChecks.length}}</span> </md-button> <md-button ng-show=\"tasMultiple\" ng-click=\"cleanTaskPerson()\" class=\"md-primary md-hue-2\"> {{cleanText}} </md-button> <md-button ng-click=\"cancel()\" class=\"md-primary md-hue-2\"> {{cancelText}} </md-button> <md-button ng-click=\"answer()\" class=\"md-primary md-hue-2\" ng-disabled=\"selectedTasksPersonsChecks.length==0\"> {{acceptText}} </md-button> <span flex></span> </md-dialog-actions> </form> </md-dialog>"
  );


  $templateCache.put('views/tableMonthBooking.html',
    "<md-subheader class=\"md-no-sticky md-primary\"> <div layout=\"row\" layout-align=\"space-around center\"> <div flex=\"70\" layout=\"row\" layout-align=\"start center\"> <md-button ng-click=\"loadMonth(-1)\"> <md-icon md-font-library=\"material-icons\" class=\"md-primary md-24\">navigate_before</md-icon> </md-button> <span flex=\"none\" class=\"month-year\"></span> <md-button ng-click=\"loadMonth(+1)\"> <md-icon md-font-library=\"material-icons\" class=\"md-primary md-24\">navigate_next</md-icon> </md-button> </div> <md-button ng-click=\"onToday()\"> <span lnt-id=\"label.header.today\">Today</span> <md-icon md-font-library=\"material-icons\" class=\"md-primary md-24\">today</md-icon> </md-button> <span flex></span> </div> </md-subheader> <md-divider> <md-grid-list md-cols=\"7\" md-gutter=\"0px\" md-row-height=\"16px\"> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekMon\">Mon</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekTue\">Tue</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekWed\">Wed</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekThu\">Thu</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekFri\">Fri</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekSat\">Sat</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekSun\">Sun</span> </md-grid-tile> </md-grid-list> <md-divider> <md-grid-list id=\"tableDates\" md-cols=\"7\" md-gutter=\"0px\" md-row-height=\"fit\"> <md-grid-tile ng-repeat=\"a in range(42) track by $index\" ng-click=\"disabledNextTabs(); onSelectDate($event)\"> <span id=\"date\"></span> </md-grid-tile> </md-grid-list></md-divider></md-divider>"
  );

}]);
