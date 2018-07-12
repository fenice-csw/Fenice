<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />
<eprot:page title="Cruscotto-Struttura">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriProtocollo" /></div>
	<br />
	<html:form action="/page/cruscotto/amministrazione.do">

			<jsp:include page="/WEB-INF/subpages/cruscotto/uffici.jsp" />
	
			<br class="hidden" />
	
			<logic:notEmpty name="cruscottoForm" property="sottoposto">
				<div class="sezione">
					<logic:equal name="cruscottoForm" property="sottoposto.utenteId" value="0">
					<span class="title"> 
						<strong>Protocolli associati all'ufficio <span class="evidenziato"><bean:write name="cruscottoForm" property="sottoposto.descrizioneUfficio"/></span> </strong>
					</span> 
					</logic:equal>
					<logic:notEqual name="cruscottoForm" property="sottoposto.utenteId" value="0">
					<span class="title"> 
						<strong>Protocolli associati all'utente <span class="evidenziato"><bean:write name="cruscottoForm" property="sottoposto.nomeUtente"/></span> </strong>
					</span> 
					</logic:notEqual>
					<jsp:include page="/WEB-INF/subpages/cruscotto/amministrazione/lista.jsp" />
					<br class="hidden" />
					<br>
					<jsp:include page="/WEB-INF/subpages/cruscotto/amministrazione/listaPosta.jsp" />
					<br>
					<logic:notEqual name="cruscottoForm" property="sottoposto.utenteId" value="0">
					<span class="title"> 
						<strong>Fascicoli dove l'utente <span class="evidenziato"><bean:write name="cruscottoForm" property="sottoposto.nomeUtente"/></span> &egrave; referente</strong>
					</span>	
					<jsp:include page="/WEB-INF/subpages/cruscotto/amministrazione/listaFascicoliReferente.jsp" />
					<span class="title"> 
						<strong>Fascicoli dove l'utente <span class="evidenziato"><bean:write name="cruscottoForm" property="sottoposto.nomeUtente"/></span> &egrave; istruttore </strong>
					</span>	
					<jsp:include page="/WEB-INF/subpages/cruscotto/amministrazione/listaFascicoliIstruttore.jsp" />
					</logic:notEqual>
				</div>
			</logic:notEmpty>
		
	</html:form>
</eprot:page>
