<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />
<table summary="">
	<tr>
	    <td class="label"><label for="indirizzo_toponimo">
		<bean:message key="soggetto.indirizzo" />:</label>
		</td>
	    <td align="left">
		<html:text property="soggetto.indirizzo.toponimo" styleId="indirizzo_toponimo" size="30" maxlength="50" />
		</td>
	</tr>	
		
	<tr >
	    <td class="label"><label for="indirizzo_civico">
		<bean:message key="soggetto.numero" />:</label>
		</td>
	    <td align="left">
		<html:text property="soggetto.indirizzo.civico" styleId="indirizzo_civico" size="10" maxlength="50" />
		</td>
	</tr>
			
	<tr >
	    <td class="label"><label for="indirizzo_comune">
		<bean:message key="soggetto.localita" /><span
          class="obbligatorio"> * </span>:</label>
		</td>
	    <td align="left">
		<html:text property="soggetto.indirizzo.comune" styleId="indirizzo_comune" size="30" maxlength="50" />
		</td>
	</tr>	
		
	<tr >
	    <td class="label"><label for="indirizzo_cap">
		<bean:message key="soggetto.cap" />:</label>
		</td>
	    <td align="left">
		<html:text property="soggetto.indirizzo.cap" styleId="indirizzo_cap" size="5" maxlength="5" />
		</td>
	</tr>	
		
	<tr >
	    <td class="label"><label for="indirizzo_provincia">
		<bean:message key="soggetto.provincia" />:</label>
		</td>
	    <td align="left">
		<html:select property="soggetto.indirizzo.provinciaId">
			<html:optionsCollection name ="LOOKUP_DELEGATE" property="province" value="provinciaId" label="descrizioneProvincia" />
		</html:select>
		</td>
	</tr>		
	
	<tr >
	    <td class="label"><label for="telefono">
		<bean:message key="soggetto.telefono" />:</label>
		</td>
	    <td align="left">
		<html:text property="soggetto.telefono" styleId="telefono" size="10" maxlength="16" />
		</td>
	</tr>		
	
	<tr >
	    <td class="label"><label for="teleFax">
		<bean:message key="soggetto.fax" />:</label>
		</td>
	    <td align="left">
		<html:text property="soggetto.teleFax" styleId="teleFax" size="10" maxlength="16" />
		</td>
	</tr>		
	
	<tr >
	    <td class="label"><label for="indirizzoWeb">
		<bean:message key="soggetto.web" />:</label>
		</td>
	    <td>
		<html:text property="soggetto.indirizzoWeb" styleId="indirizzoWeb" size="30" maxlength="50" />
		</td>
	</tr>
	
</table>
