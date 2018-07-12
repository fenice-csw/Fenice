<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Dashboard">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" /></div>

	<div align="center"><br>
	<br>
	<br>
	<div id="zona_lavoro">
		<div id="titolo_zonalavoro" align="left"><strong>Zona lavoro</strong></div>
		<div id="corpo_zonalavoro" align="left">
			<ul>
				<li><html:link href="page/amministrazione/organizzazione/aoo/aoo.do">Gestisci le Aree Organizzative Omogenee</html:link></li>
				<li><html:link href="page/amministrazione.do">Modifica i dati dell'Amministrazione</html:link></li>
				<li><html:link href="gestisciAOO.do?ricaricaOrganizzazioneAction=true">Ricarica organizzazione</html:link></li>
				<li><html:link href="page/log.do">LOG</html:link></li>
			</ul>
		</div>
			
	</div>
	<%--<table border="0" cellpadding="0" cellspacing="0" bordercolor="#006666">
		<tr>
			<td>
			<div align="left"><strong>Zona lavoro</strong></div>
			</td>
			<td>&nbsp;</td>
			<td>
			<div align="right"></div>
			</td>
		</tr>
		<tr>
			<td><html:link
				href="page/amministrazione/organizzazione/aoo/aoo.do">Gestisci le Aree Organizzative Omogenee</html:link></td>
		</tr>
		<tr>
			<td><html:link
				href="page/amministrazione.do">Modifica i dati dell'Amministrazione</html:link></td>
		</tr>
		<tr>
			<td><html:link
				href="gestisciAOO.do?ricaricaOrganizzazioneAction=true">Ricarica organizzazione</html:link></td>
		</tr>
	</table>--%>
	</div>
</eprot:page>
