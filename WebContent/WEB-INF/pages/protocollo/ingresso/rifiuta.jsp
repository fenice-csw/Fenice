<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Riassegna protocollo">


<html:form action="/page/protocollo/ingresso/rifiuta.do">

<div id="protocollo">


    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />


<br class="hidden" />

<jsp:include page="/WEB-INF/subpages/protocollo/rifiuta/datiProtocollo.jsp" />

<br class="hidden" />


<div class="sezione">
	<span class="title">
		<strong>Estremi del Rifiuto </strong>
	</span>
	<p>
	<html:text styleId="messaggio" property="msg" size="60" maxlength="250"/>
	</p>
</div>

<br class="hidden" />

<logic:notEmpty name="rifiutoForm" property="reportFormatsCollection">

<div class="sezione">
	<span class="title">
		<strong>Stampa del rifiuto </strong>
	</span>
	<p>
	<bean:message key="report.messaggio_per_rifiutato"/>:
		<br />
		<ul>
		<logic:iterate id="currentRecord" name="rifiutoForm" property="reportFormatsCollection" >
		  	<li>
		  	<html:link action="/page/protocollo/ingresso/rifiuta" name="currentRecord" property="parameters" target="_blank">
				<bean:write name="currentRecord" property="descReport"/>
		  	</html:link >
		  	</li>
		</logic:iterate>
		</ul>
	</p>  
</div>
</logic:notEmpty>
	
<br class="hidden" />

<div>
	<logic:empty name="rifiutoForm" property="reportFormatsCollection">
		<html:submit styleClass="submit" property="rifiutaAction" value="Rifiuta" alt="Riassegna protocollo" />
		<html:submit styleClass="submit" property="annullaAction" value="Annulla" alt="Annulla l'operazione" />
	</logic:empty>
	<logic:notEmpty name="rifiutoForm" property="reportFormatsCollection">
		<html:submit styleClass="submit" property="indietroAction" value="Indietro" alt="ritorna al dashboard"/>
	</logic:notEmpty>
</div>
</div>
</html:form>

</eprot:page>