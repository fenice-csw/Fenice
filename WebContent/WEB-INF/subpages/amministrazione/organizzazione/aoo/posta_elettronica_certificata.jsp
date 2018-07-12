<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml />
<p>
	<label for="pecAbilitata"><bean:message key="amministrazione.organizzazione.aoo.abilitata"/></label>:
	<html:checkbox property="pecAbilitata" />
&nbsp;
	<label for="pec_indirizzo"><bean:message key="amministrazione.organizzazione.aoo.indirizzo"/></label>:
	<html:text property="pec_indirizzo" maxlength="200" size="50"></html:text>
&nbsp;
	<label for="pec_ssl_port"><bean:message key="amministrazione.organizzazione.aoo.porta"/></label>:
	<html:text property="pec_ssl_port" maxlength="5" size="5"></html:text> 

</p>
<p>
	<label for="pec_username"><bean:message key="amministrazione.organizzazione.aoo.username"/></label>:
	<html:text property="pec_username" maxlength="50" size="25"></html:text>
&nbsp;
	<label for="pec_pwd"><bean:message key="amministrazione.organizzazione.aoo.password"/></label>:
	<html:password property="pec_pwd" maxlength="20" size="20"></html:password> 
</p>
<p>
	<label for="pec_pop3"><bean:message key="amministrazione.organizzazione.aoo.hostpop"/></label>:
	<html:text property="pec_pop3" maxlength="100" size="30"></html:text> 
&nbsp;
	<label for="pec_smtp"><bean:message key="amministrazione.organizzazione.aoo.hostsmtp"/></label>:
	<html:text property="pec_smtp" maxlength="100" size="30"></html:text>
&nbsp;
	<label for="pec_smtp_port"><bean:message key="amministrazione.organizzazione.aoo.portasmtp"/></label>:
	<html:text property="pec_smtp_port" maxlength="5" size="5"></html:text> 
</p>
