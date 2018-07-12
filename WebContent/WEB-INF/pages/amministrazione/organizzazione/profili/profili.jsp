<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Gestione profili">

	<html:form action="/page/amministrazione/profilo.do">
		<div id="protocollo-errori"><html:errors
			bundle="bundleErroriProtocollo" /></div>
		<div id="protocollo-messaggi">
			<html:messages id="msg" message="true">
				<ul>
					<li>
						<bean:write name="msg" />
					</li>
				</ul>
			</html:messages>
		</div>


		<logic:equal name="profiloForm" property="id" value="0">
			<logic:equal name="profiloForm" property="nuovo" value="true">
				<jsp:include
					page="/WEB-INF/subpages/amministrazione/organizzazione/profili/formAggiornamento.jsp" />
			</logic:equal>
			<logic:notEqual name="profiloForm" property="nuovo" value="true">
				<jsp:include
					page="/WEB-INF/subpages/amministrazione/organizzazione/profili/lista.jsp" />
			</logic:notEqual>
			<br class="hidden" />
		</logic:equal>
		<logic:greaterThan name="profiloForm" property="id" value="0">
			<jsp:include
				page="/WEB-INF/subpages/amministrazione/organizzazione/profili/formAggiornamento.jsp" />
		</logic:greaterThan>



	</html:form>

</eprot:page>