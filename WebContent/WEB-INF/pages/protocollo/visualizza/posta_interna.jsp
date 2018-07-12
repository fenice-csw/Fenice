<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Visualizza Protocollo">

	<html:form action="/page/protocollo/posta_interna/summaryview.do" enctype="multipart/form-data">

		<br class="hidden" />
		<jsp:include page="/WEB-INF/subpages/protocollo/visualizza/datiProtocollo.jsp" />
		 
		 <br class="hidden" />
		 <jsp:include page="/WEB-INF/subpages/protocollo/visualizza/datiDocumentoView.jsp" />

		 
		<br class="hidden" />
		
		
		<logic:equal name="visualizzaProtocolloForm" property="flagTipo" value="I">
			<div class="sezione">
				<span class="title">
					<strong>Mittenti</strong>
				</span>
				<jsp:include page="/WEB-INF/subpages/protocollo/visualizza/ingresso/mittenteView.jsp" />
			</div>
		</logic:equal>
		
		<logic:notEqual name="visualizzaProtocolloForm" property="flagTipo" value="I">
			<div class="sezione">
				<span class="title">
					<strong>Mittenti</strong>
				</span>
				<jsp:include page="/WEB-INF/subpages/protocollo/visualizza/uscita/mittenteView.jsp" />
			</div>
		</logic:notEqual>
		
		<logic:notEmpty name="visualizzaProtocolloForm" property="documentiAllegatiCollection" >
		<div class="sezione">
			<span class="title">
				<strong>Allegati</strong>
			</span>
			<jsp:include page="/WEB-INF/subpages/protocollo/visualizza/allegatiView.jsp" />
		</div>
		</logic:notEmpty>	
			
		<logic:notEmpty name="visualizzaProtocolloForm" property="protocolliAllacciati">	
		<div class="sezione">
			<span class="title">
				<strong>Collegati</strong>
			</span>
			<jsp:include page="/WEB-INF/subpages/protocollo/visualizza/allacciView.jsp" />
		</div>
		</logic:notEmpty>
				
		<logic:notEqual name="visualizzaProtocolloForm" property="flagTipo" value="U">
			<div class="sezione">
				<span class="title">
					<strong>Assegnatari/Destinatari</strong>
				</span>
				<jsp:include page="/WEB-INF/subpages/protocollo/visualizza/ingresso/assegnatariView.jsp" />
			</div>
		</logic:notEqual>
		
		<logic:equal name="visualizzaProtocolloForm" property="flagTipo" value="U">
			<div class="sezione">
				<span class="title">
					<strong>Destinatari</strong>
				</span>
				<jsp:include page="/WEB-INF/subpages/protocollo/visualizza/uscita/destinatariView.jsp" />
			</div>
		</logic:equal>
		
		<logic:notEmpty property="descrizioneAnnotazione" name="visualizzaProtocolloForm">
		<div class="sezione">
			<span class="title">
				<strong>Annotazioni</strong>
			</span>
			<jsp:include
					page="/WEB-INF/subpages/protocollo/visualizza/annotazioniView.jsp" />
		</div>
		</logic:notEmpty>
			
		<logic:notEmpty name="visualizzaProtocolloForm" property="fascicoliProtocollo">	
		<div class="sezione">
			<span class="title">
				<strong>Fascicoli</strong>
			</span>
			<jsp:include page="/WEB-INF/subpages/protocollo/visualizza/datiFascicoliView.jsp" />
		</div>
		</logic:notEmpty>
		
		<logic:notEmpty name="visualizzaProtocolloForm" property="procedimentiProtocollo">
		<div class="sezione">
			<span class="title">
				<strong>Procedimenti</strong>
			</span>
				<jsp:include
					page="/WEB-INF/subpages/protocollo/visualizza/datiProcedimentiView.jsp" />
		</div>
		</logic:notEmpty>
		
		<html:submit styleClass="submit" property="btnIndietro" value="Indietro" alt="Indietro" />
			
	</html:form>
	
</eprot:page>