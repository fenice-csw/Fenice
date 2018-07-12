<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<div>
<table class="centrale">


  <tr>  
   <!-- <td align="left">
      <bean:message key="protocollo.label"/>:
      <html:text property="annotazioneProtocolloId" size="10" maxlength="10"></html:text>
    </td>
    -->
    
    <td align="left"> 
      <bean:message key="protocollo.annotazioni.note"/>:   
      <html:text property="noteAnnotazione" size="40" maxlength="200"></html:text>
    </td>
        <td align="left"> 
    <html:submit property="btnInserisciAnnotazione" value="Inserisci Annotazione" alt="Inserisci Annotazione"/>
        </td>
    <td align="right">
    <html:submit property="btnAnnotazioni" value="Lista Annotazioni" alt="Elenco Annotazioni"/>
    </td>     
   </tr>
</table>
</div>