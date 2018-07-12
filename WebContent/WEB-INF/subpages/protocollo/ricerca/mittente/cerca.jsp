<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<p><label for="mittente"><bean:message key="protocollo.mittente"/>:</label>
<html:text property="mittente" styleId="mittente" size="30" maxlength="100" />
<html:submit property="btnCercaMittente" value="Cerca" alt="Cerca" />
</p>

