<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<html:xhtml />

<br />
<table>

</table>
<logic:iterate id="mittente" name="protocolloForm" property="mittenti">   
<fieldset>
<legend>Mittente </legend>

<table id="mittenti">

  <logic:notEmpty name="protocolloForm" property="numProtocolloMittente">
  <tr>
		<td class="label">
	      <span><bean:message key="protocollo.mittente.protocolloid" />&nbsp;:</span>
	    </td>
	    <td><span><strong>
			<bean:write name="protocolloForm" property="numProtocolloMittente"/>
	    	</strong></span>
	    </td>
	    <td class="label">
      		<span title="Data del documento"><bean:message key="protocollo.documento.data"/></span>&nbsp;:
    	</td>
    	<td>
    		<span><strong>
    		<bean:write name="protocolloForm" property="dataProtocolloMittente" />
    		</strong></span>&nbsp;
    	</td>
  </tr>
  </logic:notEmpty>

  <tr>
		<td class="label">
	      <span><bean:message key="protocollo.mittente.descrizione" />&nbsp;:</span>
	    </td>
	    <td><span><strong>
			<bean:write name="mittente" property="descrizione"/>
	    	</strong></span></td>
  </tr>
  
  <tr>
    <td class="label">
      <span><bean:message key="soggetto.indirizzo" />&nbsp;:</span>
    </td>
    <td><span><strong>
		<bean:write name="mittente" property="indirizzoNumCivico"/>
    	</strong></span></td>
  </tr>
  <tr>
    <td class="label">
      <span><bean:message key="soggetto.localita" />&nbsp;:</span>
    </td>
    <td><span><strong>
		<bean:write name="mittente" property="indirizzo.comune"/>
    	</strong></span></td>
   </tr>
   <tr>
    <td class="label">
      <span title="Codice di Avviamento Postale"><bean:message key="soggetto.cap" />&nbsp;:</span>
    </td>
    <td>
    	<span><strong>
			<bean:write name="mittente" property="indirizzo.cap"/>
    		&nbsp;&nbsp;</strong></span>
			<span><bean:message key="soggetto.provincia" />&nbsp;:</span>
        	<span><strong><html:select name="mittente" disabled="true" property="indirizzo.provinciaId">
				<html:optionsCollection property="province" value="provinciaId" label="descrizioneProvincia" />
				</html:select>
			</strong></span>
	</td>
  </tr>
  <tr>
    <td class="label">
      <span><bean:message key="soggetto.email" />&nbsp;:</span>
    </td>
    <td><span><strong>
		<bean:write name="mittente" property="indirizzoEMail"/>
    	</strong></span></td>
   </tr>
</table>
</fieldset>
</logic:iterate>

