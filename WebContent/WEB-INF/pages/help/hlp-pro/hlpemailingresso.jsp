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
Protocollo &gt; Registrazione &gt; E-mail in ingresso.
</span>
</p>
<p>
<span>
La funzione consente di avere la lista delle e-mail scaricate nella base dati e non ancora gestite.
Lutente puo' selezionare un messaggio e cancellarlo o protocollarlo.
</span>
</p>
<p>
<span>
Il body del messaggio viene convertito in PDF.
Se il messaggio non contiene allegati, il body del messaggio diventa documento principale
loggetto della e-mail confluisce nelloggetto del protocollo e il mittente della e-mail diventa mittente protocollo.
</span>
</p>
<p>
<span>
Se il messaggio contiene allegati, bisogna selezionare il documento principale tra il body del messaggio e uno dei documenti pdf in allegato(se in allegato non vi sono Pdf si assume come documento principale il body del messaggio);
tutti gli altri documenti allegati della e-mail diventano allegati del protocollo, loggetto della e-mail confluisce nelloggetto del protocollo
e il mittente della e-mail diventa mittente protocollo.
</span>
</p>
<p>
<span>
Durante questa operazione lutente viene notificato nel caso in cui vi siano delle irregolarita' nella firma dei documenti.
In qualsiasi momento lutente puo' visionare il certificato e-mail del mittente e dei file firmati.
Dopo la registrazione del protocollo il messaggio e-mail viene cancellato dal database.
</span>
</p>
</eprot:page>