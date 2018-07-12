<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<table summary="">
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.tipo"/>&nbsp;:</span>
    </td>
    <td class="subtable">
      <table  summary="">
        <tr>
          <td>
          <logic:equal name="ricercaForm" property="tipoUfficioRicerca" value="C">
          		<html:radio property="tipoProtocollo" value="" styleId="tipo_tutti"
            		onclick="document.forms[0].submit()">
            		<label for="tipo_tutti">
						Ingresso+Uscita
                	</label>
            	</html:radio>&nbsp;
            </logic:equal>
			
			<logic:iterate id="tipo" name="tipiProtocollo">	
				<bean:define id="codice" name="tipo" property="codice" />
				<html:radio property="tipoProtocollo"
                	value="codice" idName="tipo" onclick="document.forms[0].submit()">
                	<label for="tipo_<bean:write name='tipo' property='codice' />"><bean:write name="tipo" property="descrizione" /></label>
            	</html:radio>&nbsp;
    		</logic:iterate>
			
            <noscript>
              <div>
            	<html:submit property="btnImpostaTipo" value="Imposta" 
            	    styleClass="button" title="Imposta il tipo di protocollo" />
              </div>
            </noscript>
            
          </td>
        </tr>
      </table>
   
  </tr> 
  <tr>
    <td class="label">
      <label for="statoProtocollo"><bean:message key="protocollo.stato"/></label>&nbsp;:
    </td>
    <td class="subtable">
      <table summary="">
        <tr>
          <td>
			<html:select property="statoProtocollo" styleId="statoProtocollo">
					<bean:define id="tutti" name="ricercaForm" property="tutti" />
					<option value="">
						<bean:write name="ricercaForm" property="tutti" />
            		</option>
					<html:optionsCollection name="ricercaForm"  property="statiProtocollo" value="codice" label="descrizione" />
			</html:select>
          </td>
          <td>&nbsp;&nbsp;</td>
          <td><label for="flagRiservato"><bean:message key="protocollo.mittente.riservato"/></label>&nbsp;:</td>
          <td><html:checkbox property="riservato" styleId="flagRiservato" /></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
  	<td class="label"><span><bean:message key="protocollo.dataregistrazione"/>&nbsp;</span></td>
    <td class="subtable">
      <table summary="">
        <tr>
          <td>
            <label title="Data registrazione minima" for="dataRegistrazioneDa"><bean:message key="protocollo.da"/></label>&nbsp;:
          </td>
          <td>
            <html:text property="dataRegistrazioneDa" styleId="dataRegistrazioneDa" size="10" maxlength="10" />
          </td>
          <td>
		      <eprot:calendar textField="dataRegistrazioneDa" />
          </td>
          <td>&nbsp;&nbsp;</td>
          <td>
            <label title="Data registrazione massima" for="dataRegistrazioneA"><bean:message key="protocollo.a"/></label>&nbsp;:
          </td>
          <td>
            <html:text property="dataRegistrazioneA" styleId="dataRegistrazioneA" size="10" maxlength="10" />
          </td>
          <td>
           <eprot:calendar textField="dataRegistrazioneA" />
    </td>
  </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td class="label"><span><bean:message key="protocollo.numero_anno"/>&nbsp;:</span></td>
    <td class="subtable">
      <table summary="">
        <tr>
          <td>
            <label for="numeroProtocolloDa"><bean:message key="protocollo.da"/></label>
          </td>
          <td>
            <html:text property="numeroProtocolloDa" styleId="numeroProtocolloDa" size="8" maxlength="10" />&nbsp;/&nbsp;<html:text property="annoProtocolloDa" size="4" maxlength="4" />
          </td>
          <td>&nbsp;&nbsp;</td>
          <td>
            <label for="numeroProtocolloA"><bean:message key="protocollo.a"/></label>
          </td>
          <td>
            <html:text property="numeroProtocolloA" styleId="numeroProtocolloA" size="8" maxlength="10" />&nbsp;/&nbsp;<html:text property="annoProtocolloA" size="4" maxlength="4" />
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <logic:equal name="ricercaForm" property="visualizzaDestinatariAssegnatari" value="I">
  <tr>
  <td class="label"><span><bean:message key="protocollo.mittente.protocolloid"/>&nbsp;:</span></td>
  <td>
  <html:text property="numeroProtocolloMittente" styleId="numeroProtocolloMittente" size="50" maxlength="50" />
  </td>
  </tr>
  <tr>
		<td class="label">
			<label for="flagPEC"> 
				Registrato da PEC: 
			</label>
		</td>
		<td>
			<html:checkbox property="flagPEC" styleId="flagPEC"></html:checkbox>
		</td>
	</tr>
</logic:equal>
</table>

 
