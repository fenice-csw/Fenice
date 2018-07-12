<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>
<logic:messagesPresent property="registroSalvato" message="true"> 
<div id="messaggi">  
	<bean:message key="stato_registro_salvato" bundle="bundleErroriProtocollo"/>
  <br />
 </div>
</logic:messagesPresent>

<br />


<table summary="">
  <tr>
    <td class="label"><label for="registroInUso"><bean:message key="protocollo.registro"/></label>&nbsp;:</td>
    <td>
      <bean:define id="registroVOInUso" name="UTENTE_KEY" property="registroVOInUso" />
      <bean:define id="descrizioneRegistro" name="registroVOInUso" property="descrizioneRegistro" />
      <span id="registroInUso"><strong><bean:write name="registroVOInUso" property="descrizioneRegistro" /></strong></span>
    </td>
  </tr>
  <tr>
    <td class="label"><label for="dataAperturaRegistro"><bean:message key="protocollo.registro.dataApertura"/></label>&nbsp;:</td>
    <td>
      <html:text property="dataApertura" styleId="dataAperturaRegistro" size="10" />
     <eprot:calendar textField="dataAperturaRegistro" />
    </td>
  </tr>
  <tr>
    <td class="label"><span><bean:message key="protocollo.registro.cambioData"/></span>&nbsp;:</td>
    <td>
      <html:radio styleId="dataAutomatica" property="dataBloccata" value="false">
        <label for="dataAutomatica"><bean:message key="protocollo.registro.automatico"/></label>&nbsp;&nbsp;
      </html:radio>
      <html:radio styleId="dataManuale" property="dataBloccata" value="true">
        <label for="dataManuale"><bean:message key="protocollo.registro.manuale"/></label>
      </html:radio>
    </td>
  </tr>
  <tr>
    <td class="label"><label for="apertoIngresso"><bean:message key="protocollo.registro.registrazioneiningresso"/></label>&nbsp;:</td>
    <td>
      <html:checkbox styleId="apertoIngresso" property="apertoIngresso" />&nbsp;&nbsp;
    </td>
  </tr>
  <tr>
    <td class="label"><label for="apertoUscita"><bean:message key="protocollo.registro.registrazioneinuscita"/></label>&nbsp;:</td>
    <td>
      <html:checkbox styleId="apertoUscita" property="apertoUscita" />&nbsp;&nbsp;
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td class="label"><html:submit styleClass="submit" property="salvaAction" value="Salva" /></td>
  </tr>
</table>

