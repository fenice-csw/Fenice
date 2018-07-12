<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>


<html:xhtml />

<div>
	<logic:notEmpty name="listaDomandeForm" property="domandeCollection">
	
	<display:table class="simple" width="100%" requestURI="/page/domande/elenco.do?caricaLista=true"
			name="sessionScope.listaDomandeForm.domandeCollection"
			export="false" sort="list" pagesize="50" id="row">
			
			<display:column title=""> 
			
			<input type="checkbox" name="domandaChkBox" value="<bean:write name='row' property='domandaId'/>"/>
			</display:column>
			<display:column property="domandaId" title="N. domanda" />
			<display:column property="dataIscrizione" title="Data iscrizione" />
				<display:column title="Mittente">
						<bean:write name="row" property="cognome" />
						<bean:write name="row" property="nome" />
				</display:column>
			<display:column title="Oggetto" property="oggetto" />
			<display:column title="Comune" property="comune" />
			<display:column title="Stato" property="descrizioneStato" />
			<display:column title="Azioni">
				<logic:equal name="row" property="stato"value="1">
					[<html:link  action="/page/domande/elenco.do" 
						paramId="accettaDomanda" paramProperty="domandaId" paramName="row" >Protocolla</html:link>]
					[<html:link  action="/page/domande/elenco.do" 
						paramId="eliminaDomanda" paramProperty="domandaId" paramName="row" >Elimina</html:link>]
				</logic:equal>
				<logic:equal name="row" property="stato"value="3">
				[<html:link  action="/page/domande/elenco.do" 
					paramId="visualizzaProtocollo" paramProperty="domandaId" paramName="row">Visualizza</html:link>]
				[<html:link  action="/page/domande/elenco.do" 
						paramId="ripetiDomanda" paramProperty="domandaId" paramName="row" >Ripeti Simile</html:link>]
				</logic:equal>
			</display:column>	
		</display:table>
		<p>
		 <html:submit styleClass="submit" property="btnElimina" value="Elimina i Selezionati"	alt="Elimina le domande selezionati" />
		 
		</p>
	</logic:notEmpty>
</div>

