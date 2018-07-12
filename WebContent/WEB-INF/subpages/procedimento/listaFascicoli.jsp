<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />

<logic:notEmpty name="procedimentoForm" property="fascicoliCollection">
<div>
<display:table class="simple" width="95%" requestURI="/page/procedimento.do"
		name="sessionScope.procedimentoForm.fascicoliCollection"
		export="false" sort="list" pagesize="10" id="row">
		<display:column title="Numero Fascicolo">
			<html:link action="/page/procedimento.do" paramName="row" paramId="visualizzaFascicoloId" paramProperty="id" >
				<bean:write name="row" property="annoProgressivo"/>
			</html:link>
		</display:column>
		<display:column property="oggetto" title="Oggetto" />
		<display:column property="dataApertura" title="Data Apertura" />
		<display:column property="pathTitolario" titleKey="protocollo.titolario.argomento" />
	</display:table>
	
	<br/>
</div>
</logic:notEmpty>