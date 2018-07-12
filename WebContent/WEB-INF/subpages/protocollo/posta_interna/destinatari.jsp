<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<html:xhtml />

<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/uffici.jsp" />

<br />
&nbsp;&nbsp; <html:submit styleClass="button" property="listaDistribuzione" value="Inserisci da Lista Distribuzione" title="Seleziona la lista distribuzione" />&nbsp;&nbsp;
<br />
<logic:notEmpty name="protocolloForm" property="destinatari">
<table id="assegnatari">
  <tr>
    <th />
    <th><span><bean:message key="protocollo.destinatari.competente"/></span></th>
    <th><span><bean:message key="protocollo.destinatari.ufficio"/></span></th>
    <th><span><bean:message key="protocollo.destinatari.utente"/></span></th>
  </tr>
	<logic:notEmpty name="protocolloForm" property="destinatari">
	<logic:iterate id="ass" name="protocolloForm" property="destinatari">
	  
	  <tr>
	    <td>
	    	<html:multibox property="destinatariSelezionatiId">
	    		<bean:write name="ass" property="key"/>
	    	</html:multibox>
	    </td>    
	    <td>
	    <html:multibox property="destinatariCompetenti">
	    		<bean:write name="ass" property="key"/>
	    </html:multibox>
	    </td>
	    <td width="50%"><span title='<bean:write name="ass" property="descrizioneUfficio"/>'>
	    	<bean:write name="ass" property="nomeUfficio"/></span>
	    </td>
	    <td width="50%"><span><bean:write name="ass" property="nomeUtente"/></span></td>
	  </tr>
	  
	</logic:iterate>
	<br />
	<label for="messaggio"><bean:message key="protocollo.destinatari.messaggio"/>:</label>
	<html:text styleId="messaggio" property="msgDestinatarioCompetente" size="60" maxlength="250"></html:text>
	<br />	<br />
	</logic:notEmpty>

</table>
<br />
<html:submit styleClass="button" property="rimuoviAssegnatariAction" value="Rimuovi" title="Rimuove gli assegnatari selezionati dall'elenco" />
</logic:notEmpty>

<br />