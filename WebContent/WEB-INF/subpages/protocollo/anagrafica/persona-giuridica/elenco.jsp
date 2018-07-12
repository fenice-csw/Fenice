<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<logic:notEmpty name="personaGiuridicaForm" property="listaPersone">
	<display:table class="simple" width="100%" requestURI="/page/protocollo/anagrafica/persona-giuridica/cerca.do"
		name="requestScope.personaGiuridicaForm.listaPersone" export="false"
		sort="list" pagesize="15" id="row">
		<display:column title="Denominazione">
			<html:link action="/page/protocollo/anagrafica/persona-giuridica/cerca.do" 
					paramName="row" paramId="parId" 
					paramProperty="id">
					<bean:write name="row" property="descrizioneDitta"/>
			</html:link>
		</display:column>
		<display:column property="indirizzoNumCivico" title="Indirizzo"/>	
		<display:column property="indirizzo.comune" title="Comune" />
		<display:column property="indirizzoEMail" title="Email" />
	</display:table>
	
	
</logic:notEmpty>

