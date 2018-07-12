<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />


<p>
  <label for="nomeFileUpload">
   <bean:message key="protocollo.allegati.nome"/>:
  </label>
  <html:text property="nomeFileUpload" styleId="nomeFileUpload"/>
  &nbsp;
  <label for="formFileUpload">
   <bean:message key="protocollo.allegati.file"/>:
  </label>
  <html:file property="formFileUpload" styleId="formFileUpload" />
  &nbsp;
  <html:submit styleClass="button" property="allegaDocumentoAction" value="Allega" title="Allega il file selezionato" />
 
  <logic:equal name="protocolloForm" property="flagTipo" value="I">
   		<a id="various3" class="button" onclick="timer_scan_allegati('documento.do?nomeFileUpload')"  href="<%= request.getContextPath()%>/scanagent/scanagent.jsp?jSessID=<%=request.getSession().getId()%>&url=http://<%=request.getServerName()%>:<%=request.getServerPort()%><%= request.getContextPath()%>/page/protocollo/ingresso/documento.do?allegaDocumentoScannOldAction=true" >Scanner</a>
   </logic:equal>
   <logic:equal name="protocolloForm" property="flagTipo" value="U">
		<a id="various3" class="button" onclick="timer_scan_allegati('documento.do?nomeFileUpload')"  href="<%= request.getContextPath()%>/scanagent/scanagent.jsp?jSessID=<%=request.getSession().getId()%>&url=http://<%=request.getServerName()%>:<%=request.getServerPort()%><%= request.getContextPath()%>/page/protocollo/uscita/documento.do?allegaDocumentoScannOldAction=true" >Scanner</a>
   </logic:equal>
   <logic:equal name="protocolloForm" property="flagTipo" value="P">
		<a id="various3" class="button" onclick="timer_scan_allegati('documento.do?nomeFileUpload')"  href="<%= request.getContextPath()%>/scanagent/scanagent.jsp?jSessID=<%=request.getSession().getId()%>&url=http://<%=request.getServerName()%>:<%=request.getServerPort()%><%= request.getContextPath()%>/page/protocollo/posta_interna/documento.do?allegaDocumentoScannOldAction=true" >Scanner</a>
	</logic:equal>
</p>

<br />

<p>
<logic:notEmpty property="documentiAllegatiCollection" name="protocolloForm">
<ul>
<logic:iterate id="recordDocumento" property="documentiAllegatiCollection" name="protocolloForm">
	<li>
  
  <bean:define id="idx" name="recordDocumento" property="idx"/>
  <bean:define id="descrizione" name="recordDocumento" property="descrizione"/>
  <html:multibox property="allegatiSelezionatiId"><bean:write name="idx"/></html:multibox>
  <html:link 
  href="./documento.do" 
  paramId="downloadAllegatoId" 
  paramName="recordDocumento" 
  paramProperty="idx" 
  target="_blank"
  title="Download File">
  <bean:write name="recordDocumento" property="descrizione"/>
  </html:link>
  (<bean:write name="recordDocumento" property="size"/> bytes)
  
  </li>
</logic:iterate> 
</ul>
</logic:notEmpty>
  
</p>

<logic:equal name="protocolloForm" property="allegatoScansionato" value="true">
	<div id="doc_file_name_allegati" style="display:none;">OK</div>
</logic:equal>


<logic:notEmpty name="protocolloForm" property="documentiAllegatiCollection">
<br />
<p>
  <html:submit styleClass="button" property="rimuoviAllegatiAction" value="Rimuovi selezionati" alt="Rimuovi gli allegati selezionati" />
</p>
</logic:notEmpty>

