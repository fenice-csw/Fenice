<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

	<display:table class="simple" width="100%"
		requestURI="/page/protocollo/anagrafica/aoo/cerca.do"
		name="requestScope.cercaAooForm.listaAmm" export="false"
		sort="list" pagesize="15" id="row">
		<display:column title="Identificativo PAC">
			<html:link action="/page/protocollo/anagrafica/aoo/cerca.do" paramId="parId" paramName="row" paramProperty="codice">
				<bean:write name="row" property="codice" />
			</html:link>
		</display:column>
		<display:column property="name" title="Provincia" />
		<display:column property="description" title="Nome Amministrazione" />
	</display:table>


	