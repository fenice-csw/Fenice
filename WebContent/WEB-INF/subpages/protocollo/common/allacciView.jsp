<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<logic:notEmpty name="protocolloForm" property="protocolliAllacciati">
<p>

<logic:equal name="protocolloForm" property="flagTipo" value="I">
  <logic:iterate id="currentRecord" property="protocolliAllacciati" name="protocolloForm">
	  <html:link action="/page/protocollo/ingresso/documento.do" paramName="currentRecord" paramId="visualizzaProtocolloAllacciatoViewId" paramProperty="protocolloAllacciatoId">
	  	<span><bean:write name="currentRecord" property="allaccioDescrizione" /></span>&nbsp;&nbsp;
	  </html:link>
  </logic:iterate>
</logic:equal>

<logic:equal name="protocolloForm" property="flagTipo" value="U">
  <logic:iterate id="currentRecord" property="protocolliAllacciati" name="protocolloForm">
	  <html:link action="/page/protocollo/uscita/documento.do" paramName="currentRecord" paramId="visualizzaProtocolloAllacciatoViewId" paramProperty="protocolloAllacciatoId">
	  	<span><bean:write name="currentRecord" property="allaccioDescrizione" /></span>&nbsp;&nbsp;
	  </html:link>
  </logic:iterate>
</logic:equal>

<logic:equal name="protocolloForm" property="flagTipo" value="P">
  <logic:iterate id="currentRecord" property="protocolliAllacciati" name="protocolloForm">
	  <html:link action="/page/protocollo/posta_interna/documento.do" paramName="currentRecord" paramId="visualizzaProtocolloAllacciatoViewId" paramProperty="protocolloAllacciatoId">
	  	<span><bean:write name="currentRecord" property="allaccioDescrizione" /></span>&nbsp;&nbsp;
	  </html:link>
  </logic:iterate>
</logic:equal>

</p>

</logic:notEmpty>
