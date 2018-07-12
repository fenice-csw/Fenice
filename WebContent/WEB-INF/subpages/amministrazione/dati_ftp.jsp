<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml />
<table >
<tr>
	<td>
		<label for="hostFtp"><bean:message key="amministrazione.host"/><span class="obbligatorio"> * </span></label>:
	</td>
	<td>
		<html:text styleClass="obbligatorio" property="hostFtp" styleId="hostFtp" maxlength="256" size="100"></html:text><br />
	</td>
</tr><tr>
	<td>
		<label for="folderFtp"><bean:message key="amministrazione.folder"/></label>:
	</td>
	<td>
		<html:text styleClass="obbligatorio" property="folderFtp" styleId="folderFtp" maxlength="256" size="100"></html:text><br />
	</td>
</tr>
<tr>
	<td>
		<label for="portaFtp"><bean:message key="amministrazione.porta"/><span class="obbligatorio"> * </span></label>:
	</td>
	<td>
		<html:text styleClass="obbligatorio" property="portaFtp" styleId="portaFtp" maxlength="10" size="10"></html:text><br />
	</td>
</tr>
<tr>
	<td>
		<label for="userFtp"><bean:message key="amministrazione.user"/><span class="obbligatorio"> * </span></label>:
	</td>
	<td>
		<html:text styleClass="obbligatorio" property="userFtp" styleId="userFtp" maxlength="256" size="100"></html:text><br />
	</td>
</tr>

<tr>
	<td>
		<label for="passFtp"><bean:message key="amministrazione.pass"/><span class="obbligatorio"> * </span></label>:
	</td>
	<td>
		<html:password styleClass="obbligatorio" property="passFtp" styleId="passFtp" maxlength="256" size="100"></html:password>
		
	</td>
</tr>

</table>
	


	

	
	
	

	
	
	
	
