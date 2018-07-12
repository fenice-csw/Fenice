<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<p>
	<label for="gaAbilitata"><bean:message key="amministrazione.organizzazione.aoo.abilitata"/></label>:
	<html:checkbox property="gaAbilitata" />
	&nbsp;
	<label for="gaUsername"><bean:message key="amministrazione.organizzazione.aoo.username"/></label>:
	<html:text property="gaUsername" maxlength="50" size="25"></html:text>
	&nbsp;
	<label for="gaPwd"><bean:message key="amministrazione.organizzazione.aoo.password"/></label>:
	<html:password property="gaPwd" maxlength="20" size="20"></html:password>
	<br /><br />
	<label for="flagManuale">Modalit&agrave; di invio</label>:
 	<html:radio property="gaFlagInvio" styleId="flagManuale" value="false">
		<label for="flagManuale">Manuale</label>&nbsp;&nbsp;
	</html:radio>
 	<html:radio property="gaFlagInvio" styleId="flagAutomatico" value="true">
		<label for="flagAutomatico">Automatico</label>
	</html:radio>
	<br /><br />
	<label for="gaTimer">Invia alle</label>:
	<html:text property="gaTimer" maxlength="5" size="5" styleId="gaTimer"></html:text>
	<eprot:timepicker textField="gaTimer" timeFormat="H:i" step="30"/>&nbsp;
	<label>(valido solo con modalit&agrave; di invio automatico)</label>
</p>
