<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Storia Carica">

<html:form action="/page/amministrazione/organizzazione/utenti/profilo.do">
<div>
<br/>
<br/>
<label>Utente</label>: <span><strong>
<bean:write name="profiloUtenteForm" property="cognome" /> <bean:write name="profiloUtenteForm" property="nome" />
</strong></span>
<br/>


<logic:notEmpty name="profiloUtenteForm" property="versioniCarica">
<hr></hr>
<table summary="" cellpadding="2" cellspacing="2" border="1" width="100%">
	<tr>
		<th>
			<span>Dal/Al</span>
		</th>	
		<th>
			<span>Carica</span>
		</th>
		<th>
			<span>Attivo</span>
		</th>
		<th>
			<span>Ufficio</span>
		</th>
		<th>
			<span>Profilo</span>
		</th>
		<th>
			<span>Referente</span>
		</th>
			
	</tr>
	<bean:define id="versioniCarica" name="profiloUtenteForm" property="versioniCarica" />
	<logic:iterate id="currentRecord" name="profiloUtenteForm" property="versioniCarica">
	<tr>
		<td>
			<span><bean:write name="currentRecord" property="dataInizio" />/<bean:write name="currentRecord" property="dataFine" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="carica" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="attivo" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="ufficio" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="profilo" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="referente" /></span>
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




