<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Protocollo in uscita">
<bean:define id="baseUrl" value="/page/protocollo/uscita/documento.do" scope="request" />


<html:form action="/page/protocollo/uscita/allegaEdit.do" enctype="multipart/form-data">

<div id="protocollo">


    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />


<br class="hidden" />

<div>
	<bean:message key="campo.obbligatorio.msg" />
	<br class="hidden" />
</div>

<jsp:include page="/WEB-INF/subpages/protocollo/common/datiProtocollo.jsp" />

<br class="hidden" />
<logic:equal name="protocolloForm" property="webSocketEnabled" value="true" >
  	<jsp:include page="/WEB-INF/subpages/protocollo/common/datiDocumentoPerAllegato.jsp" />
</logic:equal>
<logic:equal name="protocolloForm" property="webSocketEnabled" value="false" >
	<jsp:include page="/WEB-INF/subpages/protocollo/common/datiDocumentoPerAllegatoOld.jsp" />
</logic:equal>

<br class="hidden" />



<div>


<logic:notEqual name="protocolloForm" property="protocolloId" value="0">
  	<html:submit styleClass="submit" property="salvaAction" value="Salva" alt="Salva protocollo" />
  	<html:submit styleClass="submit" property="annullaActionAllegato" value="Annulla" alt="Annulla l'operazione" />
  	<logic:notEqual name="protocolloForm" property="stato" value="C">
		<html:submit styleClass="submit" property="btnAllegaStampaEtichettaProtocollo" value="Etichetta protocollo" alt="Stampa etichetta protocollo" />
	</logic:notEqual>
</logic:notEqual> 



</div>

</div>

</html:form>

</eprot:page>