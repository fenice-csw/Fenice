<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />
<logic:notEmpty name="listaDistribuzioneForm" property="elencoSoggettiListaDistribuzione">
	<display:table class="simple" width="100%" requestURI="allacciProtocollo.do"
		name="requestScope.allaccioProtocolloForm.allacciabili"
		export="false" sort="list" pagesize="15" id="row">
		<display:column title="Numero">
			<html:multibox property="checkAllaccio"><bean:write name="row" property="protAllacciatoId"/>_<bean:write name="row" property="numProtAllacciato"/>/<bean:write name="row" property="annoProtAllacciato"/> (<bean:write name="row" property="tipoProtocollo" />)</html:multibox>
			<bean:write name="row" property="numProtAllacciato" />
		</display:column>
		<display:column property="dataProtocollo" title="Data" />
		<display:column property="tipoProtocollo" title="Tipo" />
		<display:column property="mittente" title="Mittente" />
		<display:column property="oggetto" title="Oggetto" />
	</display:table>
	<p>
	<html:submit property="btnSelezionaAllacci" value="Seleziona" alt="Seleziona Allacci"/>
	<html:submit property="btnAnnulla" value="Annulla" alt="Annulla"/>
	</p>
</logic:notEmpty>