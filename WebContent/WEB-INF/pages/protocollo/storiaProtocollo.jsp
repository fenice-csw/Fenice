<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Storia Protocollo">
<bean:define id="url" value="/page/protocollo/storia.do?versioneCorrente=true" scope="request"  />
<bean:define id="versione" name="storiaProtocolloForm" property="versione"/>

<logic:notEqual name="storiaProtocolloForm" property="scartato" value="true">
	<bean:define id="url2" value="/page/protocollo/storia.do?versioneCorrente=true" scope="request"  />
</logic:notEqual>

<html:form action="/page/protocollo/storia.do">
<div>
<label><bean:message key="protocollo.numero"/></label>: 
<html:link action="/page/protocollo/storia.do?versioneCorrente=true">
	<span><strong>
		<bean:write name="storiaProtocolloForm" property="numeroProtocollo" />
	</strong></span>
</html:link>
<br/>
<label><bean:message key="protocollo.tipo"/></label>: <span><strong>

<bean:write name="storiaProtocolloForm" property="flagTipo" />
</strong></span><br/>
<label><bean:message key="protocollo.data"/></label>: <span><strong>

<bean:write name="storiaProtocolloForm" property="dataRegistrazione" />
</strong></span><br/>
<label><bean:message key="protocollo.protocollatore"/></label>: <span><strong>

<bean:write name="storiaProtocolloForm" property="protocollatore" />
</strong></span><br/>


<logic:notEmpty name="storiaProtocolloForm" property="versioniProtocollo">
<hr></hr>
<table summary="" cellpadding="2" cellspacing="2" border="1">
	<tr>
		<th>
			<span><bean:message key="protocollo.versione"/></span>
		</th>
		<th>
			<span><bean:message key="protocollo.data.versione"/></span>
		</th>	
		<th>
			<span><bean:message key="protocollo.userupdate"/></span>
		</th>
		<th>
			<span><bean:message key="protocollo.stato"/></span>
		</th>

		<th>
			<span><bean:message key="protocollo.oggetto"/></span>
		</th>
		<logic:notEqual name="storiaProtocolloForm" property="flagTipo" value="U">
			<th>
			<span><bean:message key="protocollo.assegnatari"/></span>
			</th>
		</logic:notEqual>
		<th>
			<span><bean:message key="protocollo.estremiautorizzazione"/></span>
		</th>
				
	</tr>
	<tr>
		<td>

			<span>
				<html:link action="/page/protocollo/storia.do?versioneCorrente=true">
					<bean:write name="storiaProtocolloForm" property="versione"/>
				</html:link></span>
		</td>
		
		<td>
			<span><bean:write name="storiaProtocolloForm" property="dataRegistrazione" /></span>
		</td>
		<td>
			<span><bean:write name="storiaProtocolloForm" property="userUpdate" /></span>
		</td>
		<td>
			<span><bean:write name="storiaProtocolloForm" property="descrizioneStatoProtocollo" /></span>
		</td>

		<td>
			<span><bean:write name="storiaProtocolloForm" property="oggetto" filter="false"/></span>
		</td>
		<!-- 
		<td>
			<span><bean:write name="storiaProtocolloForm" property="cognomeMittente" /></span>
		</td>
		 -->
		<logic:notEqual name="storiaProtocolloForm" property="flagTipo" value="U">
			<td>
				<ul>
		<logic:iterate id="ass" name="storiaProtocolloForm" property="assegnatari">
		<li>
			<logic:equal name="ass" property="competente" value="true">
			<em>
			</logic:equal>
			<bean:write name="ass" property="nomeUfficio"/>
			<logic:notEmpty name="ass" property="nomeUtente">
				/<bean:write name="ass" property="nomeUtente"/>
			</logic:notEmpty>
			<logic:equal name="ass" property="competente" value="true">
			</em>
			</logic:equal>
		</li>
		</logic:iterate>
	</ul>
			</td>		
		</logic:notEqual>
		<td>
			<span><bean:write name="storiaProtocolloForm" property="estremiAutorizzazione" /></span>
		</td>
		
		
	</tr>

	
	<bean:define id="versioniProtocollo" name="storiaProtocolloForm" property="versioniProtocollo" />
	<logic:iterate id="currentRecord" name="storiaProtocolloForm" property="versioniProtocollo">
	<tr>
		<td>
			<span>
				<html:link action="/page/protocollo/storia.do" paramId="versioneSelezionata" paramName="currentRecord" paramProperty="versione">
					<bean:write name="currentRecord" property="versione" />
				</html:link>
			</span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="dateUpdated" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="userUpdated" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="statoProtocollo" /></span>
		</td>
		<td>
			<span><bean:write name="currentRecord" property="oggetto" filter="false"/></span>
		</td>
		
		<logic:notEqual name="storiaProtocolloForm" property="flagTipo" value="U">
		<td>			
			<bean:write name="currentRecord" property="assegnatario" filter="false"/>
		</td>	
		</logic:notEqual>
		<td>
			<span><bean:write name="currentRecord" property="estremiAutorizzazione" /></span>
		</td>
		
	</tr>
	</logic:iterate>
	

</table>
</logic:notEmpty>

</div>
</html:form>


</eprot:page>




