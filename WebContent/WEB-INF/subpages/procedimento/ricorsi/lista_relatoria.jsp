<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />

		<table id="tabella_mittenti" >
			<tr>
		    	<th><span>Procedimento Nr.</span></th>
		    	<th><span>Oggetto</span></th>
		    	<th><span>Data Avvio</span></th>
		    	<th><span>Ricorrente</span></th>
		  	</tr>
		<logic:iterate id="row" name="alertProcedimentoForm" property="procedimentiULLCollection">
	  		<logic:equal name="row" property="statoId" value="4">
	  		<tr>    
			    <td>
				    <html:link action="/page/procedimento/ricorsi.do" paramName="row"
							paramId="visualizzaProcedimentoId" paramProperty="procedimentoId">
							<bean:write name="row" property="numeroProcedimento" />
					</html:link>
			    </td>
			    <td><span>
			    	<bean:write name="row" property="oggetto" />
			    </span></td>
			     <td><span>
			    	<bean:write name="row" property="dataAvvioView" />
			    </span></td>
			    <td><span>
			    	<bean:write name="row" property="interessato" />
			    </span></td>
	  		</tr>
	  		</logic:equal>
		</logic:iterate>
		</table>




