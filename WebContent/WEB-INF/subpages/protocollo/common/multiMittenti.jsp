<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<html:xhtml />

<br />

<logic:equal name="protocolloForm" property="protocolloId" value="0" >
<table id="assegnatari">
<tr>
    <td class="label">
      <label for="cognomeMittente"><bean:message
          key="protocollo.mittente.cognome" /></label>&nbsp;<span
          class="obbligatorio">*</span>&nbsp;:
    </td>
    <td>
      <html:text styleClass="obbligatorio" property="multiMittenteCorrente.cognome" styleId="cognomeMittente" size="45" maxlength="100"/>
    </td>
  </tr>
  <tr>
    <td class="label">
      <label for="nomeMittente"><bean:message key="protocollo.mittente.nome"/></label>&nbsp;:
    </td>
    <td>
      <html:text property="multiMittenteCorrente.nome" styleId="nomeMittente" size="45" maxlength="40"/>
    </td>
  </tr>
  <tr>
    <td class="label">
      <label for="indirizzoMittente"><bean:message key="soggetto.indirizzo" /></label>&nbsp;:
    </td>
    <td>
      <html:text property="multiMittenteCorrente.indirizzoNumCivico" styleId="indirizzoMittente" size="45" maxlength="250" />
    </td>
  </tr>
  <tr>
    <td class="label">
      <label for="localitaMittente"><bean:message key="soggetto.localita" /></label>&nbsp;:
    </td>
    <td>
      <html:text property="multiMittenteCorrente.indirizzo.comune" styleId="localitaMittente" size="45" maxlength="100" />
    </td>
  </tr>
  <tr>
    <td class="label">
      <label title="Codice di Avviamento Postale" for="capMittente"><bean:message key="soggetto.cap" /></label>&nbsp;:
    </td>
    <td class="subtable">
      <table summary="">
        <tr>
          <td>
            <html:text property="multiMittenteCorrente.indirizzo.cap" styleId="capMittente" size="6" maxlength="5" />
          </td>
          <td>&nbsp;&nbsp;</td>
          <td class="label">
            <label for="provinciaMittente"><bean:message key="soggetto.provincia" /></label>&nbsp;:
          </td>
          <td>
            <html:select property="multiMittenteCorrente.indirizzo.provinciaId">
              <html:optionsCollection property="province" value="provinciaId" label="descrizioneProvincia" />
            </html:select>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>

<html:submit styleClass="button" property="assegnaMittenteAction" value="Assegna" title="Assegna nuovo mittente" />
<br/>
<br/>
</logic:equal>

<logic:notEmpty name="protocolloForm" property="mittenti">

<fieldset>
<legend>Mittenti inseriti</legend>
<table id="assegnatari">
<tr>
<logic:equal name="protocolloForm" property="protocolloId" value="0" >
<th></th>
</logic:equal>
<th>Nome</th>
<th>Cognome</th>
<th>Indirizzo</th>
<th>Località</th>
</tr>
<logic:iterate indexId="index" id="mittente" name="protocolloForm" property="mittenti">
      
     <tr> 
<logic:equal name="protocolloForm" property="protocolloId" value="0" >
		  <td>
	    	<html:multibox property="mittentiSelezionatiId">
	    		<bean:write name="index"/>
	    	</html:multibox>
	      </td>
</logic:equal>
		  <td><bean:write name="mittente" property="nome"/></td>
		  <td><bean:write name="mittente" property="cognome"/></td>
		  <td><bean:write name="mittente" property="indirizzoCompleto"/></td>
		  <td><bean:write name="mittente" property="indirizzo.comune"/></td>
	</tr>
</logic:iterate>
</table>
<logic:equal name="protocolloForm" property="protocolloId" value="0" >
<html:submit styleClass="button" property="rimuoviMultiMittentiAction" value="Rimuovi" title="Rimuove gli assegnatari selezionati dall'elenco" />
</logic:equal>
</fieldset>
</logic:notEmpty>

