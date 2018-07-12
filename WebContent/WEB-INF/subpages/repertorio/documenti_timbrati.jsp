<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />

<p><logic:notEmpty property="documentiAllegatiTimbratiCollection"
	name="documentoRepertorioForm">
	<fieldset><legend>Documenti inseriti</legend>
	<table summary=""  border="1" id="tabella_mittenti">
		<tr>
			<th></th>
			<th>Nome file</th>
			<th>Dimensione</th>
		</tr>
		<logic:iterate id="recordDocumento"
			property="documentiAllegatiTimbratiCollection" name="documentoRepertorioForm">
			<tr>
				<td><html:multibox property="allegatiSelezionatiId">
					<bean:write name="recordDocumento" property="idx" />
				</html:multibox></td>
				<td><html:link
					action="/page/repertorio/documento_repertorio.do"
					paramId="downloadAllegatoTimbratoId" paramName="recordDocumento"
					paramProperty="idx" target="_blank" title="Download File">
					<bean:write name="recordDocumento" property="descrizione" />
				</html:link></td>
				<td><bean:write name="recordDocumento" property="size" />
					bytes
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br />
	</fieldset>
</logic:notEmpty></p>


