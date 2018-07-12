<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Stampa Etichette Destinatari">

<html:form action="/page/stampa/prn-pro/etichetteDestinatari.do">


<%--<logic:equal name="reportForm" property="visualizzaDettagli" value="false">--%>
<%--	<logic:notEmpty name="reportEtichetteForm" property="btnStampa">--%>

	<div>  
<logic:messagesPresent property="recordNotFound" message="true">
Nessun dato da visualizzare.
</logic:messagesPresent>
<logic:notEmpty name="reportForm" property="reportFormatsCollection">
Selezionare il formato della stampa:
<br />
<br />
<li>
<logic:iterate id="currentRecord" name="reportForm" property="reportFormatsCollection" >
  <html:link action="/page/stampa/prn-pro/etichetteDestinatari" name="currentRecord" property="parameters" target="_blank">
	<bean:write name="currentRecord" property="descReport"/>
  </html:link >
  <br />
</logic:iterate>  
</li>  
</logic:notEmpty>  
</div>
	
<%--	</logic:notEmpty>--%>
<%--</logic:equal>--%>


<%--<logic:equal name="reportForm" property="visualizzaDettagli" value="true">-->
<!--<div>-->
<!--	<jsp:include page="/WEB-INF/subpages/stampa/prn-pro/statistiche/dettaglio.jsp" />-->
<!--</div>-->
<!---->
<!--</logic:equal>--%>

</html:form>
</eprot:page>



