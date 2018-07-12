<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Faldone">

<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<br />

<logic:equal name="faldoneRegistrato" value="true">
<div id="faldone_registrato">
   <bean:message key="faldone_registrato" bundle="bundleErroriFaldone" />
   <strong>
	<bean:write name="faldoneForm" property="numeroFaldone" />
   </strong>
</div>
</logic:equal>

<html:form action="/page/faldone.do">
<div id="protocollo">
	
	<jsp:include page="/WEB-INF/subpages/protocollo/faldone/datiFaldoneView.jsp" />
	

</div>
<br class="hidden" />
<br class="hidden" />
<p align="center">


	<span>Premere il tasto Conferma per effettuare l'operazione di <strong>
		<bean:write name="faldoneForm" property="operazione" />
	</strong> sul faldone selezionato</span>
<br class="hidden" />
<br class="hidden" />
</p>

<p align="center">
<logic:equal name="faldoneForm" property="operazione" value="Riapertura">
	<html:submit styleClass="submit" property="btnRiapriFaldone" value="Conferma" title="Riapri il faldone" />
	<html:submit styleClass="button" property="btnAnnullaRiapri" value="Annulla" title="Annulla l'operazione" />
</logic:equal>	
<logic:equal name="faldoneForm" property="operazione" value="Chiusura">
	<html:submit styleClass="submit" property="btnChiudiFaldone" value="Conferma" title="Chiudi il faldone" />
	<html:submit styleClass="button" property="btnAnnullaChiudi" value="Annulla" title="Annulla l'operazione" />
</logic:equal>	
<logic:equal name="faldoneForm" property="operazione" value="Cancellazione">
	<html:submit styleClass="submit" property="btnCancellaFaldone" value="Conferma" title="Cancella il faldone" />
	<html:submit styleClass="button" property="btnAnnullaCancella" value="Annulla" title="Annulla l'operazione" />
</logic:equal>
</p>
</html:form>
</eprot:page>
