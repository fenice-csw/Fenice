<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<div>
<table>
	<%-- 
		<tr>
  	  		<td colspan="6"> &nbsp; </td>
    	</tr>
    --%>
	
    <tr>
     	<td class="label">
     	 	<span><bean:message key="protocollo.tipo"/>:</span>
    	</td>
        <td colspan="6">
            <html:radio property="tipoProtocollo" value="P','I" styleId="tipo_tutti"
            		onclick="document.forms[0].submit()" >
            		<label for="tipo_tutti">
						Tutti
                	</label>
            </html:radio>&nbsp;
			
			<logic:iterate id="tipo" name="tipiProtocollo">	
				<bean:define id="codice" name="tipo" property="codice" />
				 
				<logic:notEqual name="tipo" property="descrizione" value="Uscita">	
					<html:radio property="tipoProtocollo"
                		value="codice" idName="tipo" onclick="document.forms[0].submit()">
                		<label for="tipo_<bean:write name='tipo' property='codice' />"><bean:write name="tipo" property="descrizione" /></label>
            		</html:radio>&nbsp;
    			</logic:notEqual>
    			
    		</logic:iterate>
            <noscript>
              <div>
            	<html:submit property="btnImpostaTipo" value="Imposta" 
            	    styleClass="button" title="Imposta il tipo di protocollo" />
              </div>
            </noscript>
          </td>
        </tr>
    <tr>
  	  <td> 
  	  	<label for="dataInizio">
			<bean:message key="report.datainizio"/><span class="obbligatorio"> * </span>:
		</label>
	  </td>
  	  <td>
  	  	<html:text property="dataInizio" size="10" styleId="dataInizio" styleClass="obbligatorio" maxlength="10" />
    	<eprot:calendar textField="dataInizio" />&nbsp;
  	  </td>
  	  <td>
  	 	 <label for="dataFine">
			<bean:message key="report.datafine"/><span class="obbligatorio"> * </span>:
		 </label>
  	  </td>
  	  <td>
  		  <html:text property="dataFine" styleId="dataFine" styleClass="obbligatorio" size="10" maxlength="10" />
          <eprot:calendar textField="dataFine" />
   		  &nbsp;
  	  </td>
  	   </tr>
  	   <tr>
  	   <td> 
  	  	<label for="numInizio">
			<bean:message key="report.numinizio"/>:
		</label>
	  </td>
  	  <td>
  	  	<html:text property="numInizio" size="6" styleId="numInizio" maxlength="6" />
  	  </td>
  	   <td> 
  	  	<label for="numFine">
			<bean:message key="report.numfine"/>:
		</label>
	  </td>
  	  <td>
  	  	<html:text property="numFine" size="6" styleId="numFine" maxlength="6" />
  	  </td>
    </tr>
    <tr>
		<td class="label">
			<label for="flagConoscenza"> 
				Per Conoscenza: 
			</label>
		</td>
		<td>
			<html:checkbox property="flagConoscenza" styleId="flagConoscenza"> </html:checkbox>
		</td>
	</tr>
    <tr>
    <td colspan="6"><jsp:include page="uffici.jsp" /><br /> </td>
    </tr>
    <tr>
    <td colspan="6">
    	<html:submit styleClass="submit" property="btnStampa" value="Stampa" alt="Stampa registro"/>
    	<html:submit styleClass="submit" property="btnStampaTutti" value="Stampa Tutti" alt="Stampa registro"/>
    </td>
    </tr>
</table>
</div>


