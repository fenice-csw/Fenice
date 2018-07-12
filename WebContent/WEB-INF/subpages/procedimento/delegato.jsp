<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />

<div class="sezione">
	<p>
	<table summary="">
		<tr>  
			<logic:notEmpty name="procedimentoForm" property="delegato">

				<td>
					<span><strong><bean:write name="procedimentoForm" property="delegato" /></strong></span>
				</td>
			</logic:notEmpty>	
			<td>
				<html:submit styleClass="button" property="cercaDelegatoAction"value="seleziona" title="Seleziona il delegato dalla rubrica" />
			</td>
			<logic:notEmpty name="procedimentoForm" property="delegato">
					<td>
						<html:submit styleClass="button" property="annullaDelegatoAction"value="Annulla" title="Annulla" />
					</td>
			</logic:notEmpty>
		</tr>
	</table>
	</p>
</div>