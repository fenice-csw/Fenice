<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:xhtml />


<logic:notEmpty name="protocolloForm" property="destinatari">
	<table id="assegnatari">
	  <tr>
	    <th><span><bean:message key="protocollo.destinatari.ufficio"/></span></th>
	    <th><span><bean:message key="protocollo.destinatari.utente"/></span></th>
	    <th><span><bean:message key="protocollo.destinatari.competente"/></span></th>
	  </tr>
		<logic:iterate id="ass" name="protocolloForm" property="destinatari">
		  <tr>
		    <td width="50%">
				<span title='<bean:write name="ass" property="descrizioneUfficio"/>'>
					<bean:write name="ass" property="nomeUfficio" />
				</span>
		    </td>
		    <td width="50%">
		    	<span><bean:write name="ass" property="nomeUtente" /></span>
		    </td>
		    
		    	
		    
		    <td>
				<logic:equal name="ass" property="competente" value="true"> 
					<span><bean:message key="protocollo.destinatari.si"/></span>
				</logic:equal>
				<logic:notEqual name="ass" property="competente" value="true">
					<span><bean:message key="protocollo.destinatari.no"/></span>
				</logic:notEqual>
			</td>
			
		  </tr>
		</logic:iterate>  
	</table>
</logic:notEmpty>

