<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Stampa Registro Modifiche">


<html:form action="/page/amministrazione/registroModifiche.do">
<div id="protocollo-errori">
	  <html:errors bundle="bundleErroriProtocollo" />
	</div>

<p>
	<label for="dataInizio">
		<bean:message key="report.datainizio"/><span class="obbligatorio"> * </span>:
	</label>

	<html:text property="dataInizio" styleId="dataInizio" size="10" styleClass="obbligatorio" maxlength="10" />
	<eprot:calendar textField="dataInizio" />
    &nbsp;&nbsp;<label for="dataFine">
	<bean:message key="report.datafine"/><span class="obbligatorio"> * </span>:
	</label>
	<html:text property="dataFine" styleId="dataFine" styleClass="obbligatorio" size="10" maxlength="10" />
	<eprot:calendar textField="dataFine" />
	
	<br />
	
	<jsp:include page="/WEB-INF/subpages/stampa/prn-pro/uffici.jsp" /><br />
	<html:submit styleClass="submit" property="btnVisualizza" value="Visualizza" alt="Visualizza registro" />
	<html:submit styleClass="submit" property="btnStampa" value="Stampa" alt="Stampa registro" />

</p>

<hr />

        <logic:messagesPresent property="recordNotFound" message="true">
			Nessun dato da visualizzare.
		</logic:messagesPresent>

<logic:notEmpty name="REPORT_MODIFICHE">
 
<display:table class="simple" width="100%" requestURI="/page/amministrazione/registroModifiche.do"
			name="requestScope.REPORT_MODIFICHE"
			export="false" sort="list" pagesize="30" id="row">
			<display:column property="numeroProtocollo" title="Protocollo" />
			<display:column property="dataModifica" title="Data e Ora" />
			<display:column property="utente"  title="Utente" />
			<display:column property="campo"  title="Campo" />
			<display:column property="valorePrecedente"  title="Valore Precedente" />
			
</display:table>
			
			<%-- 
<table class="simple">
	<tr>
		<th><bean:message key="amministrazione.registromodifiche.protocollo"/></th>
		<th><bean:message key="amministrazione.registromodifiche.dataora"/></th>
		<th><bean:message key="amministrazione.registromodifiche.utente"/></th>
		<th><bean:message key="amministrazione.registromodifiche.campo"/></th>
		<th><bean:message key="amministrazione.registromodifiche.valoreprecedente"/></th>
	</tr>
	<logic:iterate id="modifica" name="REPORT_MODIFICHE">
		<tr>
		<td><bean:write name="modifica" property="numeroProtocollo" /></td>
		<td><bean:write name="modifica" property="dataModifica" format="dd/MM/yyyy " /></td>
		<td><bean:write name="modifica" property="utente" filter="false" /></td>
		<td><bean:write name="modifica" property="campo" filter="false" /></td>
		<td><bean:write name="modifica" property="valorePrecedente" filter="false" /></td>
		</tr>
	</logic:iterate>
</table>
--%>


</logic:notEmpty>


</html:form>


</eprot:page>



