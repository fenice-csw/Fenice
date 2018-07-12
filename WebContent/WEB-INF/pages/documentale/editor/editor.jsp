<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Gestione documentale">


	<script type='text/javascript'
		src='/feniceWeb/editor/ckeditor/ckeditor.js'></script>
	<script type='text/javascript'
		src='/feniceWeb/editor/ckfinder/ckfinder.js'></script>

	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriProtocollo" /></div>
	<br />
	<br />
	<html:form action="/page/documentale/editor.do?saveEditorAction=true" enctype="multipart/form-data">
		<br />
		<br />
		<label for="nomeFile">Nome File<span class="obbligatorio">*
		</span>:</label>
		<html:text property="nomeFile"></html:text>
		<br />
		<br />
		<label for="nomeFile">Trattato Da:</label>
		<strong><bean:write name="editorForm" property="intestatario" /></strong>
		<br />
		<br />
		<html:textarea property="testo" styleId="editor1" cols="50" rows="20"></html:textarea>
		<script type="text/javascript">
			//<![CDATA[
				var editor=CKEDITOR.replace('editor1', {
					fullPage : true
				});
			//]]>

			
		</script>
		<div id="scomparsa" class="sezione"><span class="title"><strong>Scegli
		il tipo di protocollo</strong></span> 
		
		
		<logic:iterate id="tipo" name="tipiProtocollo">
		
			<bean:define id="codice" name="tipo" property="codice" />
			<bean:define id="OR_CONDITION" value="true"/>
			<logic:equal name="tipo" property="descrizione" value="Ingresso"><bean:define id="OR_CONDITION" value="false"/></logic:equal>
			<logic:equal name="tipo" property="descrizione" value="Fatture"><bean:define id="OR_CONDITION" value="false"/></logic:equal>
			
			<logic:equal name="OR_CONDITION" value="true">				
				<html:radio property="tipoProtocollo" value="codice" idName="tipo">
					<label for="tipo_<bean:write name='tipo' property='codice' />"><bean:write
						name="tipo" property="descrizione" /></label>
				</html:radio>&nbsp;
    		</logic:equal>	
    		
		</logic:iterate> 
		    <html:submit styleClass="submit" property="protocollaAction" value="Prosegui" alt="Protocolla" /> 
			<a href="#" class="button" onclick="ShowAndHide('button', 'scomparsa');return (false);">Nascondi</a>
		</div>

		<logic:equal name="editorForm" property="modificabile" value="true">
			<div id="button">
			<p>
				<logic:greaterThan name="editorForm" property="versione"
					value="0">
					<html:submit styleClass="submit" property="btnStoria" value="Storia"
						title="Storia del Documento" />
				</logic:greaterThan> 
				<html:submit styleClass="submit" property="pdfAction" value="Stampa"
					alt="Stampa in PDF" /> 
				<html:submit styleClass="submit"
					property="indietroAction" value="Indietro" alt="Indietro" /> 
				<html:submit
					styleClass="submit" property="annullaAction" value="Annulla"
					alt="Annulla" /> 
				<html:submit styleClass="submit"
					property="inviaProtocolloAction" value="Invia al protocollo"
					alt="Invia al protocollo" /> 
				<html:submit styleClass="submit"
						property="showAction" value="Protocolla" alt="Protocolla"
						onclick="ShowAndHide('button','scomparsa');return(false)" />
			</p>
			</div>
		</logic:equal>
		<logic:notEqual name="editorForm" property="modificabile" value="true">
			<html:submit styleClass="submit" property="btnStoria"
				value="Indietro" title="Storia del Documento" />
		</logic:notEqual>
	</html:form>

</eprot:page>
