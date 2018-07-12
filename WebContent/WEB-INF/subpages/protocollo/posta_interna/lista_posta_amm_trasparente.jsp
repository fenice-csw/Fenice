<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>


<html:xhtml />

<div>
	<logic:notEmpty name="scaricoForm" property="protocolliScaricoCollection">
		<display:table class="simple" width="100%" requestURI="/page/protocollo/dashboard/posta_interna_amm_trasparente.do"
			name="sessionScope.scaricoForm.protocolliScaricoCollection"
			export="false" sort="list" pagesize="30" id="row">
			<display:column title="">
				<logic:equal name="row" property="competente" value="true">
					<input type="checkbox" name="protocolloScaricoChkBox"
						value="<bean:write name='row' property='protocolloId'/>" />
				</logic:equal>
			</display:column>
			<display:column title="N. protocollo">
				<html:link  action="/page/protocollo/posta_interna/ricevute_amm_trasparente.do" 
					paramId="postaSelezionato" paramProperty="protocolloId" paramName="row">
					<bean:write name="row" property="annoNumeroProtocollo" />
				</html:link>	
			</display:column>	
			<display:column property="dataProtocollo" title="Registrato il" />
				<display:column title="Mittente">
					<logic:notEqual name="row" property="tipoMittente" value="M">
						<bean:write name="row" property="mittente" />
					</logic:notEqual>
				</display:column>
				
			<display:column title="Oggetto" property="oggetto" />
			<display:column title="DOC" >
					  <html:link action="/page/protocollo/posta_interna/ricevute_amm_trasparente.do" 
			             paramId="downloadDocprotocolloSelezionato" 
			             paramName="row" 
			             paramProperty="annoNumero" 
			             target="_blank"
			             title="Download File">
						<bean:write name="row" property="pdf"/>
				  </html:link>
			</display:column>
			<display:column title="Messaggio" property="messaggio" />
			<display:column title="Azioni">
				[<html:link  action="/page/protocollo/posta_interna/ricevute_amm_trasparente.do"
						paramId="btnInviaSezione" paramProperty="protocolloId" paramName="row">Invia a Sezione</html:link>]
			</display:column>	
		</display:table>
	</logic:notEmpty>
</div>

