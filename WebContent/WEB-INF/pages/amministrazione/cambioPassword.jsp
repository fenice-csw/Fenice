<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Cambio Password">
<div id="protocollo-errori">
<html:errors bundle="bundleErroriAmministrazione" />
</div>


<html:form action="/cambioPwd.do">
<table>
	<tr>
	    <th align="right">
	      <label for="oldPassword"><bean:message key="prompt.old.password"/></label>:
	    </th>
	    <td>
	      <html:password styleId="oldPassword" property="oldPassword" size="18" maxlength="20" />
	    </td>
	</tr>
		<tr>
	    <th align="right">
	      <label for="newPassword"><bean:message key="prompt.nuova.password"/></label>:
	    </th>
	    <td>
	      <html:password styleId="newPassword" property="newPassword" size="18" maxlength="20" />
	    </td>
	</tr>
  <tr>
    <th align="right">
      <label for="confirmNewPassword"><bean:message key="prompt.nuova.confirmPassword"/></label>:
    </th>
    <td>
      <html:password styleId="confirmNewPassword" property="confirmNewPassword" size="18" maxlength="20"  />
    </td>
  </tr>
  <tr><td> </td></tr>
  <tr>
    <td> </td>
    <td>
      <html:submit styleClass="submit" property="btnConferma" value="Conferma"/>
      <html:reset styleClass="submit" />
    </td>
  </tr>
	
</table>
</html:form>
</eprot:page>
