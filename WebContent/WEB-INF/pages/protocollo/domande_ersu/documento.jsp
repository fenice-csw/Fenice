<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Posta Interna">
<html:form action="/page/protocollo/domande_ersu/documento.do" enctype="multipart/form-data">

<div id="protocollo">


    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />


<br class="hidden" />

<div>
	<bean:message key="campo.obbligatorio.msg" />
	<br class="hidden" />
</div>

<br class="hidden" />
<jsp:include page="/WEB-INF/subpages/protocollo/common/datiProtocollo.jsp" />

<br class="hidden" />


<jsp:include page="/WEB-INF/subpages/protocollo/

" />

<br class="hidden" />
<div class="sezione">
<bean:define id="sezioneVisualizzata" name="
" property="sezioneVisualizzata" />
	
	
	<jsp:include page="/WEB-INF/subpages/protocollo/common/link-sezioni.jsp" />
	
	<logic:equal name="sezioneVisualizzata" value="Mittente" >
	    <jsp:include page="/WEB-INF/subpages/protocollo/ingresso/mittenteView.jsp" />
	</logic:equal>
	<logic:match name="sezioneVisualizzata" value="Assegnatari" >
    	<jsp:include page="/WEB-INF/subpages/protocollo/ingresso/assegnatari.jsp" />
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
	
	<html:submit styleClass="submit" property="salvaAction" value="Registra" alt="Salva protocollo" />
	<html:submit styleClass="submit" property="annullaAction" value="Annulla" alt="Annulla l'operazione" />
	<html:submit styleClass="submit" property="indietroAction" value="Indietro" alt="Torna alla lista delle domande" />
</logic:equal>

</div>

</div>

</html:form>

</eprot:page>