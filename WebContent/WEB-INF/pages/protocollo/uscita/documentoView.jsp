<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />
<eprot:page title="Protocollo in uscita">

	<bean:define id="baseUrl" value="/page/protocollo/uscita/documento.do"
		scope="request" />

	<logic:equal parameter="protocolloRegistrato" value="true">
		<div id="protocollo-messaggi">
		<ul>
			<li>
		<logic:greaterThan
			name="protocolloForm" property="versione" value="1">
			<bean:message key="protocollo_modificato"
				bundle="bundleErroriProtocollo" />
		</logic:greaterThan> <logic:lessEqual name="protocolloForm" property="versione" value="1">
			<bean:message key="protocollo_registrato"
				bundle="bundleErroriProtocollo" />
			<strong> <bean:write name="protocolloForm"
				property="numeroProtocollo" /> </strong>
		</logic:lessEqual></li>
		</ul>
		</div>
	</logic:equal>

	<html:form action="/page/protocollo/uscita/documentoview.do">

		<div id="protocollo">

		<div id="protocollo-errori"><jsp:include
			page="/WEB-INF/subpages/protocollo/common/errori.jsp" /></div>

		<br class="hidden" />

		<logic:equal name="protocolloForm" property="flagTipo" value="R">
			<jsp:include
				page="/WEB-INF/subpages/protocollo/registroEmergenza/datiProtocolloEmergenzaUscita.jsp" />
		</logic:equal> <logic:notEqual name="protocolloForm" property="flagTipo" value="R">
			<jsp:include
				page="/WEB-INF/subpages/protocollo/common/datiProtocollo.jsp" />
		</logic:notEqual> <br class="hidden" />

		<jsp:include
			page="/WEB-INF/subpages/protocollo/common/datiDocumentoView.jsp" />

		<br class="hidden" />

		
		 <logic:notEmpty name="protocolloForm"
			property="provvedimentoAnnullamento">
			<jsp:include
				page="/WEB-INF/subpages/protocollo/common/datiAnnullamentoView.jsp" />
			<br class="hidden" />
		</logic:notEmpty> 
		
		<logic:equal name="protocolloUscitaForm" property="documentoVisibile" value="true">
			<div class="sezione"><bean:define id="sezioneVisualizzata"
				name="protocolloUscitaForm" property="sezioneVisualizzata" /> <jsp:include
				page="/WEB-INF/subpages/protocollo/common/link-sezioni.jsp" /> <logic:equal
				name="sezioneVisualizzata" value="Mittente">
				<jsp:include
					page="/WEB-INF/subpages/protocollo/uscita/mittenteView.jsp" />
			</logic:equal> <logic:match name="sezioneVisualizzata" value="Allegati">
				<jsp:include
					page="/WEB-INF/subpages/protocollo/common/allegatiView.jsp" />
			</logic:match> <logic:match name="sezioneVisualizzata" value="Collegati">
				<jsp:include
					page="/WEB-INF/subpages/protocollo/common/allacciView.jsp" />
			</logic:match> <logic:match name="sezioneVisualizzata"
				value="Destinatari">
				<jsp:include
					page="/WEB-INF/subpages/protocollo/uscita/destinatariView.jsp" />
			</logic:match> <logic:equal name="sezioneVisualizzata" value="Annotazioni">
				<jsp:include
					page="/WEB-INF/subpages/protocollo/common/annotazioniView.jsp" />
			</logic:equal> <logic:equal name="sezioneVisualizzata" value="Titolario">
				<jsp:include
					page="/WEB-INF/subpages/protocollo/common/titolarioView.jsp" />
			</logic:equal> <logic:match name="sezioneVisualizzata" value="Fascicoli">
				<jsp:include
					page="/WEB-INF/subpages/protocollo/common/datiFascicoliView.jsp" />
			</logic:match> <logic:match name="sezioneVisualizzata" value="Procedimenti">
				<jsp:include
					page="/WEB-INF/subpages/protocollo/common/datiProcedimentiView.jsp" />
			</logic:match></div>
		</logic:equal>
		<div><logic:equal name="protocolloForm"
			property="versioneDefault" value="true">

			<logic:notEqual name="protocolloForm" property="protocolloId"
				value="0">

				<logic:notEqual name="protocolloForm" property="stato" value="C">
					<eprot:ifAuthorized permission="145">
						<logic:equal name="protocolloUscitaForm" property="documentoVisibile" value="true" >
							<html:submit styleClass="submit" property="btnModificaProtocollo"
								value="Modifica" alt="Modifica protocollo" />
						</logic:equal>
					
				</eprot:ifAuthorized>

					<eprot:ifAuthorized permission="146">
						<logic:equal name="protocolloUscitaForm" property="documentoVisibile" value="true">
							<html:submit styleClass="submit" property="btnAnnullaProtocollo"
							value="Annulla protocollo" alt="Annulla il protocollo" />
						</logic:equal>
					</eprot:ifAuthorized>
				
				</logic:notEqual>
				
				<logic:greaterThan name="protocolloForm" property="versione"
					value="0">
					<html:submit styleClass="submit" property="btnStoriaProtocollo"
						value="Storia" alt="Storia protocollo" />
				</logic:greaterThan>
				
				<html:submit styleClass="submit" property="btnNuovoProtocollo"
					value="Nuovo" alt="Nuovo protocollo" />
				
				<logic:equal name="protocolloUscitaForm" property="documentoVisibile" value="true">
					<logic:equal name="protocolloForm" property="flagTipo" value="U">
						<html:submit styleClass="submit" property="btnRipetiDatiU"
							value="Ripeti dati"
							alt="Genera nuovo protocollo in uscita dai dati correnti" />
					</logic:equal>
				</logic:equal>
				
				<logic:notEqual name="protocolloForm" property="stato" value="C">
					<logic:equal name="protocolloUscitaForm" property="documentoVisibile" value="true">
						<html:submit styleClass="submit" property="btnStampaEtichette"
							value="Etichette destinatari" alt="Stampa etichette destinatari" />
						<html:submit styleClass="submit"
							property="btnStampaEtichettaProtocollo"
							value="Etichetta protocollo" alt="Stampa etichetta protocollo" />
					</logic:equal>
				</logic:notEqual>	
			</logic:notEqual>
		</logic:equal> 
		<logic:notEqual name="protocolloForm" property="versioneDefault"
			value="true">
			<html:submit styleClass="submit" property="btnStoriaProtocollo"
				value="Storia" alt="Storia protocollo" />
		</logic:notEqual>
		
		<logic:equal name="protocolloForm" property="inoltrabile" value="true">
			<html:submit styleClass="submit" property="btnInviaInterna"
					value="Inoltra come Posta Interna" alt="Inoltra PI" />
		</logic:equal>
		
		<logic:equal name="protocolloForm" property="daRicerca" value="true">
			<html:submit styleClass="submit" property="btnIndietro"
					value="Indietro" alt="Indietro" />
		</logic:equal>
		
		</div>

		</div>

	</html:form>

</eprot:page>