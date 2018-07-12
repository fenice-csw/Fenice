<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Help">

<%--<%@ page language="java" %>	--%>
<p>
<span><strong>Persone fisiche</strong></span>
</p>
<span>
<p>
La funzione e' attivabile attraverso la navigazione del Menu selezionando la voce Protocollo &gt; Anagrafica persone fisiche &gt; Nuovo/Cerca.
</p>
<p>
La funzione consente di inserire nella rubrica dell'applicazione le persone fisiche.<br />
Per poter inserire nella rubrica una persona fisica e' necessario inserire il campo obbligatorio Cognome,
si possono inoltre inserire altre informazioni anagrafiche come nome, codice fiscale, data di nascita ecc.
</p>
<p>
L'inserimento di questi dati nella rubrica consente di richiamarli, attraverso una selezione, nella registrazione 
del protocollo in ingressso (mittente) ed in quello in uscita (destinatari).<br />
Per confermare la registrazione dei dati di una persona fisica si deve premere il tasto Salva.
</p>
<p>
Le persone fisiche inserite nella rubrica possono essere ricercate per parte di Cognome, Nome o Codice Fiscale 
premendo il tasto Cerca dopo aver inserito il criterio di ricerca.<br />
Dopo aver eseguito la ricerca e' possibile selezionare una persona fisica dalla lista e modificarne i dati.
</p>
</span>
<p>
<span><strong>Persone giuridiche</strong></span>
</p>
<span>
<p>
La funzione e' attivabile attraverso la navigazione del Menu selezionando la voce Protocollo &gt; Anagrafica persone giuridiche &gt; Nuovo/Cerca.
</p>
<p>
La funzione consente di inserire nella rubrica dell'applicazione le persone giuridiche.<br />
Per poter inserire nella rubrica una persona giuridiche e' necessario inserire il campo obbligatorio Denominazione,si possono inoltre inserire altre 
informazioni anagrafiche come partita iva, settore, indirizzo ecc.</p>
<p>
L'inserimento di questi dati nella rubrica consente di richiamarli, attraverso una selezione, nella registrazione del protocollo 
in ingressso (mittente) ed in quello in uscita (destinatari).<br />
Per confermare la registrazione dei dati di una persona fisica si deve premere il tasto Salva.
</p>
<p>
Le persone giuridiche inserite nella rubrica possono essere ricercate per parte di Denominazione o Partita Iva premendo il tasto Cerca dopo aver inserito il criterio di ricerca.<br />
Dopo aver eseguito la ricerca e' possibile selezionare una persona giuridiche dalla lista e modificarne i dati.
</p>
</span>
</eprot:page>