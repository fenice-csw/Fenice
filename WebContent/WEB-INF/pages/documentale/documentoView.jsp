<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Gestione documentale">
	<logic:equal parameter="documentoRegistrato" value="true"
		scope="request">
		<div id="protocollo-messaggi">
		<ul>
			<li><bean:message key="documentale.salvatocorrettamente" />.</li>
		</ul>
		</div>
	</logic:equal>

	<jsp:include
		page="/WEB-INF/subpages/documentale/common/pathSenzaLink.jsp" />

	<html:form action="/page/documentale/documentoView.do">
		<div id="protocollo"><jsp:include
			page="/WEB-INF/subpages/documentale/common/messaggi.jsp" />

		<div id="protocollo-errori"><jsp:include
			page="/WEB-INF/subpages/documentale/common/errori.jsp" /></div>
		<jsp:include
			page="/WEB-INF/subpages/documentale/datiDocumentoView.jsp" />


		<div><logic:equal name="documentoForm"
			property="versioneDefault" value="true">

			<logic:notEqual name="documentoForm" property="documentoId" value="0">

				<logic:equal name="documentoForm" property="statoArchivio" value="L">
					<html:submit styleClass="submit" property="modificaDocumentoAction"
						value="Modifica" alt="Modifica documento" />
					<html:submit styleClass="submit"
						property="visualizzaClassificaDocumentoAction" value="Classifica"
						alt="Classifica il documento" />
					<html:submit styleClass="submit"
						property="impostaFascicoloAction" value="Fascicola"
						alt="Fascicola il documento" />
					<html:submit styleClass="submit" property="eliminaDocumentoAction"
						value="Elimina" alt="Elimina documento" />
					<html:submit styleClass="submit" property="spostaDocumentoAction"
						value="Sposta in un altra Cartella"
						alt="Sposta il documento in un altra cartella" />
					<html:submit styleClass="submit" property="inviaDocumentoAction"
						value="Invia ad un Utente" alt="Invia il documento ad un Utente " />
				</logic:equal>

				
				<eprot:ifAuthorized permission="invio_repertori">
					<html:submit styleClass="submit" property="inviaRepertorioAction"
						value="Invia al Repertorio"
						alt="Invia il documento ad un Repertorio" />
				</eprot:ifAuthorized>
				

				<logic:equal name="documentoForm" property="statoArchivio" value="C">
					<html:submit styleClass="submit" property="inviaProtocolloAction"
						value="Invia al protocollo" alt="Invia al protocollo" />
					<html:submit styleClass="submit" property="documentiArchivioAction"
						value="Protocolla" alt="Protocolla" />
					<html:submit styleClass="submit" property="spostaDocumentoAction"
						value="Sposta in un altra Cartella" alt="Sposta il documento" />
					<html:submit styleClass="submit" property="inviaDocumentoAction"
						value="Invia ad un Utente" alt="Invia il documento ad un Utente" />
				</logic:equal>

				<logic:greaterThan name="documentoForm" property="versione"
					value="0">
					<logic:equal name="documentoForm" property="versioneDefault"
						value="true">
						<html:submit styleClass="submit"
							property="viewStoriaDocumentoAction" value="Storia documento"
							alt="Storia documento" />
					</logic:equal>
				</logic:greaterThan>
				<%-- 
				<logic:equal name="documentoForm" property="fromProcedimento" value="true">
					<input type="button" class="submit" onclick="location.href=this.parentNode.getElementsByTagName ('a').item(0).href" value="Indietro" />
					<html:link action="/page/procedimento.do" paramName="documentoForm" paramId="visualizzaProcedimentoId" paramProperty="procedimentoId" />
				</logic:equal>
				--%>
			</logic:notEqual>

		</logic:equal> <logic:equal name="documentoForm" property="versioneDefault"
			value="false">

			<logic:equal name="documentoForm" property="statoDocumento" value="1">
				<html:submit styleClass="submit"
					property="ripristinaVersioneDocumentoAction"
					value="Ripristina questa versione" alt="Ripristina questa versione" />
			</logic:equal>

			<logic:greaterThan name="documentoForm" property="permessoCorrente"
				value="0">
				<html:submit styleClass="submit"
					property="viewStoriaDocumentoAction" value="Storia documento"
					alt="Storia documento" />
			</logic:greaterThan>

		</logic:equal></div>
		</div>
	</html:form>
</eprot:page>
