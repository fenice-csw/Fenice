<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<html:xhtml />
	<jsp:include page="/WEB-INF/subpages/procedimento/uffici.jsp" />
<br />


<logic:notEmpty name="procedimentoForm" property="istruttori">
<table id="tabella_mittenti">
  <tr>
    <th >
    </th>
    <th>
    	<span><bean:message key="procedimento.istruttori.utente"/></span>
    </th>
  </tr>
	<logic:notEmpty name="procedimentoForm" property="istruttori">
	<logic:iterate id="istr" name="procedimentoForm" property="istruttori">
	  <tr>
	    <td width="5%">
	    	<html:multibox property="istruttoriSelezionatiId">
	    		<bean:write name="istr" property="key"/>
	    	</html:multibox>
	    </td>    
	    <td width="95%">
	    	<span><bean:write name="istr" property="nomeUtente"/></span>
	    </td>
	  </tr>
	</logic:iterate>
	<br />	<br />
	</logic:notEmpty>

</table>
<br />
<html:submit styleClass="button" property="rimuoviIstruttoriAction" value="Rimuovi" title="Rimuove gli istruttori selezionati dall'elenco" />
</logic:notEmpty>


<br />
