<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html:xhtml />
<logic:notEmpty name="faldoneForm" property="procedimentiFaldoneCollection">
<div>
	<display:table class="simple" width="100%"
		requestURI="procedimento.do"
		name="sessionScope.faldoneForm.procedimentiFaldoneCollection" export="false"
		sort="list" pagesize="15" id="row">
		<display:column title="Numero Procedimento">
			<html:link action="/page/procedimento.do" paramName="row" paramId="visualizzaProcedimentoId" paramProperty="id">
				<bean:write name="row" property="numeroProcedimento" />
			</html:link>
		</display:column>	
		<display:column property="oggetto" title="Oggetto" />
		<display:column property="dataAvvioPro" title="Data Avvio" />
		<display:column property="descUfficio" title="Ufficio" />
		<display:column property="pathTitolario" titleKey="protocollo.titolario.argomento" />
		<display:column property="tipoProcedimentoDesc" title="Tipo" />
	</display:table>
</div>
</logic:notEmpty>