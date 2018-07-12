<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Posta Interna">
<html:form action="/page/protocollo/posta_interna/documento.do" enctype="multipart/form-data">

<div id="protocollo">


    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />


<br class="hidden" />

<div>
	<bean:message key="campo.obbligatorio.msg" />
	<br class="hidden" />
</div>

<br class="hidden" />
<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/datiProtocollo.jsp" />

<br class="hidden" />


<logic:equal name="protocolloForm" property="webSocketEnabled" value="true" >
  	<jsp:include page="/WEB-INF/subpages/protocollo/common/datiDocumento.jsp" />
</logic:equal>
<logic:equal name="protocolloForm" property="webSocketEnabled" value="false" >
	<jsp:include page="/WEB-INF/subpages/protocollo/common/datiDocumentoOld.jsp" />
</logic:equal>
   	
<logic:greaterThan name="protocolloForm" property="protocolloId" value="0">
	<logic:greaterThan name="protocolloForm" property="versione" value="0">
		<jsp:include page="/WEB-INF/subpages/protocollo/common/estremiAutorizzazione.jsp" />
	</logic:greaterThan>
</logic:greaterThan>

<br class="hidden" />
<div class="sezione">
<bean:define id="sezioneVisualizzata" name="postaInternaForm" property="sezioneVisualizzata" />
	
	<%-- 
	<jsp:include page="/WEB-INF/subpages/protocollo/common/link-sezioni.jsp" />
	--%>
	<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/link-sezioni.jsp" />
	
	<logic:equal name="sezioneVisualizzata" value="Mittente" >
	    <jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/mittente.jsp" />
	</logic:equal>
	<logic:match name="sezioneVisualizzata" value="Destinatari" >
    	<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/destinatari.jsp" />
	</logic:match>
	<logic:match name="sezioneVisualizzata" value="Allegati" >
    	<logic:equal name="protocolloForm" property="webSocketEnabled" value="true" >
    		<jsp:include page="/WEB-INF/subpages/protocollo/common/allegati.jsp" />
    	</logic:equal>
    	<logic:equal name="protocolloForm" property="webSocketEnabled" value="false" >
    		<jsp:include page="/WEB-INF/subpages/protocollo/common/allegatiOld.jsp" />
    	</logic:equal>
	</logic:match>
	<logic:match name="sezioneVisualizzata" value="Collegati">
    	<jsp:include page="/WEB-INF/subpages/protocollo/common/allacci.jsp" />
	</logic:match>
	<logic:equal name="sezioneVisualizzata" value="Annotazioni">
    	<jsp:include page="/WEB-INF/subpages/protocollo/common/annotazioni.jsp" />
	</logic:equal>
	<logic:equal name="sezioneVisualizzata" value="Titolario">
    	<jsp:include page="/WEB-INF/subpages/protocollo/common/titolario.jsp" />
	</logic:equal>
	<logic:match name="sezioneVisualizzata" value="Fascicoli">
    	<jsp:include page="/WEB-INF/subpages/protocollo/common/datiFascicoli.jsp" />
	</logic:match>
	<logic:match name="sezioneVisualizzata" value="Procedimenti">
    	<jsp:include page="/WEB-INF/subpages/protocollo/common/datiProcedimenti.jsp" />
	</logic:match>
</div>

<div id="bottoni_salva">

<logic:equal name="protocolloForm" property="protocolloId" value="0" >
	
	<html:submit styleClass="submit" property="salvaAction" value="Registra" alt="Salva protocollo" onclick="disableButton(this)"/>
	<html:submit styleClass="submit" property="annullaAction" value="Annulla" alt="Annulla l'operazione" />
</logic:equal>

<logic:notEqual name="protocolloForm" property="protocolloId" value="0" >
	<html:submit styleClass="submit" property="salvaAction" value="Salva" alt="Salva protocollo" />
	<html:submit styleClass="submit" property="annullaAction" value="Annulla" alt="Annulla l'operazione" />
	<logic:notEqual name="protocolloForm" property="stato" value="C">
		<html:submit styleClass="submit" property="btnStampaEtichettaProtocollo" value="Etichetta protocollo" alt="Stampa etichetta protocollo" />
	</logic:notEqual>
</logic:notEqual>



</div>

</div>

</html:form>

</eprot:page>