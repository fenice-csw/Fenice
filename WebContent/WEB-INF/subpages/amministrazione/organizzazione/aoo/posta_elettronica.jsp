<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml />
<p>
	<label for="pnAbilitata"><bean:message key="amministrazione.organizzazione.aoo.abilitata"/></label>:
	<html:checkbox property="pnAbilitata" />
&nbsp;	
	<label for="pn_indirizzo"><bean:message key="amministrazione.organizzazione.aoo.indirizzo"/></label>:
	<html:text property="pn_indirizzo" maxlength="200" size="50"></html:text>
&nbsp;
	<label for="pn_ssl"><bean:message key="amministrazione.organizzazione.aoo.ssl"/></label>:
	<html:checkbox property="pn_ssl" />
&nbsp;
	<label for="pn_ssl_port"><bean:message key="amministrazione.organizzazione.aoo.portassl"/></label>:
	<html:text property="pn_ssl_port" maxlength="5" size="5"></html:text> 
</p>

<p>
	<label for="pn_username"><bean:message key="amministrazione.organizzazione.aoo.username"/></label>:
	<html:text property="pn_username" maxlength="50" size="25"></html:text>
&nbsp;
	<label for="pn_pwd"><bean:message key="amministrazione.organizzazione.aoo.password"/></label>:
	<html:password property="pn_pwd" maxlength="20" size="20"></html:password> 
</p>

<p>
	<label for="pn_pop3"><bean:message key="amministrazione.organizzazione.aoo.hostpop"/></label>:
	<html:text property="pn_pop3" maxlength="100" size="30"></html:text> 
&nbsp;
	<label for="pn_smtp"><bean:message key="amministrazione.organizzazione.aoo.hostsmtp"/></label>:
	<html:text property="pn_smtp" maxlength="100" size="30"></html:text> 
</p>