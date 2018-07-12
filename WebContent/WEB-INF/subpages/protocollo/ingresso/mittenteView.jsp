<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />



<html:xhtml />

<bean:define id="tipoMittente" name="protocolloForm" property="mittente.tipo" />

 <logic:notEqual name="tipoMittente" value="M">
<table summary="">
  <tr>
	<td class="label">
		<span><bean:message key="protocollo.mittente.protocolloid"/>&nbsp;:</span>
	</td>
	<td>
		<span><strong>
			<bean:write name="protocolloForm" property="numProtocolloMittente" />
		</strong></span>
	</td>
	 <td class="label">
      	<span title="Data del documento"><bean:message key="protocollo.documento.data"/></span>&nbsp;:
    </td>
    <td>
    	<span><strong>
    	<bean:write name="protocolloForm" property="dataProtocolloMittente" />
    	</strong></span>&nbsp;
    </td>
  </tr>

  <logic:equal name="tipoMittente" value="F">
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.mittente.cognome" />&nbsp;:</span>
    </td>
    <td><span><strong>
		<bean:write name="protocolloForm" property="mittente.cognome"/>
    	</strong></span></td>
  </tr>
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.mittente.nome"/>&nbsp;:</span>
    </td>
    <td><span><strong>
		<bean:write name="protocolloForm" property="mittente.nome"/>
    	</strong></span></td>
  </tr>
  </logic:equal>
  <logic:equal name="tipoMittente" value="G">	
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.mittente.denominazione" />&nbsp;:</span>
    </td>
    <td><span><strong>
		<bean:write name="protocolloForm" property="mittente.descrizioneDitta"/>
    	</strong></span></td>
	</tr>
  </logic:equal>

  <tr>
    <td class="label">
      <span><bean:message key="soggetto.indirizzo" />&nbsp;:</span>
    </td>
    <td><span><strong>
		<bean:write name="protocolloForm" property="mittente.indirizzoNumCivico"/>
    	</strong></span></td>
  </tr>
  <tr>
    <td class="label">
      <span><bean:message key="soggetto.localita" />&nbsp;:</span>
    </td>
    <td><span><strong>
		<bean:write name="protocolloForm" property="mittente.indirizzo.comune"/>
    	</strong></span></td>
  </tr>
  <tr>
    <td class="label">
      <span title="Codice di Avviamento Postale"><bean:message key="soggetto.cap" />&nbsp;:</span>
    </td>
    <td>
    	<span><strong>
			<bean:write name="protocolloForm" property="mittente.indirizzo.cap"/>
    		&nbsp;&nbsp;</strong></span>
			<span><bean:message key="soggetto.provincia" />&nbsp;:</span>
        	<span><strong><html:select disabled="true" property="mittente.indirizzo.provinciaId">
				<html:optionsCollection property="province" value="provinciaId" label="descrizioneProvincia" />
				</html:select>
			</strong></span>
	</td>
  </tr>
</table>
</logic:notEqual>

  <logic:equal name="tipoMittente" value="M">
		<logic:notEqual name="protocolloForm" property="versioneDefault" value="true">
	<jsp:include page="/WEB-INF/subpages/protocollo/common/multiStoriaMittentiView.jsp" />
</logic:notEqual>

<logic:equal name="protocolloForm" property="versioneDefault" value="true">
	
	<jsp:include page="/WEB-INF/subpages/protocollo/common/multiMittentiView.jsp" />
</logic:equal>
  </logic:equal>
