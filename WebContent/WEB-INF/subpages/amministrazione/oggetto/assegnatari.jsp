<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<html:xhtml />



<logic:notEmpty name="oggettiForm" property="assegnatari">
<table id="tabella_mittenti">
  <tr>
    <th />
    <th><span><bean:message key="protocollo.assegnatari.ufficio"/></span></th> 
  </tr>
	<logic:notEmpty name="oggettiForm" property="assegnatari">
	<logic:iterate id="ass" name="oggettiForm" property="assegnatari">
	  <tr>
	    <td>
	    	<html:multibox property="assegnatariSelezionatiId">
	    		<bean:write name="ass" property="key"/>
	    	</html:multibox>
	    </td>    
	    <td width="95%"><span title='<bean:write name="ass" property="descrizioneUfficio"/>'>
	    	<bean:write name="ass" property="nomeUfficio"/></span>
	    </td>
	  </tr>
	</logic:iterate>
	</logic:notEmpty>
</table>
<br />
<html:submit styleClass="button" property="rimuoviAssegnatariAction" value="Rimuovi" title="Rimuove gli assegnatari selezionati dall'elenco" />
</logic:notEmpty>


<br />
