<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<html:xhtml />
<logic:notEmpty name="reportStatisticheForm" property="dettaglioStatistiche">
<div>
	<span>
		<strong><bean:message key="report.prnpro.statistiche.dettaglio"/></strong><br />
		<bean:write name="reportStatisticheForm" property="titoloDettaglioStatistiche"/><br /><br />
		<br /><br />
	</span>
	<display:table class="simple" width="100%"
		name="requestScope.reportStatisticheForm.dettaglioStatistiche"
		requestURI="/page/stampa/prn-pro/statistiche.do" export="false"
		sort="list" pagesize="20" id="row">
		<display:column title="Numero/Anno">			
			<html:link action="/page/stampa/prn-pro/statistiche.do" paramId="protocolloId" paramName="row" paramProperty="protocolloId">
				<bean:write name="row" property="annoNumeroProtocollo" />
			</html:link>
		</display:column>
		<display:column title="Tipo documento">
				<bean:write name="row" property="tipoDocumento" />
		</display:column>
		<display:column title="Data registrazione">
				<bean:write name="row" property="dataProtocollo" />
		</display:column>
		<display:column title="Oggetto">
				<bean:write name="row" property="oggetto" />
		</display:column>
	</display:table>
</div>
</logic:notEmpty>
