<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Statistiche Protocolli">

<html:form action="/page/stampa/prn-pro/statistiche.do">
<div>
	<jsp:include page="/WEB-INF/subpages/stampa/prn-pro/statistiche/cerca.jsp" />
	<hr></hr>
</div>


<logic:equal name="reportForm" property="visualizzaDettagli" value="false">
	
	<logic:empty name="reportForm" property="btnStampa">
	<div>
		<jsp:include page="/WEB-INF/subpages/stampa/prn-pro/statistiche/lista.jsp" />
	</div>
	</logic:empty>
	


	<logic:notEmpty name="reportForm" property="btnStampa">
	<div>
		<jsp:include page="/WEB-INF/subpages/stampa/prn-pro/statistiche/stampa.jsp" />
	</div>
	</logic:notEmpty>

</logic:equal>


<logic:equal name="reportForm" property="visualizzaDettagli" value="true">
<div>
	<jsp:include page="/WEB-INF/subpages/stampa/prn-pro/statistiche/dettaglio.jsp" />
</div>

</logic:equal>
</html:form>
</eprot:page>



