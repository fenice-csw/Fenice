<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
 
 <script>
 
	function impostaTipoFatturaElettronica(){
		document.getElementById("vaiButton").click();
	}
	
</script>
 
 <html:hidden property="emailId"/>
 <html:submit styleClass="button" property="impostaTipoFatturaElettronicaAction"
				value="Vai" title="Imposta Id della fattura" styleId="vaiButton" style="display: none;"/>
				
<table summary="Protocollazione Messaggi Email">
  <tr>
    <td class="label">
      <label for="nomeMittente"><bean:message key="email.ingresso.nome_mittente"/>&nbsp;:</label>
    </td>
    <td>
        <html:text property="nomeMittente" styleId="nomeMittente" size="50" maxlength="200"/>
    </td>
    <td>
    &nbsp;
    </td>
  </tr>
  <tr>
    <td class="label">
      <label for="emailMittente"><bean:message key="email.ingresso.email_mittente"/>&nbsp;:</label>
    </td>
    <td>
        <html:text property="emailMittente" styleId="emailMittente" size="50" maxlength="200"/>
    </td>
    <td>
       &nbsp;
    </td>
  </tr>
  <tr>
    <td class="label">
      <label for="oggetto"><bean:message key="email.ingresso.oggetto"/>&nbsp;:</label>
    </td>
    <td>
        <html:text property="oggetto" styleId="oggetto" size="50" maxlength="200"/>
    </td>
    <td>
    &nbsp;
    </td>
  </tr>
  <tr>
    <td class="label">
      <label title="Data di Spedizione" for="dataSpedizione"><bean:message key="email.ingresso.data_spedizione"/></label>&nbsp;:
    </td>
    <td>
      <html:text styleClass="text" property="dataSpedizione" styleId="dataSpedizione" size="10" maxlength="10" />
      <eprot:calendar textField="dataSpedizione" />
      &nbsp;
    </td>
    <td>
    &nbsp;
    </td>
  </tr>
  <tr>
    <td class="label">
      <label title="Data di Ricezione" for="dataRicezione"><bean:message key="email.ingresso.data_ricezione"/></label>&nbsp;:
    </td>
    <td>
      <html:text styleClass="text" property="dataRicezione" styleId="dataRicezione" size="10" maxlength="10" />
      <eprot:calendar textField="dataRicezione" />
      &nbsp;
    </td>
    <td>
    &nbsp;
    </td>
  </tr>
  <tr>
  	<td>
  		<label title="Testo Mail" for="testoMessaggio"><bean:message key="email.ingresso.testomessaggio"/></label>&nbsp;:
  	</td>
  	<td>
  		<html:textarea readonly="true" property="testoMessaggio" cols="70" rows="20"></html:textarea>
  	</td>
  </tr>
  <tr>
      <td>
        <label for="docPrincipaleId">
        	<bean:message key="email.ingresso.documento_principale"/>
        </label>&nbsp;
        
     </td>
     <td>
     	<html:radio property="tipoDocumentoPrincipale"
              value="BODY" >
          <label for="bodyMessaggio"><bean:message key="email.ingresso.testomessaggio"/></label>
        </html:radio><br/>
       <logic:notEmpty name="listaEmailForm" property="allegatiEmail">
        <html:radio property="tipoDocumentoPrincipale"
              value="ALLEGATO" >
          <label for="allegatoMessaggio"><bean:message key="email.ingresso.allegatomessaggio"/></label>
        </html:radio>
       </logic:notEmpty>&nbsp;
       <logic:notEmpty name="listaEmailForm" property="allegatiEmail">
        <html:select property="docPrincipaleId">
          <html:optionsCollection property="allegatiEmail" value="id" label="descrizione" />
        </html:select>
        <label for="fatturaElettronica">Fattura Elettronica</label>
		<html:checkbox property="fatturaElettronica" styleId="fatturaElettronica" disabled="false" onclick="javascript:impostaTipoFatturaElettronica()"/>
		<logic:equal name="listaEmailForm" property="fatturaElettronica" value="true">
			<html:link action="/page/protocollo/ingresso/email.do" 
				paramId="visualizzaFatturaBrowser" 
				paramName="listaEmailForm" 
				paramProperty="docPrincipaleId"
				styleClass="button" 
				title="Visualizza nel browser"
				target="_blank">
				Visualizza
			</html:link>
		</logic:equal>
       </logic:notEmpty>
      </td>
   </tr>
</table>
