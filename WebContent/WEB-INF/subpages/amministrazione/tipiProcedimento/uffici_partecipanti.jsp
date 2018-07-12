<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<html:xhtml />


<jsp:include page="/WEB-INF/subpages/amministrazione/tipiProcedimento/uffici.jsp" />

<br />

<logic:notEmpty name="tipoProcedimentoForm" property="ufficiPartecipanti">
	<table id="tabella_mittenti">
	  <tr>
	    <th />
	    <th><span><bean:message key="protocollo.assegnatari.ufficio"/></span></th>
	    <th><span>Principale</span></th>
	  </tr>
		<logic:notEmpty name="tipoProcedimentoForm" property="ufficiPartecipanti">
			<logic:iterate id="ass" name="tipoProcedimentoForm" property="ufficiPartecipanti">
			  <tr>
			    <td>
			    	<html:multibox property="ufficiPartecipantiId">
			    		<bean:write name="ass" property="ufficioId"/>
			    	</html:multibox>
			    </td>    
			    <td width="85%">
			    	<span><bean:write name="ass" property="nomeUfficio"/></span>
			    </td>
			    <td>
			    	<html:radio property="ufficioPrincipaleId" idName="ass" value="ufficioId"></html:radio>
			    </td>
			  </tr> 
			</logic:iterate>
		</logic:notEmpty>
	</table>
	<html:submit styleClass="button" property="rimuoviUfficiPartecipantiAction" value="Rimuovi" title="Rimuove gli uffici selezionati dall'elenco" />
</logic:notEmpty>
<br />
