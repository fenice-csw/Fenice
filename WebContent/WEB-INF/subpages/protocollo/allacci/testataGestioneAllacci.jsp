<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<p>
  <html:hidden property="allaccioProtocolloId"/>
  <label for="allaccioNumProtocollo">
  	<bean:message key="protocollo.label"/>:
  </label>
 	<html:text property="allaccioNumProtocollo" styleId="allaccioNumProtocollo" size="8" maxlength="10"></html:text>
  &nbsp;
  <label for="allaccioAnnoProtocollo">
    <bean:message key="protocollo.anno"/>:
  </label>
  <html:text property="allaccioAnnoProtocollo" styleId="allaccioAnnoProtocollo" size="5" maxlength="4"></html:text>
  &nbsp;
  <html:submit styleClass="button" property="allacciaProtocolloAction" value="Allaccia" title="Allaccia il Protocollo specificato"/>
</p>
