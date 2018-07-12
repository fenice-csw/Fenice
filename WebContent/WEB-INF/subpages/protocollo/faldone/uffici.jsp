<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />

<table summary="">

  <tr>
    <td>
      <span>Ufficio corrente : <html:hidden property="ufficioCorrenteId" /></span>
    </td>
    <td>
	    <logic:notEmpty name="faldoneForm" property="ufficioCorrentePath">
		    <bean:define id="ufficioCorrentePath" name="faldoneForm" property="ufficioCorrentePath"/>
				<span title='<bean:write name="faldoneForm" property="ufficioCorrentePath" />'> <strong>
					<bean:write name="faldoneForm" property="ufficioCorrente.description" />
				</strong></span>
		</logic:notEmpty>	
      	<html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
    </td>
  </tr>
<logic:equal name="faldoneForm" property="dipendenzaTitolarioUfficio" value="1">
<logic:notEmpty name="faldoneForm" property="ufficiDipendenti">
  <tr>
    <td class="label">
      <label for="ufficioSelezionatoId"> Ufficio<span class="obbligatorio"> * </span>: </label>
    </td>
    <td>
 	    <html:select property="ufficioSelezionatoId" styleId="ufficioSelezionatoId" styleClass="obbligatorio">
			<logic:iterate id="ufficio" name="faldoneForm" property="ufficiDipendenti">
		        <option value='<bean:write name="ufficio" property="id"/>'>
		        <bean:write name="ufficio" property="description"/></option>
			</logic:iterate>
 	    </html:select>
      <html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
    </td>
  </tr>
</logic:notEmpty>
</logic:equal>
<logic:equal name="faldoneForm" property="dipendenzaTitolarioUfficio" value="0">
<logic:notEmpty name="faldoneForm" property="ufficiDipendenti">
  <tr>
    <td class="label">
      <label for="ufficioSelezionatoId"> Ufficio<span >  </span>: </label>
    </td>
    <td>
 	    <html:select property="ufficioSelezionatoId" styleId="ufficioSelezionatoId" >
			<logic:iterate id="ufficio" name="faldoneForm" property="ufficiDipendenti">
		        <option value='<bean:write name="ufficio" property="id"/>'>
		        <bean:write name="ufficio" property="description"/></option>
			</logic:iterate>
 	    </html:select>
      <html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
    </td>
  </tr>
</logic:notEmpty>
</logic:equal>


</table>
