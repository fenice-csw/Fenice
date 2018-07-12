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
Amministrazione &gt; Gestione Firma Digitale &gt; Importa Lista CA
</span>
</p>
<p>
<span>
La funzione consente di inviare il file firmato e zippato contenente la lista dei certificati del CA riconosciute dal CNIPA. 
Vengono effettuate le seguenti operazioni:
Estrazione del contenuto in chiaro del file (da p7m a zip); 
Cancellazione di tutte le CA presenti nella base dati;
Estrazione di tutti i file contenuti all'interno del file zip.
Per ogni file il sistema cerca di estrarre le informazioni della CA; se non risulta un Certificato X509 valido, alla fine del processo, viene visualizzato un errore.
Se il sistema individua un X509 valido allora estrae, oltre alle informazioni di base (marcate obbligatorie dal decreto legge sulle normative delle CA italiane), anche eventuali punti di distribuzione delle revoche (CRL).
Il sistema inserisce la CA nella tabella ca_lista ed eventuali links alle CRL nella tabella ca_crl.
Il processo continua fino a quando tutti i file (contenuti nel file zip) non sono stati processati.
Alla fine di tale processo vengono visualizzati gli eventuali errori riscontrati.
</span>
</p>
<p>
<span>
La funzione consente di inviare il file firmato e zippato contenente la lista dei certificati del CA riconosciute dal CNIPA. 
Vengono effettuate le seguenti operazioni:
Estrazione del contenuto in chiaro del file (da p7m a zip); 
Cancellazione di tutte le CA presenti nella base dati;
Estrazione di tutti i file contenuti all'interno del file zip.
Per ogni file il sistema cerca di estrarre le informazioni della CA; se non risulta un Certificato X509 valido, alla fine del processo, viene visualizzato un errore.
Se il sistema individua un X509 valido allora estrae, oltre alle informazioni di base (marcate obbligatorie dal decreto legge sulle normative delle CA italiane), anche eventuali punti di distribuzione delle revoche (CRL).
Il sistema inserisce la CA nella tabella ca_lista ed eventuali links alle CRL nella tabella ca_crl.
Il processo continua fino a quando tutti i file (contenuti nel file zip) non sono stati processati.
Alla fine di tale processo vengono visualizzati gli eventuali errori riscontrati.
</span>
</p>
<p>
<span>
E' inoltre possibile aggiornare la base dati con tutte le Revoche trovate nei punti di distribuzione CRL.  
La stessa funzione viene attivata automaticamente (ogni mattina alle 4:00) dal sistema.
Durante tale processo per ogni link di CRL presente nella base dati (tabella ca_crl) vengono effettuate le seguenti operazioni:
Individuazione del tipo di protocollo del link, che supporta http, HTTPS ed LDAP, in linea con quanto specificato dal decreto legge (DPR 07/04/2003). 
Nel caso in cui il protocollo non e' supportato, viene visualizzato un apposito messaggio di errore che viene inserito nel relativo log.
Download del file presente al link e validazione del formato del file scaricato,  che deve essere di tipo CRL (come previsto dal DPR 07/04/2003). 
In caso di errore, lo stesso viene visualizzato alla fine del processo e inserito nel relativo log.
Estrazione di tutti i record presenti nel file ed inserimento delle relative informazioni nella base dati (tabella ca_revoked_list).
</span>
</p>
</eprot:page>
