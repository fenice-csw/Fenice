<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Modifica email in uscita">

	<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />
	
	<html:form action="/page/protocollo/uscita/modificaEmailUscita.do">
		<br class="hidden" />
		<br class="hidden" />
		<table>
			<tr>
				<td class="label">
					<strong><label for="dataCreazione">Data Creazione:</label></strong>
				</td>
				<td>
					<bean:write name="emailUscitaForm" property="dataCreazione" />
				</td>
			</tr>
			<tr>
				<td class="label">
					<strong><label for="numeroProtocollo">Numero Protocollo:</label></strong>
				</td>
				<td>
					<bean:write name="emailUscitaForm" property="numeroProtocollo" />
				</td>
			</tr>
		</table>
		<div class="sezione">
		<span class="title">Destinatari</span>
			<table  id="tabella_mittenti">
        		<tr>
            		<th>key</th>
            		<th>Nominativo</th>
            		<th>Email</th>
        		</tr>
        		<logic:iterate id="orderItem" name="emailUscitaForm" property="destinatari">
            		 <html:hidden name="orderItem" property="key" indexed="true" />
            		<tr>
                		<td><bean:write name="orderItem" property="key" /></td>
                		<td><bean:write name="orderItem" property="nominativo" /></td>
                		<td><html:text name="orderItem" property="email" size="30" indexed="true" /></td>
            		</tr>
        		</logic:iterate>
    		</table>
		</div>
		<html:submit styleClass="submit" property="aggiornaDestinatari" value="Modifica" alt="Aggiorna Destinatari"/>
	</html:form>

</eprot:page>