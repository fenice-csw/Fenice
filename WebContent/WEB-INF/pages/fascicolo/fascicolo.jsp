<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Gestione fascicoli">
	<html:form action="/page/fascicolo.do" enctype="multipart/form-data">

		<div id="protocollo"><jsp:include
			page="/WEB-INF/subpages/fascicolo/errori.jsp" /> <br class="hidden" />
		<html:hidden property="id" /> <jsp:include
			page="/WEB-INF/subpages/fascicolo/fascicoloView.jsp" /> <br
			class="hidden" />
			
		<div class="sezione">
		
		<bean:define id="section" name="fascicoloForm" property="sezioneVisualizzata" /> 
		
		
		
		
		<jsp:include page="/WEB-INF/subpages/fascicolo/link-sezioni.jsp" /> 
			
		<logic:match name="section" value="Protocolli">
			<logic:equal name="fascicoloForm" property="search" value="false">
				<jsp:include page="/WEB-INF/subpages/fascicolo/protocolli.jsp" />
			</logic:equal>
			<logic:equal name="fascicoloForm" property="search" value="true">
				<jsp:include page="/WEB-INF/subpages/fascicolo/protocolliSearch.jsp" />
			</logic:equal>
		</logic:match> 
		
		<logic:match name="section" value="Documenti">
			<jsp:include page="/WEB-INF/subpages/fascicolo/documenti.jsp" />
		</logic:match> 
		<logic:match name="section" value="Procedimenti">
			<jsp:include page="/WEB-INF/subpages/fascicolo/procedimenti.jsp" />
		</logic:match>
		<logic:match name="section" value="Collegati">
			<jsp:include page="/WEB-INF/subpages/fascicolo/collegati.jsp" />
		</logic:match>
		
		<logic:match name="section" value="Sotto Fascicoli">
			<jsp:include page="/WEB-INF/subpages/fascicolo/sottoFascicoli.jsp" />
		</logic:match>
		</div>

		<div><logic:equal name="fascicoloForm"
			property="versioneDefault" value="true">
			<logic:notEqual name="fascicoloForm" property="id" value="0">
				<logic:equal name="fascicoloForm" property="modificabile"
					value="true">
					<logic:equal name="fascicoloForm" property="statoFascicolo"
						value="4">
						<logic:equal name="fascicoloForm" property="referente" value="true">
							<html:submit styleClass="submit" property="btnModifica"
								title="Modifica i dati del fascicolo">
								<bean:message key="fascicolo.button.modifica" />
							</html:submit>
					
					
							<html:submit styleClass="submit" property="btnRiapri"
								title="Riapre il fascicolo">
								<bean:message key="fascicolo.button.riapri" />
							</html:submit>
						
							<html:submit styleClass="submit" property="btnChiudi"
								title="Il fascicolo viene messo agli Atti">
								<bean:message key="fascicolo.button.chiudi" />
							</html:submit>
						
							<html:submit styleClass="submit" property="btnCancella"
								title="Cancella il fascicolo">
								<bean:message key="fascicolo.button.cancella" />
							</html:submit>
						</logic:equal>
					</logic:equal>

					<logic:equal name="fascicoloForm" property="statoFascicolo"
						value="0">
						<logic:equal name="fascicoloForm" property="referente" value="true">
							<html:submit styleClass="submit" property="btnModifica"
								title="Modifica i dati del fascicolo">
								<bean:message key="fascicolo.button.modifica" />
							</html:submit>
						
							<html:submit styleClass="submit" property="btnChiudi"
								title="Il fascicolo viene messo agli Atti">
								<bean:message key="fascicolo.button.chiudi" />
							</html:submit>
						
							<html:submit styleClass="submit" property="btnCancella"
								title="Cancella il fascicolo">
								<bean:message key="fascicolo.button.cancella" />
							</html:submit>
						</logic:equal>
					</logic:equal>

					<logic:equal name="fascicoloForm" property="statoFascicolo"
						value="1">
						<logic:equal name="fascicoloForm" property="referente" value="true">
							<html:submit styleClass="submit" property="btnRiapri"
								title="Riapre il fascicolo">
								<bean:message key="fascicolo.button.riapri" />
							</html:submit>
						</logic:equal>
					</logic:equal>
					
				</logic:equal>
			</logic:notEqual>
			
			<logic:greaterThan name="fascicoloForm" property="versione" value="0">
				<html:submit styleClass="submit" property="btnStoria" value="Storia"
					title="Storia del Fascicolo" />
			</logic:greaterThan>

		</logic:equal> 
		<html:submit styleClass="submit" property="btnStampa"title="Stampa il frontespizio del fascicolo" value="Stampa" />
		<html:submit styleClass="submit" property="btnIndietro"title="Torna alla pagina precedente" value="Indietro" />
		</div>
		</div>
	</html:form>
</eprot:page>
