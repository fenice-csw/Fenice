<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml />
<p>
	<label for="responsabile_nome"><bean:message key="amministrazione.organizzazione.aoo.nome"/></label>:
	<html:text property="responsabile_nome" maxlength="20" size="20"></html:text>
&nbsp;
	<label for="responsabile_cognome"> <bean:message key="amministrazione.organizzazione.aoo.cognome"/></label>:
	<html:text property="responsabile_cognome" maxlength="20" size="20"></html:text> 
</p>
<p>
	<label for="responsabile_email"><bean:message key="amministrazione.organizzazione.aoo.email"/></label>:
	<html:text property="responsabile_email" maxlength="20" size="20"></html:text> 
&nbsp;
	<label for="responsabile_telefono"><bean:message key="amministrazione.organizzazione.aoo.telefono"/> </label>:
	<html:text property="responsabile_telefono" maxlength="10" size="10"></html:text> 
</p>