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
	
	<html:form action="/page/documentale/avvocatoGenerale.do">
		<br />
		<br />
		<label for="oggetto">Oggetto<span class="obbligatorio">*
		</span>:</label>
		<html:text property="oggetto" size="60"/>
		<br />
		<br />
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
		
		<div class="sezione">
				<span class="title"><strong><bean:message key="documentale.destinatari" /></strong></span> 
				<jsp:include page="/WEB-INF/subpages/documentale/editor/sezioni.jsp" />
			</div>
		<logic:equal name="editorForm" property="tipo" value="IN">
		<div class="sezione">
			<span class="title"><strong><bean:message key="fascicolo.fascicoli" /></strong></span> 
			<jsp:include page="/WEB-INF/subpages/documentale/editor/fascicoliEditor.jsp" />
		</div>
		</logic:equal>
		<br class="hidden" />
		
		<div id="button">
			<logic:equal name="editorForm" property="tipo" value="OUT">
				<html:submit styleClass="submit" property="btnStampa" value="Stampa" title="Conferma invio documento al protocollo" onclick="ShowAndHide('button','scomparsa');return(true)" />
			</logic:equal>
			<logic:equal name="editorForm" property="tipo" value="IN">
				<html:submit styleClass="submit" property="btnProtocollaInterna" value="Protocolla" title="Protocolla il documento come Posta Interna"/>
			</logic:equal>
		</div>
			
		<div id="scomparsa">
			<html:submit styleClass="submit" property="btnProtocolla" value="Protocolla" alt="Protocolla" />
			<a href="#" class="button" onclick="ShowAndHide('button', 'scomparsa');return (false);">Annulla</a>
		</div>
		
	</html:form>
</eprot:page>
