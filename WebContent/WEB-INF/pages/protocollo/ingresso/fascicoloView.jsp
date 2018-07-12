<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Protocollo in ingresso">
<bean:define id="baseUrl" value="/page/protocollo/ingresso/documento.do" scope="request" />

<logic:equal parameter="protocolloRegistrato" value="true">
<div id="protocollo_registrato">
   <bean:message key="protocollo_registrato" bundle="bundleErroriProtocollo" />
   <strong>
	<bean:write name="protocolloForm" property="numeroProtocollo" />
   </strong>
</div>
</logic:equal>

<html:form action="/page/protocollo/ingresso/fascicola.do">

<div id="protocollo">


    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />


<br class="hidden" />

<logic:equal name="protocolloIngressoForm" property="documentoVisibile" value="true" >
<div class="sezione">
<bean:define id="sezioneVisualizzata" name="protocolloIngressoForm" property="sezioneVisualizzata" />
<jsp:include page="/WEB-INF/subpages/protocollo/common/link-fascicolazione.jsp" />
	<logic:equal name="sezioneVisualizzata" value="Titolario">
		<jsp:include page="/WEB-INF/subpages/protocollo/common/titolarioView.jsp" />
	</logic:equal>
	<logic:match name="sezioneVisualizzata" value="Fascicoli">
	    <jsp:include page="/WEB-INF/subpages/protocollo/common/datiFascicoliView.jsp" />
	</logic:match>
</div>
</logic:equal>

<br class="hidden" />

<jsp:include page="/WEB-INF/subpages/protocollo/common/datiProtocollo.jsp" />

<br class="hidden" />

<jsp:include page="/WEB-INF/subpages/protocollo/common/datiDocumentoView.jsp" />

<br class="hidden" />

<logic:notEmpty name="protocolloForm" property="provvedimentoAnnullamento" >
	<jsp:include page="/WEB-INF/subpages/protocollo/common/datiAnnullamentoView.jsp" />
	<br class="hidden" />
</logic:notEmpty>	

<div>

<logic:equal name="protocolloForm" property="versioneDefault" value="true">

	<logic:notEqual name="protocolloForm" property="protocolloId" value="0">

		<logic:equal name="protocolloForm" property="modificabile" value="true">
			<html:submit styleClass="submit" property="btnFascicola" value="Fascicola" alt="Fascicola registrazione di protocollo" />
		</logic:equal>
	</logic:notEqual>
</logic:equal>

</div>

</div>

</html:form>
</eprot:page>