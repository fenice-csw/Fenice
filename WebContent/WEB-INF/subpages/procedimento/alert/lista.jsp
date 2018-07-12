<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<html:xhtml />
<p></p>
<br/>
<div>
	<logic:notEmpty name="alertProcedimentoForm" property="procedimentiCollection">
		<display:table class="simple" width="95%" requestURI="/page/protocollo/dashboard/alertProcedimento.do"
			name="sessionScope.alertProcedimentoForm.procedimentiCollection"
			export="false" sort="list" pagesize="30" id="row">
			<display:column title="Numero Procedimento">
				<html:link action="/page/procedimento/alert.do" paramName="row" paramId="visualizzaProcedimentoId" paramProperty="procedimentoId" >
					<bean:write name="row" property="numeroProcedimento" />
				</html:link>
			</display:column>
			<display:column property="dataAvvio" title="Data Apertura" />
			<display:column property="responsabile" title="Responsabile" />
			<display:column property="oggetto" title="Oggetto" />
			<display:column property="giorniRimanenti" title="Giorni Mancanti" />
		</display:table>
	</logic:notEmpty>
</div>
