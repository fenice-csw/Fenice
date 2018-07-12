var section = null;

function switchVisibility(sectionId) {
  if (section != null) {
    section.style.display = "none";
    document.getElementById("link-"+section.id).style.fontWeight = "normal";
    //document.getElementById("link-"+section.id).style.textDecoration = "none";
  }
  section = document.getElementById(sectionId);
  section.style.display = "block";
  document.getElementById("link-"+sectionId).style.fontWeight = "bold";
  //document.getElementById("link-"+sectionId).style.textDecoration = "underline";
  document.forms[0].displaySection.value = sectionId;
}

function setVisibilitaSoggetto(value)
{	
	if (value=="F"){
		document.getElementById("persona-fisica").style.display = "block";
		document.getElementById("persona-giuridica").style.display = "none";
		document.forms[0].mittenteDenominazione.value = "";						
	} else {
		document.getElementById("persona-fisica").style.display = "none";
		document.getElementById("persona-giuridica").style.display = "block";		
		document.forms[0].mittenteCognome.value = "";		
		document.forms[0].mittenteNome.value = "";
	}
}
