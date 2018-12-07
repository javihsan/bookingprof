<%
String urlOpener = (String)request.getAttribute("urlOpener");
%>
<HTML>
<HEAD>
<TITLE>AutoClose</TITLE>
<script language="JavaScript" SRC="../jsp/util.js"></script>
<script language="JavaScript" type="text/JavaScript">
<!--
function Cerrar(){		
	opener.location.href = "<%=urlOpener%>";
	cerrar();
}
//-->
</script>
</HEAD>
<BODY onLoad="javascript:Cerrar();">
</BODY>
</HTML>