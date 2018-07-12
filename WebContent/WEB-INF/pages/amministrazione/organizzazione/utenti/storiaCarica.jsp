<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Storia Carica">

<html:form action="/page/amministrazione/carica.do">
<div>
<br/>
<br/>
<label>Ufficio</label>: <span><strong>
<bean:write name="caricaForm" property="nomeUfficio" />
</strong></span>
<br/>


<logic:notEmpty name="caricaForm" property="versioniCarica">
<hr></hr>
<table summary="" cellpadding="2" cellspacing="2" border="1" width="100%">
	<tr>
		<th>
			<span><bean:message key="protocollo.versione"/></span>
		</th>
		<th>
			<span><bean:message key="protocollo.data.versione"/></span>
		</th>	
		<th>
			<span><bean:message key="protocollo.userupdate"/></span>
		</th>
		<th>
			<span>Nome</span>
		</th>

		<th>
			<span>Utente</span>
		</th>
		<th>
			<span>Profilo</span>
		</th>
		<th>
			<span>Referente</span>
		</th>
				
	</tr>
	<tr>
		<td>
			<span>
				<bean:write name="caricaForm" property="versione"/>
			</span>
		</td>
		
		<td>
			<span><bean:write name="caricaForm" property="dataOperazione" /></span>
		</td>
		<td>
			<span><bean:write name="caricaForm" property="autore" /></span>
		</td>
		<td>
			<span><bean:write name="caricaForm" property="nome" /></span>
		</td>

		<td>
			<span><bean:write name="caricaForm" property="utente.fullName" /></span>
		</td>
		<td>
			<span><bean:write name="caricaForm" property="profilo.descrizioneProfilo" /></span>
		</td>
		<td>
			<span><bean:write name="caricaForm" property="referente" /></span>
		</td>	
	</tr>

	
	<bean:define id="versioniCarica" name="caricaForm" property="versioniCarica" />
	<logic:iterate id="currentRecord" name="caricaForm" property="versioniCarica">
	<tr>
		<td>
			<span><bean:write name="currentRecord" property="versione"/></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="dataOperazione" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="autore" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="carica" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="utente" /></span>
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




