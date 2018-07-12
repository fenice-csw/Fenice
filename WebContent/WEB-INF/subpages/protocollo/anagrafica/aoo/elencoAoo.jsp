<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<%--	elencoAoo<%=codice%>--%>
	<display:table class="simple" width="100%"
		requestURI="/page/protocollo/anagrafica/aoo/cerca.do"
		name="requestScope.cercaAooForm.listaAoo" export="false"
		sort="list" pagesize="15" id="row">
		<display:column title="Area Organizzativa (AOO)">
			<html:link action="/page/protocollo/anagrafica/aoo/cerca.do" 
					paramName="row" paramId="parId" 
					paramProperty="codice">
					<bean:write name="row" property="intestazione"/>
			</html:link>	
		</display:column>	
		<display:column property="destinatario" title="Responsabile" />
	</display:table>
	
	
