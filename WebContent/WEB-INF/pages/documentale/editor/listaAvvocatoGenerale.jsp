<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Lista Documenti Editor">

	<html:form action="/page/documentale/avvocatoGenerale">
	
	<logic:equal name="avvocatoGeneraleForm" property="faseRelatoria" value="true">
		<div class="sezione">
			<span class="title"> <strong>Relazioni</strong></span>
				<table id="tabella_mittenti">
			<tr>
		    	<th><span>Id</span></th>
		    	<th><span>Oggetto</span></th>
		    	<th><span>Autore</span></th>
		    	<th><span>Numero Procedimento</span></th>
		    	<th><span>Azioni</span></th>
		  	</tr>
		<logic:iterate id="row" name="avvocatoGeneraleForm" property="documentiCollection">
	  		<logic:equal name="row" property="statoProcedimento" value="4">
	  		<tr> 
			    <td>
				    <html:link action="/page/documentale/daTemplate.do" paramName="row"
							paramId="docId" paramProperty="documentoId">
							<bean:write name="row" property="documentoId" />
					</html:link>
			    </td>
			    <td><span>
			    	<bean:write name="row" property="oggetto" />
			    </span></td>
			    <td><span>
			    	<bean:write name="row" property="mittente" />
			    </span></td>
			    <td>
			    	<span>
			    		<bean:write name="row" property="numeroProcedimento" />
			    	</span>
			    </td>
			    <td>
			    	<html:link action="/page/documentale/avvocatoGenerale.do"
						paramName="row" paramId="stampa"
						paramProperty="documentoId">
						[Stampa]
					</html:link>
					<html:link action="/page/documentale/avvocatoGenerale.do"
						paramName="row" paramId="lavorato"
						paramProperty="documentoId">
						[Lavorato]
					</html:link>
			    </td>
	  		</tr>
	  		</logic:equal>
		</logic:iterate>
		</table>
	</div>
	</logic:equal>
	
	<logic:equal name="avvocatoGeneraleForm" property="faseDecretoPresidente" value="true">
	<div class="sezione">
			<span class="title"> <strong>Decreti</strong></span>
				<table id="tabella_mittenti" >
			<tr>
				<th></th>
		    	<th><span>Id</span></th>
		    	<th><span>Oggetto</span></th>
		    	<th><span>Autore</span></th>
		    	<th><span>Numero Procedimento</span></th>
		    	<th><span>Azioni</span></th>
		  	</tr>
		<logic:iterate id="row" name="avvocatoGeneraleForm" property="documentiCollection">
	  		<logic:equal name="row" property="statoProcedimento" value="6">
	  		<tr> 
	  			<td>
	  				<input type="checkbox" name="chkBox" value="<bean:write name='row' property='documentoId'/>"/>
	  			</td>   
			    <td>
				    <bean:write name="row" property="documentoId" />
			    </td>
			    <td><span>
			    	<bean:write name="row" property="oggetto" />
			    </span></td>
			    <td><span>
			    	<bean:write name="row" property="mittente" />
			    </span></td>
			    <td>
			    	<span>
			    		<bean:write name="row" property="numeroProcedimento" />
			    	</span>
			    </td>
			    
			    <td>
			    	<html:link action="/page/documentale/avvocatoGenerale.do"
						paramName="row" paramId="stampa"
						paramProperty="documentoId">
						[Stampa]
					</html:link>
			    </td>
	  		</tr>
	  		</logic:equal>
		</logic:iterate>
		</table>
		<p>
		 <html:submit styleClass="submit" property="btnStampaElencoDecreti" value="Stampa Elenco Descreti"	alt="Stampa Elenco Descreti" />
		 <html:submit styleClass="submit" property="btnElencoDecretiLavorati" value="Lavora i Selezionati"	alt="Lavora i Selezionati" />
		</p>
	</div>
	</logic:equal>
	</html:form>
</eprot:page>