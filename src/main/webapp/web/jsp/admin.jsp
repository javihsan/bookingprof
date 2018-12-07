<!-- Inicio Cabecera -->
<%@include file="adminCabecera.jsp"%>
<!-- Fin Cabecera -->
	
	<!-- Inicio List -->
	<%@include file="adminList.jsp"%>
	<!-- Fin List -->
	
	<!-- Inicio Firm -->
	<%@include file="adminFirm.jsp"%>
	<!-- Fin Firm -->
	
	<!-- Inicio Local -->
	<%@include file="adminLocal.jsp"%>
	<!-- Fin Local -->
	
	<!-- Inicio Calendar 
	<@include file="adminCalendar.jsp">
	<!-- Fin Calendar -->

 	 <aside id="menu" class="left">
         <header>
         	<span id="header_text" class="title centered"></span>
        </header>
       <article class="list scroll active">
           <ul>
            	<li class="active">
                   <a class="active" href="#list-firm" data-router="article">
                       <strong data-lnt-id="label.aside.firms"></strong>
                   </a>
               </li>
               <li>
                   <a href="#list-local" data-router="article">
                       <strong data-lnt-id="label.aside.locals"></strong>
                   </a>
               </li>
           </ul>
       </article>
    </aside>
	
	 
    <!-- App - Dependencies -->
    
    <script src="/app/model/firmModel.js"></script>
    <script src="/app/model/localModel.js"></script>
    <script src="/web/view/firmListAdminView.js"></script>
    <script src="/web/view/localListAdminView.js"></script>
    <script src="/web/controller/firmListAdminCtrl.js"></script>
    <script src="/web/controller/localListAdminCtrl.js"></script>
    
    <script src="/app/facadeCore.js"></script>
 	<script src="/web/js/appAdmin.js"></script>
 	
 	<script>
	 	Lungo.ready(function() {
	
			//console.log ("readyAdmin");
			
			document.title = lngAppName+ " - Admin";
			
			__FacadeCore.Router_article("admin","list-firm");
				
		});
 	</script>
 	
  	
    
</body>
</html>
