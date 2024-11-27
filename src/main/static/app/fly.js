var protocol_url = location.protocol+'//';
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
	
	app.config(function($stateProvider, $urlRouterProvider, $httpProvider, $mdThemingProvider, $mdDateLocaleProvider /*, $mdIconProvider*/){
		
		$mdThemingProvider.theme('default')
	    .primaryPalette('pink')
	    .accentPalette('purple')
	    .warnPalette('teal')
	    //.backgroundPalette('teal');
		
		$mdDateLocaleProvider.firstDayOfWeek = 1;
		
		$mdDateLocaleProvider.formatDate = function(date) {
		   return moment(date).format('L');
		};
		
		$mdDateLocaleProvider.parseDate = function(dateString) {
		    var m = moment(dateString, 'L', true);
		    return m.isValid() ? m.toDate() : new Date(NaN);
		};
		
		$stateProvider
			.state('search', {
				abstract: true,
				url: '/search',
				template:'<div ui-view></div>',
				controller: 'FlyController'
			})
			.state('search.home', {
				url: '',
				templateUrl: 'views/flyHome.html',
				controller: function($scope,$rootScope){
					$scope.initBook(1);
					$rootScope.setBack(null,$scope);
		          },
	            onEnter: function($rootScope){
	            	//console.log("onEnter search.home");
	            	$rootScope.sectionTit = $rootScope.findLangTextElement("label.aside.bookings");
	              }
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
				controller: function($scope){
					$scope.initLegal();
		          },
	            onEnter: function($rootScope){
	            	$rootScope.sectionTit = $rootScope.findLangTextElement("label.aside.info");
	               	$rootScope.setBack();
	              }
			});			
		
		
		$urlRouterProvider.otherwise(
			function($injector, $location) {
				//console.log('path: ',$location.path());
				$location.replace().path("/search");
			}
		);

	});
	

	// register the interceptor as a service
	app.factory('myHttpInterceptor', function($q, $rootScope) {
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
	});
	
	app.factory('cacheService', function($cacheFactory) {   
		return $cacheFactory('cache-service');
	}); 

	app.run([
		'$rootScope',
		'$state',
		'httpService',
		'cacheService',
		'$mdSidenav', 
		'$mdDialog',
		'$mdToast',
		"$mdDateLocale",
		function($rootScope, $state, httpService, cacheService, $mdSidenav, $mdDialog, $mdToast, $mdDateLocale) {
			
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
						
				var url = protocol_url + appHost + "/multiText/fly/listLocaleTexts";
				//var url = "/js/lang_es.json" // "/js/lang_es_full.json" // Para rapidez al debugear solo con front
				var data = {lanCode:lanCode};
								
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
			
			$rootScope.openNotif = function (titleContent, timeHideDelay, funPos) {
				var toast = $mdToast.simple()
		          .textContent(titleContent)
		          //.action('OK')
		          .hideDelay(timeHideDelay*1000)
		          //.highlightAction(false)
		          .parent($("#contentParent")[0])
		          .position('bottom right');
		          
		        $mdToast.show(toast).then(function(response) {
		        	//console.log("funPos:",funPos);
	        		if (funPos) {
		        		funPos();
		        	}
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
		
			appFirmDomain = 'fly' // Para local arrancado solo con front
			appHost = 'localhost:8888';//'r8-0-0-dot-dilosohairapp.appspot.com'//'localhost:8888' //Para tirar de un determinado back
			
			appHost += '/'+appFirmDomain;
			appName = 'BookingProf-' + appFirmDomain;
			
			
			setFormatCalendar = function() {
				
		   		moment.locale($rootScope.langApp);
			
			    $mdDateLocale.shortDays = eval($rootScope.findLangTextElement("general.daysWeekShort"));
			    $mdDateLocale.shortMonths = eval($rootScope.findLangTextElement("general.monthsShort"));
			};
			
			// set Text multiLanguaje
			$rootScope.changeLang($rootScope.langApp, setFormatCalendar);				
		
		}
	]); 
	
	app.directive('lntId', function($compile) {
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
	});
	
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
    	//console.log('Bootstrapping!');
    	new CordovaInit();
    }); 
	
    
    app.factory('httpService', function($http) {   
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
			//console.log ("Llamando a POST DATA... "+url,data);
			
    		var config = {timeout:45*1000};
      		//console.log ("Llamando a POST ... "+url,config);
    	   		
       		return $http.post(url,data,config); 
    	}
    	return httpService; 
    }); 
