<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<html:form action="/page/documentale/spostaDocumento.do">
<html:hidden name="cartelleForm" property="cartellaCorrenteId"/>
<p align="left">
<table width="100%">
 <tr>
  <td>
	<div class="folders">
	<logic:notEmpty name="cartelleForm" property="sottoCartelle">
	  <logic:iterate id="record"  name="cartelleForm" property="sottoCartelle">
	  <html:link paramId="sfogliaCartellaId" paramName="record" paramProperty="id" page="/page/documentale/spostaDocumento.do?browse=true">
	   <span><bean:write name="record" property="nome" /></span>
	  </html:link>
	  </logic:iterate>
	</logic:notEmpty>
	</div>
  </td>
 </tr>
 <tr>
  <td>
    <hr widht="100%"/>
	<html:submit styleClass="submit" property="spostaDocumento" value="Sposta qui" alt="Sposta qui" />
	
  </td>
  </tr>
</table>


</html:form>


