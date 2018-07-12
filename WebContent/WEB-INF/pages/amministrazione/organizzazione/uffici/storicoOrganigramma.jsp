<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Storia Carica">

<html:form action="/page/amministrazione/organizzazione/selezionaUfficio.do">
<div>
<br/>
<br/>

<logic:notEmpty name="ufficioForm" property="storicoOrganigramma">
<hr></hr>
<table summary="" cellpadding="2" cellspacing="2" border="1" width="100%">
	<tr>
		<th>
			<span><bean:message key="protocollo.data.versione"/></span>
		</th>	
		<th>
			<span><bean:message key="protocollo.userupdate"/></span>
		</th>
		<th>
			<span>Modifica Effettuata</span>
		</th>

		<th>
			<span>Azione</span>
		</th>	
	</tr>
	<bean:define id="storicoOrganigramma" name="ufficioForm" property="storicoOrganigramma" />
	<logic:iterate id="currentRecord" name="ufficioForm" property="storicoOrganigramma">
	<tr>
		<td>
			<span><bean:write name="currentRecord" property="dataStorico" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="rowCreatedUser" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="descrizione" /></span>
		</td>
		<td>
			<html:link action="/page/amministrazione/organizzazione/selezionaUfficio.do" paramId="stampaSelezionato" 
				             paramName="currentRecord" 
				             paramProperty="id" 
				             target="_blank"
				             title="Stampa">
			<span>[Stampa]</span>
			</html:link>
		</td>
	</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<html:submit styleClass="button" property="btnIndietroStoria"
					value="Indietro" title="Indietro"></html:submit>
</div>
</html:form>


</eprot:page>




