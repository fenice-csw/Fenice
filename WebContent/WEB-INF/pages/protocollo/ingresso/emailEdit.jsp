<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />

<eprot:page title="Protocollo in ingresso email">
	
	<%-- 
	<bean:define id="url" value="/page/protocollo/ingresso/email.do"/>
	--%>
	
   
	
	<html:form action="/page/protocollo/ingresso/email.do" enctype="multipart/form-data">
		
		<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />
		
		<div>
		    <jsp:include page="/WEB-INF/subpages/protocollo/ingresso/dettaglioEmail.jsp" />
		</div>
		<logic:equal name="listaEmailForm" property="nuovoMittente" value="true">
		<div class="sezione"><span class="title"><strong>Inserisci un Nuovo Mittente</strong></span>  
			<p>
			<bean:message key="email.nuovo_mittente"/>:
			</p>
  			<html:submit styleClass="submit" property="nuovoSoggettoFisico" value="Nuova Persona Fisica" />  
  			<html:submit styleClass="submit" property="nuovoSoggettoGiuridico" value="Nuova Persona Giuridica" />    
		</div>
	</logic:equal>
		<div>
		<!--  
			<html:submit styleClass="submit" property="cercaMittente" value="Protocolla" alt="Protocolla" />
			-->
			<html:submit styleClass="submit" property="cercaMittente" value="Protocolla" alt="Protocolla" />
			
			<html:submit styleClass="submit" property="cancella" value="Elimina" alt="Elimina" />
		</div>
	</html:form>

</eprot:page>

