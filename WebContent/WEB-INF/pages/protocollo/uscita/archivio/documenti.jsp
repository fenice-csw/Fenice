<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Protocollo in uscita archivio documenti">

    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />


	
	
	<bean:define id="url" value="/page/protocollo/uscita/archivio/documenti.do" scope="request"/>
	
	<html:form action="/page/protocollo/uscita/archivio/documenti.do">

		<div>
		<logic:notEmpty name="documentiArchivioForm" property="documentiInviati">
			<display:table class="simple" width="100%"
				requestURI="/page/protocollo/uscita/archivio/documenti.do"
				name="requestScope.documentiArchivioForm.documentiInviati" export="false"
				sort="list" pagesize="10" id="row">
				<display:column title="">
<%--					<html:radio property="documentoSelezionatoId" value="${row.documentoId}"></html:radio>--%>
					<bean:define id="documentoId" name="row" property="documentoId" />
					<html:radio property="documentoSelezionatoId" idName="row" value="documentoId"></html:radio>
				</display:column>

				<display:column title="Nome file" property="nome" />
				<display:column title="Oggetto" property="oggetto" />
			</display:table>
			<p><br />
				<html:submit styleClass="submit" property="protocolla" value="Protocolla" alt="Protocolla il documento selezionato" />
			</p>
		</logic:notEmpty>
		<logic:empty name="documentiArchivioForm" property="documentiInviati">
		<span><bean:message key="protocollo.uscita.archivio.documenti.messaggio"/>.</span>
		</logic:empty>
		</div>
	</html:form>

</eprot:page>

