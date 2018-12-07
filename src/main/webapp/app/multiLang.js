

var lntData = null;

changeLang = function(lanCode) {

	asyn = __FacadeCore.Service_Settings_asyncFalse();

	url = "http://" + appHost + "/multiText/listLocaleTexts";

    data = {lanCode:lanCode,domain:appFirmDomain};
    lntData = Lungo.Core.toArray($$.json(url, data));
    langApp = lntData[0].mulLanCode;
    
    local = __FacadeCore.Cache_get (appName+"local");
    if (local && Lungo.dom("#table-month")[0]){
    	__FacadeCore.Cache_remove(appName + "selectedTasks");
    	__FacadeCore.Cache_remove(appName + "selectedTasksCount");
    	__FacadeCore.Cache_remove(appName + "combiTasks");
    	
     	data = {localId:local.id}
	 	response = $$.json(urlLocalTask, data);
	 	combiTasks = Lungo.Core.toArray (response);
	 	combiTasks = Lungo.Core.orderByProperty (combiTasks, "lotName", "asc");
	 	__FacadeCore.Cache_set(appName + "combiTasks", combiTasks);
    }
    
    __FacadeCore.Service_Settings_async(asyn);

    __this = this;
    $$("*[data-lnt-id]").each( 
    		function() { 
    			dataId = $$(this).attr("data-lnt-id");
    			langText = findLangTextElement(dataId);
    			if (langText){
    	   			if ($$(this).attr("placeholder")!=null){
	    				//console.log ("placeholder",$$(this),langText);
	    				$$(this).attr("placeholder",langText)
	    			} else {
	    				//console.log ("other",$$(this),langText);
	    				$$(this).html(langText);
	    			} 
	    		}
    		}
    );
};

findLangTextElement = function(key) {
	langElement = Lungo.Core.findByProperty(lntData, "mulKey", key);
	if (langElement){
		return langElement.mulText;
	}
	return console.error("Key not found: "+key);
};


newDateTimezone = function() {
	newDate = new Date()
	newDate.setHours((newDate.getHours() - (newDate.getTimezoneOffset() / 60)));
	newDate.setMinutes(newDate.getMinutes());
    return newDate;
};

dateToString = function(date) {
  var day, month, year;

  year = date.getFullYear();
  month = date.getMonth() + 1;
  day = date.getDate();
  return year + "-" + month + "-" + day;
};

dateToStringFormat = function(date) {
    var day, month, year;
    year = date.getFullYear();
    month = eval(findLangTextElement("general.months"))[date.getMonth()]
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
    strDayWeek = eval(findLangTextElement("general.daysWeek"))[dayWeek]
    return strDayWeek+ ", " + day + "-" + month + "-" + year;
};

dateToStringSim = function(date, sym) {
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

dateToStringYearLast = function(date) {
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

formatDate = function(strDate) {
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

dateToStringHour = function(date) {
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

stringToDate = function(dateString) {
	var a = dateString.split('-');
	return new Date(a[0],(a[1]-1),a[2]);
}

getSemDay = function(sem) {
	dayWeek = getSemDayNum(sem);
    strDayWeek = eval(findLangTextElement("general.daysWeek"))[dayWeek]
    return strDayWeek;
};

getSemDayNum = function(sem) {
	if (sem=='Mon'){
		dayWeek = 0;
	} else if (sem=='Tue'){
		dayWeek = 1;
	} else if (sem=='Wed'){
		dayWeek = 2;
	} else if (sem=='Thu'){
		dayWeek = 3;
	} else if (sem=='Fri'){
		dayWeek = 4;
	} else if (sem=='Sat'){
		dayWeek = 5;
	} else if (sem=='Sun'){
		dayWeek = 6;
	}
    return dayWeek;
};

getSemDay3Str = function(date) {
	dayWeek = date.getDay();
	if (dayWeek==0){
		dayWeek = 6;
	} else {
		dayWeek = dayWeek-1;
	}
	if (dayWeek==0){
		sem = 'Mon';
	} else if (dayWeek==1){
		sem = 'Tue';
	} else if (dayWeek==2){
		sem = 'Wed';
	} else if (dayWeek==3){
		sem = 'Thu';
	} else if (dayWeek==4){
		sem = 'Fri';
	} else if (dayWeek==5){
		sem = 'Sat';
	} else if (dayWeek==6){
		sem = 'Sun';
	}
    return sem;
};

checkValidity = function(str,pattern,required) {
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

checkValidityDate = function(strDate,required) {
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


getMessageValidity = function(obj) {
	if (obj.validity.valueMissing)
		return findLangTextElement("label.requiredData")
	else
		return findLangTextElement("label.typePatternData")
};

changeArrayToFirst = function(array,obj) {
	posObj = array.indexOf (obj);
	array.splice (posObj, 1);
	array.unshift (obj);
	return array;
};

arrHasDupes = function ( A ) {                      
	var i, j, n;
	n=A.length;
                                                    
	for (i=0; i<n; i++) {                       
		for (j=i+1; j<n; j++) {
			if (A[i]==A[j]) return A[i];
	}	}
	return null;
};


setCurrentAside = function (currentStrObj, newCurrentStrObj) {                      
	menuCurrent = $$("aside#menu li a[href=\\#"+currentStrObj+"]");
	menuCurrentLi = menuCurrent.parent();
	menuCurrentLi.removeClass("active");
	menuCurrent.removeClass("active");
		
	menuNewCurrent = $$("aside#menu li a[href=\\#"+newCurrentStrObj+"]");
	menuNewCurrentLi = menuNewCurrent.parent();
	menuNewCurrentLi.addClass("active");
	return menuNewCurrent.addClass("active");
};

hideLocalAside = function () {                      
	menuLocalSelectLi = $$("aside#menu li a[href=\\#list-local]").parent();
	return menuLocalSelectLi.hide();
};

hideProductAside = function () {                      
	menuProductSelectLi = $$("aside#menu li a[href=\\#list-product]").parent();
	return menuProductSelectLi.hide();
};

hideSalesAside = function () {                      
	menuProductSelectLi = $$("aside#menu li a[href=\\#report-sales]").parent();
	return menuProductSelectLi.hide();
};

hideClientAside = function () {                      
	menuLocalSelectLi = $$("aside#menu li a[href=\\#list-clients]").parent();
	return menuLocalSelectLi.hide();
};


getStrDiary = function (diary) {                      
	if (diary==null || diary.diaTimes==null){
		return '<div class="tag cancel">'+findLangTextElement("form.closed")+'</div>';
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
