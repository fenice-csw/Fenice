<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<html:hidden property="codice"/>
<html:hidden property="descrizione"/>

<logic:notEmpty name="listaDistribuzioneForm" property="codice">
<display:table class="simple" width="100%" requestURI="listaDistribuzione.do"
		name="sessionScope.listaDistribuzioneForm.listaPersoneLD" export="false"
		sort="list" pagesize="15" id="row">
		<display:column title="Denominazione">
			<html:link action="/page/protocollo/anagrafica/listaDistribuzione" 
					paramName="row" paramId="parIdPG" 
					paramProperty="id" >
					<bean:write name="row" property="descrizioneDitta"/>
			</html:link>
		</display:column>	
		<display:column property="partitaIva" title="Partita Iva" />
	</display:table>
	
</logic:notEmpty>
