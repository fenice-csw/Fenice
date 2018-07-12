<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml />
<table >
<tr>
	<td>
		<label for="parametriLDap.versione"><bean:message key="amministrazione.versione"/><span class="obbligatorio"> * </span></label>:
	</td>
	<td>
			<html:text styleClass="obbligatorio" property="parametriLDap.versione" styleId="parametriLDap.versione" maxlength="5" size="5"></html:text><br />
	</td>
</tr>

<tr>
	<td>
		<label for="parametriLDap.porta"><bean:message key="amministrazione.porta"/><span class="obbligatorio"> * </span></label>:
	</td>
	<td>
		<html:text styleClass="obbligatorio" property="parametriLDap.porta" styleId="parametriLDap.porta" maxlength="10" size="10"></html:text><br />
	</td>
</tr>
<tr>
	<td>
			<label for="parametriLDap.use_sslSi"><bean:message key="amministrazione.ssl"/><span class="obbligatorio"> * </span></label>:
	</td>
	<td>
	<table>
		<tr>
			<td>
				<html:radio property="parametriLDap.use_ssl" styleId="parametriLDap.use_sslSi" value="1">
			</td>
			<td>
				<label for="parametriLDap.use_sslSi"><bean:message key="amministrazione.si"/></label>&nbsp;&nbsp;
			</html:radio>
			</td>
			<td>
				<html:radio property="parametriLDap.use_ssl" styleId="parametriLDap.use_sslNo" value="0">
			</td>
			<td>
				<label for="parametriLDap.use_sslNo"><bean:message key="amministrazione.no"/></label>
				</html:radio><br />
			</td>
		</tr>
	</table>
		
	</td>
</tr>
<tr>
	<td>
		<label for="parametriLDap.host"><bean:message key="amministrazione.host"/><span class="obbligatorio"> * </span></label>:
	</td>
	<td>
		<html:text styleClass="obbligatorio" property="parametriLDap.host" styleId="parametriLDap.host" maxlength="256" size="60"></html:text><br />
	</td>
</tr>
<tr>
	<td>
		<label for="parametriLDap.dn"><bean:message key="amministrazione.dn"/><span class="obbligatorio"> * </span></label>:
	</td>
	<td>
		<html:text styleClass="obbligatorio" property="parametriLDap.dn" styleId="parametriLDap.dn" maxlength="256" size="60"></html:text><br />
	</td>
</tr>

</table>
	


	

	
	
	

	
	
	
	
