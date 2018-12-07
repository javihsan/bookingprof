<!-- Inicio Cabecera -->
<%@include file="cabecera.jsp"%>
<!-- Fin Cabecera -->

	<section id="booking" data-transition="">
        <header>
         	<span id="header_text" class="title centered"></span>
         	<nav class="box">
                <a href="#menu" data-router="aside" data-icon="menu"></a>
            </nav>
            <nav class="right">
                <a href="#" class="button" data-action="new" data-icon="plus"><abbr data-lnt-id="form.add"></abbr></a>
            </nav>
            <nav class="right">
               <a href="#" class="button" data-action="today" data-icon="calendar"><abbr data-lnt-id="label.header.today"></abbr></a>
            </nav>
            <nav class="right">
               <a href="#" class="button" data-action="places" data-icon="cut"><abbr data-lnt-id="label.aside.places"></abbr></a>
            </nav>
        </header>
        <footer>
        	<nav class="">
               <a href="#table-month"   data-router="article" data-icon="calendar"><abbr data-lnt-id="label.foot.month"></abbr></a>
               <a href="#table-day"     data-router="article" data-icon="edit">    <abbr data-lnt-id="label.foot.booking"></abbr></a>
               <a href="#list-events"   data-router="article" data-icon="book">    <abbr data-lnt-id="label.foot.diary"></abbr></a>
               <a href="#list-invoices" data-router="article" data-icon="money">   <abbr data-lnt-id="label.foot.invoice"></abbr></a>
           </nav>
        </footer>
        
        <article id="list-local" class="list indented"></article>
        
        <article id="table-month" class="active booking_calendar">
        	<!-- Inicio tableMonthBooking -->
			<%@include file="tableMonthBooking.jsp"%>
			<!-- Fin tableMonthBooking -->
		</article>
	
		<!-- Inicio articleDayBooking -->
		<%@include file="articleDayBooking.jsp"%>
		<!-- Fin articleDayBooking -->
       	
        <article id="list-events" class="list scroll indented"></article>
       
        <article id="list-invoices" class="list scroll indented"></article>
       
        <article id="list-clients" class="list form scroll indented"></article>
       
        <!-- Inicio articleLangs --> 
		<%@include file="articleLangs.jsp"%>
		<!-- Fin articleLangs -->
		       
	</section>
	
	<!-- Inicio sectionEventNewManager -->
	<%@include file="sectionEventNewManager.jsp"%>
	<!-- Fin sectionEventNewManager -->

	<!-- Inicio sectionEventCancel -->
	<%@include file="sectionEventCancel.jsp"%>
	<!-- Fin sectionEventCancel -->
	
	<!-- Inicio sectionEventFin -->
	<%@include file="sectionEventFin.jsp"%>
	<!-- Fin sectionEventFin -->

	<!-- Inicio sectionTaskSelect -->
	<%@include file="sectionTaskSelect.jsp"%>
	<!-- Fin sectionTaskSelect -->

	<!-- Inicio sectionTaskSelectPerson -->
	<%@include file="sectionTaskSelectPerson.jsp"%>
	<!-- Fin sectionTaskSelectPerson -->

	<!-- Inicio sectionClientNew -->
	<%@include file="sectionClientNew.jsp"%>
	<!-- Fin sectionClientNew -->
	
	<!-- Inicio sectionClientSearch -->
	<%@include file="sectionClientSearch.jsp"%>
	<!-- Fin sectionClientSearch -->
	
	<!-- Inicio sectionCalendarSelectDiary -->
	<%@include file="sectionCalendarSelectDiary.jsp"%>
	<!-- Fin sectionCalendarSelectDiary -->
	
	<!-- Inicio sectionInvoiceNew -->
	<%@include file="sectionInvoiceNew.jsp"%>
	<!-- Fin sectionInvoiceNew -->
	
   	<aside id="menu" class="left">
      <header data-lnt-id="label.aside.title"></header>
       <article class="list scroll active">
           <ul>
               <li>
                   <a href="#list-local" data-router="article">
                       <strong data-lnt-id="label.aside.locals"></strong>
                   </a>
               </li>
               <li class="active">
                   <a class="active" href="#table-month" data-router="article">
                        <strong data-lnt-id="label.aside.bookings"></strong>
                   </a>
               </li>
               <li> 
                   <a href="#list-clients" data-router="article">
                       <strong data-lnt-id="label.aside.clients"></strong>
                   </a>
               </li>
               <li> 
                   <a href="#langs" data-router="article">
                       <strong data-lnt-id="label.aside.langs"></strong>
                   </a> 
               </li>
           </ul>
       </article>
    </aside>
    
    <!-- App - Dependencies -->
    <script src="/app/model/localModel.js"></script>
    <script src="/app/model/appointmentModel.js"></script>
   	<script src="/app/model/clientModel.js"></script>
   	<script src="/app/model/localTaskModel.js"></script>
   	<script src="/app/view/eventListView.js"></script>
   	<script src="/app/view/eventListSPView.js"></script>
   	<script src="/app/view/eventListReadView.js"></script>
    <script src="/app/view/eventListDayView.js"></script>
    <script src="/app/view/eventListFromView.js"></script>
    <script src="/app/view/eventListUntilView.js"></script>
   	<script src="/app/view/localSelectView.js"></script>
  	<script src="/app/view/bookingDayView.js"></script>
  	<script src="/app/view/bookingDaySPView.js"></script>
  	<script src="/app/view/bookingDayView.js"></script>
  	<script src="/app/view/clientListView.js"></script>
  	<script src="/app/view/searchListView.js"></script>
  	<script src="/app/controller/localSelectCtrl.js"></script>
  	<script src="/app/controller/bookingCtrl.js"></script>
  	<script src="/app/controller/bookingMonthCtrl.js"></script>
  	<script src="/app/controller/clientListCtrl.js"></script>
  	<script src="/app/booking.js"></script>
  	
  	<script src="/app/facadeCore.js"></script>
  	<script src="/app/app.js"></script>
      
    <script>
	 	Lungo.ready(function() {
	 		
			//console.log ("readyBookingOperator");
			
			adminOption = true;
			operatorRead = false;
			
			asyn = __FacadeCore.Service_Settings_asyncFalse();
			
			urlListLocalAll = "http://" + appHost + "/local/listAll";
			
			urlListLocal = "http://" + appHost + "/local/list";
			data = {domain:appFirmDomain}
	    	listLocal = $$.json(urlListLocal, data);
			
			__FacadeCore.Service_Settings_async(asyn);
			
			if (listLocal.length>1){ // Hay más de un local
				localId = __FacadeCore.Storage_get (appName+"localId");
				if (!localId){
					__FacadeCore.Router_article("booking","list-local");
				} else {
					localReady();
				}
			} else { // Solo hay un local
				hideLocalAside();
				__FacadeCore.Storage_set (appName+"localId", null);
				__FacadeCore.Storage_set (appName+"localId", listLocal[0]);
				localReady();
			} 
						
						
		});
	 	
		localReady = function() {	
	 		
			//console.log ("localReady");
				
		 	__FacadeCore.Cache_remove(appName + "selectedTasks");
		 	__FacadeCore.Cache_remove(appName + "selectedTasksCount");
	    		
			asyn = __FacadeCore.Service_Settings_asyncFalse();
			
			url = "http://" + appHost + "/firm/get";
			data = {domain:appFirmDomain}
		    firm = $$.json(url, data);
			
			__FacadeCore.Cache_remove(appName + "firm");
			__FacadeCore.Cache_set(appName + "firm", firm);
			
			localId = __FacadeCore.Storage_get (appName+"localId");
			url = "http://" + appHost + "/local/get";
			data = {id:localId};
			local = $$.json(url, data);
			
			url = "http://" + appHost + "/client/operator/list";
		    data = {domain: appFirmDomain};
		    clients = $$.json(url, data);
			__FacadeCore.Cache_remove(appName + "clients");
			__FacadeCore.Cache_set(appName + "clients", clients);
			
			__FacadeCore.Service_Settings_async(asyn);
			    
			if (local.id){
				__FacadeCore.Cache_remove(appName + "local");
				__FacadeCore.Cache_set(appName + "local", local);		
			
				asyn = __FacadeCore.Service_Settings_asyncFalse();
	   			
				urlLocalTask = "http://" + appHost + "/localTask/operator/listCombi";
	    	 	data = {localId:local.id}
		 		response = $$.json(urlLocalTask, data);
			 	combiTasks = Lungo.Core.toArray (response);
			 	combiTasks = Lungo.Core.orderByProperty (combiTasks, "lotName", "asc");
				__FacadeCore.Cache_remove(appName + "combiTasks");
			 	__FacadeCore.Cache_set(appName + "combiTasks", combiTasks);
		    
			 	url = "http://"+appHost+"/calendar/list";
				data = {localId:local.id};
				calendars = $$.json (url, data);
				calendars = Lungo.Core.orderByProperty (calendars, "calName", "asc");
				__FacadeCore.Cache_remove(appName + "calCandidates");
			 	__FacadeCore.Cache_set(appName + "calCandidates", calendars);
			 	
			 	url = "http://"+appHost+"/calendar/numCals";
				data = {localId:local.id};
				numCals = $$.json (url, data);
				__FacadeCore.Cache_remove(appName + "numCals");
			 	__FacadeCore.Cache_set(appName + "numCals", numCals);
			 				 	
			 	if (appFirmDomain == 'adveo' ){
			 		url = "http://"+appHost+"/client/operator/getUserSession";
			 		data = {firmId:firm.id};
			 		userSession = $$.json (url, data);
			 		if (userSession.authorities && userSession.authorities.indexOf("OPERATOR_READ")!=-1){
			 			operatorRead = true;
			 			hideClientAside();
			 		}
			 	}
			 	
		    	__FacadeCore.Service_Settings_async(asyn);
					      
				newDayAux = new Date()
				year = newDayAux.getFullYear()
				month = newDayAux.getMonth() + 1
				day = newDayAux.getDate()
				selectedDate =  year+"-"+month+"-"+day
		    	__FacadeCore.Cache_remove(appName + "selectedDate");
				__FacadeCore.Cache_set(appName+"selectedDate", new String(selectedDate));
			
				if (appFirmDomain == 'adveo' ){
					urlListApoByDay = "http://"+appHost+"/calendar/operator/listApoByDayGoods"
					urlEventNew = "http://"+appHost+"/event/operator/newGoods"
				} else if (firm.firConfig.configLocal.configLocSP){
					urlListApoByDay = "http://"+appHost+"/calendar/operator/listApoByDaySP"
					urlEventNew = "http://"+appHost+"/event/operator/newSP"
				} else {
					urlListApoByDay = "http://"+appHost+"/calendar/operator/listApoByDay"
					urlEventNew = "http://"+appHost+"/event/operator/new"
				}	

				if (firm.firConfig.configLocal.configLocRepeat==1){
					urlListByDiary = "http://"+appHost+"/event/operator/listByDiaryRepeat"
				} else {
					urlListByDiary = "http://"+appHost+"/event/operator/listByDiary"
				}
				
				var title = firm.firName+" - "+local.locName; 
				$$("#header_text").html (title);
				document.title = "BookingProf Operator - "+title;
			
				setCurrentAside("list-local","table-month");
				
				__FacadeCore.Router_article("booking","table-month");
			} else {
		    	__FacadeCore.Storage_set (appName+"localId", null);
		    	__FacadeCore.Router_article("booking","list-local");
		    }
						
		};
 	</script>
 	   
    
</body>
</html>
