<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />



	<tr>
		<td class="label"><bean:message
			key="protocollo.argomento" />:</td>
		<td><strong><bean:write name="procedimentoForm" property="tipoProcedimento.titolario" /></strong></td>
	</tr>
	
		<tr>
			<td class="label">Numero di giorni (massimo):</td>
			<td>
				<strong><bean:write name="procedimentoForm" property="giorniMax"/></strong>
			</td>
		</tr>
	
	<tr>
		<td class="label">Numero di giorni (alert):</td>
		<td>
			<strong><bean:write name="procedimentoForm" property="giorniAlert"/></strong>
		</td>
	</tr>
	
	<tr>
		<td class="label">Amministrazioni Partecipanti:</td>
		<td><logic:notEmpty name="procedimentoForm"
			property="tipoProcedimento.amministrazioni">
	<div class="sezione">
		<p>
			<table id="tabella_mittenti">
				<tr>
					<th>Denominazione</th>
				</tr>
				<logic:iterate id="currentRecord" name="procedimentoForm"
					property="tipoProcedimento.amministrazioni">
					<tr>
						<td><span>
						<strong> 
							<bean:write name="currentRecord" property="denominazione" />
						</strong>
						</span></td>
					</tr>
				</logic:iterate>
			</table>
		</p>
	</div>
		</logic:notEmpty>
		</td>
	</tr>
	<jsp:include page="/WEB-INF/subpages/procedimento/riferimentiLegislativiView.jsp" />
	<tr>
		<td class="label">Uffici Partecipanti :</td>
		<td><logic:notEmpty name="procedimentoForm"
			property="ufficiPartecipanti">
			<table id="tabella_mittenti">
				<tr>
			   		<th><span><bean:message key="protocollo.assegnatari.ufficio"/></span></th>
			   		<th><span>Visibilt&agrave; completa</span></th>
			  	</tr>
					<logic:iterate id="ass" name="procedimentoForm" property="ufficiPartecipantiValues">
					  	<tr>    
					    	<td width="65%">
					    		<span><bean:write name="ass" property="nomeUfficio"/></span>
					    	</td>
					    	<td>
					    		<html:multibox name="procedimentoForm" property="visibilitaUfficiPartecipantiId" disabled="true">
					    			<bean:write name="ass" property="ufficioId"/>
					    		</html:multibox>
					    	</td>
					  </tr> 
					</logic:iterate>
			</table>
		</logic:notEmpty>
		</td>
	</tr>