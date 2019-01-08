var protocol_url = location.protocol+'//';
var domainOfi = 'bookingprof.com';
//var domainLocalOfi = '127.0.0.1:8888';//'localhost:8888'; // arrancar en local con el java delante
var domainLocalOfi = '127.0.0.1:9001';//'localhost:9001'; // arrancar en local solo el front
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
	
	app.config(function($stateProvider, $urlRouterProvider, $httpProvider, $mdThemingProvider, $mdDateLocaleProvider /*, $mdIconProvider*/){
		
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
				controller: function($scope,$rootScope){
					$scope.initBook(1);
					$rootScope.setBack(null,$scope);
		          },
	            onEnter: function($rootScope){
	            	//console.log("onEnter booking.home");
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
				controller: function($scope){
					$scope.initClients();
		          },
	            onEnter: function($rootScope){
	            	$rootScope.sectionTit = $rootScope.findLangTextElement("label.aside.clients");
	               	$rootScope.setBack();
	              }
			});			
		
		
		$urlRouterProvider.otherwise(
				function($injector, $location) {
			//console.log('path: ',$location.path());
			$location.replace().path("/booking");
		});
		
		//$httpProvider.interceptors.push('myHttpInterceptor');
	
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
						
				//var url = protocol_url + appHost + "/multiText/listLocaleTexts";
				var url = "/js/lang_es.json" // Para rapidez al debugear solo con front
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
				appFirmDomain = 'demo' // Para local arrancado solo con front
				appHost = '127.0.0.1:8888';//'localhost:8888';//'r8-0-0-dot-dilosohairapp.appspot.com'//'localhost:8888' //Para tirar de un determinado back
				
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
    	console.log('Bootstrapping!');
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
    }); 
