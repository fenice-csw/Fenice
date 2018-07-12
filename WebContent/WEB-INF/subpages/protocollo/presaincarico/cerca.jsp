<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<table>
  <tr>
  <td align="left">
		<span><bean:message key="protocollo.numero_anno"/></span>
		<label for="numeroProtocolloDa">
			Cerca numero protocollo
		</label>
		<html:text property="numeroProtocolloDa" styleId="numeroProtocolloDa" size="8" maxlength="10" />
    </td>   
    <td align="right">
	    <html:submit styleClass="submit" property="btnCerca" value="Cerca" alt="Cerca protocolli assegnati"/>&nbsp;&nbsp;
    </td>  
  </tr>	
</table>
