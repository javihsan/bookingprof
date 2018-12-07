<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="organization" content="Diloso">
<meta name="author" content="Diloso">
<meta name="origen" content="Diloso">
<meta name="locality" content="Madrid, España">
<meta name="lang" content="es">
<meta name="description" content="BookingProf, booking for professionals">
<meta name="keywords" content="bookingprof, booking, reserva de cita, cita, citas, online, cita online, app, webapp, peluqueria, clinica">
<script src="/web/js/jQuery.js"></script>
<script src="/web/js/facadeWeb.js"></script>
<script src="/web/js/app.js"></script>
<link rel="stylesheet" type="text/css" href="/web/css/estilos.css">
<link rel="SHORTCUT ICON" href="/web/img/logo.png">

<title>BookingProf</title>

<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-53122341-1', 'auto');
  ga('send', 'pageview');

</script>

</head>

<body>

<section id="bkprof" class="show">	
	
		<header>
         	<div class="content">
		
				<img class="imagen_logo_titulo_corpo" src="/web/img/cabecera.jpg" border="0"/>

				<div class="rigth">
					<%@include file="mutiidioma.jsp"%>
								
					<div class="precio breakRight">
			    		<a data-router="article" href="#bkprof-contact">
			    		<div class="texto_precio_prueba">&nbsp;</div>
						<div>  
							<spring:message code="web.head.precioMes"/>
						</div>
						<div class="texto_precio_prueba">  
							<spring:message code="web.head.periodoPueba"/>
						</div></a>
					</div>
				</div>

				<nav class="breakRight">
					<a href="#bkprof-home" data-router="article" class="active"><spring:message code="web.head.nav.home"/></a>
					<a href="#bkprof-features" data-router="article" ><spring:message code="web.head.nav.features"/></a>
					<a href="#bkprof-contact" data-router="article"><spring:message code="web.head.nav.contact"/></a>
					<!--<a href="#bkprof-faq" data-router="article"><spring:message code="web.head.nav.faq"/></a>
					<a href="#bkprof-directory" data-router="article" >age code="web.head.nav.directory"/></a>
					<a href="#bkprof-protected" data-router="article" ><sprcode="web.head.nav.protected"/></a>-->
				</nav>
			</div>
			<div class="line"></div>
       	</header>
  
	  
