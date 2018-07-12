<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="siged" %>

<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Fenice - <bean:write name="title"/></title>
	
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/style/style.css" />' />
  <style type="text/css">@import url('<html:rewrite page="/script/calendar/calendar-blue.css" />');</style>
  <script type="text/javascript" src='<html:rewrite page="/script/calendar/calendar.js" />'></script>
  <script type="text/javascript" src='<html:rewrite page="/script/calendar/lang/calendar-it.js" />'></script>
  <script type="text/javascript" src='<html:rewrite page="/script/calendar/calendar-setup.js" />'></script>
  <script type="text/javascript" src='<html:rewrite page="/script/doc/albero.js" />'></script>
</head>

<body>
  <table id="layout" summary="">
  <tr>
  <td id="header" colspan="2">
    <span class="title">Fenice</span>
    <span class="aoo">
    
    <!--  
     <img  src="C:\Documents and Settings\bosco\Desktop\Logo Interno.gif"/>
    </span>
    -->
    <span class="account">
<%--      Benvenuto: <strong>${UTENTE_KEY.valueObject.fullName}</strong>&nbsp;&nbsp;-->
<!--      Ufficio: <strong>${UTENTE_KEY.ufficioVOInUso.description}</strong><br />-->
<!--      Registro: <strong>${UTENTE_KEY.registroVOInUso.descrizioneRegistro}</strong>&nbsp;&nbsp;-->
<!--      Ultimo protocollo: <strong>${UTENTE_KEY.ultimoProtocollo}</strong>--%>
          AOO: <strong><bean:write name="UTENTE_KEY" property="areaOrganizzativa.description" /></strong>&nbsp;&nbsp;
	      <html:link styleClass="logout" href="/cambioPwd.do">
	      Benvenuto: <strong><bean:write name="UTENTE_KEY" property="valueObject.fullName"/></strong>&nbsp;&nbsp;
	      </html:link>
	      Ufficio: <strong><bean:write name="UTENTE_KEY" property="ufficioVOInUso.description"/></strong><br />
	      Registro: <strong><bean:write name="UTENTE_KEY" property="registroVOInUso.descrizioneRegistro"/></strong>&nbsp;&nbsp;
	      Ultimo protocollo: <strong><bean:write name="UTENTE_KEY" property="ultimoProtocollo"/></strong>
    </span>
    <br class="hidden" />
    <a class="hidden" href="#body">Salta il menu</a>
    <br class="hidden" />
    <div id="tabmenu">
      <eprot:tab />
      <html:link styleClass="logout" href="/logoff.do">Logout</html:link>
    </div>
  </td>
  </tr>
  <tr>
    <td id="sidebar">
      <div id="sidemenu">
        <eprot:sidemenu />
        <br class="hidden" />
      </div>
    </td>
    <td id="body">
      <a name="body" />
      <div id="navbar">
        <eprot:navbar />
      </div>
      <jsp:doBody/>
    </td>
  </tr>
  </table>
<%--
  <div id="footer">
    <jsp:include page="/jsp/subpages/footer.jsp" />
  </div>
--%>
</body>
</html>
