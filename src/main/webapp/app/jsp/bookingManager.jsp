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
        </header>
        <footer>
        	<nav class="">
               <a href="#" data-action="all" data-icon="ok-sign"><abbr data-lnt-id="form.all"></abbr></a>
               <a href="#" data-action="bookable" data-icon="book"><abbr data-lnt-id="localTask.bookable"></abbr></a>
               <a href="#" data-action="billed" data-icon="money"><abbr data-lnt-id="localTask.billed"></abbr></a>
               <a href="#" data-action="reset" data-icon="undo"><abbr data-lnt-id="form.reset"></abbr></a>
               <a href="#" data-action="save"  data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>
        
        <article id="list-local" class="list indented"></article>
        
        <article id="local-tasks" class="active list form scroll"></article>
        
        <article id="list-product" class="active list form scroll"></article>
        
        <article id="list-calendar" class="list scroll indented"></article>
         
        <!-- Inicio articleGeneral -->
		<%@include file="articleGeneral.jsp"%>
		<!-- Fin articleGeneral -->
           
       	<!-- Inicio articleHours -->
		<%@include file="articleHours.jsp"%>
		<!-- Fin articleHours -->
                    
       	<!-- Inicio articleLangsManager --> 
		<%@include file="articleLangsManager.jsp"%>
		<!-- Fin articleLangsManager -->
		
		<!-- Inicio articleLangs --> 
		<%@include file="articleLangs.jsp"%>
		<!-- Fin articleLangs -->
	      
	</section>
		
	<!-- Inicio sectionLocalTaskNew -->
	<%@include file="sectionLocalTaskNew.jsp"%>
	<!-- Fin sectionLocalTaskNew -->
	
	<!-- Inicio sectionProductNew -->
	<%@include file="sectionProductNew.jsp"%>
	<!-- Fin sectionProductNew -->
	
	<!-- Inicio sectionCalendarNew -->
	<%@include file="sectionCalendarNew.jsp"%>
	<!-- Fin sectionCalendarNew -->
	
	<!-- Inicio sectionLocalTaskCalendar -->
	<%@include file="sectionLocalTaskCalendar.jsp"%>
	<!-- Fin sectionLocalTaskCalendar -->
	
	<!-- Inicio sectionHours -->
	<%@include file="sectionHours.jsp"%>
	<!-- Fin sectionHours -->
		  
    <!-- Inicio sectionHoursNew -->
	<%@include file="sectionHoursNew.jsp"%>
	<!-- Fin sectionHoursNew -->
	
   	<aside id="menu" class="left">
      <header data-lnt-id="label.aside.title"></header>
      <article class="list scroll active">
           <ul>
               <li>
                   <a href="#list-local" data-router="article">
                       <strong data-lnt-id="label.aside.locals"></strong>
                   </a>
               </li>
               <li>
                   <a href="#local-tasks" data-router="article">
                       <strong data-lnt-id="label.aside.localTasks"></strong>
                   </a>
               </li>
               <li>
                   <a href="#list-product" data-router="article">
                       <strong data-lnt-id="label.aside.products"></strong>
                   </a>
               </li>
               <li>
                   <a href="#local-general" data-router="article">
                       <strong data-lnt-id="label.aside.localGeneral"></strong>
                   </a>
               </li>
               <li> 
                   <a href="#local-hours" data-router="article">
                       <strong data-lnt-id="label.aside.hours"></strong>
                   </a>
               </li>
               <li class="active"> 
                   <a class="active" href="#list-calendar" data-router="article">
                       <strong data-lnt-id="label.aside.places"></strong>
                   </a>
               </li>
               <li> 
                   <a href="#langs-admin" data-router="article">
                       <strong data-lnt-id="label.aside.langsAdmin"></strong>
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
    <script src="/app/model/calendarModel.js"></script>
   	<script src="/app/model/localTaskModel.js"></script>
   	<script src="/app/model/productModel.js"></script>
   	<script src="/app/view/listCabView.js"></script>
   	<script src="/app/view/localSelectView.js"></script>
   	<script src="/app/view/calendarListView.js"></script>
  	<script src="/app/view/localTaskListView.js"></script>
  	<script src="/app/view/productListView.js"></script>
  	<script src="/app/controller/localSelectCtrl.js"></script>
  	<script src="/app/controller/localGeneralCtrl.js"></script>
  	<script src="/app/controller/calendarListCtrl.js"></script>
  	<script src="/app/controller/localTaskListCtrl.js"></script>
  	<script src="/app/controller/productListCtrl.js"></script>
  	<script src="/app/controller/hoursCtrl.js"></script>
  	<script src="/app/booking.js"></script>
  	  	
  	<script src="/app/facadeCore.js"></script>
  	<script src="/app/app.js"></script>
      
    <script>
	 	Lungo.ready(function() {
	 		
			//console.log ("readyBookingManager");
			
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
			
			asyn = __FacadeCore.Service_Settings_asyncFalse();
		
			url = "http://" + appHost + "/firm/get";
			data = {domain:appFirmDomain}
		    firm = $$.json(url, data);

			__FacadeCore.Cache_remove(appName + "firm");
			__FacadeCore.Cache_set(appName + "firm", firm);
			
			if (firm.firBilledModule==0){
				hideProductAside();
			}
			
			localId = __FacadeCore.Storage_get (appName+"localId");
			url = "http://" + appHost + "/local/get";
			data = {id:localId};
			local = $$.json(url, data);
			
		    __FacadeCore.Service_Settings_async(asyn);
		    
		    if (local.id){
		    	__FacadeCore.Cache_remove(appName + "local");
				__FacadeCore.Cache_set(appName + "local", local);
			
				newDayAux = new Date()
				year = newDayAux.getFullYear()
				month = newDayAux.getMonth() + 1
				day = newDayAux.getDate()
				selectedDate =  year+"-"+month+"-"+day
		    	__FacadeCore.Cache_remove(appName + "selectedDate");
				__FacadeCore.Cache_set(appName+"selectedDate", new String(selectedDate));
			    
			    var title = firm.firName+" - "+local.locName; 
				$$("#header_text").html (title);
				document.title = "BookingProf Manager - "+title;
			    			    
			    setCurrentAside("list-local","list-calendar");
			    
				__FacadeCore.Router_article("booking","list-calendar");
		    } else {
		    	__FacadeCore.Storage_set (appName+"localId", null);
		    	__FacadeCore.Router_article("booking","list-local");
		    }
						
		};
	 	
 	</script>
 	   
    
</body>
</html>
