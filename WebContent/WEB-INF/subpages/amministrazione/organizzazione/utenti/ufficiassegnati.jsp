<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />

<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/utenti/uffici.jsp" />


<logic:notEmpty name="profiloUtenteForm" property="uffici">
<logic:equal name="profiloUtenteForm" property="id" value="0">
<br />
<table id="uffici">
  <tr>
    <th><span><bean:message key="amministrazione.organizzazione.uffici.ufficioassegnato"/></span></th>
  </tr>
<logic:iterate id="ass" name="profiloUtenteForm" property="uffici">
  <tr>
    <td>
		<html:multibox property="ufficiSelezionatiId" >
			<bean:write name="ass" property="ufficioId"/>
		</html:multibox>
		<bean:define id="descrizioneUfficio" name="ass" property="descrizioneUfficio"/>
		&nbsp;<span title="">
			<bean:write name="ass" property="descrizioneUfficio"/>
		</span>
	</td>
  </tr>

</logic:iterate>
</table>
<br />
<html:submit styleClass="button" property="rimuoviAssegnatariAction" value="Rimuovi" title="Rimuove gli assegnatari selezionati dall'elenco" />
</logic:equal>
</logic:notEmpty>