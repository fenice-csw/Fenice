<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Invia a Sezione">
<html:form action="/page/protocollo/posta_interna/invio_ammtrasparente.do" enctype="multipart/form-data">

<div id="protocollo">
    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />
	<br class="hidden" />

	<br class="hidden" />
	<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/datiProtocollo.jsp" />

	<br class="hidden" />
	<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/dati_documento_amm_trasparente.jsp" />

	<br class="hidden" />
	<div class="sezione">
	    <span class="title"><strong>Seleziona la Sezione</strong></span>
		<logic:notEmpty name="inviaSezioneForm" property="sezioni">
			<display:table class="simple" width="100%"
				name="sessionScope.inviaSezioneForm.sezioni" export="false"
				sort="page" pagesize="15" id="sezione"
				requestURI="/page/protocollo/posta_interna/invio_amm_trasparente.do">
				<display:column title="ID">
				<html:link action="/page/protocollo/posta_interna/invio_amm_trasparente.do" paramName="sezione"
					paramId="sezioneId" paramProperty="sezioneId">
					<bean:write name="sezione" property="sezioneId" />
				</html:link>
			</display:column>
				<display:column property="descrizione" title="Descrizione" />
			</display:table>
		</logic:notEmpty>
		<br/>
		<br/>
		<logic:notEqual name="inviaSezioneForm" property="sezioneId" value="0">
			<span>
				Sezione Selezionata:
				<strong>
		    		<bean:write name="inviaSezioneForm" property="nomeSezione"/>
		    	</strong>
		    </span>
	     </logic:notEqual>
     </div>

	<div>
		<html:submit styleClass="submit" property="btnInvioSezione" value="Conferma" title="Conferma invio documento alla sezione" />
		<html:submit styleClass="button" property="btnAnnullaSelezione" value="Annulla" title="Annulla l'operazione" />
	</div>
</div>

</html:form>
</eprot:page>