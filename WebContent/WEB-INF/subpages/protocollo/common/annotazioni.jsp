<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<table summary="">
<!-- 
  <tr>  
    <td class="label">
      <label for="posizioneAnnotazione"><bean:message key="protocollo.annotazioni.posizione"/>:</label>
    </td>  
    <td>
      <html:text property="posizioneAnnotazione" styleId="posizioneAnnotazione" size="10" maxlength="10"></html:text>
<logic:greaterThan value="0" name="protocolloForm" property="protocolloId">
      <html:submit styleClass="button" property="btnAnnotazioni" value="Annotazioni" title="Gestione delle annotazioni" />
</logic:greaterThan>
    </td>
  </tr>
  <tr>  
    <td class="label">
      <label for="chiaveAnnotazione"><bean:message key="protocollo.annotazioni.chiave"/>:</label>
    </td>  
    <td>
      <html:text property="chiaveAnnotazione" styleId="chiaveAnnotazione" size="50" maxlength="255"></html:text>&nbsp;
    </td>
  </tr>
  -->
  <tr>  
    <td class="label">
      <label for="descrizioneAnnotazione"><bean:message key="protocollo.annotazioni.note"/>:</label>
    </td>  
    <td>
      <html:textarea property="descrizioneAnnotazione" styleId="descrizioneAnnotazione" cols="50" rows="3"></html:textarea>
    </td>  
  </tr>
</table>