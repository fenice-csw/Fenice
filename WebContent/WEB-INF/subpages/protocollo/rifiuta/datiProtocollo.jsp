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
			<bean:write name="rifiutoForm" property="numeroProtocollo" />
		</strong></span>
		&nbsp;&nbsp;
		<span><bean:message key="protocollo.dataregistrazione"/></span>&nbsp;:
		<span><strong>
			<bean:write name="rifiutoForm" property="dataRegistrazione" />
		</strong></span>
		&nbsp;&nbsp;
	</p>

		<p>
			<label for="protocollatore"><bean:message key="protocollo.protocollatore"/></label>&nbsp;:
			<span id="protocollatore"><strong>
				<bean:write name="rifiutoForm" property="protocollatore"/>
			</strong></span>
		</p>
		
		<p>
			<label for="oggetto"><bean:message key="protocollo.oggetto"/></label>&nbsp;:
			<span id="oggetto"><strong>
				<bean:write name="rifiutoForm" property="oggetto"/>
			</strong></span>
		</p>
		
		<logic:equal name="rifiutoForm"  property= "riservato" value="true">
			<p>
				<span><strong>
					*** Riservato ***
				</strong></span>
			</p>	
		</logic:equal>
		
</div>
