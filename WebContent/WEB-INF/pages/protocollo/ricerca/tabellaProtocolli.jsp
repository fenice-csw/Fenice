<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<eprot:page title="Ricerca Protocolli">
<br />
	<div id="protocollo-errori"><html:errors
			bundle="bundleErroriProtocollo" /></div>
	<html:form action="/page/protocollo/ricerca.do">


		<logic:notEmpty name="ricercaForm"	property="protocolliCollection">
			<display:table class="simple" width="100%"
				requestURI="/page/protocollo/ricerca.do"
				name="sessionScope.ricercaForm.protocolliCollection" export="false"
				sort="list" pagesize="30" id="row">
				<logic:equal scope="session" name="elencoProtocolliDaProcedimento" value="true">
				<display:column title="">
				    <input type="checkbox" name="protocolliSelezionati" value="<bean:write name='row' property='protocolloId'/>" />
				</display:column>
				</logic:equal>
				<logic:equal scope="session" name="tornaFascicolo" value="true">
				<display:column title="">
				    <input type="checkbox" name="protocolliSelezionati" value="<bean:write name='row' property='protocolloId'/>" />
				</display:column>
				</logic:equal>

				<display:column title="N. Protocollo">
					<html:link action="/page/protocollo/ricerca.do" paramName="row" paramId="protocolloSelezionato" paramProperty="protocolloId">
						<bean:write name="row" property="annoNumeroProtocollo" />
					</html:link>
				</display:column>	
				<logic:equal name="row" property="tipoProtocollo" value="I">
					<display:column title="Tipo">
						Ingresso
					</display:column>
				</logic:equal>
				<logic:equal name="row" property="tipoProtocollo" value="U">
					<display:column title="Tipo">
						Uscita
					</display:column>
				</logic:equal>
				<logic:equal name="row" property="tipoProtocollo" value="P">
					<display:column title="Tipo">
						Posta Interna
					</display:column>
				</logic:equal> 
				 <!-- -->
				<display:column property="dataProtocollo" title="Registrato il" />
				
				<display:column title="Mittente/Destinatari">
					<logic:notEqual name="row" property="tipoMittente" value="M">
						<bean:write name="row" property="mittente" />
					</logic:notEqual>
					<logic:equal name="row" property="tipoMittente" value="M">
						<ul>
							<logic:iterate id="mittente" name="row" property="mittenti">
								<li><bean:write name="mittente" property="descrizione"/></li>
							</logic:iterate>
						</ul>
					</logic:equal>
				</display:column>
				
				<logic:equal name="ricercaForm" property="labelDestinatarioAssegnatario" value="Assegnatari">
					<display:column property="destinatario" title="Assegnatari" />
				</logic:equal>
				<logic:equal name="ricercaForm" property="labelDestinatarioAssegnatario" value="Destinatari">
					<display:column property="destinatario" title="Destinatari" />
				</logic:equal>
				<logic:equal name="ricercaForm" property="labelDestinatarioAssegnatario" value="Assegnatari/Destinatari">
					<display:column property="destinatario" title="Assegnatari/Destinatari" />
				</logic:equal>
				<display:column property="oggetto" title="Oggetto" />
				<display:column title="Doc">
					<logic:equal name="row" property="modificabile" value="true">
						<html:link action="/page/protocollo/ricerca.do" 
				             paramId="downloadDocprotocolloSelezionato" 
				             paramName="row" 
				             paramProperty="protocolloId" 
				             title="Download File">
							<span><bean:write name="row" property="pdf" /></span>
						</html:link>
					</logic:equal>
					<logic:notEqual name="row" property="modificabile" value="true">
						<span><bean:write name="row" property="pdf" /></span>
					</logic:notEqual>
				</display:column>	
				<display:column property="statoProtocollo" title="Stato" />
			</display:table>
			
			
			
		</logic:notEmpty>
	
		<logic:empty name="ricercaForm"	property="protocolliCollection">
			<br /><span><strong>Nessun protocollo per gli estremi di ricerca impostati.</strong></span><br /><br />
		</logic:empty>
	
		<div>
			<logic:equal scope="session" name="elencoProtocolliDaProcedimento" value="true">
				<html:submit styleClass="submit" property="btnSelezionaProtocolli" value="Seleziona" alt="Seleziona" />
			</logic:equal>
			<logic:equal scope="session" name="tornaFascicolo" value="true">
				<html:submit styleClass="submit" property="btnSelezionaProtocolli" value="Seleziona" alt="Seleziona" />
			</logic:equal>
			<html:submit styleClass="submit" property="btnAnnulla" value="Nuova ricerca" alt="Nuova ricerca" />
			<html:submit styleClass="submit" property="btnRipeti" value="Modifica ricerca" alt="Modifica ricerca" />
			<html:submit styleClass="submit" property="btnStampa" value="Stampa" alt="Stampa la ricerca" />			
		</div>
	</html:form>
</eprot:page>

