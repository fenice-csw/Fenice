<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<html:hidden property="codice"/>
<html:hidden property="descrizione"/>
<table summary="">
  <tr>
    <td class="label">
      <span><bean:message key="soggetto.lista.tipo"/>&nbsp;:&nbsp;</span>
    </td>
    <td>
        <html:radio property="tipo" styleId="personaFisica"
             value="F" onclick="document.forms[0].submit()">
        <label for="personaFisica"><bean:message key="soggetto.lista.personafisica"/></label>
        </html:radio> 
        &nbsp;&nbsp;
        <html:radio property="tipo" styleId="personaGiuridica"
              value="G" onclick="document.forms[0].submit()">
          <label for="personaGiuridica"><bean:message key="soggetto.lista.personagiuridica"/></label>
        </html:radio>
        &nbsp;&nbsp;
        
   <%--      <script type="text/javascript"></script>
        <noscript>
          <div>
          <html:submit styleClass="button" value="Imposta" title="Imposta il tipo di Mittente"/>
          &nbsp;&nbsp;
          </div>
        </noscript> --%>
        
        <html:submit styleClass="button" property="cercaSoggettiAction" value="Seleziona" title="Seleziona il tipo di soggetto"/>
    </td>
  </tr>

<logic:notEmpty name="listaDistribuzioneForm" property="tipo">
<bean:define id="tipo" name="listaDistribuzioneForm" property="tipo"/>
</logic:notEmpty>
	
 </table>
<logic:notEmpty name="listaDistribuzioneForm" property="elencoSoggettiListaDistribuzione">
<hr></hr>
<table summary="" border="1" width="98%">
	<tr>
		<th>
		</th>
		<th>
			<span>
			<bean:message key="soggetto.giuridica.denominazione"/> - 
			<bean:message key="soggetto.fisica.cognome"/>
			</span>
		</th>	
		<th>
			<span>
			<bean:message key="soggetto.giuridica.piva"/>
			</span>
		</th>
												
	</tr>
	<logic:iterate id="currentRecord" name="listaDistribuzioneForm" property="elencoSoggettiListaDistribuzioneCollection">
	<tr>
		<td>
			<html:multibox property="soggettiSelezionatiId">
				<bean:write name="currentRecord" property="id" />
			</html:multibox>
		</td>
		<td>
			<span>
			<logic:equal name="currentRecord" property="tipo" value= "F">	
			<bean:write name="currentRecord" property="cognome" />
			</logic:equal>
			<logic:equal name="currentRecord" property="tipo" value="G">
			<bean:write name="currentRecord" property="descrizioneDitta" />
			</logic:equal>
			</span>
		</td>

		<td>
			<span>
				<bean:write name="currentRecord" property="partitaIva" />
			</span><br/>
			
		</td>
	</tr>

	</logic:iterate>

</table>

	<html:submit styleClass="button" property="rimuoviSoggetti" value="Rimuovi" title="Rimuovi i soggetti selezionati dall'elenco "/>

</logic:notEmpty>