<%@include file="cabecera.jsp"%>
<%
String actionLoad = (String)request.getParameter("actionLoad");
%>
<script language="JavaScript">
<!--
function verifierForm() {
	
	if (document.ContactInfo.name.value==""){
		alert ('<spring:message code="web.bkprof-contact.form.obligName"/>')
		document.ContactInfo.name.focus()
//	} else if (document.ContactInfo.surname.value==""){
//		alert ('<spring:message code="web.bkprof-contact.form.obligSurname"/>')
//		document.ContactInfo.surname.focus()
	} else if (document.ContactInfo.email.value==""){
		alert ('<spring:message code="web.bkprof-contact.form.obligEmail"/>')
		document.ContactInfo.email.focus()
//	} else if (document.ContactInfo.telf.value==""){
//		alert ('<spring:message code="web.bkprof-contact.form.obligTelf"/>')
//		document.ContactInfo.telf.focus()
//	} else if (document.ContactInfo.address.value==""){
//		alert ('<spring:message code="web.bkprof-contact.form.obligAddress"/>')
//		document.ContactInfo.address.focus()
	} else if (document.ContactInfo.comment.value.length>500){
		alert ('<spring:message code="web.bkprof-contact.form.maxComment"/>')
		document.ContactInfo.comment.focus()
	} else {
		document.ContactInfo.submit();
	} 
	
}
<%
if (actionLoad != null && actionLoad.equals("emailok")){
%>
alert("<spring:message code="web.bkprof-contact.textResponse"/>");
<%
}
%>
//-->
</script>  
    <article id="bkprof-home" class="home show">
		<div class="content">
	    	<div id="bkprof-home-agenda" class="cuadro_contenido">
	    		<img src="/web/img/home_agenda.jpg" />
				<h2><spring:message code="web.bkprof-home.agenda"/></h2>
	   			<h1><spring:message code="web.bkprof-home.agendaMin"/></h1>
			</div>
	
		   	<div id="bkprof-home-reserva" class="cuadro_contenido">
	    		<img src="/web/img/home_reserva.jpg" />
				<h2><spring:message code="web.bkprof-home.reserva"/></h2>
	    		<h1><spring:message code="web.bkprof-home.reservaMin"/></h1>		
			</div>
			
			<div id="bkprof-home-report" class="cuadro_contenido">
				<img src="/web/img/home_report.jpg" />
				<h2><spring:message code="web.bkprof-home.report"/></h2>
				<h1><spring:message code="web.bkprof-home.reportMin"/></h1>
			</div>
		</div>
    </article>
    
    <article id="bkprof-features" class="features show">
		<div class="content">
		
	    	<div id="bkprof-features-multilocal" class="cuadro_contenido">
				<div class="imgFeature"><img src="/web/img/icon-multilocal.png"></div>
				<h2><spring:message code="web.bkprof-features.multilocal"/></h2>
	    		<h1><spring:message code="web.bkprof-features.multilocalMin"/></h1>
			</div>
			<div id="bkprof-features-multiplace" class="cuadro_contenido right">
				<div class="imgFeature"><img style="width:70px;" src="/web/img/icon-multiplace.png"></div>
				<h2><spring:message code="web.bkprof-features.multiplace"/></h2>
	    		<h1><spring:message code="web.bkprof-features.multiplaceMin"/></h1>
			</div>
			<div id="bkprof-features-multilang" class="cuadro_contenido break">
				<div class="imgFeature"><img src="/web/img/icon-multilang.png"></div>
				<h2><spring:message code="web.bkprof-features.multilang"/></h2>
	    		<h1><spring:message code="web.bkprof-features.multilangMin"/></h1>
			</div>
			<div id="bkprof-features-services" class="cuadro_contenido right">
				<div class="imgFeature"><img style="width:70px;" src="/web/img/icon-services.png"></div>
				<h2><spring:message code="web.bkprof-features.multiservices"/></h2>
	    		<h1><spring:message code="web.bkprof-features.multiservicesMin"/></h1>
			</div>
			<div id="bkprof-features-multidevice" class="cuadro_contenido break">
				<div class="imgFeature"><img src="/web/img/icon-multidevice.png"></div>
				<h2><spring:message code="web.bkprof-features.multidevice"/></h2>
	    		<h1><spring:message code="web.bkprof-features.multideviceMin"/></h1>
			</div>
			<div id="bkprof-features-cloud" class="cuadro_contenido right">
				<div class="imgFeature"><img style="width:70px;height:50px;" src="/web/img/icon-cloud.png"></div>
				<h2><spring:message code="web.bkprof-features.multicloud"/></h2>
	    		<h1><spring:message code="web.bkprof-features.multicloudMin"/></h1>
			</div>
		</div>
    </article>
    
    <article id="bkprof-contact" class="contact show">
		<div class="content">
	    	<div id="bkprof-contact-contact" class="cuadro_contenido">
				<h2><span class="strong"><spring:message code="web.bkprof-contact.telf"/></span> +34 687 901 502</h2>
				<h2><span class="strong"><spring:message code="web.bkprof-contact.email"/></span> <a href="mailto:info@diloso.com">info@diloso.com</a></h2>
				<h1>*<spring:message code="web.bkprof-contact.notePrice"/></h1>
				<h1>**<spring:message code="web.bkprof-contact.notesWindows"/></h1>
			</div>

			<div id="bkprof-contact-form" class="cuadro_contenido">
		       	<h2><spring:message code="web.bkprof-contact.textInfo"/></h2>
       
	     	    <form action="/webmail/sendMailInfo" method="post" name="ContactInfo" id="ContactInfo">
	           		<div><label for="name"><spring:message code="web.bkprof-contact.name"/>:<span class="required" title="<spring:message code="form.required"/>">*</span></label><input type="text" name="name" id="name" required/></div>
	               	<div><label for="surname"><spring:message code="web.bkprof-contact.surname"/>:</label><input type="text" name="surname" id="surname" /></div>
	                <div><label for="email"><spring:message code="web.bkprof-contact.email"/>:<span class="required" title="<spring:message code="form.required"/>">*</span></label><input type="text" name="email" id="email" required/></div>	
	                <div><label for="telf"><spring:message code="web.bkprof-contact.telf"/>:</label><input type="text" name="telf" id="telf" /></div>
	                <!-- <div><label for="address"><spring:message code="web.bkprof-contact.address"/>:</label><input type="text" name="address" id="address" /></div> -->
	   	            <div><label for="comment"><spring:message code="web.bkprof-contact.comment"/>:</label><textarea name="comment" id="comment" rows="5" cols="30"></textarea></div>
			      	<div><input type="button" value="<spring:message code="form.send"/>" onclick="verifierForm()" /></div>
	       	   	</form>
		   	</div> 		        		
        </div>
    </article>
  

<%@include file="pie.jsp"%>
  
