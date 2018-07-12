<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<logic:notEmpty name="visualizzaProtocolloForm" property="destinatari">

<hr></hr>
<table summary="" border="1" width="98%">
	<tr>
		<th>
			<span for="tipoDestinatario">
			<bean:message key="protocollo.destinatario.tipo"/>
			</span>
		</th>
		<th>
			<span for="titoloDestinatario">
			<bean:message key="protocollo.destinatario.titolidestinatario"/>
		    </span>
		</th>
		<th>
			<span for="nominativoDestinatario">
			<bean:message key="protocollo.destinatario.nominativo"/>
			</span>
		</th>
		<th>
			<span for="indirizzoDestinatario">
			<bean:message key="protocollo.destinatario.indirizzo"/><br/>
			</span>
		</th>
		<th>
			<span for="emailDestinatario">
			<bean:message key="protocollo.destinatario.email"/>
			</span>
		</th>
		<th>
			<span for="tipoSpedizione">
			<bean:message key="protocollo.destinatario.tipospedizione"/>
			</span>
		</th>
		<th>
			<span title="Data spedizione" for="dataSpedizione">
			<bean:message key="protocollo.destinatario.dataspedizione"/>
		    </span>
		</th>
		<th>
			<span title="Per conoscenza" for="flagConoscenza">
			<bean:message key="protocollo.destinatario.pc"/>
		    </span>
	    </th>
	    <th>
			<span title="Presso" for="flagPresso">
			<bean:message key="protocollo.destinatario.presso"/>
		    </span>
	    </th>										
	</tr>

<logic:iterate id="currentRecord" name="visualizzaProtocolloForm" property="destinatari">
	<tr>
	
		<td>
			<span>
				<bean:write name="currentRecord" property="flagTipoDestinatario" />
			</span>
		</td>
		<td>
			<span>
				<bean:write name="currentRecord" property="titoloDestinatario" />
			</span>
		</td>
		<td>
			<span>
				<bean:write name="currentRecord" property="destinatario" />
			</span>
		</td>
		<td>
			<logic:notEmpty name="currentRecord" property="indirizzo">
				<span>
					<bean:write name="currentRecord" property="indirizzo" /><br/>
				</span>
			</logic:notEmpty>
			<logic:notEmpty name="currentRecord" property="capDestinatario">
				<span>
					<bean:write name="currentRecord" property="capDestinatario" />
				</span>
			</logic:notEmpty>
			
			<logic:notEmpty name="currentRecord" property="citta">
				<span>
					<bean:write name="currentRecord" property="citta" />
				</span>
			</logic:notEmpty>
			
		</td>
		<td>
			<span>
				<bean:write name="currentRecord" property="email" />
			</span>
		</td>
		<td>
	  		<span>
	  			<bean:write name="currentRecord" property="mezzoDesc"/><br>
	  		</span>
		</td>
		<td>
			<span>
				<bean:write name="currentRecord" property="dataSpedizione" /><br>
			</span>
		</td>
		<td>
			<span>
				<bean:write name="currentRecord" property="conoscenza" />
			</span>
		</td>
		<td>
			<span>
				<bean:write name="currentRecord" property="presso" />
			</span>
		</td>
	</tr>
</logic:iterate> 
</table>
</logic:notEmpty>



