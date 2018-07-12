<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Scan Agent Frame</title>
</head>
<body>
<div align="center">
	
	<applet
		code="it.compit.scanagent.Applet"
		archive="FeniceScanAgent.jar"
		width =700
		height=450
		>
		<%
			//Nome file senza estensione
			if(request.getParameter("jSessID") != null)
				out.println("<param name=\"jSessID\"  value=\""+request.getParameter("jSessID") + "\" />");
			else
				out.println("<param name=\"jSessID\"  value=\"\" />");
		
			if(request.getParameter("url") != null)
				out.println("<param name=\"url\"  value=\""+request.getParameter("url") + "\" />");
			else
				out.println("<param name=\"url\"  value=\"\" />");
			
			if(request.getParameter("nProt") != null)
				out.println("<param name=\"nProt\"  value=\""+request.getParameter("nProt") + "\" />");
			else
				out.println("<param name=\"nProt\"  value=\"\" />");
		%>
	</applet>
	</div>
</body>
</html>