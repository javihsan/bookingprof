<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="java.util.Enumeration"%>

		<div id="idioma">
<% 
   	String paramsPath = "";
   	String param = "";
   	for(Enumeration e = request.getParameterNames();e.hasMoreElements(); ){
   		param = (String)e.nextElement();
  		if (!param.equals("lang")){
			paramsPath = "&"+param+"="+request.getParameter(param);
	   	}
   	}
   	if (RequestContextUtils.getLocale(request).getLanguage().equals("en")){ %>
		<a href="?lang=es<%=paramsPath%>"><img src="/web/img/idioma/espanol.jpg" width="70px" height="25px" alt="Español" /></a>
   	<% } else if (RequestContextUtils.getLocale(request).getLanguage().equals("es")){ %>
		<a href="?lang=en<%=paramsPath%>"><img src="/web/img/idioma/english.jpg" width="70px" height="25px" alt="English" /></a>
	<% } %>
			
		</div> 	

