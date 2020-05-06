var protocol_url = location.protocol+'//';
var domainOfi = 'bookingprof.com';
var domainLocalOfi = '127.0.0.1:8888';//'localhost:8888'; // arrancar en local con el java delante
//var domainLocalOfi = '127.0.0.1:9001';//'localhost:9001'; // arrancar en local solo el front
var domainSpotOfi = 'dilosohairapp.appspot.com';

var appHost = location.host;
var appServerName = appHost.split(":")[0];

var appFirmDomain = '';
var appName = '';

var historyLoc = [];

var App = {
		config : {
			version: "1.0"
		},
		onLine: true
	};

	var app = angular.module("app", ['ui.router','ngSanitize', 'ngMaterial', 'ngMessages']);
	
	app.config(["$stateProvider", "$urlRouterProvider", "$httpProvider", "$mdThemingProvider", "$mdDateLocaleProvider", function($stateProvider, $urlRouterProvider, $httpProvider, $mdThemingProvider, $mdDateLocaleProvider /*, $mdIconProvider*/){
		
		$mdThemingProvider.theme('default')
	    .primaryPalette('pink')
	    .accentPalette('purple')
	    .warnPalette('teal')
	    //.backgroundPalette('teal');
		
		$mdDateLocaleProvider.formatDate = function(date) {
			return date ? __Utils.dateToStringFormat(date) : '';
		};
		
		
//		$mdIconProvider
//    	.iconSet('social', 'images/social-icons.svg', 24)
  		
		$stateProvider
			.state('booking', {
				abstract: true,
				url: '/booking',
				template:'<div ui-view></div>',
				controller: 'BookController'
			})
			.state('booking.home', {
				url: '',
				templateUrl: 'views/bookingHome.html',
				controller: ["$scope", "$rootScope", function($scope,$rootScope){
					$scope.initBook(1);
					$rootScope.setBack(null,$scope);
		          }],
	            onEnter: ["$rootScope", function($rootScope){
	            	//console.log("onEnter booking.home");
	            	$rootScope.sectionTit = $rootScope.findLangTextElement("label.aside.bookings");
	              }]
			})
			.state('legal', {
				abstract: true,
				url: '/legal',
				template:'<div ui-view></div>',
				controller: 'LegalController'
			})
			.state('legal.home', {
				url: '',
				templateUrl: 'views/legal.html',
				controller: ["$scope", function($scope){
					$scope.initLegal();
		          }],
	            onEnter: ["$rootScope", function($rootScope){
	            	$rootScope.sectionTit = $rootScope.findLangTextElement("label.aside.info");
	               	$rootScope.setBack();
	              }]
			})
			.state('clients', {
				abstract: true,
				url: '/clients',
				template:'<div ui-view></div>',
				controller: 'ClientsController'
			})
			.state('clients.home', {
				url: '',
				templateUrl: 'views/clients.html',
				controller: ["$scope", function($scope){
					$scope.initClients();
		          }],
	            onEnter: ["$rootScope", function($rootScope){
	            	$rootScope.sectionTit = $rootScope.findLangTextElement("label.aside.clients");
	               	$rootScope.setBack();
	              }]
			});			
		
		
		$urlRouterProvider.otherwise(
				function($injector, $location) {
			//console.log('path: ',$location.path());
			$location.replace().path("/booking");
		});
		
		//$httpProvider.interceptors.push('myHttpInterceptor');
	
	}]);
	

	// register the interceptor as a service
	app.factory('myHttpInterceptor', ["$q", "$rootScope", function($q, $rootScope) {
		return {
			
			// optional method
			'responseError' : function(rejection) {
				//console.log("errorCall",rejection);

				var errorCode, errorText, funCall;
				errorCode = rejection.status;
				if (!errorCode || errorCode==-1){
					if ($rootScope.findLangTextElement("label.notification.errorBase.text")){
						errorText = $rootScope.findLangTextElement("label.notification.errorBase.text");
					} else {
						errorText = "Error RED";
					}	
				} else { // Buscamos el errror dentro del response porque 
					errorText = rejection.statusText;
				}
				if (errorCode!=409){
					funCall = null;
//						funCall = function() {
//							if (Lungo.dom("#booking")[0]){
//								__FacadeCore.Router_article("booking","table-month");
//							}
//						};
				} else { // Estamos en el caso de no refrescar la pantalla por errores de formulario
					funCall = null;
				}
				$rootScope
					.openNotif(
						//$rootScope.findLangTextElement("label.notification.errorBase.title"),
						errorText,
						3,
						funCall);
				
				return $q.reject(rejection);
			}
		};
	}]);
	
	app.factory('cacheService', ["$cacheFactory", function($cacheFactory) {   
		return $cacheFactory('cache-service');
	}]); 

	app.run([
		'$rootScope',
		'$state',
		'httpService',
		'cacheService',
		'$mdSidenav', 
		'$mdDialog',
		'$mdToast',
		function($rootScope, $state, httpService, cacheService, $mdSidenav, $mdDialog, $mdToast) {
			
			$rootScope.$on('$stateChangeSuccess', function() {
				//console.log ("$stateChangeSuccess", $state.current.name);
				historyLoc.push($state.current.name);
		    });

		    $rootScope.historyBack = function () {
		        var prevUrl = historyLoc.length > 1 ? historyLoc.splice(-2)[0] : "/";
		        //console.log ("historyBack to ", prevUrl);
		        $state.go(prevUrl);
		    };
			
			$rootScope.goBack = function() {
				strRun = "";
				if ($rootScope.currentScope) {
					strRun += "$rootScope.currentScope.";
				}
				strRun += $rootScope.sectionBack;
				//console.log('goBack',strRun);
				eval(strRun);
			};

			$rootScope.setBack = function(fun, scope) {
				//console.log ("setBack",fun,scope);
				if (fun == undefined) {
					fun = null;
				}
				if (scope == undefined) {
					scope = null;
				}
				$rootScope.sectionBack = fun;
				$rootScope.currentScope = scope;
			};
			
			$rootScope.toggleSidenav = function(optClose) {
				//console.log("toggleSidenav",optClose)
				if (!optClose ||(optClose && $mdSidenav('left').isOpen())){    
					$mdSidenav('left').toggle();
				}	
			};
			
			
			$rootScope.changeLang = function(lanCode,fun) {
				//console.log ("changeLang",lanCode,fun!=null);
				
				__Utils = new Utils($rootScope);
				__FacadeCore = new FacadeCore(cacheService);
						
				var url = protocol_url + appHost + "/multiText/listLocaleTexts";
				//var url = "/js/lang_es.json" // Para rapidez al debugear solo con front
				var data = {lanCode:lanCode,domain:appFirmDomain};
								
				httpService.GET(url,data).then(
					function(response) {
						$rootScope.langApp = response.data[0].mulLanCode;
						$rootScope.langAppName = $rootScope.langApp.substr(0,2).toUpperCase();
						$rootScope.lntData = response.data;
						
						$rootScope.selectedTasks = undefined;
						$rootScope.selectedTasksCount = undefined;
											 
						if (fun){
							fun();
						} 
						
					}
				);
			};
			
			$rootScope.findLangTextElement = function(key) {
				if ($rootScope.lntData){
					langElement = __Utils.findByProp($rootScope.lntData, "mulKey", key);
					if (langElement){
						return langElement.mulText;
					}
					return "incorre";
				}
				return "";
			};
				   	
		   	/* LOCALS */
			// Cambiamos el local
			$rootScope.selectLocal = function(localId) {
				//console.log("selectLocal", localId);
				if (!$rootScope.local 
						|| ($rootScope.local && $rootScope.local.id != localId)) {
					$rootScope.isViewLoading = true;
					__FacadeCore.Storage_set(appName+ "localId", localId);
					return $rootScope.localReady();
				}
			};
			
		   	$rootScope.showLocals = function(titleDialog, titleContent) {
				//console.log ("showLocals: titleDialog, titleContent",titleDialog, titleContent);
				$mdDialog.show({
			      controller: DialogLocalController,
			      templateUrl: 'views/modalDialogLocals.html',
			      parent: angular.element(document.body),
			      clickOutsideToClose:true,
		          locals: { titleDialog: titleDialog, titleContent: titleContent}
			    })
			    .then(function(obj) {
			    	$rootScope.selectLocal(obj.id);
			    	$rootScope.toggleSidenav('close');
					$rootScope.openNotif($rootScope.findLangTextElement("local.selected.text") + " " + obj.locName, 2, null);
			    });
				
			};
			
			var DialogLocalController = function ($scope, $mdDialog, titleDialog, titleContent) {

				$scope.acceptText = $rootScope.findLangTextElement("form.accept");
				$scope.cancelText = $rootScope.findLangTextElement("form.cancel");
				
				$scope.titleDialog = titleDialog;
			    $scope.titleContent = titleContent;

			    $scope.returnObj = undefined;
			    if ($rootScope.local){
			    	$scope.returnObj = __Utils.findByProp($rootScope.listLocal, 'id', $rootScope.local.id);
			    }
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

			};
			DialogLocalController.$inject = ["$scope", "$mdDialog", "titleDialog", "titleContent"];
			
			$rootScope.openNotif = function (titleContent, timeHideDelay, funPos) {
				var toast = $mdToast.simple()
		          .content(titleContent)
		          //.action('OK')
		          .hideDelay(timeHideDelay*1000)
		          //.highlightAction(false)
		          .parent($("#contentParent")[0])
		          .position('bottom right');
		          
		        $mdToast.show(toast).then(function(response) {
		        	//console.log("funPos:",funPos);
//		        	if ( response == 'ok' ) {
		        		if (funPos) {
		        			funPos();
		        		}
	//	            }
		        });   
		   	};
			 	
			console.log ("readyApp");
			
			$rootScope.langApp = '';
			$rootScope.langAppName = '';
			$rootScope.lntData = undefined;
			$rootScope.local = undefined;
			$rootScope.showLang = false;
			$rootScope.isViewLoading = undefined; 
			$rootScope.selectedDate = undefined;

			if (appHost.toLowerCase() == "www." + domainOfi
					|| appHost.toLowerCase() == "app." + domainOfi
					|| appHost.toLowerCase() == domainLocalOfi
					|| appHost.toLowerCase().indexOf(domainSpotOfi) != -1) {
				
				console.log ("Sin dominio propio");
				
				a = location.pathname.split("/");
				appFirmDomain = a[1];
				//appFirmDomain = 'demo' // Para local arrancado solo con front
				//appHost = '127.0.0.1:8888';//'localhost:8888';//'r8-0-0-dot-dilosohairapp.appspot.com'//'localhost:8888' //Para tirar de un determinado back
				
				appHost += '/'+appFirmDomain;
				appName = 'BookingProf-' + appFirmDomain;
				
				// set Text multiLanguaje
				$rootScope.changeLang($rootScope.langApp);

			} else {
	
				//console.log ("Dominio propio: ",appServerName);

				var url = protocol_url + appHost + "/firm/getDomainServer";
				var data = {server:appServerName};
				
				httpService.GET(url,data).then(
					function(response) {
				
						appFirmDomain = response.data;

						appHost += '/'+appFirmDomain;
						appName = 'BookingProf-' + appFirmDomain;
					
						// set Text multiLanguaje
						$rootScope.changeLang($rootScope.langApp);

					}
				);	
			}
		
		}
	]); 
	
	app.directive('lntId', ["$compile", function($compile) {
	    return {
	        restrict: 'A',
	        link: function(scope,element, attrs)
	        {
	        	element.removeAttr('lnt-id');
	        	element.attr('ng-bind', "findLangTextElement('"+attrs.lntId+"')");
	        	content = element[0].outerHTML;
	        	element.html($compile(content)(scope));
	        }
	    };
	}]);
	
	app.directive('loading', function() {
		return {
			templateUrl : 'views/loading.html',
			restrict : 'E'
		};
	});

	app.directive('loadingSub', function() {
		return {
			templateUrl : 'views/loadingSub.html',
			restrict : 'E'
		};
	});
	
    function onOnline(){ 
    	App.onLine = true; 
    }
    function onOffline(){ 
    	App.onLine = false; 
    }
	
    var CordovaInit = function() {
    	 
    	var onDeviceReady = function() {
    		document.addEventListener("online", onOnline, false);
    	    document.addEventListener("offline", onOffline, false);
    		if(navigator.network.connection.type == Connection.NONE) {
    	    	App.onLine = false;
    		}
    		FastClick.attach(document.body);
    		AngularInit('deviceready');
    	};
    	 
    	var AngularInit = function(event) {
    		//console.log('AngularInit, bootstrapping application setup.');
    		angular.bootstrap($('body'), ['app']);
    	};
    	 
    	this.bindEvents = function() {
    		document.addEventListener('deviceready', onDeviceReady, false);
    	};
    	 
    	//If cordova is present, wait for it to initialize, otherwise just try to
    	//bootstrap the application.
    	if (window.cordova !== undefined) {
    		//console.log('Cordova found, wating for device.');
    		this.bindEvents();
    	} else {
    		//console.log('Cordova not found, booting application');
    		AngularInit('manual')
    	}
    };
    	 
    $(function() {
    	console.log('Bootstrapping!');
    	new CordovaInit();
    }); 
	
    
    app.factory('httpService', ["$http", function($http) {   
    	var httpService = {};
    	
    	httpService.GET = function(url, data) { 
    		if (data==null) data = [];
    		var keys = Object.keys(data);
    		var strParam = "";
    		angular.forEach(keys, function (key) {
    			if (strParam!="") strParam += "&";
    			else strParam += "?";
    			strParam += key+"="+eval("data."+key);
    		});
    		url += strParam;
    
    		var config = {timeout:45*1000};
    		
    		//console.log ("Llamando a GET ... "+url,config);
    		return $http.get(url,config); 
    	}   
		httpService.POST = function(url, data) { 
			if (data==null) data = [];
    		var keys = Object.keys(data);
    		var strParam = "";
    		angular.forEach(keys, function (key) {
    			if (strParam!="") strParam += "&";
    			else strParam += "?";
    			strParam += key+"="+eval("data."+key);
    		});
    		url += strParam;
	
    		var config = {timeout:45*1000};
    		//console.log ("Llamando a POST ... "+url,config);
    		return $http.post(url,data,config); 
    	}
    	return httpService; 
    }]); 


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
		    this.setToday();
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
		if (dd<today || dd>maxDate){
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
	
	this.setToday = function() {
		var date = new Date();
		var obj = this.elementId.find("span[datetime='"+date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+"']")
		obj.addClass('today');
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


app.controller("MenuBehaviour", [
  		"$scope", "$location", "$rootScope", 
		function($scope, $location, $rootScope) {
  			
  			$scope.previusPath = "/booking";
  			
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
				"BookController",
				[
						"$scope",
						"$state",
						"httpService",
						'$anchorScroll',
						"$rootScope",
						"$filter",
						"$interval",
						"$mdDialog",
						function($scope, $state, httpService, $anchorScroll, $rootScope, $filter, $interval, $mdDialog) {
							// Se esta cargando la pagina
							$rootScope.isViewLoading = true;
							
							$scope.childAuto = {};

							// Para evitar error en el html angular, anchura de tabla de dias
							$scope.appo = {};
							$scope.appo.cols = 1;							
							
							$scope.disabledNextTabs = function() {
							    //console.log ("disabledNextTabs");
							  $scope.tabsBook[1].disabled = true;
								$scope.tabsBook[2].disabled = true;
								$scope.tabsBook[3].disabled = true;
							    
							};
							
							$scope.toggleSelectDate = function() {
								//console.log ("toggleSelectDate");
								$scope.showSelectedDate = !$scope.showSelectedDate;
							};
														
							$scope.formatDateSelected = function() {
								if($rootScope.selectedDate){
									date = __Utils.stringToDate($rootScope.selectedDate);
									return __Utils.dateToStringFormat(date);
								}
							};
							
							$scope.extractYear = function() {
								if($rootScope.selectedDate){
									date = __Utils.stringToDate($rootScope.selectedDate);
									return date.getFullYear();
								}
							};
							
							$scope.extractDayWeek = function(selDate) {
								if (selDate){
									date = selDate;
									date = __Utils.stringToDate(date);
									return __Utils.dateToDayWeekDayMonthFormat(date);
								}
							};
							
							$scope.extractDayWeekMonth = function() {
								if($rootScope.selectedDate){
									date = $rootScope.selectedDate;
									date = __Utils.stringToDate(date);
									return __Utils.dateToDayWeekMonthFormat(date);
								}
							};
							
							$scope.extractDayMonth = function(sel) {
								if($rootScope.selectedDate){
									date = $rootScope.selectedDate;
									date = __Utils.stringToDate(date);
									if (sel){
										date.setDate(date.getDate() + sel);
									}
									return date.getDate();
								}
							};
							
							$scope.extractSemDay = function(sel) {
								if($rootScope.selectedDate){
									date = $rootScope.selectedDate;
									date = __Utils.stringToDate(date);
									if (sel){
										date.setDate(date.getDate() + sel);
									}
									return __Utils.dateToDayWeekFormat(date);
								}
							};
							
							$scope.range = function(n) {
						        return new Array(n);
						    };
						    
						    $scope.icons_num = function(n) {
						        var icoArray = ['one','two','3','4','5','6'];
						    	return icoArray[n];
						    };
							
							$scope.goToBookingHome = function() {
								//console.log("goToBookingHome");
								$state.go('booking.home');
							};
							
							
							// Al iniciar la pantalla de Booking
							$scope.initBook = function(reset) {
								//console.log("initBookingIntento",$rootScope.sectionTit);
								if ($rootScope.firm) {
									//console.log("initBook",reset);
									
									$rootScope.isViewLoading = true;
									$rootScope.existsMenu = true;

								 	var tabsBook = [
							   	          { title: $rootScope.findLangTextElement("tab.tabEvent.title"), disabled: false},
							   	          { title: $rootScope.findLangTextElement("tab.tabHour.title"), disabled: true},
							   	          { title: $rootScope.findLangTextElement("tab.tabClient.title"), disabled: true},
							   	          { title: $rootScope.findLangTextElement("tab.tabEnd.title"), disabled: true}
						   	            ];
								 	
								 	$scope.tabsBook = tabsBook;
								 	$scope.selectedTabIndex = 0;

//							   	$scope.addTab = function (title, view) {
//							        view = view || title + " Content View";
//							        tabs.push({ title: title, content: view, disabled: false});
//							      };
//							   $scope.removeTab = function (tab) {
//							        var index = tabs.indexOf(tab);
//							        tabs.splice(index, 1);
//							      };
									
									if (reset){
										
										$rootScope.sectionTit = $rootScope.findLangTextElement("label.aside.bookings");
										
										$scope.selectedTabIndex = 0;
										$scope.tabsBook[1].disabled = true;
										$scope.tabsBook[2].disabled = true;
										$scope.tabsBook[3].disabled = true;
										
										$rootScope.selectedDate = undefined;
										$rootScope.selectedTasksCount = undefined;
										$scope.showSelectedDate = true;
										$scope.personscope = undefined;
										
										$scope.appo = {};
										$scope.appo.cols = 1;	
										
										$scope.client = {};

									}
									
									var a, newDayAux;
				
									if ($rootScope.firm.firBilledModule === 0) {
										// this.footerInvoice.hide();
									}

									if ($rootScope.operatorRead) {
										// this.footerBook.hide();
									}

									if($rootScope.selectedDate){
										a = $rootScope.selectedDate.split('-');
										newDayAux = new Date(a[0], a[1] - 1, a[2]);
									} else {
										newDayAux = new Date();
									}	

									var stopCal = $interval(function() {
										if ($("#table-month")) {
											$scope.createCalendar(newDayAux);
											// impedimos que se vuelva a ejecutar esta funcion
											$interval.cancel(stopCal);
										}
									}, 250);
								}
							};

							$scope.createCalendar = function(newDayAux) {
								//console.log("createCalendar",newDayAux);
								var openDaysAux, weekDaysClosed, tableMonth;

								tableMonth = $("#table-month");
								openDaysAux = $rootScope.local.locOpenDays;
								weekDaysClosed = $rootScope.local.locSemanalDiary.closedDiary;
								create($scope, httpService, tableMonth, weekDaysClosed, {
										date : newDayAux,
										openDays : openDaysAux
									});
								return $scope.initSelectTask();
							};

							$scope.onToday = function() {
								//console.log("onToday");
								var newDayAux = new Date();
								return $scope.createCalendar(newDayAux);
							};

							$scope.loadMonth = function(direction) {
								//console.log("loadMonth",direction);
								return loadMonthDelta(direction);
							};
							
							$scope.onSelectDate= function(event) {
								//console.log("onSelectDate",event);
								var elemen = $(event.currentTarget).find("span#date");
								if (!elemen.parent().hasClass('date_closed')) {
									if ($rootScope.adminOption
											|| !elemen.parent().hasClass('date_not_enabled')) {
										var selectedDate = elemen.attr("datetime");
										$rootScope.selectedDate = new String(selectedDate);
										$scope.toggleSelectDate();
									}
								}
							};
							
							// Al iniciar la pantalla de SelectTask
							$scope.initSelectTask = function() {
								//console.log("initSelectTask", $scope.personscope);
								
								if (!$scope.personscope){
									$scope.personscope = {};
									$scope.personscope.persons = [];
									for ( var i = 1; i <= $rootScope.local.locNumPersonsApo; i++) {
										var optObj =  { id: i, name: i.toString() };
										$scope.personscope.persons.push(optObj);
									}
									$scope.personscope.numPersons = 1;
									$scope.personscope.selectedTasksPersons = [];
									$scope.personscope.selectedTasksPersonsStr = [];
									
									$scope.selCalendar = {};
									$scope.selCalendar.calendars = [];
									$scope.selCalendar.selectedCalendar = [];
								}
								
								return $scope.changeNumPersons();

							};

							$scope.changeNumPersons = function() {
								//console.log("changeNumPersons");

								var defaultTask, selectedTasksPer, taskSelPer, h, i, ind;

								defaultTask = __Utils.findByProp($rootScope.combiTasks, "id", $rootScope.local.locTaskDefaultId);
								taskSelPer = {
									id : defaultTask.id,
									tasName : defaultTask.lotName
								};
								if (appFirmDomain === 'adveo') {
									taskSelPer.numLines = 1;
									taskSelPer.numPallets = 1;
								}
								ind = 0;
								for (h = 0; h < $scope.personscope.numPersons; h++) {
									selectedTasksPer = $scope.personscope.selectedTasksPersons[h];
									if (!selectedTasksPer) {
										if ($rootScope.selectedTasksCount
												&& $rootScope.selectedTasksCount[h]) {
											selectedTasksPer = new Array();
											for (i = 0; i <= $rootScope.selectedTasksCount[h]; i++) {
												selectedTasksPer[i] = $rootScope.selectedTasks[ind];
												i++;
												ind++;
											}
										} else {
											selectedTasksPer = new Array();
											selectedTasksPer[0] = taskSelPer;
										}
										$scope.personscope.selectedTasksPersons[h] = selectedTasksPer;
									}	
								}

								if (appFirmDomain == 'adveo') {
									$scope.isAdveo = true;
									//return this.showTasksGoods();
								} else {
									$scope.isAdveo = false;
									//return this.showTasks();
								}
								return $scope.showTasks();
							};

							$scope.showTasks = function() {
								//console.log("showTasks");
								var h, i, selectedTasksPer, strTask, tasksSelect;
								for (h = 0; h < $scope.personscope.numPersons; h++) {
									selectedTasksPer = $scope.personscope.selectedTasksPersons[h];
									strTask = "";
									for (i = 0; i < selectedTasksPer.length; i++) {
										if (i > 0) {
											strTask += " , ";
										}
										strTask += selectedTasksPer[i].tasName;
									}
									$scope.personscope.selectedTasksPersonsStr[h] = strTask; 
								}
								if ($rootScope.local.locSelCalendar == 1) {
									$scope.fillSelCalendar();
								}
								return $rootScope.isViewLoading = false;
							};
							
							$scope.fillSelCalendar = function() {
								//console.log("fillSelCalendar");
								var data, h, j, i, selectTaskParam, selectedTasksPer, url;
								j = 0;
								selectTaskParam = new Array()
								for (h = 0; h < $scope.personscope.numPersons; h++) {
									selectedTasksPer = $scope.personscope.selectedTasksPersons[h];
									for (i = 0; i < selectedTasksPer.length; i++) {
										if (selectTaskParam.indexOf(selectedTasksPer[i].id) == -1) {
											selectTaskParam[j] = selectedTasksPer[i].id;
											j++;
										}
									}
								}
								url = protocol_url + appHost + "/calendar/listCandidate";
								data = {
									localId : $rootScope.local.id,
									selectedTasks : selectTaskParam
								};
								return httpService
										.GET(url, data)
										.then(
												function(response) {
													$scope.selCalendar.calendars = __Utils.sortByPropChar(response.data, "calName", true);
													$scope.selCalendar.selectedCalendar = [];
													for (i = 0; i < $scope.selCalendar.calendars.length; i++) {
														if ($scope.selCalendar.calendars[i].calName
																.toLowerCase()
																.indexOf("pere") !== -1) {
															$scope.selCalendar.selectedCalendar[0] = $scope.selCalendar.calendars[i].id;
														} else {
															calAux = {id:-1};
															$scope.selCalendar.selectedCalendar[0] = calAux;
														}
													}
												});
							};
							
							$scope.saveTaskSelect = function() {
								//console.log("saveTaskSelect");
								
								$rootScope.isViewLoading = true;
								
								$scope.tabsBook[1].disabled = false;
								$scope.selectedTabIndex = 1;

								
								var h, i, s, selectedTasks, selectedTasksCount, selectedTasksPer;
								selectedTasks = new Array();
						        selectedTasksCount = new Array();
						        s = 0;
								for (h = 0; h < $scope.personscope.numPersons; h++) {
									selectedTasksPer = $scope.personscope.selectedTasksPersons[h];
									for (i = 0; i < selectedTasksPer.length; i++) {
										selectedTasks[s] = selectedTasksPer[i];
										s++;
									}
							        selectedTasksCount[h] = selectedTasksPer.length;
								}
								$rootScope.selectedTasks = selectedTasks;
								$rootScope.selectedTasksCount = selectedTasksCount;

								$scope.initDayAppos();
							};
							
							/* SelectTaskPerson  ********************************************/
							
							// Ir a la pantalla de SelectTaskPerson
							$scope.goToSelectTaskPerson = function(numPerson) {
								//console.log("goToSelectTaskPerson", numPerson);
								$scope.personscope.selectedTasksNumPerson = numPerson;
								var selectedTasksPer = $scope.personscope.selectedTasksPersons[$scope.personscope.selectedTasksNumPerson-1];
								$scope.personscope.cabText = $rootScope.findLangTextElement("label.template.job");
							    if ($rootScope.local.locNumPersonsApo > 1) {
							    	$scope.personscope.cabText += " " + $rootScope.findLangTextElement("label.template.jobForPerson") + " " + $scope.personscope.selectedTasksNumPerson;
							    }
								$scope.showTaskPerson($scope.personscope.cabText, $rootScope.findLangTextElement("general.select"), selectedTasksPer);
							}
							
							$scope.showTaskPerson = function(titleDialog, titleContent, selectedTasksPer) {
								//console.log ("showTaskPerson: ",titleDialog, titleContent, selectedTasksPer);
								
						    	$mdDialog.show({
							      controller: DialogController,
							      templateUrl: 'views/modalDialogTasks.html',
							      parent: angular.element(document.body),
							      clickOutsideToClose:true,
						          locals: { titleDialog: titleDialog, titleContent: titleContent, selectedTasksPer: selectedTasksPer }
							    })
							    .then(function(obj) {
							    	if ($scope.saveTaskPerson(obj)){
							    		$scope.disabledNextTabs();	
							    	} 
							    });
							};
							
							var DialogController = function ($scope, $mdDialog, titleDialog, titleContent, selectedTasksPer) {
								
								$scope.tasMultiple = true;
								if ($rootScope.local.locMulServices == 0) {
									$scope.tasMultiple = false;
								}
								
								$scope.selectedTasksPersonsChecks = [];
								for (i = 0; i < selectedTasksPer.length; i++) {
									$scope.selectedTasksPersonsChecks.push(selectedTasksPer[i].id);
								}
								
								$scope.acceptText = $rootScope.findLangTextElement("form.accept");
								$scope.cancelText = $rootScope.findLangTextElement("form.cancel");
								$scope.cleanText = $rootScope.findLangTextElement("form.clean");

								$scope.titleDialog = titleDialog;
							    $scope.titleContent = titleContent;
							    $scope.selectedTasksPer = selectedTasksPer;

							    
							    $scope.selectObj = function(obj) {
							    	$scope.selectTaskPerson(obj);
								}
							
								$scope.hide = function() {
									$mdDialog.hide();
								};
								  
								$scope.cancel = function() {
								    $mdDialog.cancel();
								};
								  
								$scope.answer = function() {
								    $mdDialog.hide($scope.selectedTasksPersonsChecks);
								};
								
								// Al seleccionar/des un servicio en Person
								$scope.selectTaskPerson = function(taskId) {
									//console.log("selectTaskPerson", taskId);
									var idx = $scope.selectedTasksPersonsChecks.indexOf(taskId);
									if ($scope.tasMultiple){
										// is currently selected
									    if (idx > -1) {
									      $scope.selectedTasksPersonsChecks.splice(idx, 1);
									    }
									    // is newly selected
									    else {
									      $scope.selectedTasksPersonsChecks.push(taskId);
									    }
									} else {
							    		// Not is currently selected
									    if (idx == -1) {
									    	$scope.selectedTasksPersonsChecks = [];
									    	$scope.selectedTasksPersonsChecks.push(taskId);
									    }	
							    	}
								}
								
								// Limpiamos servicios en Multiple Person
								$scope.cleanTaskPerson = function() {
									//console.log("cleanTaskPerson");
									$scope.selectedTasksPersonsChecks = [];
								}
								
							}
							DialogController.$inject = ["$scope", "$mdDialog", "titleDialog", "titleContent", "selectedTasksPer"];
							
							// Salvamos seleccion de servicios en Person
							$scope.saveTaskPerson = function(selectedTasksPersonsChecks) {
								//console.log("saveTaskPerson", selectedTasksPersonsChecks);
								
								var taskSelPer = undefined;
								var taskAux = undefined;
								var selectedTasksPerIndiv = [];
								var selectedTasksPer = [];
								i = 0;
						        c = 0;
								angular.forEach(selectedTasksPersonsChecks, function (id) {
									taskAux = __Utils.findByProp($rootScope.combiTasks, "id", id);
									taskSelPer = {
										id : taskAux.id,
										tasName : taskAux.lotName
									};
									selectedTasksPer[i] = taskSelPer;
									i++;
									if (taskAux.lotTaskCombiId && taskAux.lotTaskCombiId.length > 0) {
										 var combiIds = taskAux.lotTaskCombiId;
										 for (j = 0; j<combiIds.length; j++) {
											 selectedTasksPerIndiv[c] = combiIds[j];
								             c++;
								         }
									} else {
										selectedTasksPerIndiv[c] = id;
							            c++;
									}
								});

								if (selectedTasksPer.length > 0) {
						          var duplicate = __Utils.arrHasDupes(selectedTasksPerIndiv);
						          if (duplicate) {
						            var duplicateText = __Utils.findByProp($rootScope.combiTasks, "id", duplicate).lotName;
						            $rootScope.openNotif($rootScope.findLangTextElement("label.notification.localTaskCombiDiferent.text") + " " + duplicateText + " " + $rootScope.findLangTextElement("label.notification.localTaskCombiDiferent.text2"),
											3, null);
						            return false;
						          } else {
						        	  $scope.personscope.selectedTasksPersons[$scope.personscope.selectedTasksNumPerson-1] = selectedTasksPer;
						        	  $scope.showTasks();
						        	  return true;
						          }
						        } else {
						        	 $rootScope.openNotif($rootScope.findLangTextElement("label.notification.localTaskSelectOne.text"), 2, null);
						        	 return false;
						        }
							}
							
							/* AposDay ********************************************/
							
							// date_closed or date_not_enabled
							$scope.isNotSel = function(dateApos) {
								//console.log("isNotSelIntento", dateApos);
								if ($rootScope.selectedDate){
									//console.log("isNotSel", dateApos);
									var date = __Utils.stringToDate($rootScope.selectedDate);
									date.setDate(date.getDate() + dateApos);
									var weekDaysClosed = $rootScope.local.locSemanalDiary.closedDiary;
									var dayWeek = date.getDay();
									if (dayWeek==0){
										dayWeek = 6;
									} else {
										dayWeek = dayWeek-1;
									}
									for (i=0;i<weekDaysClosed.length;i++){
										if (dayWeek==weekDaysClosed[i]) {
											return true;
										}
									}
									var today = new Date();
									today.setHours(0);
									today.setMinutes(0);
									today.setSeconds(0);
									today.setMilliseconds(0)
									var oneDay = 1000 * 60 * 60 * 24;
									var maxDate = new Date();
									maxDate.setTime(today.getTime() + ((_this.settings.openDays-1)*oneDay) );
									if (date<today || date>maxDate){
										return true;
									}
									var annual = {};
									for (var i = 0; i < $scope.annuals.length; i++) {
										annual = $scope.annuals[i];
										day = new Date(annual.anuDate);
										strDay = __Utils.dateToString(day);
										strDate = __Utils.dateToString(date);	
										if (strDate == strDay) {
											if (annual.anuClosed == 1) {
												return true;
											}
										}
									}
									return false;
								}
								return false;
							}
							
							// Al iniciar la pantalla de Booking Days
							$scope.initDayAppos = function(dateIncr,event,dateApos) {
								//console.log("initDayAppos", dateIncr, event, dateApos);
								if (event){
									var elemen = $(event.currentTarget);
									if (elemen.hasClass('date_closed') ||
										 (!$rootScope.adminOption && elemen.hasClass('date_not_enabled')) ){
										return;
									}
								}	
								
								$rootScope.isViewLoading = true;
								
								$scope.appo = {};
								$scope.appo.appointments = new Array();
								$scope.appo.appoSel = undefined;
								$scope.appo.cols = 1;
								
								var data, selectTaskParam, selectedCalendars, selectedCalendarsParam, selectedTasks, selectedTasksCount, h, i;

								selectedTasks = $rootScope.selectedTasks;
								if (selectedTasks) {
									selectedTasksCount = $rootScope.selectedTasksCount;
									selectedCalendars = $scope.selCalendar.selectedCalendar;
									if ($scope.local.locSelCalendar == 1 && selectedCalendars[0].id ==-1) {
										selectedCalendarsParam = "";
									} else {
										selectedCalendarsParam = new Array();
										for (i in selectedCalendars){
											selectedCalendarsParam[i] = selectedCalendars[i].id;
										}
									}
									
									if (dateApos){
										$rootScope.selectedDate = dateApos; 
									} else if (dateIncr){
										date = __Utils.stringToDate($rootScope.selectedDate);
										date.setDate(date.getDate() + dateIncr);
										$rootScope.selectedDate = __Utils.dateToString(date); 
									}
									
									selectTaskParam = new Array();
									for (h in selectedTasks){	
										selectTaskParam[h] = selectedTasks[h].id;
										h++;
									}
									
									data = {
										localId : $rootScope.local.id,
										selectedDate : $rootScope.selectedDate.toString(),
										selectedTasks : selectTaskParam,
										selectedTasksCount : selectedTasksCount,
										selectedCalendars : selectedCalendarsParam
									};
									if (selectedTasks[0].numLines) {
										data.numLines = selectedTasks[0].numLines;
										data.numPallets = selectedTasks[0].numPallets;
									}

									var promiseAposDay = httpService.GET($rootScope.urlListApoByDay, data);
									return promiseAposDay.then(
										function(response) {
											return $scope.showCalendarDay(response, data);
										}
									);
								} 
							};
							
							$scope.showCalendarDay = function(response, data) {
								//console.log("showCalendarDay");
								var a, appointment, bgColor, cal, h, hourAux, hours, newDayAux, num_apo, resultAppos, todayAux, top_hour, view, x, _j, _l, _len, _len1, _len2, _m;
																
								if (response.data.length > 0) {
									$scope.errorApoDay = false;
									resultAppos = __Utils.sortByPropChar(response.data,"apoName", false);
									hours = [];
									for (h =  0; h <= 23; h++) {
										hours[h] = [ 0, 0 ];
									}

									//Buscamos las horas que tienen citas
									for (_j = 0, _len = resultAppos.length; _j < _len; _j++) {
										appointment = resultAppos[_j];
										hourAux = parseInt(appointment.apoName.split(":")[0]);
										hours[hourAux][1] = 1;
									}

									//Reordenamos el orden de las horas que tienen citas
									x = -1;
									for (h = 0; h <= 23; h++) {
										if (hours[h][1] == 1) {
											x++;
											hours[h][1] = x;
										}
									}
									// Al final x+1 determina el num de horas visibles, lo que determina la altura: 60 base + numLineas * altura linea
									$scope.appo.height = 60+((x+1)*85);
									$scope.appo.cols = 1;
									
									bgColor = 0;
									$scope.appo.appointments = new Array();
									for (_l = 0, _len1 = resultAppos.length; _l < _len1; _l++) {
										appointment = resultAppos[_l];
										hourAux = parseInt(appointment.apoName.split(":")[0]);
										num_apo = hours[hourAux][0];
										if (num_apo+1>$scope.appo.cols){
											$scope.appo.cols = num_apo+1;
										}
										hours[hourAux][0] = hours[hourAux][0] + 1;
										if ($rootScope.numCals > 1
												&& $rootScope.firm.firConfig.configLocal.configLocSelCalAfter) {
											bgColor = 1;
											for (_m = 0, _len2 = $rootScope.calCandidates.length; _m < _len2; _m++) {
												cal = $rootScope.calCandidates[_m];
												if (cal.id !== appointment.apoCalendarId) {
													bgColor++;
												} else {
													break;
												}
											}
										}
										appointment.enabled = true;
										appointment.bgColor = bgColor;
										$scope.appo.appointments.push(appointment);
								  }
									//console.log("$scope.appo.appointments",$scope.appo.appointments);
									return $rootScope.isViewLoading = false;
								} else {
									$scope.errorApoDay = true;
									if (!$rootScope.adminOption){
										$rootScope.openNotif($rootScope.findLangTextElement("label.notification.notavailablesearch") + " " + $scope.formatDateSelected(), 3, null);
										$rootScope.isViewLoading = false;
										return $scope.nextDayAppos();
									} else {
										$rootScope.openNotif($rootScope.findLangTextElement("label.notification.notavailablesearch") + " " + $scope.formatDateSelected(), 3, null);
										$rootScope.isViewLoading = false;
										return $scope.nextDayAppos();
//										Lungo.Notification
//												.success(
//														findLangTextElement("label.notification.notavailableAdmin.title"),
//														findLangTextElement("label.notification.notavailableAdmin.text"),
//														null, 5);
//										a = data.selectedDate.split('-');
//										newDayAux = new Date(a[0], a[1] - 1,
//												a[2]);
//										todayAux = new Date();
//										newDayAux
//												.setHours(todayAux.getHours()
//														- (todayAux
//																.getTimezoneOffset() / 60));
//										newDayAux.setMinutes(todayAux
//												.getMinutes());
//										appointment = new __Model.Appointment({
//											enabled : true,
//											apoStartTime : newDayAux
//										});
//										__FacadeCore.Cache_remove(appName
//												+ "newApo");
//										__FacadeCore.Cache_set(appName
//												+ "newApo", appointment);
//										return __FacadeCore
//												.Router_section("#newEvent");
									}
								}
							};
							
							// Proximos dias disponibles con cita
							$scope.nextDayAppos = function() {
								//console.log("nextDayAppos");
								
								$rootScope.isSubViewLoading = true;
								
								$scope.appo = {};
								$scope.appo.appointments = new Array();
								$scope.appo.appoSel = undefined;
								$scope.appo.cols = 1;
								$scope.appo.nextDays = undefined;
								
								var data, selectTaskParam, selectedCalendars, selectedCalendarsParam, selectedTasks, selectedTasksCount, h, i;

								selectedTasks = $rootScope.selectedTasks;
								if (selectedTasks) {
									selectedTasksCount = $rootScope.selectedTasksCount;
									selectedCalendars = $scope.selCalendar.selectedCalendar;
									if ($scope.local.locSelCalendar == 1 && selectedCalendars[0].id ==-1) {
										selectedCalendarsParam = "";
									} else {
										selectedCalendarsParam = new Array();
										for (i in selectedCalendars){
											selectedCalendarsParam[i] = selectedCalendars[i].id;
										}
									}
									
									selectTaskParam = new Array();
									for (h in selectedTasks){	
										selectTaskParam[h] = selectedTasks[h].id;
										h++;
									}
									
									data = {
										localId : $rootScope.local.id,
										selectedDate : $rootScope.selectedDate.toString(),
										selectedTasks : selectTaskParam,
										selectedTasksCount : selectedTasksCount,
										selectedCalendars : selectedCalendarsParam
									};
									if (selectedTasks[0].numLines) {
										data.numLines = selectedTasks[0].numLines;
										data.numPallets = selectedTasks[0].numPallets;
									}
									var promiseNextAposDay = httpService.GET($rootScope.urlNextAposDay, data);
									return promiseNextAposDay.then(
										function(response) {
											$scope.appo.nextDays = response.data;
											return $rootScope.isSubViewLoading = false;
										}
									);
								} 
							};	
							
							/* New Appo ***************************************************/
							$scope.onSelectDayAppo = function(appointment) {
								//console.log("onSelectDayAppo",appointment);
								
								$scope.appo.appoSel = appointment;
															    
								if ($scope.local.locSelCalendar == 1){
									$scope.selCalendar.selectedCalendar[0].calName = "";
									calSel = __Utils.findByProp($scope.selCalendar.calendars, "id", $scope.selCalendar.selectedCalendar[0].id);
									if (calSel){
										$scope.selCalendar.selectedCalendar[0].calName = calSel.calName;
									}
								}
								
								// Obtener cliente
								if ($rootScope.adminOption) {  // Si el entorno es manager
									$scope.client = {};
									$scope.client.name = "";
									$scope.client.email = "";
									$scope.client.telf = "";
									$scope.client.observ = "";
								} else { 
									// Intentamos obtener el cliente de la cookie del dispositivo
									$scope.client = __FacadeCore.Storage_get(appName+ "eveClient");
									if (!$scope.client){ // Si no, lo inicializamos
										$scope.client = {};
										$scope.client.name = "";
										$scope.client.email = "";
										$scope.client.telf = "";
									}
									$scope.client.observ = "";
								}
								// Limpiamos el input auto de nombre de ciente
								$scope.childAuto.cleanSelItem();			
								
								if ($rootScope.firm.firConfig.configClient.extraBook.celebrationDate) {
									$scope.isCelebration = true;
									$scope.client.celebrationDate = new Date();
								} else {
									$scope.isCelebration = false;
								}
								
								$scope.tabsBook[2].disabled = false;
								return $scope.selectedTabIndex = 2;
								
							};
							
							$scope.formatDateCelebration = function() {
								if($scope.client && $scope.client.celebrationDate){
									return __Utils.dateToStringFormat($scope.client.celebrationDate);
								}
							};

							$scope.newCient = true;
							
							$scope.isNewClient = function() {
								return $scope.newCient;
							}
							
							$scope.actionNewClient = function(text) {
								$scope.newCient = true;
								$scope.client.name = text;
								$scope.client.email = "";
						    $scope.client.telf = "";
							}

							$scope.querySearch = function(query) {
								    var results = query ? $rootScope.clients.filter($scope.createFilterFor(query)) : $rootScope.clients,
						          deferred;
				       			return results;
							    
//							    	var urlClients = protocol_url + appHost + "/client/operator/list"
//								    var data = {domain:appFirmDomain}
//							        return httpService.GET(urlClients,data).then(
//							       		function(response) {
//							       			var results = response.data;
//							       			return results;
//										}
//									)
						    }

					
							$scope.createFilterFor = function(query) {   	
									var lowercaseQuery = query.toLowerCase();
									return function filterFn(client) {
										var lowercaseClient = client.whoName.toLowerCase();
											return (lowercaseClient.indexOf(lowercaseQuery) !== -1);
									};
							}

							$scope.selectedItemChange = function(item) {    
									if (item){
						    		//console.log('Item changed to ' + JSON.stringify(item));
										$scope.client = {};
										$scope.client.id = item.id;
						    		$scope.client.name = item.whoName;
						    		$scope.client.email = item.whoEmail;
						    		$scope.client.telf = item.whoTelf1;
						    		$scope.newCient = false;
						    	}
							}
							
							$scope.changeCliEmail = function() {    
							 //console.log('changeCliEmail ',$scope.client.email );
								var clients = $scope.client.email ? $rootScope.clients.filter($scope.createFilterForEmail($scope.client.email)) : $rootScope.clients,
										deferred;
								if (clients.length==1){
									// Asignamos el cliente
								  $scope.childAuto.setSelItem(clients[0]);	
								}
							}
							 
							$scope.createFilterForEmail = function(email) {   	
								//console.log('createFilterForEmail ',email );
								if (email){
									var lowercaseQuery = email.toLowerCase();
									return function filterFn(client) {
										if (client.whoEmail){
											var lowercaseClient = client.whoEmail.toLowerCase();
											if (lowercaseClient === lowercaseQuery){
												return client;
											}
										}			
									};
								}	
						}

							$scope.sendNewAppo = function() {
								//console.log("sendNewAppo");
																
								var a, appointment, startTime, data, eveClient, selectedTasks, selectTaskParam, taskSel, selectedTasksCount, selectedCalendarsParam, selectedCalendars, calendarSel, promiseListLocal;
								$rootScope.isViewLoading = true;
							   
								appointment = $scope.appo.appoSel;
								startTime = appointment.apoStartTime;
								/*if ($rootScope.adminOption) {
									a = this.eveTime.val().split(':');
									startTime = new Date(startTime);
									startTime.setUTCHours(a[0]);
									startTime.setUTCMinutes(a[1]);
									startTime = startTime.getTime();
									}
								}*/
								data = {
									eveDescAlega: $scope.client.observ,
									localId : $rootScope.local.id,
									eveStartTime: startTime
								};
								if ($scope.newCient == false){
									data.cliId = $scope.client.id;
								}
								data.cliName = $scope.client.name;
								data.cliEmail = $scope.client.email;
								data.cliTelf = $scope.client.telf;
																		
								selectedTasks = $rootScope.selectedTasks;
								selectTaskParam = new Array();
								for (i in selectedTasks){
									selectTaskParam[i] = selectedTasks[i].id;
								}
								data.selectedTasks = selectTaskParam;
								
								selectedTasksCount = $rootScope.selectedTasksCount;
								data.selectedTasksCount = selectedTasksCount;
								
								selectedCalendars = $scope.selCalendar.selectedCalendar;
								if ($scope.local.locSelCalendar == 1 && selectedCalendars[0].id ==-1) {
									selectedCalendarsParam = "";
								} else {
									selectedCalendarsParam = new Array();
									for (i in selectedCalendars){
										selectedCalendarsParam[i] = selectedCalendars[i].id;
									}
								}
								data.selectedCalendars = selectedCalendarsParam;
							
								if (selectedTasks[0].numLines) {
									data.numLines = selectedTasks[0].numLines;
									data.numPallets = selectedTasks[0].numPallets;
								}
								
								if ($scope.isCelebration){
									data.celebrationDate = __Utils.dateToStringYearLast($scope.client.celebrationDate);
								}
								
								promiseSave = httpService.POST($rootScope.urlEventNew, data);
						
								var thenSave = function(response) {
									//console.log("thenSave");
									$scope.errorSave = undefined;
									if (!$rootScope.adminOption) {	
										__FacadeCore.Storage_set(appName + "eveClient", null);
										__FacadeCore.Storage_set(appName + "eveClient", $scope.client);
									}
									$rootScope.isViewLoading = false;
									
									$scope.tabsBook[3].disabled = false;
									$scope.selectedTabIndex = 3;
									
									$scope.tabsBook[0].disabled = true;
									$scope.tabsBook[1].disabled = true;
									return $scope.tabsBook[2].disabled = true;
								};

								var errorSave = function(response) {
									//console.log("errorSave");
									$scope.errorSave = response.statusText;
									$rootScope.isViewLoading = false;
									$rootScope.openNotif(response.statusText, 5, null);
									
									$scope.tabsBook[3].disabled = false;
									$scope.selectedTabIndex = 3;
										
									$scope.tabsBook[0].disabled = true;
									$scope.tabsBook[1].disabled = true;
									return $scope.tabsBook[2].disabled = true;
								};
					
							  promiseSave.then(thenSave, errorSave);
		
							};	
							
						
						} ]);

						app
						.controller(
								"BookControllerAuto",
								[
										"$scope",
										"$rootScope",
										function($scope, $rootScope) {

						var parentScope = $scope.$parent.$parent;
						parentScope.childAuto = $scope;
						
						var self = this;
						self.querySearch =  $scope.querySearch;
						self.selectedItemChange = $scope.selectedItemChange;
						self.actionNewClient = $scope.actionNewClient;
						self.isNewClient = $scope.isNewClient;
				
						$scope.cleanSelItem = function() {
							self.selectedItem = null;
							self.searchText = "";
						}

						$scope.setSelItem = function(client) {
							self.selectedItem = client;
						}
												
					} ]);
app.directive('showMonth', function() {
	return {
		templateUrl : 'views/tableMonthBooking.html',
		restrict : 'E'
	};
});

app.directive('showTasks', function() {
	return {
		templateUrl : 'views/bookingSelectTasks.html',
		restrict : 'E'
	};
});

app.directive('showAposDay', function() {
	return {
		templateUrl : 'views/bookingAposDay.html',
		restrict : 'E'
	};
});

app.directive('showAposNew', function() {
	return {
		templateUrl : 'views/bookingAposNew.html',
		restrict : 'E'
	};
});

app.directive('showAposNewAdmin', function() {
	return {
		templateUrl : 'views/bookingAposNewAdmin.html',
		restrict : 'E'
	};
});

app.directive('showAposEnd', function() {
	return {
		templateUrl : 'views/bookingAposEnd.html',
		restrict : 'E'
	};
});

//TaskSelectCtrl.prototype.validateForm = function() {
//    var error_numLines, error_numPallets, numLinesSel, numPalletsSel, result;
//
//    result = true;
//    numLinesSel = $$("#numLines");
//    error_numLines = $$("#error_numLines");
//    error_numLines.html("");
//    numPalletsSel = $$("#numPallets");
//    error_numPallets = $$("#error_numPallets");
//    error_numPallets.html("");
//    if (numLinesSel[0] && !numLinesSel[0].checkValidity()) {
//      error_numLines.html(getMessageValidity(numLinesSel[0]));
//      numLinesSel[0].focus();
//      result = false;
//    } else if (numPalletsSel[0] && !numPalletsSel[0].checkValidity()) {
//      error_numPallets.html(getMessageValidity(numPalletsSel[0]));
//      numPalletsSel[0].focus();
//      result = false;
//    }
//    return result;
//  };

app
		.controller(
				"LangsController",
				[
						"$scope", "$state", "$location", "httpService", "$rootScope", "$mdDialog", "$mdMedia",
						function($scope, $state, $location, httpService, $rootScope, $mdDialog, $mdMedia) {
							
							// Callback de changeLang para selectLang
							$scope.callSelectLang = function() {
								//console.log("estamos en booking o operator");
								if ($state.get("booking.home")) {
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
								} else if ($state.get("localTasks.home")) {
									//console.log("estamos en manager");
									return $state.go('localTasks.home');
								} else if ($state.get("reportSales.home")) {
									//console.log("estamos en report");
									return $state.go('reportSales.home');
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
    "<md-content> <md-content ng-show=\"!errorApoDay\"> <loading></loading> <div> <md-grid-list md-cols=\"5\" md-gutter=\"0px\" md-row-height=\"16px\"> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(-2)\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(-1)\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay()\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(1)\"></span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" ng-bind=\"extractSemDay(2)\"></span> </md-grid-tile> </md-grid-list> <md-divider> <md-grid-list id=\"tableDatesAux\" md-cols=\"5\" md-gutter=\"0px\" md-row-height=\"56px\"> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(-2)}\" ng-click=\"initDayAppos(-2, $event)\"> <span ng-bind=\"extractDayMonth(-2)\"></span> </md-grid-tile> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(-1)}\" ng-click=\"initDayAppos(-1, $event)\"> <span ng-bind=\"extractDayMonth(-1)\"></span> </md-grid-tile> <md-grid-tile class=\"date_not_enabled\"> <span class=\"today\" ng-bind=\"extractDayMonth()\"></span> </md-grid-tile> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(1)}\" ng-click=\"initDayAppos(1, $event)\"> <span ng-bind=\"extractDayMonth(1)\"></span> </md-grid-tile> <md-grid-tile ng-class=\"{date_not_enabled:isNotSel(2)}\" ng-click=\"initDayAppos(2, $event)\"> <span ng-bind=\"extractDayMonth(2)\"></span> </md-grid-tile> </md-grid-list> </md-divider></div> <md-divider class=\"clear\"> <div> <md-grid-list id=\"tableDays\" md-cols=\"{{appo.cols}}\" md-gutter-sm=\"4px\" md-row-height=\"fit\" style=\"height:{{appo.height}}px !important\"> <md-grid-tile ng-repeat=\"appointment in appo.appointments\" ng-click=\"onSelectDayAppo(appointment)\"> <span ng-hidess=\"appointment.bgColor>0\" class=\"calendarDayText\"> {{appointment.apoName}} <span ng-show=\"appointment.bgColor>0\" class=\"calendarDayText\"> {{appointment.apoCalendarName}} </span> <md-icon md-font-library=\"material-icons\" class=\"md-warn\">check</md-icon> </span> <!-- \t\t<div ng-show=\"appointment.bgColor>0\" class=\"calendarDaySP bg-color{{bgColor}}\" style=\"margin-top: {{appointment.apoX}}px; left: {{appointment.apoY}}%;\">\n" +
    "\t\t\t\t\t\t\t\t\t\t\t<p class=\"calendarDayText\">{{appointment.apoName}}</p>\n" +
    "\t\t\t\t\t\t\t\t\t\t\t<p class=\"calendarDayText special\">{{appointment.apoCalendarName}}</p>\n" +
    "\t\t\t\t\t\t\t\t\t\t</div> --> </md-grid-tile> </md-grid-list> </div> </md-divider></md-content> <md-content ng-show=\"errorApoDay\"> <md-toolbar> <div class=\"md-toolbar-tools notif cab\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools notif notsave\"> <span flex></span> <span flex>{{findLangTextElement(\"label.notification.notavailable.title\")}}</span> <span flex></span> </div> </md-toolbar> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item class=\"md-1-line\"> <p>{{findLangTextElement(\"label.notification.notavailable.text\")}}</p> </md-list-item> <md-list-item ng-show=\"appo.nextDays.length>0\" class=\"md-1-line\"> <p ng-show=\"!isSubViewLoading\">{{findLangTextElement(\"localTask.notavailablesearchresult\")}} {{formatDateSelected()}}</p> </md-list-item> </md-list> </div> <div ng-hide=\"appo.nextDays.length==0\"> <loading_sub></loading_sub> <md-grid-list ng-show=\"!isSubViewLoading\" id=\"tableDatesNext\" md-cols-xs=\"2\" md-cols=\"4\" md-gutter-sm=\"10px\" md-gutter=\"0px\" md-row-height=\"76px\"> <md-grid-tile ng-repeat=\"nextDay in appo.nextDays\" ng-click=\"initDayAppos(null, $event, nextDay)\"> <span ng-bind=\"extractDayWeek(nextDay)\"></span> </md-grid-tile> </md-grid-list> </div> <div ng-show=\"!isSubViewLoading && appo.nextDays.length==0\"> <md-divider class=\"clear-min\"> <div layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"initBook(1)\" class=\"md-primary md-hue-2\"> <span lnt-id=\"form.accept\">Aceptar</span> </md-button> <span flex></span> </div> </md-divider></div> </md-divider></md-content> </md-content>"
  );


  $templateCache.put('views/bookingAposEnd.html',
    "<md-content> <md-content ng-show=\"!errorSave\"> <md-toolbar> <div class=\"md-toolbar-tools notif cab\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools notif\"> <span flex></span> <span ng-show=\"!adminOption\" flex>{{findLangTextElement(\"label.notification.bookedApo.title\")}}</span> <span ng-show=\"adminOption\" flex>{{findLangTextElement(\"label.notification.bookedApoAdmin.title\")}}</span> <span flex></span> </div> </md-toolbar> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item class=\"md-1-line\"> <p ng-show=\"!adminOption\">{{findLangTextElement(\"label.notification.bookedApo.text\")}}</p> <p ng-show=\"adminOption\">{{findLangTextElement(\"label.notification.bookedApoAdmin.text\")}}</p> </md-list-item> <md-divider> <md-list-item> <div layout=\"column\" layout-align=\"center start\" layout-gt-sm=\"row\" layout-align-gt-sm=\"start center\"> <div layout=\"row\" style=\"margin-right: 32px\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">today</md-icon> <p>{{formatDateSelected()}}</p> </div> <div layout=\"row\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">schedule</md-icon> <p>{{appo.appoSel.apoName}}</p> </div> </div> </md-list-item> <md-divider> <md-list-item ng-show=\"local.locNumPersonsApo > 1\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person_add</md-icon> <p>{{findLangTextElement(\"label.html.apoFor1\")}} {{personscope.numPersons}} {{findLangTextElement(\"label.html.apoFor2\")}}</p> </md-list-item> <md-list-item ng-show=\"!isAdveo\" ng-repeat=\"numPerson in personscope.persons | limitTo:personscope.numPersons\"> <md-icon ng-if=\"local.locNumPersonsApo == 1\" md-font-library=\"material-icons\" class=\"md-24\">build</md-icon> <md-icon ng-if=\"local.locNumPersonsApo > 1\" md-font-library=\"material-icons\" class=\"md-24\">looks_{{icons_num($index)}}</md-icon> <p ng-show=\"!tasMultiple\">{{personscope.selectedTasksPersons[$index][0].tasName}}</p> <p ng-show=\"tasMultiple\">{{personscope.selectedTasksPersonsStr[$index]}}</p> </md-list-item> <md-list-item ng-show=\"isAdveo\"> <p>{{findLangTextElement(\"label.template.numLines\")}}: {{personscope.selectedTasksPersons[0][0].numLines}}</p> <p>{{findLangTextElement(\"label.template.numPallets\")}}: {{personscope.selectedTasksPersons[0][0].numPallets}}</p> </md-list-item> <md-list-item ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 fmd-hue-3\">perm_contact_calendar</md-icon> <p>{{findLangTextElement(\"label.header.places\")}}: {{selCalendar.selectedCalendar[0].calName}}</p> </md-list-item> <md-divider> <md-list-item> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person</md-icon> <p>{{client.name}}</p> </md-list-item> <md-list-item> <div layout=\"column\" layout-align=\"center start\" layout-gt-sm=\"row\" layout-align-gt-sm=\"start center\"> <div layout=\"row\" style=\"margin-right: 32px\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">email</md-icon> <p>{{client.email}}</p> </div> <div layout=\"row\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">phone</md-icon> <p>{{client.telf}}</p> </div> </div> </md-list-item> <md-list-item ng-show=\"client.celebrationDate\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">today</md-icon> <p>{{formatDateCelebration()}}</p> </md-list-item> <md-list-item ng-show=\"client.observ!=''\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">insert_comment</md-icon> <p>{{client.observ}}</p> </md-list-item> </md-divider></md-divider></md-divider></md-list> </div> </md-divider></md-content> <md-content ng-show=\"errorSave\"> <md-toolbar> <div class=\"md-toolbar-tools notif cab\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools notif notsave\"> <span flex></span> <span flex>{{findLangTextElement(\"label.notification.errorBase.title\")}}</span> <span flex></span> </div> </md-toolbar> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item class=\"md-1-line\"> <p>{{errorSave}}</p> </md-list-item> </md-list> </div> </md-divider></md-content> <md-divider class=\"clear-min\"> <div layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"initBook(1)\" class=\"md-primary md-hue-2\"> <span lnt-id=\"form.accept\">Aceptar</span> </md-button> <span flex></span> </div> </md-divider></md-content>"
  );


  $templateCache.put('views/bookingAposNew.html',
    "<form name=\"apoNewForm\"> <md-content> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item> <div layout=\"column\" layout-align=\"center start\" layout-gt-sm=\"row\" layout-align-gt-sm=\"start center\"> <div layout=\"row\" style=\"margin-right: 32px\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">today</md-icon> <p>{{formatDateSelected()}}</p> </div> <div layout=\"row\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">schedule</md-icon> <p>{{appo.appoSel.apoName}}</p> </div> </div> </md-list-item> <md-divider> <md-list-item ng-show=\"local.locNumPersonsApo > 1\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person_add</md-icon> <p>{{findLangTextElement(\"label.html.apoFor1\")}} {{personscope.numPersons}} {{findLangTextElement(\"label.html.apoFor2\")}}</p> </md-list-item> <md-divider> <md-list-item ng-show=\"!isAdveo\" ng-repeat=\"numPerson in personscope.persons | limitTo:personscope.numPersons\"> <md-icon ng-if=\"local.locNumPersonsApo == 1\" md-font-library=\"material-icons\" class=\"md-24\">build</md-icon> <md-icon ng-if=\"local.locNumPersonsApo > 1\" md-font-library=\"material-icons\" class=\"md-24\">looks_{{icons_num($index)}}</md-icon> <p ng-show=\"!tasMultiple\">{{personscope.selectedTasksPersons[$index][0].tasName}}</p> <p ng-show=\"tasMultiple\">{{personscope.selectedTasksPersonsStr[$index]}}</p> </md-list-item> <md-list-item ng-show=\"isAdveo\"> <p>{{findLangTextElement(\"label.template.numLines\")}}: {{personscope.selectedTasksPersons[0][0].numLines}}</p> <p>{{findLangTextElement(\"label.template.numPallets\")}}: {{personscope.selectedTasksPersons[0][0].numPallets}}</p> </md-list-item> <md-divider ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-list-item ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 fmd-hue-3\">perm_contact_calendar</md-icon> <p>{{findLangTextElement(\"label.header.places\")}}: {{selCalendar.selectedCalendar[0].calName}}</p> </md-list-item> </md-divider></md-divider></md-divider></md-list> </div> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\"> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person</md-icon> <label>{{findLangTextElement(\"client.name\")}}</label> <input name=\"cliName\" ng-model=\"client.name\" ng-maxlength=\"100\" required> <div ng-messages=\"apoNewForm.cliName.$error\"> <div ng-message=\"required\">{{findLangTextElement(\"label.requiredData\")}}</div> <div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div> </div> </md-input-container> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">email</md-icon> <label>{{findLangTextElement(\"client.email\")}}</label> <input type=\"email\" name=\"cliEmail\" ng-model=\"client.email\" required ng-pattern=\"/^.+@.+\\..+$/\"> <div ng-messages=\"apoNewForm.cliEmail.$error\"> <div ng-message-exp=\"['required', 'pattern']\">{{findLangTextElement(\"label.requiredData\")}} / {{findLangTextElement(\"label.typePatternData\")}}</div> </div> </md-input-container> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">phone</md-icon> <label>{{findLangTextElement(\"client.telf\")}}</label> <input type=\"tel\" name=\"cliTelf\" ng-model=\"client.telf\" required placeholder=\"9.. / 6..\"> <div ng-messages=\"apoNewForm.cliTelf.$error\"> <div ng-message=\"required\">{{findLangTextElement(\"label.requiredData\")}}</div> </div> </md-input-container> </div> <md-divider class=\"clear-min\"> <div layout=\"column\" ng-show=\"isCelebration\"> <md-input-container class=\"md-block\" flex-gt-sm> <label>{{findLangTextElement(\"client.celebrationDate\")}}</label> <md-datepicker ng-model=\"client.celebrationDate\" md-min-date=\"client.celebrationDate\"></md-datepicker> </md-input-container> </div> <md-divider class=\"clear-min\"> <div layout=\"column\"> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">insert_comment</md-icon> <label>{{findLangTextElement(\"event.com\")}}</label> <textarea name=\"cliObserv\" ng-model=\"client.observ\" ng-maxlength=\"300\"></textarea> <div ng-messages=\"apoNewForm.cliObserv.$error\"> <div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div> </div> </md-input-container> </div> <!-- <md-divider class=\"clear-min\"> --> <!-- \n" +
    "\t\t<div layout=\"column\">\t\n" +
    "\t\t\t<md-input-container class=\"md-block\" flex-gt-sm>\n" +
    "\t\t\t\t<md-icon md-font-library=\"material-icons\" class=\"md-24\">report</md-icon>\n" +
    "\t\t\t\t<label>{{findLangTextElement(\"event.legal\")}}</label> \n" +
    "\t\t\t\t <!-- <md-checkbox ng-model=\"client.legal\"> Checkbox 1: {{ data.cb1 }}</md-checkbox>--> <!-- <div ng-messages=\"apoNewForm.cliLegal.$error\">\n" +
    "\t\t\t\t\t<div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div>\n" +
    "\t\t\t\t</div> --> <!-- \t</md-input-container>\n" +
    "\t\t</div> --> <md-divider class=\"clear-min\"> <div layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"(apoNewForm.$invalid=true) && sendNewAppo()\" class=\"md-primary md-hue-2\" ng-disabled=\"apoNewForm.$invalid\"> <span lnt-id=\"form.send\">Enviar</span> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-accents\">send</md-icon> </md-button> <span flex></span> </div> </md-divider></md-divider></md-divider></md-divider></md-content> </form>"
  );


  $templateCache.put('views/bookingAposNewAdmin.html',
    "<form name=\"apoNewAdminForm\"> <md-content> <div layout=\"column\" layout-gt-sm=\"row\" layout-align=\"space-between center\"> <md-list> <md-list-item> <div layout=\"column\" layout-align=\"center start\" layout-gt-sm=\"row\" layout-align-gt-sm=\"start center\"> <div layout=\"row\" style=\"margin-right: 32px\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">today</md-icon> <p>{{formatDateSelected()}}</p> </div> <div layout=\"row\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\" style=\"margin-right: 32px\">schedule</md-icon> <p>{{appo.appoSel.apoName}}</p> </div> </div> </md-list-item> <md-divider> <md-list-item ng-show=\"local.locNumPersonsApo > 1\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person_add</md-icon> <p>{{findLangTextElement(\"label.html.apoFor1\")}} {{personscope.numPersons}} {{findLangTextElement(\"label.html.apoFor2\")}}</p> </md-list-item> <md-divider> <md-list-item ng-show=\"!isAdveo\" ng-repeat=\"numPerson in personscope.persons | limitTo:personscope.numPersons\"> <md-icon ng-if=\"local.locNumPersonsApo == 1\" md-font-library=\"material-icons\" class=\"md-24\">build</md-icon> <md-icon ng-if=\"local.locNumPersonsApo > 1\" md-font-library=\"material-icons\" class=\"md-24\">looks_{{icons_num($index)}}</md-icon> <p ng-show=\"!tasMultiple\">{{personscope.selectedTasksPersons[$index][0].tasName}}</p> <p ng-show=\"tasMultiple\">{{personscope.selectedTasksPersonsStr[$index]}}</p> </md-list-item> <md-list-item ng-show=\"isAdveo\"> <p>{{findLangTextElement(\"label.template.numLines\")}}: {{personscope.selectedTasksPersons[0][0].numLines}}</p> <p>{{findLangTextElement(\"label.template.numPallets\")}}: {{personscope.selectedTasksPersons[0][0].numPallets}}</p> </md-list-item> <md-divider ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-list-item ng-show=\"local.locSelCalendar == 1 && selCalendar.selectedCalendar[0].calName\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 fmd-hue-3\">perm_contact_calendar</md-icon> <p>{{findLangTextElement(\"label.header.places\")}}: {{selCalendar.selectedCalendar[0].calName}}</p> </md-list-item> </md-divider></md-divider></md-divider></md-list> </div> <md-divider class=\"clear-min\"> <div layout=\"column\" layout-gt-sm=\"row\" ng-controller=\"BookControllerAuto as ctrl\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 person-autocomplete\">person</md-icon> <md-autocomplete required md-floating-label=\"{{findLangTextElement('client.name')}}\" md-min-length=\"3\" md-input-name=\"cliName\" md-input-maxlength=\"100\" md-no-cache=\"true\" md-items=\"item in ctrl.querySearch(ctrl.searchText)\" md-item-text=\"item.whoName\" md-selected-item=\"ctrl.selectedItem\" md-search-text=\"ctrl.searchText\" md-selected-item-change=\"ctrl.selectedItemChange(item)\" md-search-text-change=\"ctrl.actionNewClient(ctrl.searchText)\" md-escape-options=\"clear\" md-clear-button=\"true\"> <md-item-template> <span md-highlight-text=\"ctrl.searchText\">{{item.whoName}}</span> </md-item-template> <!--<md-not-found>\n" +
    "          \t\t\tNuebo cienre\n" +
    "          \t\t\t<a ng-click=\"ctrl.actionNewClient()\">Create a new one!</a>\n" +
    "        \t\t  </md-not-found>--> <div ng-messages=\"apoNewAdminForm.cliName.$error\"> <div ng-message=\"required\">{{findLangTextElement(\"label.requiredData\")}}</div> <div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div> </div> </md-autocomplete> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">email</md-icon> <label>{{findLangTextElement(\"client.email\")}}</label> <input ng-disabled=\"!ctrl.isNewClient()\" type=\"email\" name=\"cliEmail\" ng-model=\"client.email\" required ng-pattern=\"/^.+@.+\\..+$/\" ng-change=\"changeCliEmail()\"> <div ng-messages=\"apoNewAdminForm.cliEmail.$error\"> <div ng-message-exp=\"['required', 'pattern']\">{{findLangTextElement(\"label.requiredData\")}} / {{findLangTextElement(\"label.typePatternData\")}}</div> </div> </md-input-container> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">phone</md-icon> <label>{{findLangTextElement(\"client.telf\")}}</label> <input ng-disabled=\"!ctrl.isNewClient()\" type=\"tel\" name=\"cliTelf\" ng-model=\"client.telf\" required placeholder=\"9.. / 6..\"> <div ng-messages=\"apoNewAdminForm.cliTelf.$error\"> <div ng-message=\"required\">{{findLangTextElement(\"label.requiredData\")}}</div> </div> </md-input-container> </div> <md-divider class=\"clear-min\"> <div layout=\"column\" ng-show=\"isCelebration\"> <md-input-container class=\"md-block\" flex-gt-sm> <label>{{findLangTextElement(\"client.celebrationDate\")}}</label> <md-datepicker ng-model=\"client.celebrationDate\" md-min-date=\"client.celebrationDate\"></md-datepicker> </md-input-container> </div> <md-divider class=\"clear-min\"> <div layout=\"column\"> <md-input-container class=\"md-block\" flex-gt-sm> <md-icon md-font-library=\"material-icons\" class=\"md-24\">insert_comment</md-icon> <label>{{findLangTextElement(\"event.com\")}}</label> <textarea name=\"cliObserv\" ng-model=\"client.observ\" ng-maxlength=\"300\"></textarea> <div ng-messages=\"apoNewAdminForm.cliObserv.$error\"> <div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div> </div> </md-input-container> </div> <!-- <md-divider class=\"clear-min\"> --> <!-- \n" +
    "\t\t<div layout=\"column\">\t\n" +
    "\t\t\t<md-input-container class=\"md-block\" flex-gt-sm>\n" +
    "\t\t\t\t<md-icon md-font-library=\"material-icons\" class=\"md-24\">report</md-icon>\n" +
    "\t\t\t\t<label>{{findLangTextElement(\"event.legal\")}}</label> \n" +
    "\t\t\t\t <!-- <md-checkbox ng-model=\"client.legal\"> Checkbox 1: {{ data.cb1 }}</md-checkbox>--> <!-- <div ng-messages=\"apoNewAdminForm.cliLegal.$error\">\n" +
    "\t\t\t\t\t<div ng-message=\"maxlength\">{{findLangTextElement(\"label.maxLengthData\")}}</div>\n" +
    "\t\t\t\t</div> --> <!-- \t</md-input-container>\n" +
    "\t\t</div> --> <md-divider class=\"clear-min\"> <div layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"(apoNewAdminForm.$invalid=true) && sendNewAppo()\" class=\"md-primary md-hue-2\" ng-disabled=\"apoNewAdminForm.$invalid\"> <span lnt-id=\"form.send\">Enviar</span> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-accents\">send</md-icon> </md-button> <span flex></span> </div> </md-divider></md-divider></md-divider></md-divider></md-content> </form>"
  );


  $templateCache.put('views/bookingHome.html',
    "<article class=\"booking_calendar\"> <md-tabs id=\"tabsbook\" md-selected=\"$parent.selectedTabIndex\" md-dynamic-height md-border-bottom md-autoselect> <md-tab label=\"{{tabsBook[0].title}}\" ng-disabled=\"tabsBook[0].disabled\" ng-click=\"$parent.selectedTabIndex = 0;\"> <loading></loading> <md-toolbar ng-show=\"!isViewLoading && selectedDate\"> <div class=\"md-toolbar-tools date\"> <span flex></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools date year\" layout-align=\"start center\"> <span flex ng-bind=\"extractYear()\"></span> <span flex></span> <span flex></span> </div> <div class=\"md-toolbar-tools date\" layout-align=\"start center\"> <span flex=\"70\" ng-bind=\"extractDayWeekMonth()\"></span> <span flex></span> <md-button ng-click=\"toggleSelectDate()\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-light\">arrow_drop_down_circle</md-icon> </md-button> </div> </md-toolbar> <show-month id=\"table-month\" ng-show=\"!isViewLoading && showSelectedDate\"> </show-month> <show-tasks ng-show=\"!isViewLoading && selectedDate\"> </show-tasks> <md-divider class=\"clear\"> <div ng-show=\"!isViewLoading && selectedDate\" layout=\"row\" class=\"md-actions\" layout-align=\"start center\"> <span flex></span> <md-button ng-click=\"saveTaskSelect()\" class=\"md-primary md-hue-2\"> <span lnt-id=\"event.searchHours\">Ver resultados</span> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-accents\">search</md-icon> </md-button> <span flex></span> </div> </md-divider></md-tab> <md-tab label=\"{{tabsBook[1].title}}\" ng-disabled=\"tabsBook[1].disabled\" ng-click=\"$parent.selectedTabIndex = 1;\"> <loading></loading> <show-apos-day ng-show=\"!isViewLoading\"> </show-apos-day> </md-tab> <md-tab label=\"{{tabsBook[2].title}}\" ng-disabled=\"tabsBook[2].disabled\" ng-click=\"$parent.selectedTabIndex = 2;\"> <loading></loading> <show-apos-new ng-show=\"!isViewLoading && !adminOption\"> </show-apos-new> <show-apos-new-admin ng-show=\"!isViewLoading && adminOption\"> </show-apos-new-admin> </md-tab> <md-tab label=\"{{tabsBook[3].title}}\" ng-disabled=\"tabsBook[3].disabled\" ng-click=\"$parent.selectedTabIndex = 3;\"> <loading></loading> <show-apos-end ng-show=\"!isViewLoading\"> </md-tab> </md-tabs> </article>"
  );


  $templateCache.put('views/bookingSelectTasks.html',
    "<form> <md-content> <div layout=\"column\"> <md-list> <div ng-show=\"local.locNumPersonsApo > 1\" class=\"block-sep\"> <md-list-item class=\"md-2-line\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">person_add</md-icon> <div class=\"md-list-item-text\"> <p lnt-id=\"task.select.people\"></p> </div> </md-list-item> <md-list-item class=\"md-2-line\"> <md-icon></md-icon> <div class=\"md-list-item-text\"> <md-input-container> <md-select ng-model=\"personscope.numPersons\" ng-change=\"disabledNextTabs();changeNumPersons()\" aria-label=\"{{findLangTextElement('task.select.people')}}\"> <md-option ng-repeat=\"numPerson in personscope.persons\" value=\"{{numPerson.id}}\"> {{numPerson.name}} </md-option> </md-select> </md-input-container> </div> </md-list-item> </div> <div ng-show=\"!isAdveo\" ng-repeat=\"numPerson in personscope.persons | limitTo:personscope.numPersons\" class=\"block-sep\"> <md-list-item class=\"md-2-line\"> <md-icon ng-if=\"local.locNumPersonsApo == 1\" md-font-library=\"material-icons\" class=\"md-24\">build</md-icon> <md-icon ng-if=\"local.locNumPersonsApo > 1\" md-font-library=\"material-icons\" class=\"md-24\">looks_{{icons_num($index)}}</md-icon> <div class=\"md-list-item-text\"> <p lnt-id=\"label.template.job\"></p> </div> </md-list-item> <md-list-item class=\"md-2-line\"> <md-icon></md-icon> <div class=\"md-list-item-text taskSelect\"> <a ng-href=\"\" ng-click=\"goToSelectTaskPerson(numPerson.id)\"> <font>{{personscope.selectedTasksPersonsStr[$index]}}</font> </a> <md-button ng-controller=\"LangsController\" ng-show=\"sectionBack==null && existsMenu\" ng-click=\"goToSelectTaskPerson(numPerson.id)\"> <md-icon md-font-library=\"material-icons\" class=\"md-24\">arrow_drop_down</md-icon> </md-button> </div> </md-list-item> </div> <md-list-item ng-show=\"isAdveo\" class=\"md-2-line\"> <label id=\"error_numLines\" style=\"color:red\"></label> <label>{{findLangTextElement(\"label.template.selNumLines\")}}: </label> <input id=\"numLines\" size=\"3\" type=\"number\" ng-model=\"personscope.selectedTasksPersons[0][0].numLines\" max=\"999\" min=\"1\" required> <label id=\"error_numPallets\" style=\"color:red\"></label> <label>{{findLangTextElement(\"label.template.selNumPallets\")}}: </label> <input id=\"numPallets\" size=\"3\" type=\"number\" ng-model=\"personscope.selectedTasksPersons[0][0].numPallets\" max=\"999\" min=\"1\" required> </md-list-item> <div ng-show=\"local.locSelCalendar == 1\" class=\"block-sep\"> <md-list-item class=\"md-2-line\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-hue-3\">perm_contact_calendar</md-icon> <div class=\"md-list-item-text\"> <p lnt-id=\"place.select.cabText\"></p> </div> </md-list-item> <md-list-item class=\"md-2-line\"> <md-icon></md-icon> <div class=\"md-list-item-text\"> <md-input-container> <md-select ng-model=\"selCalendar.selectedCalendar[0].id\" ng-change=\"disabledNextTabs()\" aria-label=\"{{findLangTextElement('place.select.cabText')}}\"> <md-option value=\"-1\">{{findLangTextElement(\"general.anyone\")}}</md-option> <md-option ng-repeat=\"calendar in selCalendar.calendars\" value=\"{{calendar.id}}\"> {{calendar.calName}} </md-option> </md-select> </md-input-container> </div> </md-list-item> </div> </md-list> </div> </md-content> </form>"
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


  $templateCache.put('views/modalDialogTasks.html',
    "<md-dialog aria-label=\"{{titleDialog}}\" ng-cloak> <form> <md-toolbar> <div class=\"md-toolbar-tools title\"> <span flex></span> <span flex></span> <md-button class=\"md-icon-button\" ng-click=\"cancel()\"> <md-icon md-font-library=\"material-icons\" class=\"md-24 md-light\">close</md-icon> </md-button> </div> <div class=\"md-toolbar-tools title\"> <span flex></span> <span flex=\"70\">{{titleDialog}}</span> <span flex></span> </div> </md-toolbar> <md-dialog-content> <div class=\"md-dialog-content\"> <div layout=\"column\" layout-margin> <md-subheader class=\"md-no-sticky\">{{titleContent}}</md-subheader> <div ng-cloak ng-repeat=\"task in $parent.combiTasks\"> <md-checkbox aria-label=\"task.lotName\" class=\"md-primary md-hue-2\" ng-checked=\"selectedTasksPersonsChecks.indexOf(task.id) > -1\" ng-click=\"selectObj(task.id)\"> {{ task.lotName }} </md-checkbox> </div> </div> </div> </md-dialog-content> <md-dialog-actions layout=\"row\" layout-align=\"space-around center\"> <span flex ng-show=\"!tasMultiple\"></span> <md-button ng-show=\"tasMultiple\" class=\"md-fab md-mini md-primary md-hue-2\"> <span>{{selectedTasksPersonsChecks.length}}</span> </md-button> <md-button ng-show=\"tasMultiple\" ng-click=\"cleanTaskPerson()\" class=\"md-primary md-hue-2\"> {{cleanText}} </md-button> <md-button ng-click=\"cancel()\" class=\"md-primary md-hue-2\"> {{cancelText}} </md-button> <md-button ng-click=\"answer()\" class=\"md-primary md-hue-2\" ng-disabled=\"selectedTasksPersonsChecks.length==0\"> {{acceptText}} </md-button> <span flex></span> </md-dialog-actions> </form> </md-dialog>"
  );


  $templateCache.put('views/tableMonthBooking.html',
    "<md-subheader class=\"md-no-sticky md-primary\"> <div layout=\"row\" layout-align=\"space-around center\"> <div flex=\"70\" layout=\"row\" layout-align=\"start center\"> <md-button ng-click=\"loadMonth(-1)\"> <md-icon md-font-library=\"material-icons\" class=\"md-primary md-24\">navigate_before</md-icon> </md-button> <span flex=\"none\" class=\"month-year\"></span> <md-button ng-click=\"loadMonth(+1)\"> <md-icon md-font-library=\"material-icons\" class=\"md-primary md-24\">navigate_next</md-icon> </md-button> </div> <md-button ng-click=\"onToday()\"> <span lnt-id=\"label.header.today\">Today</span> <md-icon md-font-library=\"material-icons\" class=\"md-primary md-24\">today</md-icon> </md-button> <span flex></span> </div> </md-subheader> <md-divider> <md-grid-list md-cols=\"7\" md-gutter=\"0px\" md-row-height=\"16px\"> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekMon\">Mon</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekTue\">Tue</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekWed\">Wed</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekThu\">Thu</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekFri\">Fri</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekSat\">Sat</span> </md-grid-tile> <md-grid-tile> <span id=\"dateWeek\" lnt-id=\"general.daysWeekSun\">Sun</span> </md-grid-tile> </md-grid-list> <md-divider> <md-grid-list id=\"tableDates\" md-cols=\"7\" md-gutter=\"0px\" md-row-height=\"fit\"> <md-grid-tile ng-repeat=\"a in range(42) track by $index\" ng-click=\"disabledNextTabs(); onSelectDate($event)\"> <span id=\"date\"></span> </md-grid-tile> </md-grid-list></md-divider></md-divider>"
  );

}]);
