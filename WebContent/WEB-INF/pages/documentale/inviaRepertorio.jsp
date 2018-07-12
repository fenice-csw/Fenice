<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Invio documenti al repertorio">
<jsp:include page="/WEB-INF/subpages/documentale/common/pathSenzaLink.jsp" />
<html:form action="/page/documentale/inviaRepertorio.do" >

<div id="protocollo">

    <jsp:include page="/WEB-INF/subpages/documentale/common/messaggi.jsp" />

<div id="protocollo-errori">
    <jsp:include page="/WEB-INF/subpages/documentale/common/errori.jsp" />
</div>

<br/>
<br/>
<jsp:include page="/WEB-INF/subpages/documentale/datiDocumentoView.jsp" />
	
	
      
	
	<div class="sezione">
    <span class="title"><strong>Seleziona il Repertorio</strong></span>
	<logic:notEmpty name="documentoForm" property="repertori">
		<display:table class="simple" width="100%"
			name="sessionScope.documentoForm.repertori" export="false"
			sort="page" pagesize="15" id="repertorio"
			requestURI="/page/documentale/inviaRepertorio.do">
			<display:column title="ID">
				<html:link action="/page/documentale/inviaRepertorio.do" paramName="repertorio"
					paramId="repertorioId" paramProperty="repertorioId">
					<bean:write name="repertorio" property="repertorioId" />
				</html:link>
			</display:column>
			<display:column property="descrizione" title="Descrizione" />
		</display:table>
	</logic:notEmpty>
	<br/>
	<br/>
	<logic:notEqual name="documentoForm" property="repertorioId" value="0">
	<span>
		Repertorio Selezionato:
		<strong>
    		<bean:write name="documentoForm" property="nomeRepertorio"/>
    	</strong>
    </span>
     </logic:notEqual>
     </div>
	<br class="hidden" />
<div>
<html:submit styleClass="submit" property="btnInvioRepertorio" value="Conferma invio" title="Conferma invio documento al repertorio" />
<html:submit styleClass="button" property="btnAnnullaSelezione" value="Annulla" title="Annulla l'operazione" />
</div>
</div>
</html:form>
</eprot:page>