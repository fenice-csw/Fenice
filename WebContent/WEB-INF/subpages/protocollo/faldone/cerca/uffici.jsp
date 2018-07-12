<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />

<table summary="">

  <tr>
    <td class="label">
      <span><bean:message key="protocollo.faldone.ufficiocorrente"/> : <html:hidden name="ricercaFaldoneForm" property="ufficioCorrenteId" /></span>
    </td>
    <td>
    <logic:notEmpty name="ricercaFaldoneForm" property="ufficioCorrentePath">
	    <bean:define id="ufficioCorrentePath" name="ricercaFaldoneForm" property="ufficioCorrentePath"/>
			<span title='<bean:write name="ricercaFaldoneForm" property="ufficioCorrentePath" />'> <strong>
				<bean:write name="ricercaFaldoneForm" property="ufficioCorrente.description" />
			</strong></span>
	</logic:notEmpty>	
      <html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
    </td>
  </tr>

<logic:notEmpty name="ricercaFaldoneForm" property="ufficiDipendenti">
  <tr>
    <td class="label">
      <label for="ufficioSelezionatoId"><bean:message key="protocollo.faldone.ufficio"/> :</label>
    </td>
    <td>
 	    <html:select name="ricercaFaldoneForm" property="ufficioSelezionatoId">
			<logic:iterate id="ufficio" name="ricercaFaldoneForm" property="ufficiDipendenti">
		        <option value='<bean:write name="ufficio" property="id"/>'>
		        <bean:write name="ufficio" property="description"/></option>
			</logic:iterate>
 	    </html:select>
      <html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
    </td>
  </tr>
</logic:notEmpty>

</table>
