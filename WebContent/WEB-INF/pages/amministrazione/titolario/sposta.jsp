<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Titolario">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriAmministrazione" />
</div>

<html:form action="/page/amministrazione/spostaTitolario.do">
<table summary="">

  <tr>
    <td class="label">
      <span><bean:message key="protocollo.argomento.argomentopadre"/>:</span>
    </td>
    <td colspan="2">
      <span><strong>
        <bean:write name="titolarioForm" property="parentPath" /> - <bean:write name="titolarioForm" property="parentDescrizione" />
		<html:hidden property="parentPath"/>
		<html:hidden property="parentId"/>
      </strong></span>
    </td>
  </tr>


	<tr>  
		<td class="label">
			<label for="codice"><bean:message key="protocollo.argomento.codice"/></label>:
		</td>  
		<td>
			<html:hidden property="id"/>
			<span><strong><bean:write name="titolarioForm" property="codice" /></strong></span>
		</td>  
	</tr>
	<tr>  
    	<td class="label">
      		<label for="descrizione"><bean:message key="protocollo.argomento.descrizione"/></label>:
    	</td>  
    	<td>
			<span><strong><bean:write name="titolarioForm" property="descrizione" /></strong></span>
    	</td>
    	 
	</tr>
</table>
<div class="sezione">
<span class="title"><strong><bean:message key="protocollo.argomento.sposta"/></strong><br/></span>
	<jsp:include page="/WEB-INF/subpages/amministrazione/titolario/spostaTitolario.jsp" />
</div>
<html:submit styleClass="submit" property="btnConfermaSposta" value="Sposta" title="Sposta la voce selezionata" />
<html:submit styleClass="submit" property="btnAnnullaSposta" value="Annulla" title="Annulla l'operazione" />

</html:form>
</eprot:page>
