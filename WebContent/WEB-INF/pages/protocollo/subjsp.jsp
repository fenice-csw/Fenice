<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<div id="corpo">
<table class="centrale">
  <tr>
    <td align="right" colspan="3">
      <bean:message key="protocollo.mittente.protocolloid"/>:
    </td>
    <td align="left" colspan="3">
      <html:text property="mittenteId" size="10" maxlength="10"></html:text>
    </td>
  </tr>
</table>
</div>