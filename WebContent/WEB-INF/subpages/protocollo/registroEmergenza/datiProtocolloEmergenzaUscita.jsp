<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<div class="sezione">
	<span class="title">
	<strong><bean:message key="protocollo.protocollo"/></strong>
	<logic:greaterThan name="protocolloForm" property="numeroProtocolliRegistroEmergenza" value="0" >
		&nbsp;(
		<html:link action="/page/protocollo/emergenza.do?listaProtocolliPrenotati=true" style="color:red" >
		Ci sono protocolli da Registro Emergenza. 
		</html:link>)
	</logic:greaterThan>
	
	</span>
	<p>
		&nbsp;
		<span id="numeroProtocollo"><strong>
			Sessione d'Emergenza del <bean:write name="protocolloForm" property="dataRegistrazione" />, numeri di Protocolli da assegnare: <%=session.getAttribute("daEmergenza") %>
		</strong></span>
		&nbsp;&nbsp;
		
		<label for="tipoProtocollo"><bean:message key="protocollo.tipo"/></label>&nbsp;:
		<span id="tipoProtocollo"><strong>
				U
			</strong></span>
			
	</p>

	<logic:greaterThan name="protocolloForm" property="protocolloId" value="0">
		<p>
			<label for="protocollatore"><bean:message key="protocollo.protocollatore"/></label>&nbsp;:
			<span id="protocollatore"><strong>
				<bean:write name="protocolloForm" property="protocollatore"/>
			</strong></span>
			<logic:equal name="protocolloForm" property="versioneDefault" value="false"> 
				&nbsp;
				<label for="Versione"><bean:message key="protocollo.versione"/></label>:
				<span id="Versione"><strong>
				<bean:write name="protocolloForm" property="versione"/>
				</strong></span>
			</logic:equal>
		</p>
	</logic:greaterThan>	

	<label for="numProtocolloEmergenza">
		numero protocollo d'emergenza
	</label>&nbsp;: 
	
	<logic:equal name="protocolloForm" property="numProtocolloEmergenza" value="-1">
		<html:text property="numProtocolloEmergenza" value="" size="3"></html:text>
	</logic:equal>
	
	<logic:notEqual name="protocolloForm" property="numProtocolloEmergenza" value="-1">
		<html:text property="numProtocolloEmergenza" size="3"></html:text>
	</logic:notEqual>
	
</div>
