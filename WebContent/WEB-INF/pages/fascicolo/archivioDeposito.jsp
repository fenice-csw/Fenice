<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Archivio Deposito">

	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" /></div>

	<div class="sezione"><span class="title"> <strong>Lista Fascicoli</strong> </span> 
		<logic:notEmpty name="archivioDepositoForm" property="fascicoliCollection">
			<display:table class="simple" width="100%"
				name="sessionScope.archivioDepositoForm.fascicoliCollection" export="false"
				sort="page" pagesize="15" id="fascicolo"
				requestURI="/page/archivio_deposito.do">
				<display:column property="annoProgressivo" title="N.fascicolo" />
				<display:column property="oggetto" title="Oggetto" />
				<display:column property="dataApertura" title="Data Apertura" />
				<display:column property="dataChiusura" title="Data Chiusura" />
				<display:column property="descrizioneUfficioIntestatario" title="Ufficio" />
				<display:column property="pathTitolario" title="Classificazione" />
			</display:table>
		</logic:notEmpty> 
		<logic:empty name="archivioDepositoForm" property="fascicoliCollection">
			Nessun fascicolo presente in archivio
		</logic:empty>
	</div>
	
	<html:form action="/page/archivio_deposito.do">
		<html:submit styleClass="submit" property="btnVersamento" value="Inizia Versamento" title="Inizia Versamento nell'Archivio di Deposito" />
	</html:form>
</eprot:page>