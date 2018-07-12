<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<html:xhtml />
<logic:notEmpty name="reportStatisticheForm" property="statistiche">
<div>
	<display:table class="simple" width="100%"
		name="requestScope.reportStatisticheForm.statistiche"
		requestURI="/page/stampa/prn-pro/statistiche.do" export="false"
		sort="list" pagesize="20" id="row">
		<display:column  title="Ufficio" property="ufficio" />
		<display:column property="utente" title="Utente" />
		<display:column title="Totale">
			<logic:greaterThan name="row" property="numProt" value="0" >
				<a href="statistiche.do?
				dataInizio=<bean:write name="reportStatisticheForm" property="dataInizio"/>&amp;
				dataFine=<bean:write name="reportStatisticheForm" property="dataFine"/>&amp;
				utenteId=<bean:write name="row" property="utenteId"/>&amp;
				ufficioId=<bean:write name="row" property="ufficioId"/>
				">
				<bean:write name="row" property="numProt" /></a>
			</logic:greaterThan>	
		</display:column>
		<display:column title="Sospesi">
			<logic:greaterThan name="row" property="numProtSospesi" value="0" >
				<a href="statistiche.do?
				dataInizio=<bean:write name="reportStatisticheForm" property="dataInizio"/>&amp;
				dataFine=<bean:write name="reportStatisticheForm" property="dataFine"/>&amp;
				utenteId=<bean:write name="row" property="utenteId"/>&amp;
				ufficioId=<bean:write name="row" property="ufficioId"/>&amp;
				statoProtocollo=S">
					<bean:write name="row" property="numProtSospesi" />
				</a>
			</logic:greaterThan>	
		</display:column>
		<display:column title="in Lavorazione">
			<logic:greaterThan name="row" property="numProtLavorazione" value="0" >
				<a href="statistiche.do?
				dataInizio=<bean:write name="reportStatisticheForm" property="dataInizio"/>&amp;
				dataFine=<bean:write name="reportStatisticheForm" property="dataFine"/>&amp;
				utenteId=<bean:write name="row" property="utenteId"/>&amp;
				ufficioId=<bean:write name="row" property="ufficioId"/>&amp;
				statoProtocollo=N">
					<bean:write name="row" property="numProtLavorazione" />
				</a>
			</logic:greaterThan>	
		</display:column>
		<display:column title="agli Atti">
			<logic:greaterThan name="row" property="numProtAtti" value="0" >
				<a href="statistiche.do?
				dataInizio=<bean:write name="reportStatisticheForm" property="dataInizio"/>&amp;
				dataFine=<bean:write name="reportStatisticheForm" property="dataFine"/>&amp;
				utenteId=<bean:write name="row" property="utenteId"/>&amp;
				ufficioId=<bean:write name="row" property="ufficioId"/>&amp;
				statoProtocollo=A">
					<bean:write name="row" property="numProtAtti" />
				</a>
			</logic:greaterThan>	
		</display:column>
		<display:column title="in Risposta">
			<logic:greaterThan name="row" property="numProtRisposta" value="0" >
				<a href="statistiche.do?
				dataInizio=<bean:write name="reportStatisticheForm" property="dataInizio"/>&amp;
				dataFine=<bean:write name="reportStatisticheForm" property="dataFine"/>&amp;
				utenteId=<bean:write name="row" property="utenteId"/>&amp;
				ufficioId=<bean:write name="row" property="ufficioId"/>&amp;
				statoProtocollo=R">
					<bean:write name="row" property="numProtRisposta" />
				</a>
			</logic:greaterThan>	
		</display:column>
		<display:column title="Annullati">
			<logic:greaterThan name="row" property="numProtAnnullati" value="0" >
				<a href="statistiche.do?
				dataInizio=<bean:write name="reportStatisticheForm" property="dataInizio"/>&amp;
				dataFine=<bean:write name="reportStatisticheForm" property="dataFine"/>&amp;
				utenteId=<bean:write name="row" property="utenteId"/>&amp;
				ufficioId=<bean:write name="row" property="ufficioId"/>&amp;
				statoProtocollo=C">
					<bean:write name="row" property="numProtAnnullati" />
				</a>
			</logic:greaterThan>	
		</display:column>
		<display:column title="Rifiutati">
			<logic:greaterThan name="row" property="numProtRifiutati" value="0" >
				<a href="statistiche.do?
				dataInizio=<bean:write name="reportStatisticheForm" property="dataInizio"/>&amp;
				dataFine=<bean:write name="reportStatisticheForm" property="dataFine"/>&amp;
				utenteId=<bean:write name="row" property="utenteId"/>&amp;
				ufficioId=<bean:write name="row" property="ufficioId"/>&amp;
				statoProtocollo=F">
					<bean:write name="row" property="numProtRifiutati" />
				</a>
			</logic:greaterThan>	
		</display:column>
		<display:column title="Procedimento">
			<logic:greaterThan name="row" property="numProtProcedimento" value="0" >
				<a href="statistiche.do?
				dataInizio=<bean:write name="reportStatisticheForm" property="dataInizio"/>&amp;
				dataFine=<bean:write name="reportStatisticheForm" property="dataFine"/>&amp;
				utenteId=<bean:write name="row" property="utenteId"/>&amp;
				ufficioId=<bean:write name="row" property="ufficioId"/>&amp;
				statoProtocollo=P">
					<bean:write name="row" property="numProtProcedimento" />
				</a>
			</logic:greaterThan>	
		</display:column>

	</display:table>
</div>
</logic:notEmpty>
