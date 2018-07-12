<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<html:xhtml />
<div class="sezione">
	<span class="title"><strong><bean:message key="fascicolo.invioprotocollo.documenti"/></strong></span>	    
	<bean:define value="/page/fascicoli.do?" scope="request" id="url" />
	<logic:notEmpty name="fascicoloForm" property="documentiFascicolo">
	<table width="95%" summary="" border="1">
		<tr>
			<th>
				<span><bean:message key="fascicolo.invioprotocollo.documento"/></span>
			</th>		
			<th>
				<span><bean:message key="fascicolo.invioprotocollo.descrizione"/></span>
			</th>		
			<th>
				<span><bean:message key="fascicolo.invioprotocollo.documentoprincipale"/></span>
			</th>
			<th>
				<span><bean:message key="fascicolo.invioprotocollo.allegato"/></span>
			</th>
		</tr>


		<logic:iterate id="row" name="fascicoloForm" property="documentiFascicolo" >
			<tr>
				<td>
					<span>
						<bean:write name="row" property="nomeFile" />
					</span>
				</td>		
				<td>
					<span>
						<bean:write name="row" property="descrizione" />
					</span>
				</td>		
				<td>
					<html:radio property="documentoPrincipaleSelezionato" value="id" idName="row"></html:radio>
				</td>
				<td>
					<html:multibox property="documentiAllegatiSelezionati"><bean:write name="row" property="id" /></html:multibox>
				</td>
			</tr>
		</logic:iterate>

	</table>

	</logic:notEmpty>
</div>