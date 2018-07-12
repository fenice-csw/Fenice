<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Protocollo in ingresso">
<html:form action="/page/protocollo/posta_interna/fascicolaEdit.do" enctype="multipart/form-data">

<div id="protocollo">


    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />


<br class="hidden" />





<br class="hidden" />
<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/datiProtocollo.jsp" />

<br class="hidden" />
<jsp:include page="/WEB-INF/subpages/protocollo/common/datiDocumentoView.jsp" />

<br class="hidden" />

<div class="sezione">
<span class="title">
	<strong>Fascicoli</strong>
	</span>
	    <jsp:include page="/WEB-INF/subpages/protocollo/common/datiFascicoli.jsp" />
</div>

<div>

<!-- 
<logic:equal name="protocolloForm" property="protocolloId" value="0" >
	<html:submit styleClass="submit" property="salvaAction" value="Registra" alt="Salva protocollo" />
	<html:submit styleClass="submit" property="annullaAction" value="Annulla" alt="Annulla l'operazione" />
</logic:equal>
 -->
 
<logic:notEqual name="protocolloForm" property="protocolloId" value="0" >
	<html:submit styleClass="submit" property="salvaAction" value="Fascicola" alt="Salva protocollo" />
	<html:submit styleClass="submit" property="annullaAction" value="Annulla" alt="Annulla l'operazione" />
</logic:notEqual>



</div>

</div>

</html:form>

</eprot:page>