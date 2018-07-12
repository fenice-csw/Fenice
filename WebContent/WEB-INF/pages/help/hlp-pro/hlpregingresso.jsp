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
Protocollo &gt; Registrazione &gt; Documento in ingresso
</span>
</p>
<p>
<span>
La funzione consente di registrare, mediante assegnazione di un numero di protocollo univoco all'interno di un registro, i documenti in ingresso ad ogni singola Area Organizzativa Omogenea. 
</span>
</p>
<p>
<span>
Tutti i documenti da protocollare saranno registrati con le informazioni dell'Area Organizzativa Omogenea di appartenenza, della data e del numero di protocollo assegnati in automatico. 
Per effettuare una registrazione e' necessario avvalorare i campi obbligatori Tipo, Oggetto, Tipo Mittente e Denominazione se persona giuridica o Cognome se persona fisica.
E' necessario, inoltre, assegnare il documento da protocollare ad un ufficio o ad un utente premendo il tasto Assegnatari e selezionando l'ufficio e/o l'utente al quale assegnare il documento.
Per confermare i dati inseriti si deve premere il tasto Conferma.
</span>
</p>
<p>
<span>
Il sistema, dopo la registrazione, prospetta in alto a destra il numero e la data del protocollo appena inserito.
Per protocollare un documento elettronico occorre premere il tasto Sfoglia e selezionare il documento sul proprio PC.
Successivamente si deve inviare il file selezionato al Server (premendo il tasto Allega adiacente al tasto Sfoglia) e premere il tasto Conferma.
</span>
</p>
</eprot:page>