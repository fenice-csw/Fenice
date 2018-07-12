<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Gestione allaccio fascicolo">
	<html:form action="/page/fascicolo/allaccioview.do" enctype="multipart/form-data">

		<div id="protocollo">
		<jsp:include page="/WEB-INF/subpages/fascicolo/errori.jsp" /> <br class="hidden" />
		<html:hidden property="id" /> 
		<jsp:include
			page="/WEB-INF/subpages/fascicolo/fascicoloView.jsp" /> <br
			class="hidden" />
			
		<div class="sezione">
		
		<bean:define id="section" name="fascicoloForm" property="sezioneVisualizzata" /> 
		
		<jsp:include page="/WEB-INF/subpages/fascicolo/link-sezioni.jsp" /> 
		
		<logic:match name="section" value="Protocolli">
			<jsp:include page="/WEB-INF/subpages/fascicolo/protocolliView.jsp" />
		</logic:match> 
		
		<logic:match name="section" value="Documenti">
			<jsp:include page="/WEB-INF/subpages/fascicolo/documentiView.jsp" />
		</logic:match> 
		
		<logic:match name="section" value="Procedimenti">
			<jsp:include page="/WEB-INF/subpages/fascicolo/procedimentiView.jsp" />
		</logic:match>
		
		<logic:match name="section" value="Collegati">
			<jsp:include page="/WEB-INF/subpages/fascicolo/collegatiView.jsp" />
		</logic:match>
		
			<logic:match name="section" value="Sotto Fascicoli">
			<jsp:include page="/WEB-INF/subpages/fascicolo/sottoFascicoliView.jsp" />
		</logic:match>
		
		</div>

		<div>
		<html:submit styleClass="submit" property="btnIndietro" value="indietro"title="Torna al fascicolo Principale"/>
		</div>
		</div>
	</html:form>
</eprot:page>
