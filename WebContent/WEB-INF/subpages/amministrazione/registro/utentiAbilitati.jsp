<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page language="java" %>

<html:xhtml />
<logic:notEmpty name="registroForm" property="utentiAbilitati">
	<display:table class="simple" width="98%" requestURI="/page/amministrazione/registro.do"
		name="requestScope.registroForm.utentiAbilitati"
		export="false" sort="list" pagesize="10" id="row">
		<display:column title="Utente">
			<html:multibox property="utentiSelezionati">
				<bean:write name="row" property="id"/>
			</html:multibox>
			<bean:write name="row" property="fullName"/>
		</display:column>
	</display:table>
	<br /><html:submit styleClass="submit" property="rimuoviUtenti" value="Rimuovi utenti" title="Rimuovi gli utenti selezionati"/>
</logic:notEmpty>
<logic:empty name="registroForm" property="utentiAbilitati">
	<span><bean:message key="amministrazione.registro.messaggio1"/>.</span><br />
</logic:empty>	