<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />
<eprot:page title="Documento Inviato">

	<div id="protocollo-messaggi">

	<ul>
		<li>
		<strong><span>Documento Inviato con successo.</span></strong>
		</li>
	</ul>
	</div>
	<p><logic:equal name="cartelleForm" property="fromProcedimento"
		value="true">
		<input type="button" class="submit"
			onclick="location.href=this.parentNode.getElementsByTagName ('a').item(0).href"
			value="Indietro" />
		<html:link action="/page/procedimento.do" paramName="cartelleForm"
			paramId="visualizzaProcedimentoId" paramProperty="procedimentoId" />
	</logic:equal></p>
</eprot:page>




