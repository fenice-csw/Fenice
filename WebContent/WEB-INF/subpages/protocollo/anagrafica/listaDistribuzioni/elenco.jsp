<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<logic:notEmpty name="listaDistribuzioneForm" property="elencoListaDistribuzione">
	<display:table class="simple" width="100%" 
		requestURI="/page/protocollo/anagrafica/listaDistribuzione.do"
		name="sessionScope.listaDistribuzioneForm.elencoListaDistribuzione" export="false"
		sort="list" pagesize="15" id="row">
		<display:column title="Descrizione">
			<html:link action="/page/protocollo/anagrafica/listaDistribuzione/nuovo.do" 
					paramName="row" paramId="parId" 
					paramProperty="id">
					<bean:write name="row" property="description"/>
			</html:link>
		</display:column>	
	</display:table>
</logic:notEmpty>
