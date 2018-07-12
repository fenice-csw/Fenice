<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Invio documenti da editor al protocollo">
	
	<div id="protocollo-errori">
		<html:errors bundle="bundleErroriProtocollo" />
	</div>
	<%--  
	<script type='text/javascript' src='/feniceWeb/ckeditor/ckeditor.js'></script>
	--%>
	<html:form action="/page/documentale/inviaDocumentoEditor.do">
		<br />
		<br />
		<div class="sezione">
			<span class="title"><strong><bean:message key="documentale.destinatari" /></strong></span> 
			<jsp:include page="/WEB-INF/subpages/documentale/editor/destinatariEditor.jsp" />
		</div>
		<div class="sezione">
			<span class="title"><strong><bean:message key="fascicolo.fascicoli" /></strong></span> 
			<jsp:include page="/WEB-INF/subpages/documentale/editor/fascicoliEditor.jsp" />
		</div>
		<br class="hidden" />
			<html:submit styleClass="submit"
			property="btnInvioProtocollo" value="Conferma"
			title="Conferma invio documento al protocollo" />
			<html:link action="/page/documentale/editor.do" paramName="editorForm" paramId="docId"paramProperty="documentoId" styleClass="button">
					Indietro
			</html:link>
	</html:form>
</eprot:page>
