<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html xmlns="http://www.w3.org/1999/xhtml\">
<head>
	<title>Fenice - Segni diacritici</title>
    <link rel='stylesheet' type='text/css' href='/eprot/style/style.css' />
	<style type='text/css'>@import url('/eprot/script/calendar/calendar-blue.css');</style>
	<script type='text/javascript' src='/eprot/script/calendar/calendar.js'></script>
	<script type='text/javascript' src='/eprot/script/calendar/lang/calendar-it.js'></script>
	<script type='text/javascript' src='/eprot/script/calendar/calendar-setup.js'></script>
	<script type='text/javascript' src='/eprot/script//script/doc/albero.js'></script>

	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<style>
		textarea{
			background-image:url();
			color:ff0000;
		}
	</style>
	
<%
	String campo = request.getParameter("campo");
%>

	<SCRIPT>
		function segn(ogg) {
		<% if ( campo!=null ) { %>
			opener.document.forms[0].<%=campo%>.value = opener.document.forms[0].<%=campo%>.value + ogg
			opener.document.forms[0].<%=campo%>.className = "formcol"
		<% } else { %>
			opener.document.forms[0].note.value = opener.document.forms[1].note.value + ogg
			opener.document.forms[0].note.className = "formcol"
		<% } %>
			self.close();
		}
	</SCRIPT>
	
	
</head>

