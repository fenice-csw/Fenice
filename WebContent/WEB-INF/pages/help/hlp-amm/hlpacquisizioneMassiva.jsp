<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Help">
<br />
<p>
<span><strong>Acquisizione nuovi documenti</strong></span>
</p>
<span>
<p>
La funzione e' attivabile attraverso la navigazione del Menu selezionando la voce Amministrazione &gt; Acquisizione Massiva &gt; Acquisizione nuovi documenti.
</p>
<p>
Per ogni Area Organizzativa Omogenea e' presente, sul file system, una cartella di lavoro dove vengono memorizzati documenti scansiti con l'operazione di acquisizione massiva
da scanner, pronti per la protocollazione.
</p>
<p>
La funzione, dopo aver attivato opportuni controlli di congruenza e coerenza del nome dei files relativi ai documenti, 
consente, all'utente Amministratore, di acquisire e quindi protocollare i documenti presenti sul file system della relativa Area Omogenea Organizzativa.<br />
Viene prospettata all'utente l'esito dell'operazione con l'indicazione del numero di documenti acquisiti e quelli scartati con l'indicazione del motivo dello
scarto.
I files dei documenti protocollati vengono cancellati dalla directory del file system.<br />
Inoltre l'utente puo' eventualmente scegliere di cancellare direttamente uno o piu'
files utilizzando la specifica opzione.

</p>
</span>
<br />
<p>
<span><strong>Logs acquisizioni</strong></span>
</p>
<span>
<p>
La funzione e' attivabile attraverso la navigazione del Menu selezionando la voce Amministrazione &gt; Acquisizione Massiva &gt; Logs.
</p>
<p>
Per ogni Area Organizzativa Omogenea e' presente una tabella contenente i logs relativi alle acquisizioni dei documenti scansiti e da protocollare, 
che hanno avuto esito negativo.
</p>
<p>
La funzione consente, all'utente Amministratore, di visualizzare i logs dei files corrispondenti ai documenti della relativa Area Organizzativa Omogenea,
scansiti e da protocollare, che non hanno superato gli opportuni controlli di congruenza. 
<br />
</p>
<p>
Si fa presente che, per essere congruo e coerente, il nome del file deve essere di tipo numerico intero cosi' definito: 0000IdProtocollo.pdf (es. 0000259.pdf).
Il sistema scartera' tutti i files il cui nome non e' congruente con la regola di definizione dello stesso; tutti quelli che non hanno un corrispondente protocollo e
tutti quelli che, pur soddisfacendo le suddette condizioni, hanno gia' un documento protocollato.
</p>
</span>
</eprot:page>
