<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<logic:notEmpty name="listaDistribuzioneForm" property="codice">
<display:table class="simple" width="100%" requestURI="listaDistribuzione.do"
		name="sessionScope.listaDistribuzioneForm.listaPersoneLD" export="false"
		sort="list" pagesize="15" id="row">
		<display:column title="Cognome">
			<html:link action="/page/protocollo/anagrafica/listaDistribuzione" 
					paramName="row" paramId="parIdPF" 
					paramProperty="id" >
					<bean:write name="row" property="cognome"/>
			</html:link>
		</display:column>	
		<display:column property="nome" title="Nome" />
	</display:table>
</logic:notEmpty>

