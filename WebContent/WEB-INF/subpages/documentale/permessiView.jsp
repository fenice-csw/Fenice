<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />
<logic:notEmpty name="documentoForm" property="permessi" >
<table id="assegnatari" width="100%">
  <tr>
    <th><span><bean:message key="documentale.tipopermessi"/></span></th>
    <th><span><bean:message key="documentale.ufficio"/></span></th>
    <th><span><bean:message key="documentale.utente"/></span></th>
    <th><span><bean:message key="documentale.messaggio"/></span></th>    
  </tr>
<logic:notEmpty name="documentoForm" property="permessi">
<logic:iterate id="per" name="documentoForm" property="permessi">
  <tr>
    <td><span>
		<bean:write name="per" property="descrizioneTipoPermesso" />
    	</span></td>
    <td>
    	<logic:notEmpty name="per" property="nomeUfficio">
    		<span title="<bean:write name='per' property='nomeUfficio' />">
				<bean:write name="per" property="nomeUfficio" />
    		</span>
    	</logic:notEmpty>	
    </td>
    <td><span>
		<logic:notEmpty name="per" property="nomeUfficio">
			<bean:write name="per" property="nomeUtente" />
		</logic:notEmpty>	
    </span></td>
    <td><span>
		<logic:notEmpty name="per" property="msgPermesso">
			<bean:write name="per" property="msgPermesso" />
		</logic:notEmpty>	
    </span></td>

  </tr>
</logic:iterate>  
</logic:notEmpty>
</table>
<br />
</logic:notEmpty>
