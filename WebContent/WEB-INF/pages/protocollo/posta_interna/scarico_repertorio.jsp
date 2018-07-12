<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Scarico Posta Interna">

<html:form action="/page/protocollo/posta_interna/ricevute_repertorio.do" focus="numeroProtocolloDa">

<div> 
	<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/cerca_lista_repertorio.jsp" />
	<hr></hr>
</div>
 
<div>
	<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/lista_posta_repertorio.jsp" />
</div>
</html:form>

</eprot:page>




