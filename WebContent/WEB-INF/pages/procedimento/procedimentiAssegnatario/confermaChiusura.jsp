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
	<html:form action="/page/procedimento/scarico.do" enctype="multipart/form-data">
		<table summary="">
			<tr>
				<td class="label"><label title="Data Avvio" for="dataAvvio"><bean:message key="procedimento.data.avvio" /></label>:</td>
				<td colspan="2">
					<strong>
						<bean:write name="alertProcedimentoForm" property="dataAvvio" />
					</strong>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="numProcedimento"><bean:message key="procedimento.numeroProcedimento" /></label>:
				<br />
				</td>
				<td colspan="2">
					<span><strong>
						<bean:write name="alertProcedimentoForm" property="numProcedimento" />
					</strong></span>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="responsabile"><bean:message key="procedimento.responsabile" /></label>:
				<br />
				</td>
				<td colspan="2">
					<span><strong>
						<bean:write name="alertProcedimentoForm" property="responsabile" />
					</strong></span>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="oggettoProcedimento"><bean:message key="procedimento.oggetto" /></label>:
				<br />
				</td>
				<td colspan="2">
					<span><strong>
						<bean:write name="alertProcedimentoForm" property="oggettoProcedimento" />
					</strong></span>
				</td>
			</tr>
		</table>
		<br />
		<span>
			<strong>
				Vuoi cancellare il procedimento?
			</strong>
		</span>
		<html:submit styleClass="button" property="annullaAction" value="Annulla" title="Annulla Chiusura Assegnazione" />
		<logic:equal name="alertProcedimentoForm" property="istruttore" value="true">
			<html:submit styleClass="submit" property="confermaChiusuraIstruttoriaAction" value="Ok" title="Conferma Chiusura Assegnazione"/>
		</logic:equal>
		<logic:equal name="alertProcedimentoForm" property="istruttore" value="false">
			<html:submit styleClass="submit" property="confermaChiusuraAction" value="Conferma" title="Conferma Chiusura Assegnazione"/>
		</logic:equal>
	</html:form>
</eprot:page>
