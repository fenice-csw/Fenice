<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>


<html:xhtml />

<logic:notEmpty name="presaInCaricoForm"
	property="protocolliInCaricoCollection">
	<div><display:table class="simple" width="100%"
		name="sessionScope.presaInCaricoForm.protocolliInCaricoCollection"
		requestURI="/page/protocollo/dashboard/assegnati.do" export="false"
		sort="list" pagesize="30" id="row">
		<display:column title="">
			<logic:equal name="row" property="inProcedimento" value="false">			
				<input type="checkbox" name="protocolliSelezionati"
					value="<bean:write name='row' property='protocolloId'/>" />
			</logic:equal>
		</display:column>
		<display:column title="N. protocollo">
			<html:link action="/page/protocollo/presaInCarico.do" paramName="row"
				paramId="protocolloSelezionato" paramProperty="protocolloId">
				<bean:write name="row" property="annoNumeroProtocollo" />
			</html:link>
		</display:column>
		<display:column property="dataProtocollo" title="Registrato il" />
		<display:column title="Mittente">
			<logic:notEqual name="row" property="tipoMittente" value="M">
				<bean:write name="row" property="mittente" />
			</logic:notEqual>
			<logic:equal name="row" property="tipoMittente" value="M">
				<ul>
					<logic:iterate id="mittente" name="row" property="mittenti">
						<li><bean:write name="mittente" property="descrizione" /></li>
					</logic:iterate>
				</ul>
			</logic:equal>
		</display:column>
		<display:column title="Oggetto">
			<bean:write name="row" property="oggetto" />
		</display:column>
		<display:column title="DOC">
			<html:link action="/page/protocollo/presaInCarico.do"
				paramId="downloadDocprotocolloSelezionato" paramName="row"
				paramProperty="annoNumero" target="_blank" title="Download File">

				<bean:write name="row" property="pdf" />
			</html:link>
		</display:column>
		<display:column title="Messaggio" property="messaggio" />
		<display:column title="Azioni">
			<logic:equal name="row" property="competente" value="true">
				<logic:equal name="row" property="inProcedimento" value="true">
					<logic:equal name="row" property="titolareProcedimento" value="true">
					[<html:link action="/page/protocollo/presaInCarico.do" 
						paramId="assegnaProcedimento" paramProperty="protocolloId" paramName="row">Procedimento</html:link>]
					[<html:link action="/page/protocollo/presaInCarico.do"
					paramId="daFascicolare" paramProperty="protocolloId" paramName="row">Fascicola</html:link>]
		 		</logic:equal>
		 		<logic:notEqual name="row" property="titolareProcedimento" value="true">
					[<html:link action="/page/protocollo/presaInCarico.do" 
						paramId="lavoratoProcedimento" paramProperty="protocolloId" paramName="row">Lavorato</html:link>]
		 		</logic:notEqual>	
			</logic:equal>
			<logic:notEqual name="row" property="inProcedimento" value="true">
				[<html:link action="/page/protocollo/presaInCarico.do"
					paramId="daFascicolare" paramProperty="protocolloId" paramName="row">Fascicola</html:link>]
			</logic:notEqual>
		 		[<html:link action="/page/protocollo/presaInCarico.do"
					paramId="daRifiutare" paramProperty="protocolloId" paramName="row">Rifiuta</html:link>] 
				<eprot:ifAuthorized permission="reassign">
					[<html:link action="/page/protocollo/presaInCarico.do"
						paramId="daRiassegnare" paramProperty="protocolloId" paramName="row">Riassegna</html:link>]
				</eprot:ifAuthorized>
			</logic:equal>
			<logic:equal name="row" property="competente" value="false">
				<logic:equal name="row" property="inProcedimento" value="false">
					[<html:link  action="/page/protocollo/presaInCarico.do"
						paramId="daFascicolare" paramProperty="protocolloId" paramName="row">Presa Visione</html:link>]
				</logic:equal>
				<logic:equal name="row" property="inProcedimento" value="true">
					[<html:link action="/page/protocollo/presaInCarico.do" 
						paramId="lavoratoProcedimento" paramProperty="protocolloId" paramName="row">Presa Visione</html:link>]
				</logic:equal>
			</logic:equal>
		</display:column>

	</display:table>
	
	<p>
	
	<eprot:ifAuthorized permission="reassign">
	<html:submit styleClass="submit" property="btnRiassegna"
		value="Riassegna i Selezionati" alt="Riassegna i protocolli" /> 
	</eprot:ifAuthorized>
	<html:submit
		styleClass="submit" property="btnFascicolaSelezionati"
		value="Fascicola i Selezionati" alt="Fascicola i protocolli" />
		
		</p>
	</div>
</logic:notEmpty>
