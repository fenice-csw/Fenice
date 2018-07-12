<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Protocollo in ingresso email - Log">
	
	
	<bean:define id="url" value="/page/protocollo/ingresso/emailLog.do" scope="request" />
	<div id="protocollo-errori">
    	<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />
	</div>
	<html:form action="/page/protocollo/ingresso/emailLog.do">
		
		<div>
		<html:radio property="tipoEvento"
              value="10" >
          <label for="tipoEventoIngresso"><bean:message key="email.ingresso.logingresso"/></label>
        </html:radio>
        <html:radio property="tipoEvento"
              value="11" >
          <label for="tipoEventoUscita"><bean:message key="email.ingresso.loguscita"/></label>
        </html:radio>
        <html:radio property="tipoEvento"
              value="12" >
          <label for="tipoEventoCRL"><bean:message key="email.ingresso.logcrl"/></label>
        </html:radio>
        <html:submit styleClass="submit" property="visualizza" value="Visualizza" alt="Visualizza i log" />
        <br/><br/>
		<logic:notEmpty name="listaEmailLogForm" property="listaEmail">
			<display:table class="simple" width="98%"
				requestURI="/page/protocollo/ingresso/emailLog.do"
				name="requestScope.listaEmailLogForm.listaEmail" export="false"
				sort="list" pagesize="10" id="row">
				<display:column title="">
					<html:multibox property="emailSelezionateId"> 
						<bean:write name='row' property='eventoId' /> 
					</html:multibox>	
				</display:column>
				<display:column title="Data" property="data" />
				<display:column title="Oggetto" property="oggetto" />
				<display:column title="Tipo" property="tipo" />
				<display:column title="Errore" property="message" />
				<display:column title="Destinatari" property="destinatari" />
			</display:table>
			<p>
				<html:submit styleClass="submit" property="elimina" value="Elimina" alt="Elimina Selezionati" />
			</p>
		</logic:notEmpty>
		<logic:empty name="listaEmailLogForm" property="listaEmail">
		Nessun record trovato.		 
		</logic:empty>
		</div>
	</html:form>

</eprot:page>

