<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<p>
<bean:message key="protocollo.argomento.codice"/>:
<html:text property="codiceArgomento" size="20" maxlength="20"></html:text>&nbsp;&nbsp;
<bean:message key="protocollo.argomento.descrizione"/>:
<html:text property="descrizioneArgomento" size="50" maxlength="150"></html:text>&nbsp;&nbsp;
<html:submit property="btnCercaArgomento" value="Cerca" alt="Cerca in Titolario"/>
</p>
<hr />
