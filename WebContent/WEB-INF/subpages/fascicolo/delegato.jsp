<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />

<div class="sezione">
	<p>
	<table summary="">
				<tr>
					<td>
						<logic:notEmpty name="fascicoloForm" property="delegato">
							<span><strong><bean:write name="fascicoloForm" property="delegato" /></strong></span>
				    	</logic:notEmpty>
				    	
					</td>
					<td>
						<html:submit styleClass="button" property="cercaSoggettoAction"value="seleziona il delegato" title="Seleziona il mittente dalla rubrica" />
							<logic:notEmpty name="fascicoloForm" property="delegato">
								<html:submit styleClass="button" property="annullaDelegatoAction"value="annulla" title="annulla" />
					    	</logic:notEmpty>
					</td>
				</tr>
	</table>
	</p>
</div>