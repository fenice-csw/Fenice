<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />


<eprot:page title="Stampa Comunicazione Avvio">
	<div class="sezione" styleClass="obbligatorio">
	<span class="title"> <strong> <bean:message
		key="procedimento.procedimento" /></strong></span> 
		<html:form action="/page/procedimento.do" enctype="multipart/form-data">
		<div id="protocollo-errori"><html:errors
			bundle="bundleErroriProtocollo" /></div>
		<logic:messagesPresent message="true">
			<div id="protocollo-messaggi">
			<ul>
				<html:messages id="actionMessage" message="true"
					bundle="bundleErroriProtocollo">
					<li><bean:write name="actionMessage" /></li>
				</html:messages>
			</ul>
			</div>
		</logic:messagesPresent>
		
		<table summary="">
			<tr>
				<td class="label"><label title="Data Avvio" for="dataAvvio"><bean:message key="procedimento.data.avvio" /></label>:</td>
				<td colspan="2">
					<strong>
						<bean:write name="procedimentoForm" property="dataAvvio" />
					</strong>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="numProcedimento"><bean:message key="procedimento.numeroProcedimento" /></label>:
				<br />
				</td>
				<td colspan="2">
					<span><strong>
						<bean:write name="procedimentoForm" property="numeroProcedimento" />
					</strong></span>
				</td>
			</tr>
			<tr>
		<td class="label"><label for="responsabile"><bean:message
			key="procedimento.responsabile" /></label> <span class="obbligatorio">*</span>:
		</td>
		<td><span><strong> <bean:write
			name="procedimentoForm" property="responsabile" /> </strong></span></td>
	</tr>
			<tr>
				<td class="label"><label for="oggettoProcedimento"><bean:message key="procedimento.oggetto" /></label>:
				<br />
				</td>
				<td colspan="2">
					<span><strong>
						<bean:write name="procedimentoForm" property="oggettoProcedimento" />
					</strong></span>
				</td>
			</tr>
		</table>
		<br>
		<br>
		<span>Vuoi stampare la lettera di comunicazione avvio?</span>
				<html:submit styleClass="submit" property="stampaComunicazioneAvvioAction" value="Stampa" title="Stampa la lettera di comunicazione avvio" />
				<br><br>
				<html:submit styleClass="submit" property="tornaProtocolloAction" value="Torna al protocollo" title="Torna al protocollo " />
	</html:form>
	</div>
</eprot:page>
