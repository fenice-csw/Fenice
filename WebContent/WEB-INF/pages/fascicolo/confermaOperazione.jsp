<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione fascicolo">

    <jsp:include page="/WEB-INF/subpages/fascicolo/errori.jsp" />

<html:form action="/page/fascicolo">
<html:hidden property="id"/>

<jsp:include page="/WEB-INF/subpages/fascicolo/datiFascicolo.jsp" />
<br class="hidden" />
<br class="hidden" />
<p align="center">
<logic:equal name="fascicoloForm" property="operazione" value="Scarto" >
<label for="propostaScarto"><bean:message key="fascicolo.confermaoperazione"/></label>:
<html:select property="propostaScarto">
	<html:optionsCollection property="destinazioniScarto" value="description" label="description" />
</html:select><br /><br />


</logic:equal>

	<span>Premere il tasto Conferma per effettuare l'operazione di <strong>
		<bean:write name="fascicoloForm" property="operazione" />
	</strong> sul fascicolo selezionato</span>
<br class="hidden" />
<br class="hidden" />
</p>

<p align="center">
<html:submit styleClass="submit" property="btnOperazioni" value="Conferma" title="Modifica il fascicolo" />
<html:submit styleClass="button" property="btnAnnulla" value="Annulla" title="Annulla l'operazione" />
</p>
</html:form>
</eprot:page>
