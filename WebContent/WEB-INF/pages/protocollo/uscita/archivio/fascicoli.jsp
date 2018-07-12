<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Protocollo in uscita archivio fascicoli">
	
	
	<bean:define id="url" value="/page/protocollo/uscita/archivio/fascicoli.do" scope="request"/>
	
    	<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />
	
	<html:form action="/page/protocollo/uscita/archivio/fascicoli.do">

		<div>
		<logic:notEmpty name="fascicoliArchivioForm" property="fascicoliInviatiCollection">
			<display:table class="simple" width="100%"
				requestURI="/page/protocollo/uscita/archivio/fascicoli.do"
				name="requestScope.fascicoliArchivioForm.fascicoliInviatiCollection" export="false"
				sort="list" pagesize="10" id="row">
				<display:column title="">
					<html:radio property="fascicoloSelezionatoId" value="id" idName="row"></html:radio>
				</display:column>

				<display:column title="Fascicolo" property="annoProgressivo" />
				<display:column title="Documenti" property="documenti" />
			</display:table>
			<p><br />
				<html:submit styleClass="submit" property="protocolla" value="Protocolla" alt="Protocolla il fascicolo selezionato" />
			</p>
		</logic:notEmpty>
		<logic:empty name="fascicoliArchivioForm" property="fascicoliInviatiCollection">
		<span><bean:message key="protocollo.uscita.archivio.fascicolo.messaggio"/>.</span>
		</logic:empty>
		</div>
	</html:form>

</eprot:page>

