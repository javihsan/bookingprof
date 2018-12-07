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
		return new Date(a[0],(a[1]-1),a[2]);
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
