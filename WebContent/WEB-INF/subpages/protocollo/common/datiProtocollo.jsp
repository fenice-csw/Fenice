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
		<label for="numeroProtocollo"><bean:message key="protocollo.numero"/></label>&nbsp;:
		<span id="numeroProtocollo"><strong>
			<logic:equal name="protocolloForm" property="protocolloId" value="0"> 
				&lt;nuovo&gt; 
			</logic:equal>
			<logic:notEqual name="protocolloForm" property="protocolloId" value="0"> 
				<bean:write name="protocolloForm" property="numeroProtocollo" />
			</logic:notEqual>	
			</strong></span>
		&nbsp;&nbsp;
		<span><bean:message key="protocollo.dataregistrazione"/></span>&nbsp;:
		<span><strong>
			<bean:write name="protocolloForm" property="dataRegistrazione" />
		</strong></span>
		&nbsp;&nbsp;
		<label for="tipoProtocollo"><bean:message key="protocollo.tipo"/></label>&nbsp;:
		<span id="tipoProtocollo"><strong>
			<bean:write name="protocolloForm" property="flagTipo" />
			</strong></span>
			<!-- 
		<logic:greaterThan name="protocolloForm" property="protocolloId" value="0" >
			&nbsp;&nbsp;
			<label for="descrizioneStatoProtocollo"><bean:message key="protocollo.statodescrizione"/></label>&nbsp;:
			<span id="descrizioneStatoProtocollo"><strong>
				<bean:write name="protocolloForm" property="descrizioneStatoProtocollo" />
			</strong></span>
		</logic:greaterThan>
		 -->	
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

	<logic:greaterThan name="protocolloForm" property="numProtocolloEmergenza" value="0" >
			<span>(da registro d'emergenza numero <strong><bean:write name="protocolloForm" property="numProtocolloEmergenza"/></strong>)</span>
	</logic:greaterThan>

</div>
