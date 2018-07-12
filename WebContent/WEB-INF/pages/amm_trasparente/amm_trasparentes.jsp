<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Lista Sezioni Amministrazione Trasparente">

	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" /></div>

	<html:form action="/page/amm_trasparente">
		<div class="sezione"><span class="title"> <strong>Nuovo</strong>
		</span>
		<table summary="">
			<tr>
				<td class="label"><label for="descrizione"><bean:message
					key="sezione.descrizione" /></label> <span class="obbligatorio">
				* </span>:</td>
				<td><html:text property="descrizione" disabled="false"
					size="60" maxlength="150">
				</html:text></td>
			</tr>
			<tr>
 				<td>
 					<label for="flagWebNo">Visibilità Web</label>:
 				</td>
 				<td>
 					<table>
 						<tr>
 							<td>
 								<html:radio property="flagWeb" styleId="flagWebNo" value="0">
									<label for="flagWebNo"><bean:message key="amministrazione.no"/></label>&nbsp;&nbsp;
								</html:radio>
 							</td>
 							<td>
 								<html:radio property="flagWeb" styleId="flagWebSi" value="1">
									<label for="flagWebSi"><bean:message key="amministrazione.si"/></label>
								</html:radio><br />
							</td>
						</tr>
 					</table>
 				</td>
 			</tr>
			<tr>
				<td></td>
				<td>
				<div class="sezione"><span class="title"> <strong>Responsabile
				della Sezione</strong> </span> <jsp:include
					page="/WEB-INF/subpages/amm_trasparente/responsabile.jsp" /></div>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><html:submit styleClass="submit" property="btnConferma"
					value="Salva" title="Salva la Sezione" /></td>
			</tr>
		</table>
		</div>
	</html:form>

	<div class="sezione"><span class="title"> <strong>Lista
	sezioni</strong> </span> <logic:notEmpty name="sezioniForm" property="sezioni">
		<display:table class="simple" width="100%"
			name="sessionScope.sezioniForm.sezioni" export="false"
			sort="page" pagesize="15" id="sezione"
			requestURI="/page/amm_trasparente.do">
			
			<display:column title="ID">
				<html:link action="/page/amm_trasparentes.do" paramName="sezione"
					paramId="modificaSezione" paramProperty="sezioneId">
					<bean:write name="sezione" property="sezioneId" />
				</html:link>
			</display:column>
			<display:column property="descrizione" title="Descrizione" />
			<display:column property="nomeResponsabile" title="Responsabile" />
			<display:column title="Visibilità Web">
				<logic:equal name="sezione" property="flagWeb" value="1">
 					SI
 				</logic:equal>
 				<logic:equal name="sezione" property="flagWeb" value="0">
 					NO
 				</logic:equal>
			</display:column>
		</display:table>
	</logic:notEmpty> <logic:empty name="sezioniForm" property="sezioni">
			Nessuna sezione presente nel sistema
		</logic:empty></div>
</eprot:page>