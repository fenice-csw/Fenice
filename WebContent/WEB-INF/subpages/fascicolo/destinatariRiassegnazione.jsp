<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<html:xhtml />

<jsp:include page="/WEB-INF/subpages/fascicolo/ufficio.jsp" />
<jsp:include page="/WEB-INF/subpages/fascicolo/titolario.jsp" />
<tr>
	<td class="labelEvidenziata">
		<label for="note"><bean:message key="fascicolo.referente" /><span class="obbligatorio"> * </span></label>:
	</td>
	<td>
		<logic:notEmpty name="fascicoloForm" property="utenti">
			<html:select property="utenteSelezionatoId" styleClass="evidenziato" >
				<html:option value=""></html:option>
				<html:optionsCollection property="utenti" value="id" label="caricaFullName" />
			</html:select>
		</logic:notEmpty>  
		<logic:empty name="fascicoloForm" property="utenti">
			<html:hidden property="utenteSelezionatoId" value="0"/>
		</logic:empty>
	</td>
</tr>
