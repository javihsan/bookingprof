<!-- Inicio Cabecera -->
<%@include file="cabecera.jsp"%>
<!-- Fin Cabecera -->

	<section id="booking" data-transition="">
        <header>
         	<span id="header_text" class="title centered"></span>
         	<nav class="box">
                <a href="#menu" data-router="aside" data-icon="menu"></a>
            </nav>
            <!-- <nav class="right">
                <a href="#" class="button" data-action="new" data-icon="plus"><abbr data-lnt-id="form.add"></abbr></a>
            </nav>-->
        </header>
        <footer>
           <nav class="">
               <a href="#" data-action="send" data-icon="envelope"><abbr data-lnt-id="form.send"></abbr></a>
           </nav>
        </footer>
        
        <article id="list-local" class="list indented"></article>
        
        <!-- Inicio reportArticleSales --> 
		<%@include file="reportArticleSales.jsp"%>
		<!-- Fin reportArticleSales -->
         
        <!-- Inicio reportArticleSales --> 
		<%@include file="reportArticleApo.jsp"%>
		<!-- Fin reportArticleSales --> 
            		
		<!-- Inicio articleLangs --> 
		<%@include file="articleLangs.jsp"%>
		<!-- Fin articleLangs -->
	      
	</section>
	
	<!-- Inicio reportSectionRange --> 
	<%@include file="reportSectionRange.jsp"%>
	<!-- Fin reportSectionRange -->

	
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
                   <a class="active" href="#report-sales" data-router="article">
                       <strong data-lnt-id="report.label.aside.sales"></strong>
                   </a>
               </li>
               <li>
                   <a href="#report-apo" data-router="article">
                       <strong data-lnt-id="report.label.aside.apo"></strong>
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
    
    <!-- Google Dependencies -->
    <script src="http://www.google.com/jsapi?autoload={'modules':[{'name':'visualization',
       'version':'1.1','packages':['corechart','table','timeline','controls']}]}"></script>
    
    <!-- App - Dependencies -->
    <script src="/app/model/localModel.js"></script>
    <script src="/app/view/listCabView.js"></script>
    <script src="/app/view/localSelectView.js"></script>
    <script src="/app/controller/localSelectCtrl.js"></script>
	<script src="/app/controller/reportSalesCtrl.js"></script>
	<script src="/app/controller/reportApoCtrl.js"></script>
  	
  	<script src="/app/facadeCore.js"></script>
  	<script src="/app/app.js"></script>
      
    <script>
	 	Lungo.ready(function() {
	 		
			//console.log ("readyReport");
			
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
				hideSalesAside();
			}
			
			localId = __FacadeCore.Storage_get (appName+"localId");
			url = "http://" + appHost + "/local/get";
			data = {id:localId};
			local = $$.json(url, data);
			
			__FacadeCore.Service_Settings_async(asyn);
			    
			if (local.id){
				__FacadeCore.Cache_remove(appName + "local");
				__FacadeCore.Cache_set(appName + "local", local);	 
			 
				var title = firm.firName+" - "+local.locName; 
				$$("#header_text").html (title);
				document.title = "BookingProf Report - "+title;
				
			    if (firm.firBilledModule==0){
				    	setCurrentAside("list-local","report-apo");
			    	__FacadeCore.Router_article("booking","report-apo");
					
			    } else {
			    	setCurrentAside("list-local","report-sales");
			    	__FacadeCore.Router_article("booking","report-sales");
			    }	
			 } else {
			    __FacadeCore.Storage_set (appName+"localId", null);
			    __FacadeCore.Router_article("booking","list-local");
			 }	
		};
	 	
 	</script>
 	   
    
</body>
</html>
