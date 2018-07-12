<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />



	<tr>
		<td class="label"><bean:message
			key="protocollo.argomento.massimario" /> :</td>
		<td><bean:write name="fascicoloForm"
			property="titolario.massimario" /></td>
	</tr>
	<tr>
		<td class="label">Numero di giorni (massimo) :</td>
		<td>
			<html:text property="giorniMax" size="6"></html:text>
		</td>
	</tr>
	<tr>
		<td class="label">Numero di giorni (alert) :</td>
		<td>
			<html:text property="giorniAlert" size="6"></html:text>
		</td>
	</tr>
	<!--  
	<tr>
		<td class="label">Responsabile del Procedimento :</td>
		<td><bean:write name="fascicoloForm"
			property="titolario.nomeResponsabile" /></td>
	</tr>
	-->
	<tr>
		<td class="label">Amministrazioni Partecipanti :</td>
		<td><logic:notEmpty name="fascicoloForm"
			property="titolario.amministrazioni">

			<table border="1">
				<tr>
					<th>Denominazione</th>
				</tr>
				<logic:iterate id="currentRecord" name="fascicoloForm"
					property="titolario.amministrazioni">
					<tr>
						<td><span> <bean:write name="currentRecord"
							property="destinatario" />
						</span></td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		</td>
	</tr>
	<jsp:include page="/WEB-INF/subpages/fascicolo/riferimentiLegislativi.jsp" />