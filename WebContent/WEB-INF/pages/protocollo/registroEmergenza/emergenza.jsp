<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />
<eprot:page title="Registro d'emergenza - prenota protocolli">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriProtocollo" /></div>
	<html:form action="/page/protocollo/emergenza">
	
	<div class="sezione">

		<span class="title">
			<strong>Apri una nuova sessione d'emergenza</strong>
		</span>
		
		<table summary="">
			<tr>
				<td class="label"><label title="Data Registrazione"> <bean:message
					key="protocollo.dataregistrazione" /></label>&nbsp;:</td>
				<td><html:hidden property="dataRegistrazioneProtocollo" /> <span><bean:write
					name="registroEmergenzaForm" property="dataRegistrazioneProtocollo" /></span></td>
			</tr>
			<tr>
				<td class="label"><label title="Numero di protocolli da prenotare"
					for="numeroProtocolliIngresso"> <bean:message
					key="protocollo.numeroemergenza" /></label>&nbsp;:</td>
				<td align="left"><html:text styleClass="text" styleId="numeroProtocolliIngresso"
					property="numeroProtocolliIngresso" size="10" maxlength="10" /></td>
			</tr>
			
			<tr style="display:none;">
				<td class="label"><label title="Numero di protocolli in uscita(dovrebbe essere nascosto)"
					for="numeroProtocolliUscita"> <bean:message
					key="protocollo.numeroprotocolliuscita" /></label>&nbsp;:</td>
				<td align="left"><html:text styleClass="text" styleId="numeroProtocolliUscita"
					property="numeroProtocolliUscita" size="10" maxlength="10" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td align="left"><html:submit styleClass="submit"
					property="salvaEmergenzaAction" value="Salva Emergenza" alt="Salva" />
				</td>
			</tr>
		</table>
		
	</div>
		
		<br class="hidden" />
		
		<div>
    		<jsp:include page="/WEB-INF/subpages/protocollo/registroEmergenza/listaView.jsp" />
		</div>

	</html:form>
	
	
</eprot:page>

