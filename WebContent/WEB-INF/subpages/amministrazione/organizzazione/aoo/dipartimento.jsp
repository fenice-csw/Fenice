<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml />
<label for="dipartimento_codice"><bean:message key="amministrazione.organizzazione.aoo.codice"/></label>:
<html:text property="dipartimento_codice" maxlength="20" size="20"></html:text>
<label for="dipartimento_descrizione"><bean:message key="amministrazione.organizzazione.aoo.descrizione"/></label>:
<html:text property="dipartimento_descrizione" maxlength="20" size="20"></html:text>					
