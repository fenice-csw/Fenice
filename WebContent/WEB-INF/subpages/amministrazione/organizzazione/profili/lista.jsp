<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<div>  
<logic:notEmpty name="profiloForm" property="profili">
<ul>
<span> 
<strong><bean:message key="profilo.profiloutente"/>:</strong></span>
<br />
<logic:iterate id="currentRecord" property="profili" name="profiloForm">
	<li>
	<html:link action="/page/amministrazione/profilo.do" paramId="parId" paramName="currentRecord" paramProperty="id">
	<span><bean:write name="currentRecord" property="descrizioneProfilo"/></span>
	</html:link >
	</li>
</logic:iterate>

</ul>
<br/>
</logic:notEmpty>    
&nbsp;&nbsp;<html:submit styleClass="submit" property="btnNuovo" value="Nuovo" alt="Inserisce un nuovo profilo"/>
</div>
<hr />

