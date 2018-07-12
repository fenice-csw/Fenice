<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Invia a Repertorio">
<html:form action="/page/protocollo/posta_interna/invio_repertorio.do" enctype="multipart/form-data">

<div id="protocollo">
    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />
	<br class="hidden" />

	<br class="hidden" />
	<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/datiProtocollo.jsp" />

	<br class="hidden" />
	<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/dati_documento_repertorio.jsp" />

	<br class="hidden" />
	<div class="sezione">
	    <span class="title"><strong>Seleziona il Repertorio</strong></span>
		<logic:notEmpty name="inviaRepertorioForm" property="repertori">
			<display:table class="simple" width="100%"
				name="sessionScope.inviaRepertorioForm.repertori" export="false"
				sort="page" pagesize="15" id="repertorio"
				requestURI="/page/protocollo/posta_interna/invio_repertorio.do">
				<display:column title="ID">
				<html:link action="/page/protocollo/posta_interna/invio_repertorio.do" paramName="repertorio"
					paramId="repertorioId" paramProperty="repertorioId">
					<bean:write name="repertorio" property="repertorioId" />
				</html:link>
			</display:column>
				<display:column property="descrizione" title="Descrizione" />
			</display:table>
		</logic:notEmpty>
		<br/>
		<br/>
		<logic:notEqual name="inviaRepertorioForm" property="repertorioId" value="0">
			<span>
				Repertorio Selezionato:
				<strong>
		    		<bean:write name="inviaRepertorioForm" property="nomeRepertorio"/>
		    	</strong>
		    </span>
	     </logic:notEqual>
     </div>

	<div>
		<html:submit styleClass="submit" property="btnInvioRepertorio" value="Conferma" title="Conferma invio documento al repertorio" />
		<html:submit styleClass="button" property="btnAnnullaSelezione" value="Annulla" title="Annulla l'operazione" />
	</div>
</div>

</html:form>
</eprot:page>