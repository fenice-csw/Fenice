<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione tipi documento">
<%--<c:url value="/page/amministrazione/tipidocumento.do" var="url" scope="request" />--%>
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<html:form action="page/amministrazione/tipidocumento.do" >
<logic:notEmpty name="tipoDocumentoForm" property="tipiDocumento">
<display:table class="simple" width="100%"
	requestURI="/page/amministrazione/tipidocumento.do"
	name="requestScope.tipoDocumentoForm.tipiDocumento" export="false"
	sort="list" pagesize="10" id="row">
	<display:column title="Descrizione" property="descrizione"	
		url="/page/amministrazione/tipidocumento.do" 
		paramId="parId" paramProperty="id" />
	<display:column property="protocolli" title="Protocolli associati" />
</display:table>
</logic:notEmpty>
<hr></hr>
<table summary="">
	<tr>
		<td>		
		 	<html:submit styleClass="submit" property="btnNuovoTipoDocumento" value="Nuovo" title="Inserisce un nuovo tipo di documento"/>
		</td>
	</tr>
</table>
</html:form>
</eprot:page>
