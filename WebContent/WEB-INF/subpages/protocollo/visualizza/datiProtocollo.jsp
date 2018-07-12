<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<div class="sezione">
	<span class="title">
	<strong><bean:message key="protocollo.protocollo"/></strong>
	</span>
	<p>
		<label for="numeroProtocollo"><bean:message key="protocollo.numero"/></label>&nbsp;:
		<span id="numeroProtocollo"><strong>
				<bean:write name="visualizzaProtocolloForm" property="numeroProtocollo" />
				<input type="hidden" id="mio_protnum" value="<bean:write name="visualizzaProtocolloForm" property="numeroProtocollo" />/<bean:write name="visualizzaProtocolloForm" property="flagTipo" /> del <bean:write name="visualizzaProtocolloForm" property="dataRegistrazione" />"/>
			</strong></span>
		&nbsp;&nbsp;
		<span><bean:message key="protocollo.dataregistrazione"/></span>&nbsp;:
		<span><strong>
			<bean:write name="visualizzaProtocolloForm" property="dataRegistrazione" />
		</strong></span>
		&nbsp;&nbsp;
		<label for="tipoProtocollo"><bean:message key="protocollo.tipo"/></label>&nbsp;:
		<span id="tipoProtocollo"><strong>
			<bean:write name="visualizzaProtocolloForm" property="flagTipo" />
			</strong></span>
	</p>

		<p>
			<label for="protocollatore"><bean:message key="protocollo.protocollatore"/></label>&nbsp;:
			<span id="protocollatore"><strong>
				<bean:write name="visualizzaProtocolloForm" property="protocollatore"/>
			</strong></span>
				&nbsp;
				<label for="Versione"><bean:message key="protocollo.versione"/></label>:
				<span id="Versione"><strong>
				<bean:write name="visualizzaProtocolloForm" property="versione"/>
				</strong></span>
		</p>

</div>
