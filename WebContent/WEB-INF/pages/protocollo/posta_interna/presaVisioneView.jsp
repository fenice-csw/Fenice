<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Posta interna">

<html:form action="/page/protocollo/posta_interna/presaVisione.do">

<div id="protocollo">


    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />


<br class="hidden" />

<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/datiProtocollo.jsp" />

<br class="hidden" />

<jsp:include page="/WEB-INF/subpages/protocollo/common/datiDocumentoView.jsp" />

<br class="hidden" />

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

</div>
<html:submit styleClass="submit" property="btnPresaVisione" value="Presa Visione" alt="Presa visione posta interna" />

</html:form>
</eprot:page>