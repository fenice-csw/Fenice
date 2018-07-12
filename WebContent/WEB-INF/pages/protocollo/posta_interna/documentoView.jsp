<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Posta interna">

<bean:define id="baseUrl" value="/page/protocollo/posta_interna/documento.do" scope="request" />

<logic:equal parameter="protocolloRegistrato" value="true">
	<div id="protocollo-messaggi">
		<ul>
			<li>
		
		<logic:greaterThan name="protocolloForm" property="versione" value="1">
			<logic:equal name="protocolloForm" property="stato" value="A">
				<bean:message key="posta_fascicolata" bundle="bundleErroriProtocollo" />
			</logic:equal>
			<logic:notEqual name="protocolloForm" property="stato" value="A">
				<bean:message key="posta_modificata" bundle="bundleErroriProtocollo" />
			</logic:notEqual>
		</logic:greaterThan> 
		
		<logic:lessEqual name="protocolloForm" property="versione" value="1">
			<bean:message key="posta_registrata"
				bundle="bundleErroriProtocollo" />
			<strong> <bean:write name="protocolloForm" property="numeroProtocollo" /> </strong>
		</logic:lessEqual>
	
	</li>
		</ul>
		</div>
	</logic:equal>

<html:form action="/page/protocollo/posta_interna/documentoview.do">

<div id="protocollo">


    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />


<br class="hidden" />

<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/datiProtocollo.jsp" />

<br class="hidden" />

<jsp:include page="/WEB-INF/subpages/protocollo/common/datiDocumentoView.jsp" />

<br class="hidden" />

<logic:notEmpty name="protocolloForm" property="provvedimentoAnnullamento" >
	<jsp:include page="/WEB-INF/subpages/protocollo/common/datiAnnullamentoView.jsp" />
	<br class="hidden" />
</logic:notEmpty>	



<logic:equal name="postaInternaForm" property="documentoVisibile" value="true" >
<div class="sezione">
<bean:define id="sezioneVisualizzata" name="postaInternaForm" property="sezioneVisualizzata" />
<jsp:include page="/WEB-INF/subpages/protocollo/common/link-sezioni.jsp" />
	
	<logic:equal name="sezioneVisualizzata" value="Mittente">
		<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/mittenteView.jsp" />
	</logic:equal>
	<logic:match name="sezioneVisualizzata" value="Allegati">
    	<jsp:include page="/WEB-INF/subpages/protocollo/common/allegatiView.jsp" />
	</logic:match>
	<logic:match name="sezioneVisualizzata" value="Collegati">
    	<jsp:include page="/WEB-INF/subpages/protocollo/common/allacciView.jsp" />
	</logic:match>
	<logic:match name="sezioneVisualizzata" value="Destinatari">
		<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/destinatariView.jsp" />
	</logic:match>
	<logic:equal name="sezioneVisualizzata" value="Annotazioni">
		<jsp:include page="/WEB-INF/subpages/protocollo/common/annotazioniView.jsp" />
	</logic:equal>
	<logic:equal name="sezioneVisualizzata" value="Titolario">
		<jsp:include page="/WEB-INF/subpages/protocollo/common/titolarioView.jsp" />
	</logic:equal>
	<logic:match name="sezioneVisualizzata" value="Fascicoli">
	    <jsp:include page="/WEB-INF/subpages/protocollo/common/datiFascicoliView.jsp" />
	</logic:match>
	<logic:match name="sezioneVisualizzata" value="Procedimenti">
	    <jsp:include page="/WEB-INF/subpages/protocollo/common/datiProcedimentiView.jsp" />
	</logic:match>
</div>
</logic:equal>
<div>

<logic:equal name="protocolloForm" property="versioneDefault" value="true">

	<logic:notEqual name="protocolloForm" property="protocolloId" value="0">

	
		<logic:equal name="postaInternaForm" property="documentoVisibile" value="true">
			<eprot:ifAuthorized permission="modify_pi">
				<html:submit styleClass="submit" property="btnModificaProtocollo" value="Modifica" alt="Modifica protocollo" />
				<html:submit styleClass="submit" property="btnAnnullaProtocollo" value="Annulla protocollo" alt="Annulla il protocollo" />
			</eprot:ifAuthorized>
		</logic:equal>
		
		<logic:greaterThan name="protocolloForm" property="versione" value="0">
			<html:submit styleClass="submit" property="btnStoriaProtocollo" value="Storia" alt="Storia protocollo" />
		</logic:greaterThan>	
		<html:submit styleClass="submit" property="btnNuovoProtocollo" value="Nuovo" alt="Nuovo protocollo" />
		<logic:equal name="postaInternaForm" property="documentoVisibile" value="true">
	  		<logic:equal name="protocolloForm" property="flagTipo" value="I">
				<html:submit styleClass="submit" property="btnRipetiDatiI" value="Ripeti dati" alt="Genera nuovo protocollo in ingresso dai dati correnti" />
			</logic:equal>
			<html:submit styleClass="submit" property="btnStampaEtichettaProtocollo" value="Etichetta protocollo" alt="Stampa etichetta protocollo" />
		</logic:equal>
		
	</logic:notEqual>
</logic:equal>

<logic:notEqual name="protocolloForm" property="versioneDefault" value="true">
	<html:submit styleClass="submit" property="btnStoriaProtocollo" value="Storia" alt="Storia protocollo" />
</logic:notEqual>

<logic:equal name="protocolloForm" property="daRicerca" value="true">
	<html:submit styleClass="submit" property="btnIndietro" value="Indietro" alt="Indietro" />
</logic:equal>

</div>

</div>

</html:form>
</eprot:page>