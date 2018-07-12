<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />
  <tr>
    <td class="label">
     <span ><bean:message key="fascicolo.ufficio" /> :  <html:hidden property="ufficioCorrenteId" /></span>  
    </td>
    <td>
      <bean:define id="ufficioCorrentePath" name="ricercaFascicoliForm" property="ufficioCorrentePath" />
      <bean:define id="ufficioCorrenteDes" name="ricercaFascicoliForm" property="ufficioCorrente.description" /> 
      <span title="<bean:write name='ricercaFascicoliForm' property='ufficioCorrentePath'/>" ><strong> <bean:write name='ricercaFascicoliForm' property='ufficioCorrente.nomeUfficio'/></strong></span>
      <html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
    </td>
  </tr>


<logic:notEmpty name="ricercaFascicoliForm" property="ufficiDipendenti">
  <tr>
    <td class="label">
&nbsp;
    </td>
    <td>
 	    <html:select property="ufficioSelezionatoId" >		
		<logic:iterate id="ufficioD" name="ricercaFascicoliForm" property="ufficiDipendenti">
				<bean:define id="idfa" name="ufficioD" property="id" />
		        <option value="<bean:write name="ufficioD" property="id" />"><bean:write name="ufficioD" property="nomeUfficio"/></option>
		</logic:iterate>
 	    </html:select>
		<html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
    </td>
    
  </tr>
</logic:notEmpty>  




