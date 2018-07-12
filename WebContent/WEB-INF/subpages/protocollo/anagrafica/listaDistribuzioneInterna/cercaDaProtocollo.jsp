<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<div id="cerca">
<table>
<tr >
    <td>
    <logic:messagesPresent message="true">
   		<html:messages id="actionMessage" property="salvato" message="true" bundle="bundleMessaggiAmministrazione">
     		<li>
     		 <bean:write name="actionMessage"/>
      		</li>
   		</html:messages> 
    </logic:messagesPresent>
		<span><bean:message key="soggetto.lista.denominazione" />:</span>
		<html:text property="descrizione" size="30" maxlength="100" />
		<html:submit styleClass ="submit" property="cercaDaProtocolloAction" value="Cerca" alt="Cerca" />
		<html:submit styleClass ="submit" property="indietroLD" value="Indietro" alt="Indietro" />
    </td>
    
</tr>
</table>

<%--<jsp:include page="/WEB-INF/subpages/protocollo/anagrafica/listaDistribuzioni/soggettiListaDistribuzione.jsp" />--%>
</div>

