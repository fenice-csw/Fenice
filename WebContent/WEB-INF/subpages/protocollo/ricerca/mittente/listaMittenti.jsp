<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>


<logic:notEmpty name="ricercaForm" property="mittenti">
<display:table 	class="simple" 	width="100%" name="sessionScope.ricercaForm.mittenti" 
export="false" sort="list" 	pagesize="10" id="row">
	<display:column property="mittente" title="Mittente" href="/page/protocollo/ricerca.do"  paramId="parMittente"/>	
	<display:column property="tipo" title="Tipo" />
</display:table>   
</logic:notEmpty>    

<p>
<html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="Nuova ricerca"/></p>


