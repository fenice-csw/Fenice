<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />
<div>
</div>

<div>
<logic:notEmpty name="fascicoloForm" property="protocolliFascicoloCollection">
	<display:table class="simple" width="95%" requestURI="/page/fascicolo/allaccioview.do"
		name="sessionScope.fascicoloForm.protocolliFascicolo"
		export="false" sort="list" pagesize="10" id="row">
				<display:column title="Numero" property="annoNumeroProtocollo"/>
				<display:column property="tipoProtocollo" title="Tipo" />
				<display:column property="dataProtocollo" title="Registrato il" />
				<display:column title="Mittente">
					<logic:notEqual name="row" property="tipoMittente" value="M">
						<bean:write name="row" property="mittente" />
					</logic:notEqual>
					<logic:equal name="row" property="tipoMittente" value="M">
						<ul>
							<logic:iterate id="mittente" name="row" property="mittenti">
								<li><bean:write name="mittente" property="descrizione"/></li>
							</logic:iterate>
						</ul>
					</logic:equal>
				</display:column>
				<display:column property="oggetto" title="Oggetto" />
				<display:column title="Doc" property="pdf"/>
	</display:table>
</logic:notEmpty>
<logic:empty name="fascicoloForm" property="protocolliFascicoloCollection">
	<span><bean:message key="fascicolo.messaggio4"/></span>
</logic:empty>
</div>
