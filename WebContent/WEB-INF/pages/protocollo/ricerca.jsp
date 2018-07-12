<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />
<eprot:page title="Ricerca protocollo">


	<html:form action="/page/protocollo/ricerca.do">
		<bean:define id="mozioneUscita" name="ricercaForm"
			property="mozioneUscita" />

		<div id="protocollo-errori"><html:errors
			bundle="bundleErroriProtocollo" /></div>

	<div id="bottoni_salva">
		<html:submit styleClass="submit" property="btnCerca" value="Cerca"
			alt="Ricerca protocolli" />
		<html:reset styleClass="submit" property="ResetAction" value="Annulla"
			alt="Ripulisce il form" />
		<logic:equal name="ricercaForm" property="indietroVisibile"
			value="true">
			<html:submit styleClass="submit" property="indietro" value="Indietro"
				alt="Indietro" />
		</logic:equal>
	</div>
	
		<div class="sezione"><span class="title"><strong><bean:message
			key="protocollo.ricerca.datiGenerali" /></strong></span> <jsp:include
			page="/WEB-INF/subpages/protocollo/ricerca/datiProtocollo.jsp" />
		</div>

		<div class="sezione"><span class="title"><strong><bean:message
			key="protocollo.ricerca.documento" /></strong></span> <jsp:include
			page="/WEB-INF/subpages/protocollo/ricerca/datiDocumento.jsp" />
		</div>
				
		<logic:empty name="ricercaForm" property="visualizzaDestinatariAssegnatari">
			<div class="sezione"><span class="title"><strong>Mittente/Destinatario</strong></span>
			<br />
			<span><bean:message
				key="protocollo.ricerca.cognomeDenominazione" />: </span><html:text
				property="mittente" size="30" maxlength="100" /></div>
			<div class="sezione"><span class="title"><strong><bean:message
					key="protocollo.ricerca.protocollatore" /></strong></span> <jsp:include
					page="/WEB-INF/subpages/protocollo/ricerca/protocollatori.jsp" />
				</div>
		</logic:empty>
		
		<logic:notEmpty name="ricercaForm"
			property="visualizzaDestinatariAssegnatari">
			<logic:equal name="ricercaForm" property="visualizzaDestinatariAssegnatari" value="I">
				<div class="sezione"><span class="title"><strong><bean:message
					key="protocollo.ricerca.mittente" /></strong></span> <br />
				<span><bean:message
					key="protocollo.ricerca.cognomeDenominazione" />: </span><html:text
					property="mittente" size="30" maxlength="100" /></div>
				<div class="sezione"><span class="title"><strong><bean:message
					key="protocollo.ricerca.assegnatario" /></strong></span> <jsp:include
					page="/WEB-INF/subpages/protocollo/ricerca/assegnatari.jsp" /></div>
			</logic:equal>

			<logic:equal name="ricercaForm"
				property="visualizzaDestinatariAssegnatari" value="U">
				<div class="sezione"><span class="title"><strong><bean:message
					key="protocollo.ricerca.mittente" /></strong></span> <jsp:include
					page="/WEB-INF/subpages/protocollo/ricerca/assegnatari.jsp" /></div>
				<div class="sezione"><span class="title"><strong><bean:message
					key="protocollo.ricerca.destinatario" /></strong></span> <br />
				<span><bean:message
					key="protocollo.ricerca.cognomeDenominazione" />: </span><html:text
					property="destinatario" size="30" maxlength="100" /></div>
			</logic:equal>

			<logic:equal name="ricercaForm"
				property="visualizzaDestinatariAssegnatari" value="P">
				<div class="sezione"><span class="title"><strong><bean:message
					key="protocollo.ricerca.mittente" /></strong></span> <jsp:include
					page="/WEB-INF/subpages/protocollo/ricerca/assegnatari.jsp" /></div>
				<div class="sezione"><span class="title"><strong><bean:message
					key="protocollo.ricerca.assegnatario" /></strong></span> <jsp:include
					page="/WEB-INF/subpages/protocollo/ricerca/posta_interna/assegnatari.jsp" />
				</div>
			</logic:equal>


				<div class="sezione"><span class="title"><strong><bean:message
					key="protocollo.ricerca.protocollatore" /></strong></span> <jsp:include
					page="/WEB-INF/subpages/protocollo/ricerca/protocollatori.jsp" />
				</div>
		</logic:notEmpty>

		<div class="sezione"><span class="title"><strong><bean:message
			key="protocollo.ricerca.annotazione" /></strong></span> <jsp:include
			page="/WEB-INF/subpages/protocollo/ricerca/annotazioni.jsp" /></div>
		<div id="bottoni_salva">
		<html:submit styleClass="submit" property="btnCerca" value="Cerca"
			alt="Ricerca protocolli" />
		<%--	<html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="Ripulisce il form" />--%>
		<html:reset styleClass="submit" property="ResetAction" value="Annulla"
			alt="Ripulisce il form" />
		<logic:equal name="ricercaForm" property="indietroVisibile"
			value="true">
			<html:submit styleClass="submit" property="indietro" value="Indietro"
				alt="Indietro" />
		</logic:equal>
</div>
	</html:form>

</eprot:page>
