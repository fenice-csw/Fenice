<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<%-- --%>
<script type='text/javascript' src='/feniceWeb/script/scanagent/jquery.min.js'></script>
<script type='text/javascript' src='/feniceWeb/script/scanagent/jquery-1.4.3.min.js'></script>
<script type='text/javascript' src='/feniceWeb/script/scanagent/jquery.fancybox-1.3.4.pack.js'></script>
<script type='text/javascript' src='/feniceWeb/script/scanagent/jquery.mousewheel-3.0.4.pack.js'></script>
<script type="text/javascript" src="/feniceWeb/script/scanagent/jquery.timer.js"></script>
<link rel="stylesheet" type="text/css" href="/feniceWeb/style/jquery.fancybox-1.3.4.css" media="screen" />
	
	<script type='text/javascript' >
		$(document).ready(function() {
		$("#variousPI").fancybox({
			'width'				: 800,
			'height'			: 600,
			'transitionIn'		: 'none',
			'transitionOut'		: 'none',
			'type'				: 'iframe'
		});
	});

	</script>
<%-- --%>

<html:xhtml />
<eprot:page title="Notifiche Posta Interna">
	<html:form action="/page/protocollo/scarico.do" focus="dataRegistrazioneDa">
		<div>
			<jsp:include page="/WEB-INF/subpages/protocollo/checkPI/cerca.jsp" />
			<hr></hr>
		</div>
		<div>
			<jsp:include page="/WEB-INF/subpages/protocollo/checkPI/lista.jsp" />
		</div>
		<html:submit styleClass="submit" property="btnLeggiTutteNotifiche" value="Visti tutti"  alt="Considera lette tutte le notifiche" />
	</html:form>
</eprot:page>




