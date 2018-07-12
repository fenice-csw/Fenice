<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>


<html:xhtml />
<html:hidden property="id"/>
<html:hidden property="versione"/>
<table>
 <tr>
    <td>
  	<table>
 		<tr>
 			<td>
 				<label for="codice"><bean:message key="amministrazione.codice"/><span class="obbligatorio"> * </span></label>:
 			</td>
 			<td>
 				<html:text property="codice" styleClass="obbligatorio" styleId="codice" maxlength="20" size="20"></html:text><br/>
			<td>
			</td>
		</tr>
 	</table>
    </td>
  </tr>
  
 <tr> 
 	<td colspan="2">
 	
 		<label for="descrizione"><bean:message key="amministrazione.descrizione"/><span class="obbligatorio"> * </span></label>:
 	 	<html:text property="descrizione" styleClass="obbligatorio" styleId="descrizione" maxlength="256" size="60"></html:text><br />
 	</td>
 	
 	
 </tr>
  <tr>
 	<td>
 		<label for="flagRegistroPostaSeparatoNo"><bean:message key="amministrazione.registro_posta_separato"/></label>:
 	</td>
 	<td>
	 	<table>
	 		<tr>
		 		<td>
		 			<html:radio property="flagRegistroPostaSeparato" styleId="flagRegistroPostaSeparatoNo" value="0">
					<label for="flagRegistroPostaSeparatoNo"><bean:message key="amministrazione.no"/></label>&nbsp;&nbsp;
					</html:radio>
		 		</td>
		 		<td>
		 			<html:radio property="flagRegistroPostaSeparato" styleId="flagRegistroPostaSeparatoSi" value="1">
					<label for="flagRegistroPostaSeparatoSi"><bean:message key="amministrazione.si"/></label>
					</html:radio><br />
				
				</td>
			</tr>
	 	</table>
 	</td>
 </tr>
 <tr>
 	<td>
 		<label for="flagLdapNo"><bean:message key="amministrazione.autenticazione"/></label>:
 	</td>
 	<td>
 	<table>
 	<tr>
 	<td>
 		<html:radio property="flagLdap" styleId="flagLdapNo" value="0">
		<label for="flagLdapNo"><bean:message key="amministrazione.no"/></label>&nbsp;&nbsp;
		</html:radio>
 	</td>
 	<td>
 	<html:radio property="flagLdap" styleId="flagLdapSi" value="1">
	<label for="flagLdapSi"><bean:message key="amministrazione.si"/></label>
	</html:radio><br />
	<td>
	</td>
	</tr>
	
 	</table>
 	</td>
 </tr>
 <tr>
	 <td>
		 <label for="pathDocAquisMassiva"><bean:message key="amministrazione.messaggio"/></label>:
	 </td>
	 <td>
		 <html:text property="pathDocAquisMassiva" styleId="pathDocAquisMassiva" maxlength="255" size="60"></html:text>
	 </td>
	 
 </tr>
 <tr>
	 <td>
		 <label for="pathDocumentiProtocollo"><bean:message key="amministrazione.messaggiodoc"/></label>:
	 </td>
	 <td>
		 <html:text property="pathDocumentiProtocollo" styleId="pathDocumentiProtocollo" maxlength="255" size="60"></html:text>
	 </td>
 </tr> 
 <tr>
 	<td>
 		<label for="flagRegistroPostaSeparatoNo"><bean:message key="amministrazione.flag_webscanner"/></label>:
 	</td>
 	<td>
	 	<table>
	 		<tr>
		 		<td>
		 			<html:radio property="webSocketEnabled" styleId="webSocketEnabledNo" value="0">
					<label for="webSocketEnabledNo"><bean:message key="amministrazione.no"/></label>&nbsp;&nbsp;
					</html:radio>
		 		</td>
		 		<td>
		 			<html:radio property="webSocketEnabled" styleId="webSocketEnabledSi" value="1">
					<label for="webSocketEnabledSi"><bean:message key="amministrazione.si"/></label>
					</html:radio><br />
				
				</td>
			</tr>
	 	</table>
 	</td>
 </tr>
</table>

	
	
	

	
	
	
	
