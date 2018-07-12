<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />
<html:errors bundle="bundleErroriProtocollo"
	property="destinatario_nome_obbligatorio" />
<html:errors bundle="bundleErroriProtocollo" property="formato_data" />



<logic:notEmpty name="titolarioForm" property="amministrazioni">
	<hr></hr>
	<table summary="" border="1" width="98%">
		<tr>
			<th></th>
			<th>Denominazione</th>
		</tr>

		<logic:iterate id="currentRecord" name="titolarioForm"
			property="amministrazioni">
			<tr>
				<td><html:multibox property="amministrazioniSelezionateId">
					<bean:write name="currentRecord" property="idx" />
				</html:multibox></td>
				<td><span> <bean:write name="currentRecord"
					property="destinatario" /><br />
				</span></td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>

<p><html:submit styleClass="button" property="cercaAmministrazioni"
	value="Seleziona" title="Seleziona le amministrazioni della rubrica" />
<logic:notEmpty name="titolarioForm" property="amministrazioni">
	<html:submit styleClass="button" property="rimuoviAmministrazione"
		value="Rimuovi"
		title="Rimuovi i destinatari selezionati dall'elenco dei destinatari" />
</logic:notEmpty></p>

