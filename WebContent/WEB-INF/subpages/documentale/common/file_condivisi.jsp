<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<html:xhtml />
<div>

	<display:table class="simple" width="100%" requestURI="/page/documentale/documentiCondivisi.do"
    name="documentiCondivisiForm" property="fileCondivisi" 
	export="false" sort="list" pagesize="30" id="currentRecord">
		<display:column title="Nome file">
			<html:link paramId="documentoSelezionatoId" paramName="currentRecord" paramProperty="id" page="/page/documentale/documentiCondivisi.do">
   			<span><bean:write name="currentRecord" property="nomeFile" /></span>
  			</html:link>
		</display:column>
		<display:column property="dataDocumentoView" title="Data" />
		<display:column property="oggetto" title="Oggetto" />
		<display:column property="descrizioneArgomento" title="Argomento" />
		<display:column property="rowCreatedUser" title="Owner" />
		<display:column title="Stato">
		 <logic:equal name="currentRecord" property="statoArchivio" value="L">
		 <bean:message key="documentale.lavorazione"/>
		 </logic:equal>
		 <logic:equal name="currentRecord" property="statoArchivio" value="C">
		 <bean:message key="documentale.classificato"/>
		 </logic:equal>
		 <logic:equal name="currentRecord" property="statoArchivio" value="I">
		  <bean:message key="documentale.inviatoprotocollo"/>
		 </logic:equal>
		 <logic:equal name="currentRecord" property="statoArchivio" value="R">
		  <bean:message key="documentale.inviatorepertorio"/>
		 </logic:equal>
		 <logic:notEqual name="currentRecord" property="statoDocumento" value="0">
		   <bean:message key="documentale.checkedout"/>: <bean:write name="currentRecord" property="usernameLav"/>
		 </logic:notEqual>
		</display:column>
		<display:column title="Assegnato da" property="assegnatoDa" />
		<display:column title="Messaggio" property="messaggio" />
	</display:table>
</div>

