<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<eprot:page title="Area Organizzativa">

	<bean:define id="baseUrl" value="/page/amministrazione/organizzazione/aoo/aoo.do" scope="request" />
	
	<div id="protocollo-errori">
    	<jsp:include page="/WEB-INF/subpages/amministrazione/common/errori.jsp" />
	</div>
	<html:form action="/page/amministrazione/organizzazione/aoo/aoo.do">

	<div class="sezione">
		<span class="title"><strong><bean:message key="amministrazione.organizzazione.aoo.areaorganizzativa"/></strong></span><br />
		<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/aoo/dati_generali.jsp"/>
	</div>
	<div class="sezione">
		<span class="title"><strong><bean:message key="amministrazione.organizzazione.aoo.responsabile"/></strong></span><br />
		<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/aoo/responsabile.jsp"/>
	</div>
	<div class="sezione">
		<span class="title"><strong><bean:message key="amministrazione.organizzazione.aoo.indirizzorecapito"/></strong></span><br />
		<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/aoo/indirizzo.jsp" />
	</div>
	<div class="sezione">
		<span class="title"><strong><bean:message key="amministrazione.organizzazione.aoo.postaelettronica"/></strong></span><br />
		<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/aoo/posta_elettronica.jsp" />
	</div>
	<div class="sezione">
		<span class="title"><strong><bean:message key="amministrazione.organizzazione.aoo.postaelettronicacertificata"/></strong></span><br />
		<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/aoo/posta_elettronica_certificata.jsp" />
	</div>
	<div class="sezione">
		<span class="title"><strong><bean:message key="amministrazione.organizzazione.aoo.postaelettronicarecupero"/></strong></span><br />
		<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/aoo/posta_elettronica_recupero.jsp" />
	</div>
	<div class="sezione">
		<span class="title"><strong>Utenza Conservazione</strong></span><br />
		<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/aoo/gestione_archivi.jsp" />
	</div>
	<label for="pecTimer"><bean:message key="amministrazione.organizzazione.aoo.messaggio1"/></label>:
	<html:text property="pecTimer" maxlength="10" size="10"></html:text>
	<p>
		<html:submit styleClass="submit" property="btnSalva" value="Salva" title="Salva le modifiche effettuate" /> 
		<html:submit styleClass="submit" property="btnAnnulla" value="Annulla" title="Annulla le modifiche effettuate" />
	</p>
	</html:form>
</eprot:page>
