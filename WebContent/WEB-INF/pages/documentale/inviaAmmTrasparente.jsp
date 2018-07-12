<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Invio documenti alla sezione">
<jsp:include page="/WEB-INF/subpages/documentale/common/pathSenzaLink.jsp" />
<html:form action="/page/documentale/inviaAmmTrasparente.do" >

<div id="protocollo">

    <jsp:include page="/WEB-INF/subpages/documentale/common/messaggi.jsp" />

<div id="protocollo-errori">
    <jsp:include page="/WEB-INF/subpages/documentale/common/errori.jsp" />
</div>

<br/>
<br/>
<jsp:include page="/WEB-INF/subpages/documentale/datiDocumentoView.jsp" />
	
	
      
	
	<div class="sezione">
    <span class="title"><strong>Seleziona la Sezione</strong></span>
	<logic:notEmpty name="documentoSezioneForm" property="sezioni">
		<display:table class="simple" width="100%"
			name="sessionScope.documentoSezioneForm.sezioni" export="false"
			sort="page" pagesize="15" id="sezione"
			requestURI="/page/documentale/inviaAmmTrasparente.do">
			<display:column title="ID">
				<html:link action="/page/documentale/inviaAmmTrasparente.do" paramName="sezione"
					paramId="sezioneId" paramProperty="sezioneId">
					<bean:write name="sezione" property="sezioneId" />
				</html:link>
			</display:column>
			<display:column property="descrizione" title="Descrizione" />
		</display:table>
	</logic:notEmpty>
	<br/>
	<br/>
	<logic:notEqual name="documentoSezioneForm" property="sezioneId" value="0">
	<span>
		Sezione Selezionata:
		<strong>
    		<bean:write name="documentoSezioneForm" property="nomeSezione"/>
    	</strong>
    </span>
     </logic:notEqual>
     </div>
	<br class="hidden" />
<div>
<html:submit styleClass="submit" property="btnInvioAmmTrasparente" value="Conferma invio" title="Conferma invio documento alla Sezione" />
<html:submit styleClass="button" property="btnAnnullaSelezione" value="Annulla" title="Annulla l'operazione" />
</div>
</div>
</html:form>
</eprot:page>