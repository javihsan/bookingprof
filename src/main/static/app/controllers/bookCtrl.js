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
									// Intentamos obtener el cliente de sesion 
									$scope.client = __FacadeCore.Cache_get(appName + "clientSession");
									if (!$scope.client) { // Si no, lo obtenemos de la cookie del dispositivo
										$scope.client = __FacadeCore.Storage_get(appName+ "eveClient");
										if (!$scope.client){ // Si no, lo inicializamos
											$scope.client = {};
											$scope.client.name = "";
											$scope.client.email = "";
											$scope.client.telf = "";
										}
									}
									$scope.client.observ = "";
								}
								
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

							
							$scope.sendNewAppo = function() {
								//console.log("sendNewAppo");
																
								var a, appointment, startTime, data, eveClient, selectedTasks, selectTaskParam, taskSel, selectedTasksCount, selectedCalendarsParam, selectedCalendars, calendarSel, promiseListLocal;
								$rootScope.isViewLoading = true;
							    //if (this.changeCliEmail(event)) {
							        appointment = $scope.appo.appoSel;
							        startTime = appointment.apoStartTime;
							        if ($rootScope.adminOption) {
							          a = this.eveTime.val().split(':');
							          startTime = new Date(startTime);
							          startTime.setUTCHours(a[0]);
							          startTime.setUTCMinutes(a[1]);
							          startTime = startTime.getTime();
							          if (this.eveClientNew.val() === "0") {
							            eveClient = __FacadeCore.Cache_get(appName + "selectClient");
							          }
							        }
							        data = {
							          eveDescAlega: $scope.client.observ,
							          localId : $rootScope.local.id,
							          eveStartTime: startTime
							        };
							        if (eveClient) {
							          data.cliId = eveClient.cliId;
							        } else {
							          data.cliName = $scope.client.name;
							          data.cliEmail = $scope.client.email;
							          data.cliTelf = $scope.client.telf;
							        }
							        
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
										if ($rootScope.adminOption) {
											// message = findLangTextElement("label.notification.bookedApoAdmin.title")
								        } else {
								        	// message = findLangTextElement("label.notification.bookedApo.title")
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
							      
							   //}
							};							
							
							this.querySearch =  $scope.querySearch;
							this.selectedItemChange = $scope.selectedItemChange
							this.actionNewClient = $scope.actionNewClient
							this.isNewClient = $scope.isNewClient
							this.textChange = $scope.textChange
							
							$scope.newCient = true;
							
							$scope.isNewClient = function() {
								return $scope.newCient;
							}
							
							$scope.actionNewClient = function() {
								//console.log("actionNewClient")
								$scope.newCient = true;
								$scope.client.email = "";
						    	$scope.client.telf = "";
							}

							$scope.querySearch = function(query) {
								//var results = $rootScope.clients;
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
						    		$scope.client.name = item.whoName;
						    		$scope.client.email = item.whoEmail;
						    		$scope.client.telf = item.whoTelf1;
						    		$scope.newCient = false;
						    	}
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
