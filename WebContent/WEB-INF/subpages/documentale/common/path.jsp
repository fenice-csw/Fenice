<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<table width="100%" >
<tr>
<td colspan="2">
</td>
</tr>
 <tr>
  <td widht="100%" align="left" style="border-style: solid; border-width: 1px; border-color: #999999; background-color: #DDDDDD">
	<span><bean:message key="documentale.indirizzo"/>:&nbsp;</span>
	<logic:notEmpty name="cartelleForm" property="pathCartella">
	<logic:iterate id="record"  name="cartelleForm" property="pathCartella">
	  /
		  <html:link paramId="gotoCartellaId" paramName="record" paramProperty="id" page="/page/documentale/cartelle.do?browse=true">
		   <span><bean:write name="record" property="nome" /></span>
		  </html:link>
	</logic:iterate>
	</logic:notEmpty>
	
  </td>
  <td widht="1%" align="right">
	<html:submit styleClass="submit" property="nuovaCartella" value="Nuova Cartella" alt="Nuova Cartella" />
	<html:submit styleClass="submit" property="rinominaCartella" value="Rinomina" alt="Rinomina Cartella" />
	<html:submit styleClass="submit" property="eliminaCartella" value="Elimina" alt="Elimina Cartella" />
  </td>
 </tr>
</table>
<hr widht="100%"/>

