<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<div class="sezione">
	<span class="title">
	<strong><bean:message key="postainterna.titolo"/></strong>
	
	</span>
	<p>
		<label for="numeroProtocollo"><bean:message key="postainterna.numero"/></label>&nbsp;:
		<span id="numeroProtocollo"><strong>
			<logic:equal name="protocolloForm" property="protocolloId" value="0"> 
				&lt;nuovo&gt; 
			</logic:equal>
			<logic:notEqual name="protocolloForm" property="protocolloId" value="0"> 
				<bean:write name="protocolloForm" property="numeroProtocollo" />
				<input type="hidden" id="mio_protnum" value="<bean:write name="protocolloForm" property="numeroProtocollo" />/<bean:write name="protocolloForm" property="flagTipo" /> del <bean:write name="protocolloForm" property="dataRegistrazione" />"/>
			</logic:notEqual>	
			</strong></span>
		&nbsp;&nbsp;
		<span><bean:message key="protocollo.dataregistrazione"/></span>&nbsp;:
		<span><strong>
			<bean:write name="protocolloForm" property="dataRegistrazione" />
		</strong></span>
		&nbsp;&nbsp;
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
			<span>(da registro emergenza)</span>
	</logic:greaterThan>

</div>
