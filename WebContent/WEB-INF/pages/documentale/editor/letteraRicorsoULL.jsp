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
	<html:form action="/page/documentale/letteraRicorsoULL.do" enctype="multipart/form-data">
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
					toolbar : 'ULL',
					fullPage : true
				});
			//]]>

			
		</script>
		
		<br />
		<br />
		<label title="Data Evidenza" for="dataScadenza"><bean:message
			key="procedimento.data.evidenza" />:</label>
		<html:text styleClass="obbligatorio" styleId="dataScadenza" property="dataScadenza"
		    name="editorForm" size="10" maxlength="10"  /><eprot:calendar textField="dataScadenza" />
		<br />
		<br />
		<label for="textScadenza">Motivazioni:</label>
		<html:textarea property="textScadenza" name="editorForm" cols="50" rows="5" />    
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
				
				<html:submit styleClass="submit"
					property="inviaProtocolloAction" value="Invia al protocollo"
					alt="Invia al protocollo" /> 
				<%-- --%>
				<html:submit styleClass="submit"
					property="protocollaAction" value="Protocolla" alt="Protocolla"/> 
				<%--  
				<html:submit styleClass="submit" property="inviautenteAction" value="Invia ad un Utente" alt="Invia il documento ad un Utente" />
				--%>
				
			</p>
			</div>
		</logic:equal>
		<logic:notEqual name="editorForm" property="modificabile" value="true">
			<html:submit styleClass="submit" property="btnStoria"
				value="Indietro" title="Storia del Documento" />
		</logic:notEqual>
	</html:form>

</eprot:page>
