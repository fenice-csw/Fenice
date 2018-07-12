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
La funzione e' attivabile attraverso la navigazione del Menu selezionando la voce Protocollo &gt; Ricerca.
</span>
</p>
<p>
<span>
La funzione consente di ricercare i documenti protocollati impostando appositi filtri di ricerca (Data di Registrazione, Numero di Protocollo, Oggetto,  Mittente,  Destinatario, Tipologia del Documento ecc.) e premendo il tasto cerca. 
In questo caso appare a video la lista dei documenti protocollati corrispondenti ai criteri di ricerca.
</span>
</p>
<p>
<span>
Se non viene impostato alcun parametro di ricerca, premendo il tasto cerca appare a video la lista di tutti i documenti protocollati.
</span>
</p>
<p>
<span>
Ciascuno dei protocolli della lista puo' essere visualizzato nel dettaglio cliccando sul numero del protocollo corrispondente e puo' essere aggiornato se l'utente dispone dei permessi necessari alla modifica.
</span>
</p>
</eprot:page>