<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Lista Sezioni Amministrazione Trasparente">

	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" /></div>

	<div class="sezione"><span class="title"> <strong>Sezioni in Gestione</strong>
	</span> <logic:notEmpty name="sezioneForm" property="sezioni">
		<display:table class="simple" width="100%"
			name="sessionScope.sezioneForm.sezioni" export="false"
			sort="page" pagesize="15" id="sezione"
			requestURI="/page/amm_trasparentes.do?responsabileAction=true">
			<display:column title="ID">
				<html:link action="/page/amm_trasparente.do" paramName="sezione"
					paramId="sezioneId" paramProperty="sezioneId">
					<bean:write name="sezione" property="sezioneId" />
				</html:link>
			</display:column>
			<display:column property="descrizione" title="Descrizione" />
		</display:table>
	</logic:notEmpty> 
	<logic:empty name="sezioneForm" property="sezioni">
			Non hai in gestione nessuna sezione
		</logic:empty></div>
</eprot:page>