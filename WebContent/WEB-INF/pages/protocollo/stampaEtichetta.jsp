<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />
<head>
<html:base />
</head>

<eprot:page title="Stampa etichetta">
	
		<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />

	<br class="hidden" />

	<jsp:include page="/WEB-INF/subpages/protocollo/common/datiProtocollo.jsp" />

	<br class="hidden" />

	<html:form action="/page/protocollo/stampaEtichetta.do">
		<table summary="" width="100%">
		<tr>
		<td>
		<div class="sezione">
			<span class="title"> <strong><bean:message key="protocollo.stampa.etichette.parametri" /></strong> </span> <br />			
			<table summary="">
				<tr>
					<td class="label">
						<label title="Margine sinistro" for="margineSinistro"> 
								<bean:message key="protocollo.stampa.etichette.label.margine.sx" /> 
						</label>&nbsp;:
					</td>
					<td>
						<html:text styleClass="text" property="margineSinistro" styleId="margineSinistro" size="3" maxlength="5" />&nbsp;
					</td>
				</tr>
				<tr>
					<td class="label">
						<label title="Margine superiore" for="margineSuperiore"> 
								<bean:message key="protocollo.stampa.etichette.label.margine.superiore" /> 
						</label>&nbsp;:
					</td>
					<td>
						<html:text styleClass="text" property="margineSuperiore"styleId="margineSuperiore" size="3" maxlength="5" />&nbsp;
					</td>
				</tr>
				<tr>
					<td class="label">
						<label title="Larghezza" for="larghezzaEtichetta"> 
								<bean:message key="protocollo.stampa.etichette.label.larghezza" /> 
						</label>&nbsp;:
					</td>
					<td>
						<html:text styleClass="text" property="larghezzaEtichetta" styleId="larghezzaEtichetta" size="3" maxlength="5" />&nbsp;
					</td>
				</tr>
				<tr>
					<td class="label">
						<label title="Altezza etichetta" for="altezzaEtichetta"> 
							<bean:message key="protocollo.stampa.etichette.label.altezza" /> 
						</label>&nbsp;:
					</td>
					<td>
						<!--  html:hidden property="protocolloId" /--> 
						<html:text styleClass="text" property="altezzaEtichetta" styleId="altezzaEtichetta" size="3" maxlength="5" />
					</td>
				</tr>
				<tr>
					<td class="label">
						<label title="Rotazione etichetta" for="rotazione"> 
							<bean:message key="protocollo.stampa.etichette.label.rotazione" /> 
						</label>&nbsp;:
					</td>
					<td>
						<html:text styleClass="text" property="rotazione" styleId="rotazione" size="3" maxlength="5" />
					</td>
				</tr>
			</table>
<br />
	        <html:radio property="tipoStampa" value="S" styleId="A4">
					<label title="Stampa su foglio A4" for="A4">
						<bean:message key="protocollo.stampa.etichette.su.foglioA4" /> 
					</label>
	        </html:radio>&nbsp;&nbsp;
			<html:select styleClass="obbligatorio" disabled="false" property="modoStampaA4">
				<html:optionsCollection property="modalitaStampaA4" value="codice" label="description" />			
			</html:select> 
			<br />
	        <html:radio property="tipoStampa" value="N" styleId="NoA4">
					<label title="Stampa su stampantina" for="NoA4">
						<bean:message key="protocollo.stampa.etichette.su.stampante" />
					</label>
	        </html:radio>&nbsp;
            <noscript>
              <div>
            	<html:submit property="btnImpostaParametriStampa" value="Imposta" styleClass="button" title="Imposta il tipo di stampa" />
              </div>
            </noscript>

		<br />		
		<br />
			<html:submit styleClass="submit" property="btnImpostaParametriStampa" value="Reimposta" title="Imposta i margini di stampa" />            
			<html:submit styleClass="submit" property="btnSalvaConfigurazione" value="Salva configurazione" title="Imposta i margini di stampa" />            
		</div>
		</td>
		</tr>	
		</table>
		<div class="sezione">
			<span class="title"><strong>Stampante</strong></span> 
			<applet name="barcode" align="top" height="100" 
					archive="barcode-print-applet.jar,barcode4j.jar"
					code="it.finsiel.siged.mvc.presentation.applet.BarcodePrintApplet.class"  
					codebase="../../../download/">
					<param name="MargineSinistro"
						value="<bean:write name="stampaEtichettaForm" property="margineSinistro"/>">
					<param name="MargineSuperiore"
						value="<bean:write name="stampaEtichettaForm" property="margineSuperiore"/>">
					<param name="LarghezzaEtichetta"
						value="<bean:write name="stampaEtichettaForm" property="larghezzaEtichetta"/>">
					<param name="AltezzaEtichetta"
						value="<bean:write name="stampaEtichettaForm" property="altezzaEtichetta"/>">
					<param name="stampaSuFoglioA4" 
						value="<bean:write name="stampaEtichettaForm" property="tipoStampa"/>">
					<param name="deltaXMM"
						value="<bean:write name="stampaEtichettaForm" property="deltaXMM"/>">
					<param name="deltaYMM"
						value="<bean:write name="stampaEtichettaForm" property="deltaYMM"/>">						
					<param name="rotazione"
						value="<bean:write name="stampaEtichettaForm" property="rotazione"/>">			
					<param name="Riga_1"
						value="<bean:write name='UTENTE_KEY' property='areaOrganizzativa.description' />">
					<logic:equal name="protocolloForm" property="flagTipo" value="U">
						<param name="Riga_3"
							value="Uscita del <bean:write name='protocolloForm' property='dataRegistrazioneToEtichetta'/>" />
					</logic:equal> 
					<logic:equal name="protocolloForm" property="flagTipo" value="I">
						<param name="Riga_3"
							value="Ingresso del <bean:write name='protocolloForm' property='dataRegistrazioneToEtichetta'/>" />
					</logic:equal>
					<logic:equal name="protocolloForm" property="flagTipo" value="P">
						<param name="Riga_3"
							value="Posta Interna del <bean:write name='protocolloForm' property='dataRegistrazioneToEtichetta'/>" />
					</logic:equal> 		
					<param name="Riga_4"
						value="<bean:write name='protocolloForm' property='numeroEtichetta'/>" />
					<param name="Riga_5"
						value="<bean:write name='protocolloForm' property='titolarioFascicolo'/>" />
					 Installare Java 1.4 o superiore. 
				</applet>		
			</div>					
			<html:submit styleClass="submit" property="btnAnnullaStampa" value="Torna al protocollo" title="Annulla l'operazione e ritorna la protocollo selezionato" />
			
	</html:form>
</eprot:page>
