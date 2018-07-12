<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />
<logic:notEmpty name="fascicoloForm" property="utenti">
	<html:select property="utenteSelezionatoId" styleClass="evidenziato" >
		<html:option value=""></html:option>
		<html:optionsCollection property="utenti" value="id" label="caricaFullName" />
	</html:select>
</logic:notEmpty>  
<logic:empty name="fascicoloForm" property="utenti">
	<html:hidden property="utenteSelezionatoId" value="0"/>
</logic:empty>

