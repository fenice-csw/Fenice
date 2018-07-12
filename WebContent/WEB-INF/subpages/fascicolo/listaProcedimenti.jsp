<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Procedimenti da associare al Fascicolo">

	<div id="protocollo-errori">
		<html:errors bundle="bundleErroriProtocollo" property="procedimentoSelezionato" />
	</div>

	<html:form action="/page/fascicolo.do">

		<div>
		<br />
		<logic:notEmpty name="fascicoloForm" property="procedimenti">
			<display:table class="simple" width="100%"
				requestURI="/page/procedimento/ricerca.do"
				name="sessionScope.fascicoloForm.procedimenti" export="false"
				sort="list" pagesize="15" id="row">
				<display:column title="">
					<input type="checkbox" name="procedimentiSelezionati" value="<bean:write name='row' property='id'/>" />
				</display:column>

				<display:column title="Numero Procedimento">
					<html:link action="/page/procedimento.do" paramName="row" paramId="visualizzaProcedimentoId" paramProperty="id">
						<bean:write name="row" property="numeroProcedimento" />
					</html:link>
				</display:column>
				<display:column property="oggetto" title="Oggetto" />
				<display:column property="dataAvvio" title="Data Avvio" />
				<display:column property="descUfficio" title="Ufficio" />
				<display:column property="pathTitolario" titleKey="protocollo.titolario.argomento" />
				<display:column property="tipoProcedimentoDesc" title="Tipo" />	
			</display:table>
		</logic:notEmpty>
	
		<logic:empty name="fascicoloForm" property="procedimenti">
			<br /><span><strong>Nessun protocollo per gli estremi di ricerca impostati.</strong></span><br /><br />
		</logic:empty>
		<div>
			<html:submit styleClass="submit" property="btnSelezionaProcedimenti" value="Seleziona" alt="Seleziona" />
			<html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="Annulla" />
		</div>
	</div>
	</html:form>

</eprot:page>

