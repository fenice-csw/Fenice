<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<eprot:page title="Lista email uscita">
	
	<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />
	<html:form action="/page/protocollo/ingresso/emailLog.do">
	
	<div class="sezione">
		<span class="title"><strong><bean:message key="fascicolo.datiricerca"/></strong></span>
		<table>
		<!--  -->   
		    <tr>
				<td> 
			  	  	<label for="statoMail">
						Stato:
					</label>
				  </td>		    	
		          <td>
					<html:select property="statoMail" styleId="statoMail">
							<option value="" label="tutte">
							<html:optionsCollection name="emailUscitaForm"  property="statiMail" value="codice" label="description" />
					</html:select>
		          </td>
		          <td>&nbsp;</td>
			  	  <td> 
			  	  	<label for="dataInizio">
						Data Creazione dal:
					</label>
				  </td>
			  	  <td>
			  	  	<html:text property="dataInizio" size="10" styleId="dataInizio" styleClass="obbligatorio" maxlength="10" />
			    	<eprot:calendar textField="dataInizio" />&nbsp;
			  	  </td>
			  	  <td> 
			  	  	<label for="dataFine">
						Al:
					</label>
				  </td>
			  	  <td>
			  		  <html:text property="dataFine" styleId="dataFine" styleClass="obbligatorio" size="10" maxlength="10" />
			          <eprot:calendar textField="dataFine" />
			   		  &nbsp;
			  	  </td>
			    <td>
			    	<html:submit styleClass="submit" property="cercaAction" value="Cerca" alt="Cerca tra le email in uscita"/>
			    </td>
			</tr>
		</table>
	</div>
	
		<div>
		<logic:notEmpty name="emailUscitaForm" property="mailUscita">
			<display:table class="simple" width="100%"
				requestURI="/page/protocollo/ingresso/emailLog.do"
				name="sessionScope.emailUscitaForm.mailUscitaCollection" export="false"
				sort="list" pagesize="10" id="row">
				<display:column title="Data creazione" property="dataCreazione" />
				<display:column title="Data spedizione" property="dataInvio" />
								
				<display:column title="Numero Protocollo">
					<a href="<%= request.getContextPath()%>/page/protocollo/uscita/summaryview.do?type=U&protocolloId=<bean:write name='row' property='protocolloId' />" >
						<bean:write name="row" property="numeroAnnoProtocollo"/>
					</a>
				</display:column>
				
				<display:column title="Oggetto Protocollo" property="oggettoProtocollo"/>
				<display:column title="Inviata a">
					<ul>
					 <logic:iterate id="currentRecord" property="destinatari" name="row">
						<li>
							<bean:write name="currentRecord" property="nominativo" /> (<bean:write name="currentRecord" property="email" />)
						</li>
						</logic:iterate>
					</ul>
				</display:column>
				<display:column title="Stato" property="descrizioneStato"/>
				<display:column title="Azioni">
					[<html:link  action="/page/protocollo/ingresso/emailLog.do"
						paramId="elimina" paramProperty="mailId" paramName="row">Elimina</html:link>]
					[<html:link  action="/page/protocollo/ingresso/emailLog.do"
						paramId="modifica" paramProperty="mailId" paramName="row">Modifica</html:link>]
			</display:column>
			</display:table>
		</logic:notEmpty>
		<!--  
		<logic:empty name="emailUscitaForm" property="mailUscita">
			Nessuna Email in Uscita.		 
		</logic:empty>
		-->
		</div>
	</html:form>

</eprot:page>
