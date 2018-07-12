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
						<logic:notEmpty name="procedimentoForm" property="autoritaEmanante">
							<span><strong><bean:write name="procedimentoForm" property="autoritaEmanante" /> </strong></span>
				    	</logic:notEmpty>
				    	
					</td>
					<td>
						<html:submit styleClass="button" property="cercaAutoritaEmananteAction"value="seleziona" title="Seleziona l' autorita emanante dalla rubrica" />
						<logic:notEmpty name="procedimentoForm" property="autoritaEmanante">
								<html:submit styleClass="button" property="annullaAutoritaEmananteAction"value="Annulla" title="Annulla" />
				    	</logic:notEmpty>
					</td>
				</tr>
	</table>
	</p>
</div>