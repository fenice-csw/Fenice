<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />

<eprot:page title=" Procedimenti Istruttore">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>
<br/>
<hr></hr>
	<html:form action="/page/procedimento/scarico.do">
		<jsp:include page="/WEB-INF/subpages/procedimento/procedimentoAssegnatari/listaInternaIstruttore.jsp" />
	</html:form>
</eprot:page>




