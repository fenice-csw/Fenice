<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<div>
<table>
	<tr>
  	  <td colspan="6"> &nbsp; </td>
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
  	  <td> 
  		  <label for="tipoProtocollo">
		  <bean:message key="protocollo.documento.tipo"/>:
		  </label>
  	  </td>
  	  <td> 
  	 	 <html:select name="reportRegistroForm" property="tipoProtocollo">
	     <option value=""><bean:write name="reportRegistroForm" property="ingressoUscita"/></option>
	     <option value="P"><bean:write name="reportRegistroForm" property="postaInterna"/></option>
	    </html:select>
  	  </td>
    </tr>
    <tr>
    <td colspan="6">
    	<html:submit styleClass="submit" property="btnStampa" value="Stampa" alt="Stampa registro"/>
    	<%--  
    	<html:submit styleClass="submit" property="btnBackup" value="Consolida su CD" alt="Backup dei dati" onclick="javascript:confirm('Prima di procedere controlla che il CD sia inserito nell\\'apposito drive.\n Attenzione! L\\'operazione puo richiedere alcuni minuti.')"/>
    	--%>
    </td>
    </tr>
</table>
</div>


