<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />
	
<eprot:page title="Acquisizione massiva">

	<bean:define value="/page/amministrazione/acquisizioneMassiva.do" scope="request" id="url" />
	<div id="protocollo-errori">
	<html:errors bundle="bundleErroriProtocollo" />
	</div>
	<logic:empty name="acquisizioneMassivaForm" property="documentiAcquisizione">
	<br />
	<span><strong><bean:message key="amministrazione.acquisizione.messaggio"/>.</strong></span>
	</logic:empty>
	<html:form action="/page/amministrazione/acquisizioneMassiva.do">

		<div>
		<logic:notEmpty name="acquisizioneMassivaForm" property="documentiAcquisizione">
			<display:table class="simple" width="100%" requestURI="/page/amministrazione/acquisizioneMassiva.do"
					name="requestScope.acquisizioneMassivaForm.documentiAcquisizione" 
					export="false" sort="list" pagesize="15" id="row">
				<display:column property="name" title="Nome File" />
				<display:column title="Da eliminare">
					<bean:define id="rowName" name="row" property="name"/>	
					<input type="checkbox" name="documentiDaEliminare" value="<bean:write name='row' property='name'/>" />
				</display:column>


			</display:table>
			<p>
				<html:submit styleClass="submit" property="btnConferma"	value="Acquisisci i documenti" alt="Acquisisci i documenti" />
				<html:submit styleClass="submit" property="btnCancellaFiles"	value="Elimina i files selezionati" alt="Elimina i files selezionati" />
			</p>
		</logic:notEmpty>

		</div>
	</html:form>

</eprot:page>

