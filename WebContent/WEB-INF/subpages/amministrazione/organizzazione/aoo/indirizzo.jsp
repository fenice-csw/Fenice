<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml />
<p>
		<label for="indi_dug"><bean:message key="amministrazione.organizzazione.aoo.dug"/><span class="obbligatorio"> * </span></label>:
		<html:text styleClass="obbligatorio" property="indi_dug" maxlength="20" size="20"></html:text>&nbsp;
		<label for="indi_toponimo"><bean:message key="amministrazione.organizzazione.aoo.toponimo"/><span class="obbligatorio"> * </span></label>:
		<html:text styleClass="obbligatorio" property="indi_toponimo" maxlength="20" size="20"></html:text>&nbsp;
		<label for="indi_civico"><bean:message key="amministrazione.organizzazione.aoo.civico"/><span class="obbligatorio"> * </span></label>:
		<html:text styleClass="obbligatorio" property="indi_civico" maxlength="10" size="10"></html:text> 
</p>
<p>					
		<label for="indi_cap"><bean:message key="amministrazione.organizzazione.aoo.cap"/><span class="obbligatorio"> * </span></label>:
		<html:text styleClass="obbligatorio" property="indi_cap" maxlength="5" size="5"></html:text>&nbsp;
		<label for="indi_comune"><bean:message key="amministrazione.organizzazione.aoo.comune"/><span class="obbligatorio"> * </span></label>:
		<html:text styleClass="obbligatorio" property="indi_comune" maxlength="30" size="30"></html:text>&nbsp;
		<label for="provincia_id"><bean:message key="amministrazione.organizzazione.aoo.provincia"/><span class="obbligatorio"> * </span></label>:
		<html:select styleClass="obbligatorio" property="provincia_id">
			<html:optionsCollection property="province" value="provinciaId" label="descrizioneProvincia" />
		</html:select>
</p>
<p>
	<label for="telefono"><bean:message key="amministrazione.organizzazione.aoo.telefono"/></label>:
	<html:text property="telefono" maxlength="20" size="20"></html:text>
&nbsp;
	<label for="fax"><bean:message key="amministrazione.organizzazione.aoo.fax"/></label>:
	<html:text property="fax" maxlength="20" size="20"></html:text><br/>
</p>
