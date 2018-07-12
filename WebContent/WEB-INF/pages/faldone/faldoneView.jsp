<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />

<eprot:page title="Faldone">
	<div id="protocollo-errori">
		<html:errors bundle="bundleErroriFaldone" />
	</div>

	<html:form action="/page/faldone.do">
		<div id="protocollo">
			<jsp:include page="/WEB-INF/subpages/protocollo/faldone/datiFaldoneView.jsp" />
		</div>
		<logic:equal name="faldoneForm" property="versioneDefault"
			value="true">
			<logic:greaterThan name="faldoneForm" property="faldoneId" value="0">
				<logic:equal name="faldoneForm" property="modificabile" value="true">
					<html:submit styleClass="submit" property="btnModificaFaldone"
						value="Modifica" alt="Modifica il faldone" />
					<%--html:submit styleClass="submit" property="btnCancella"
						value="Cancella" alt="Cancella il faldone" --%>
					<html:submit styleClass="submit" property="btnNuovoFaldone"
						value="Nuovo" alt="Crea un nuovo faldone" />
				</logic:equal>
			</logic:greaterThan>
		</logic:equal>
		<logic:greaterThan name="faldoneForm" property="versione" value="0">
			<html:submit styleClass="submit" property="btnStoria" value="Storia"
				title="Storia del Faldone" />
		</logic:greaterThan>
	</html:form>

</eprot:page>
