<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<script type='text/javascript' src='/feniceWeb/editor/ckeditor/ckeditor.js'></script>
<script type='text/javascript' src='/feniceWeb/editor/ckfinder/ckfinder.js'></script>

<eprot:page title="Invio documenti da editor al protocollo">

 <jsp:include page="/WEB-INF/subpages/documentale/common/messaggi.jsp" />

<div id="protocollo-errori">
    <jsp:include page="/WEB-INF/subpages/documentale/common/errori.jsp" />
</div>
	 
	<script type='text/javascript' src='/feniceWeb/ckeditor/ckeditor.js'></script>
	
	<html:form action="/page/documentale/daTemplate.do">
		<br />
		<br />
		<label for="oggetto">Oggetto<span class="obbligatorio">*
		</span>:</label>
		<html:text property="oggetto" size="60"/>
		<br />
		<br />
		<%--
			<label for="testo">Testo<span class="obbligatorio">*
			</span>:</label>
			<html:textarea property="testo" cols="90" rows="10"/>
			<br />
			<br />
	 	--%>
		<html:textarea property="testo" styleId="editor1" cols="50" rows="20"></html:textarea>
		<script type="text/javascript">
			//<![CDATA[
				var editor=CKEDITOR.replace('editor1', {
			        customConfig : 'BBCCAA_config.js'				
				        });
			//]]>
		</script>
		<br />
		<br />
		
		<logic:equal name="editorForm" property="ull" value="false">
			<div class="sezione">
				<span class="title"><strong><bean:message key="documentale.destinatari" /></strong></span> 
				<jsp:include page="/WEB-INF/subpages/documentale/editor/sezioni.jsp" />
			</div>
		</logic:equal>
		<div class="sezione">
			<span class="title"><strong><bean:message key="fascicolo.fascicoli" /></strong></span> 
			<jsp:include page="/WEB-INF/subpages/documentale/editor/fascicoliEditor.jsp" />
		</div>
		<logic:equal name="editorForm" property="ull" value="false">
		<div class="sezione">
			<span class="title"><strong>Protocollo collegato</strong></span> 
			<jsp:include page="/WEB-INF/subpages/documentale/editor/allacci.jsp" />
		</div>
		</logic:equal>
		<br class="hidden" />
		
		<div id="button">
		
			<logic:equal name="editorForm" property="tipo" value="OUT">
				<html:submit styleClass="submit" property="btnStampa" value="Stampa" title="Conferma invio documento al protocollo" onclick="ShowAndHide('button','scomparsa');return(true)" />
			</logic:equal>
			<logic:equal name="editorForm" property="tipo" value="IN">
				<logic:equal name="editorForm" property="dirigente" value="false">
					<a href="#" class="button" onclick="ShowAndHide('button', 'scomparsa');return (false);">Invia per la Firma</a>
					<logic:equal name="editorForm" property="ull" value="true">
					 	<html:submit styleClass="submit" property="btnIndietroProcedimento" value="Indietro" title="Torna al Procedimento"/>
					</logic:equal>
				</logic:equal>
				<logic:equal name="editorForm" property="dirigente" value="true">
					<html:submit styleClass="submit" property="btnConferma" value="Salva" title="Salva il documento"/>
					<html:submit styleClass="submit" property="btnProtocollaInterna" value="Protocolla Interna" title="Protocolla il documento come Posta Interna"/>
					
					<logic:equal name="editorForm" property="ull" value="true">
					 	<html:submit styleClass="submit" property="btnIndietroProcedimento" value="Indietro" title="Torna al Procedimento"/>
						<logic:equal name="editorForm" property="responsabileEnte" value="false">
							<logic:equal name="editorForm" property="statoProcedimentoULL" value="4">
								<html:submit styleClass="submit" property="btnInviaAvvocatoGenerale" value="Invia all'avvocato generale" title="Invia all'avvocato generale"/>
							</logic:equal>
							<logic:equal name="editorForm" property="statoProcedimentoULL" value="6">
								<html:submit styleClass="submit" property="btnInviaAvvocatoGenerale" value="Invia all'avvocato generale" title="Invia all'avvocato generale"/>
							</logic:equal>
						</logic:equal>
					</logic:equal>
					
					<logic:notEqual name="editorForm" property="documentoId" value="0">
						<html:submit styleClass="submit" property="btnElimina" value="Elimina" title="Elimina il documento"/>
						<a href="#" class="button" onclick="ShowAndHide('button', 'scomparsa');return (false);">Ritorna</a>
					</logic:notEqual>
				</logic:equal>
			</logic:equal>
		</div>
			
		<div id="scomparsa">
			<logic:equal name="editorForm" property="tipo" value="OUT">		
				<html:submit styleClass="submit" property="btnProtocolla" value="Protocolla" alt="Protocolla" />
			</logic:equal>
			<logic:equal name="editorForm" property="tipo" value="IN">
				<label for="msgCarica">Messaggio:</label>
				<html:textarea property="msgCarica" cols="90" rows="2"/>
				<br />
				<logic:equal name="editorForm" property="dirigente" value="false">
					<html:submit styleClass="submit" property="btnInviaFirma" value="Continua" title="Invia documento al dirigente per la firma" onclick="ShowAndHide('button','scomparsa');return(true)"/>
				</logic:equal>
				<logic:equal name="editorForm" property="dirigente" value="true">
					<html:submit styleClass="submit" property="btnRifiuta" value="Continua" title="Ritorna il documento"/>
				</logic:equal>
			</logic:equal>
			<a href="#" class="button" onclick="ShowAndHide('button', 'scomparsa');return (false);">Annulla</a>
		</div>
		
	</html:form>
</eprot:page>