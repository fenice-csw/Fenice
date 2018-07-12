<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />


<eprot:page title="Procedimento Assegnatari">
	<span class="title"> 
		<strong> 
			<bean:message key="procedimento.procedimento" />
		</strong>
	</span> 
	<html:form action="/page/procedimento.do" enctype="multipart/form-data">
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
				<td class="label"><label for="numeroProcedimento"><bean:message key="procedimento.numeroProcedimento" /></label>:
				<br />
				</td>
				<td colspan="2">
					<span><strong>
						<bean:write name="procedimentoForm" property="numeroProcedimento" />
					</strong></span>
				</td>
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
			<tr>
				<td class="labelEvidenziata"><label for="interessato">Ricorrente</label>:
				</td>
			<td>
				<logic:notEmpty name="procedimentoForm" property="interessato">
					<span><strong><bean:write name="procedimentoForm" property="interessato" /></strong></span>
				</logic:notEmpty>
			</td>
		</tr>	
		</table>
		<br />
		
		<div class="sezione"><span class="title"><strong>Assegnatari</strong></span> <jsp:include
				page="/WEB-INF/subpages/procedimento/uffici_assegnatario_principale.jsp" />
		</div>
		<logic:notEmpty name="procedimentoForm" property="assegnatarioPrincipale">
			<span>Assegnatario: <strong>
    			<bean:write name="procedimentoForm" property="assegnatarioPrincipale.descrizioneUfficio"/>
    			<bean:write name="procedimentoForm" property="assegnatarioPrincipale.nomeUtente"/>
    			</strong>
    		</span>
<br />
</logic:notEmpty>
		<html:submit styleClass="submit" property="annullaOperazioneAction" value="Indietro" title="Indietro " />
		<html:submit styleClass="submit" property="confermaRiassegnazioneAction" value="Riassegna" title="Conferma Riassegnazione Assegnazione"/>
	
	</html:form>
</eprot:page>
