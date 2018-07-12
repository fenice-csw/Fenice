<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Help">
<p>
<span>
La funzione e' attivabile attraverso la navigazione del Menu selezionando la voce 
Protocollo &gt; Registrazione &gt; E-mail Log
</span>
</p>
<p>
<span>
La funzione consente di visualizzare: 
il log delle e-mail in ingresso scartate con l'indicazione del tipo di errore che ha determinato il cattivo esito delle e-mail in ingresso; 
il log delle e-mail in uscita scartate con l'indicazione del tipo di errore che ha determinato il cattivo esito delle e-mail in uscita; 
il log delle e-mail scartate perche' e' scaduto il relativo certificato. 
</span>
</p>
<p>
<span>
E' possibile eliminare qualunque elemento della lista visualizzata, selezionandolo e premendo il tasto elimina.
</span>
</p>
</eprot:page>