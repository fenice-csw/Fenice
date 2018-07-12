<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml />
<p>
	<label for="prec_indirizzo"><bean:message key="amministrazione.organizzazione.aoo.indirizzo_invio"/></label>:
	<html:text property="prec_indirizzo_invio" maxlength="200" size="50"></html:text>
&nbsp;	
	<label for="prec_indirizzo"><bean:message key="amministrazione.organizzazione.aoo.indirizzo_ricezione"/></label>:
	<html:text property="prec_indirizzo_ricezione" maxlength="200" size="50"></html:text>
</p>
<p>
	<label for="prec_username"><bean:message key="amministrazione.organizzazione.aoo.username"/></label>:
	<html:text property="prec_username" maxlength="50" size="25"></html:text>
&nbsp;
	<label for="prec_pwd"><bean:message key="amministrazione.organizzazione.aoo.password"/></label>:
	<html:password property="prec_pwd" maxlength="20" size="20"></html:password> 
&nbsp;	
	<label for="prec_smtp"><bean:message key="amministrazione.organizzazione.aoo.hostsmtp"/></label>:
	<html:text property="prec_smtp" maxlength="100" size="30"></html:text>
</p>
