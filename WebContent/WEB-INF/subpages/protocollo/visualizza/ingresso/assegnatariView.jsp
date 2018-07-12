<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:xhtml />


<logic:notEmpty name="visualizzaProtocolloForm" property="assegnatari">
	<table id="tabella_mittenti">
	  <tr>
	    <th><span><bean:message key="protocollo.assegnatari.ufficio"/></span></th>
	    <th><span><bean:message key="protocollo.assegnatari.utente"/></span></th>
	    <th><span><bean:message key="protocollo.assegnatari.competente"/></span></th>
	  </tr>
		<logic:iterate id="ass" name="visualizzaProtocolloForm" property="assegnatari">
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
					<span><bean:message key="protocollo.assegnatari.si"/></span>
				</logic:equal>
				<logic:notEqual name="ass" property="competente" value="true">
					<span><bean:message key="protocollo.assegnatari.no"/></span>
				</logic:notEqual>
			</td>
			
		  </tr>
		</logic:iterate>  
	</table>
</logic:notEmpty>