<body>
	<br/><br/>
	<table align = "center" valign="center" border="1" cellpadding="3" cellspacing="0" width="80%">
		<TR bgcolor="white">
			<TD align="left" valign="top" width="100">
				<img title="top" align="center" border="0" src="<html:rewrite page='/images/menu/topleft.gif'/>">				
			</TD>
			<TD align="center" valign="center" width="300" colspan=2>
				<font face="Times New Roman" color=black size=6><I><b>Segni Diacritici</b></I></font>
			</TD>
		</TR>
	
		<tr valign="top">
			<th align="left" bgcolor=gray>Character</th>
			<th align="center" bgcolor=gray>&nbsp;HTML 4.0&nbsp;<br></th>
			<th align="left" bgcolor=gray>Character Name</th>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&#126;");'>&#126;</a></td>
			<td>&nbsp;</td>
			<td>tilde</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;euro;");'>&#128;</a></td>
			<td>&amp;euro;</td>
			<td>euro sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;sbquo;");'>&#130;</a></td>
			<td>&amp;sbquo;</td>
			<td>single low-9 quotation mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;fnof;");'>&#131;</a></td>
			<td>&amp;fnof;</td>
			<td>Lowercase letter f with hook</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;bdquo;");'>&#132;</a></td>
			<td>&amp;bdquo;</td>
			<td>double low-9 quotation mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;hellip;");'>&#133;</a></td>
			<td>&amp;hellip;</td>
			<td>horizontal ellipsis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;dagger;");'>&#134;</a></td>
			<td>&amp;dagger;</td>
			<td>dagger</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Dagger;");'>&#135;</a></td>
			<td>&amp;Dagger;</td>
			<td>double dagger</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;circ;");'>&#136;</a></td>
			<td>&amp;circ;</td>
			<td>modifier letter circumflex accent</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;permil;");'>&#137;</a></td>
			<td>&amp;permil;</td>
			<td>per mille sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Scaron;");'>&#138;</a></td>
			<td>&amp;Scaron;</td>
			<td>Capital letter S with caron</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;lsaquo;");'>&#139;</a></td>
			<td>&amp;lsaquo;</td>
			<td>single left-pointing angle quotation mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;OElig;");'>&#140;</a></td>
			<td>&amp;OElig;</td>
			<td>Capital ligature OE</td>
		</tr>
	
		<!--
	    <tr valign="top"> 
	      <td ><a href='javascript:segn("&#142;");'>&#142;</a></td>
	      
	      <td>&nbsp;</td>
	      <td>Capital letter Z with caron</td>
	    </tr>
	    -->
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;lsquo;");'>&#145;</a></td>
			<td>&amp;lsquo;</td>
			<td>left single quotation mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;rsquo;");'>&#146;</a></td>
			<td>&amp;rsquo;</td>
			<td>right single quotation mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ldquo;");'>&#147;</a></td>
			<td>&amp;ldquo;</td>
			<td>left double quotation mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;rdquo;");'>&#148;</a></td>
			<td>&amp;rdquo;</td>
			<td>right double quotation mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;bull;");'>&#149;</a></td>
			<td>&amp;bull;</td>
			<td>bullet</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ndash;");'>&#150;</a></td>
			<td>&amp;ndash;</td>
			<td>en dash</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;mdash;");'>&#151;</a></td>
			<td>&amp;mdash;</td>
			<td>em dash</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;tilde;");'>&#152;</a></td>
			<td>&amp;tilde;</td>
			<td>small tilde</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;trade;");'>&#153;</a></td>
			<td>&amp;trade;</td>
			<td>trade mark sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;scaron;");'>&#154;</a></td>
			<td>&amp;scaron;</td>
			<td>Lowercase letter s with caron</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;rsaquo;");'>&#155;</a></td>
			<td>&amp;rsaquo;</td>
			<td>single right-pointing angle quotation mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;oelig;");'>&#156;</a></td>
			<td>&amp;oelig;</td>
			<td>Lowercase ligature oe</td>
		</tr>
	
		<!--   
	     <tr valign="top"> 
	       <td ><a href='javascript:segn("&#158;");'>&#158;</a></td>
	       <td>&nbsp;</td>
	       <td>Lowercase letter z with caron</td>
	     </tr>
	    -->
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Yuml;");'>&#159;</a></td>
			<td>&amp;Yuml;</td>
			<td>Capital letter Y with diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><span style="background-color:#c0c0c0;"><a href='javascript:segn("&amp;nbsp;");'>&#160;</a></span></td>
			<td>&amp;nbsp;</td>
			<td>no-break space</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;iexcl;");'>&#161;</a></td>
			<td>&amp;iexcl;</td>
			<td>inverted exclamation mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;cent;");'>&#162;</a></td>
			<td>&amp;cent;</td>
			<td>cent sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;pound;");'>&#163;</a></td>
			<td>&amp;pound;</td>
			<td>pound sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;curren;");'>&#164;</a></td>
			<td>&amp;curren;</td>
			<td>currency sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;yen;");'>&#165;</a></td>
			<td>&amp;yen;</td>
			<td>yen sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;brvbar;");'>&#166;</a></td>
			<td>&amp;brvbar;</td>
			<td>broken bar</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;sect;");'>&#167;</a></td>
			<td>&amp;sect;</td>
			<td>section sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;uml;");'>&#168;</a></td>
			<td>&amp;uml;</td>
			<td>diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;copy;");'>&#169;</a></td>
			<td>&amp;copy;</td>
			<td>copyright sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ordf;");'>&#170;</a></td>
			<td>&amp;ordf;</td>
			<td>feminine ordinal indicator</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;laquo;");'>&#171;</a></td>
			<td>&amp;laquo;</td>
			<td>left-pointing double angle quotation mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;not;");'>&#172;</a></td>
			<td>&amp;not;</td>
			<td>not sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;shy;");'>&#173;</a></td>
			<td>&amp;shy;</td>
			<td>soft hyphen</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;reg;");'>&#174;</a></td>
			<td>&amp;reg;</td>
			<td>registered sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;macr;");'>&#175;</a></td>
			<td>&amp;macr;</td>
			<td>macron</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;deg;");'>&#176;</a></td>
			<td>&amp;deg;</td>
			<td>degree sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;plusmn;");'>&#177;</a></td>
			<td>&amp;plusmn;</td>
			<td>plus-minus sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;sup2;");'>&#178;</a></td>
			<td>&amp;sup2;</td>
			<td>superscript two</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;sup3;");'>&#179;</a></td>
			<td>&amp;sup3;</td>
			<td>superscript three</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;acute;");'>&#180;</a></td>
			<td>&amp;acute;</td>
			<td>acute accent</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;micro;");'>&#181;</a></td>
			<td>&amp;micro;</td>
			<td>micro sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;para;");'>&#182;</a></td>
			<td>&amp;para;</td>
			<td>pilcrow sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;middot;");'>&#183;</a></td>
			<td>&amp;middot;</td>
			<td>middle dot</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;cedil;");'>&#184;</a></td>
			<td>&amp;cedil;</td>
			<td>cedilla</td>
		</tr>
		
		<tr valign="top">
			<td><a href='javascript:segn("&amp;sup1;");'>&#185;</a></td>
			<td>&amp;sup1;</td>
			<td>superscript one</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ordm;");'>&#186;</a></td>
			<td>&amp;ordm;</td>
			<td>masculine ordinal indicator</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;raquo;");'>&#187;</a></td>
			<td>&amp;raquo;</td>
			<td>right-pointing double angle quotation mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;frac14;");'>&#188;</a></td>
			<td>&amp;frac14;</td>
			<td>vulgar fraction one quarter</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;frac12;");'>&#189;</a></td>
			<td>&amp;frac12;</td>
			<td>vulgar fraction one half</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;frac34;");'>&#190;</a></td>
			<td>&amp;frac34;</td>
			<td>vulgar fraction three quarters</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;iquest;");'>&#191;</a></td>
			<td>&amp;iquest;</td>
			<td>inverted question mark</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Agrave;");'>&#192;</a></td>
			<td>&amp;Agrave;</td>
			<td>Capital letter A with grave</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Aacute;");'>&#193;</a></td>
			<td>&amp;Aacute;</td>
			<td>Capital letter A with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Acirc;");'>&#194;</a></td>
			<td>&amp;Acirc;</td>
			<td>Capital letter A with circumflex</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Atilde;");'>&#195;</a></td>
			<td>&amp;Atilde;</td>
			<td>Capital letter A with tilde</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Auml;");'>&#196;</a></td>
			<td>&amp;Auml;</td>
			<td>Capital letter A with diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Aring;");'>&#197;</a></td>
			<td>&amp;Aring;</td>
			<td>Capital letter A with ring above</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;AElig;");'>&#198;</a></td>
			<td>&amp;AElig;</td>
			<td>Capital letter AE</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Ccedil;");'>&#199;</a></td>
			<td>&amp;Ccedil;</td>
			<td>Capital letter C with cedilla</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Egrave;");'>&#200;</a></td>
			<td>&amp;Egrave;</td>
			<td>Capital letter E with grave</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Eacute;");'>&#201;</a></td>
			<td>&amp;Eacute;</td>
			<td>Capital letter E with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Ecirc;");'>>&#202;</a></td>
			<td>&amp;Ecirc;</td>
			<td>Capital letter E with circumflex</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Euml;");'>&#203;</a></td>
			<td>&amp;Euml;</td>
			<td>Capital letter E with diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Igrave;");'>&#204;</a></td>
			<td>&amp;Igrave;</td>
			<td>Capital letter I with grave</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Iacute;");'>&#205;</a></td>
			<td>&amp;Iacute;</td>
			<td>Capital letter I with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Icirc;");'>&#206;</a></td>
			<td>&amp;Icirc;</td>
			<td>Capital letter I with circumflex</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Iuml;");'>&#207;</a></td>
			<td>&amp;Iuml;</td>
			<td>Capital letter I with diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ETH;");'>&#208;</a></td>
			<td>&amp;ETH;</td>
			<td>Capital letter Eth</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Ntilde;");'>&#209;</a></td>
			<td>&amp;Ntilde;</td>
			<td>Capital letter N with tilde</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Ograve;");'>&#210;</a></td>
			<td>&amp;Ograve;</td>
			<td>Capital letter O with grave</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Oacute;");'>&#211;</a></td>
			<td>&amp;Oacute;</td>
			<td>Capital letter O with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Ocirc;");'>&#212;</a></td>
			<td>&amp;Ocirc;</td>
			<td>Capital letter O with circumflex</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Otilde;");'>&#213;</a></td>
			<td>&amp;Otilde;</td>
			<td>Capital letter O with tilde</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Ouml;");'>&#214;</a></td>
			<td>&amp;Ouml;</td>
			<td>Capital letter O with diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;times;");'>&#215;</a></td>
			<td>&amp;times;</td>
			<td>multiplication sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Oslash;");'>&#216;</a></td>
			<td>&amp;Oslash;</td>
			<td>Capital letter O with stroke</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Ugrave;");'>&#217;</a></td>
			<td>&amp;Ugrave;</td>
			<td>Capital letter U with grave</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Uacute;");'>&#218;</a></td>
			<td>&amp;Uacute;</td>
			<td>Capital letter U with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Ucirc;");'>&#219;</a></td>
			<td>&amp;Ucirc;</td>
			<td>Capital letter U with circumflex</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Uuml;");'>&#220;</a></td>
			<td>&amp;Uuml;</td>
			<td>Capital letter U with diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;Yacute;");'>&#221;</a></td>
			<td>&amp;Yacute;</td>
			<td>Capital letter Y with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;THORN;");'>&#222;</a></td>
			<td>&amp;THORN;</td>
			<td>Capital letter Thorn</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;szlig;");'>&#223;</a></td>
			<td>&amp;szlig;</td>
			<td>Lowercase letter sharp s</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;agrave;");'>&#224;</a></td>
			<td>&amp;agrave;</td>
			<td>Lowercase letter a with grave</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;aacute;");'>&#225;</a></td>
			<td>&amp;aacute;</td>
			<td>Lowercase letter a with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;acirc;");'>&#226;</a></td>
			<td>&amp;acirc;</td>
			<td>Lowercase letter a with circumflex</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;atilde;");'>&#227;</a></td>
			<td>&amp;atilde;</td>
			<td>Lowercase letter a with tilde</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;auml;");'>&#228;</a></td>
			<td>&amp;auml;</td>
			<td>Lowercase letter a with diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;aring;");'>&#229;</a></td>
			<td>&amp;aring;</td>
			<td>Lowercase letter a with ring above</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;aelig;");'>&#230;</a></td>
			<td>&amp;aelig;</td>
			<td>Lowercase letter ae</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ccedil;");'>&#231;</a></td>
			<td>&amp;ccedil;</td>
			<td>Lowercase letter c with cedilla</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;egrave;");'>&#232;</a></td>
			<td>&amp;egrave;</td>
			<td>Lowercase letter e with grave</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;eacute;");'>&#233;</a></td>
			<td>&amp;eacute;</td>
			<td>Lowercase letter e with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ecirc;");'>&#234;</a></td>
			<td>&amp;ecirc;</td>
			<td>Lowercase letter e with circumflex</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;euml;");'>&#235;</a></td>
			<td>&amp;euml;</td>
			<td>Lowercase letter e with diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;igrave;");'>&#236;</a></td>
			<td>&amp;igrave;</td>
			<td>Lowercase letter i with grave</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;iacute;");'>&#237;</a></td>
			<td>&amp;iacute;</td>
			<td>Lowercase letter i with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;icirc;");'>&#238;</a></td>
			<td>&amp;icirc;</td>
			<td>Lowercase letter i with circumflex</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;iuml;");'>&#239;</a></td>
			<td>&amp;iuml;</td>
			<td>Lowercase letter i with diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;eth;");'>&#240;</a></td>
			<td>&amp;eth;</td>
			<td>Lowercase letter eth</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ntilde;");'>&#241;</a></td>
			<td>&amp;ntilde;</td>
			<td>Lowercase letter n with tilde</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ograve;");'>&#242;</a></td>
			<td>&amp;ograve;</td>
			<td>Lowercase letter o with grave</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;oacute;");'>&#243;</a></td>
			<td>&amp;oacute;</td>
			<td>Lowercase letter o with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ocirc;");'>&#244;</a></td>
			<td>&amp;ocirc;</td>
			<td>Lowercase letter o with circumflex</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;otilde;");'>&#245;</a></td>
			<td>&amp;otilde;</td>
			<td>Lowercase letter o with tilde</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ouml;");'>&#246;</a></td>
			<td>&amp;ouml;</td>
			<td>Lowercase letter o with diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;divide;");'>&#247;</a></td>
			<td>&amp;divide;</td>
			<td>division sign</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;oslash;");'>&#248;</a></td>
			<td>&amp;oslash;</td>
			<td>Lowercase letter o with stroke</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ugrave;");'>&#249;</a></td>
			<td>&amp;ugrave;</td>
			<td>Lowercase letter u with grave</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;uacute;");'>&#250;</a></td>
			<td>&amp;uacute;</td>
			<td>Lowercase letter u with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;ucirc;");'>&#251;</a></td>
			<td>&amp;ucirc;</td>
			<td>Lowercase letter with circumflex</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;uuml;");'>&#252;</a></td>
			<td>&amp;uuml;</td>
			<td>Lowercase letter u with diaeresis</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;yacute;");'>&#253;</a></td>
			<td>&amp;yacute;</td>
			<td>Lowercase letter y with acute</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;thorn;");'>&#254;</a></td>
			<td>&amp;thorn;</td>
			<td>Lowercase letter thorn</td>
		</tr>
	
		<tr valign="top">
			<td><a href='javascript:segn("&amp;yuml;");'>&#255;</a></td>
			<td>&amp;yuml;</td>
			<td>Lowercase letter y with diaeresis</td>
		</tr>
	</table>
</body>
</html>