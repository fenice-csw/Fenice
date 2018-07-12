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
Documentale &gt; Fascicoli
</span>
</p>
<p>
<span>
La funzione consente di effettuare la ricerca dei fascicoli. 
</span>
</p>
<p>
<span>
Se non viene impostato alcun parametro di ricerca, si ottiene la lista di tutti i fascicoli; 
se, invece, si imposta uno o piu' parametri di ricerca, si ottiene la lista dei fascicoli corrispondenti ai parametri impostati.
</span>
</p>
<p>
<span>
Selezionando il fascicolo d'interesse, appare a video il dettaglio dello stesso, con l'indicazione dei protocolli e dei documenti relativi al fascicolo.
</span>
</p>
<p>
<span>
Si puo procedere alla modifica, alla chiusura, all'invio al protocollo e alla cancellazione del fascicolo.
Selezionando il protolocollo-documento d'interesse, appare a video il dettaglio dello stesso ed e' possibile modificarlo, annullarlo o visualizzarne la storia.
</span>
</p>
</eprot:page>
