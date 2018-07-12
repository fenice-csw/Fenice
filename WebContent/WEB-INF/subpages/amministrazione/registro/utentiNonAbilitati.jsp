<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />
<logic:notEmpty name="registroForm" property="utentiNonAbilitati">
	<display:table class="simple" width="98%" requestURI="/page/amministrazione/registro.do"
		name="requestScope.registroForm.utentiNonAbilitati"
		export="false" sort="list" pagesize="10" id="row">
		<display:column title="Utente">
			<html:multibox property="utentiSelezionati">
				<bean:write name="row" property="id" />
			</html:multibox>
			<bean:write name="row" property="fullName" />
		</display:column>
	</display:table>
	<br /><html:submit styleClass="submit" property="aggiungiUtenti" value="Aggiungi utenti" title="Aggiunge nuovi utenti al registro"/>
</logic:notEmpty>
<logic:empty name="registroForm" property="utentiNonAbilitati">
	<span><bean:message key="amministrazione.registro.messaggio1"/>.</span><br />
</logic:empty>
	