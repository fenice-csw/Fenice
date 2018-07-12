<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />
<table summary="">
  <tr>
    <td class="label">
      <span><bean:message key="report.prnpro.ufficiocorrente"/>: <html:hidden property="ufficioCorrenteId" /></span>
    </td>
    <td>
		<span title="<bean:write name="reportForm" property="ufficioCorrentePath"/>">
      	<strong>
			<bean:write name="reportForm" property="ufficioCorrente.nomeUfficio" />
      	</strong>
      	</span>
      <html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
    </td>
  </tr>


<logic:notEmpty name="reportForm" property="ufficiDipendenti">
  <tr>
    <td class="label">
      <label for="ufficioSelezionatoId"><bean:message key="report.prnpro.ufficio"/>:</label>
    </td>
    <td>
 	    <html:select property="ufficioSelezionatoId">
 	    
			<logic:iterate id="ufficio" name="reportForm" property="ufficiDipendenti">
				<bean:define id="id" name="ufficio" property="id" />
				<option value="<bean:write name="ufficio" property="id" />"><bean:write name="ufficio" property="nomeUfficio"/></option>
			</logic:iterate>
 	    </html:select>
      <html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
    </td>
  </tr>
</logic:notEmpty>
</table>