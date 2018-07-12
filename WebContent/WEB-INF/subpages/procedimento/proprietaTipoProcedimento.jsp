<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />



	<tr>
		<td class="label"><bean:message
			key="protocollo.argomento" /> :</td>
		<td><bean:write name="procedimentoForm" property="tipoProcedimento.titolario" /></td>
	</tr>
	
		<tr>
			<td class="label">Numero di giorni (massimo) :</td>
		<logic:equal name="procedimentoForm" property="procedimentoId" value="0">
			<td>
				<html:text property="giorniMax" size="6"></html:text>
			</td>
		</logic:equal>
		<logic:notEqual name="procedimentoForm" property="procedimentoId" value="0">
			<td>
				<bean:write name="procedimentoForm" property="giorniMax"/>
			</td>
		</logic:notEqual>
		</tr>
	
	<tr>
		<td class="label">Numero di giorni (alert) :</td>
		<td>
			<html:text property="giorniAlert" size="6"></html:text>
		</td>
	</tr>
	
	<tr>
		<td class="label">Amministrazioni Partecipanti :</td>
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
						<td><span> <bean:write name="currentRecord" property="denominazione" />
						</span></td>
					</tr>
				</logic:iterate>
			</table>
		</p>
	</div>
		</logic:notEmpty>
		</td>
	</tr>
	<jsp:include page="/WEB-INF/subpages/procedimento/riferimentiLegislativi.jsp" />
	<tr>
		<td class="label">Uffici Partecipanti :</td>
		<td><logic:notEmpty name="procedimentoForm"
			property="ufficiPartecipanti">
			<div class="sezione">
				<p>
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
							    		<html:multibox name="procedimentoForm" property="visibilitaUfficiPartecipantiId">
							    			<bean:write name="ass" property="ufficioId"/>
							    		</html:multibox>
							    	</td>
							  </tr> 
							</logic:iterate>
					</table>
				</p>
			</div>
		</logic:notEmpty>
		</td>
	</tr>