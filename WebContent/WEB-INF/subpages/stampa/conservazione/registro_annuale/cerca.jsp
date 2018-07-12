<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<div>
<table>
	<tr>
  	  <td colspan="6"> &nbsp; </td>
    </tr>
    <tr>
  	  <td> 
  	  	<label for="dataInizio">
			<bean:message key="report.datasingola"/><span class="obbligatorio"> * </span>:
		</label>
	  </td>
  	  <td>
  	  	<html:text property="anno" size="4" styleId="anno" styleClass="obbligatorio" maxlength="4" />
  	  </td>
    </tr>
    <tr>
    <td colspan="6">
    	<html:submit styleClass="submit" property="btnStampa" value="Stampa" alt="Stampa registro"/>
    </td>
    </tr>
</table>
</div>


