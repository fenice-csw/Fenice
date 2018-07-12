<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />
<eprot:page title=" Ricorsi Straordinari">

	<html:form action="/page/procedimento/ricorsi.do">
		<p></p>
		<br />
		<p>
		<div>
		
		<logic:notEmpty name="alertProcedimentoForm" property="protocolliEvidenzaCollection">
			<div class="sezione">
			<span class="title"> <strong>Documenti in Evidenza</strong></span>
			<jsp:include
					page="/WEB-INF/subpages/procedimento/ricorsi/evidenze.jsp" />
		</div>
		</logic:notEmpty>
		
		<logic:notEmpty name="alertProcedimentoForm"
			property="procedimentiULLCollection">
		
		<logic:equal name="alertProcedimentoForm" property="riassegnato" value="true">
		<div class="sezione">
			<span class="title"> <strong>Riassegnati</strong></span>
			<jsp:include
					page="/WEB-INF/subpages/procedimento/ricorsi/lista_riassegnati.jsp" />
		</div>
		</logic:equal>
		
		<logic:equal name="alertProcedimentoForm" property="statoIstruttoria" value="true">
		<div class="sezione">
			<span class="title"> <strong>Fase Istruttoria</strong></span>
			<jsp:include
					page="/WEB-INF/subpages/procedimento/ricorsi/lista_istruttoria.jsp" />
		</div>
		</logic:equal>
		
		<logic:equal name="alertProcedimentoForm" property="statoRelatoria" value="true">
		<div class="sezione">
			<span class="title"> <strong>Fase Relatoria</strong></span>
			<jsp:include
					page="/WEB-INF/subpages/procedimento/ricorsi/lista_relatoria.jsp" />
		</div>
		</logic:equal>
		
		<logic:equal name="alertProcedimentoForm" property="statoParereConsiglio" value="true">
		<div class="sezione">
			<span class="title"> <strong>Attendi Parere del Consiglio</strong></span>
			<jsp:include
					page="/WEB-INF/subpages/procedimento/ricorsi/lista_parere_consiglio.jsp" />
		</div>
		</logic:equal>
		
		<logic:equal name="alertProcedimentoForm" property="statoDecreto" value="true">
		<div class="sezione">
			<span class="title"> <strong>Prepara Decreto del Presidente</strong></span>
			<jsp:include
					page="/WEB-INF/subpages/procedimento/ricorsi/lista_decreto.jsp" />
		</div>
		 </logic:equal>
		 
		 <logic:equal name="alertProcedimentoForm" property="statoAttendiDecreto" value="true">
		<div class="sezione">
			<span class="title"> <strong>Attendi Decreto del Presidente</strong></span>
			<jsp:include
					page="/WEB-INF/subpages/procedimento/ricorsi/lista_attendi_decreto.jsp" />
		</div>
		 </logic:equal>
		 
		</logic:notEmpty>
		</div>


	</html:form>

</eprot:page>




