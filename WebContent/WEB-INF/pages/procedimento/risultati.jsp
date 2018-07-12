<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />

<eprot:page title="Ricerca procedimenti">

<html:form action="/page/procedimento/ricerca.do">
<p> &nbsp; </p>
<jsp:include page="/WEB-INF/subpages/procedimento/cerca/lista.jsp"/>

<logic:equal scope="session" name="tornaFaldone" value="true">
  <logic:notEmpty scope="session" name="ricercaProcedimentoForm" property="procedimenti">
	<br/>
	<html:submit styleClass="submit" property="btnSeleziona" value="Seleziona" alt="Seleziona i procedimenti" />
  </logic:notEmpty>
  <logic:empty scope="session" name="ricercaProcedimentoForm" property="procedimenti">
   <html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="Annulla" />  
  </logic:empty>
</logic:equal>

<logic:equal scope="session" name="tornaProtocollo" value="true">
  <logic:notEmpty scope="session" name="ricercaProcedimentoForm" property="procedimenti">
	<br/>
	<html:submit styleClass="submit" property="btnSeleziona" value="Seleziona" alt="Seleziona i procedimenti" />
  </logic:notEmpty>
  <logic:empty scope="session" name="ricercaProcedimentoForm" property="procedimenti">
   <html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="Annulla" />  
  </logic:empty>
</logic:equal>

<logic:equal scope="session" name="btnCercaProcedimentiDaFaldoni" value="true">
  <logic:notEmpty scope="session" name="ricercaProcedimentoForm" property="procedimenti">
	<br/>
	<html:submit styleClass="submit" property="btnSeleziona" value="Seleziona" alt="Seleziona i procedimenti" /> 
  </logic:notEmpty>
  <logic:empty scope="session" name="ricercaProcedimentoForm" property="procedimenti"> 
   <html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="Annulla" />  
  </logic:empty>
</logic:equal>

<logic:equal scope="session" name="tornaFascicolo" value="true">
  <logic:notEmpty scope="session" name="ricercaProcedimentoForm" property="procedimenti">
	<br/>
	<html:submit styleClass="submit" property="btnSeleziona" value="Seleziona" alt="Seleziona i procedimenti" />
  </logic:notEmpty>
  <logic:empty scope="session" name="ricercaProcedimentoForm" property="procedimenti">
   <html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="Annulla" />  
  </logic:empty>
</logic:equal>

<logic:equal scope="session" name="risultatiProcedimentiDaProtocollo" value="true">
  <logic:notEmpty scope="session" name="ricercaProcedimentoForm" property="procedimenti">
	<br/>
	<html:submit styleClass="submit" property="btnSeleziona" value="Seleziona" alt="Seleziona i procedimenti" />
  </logic:notEmpty>
  <logic:empty scope="session" name="ricercaProcedimentoForm" property="procedimenti">
   <html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="Annulla" />  
  </logic:empty>
</logic:equal>
<logic:equal scope="session" name="ricercaSemplice" value="true">
<logic:notEmpty scope="session" name="ricercaProcedimentoForm" property="procedimenti">
	<br/>
  </logic:notEmpty>
  <logic:empty scope="session" name="ricercaProcedimentoForm" property="procedimenti">
   <html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="Annulla" />  
  </logic:empty>
</logic:equal>
<html:submit styleClass="submit" property="annullaAction" value="Nuova Ricerca" alt="Nuova Ricerca" />
</html:form>
</eprot:page>