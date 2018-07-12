<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Conservazione Registro Annuale">

<div id="protocollo-errori">
	<html:errors bundle="bundleErroriProtocollo" />
</div>

<logic:messagesPresent message="true">
	<div id="protocollo-messaggi">
   		<ul>
   			<html:messages id="actionMessage" message="true" bundle="bundleErroriProtocollo">
      		<li>
      			<bean:write name="actionMessage"/>
      		</li>
   			</html:messages> 
   		</ul>
	</div>
</logic:messagesPresent>

<html:form action="/page/stampa/conservazione/registro_annuale.do">
<div>
	<jsp:include page="/WEB-INF/subpages/stampa/conservazione/registro_annuale/cerca.jsp" />
	<hr></hr>		
</div>
<div>
	<jsp:include page="/WEB-INF/subpages/stampa/conservazione/registro_annuale/lista.jsp" />
</div>
</html:form>
</eprot:page>



