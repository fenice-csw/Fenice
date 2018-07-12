<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<html:xhtml />


<jsp:include page="/WEB-INF/subpages/protocollo/common/uffici.jsp" />

<br />


<logic:notEmpty name="protocolloForm" property="assegnatari">
<table id="tabella_mittenti">
  <tr>
    <th />
    <th><span><bean:message key="protocollo.assegnatari.competente"/></span></th>
    <th><span><bean:message key="protocollo.assegnatari.ufficio"/></span></th>
    <th><span><bean:message key="protocollo.assegnatari.utente"/></span></th>
    <th><span><bean:message key="protocollo.assegnatari.titolare_procedimento"/>*</span></th>
  </tr>
	<logic:notEmpty name="protocolloForm" property="assegnatari">
	<logic:iterate id="ass" name="protocolloForm" property="assegnatari">
	  <tr>
	    <td>
	    	<html:multibox property="assegnatariSelezionatiId">
	    		<bean:write name="ass" property="key"/>
	    	</html:multibox>
	    </td>    
	    <td>
	    <html:multibox property="assegnatariCompetenti" >
	    		<bean:write name="ass" property="key"/>
	    </html:multibox>
	    </td>
	    <td width="40%"><span title='<bean:write name="ass" property="descrizioneUfficio"/>'>
	    	<bean:write name="ass" property="nomeUfficio"/></span>
	    </td>
	    <td width="50%"><span><bean:write name="ass" property="nomeUtente"/></span></td>
	  	
	  	<td>		
    		<html:radio property="titolareProcedimento" idName="ass" value="key" ondblclick="toggle(this);"></html:radio>
    	</td>
	  </tr> 
	</logic:iterate>
	<tr hidden="true">
		<td>		
    		<input type="radio" name="titolareProcedimento" value="0" id="no_value"/>
    	</td>
	</tr>
	<label for="messaggio"><bean:message key="protocollo.assegnatari.messaggio"/>:</label>
	<html:text styleId="messaggio" property="msgAssegnatarioCompetente" size="60" maxlength="250"></html:text>
	<br />	<br />
	</logic:notEmpty>
</table>
<span id="nota">* Doppio click per deselezionare</span>
<br />
<html:submit styleClass="button" property="rimuoviAssegnatariAction" value="Rimuovi" title="Rimuove gli assegnatari selezionati dall'elenco" />
</logic:notEmpty>

<script language="javascript" type="text/javascript">
function toggle(radioBtn)
{   
  if(radioBtn.checked)
  {
	  radioBtn.checked=false;
	  document.getElementById("no_value").checked=true;
	  
  }
}
</script>

<br />
