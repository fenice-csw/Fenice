<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Storia documento">
<html:form action="/page/documentale/documentoView.do">
<jsp:include page="/WEB-INF/subpages/documentale/common/pathSenzaLink.jsp" />

    <jsp:include page="/WEB-INF/subpages/documentale/common/messaggi.jsp" />

<div id="protocollo-errori">
    <jsp:include page="/WEB-INF/subpages/documentale/common/errori.jsp" />
</div>

<logic:notEmpty name="documentaleStoriaDocumenti">
<span><bean:message key="documentale.versionecorrente"/>: <strong>
<bean:define id="documentoId" name="documentoForm" property="documentoId"/>
<html:link action="/page/documentale/cerca.do" paramId="documentoSelezionato" paramName="documentoId">
	<bean:write name="documentoForm" property="nomeFile" />
</html:link><br /><br />

</strong></span>


<table summary="" cellpadding="2" cellspacing="2" border="1">
	<tr>
		<th>
			<span><bean:message key="documentale.versione"/></span>
		</th>		
		<th>
			<span><bean:message key="documentale.nome"/></span>
		</th>	
		<th>
			<span><bean:message key="documentale.versione_data"/></span>
		</th>	
		<th>
			<span><bean:message key="protocollo.userupdate"/></span>
		</th>
		<th>
			<span><bean:message key="protocollo.stato"/></span>
		</th>	
		<th>
			<span><bean:message key="documentale.oggetto"/></span>
		</th>		
<%-- 	<th>
			<span><bean:message key="documentale.tipo_documento"/></span>
		</th>
	<th>
			<span><bean:message key="documentale.versione_data"/></span>
		</th>--%>
	</tr>
	<bean:define id="doc" name="documentaleStoriaDocumenti" />
	<logic:notEmpty name="documentaleStoriaDocumenti"> 
	<logic:iterate id="currentRecord" name="documentaleStoriaDocumenti"> 
	<tr>
		<td>
			<span>
				<html:link action="/page/documentale/documentoView.do" name="currentRecord" property="params">
					<bean:write name="currentRecord" property="versione" />
				</html:link>
			</span>
		</td>
		<td>
			<span>
				<bean:write name="currentRecord" property="nome" />
			</span>
		</td>
		<td>
			<span>
				<bean:write name="currentRecord" property="dataModifica" />
			</span>
		</td>
		<td>
			<span>
				<bean:write name="currentRecord" property="owner" />
			</span>
		</td>
		<td>
		<span>
		<logic:equal name="currentRecord" property="stato" value="L"><bean:message key="documentale.lavorazione"/></logic:equal>
		<logic:equal name="currentRecord" property="stato" value="C"><bean:message key="documentale.classificato"/></logic:equal>
		<logic:equal name="currentRecord" property="stato" value="I"><bean:message key="documentale.inviatoprotocollo"/></logic:equal>
		</span>
<%--			<span>-->
<!--				<bean:write name="currentRecord" property="stato" />-->
<!--			</span>--%>
		</td>		
		<td>
			<span>
				<bean:write name="currentRecord" property="oggetto" />
			</span>
		</td>
<%--	<td>
			<span>
				<bean:write name="currentRecord" property="tipoDocumento" />
			</span>
		</td>
	<td>
			<span>
				<bean:write name="currentRecord" property="dataModifica" />
			</span>
		</td>--%>
	</tr>
	</logic:iterate>
	</logic:notEmpty>
</table>
</logic:notEmpty>

</html:form>
</eprot:page>
