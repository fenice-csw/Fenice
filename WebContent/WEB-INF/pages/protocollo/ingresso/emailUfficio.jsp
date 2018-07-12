<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Protocollo in ingresso email">
	
	
	<br/>
	<br/>
	<bean:define id="url" value="/page/protocollo/ingresso/email_ufficio.do" scope="request" />
	
    	<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />
	
	<html:form action="/page/protocollo/ingresso/email_ufficio.do">

		<div>
		<logic:notEmpty name="listaEmailForm" property="listaEmail">
			<display:table class="simple" width="100%"
				requestURI="/page/protocollo/ingresso/email_ufficio.do"
				name="requestScope.listaEmailForm.listaEmail" export="false"
				sort="list" pagesize="10" id="row">
				<display:column title="">
					<bean:define id="id" name="row" property="id"/>
					<html:radio property="emailSelezionataId" idName="row" value="id"></html:radio>
				</display:column>

				<display:column title="Inviata da" property="nomeMittente" />
				<display:column title="Email" property="emailMittente" />
				<display:column title="Data spedizione" property="dataSpedizione" />
				<display:column title="Oggetto" property="testoMessaggio" />
					
				<display:column title="Allegati">
					<logic:notEqual name="row" property="numeroAllegati" value="0">
					<ul>
					 <logic:iterate id="currentRecord" property="descrizioneAllegati" name="row">
						<li>
						<html:link action="/page/protocollo/ingresso/email_ufficio.do" 
				             paramId="downloadAllegatoId" 
				             paramName="currentRecord" 
				             paramProperty="id" 
				             target="_blank"
				             title="Download File">
							<span><bean:write name="currentRecord" property="descrizione" /></span>
						</html:link>
						</li>
						</logic:iterate>
						</ul>
					</logic:notEqual>
					<logic:equal name="row" property="numeroAllegati" value="0">
						Nessun allegato
					</logic:equal>
				</display:column>
			</display:table>
			<p>
				<html:submit styleClass="submit" property="visualizza" value="Visualizza" alt="Visualizza il messaggio selezionato" />
				<html:submit styleClass="submit" property="cancella" value="Elimina" alt="Elimina" />
			</p>
		</logic:notEmpty>
		<logic:empty name="listaEmailForm" property="listaEmail">
		<bean:message key="email.ingresso"/>.		 
		</logic:empty>
		</div>
	</html:form>

</eprot:page>
