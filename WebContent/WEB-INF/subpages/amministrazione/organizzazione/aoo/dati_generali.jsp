<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
	<html:hidden property="id"/>
	<html:hidden property="versione"/>
	<label for="tipo_aoo"><bean:message key="amministrazione.organizzazione.aoo.tipo"/><span class="obbligatorio"> * </span></label>:
	<html:select styleClass="obbligatorio" property="tipo_aoo">
		<html:optionsCollection property="tipiAoo" value="codice" label="description" />
	</html:select>
	&nbsp;
	<label for="description"><bean:message key="amministrazione.organizzazione.aoo.descrizione"/><span class="obbligatorio"> * </span></label>:
	<html:text property="description" styleClass="obbligatorio" maxlength="100" size="25"></html:text>
	&nbsp;
	<label for="codi_aoo"><bean:message key="amministrazione.organizzazione.aoo.codice"/><span class="obbligatorio"> * </span></label>: 
	<html:text property="codi_aoo" styleClass="obbligatorio" maxlength="100" size="20"></html:text>

	<p><label for="data_istituzione"><bean:message key="amministrazione.organizzazione.aoo.dataistituzione"/><span class="obbligatorio"> * </span></label>:
		<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/aoo/dataIstituzione.jsp" />&nbsp;
		<label for="data_soppressione"><bean:message key="amministrazione.organizzazione.aoo.datasoppressione"/></label>:
		<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/aoo/dataSoppressione.jsp" />
	</p>
	<p>
		<label for="dipartimento_codice"><bean:message key="amministrazione.organizzazione.aoo.codicedipartimento"/><span class="obbligatorio"> * </span></label>:
		<html:text property="dipartimento_codice" maxlength="3" size="4"></html:text>&nbsp;
		<label for="dipartimento_descrizione"><bean:message key="amministrazione.organizzazione.aoo.descrizionedipartimento"/></label>:
		<html:text property="dipartimento_descrizione" maxlength="20" size="20"></html:text>
	</p>
	
	<p>
		<label for="documentoReadable"><bean:message key="amministrazione.organizzazione.aoo.documento.readable"/></label>:
		<html:checkbox property="documentoReadable" />
		<label for="ricercaUfficiFull"><bean:message key="amministrazione.organizzazione.aoo.ricercaUfficiFull"/></label>:
		<html:checkbox property="ricercaUfficiFull" />
	</p>
	<p>
		<label for="idCommittenteFattura"><bean:message key="amministrazione.organizzazione.aoo.idCommittenteFattura"/></label>:
		<html:text property="idCommittenteFattura" maxlength="50" size="20"></html:text>
	</p>
	<p>
		<label for="anniVisibilitaBacheca"><bean:message key="amministrazione.organizzazione.aoo.anniVisibilitaBacheca"/><span class="obbligatorio"> * </span></label>:
		<html:text property="anniVisibilitaBacheca" maxlength="2" size="2"></html:text>
	</p>
	<p>
		<label for="maxRighe"><bean:message key="amministrazione.organizzazione.aoo.maxRighe"/><span class="obbligatorio"> * </span></label>:
		<html:text property="maxRighe" maxlength="5" size="5"></html:text>
	</p>
	<p>
		<label for="flagManuale">Pubblicazione dei documenti firmati nel repertorio</label>:
 		<html:radio property="flagPubblicazioneP7m" styleId="flagFirmato" value="0">
			<label for="flagFirmato">Originali</label>&nbsp;&nbsp;
		</html:radio>
 		<html:radio property="flagPubblicazioneP7m" styleId="flagCopia" value="1">
			<label for="flagCopia">Copia Conforme</label>
		</html:radio>
	 	<html:radio property="flagPubblicazioneP7m" styleId="flagEntrambi" value="2">
			<label for="flagEntrambi">Entrambi</label>
		</html:radio>
	</p>