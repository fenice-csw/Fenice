<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<logic:notEmpty name="ricercaForm" property="destinatari">
	<display:table class="simple" width="100%"
		name="sessionScope.ricercaForm.destinatari" export="false" sort="list"
		requestURI="/protocollo/ricerca/cercaDestinatario.do" pagesize="10"
		id="row">
		<display:column property="destinatario" title="Destinatario"
			href="/page/protocollo/ricerca.do" paramId="parDestinatario" />
	</display:table>
</logic:notEmpty>

<p><html:submit styleClass="submit" property="btnAnnulla"
	value="Annulla" alt="Nuova ricerca" /></p>
