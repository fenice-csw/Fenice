function ShowAndHide(id1, id2) {
	if (document.getElementById) {
		el1 = document.getElementById(id1);
		el2 = document.getElementById(id2);
		if (el1.style.display == "none") {
			el1.style.display = "block";
			el2.style.display = "none";
		} else {
			el1.style.display = "none";
			el2.style.display = "block";
		}
	}
}

function ShowDiv(id1, id2) {
	if (document.getElementById) {
		el1 = document.getElementById(id1);
		el2 = document.getElementById(id2);
		el1.style.display = "block";
		el2.style.display = "none";
	}
}

function disableButton(e) {
	e.style.display = "none";
	return;
}

function ShowAndHideRicercaPage(id1) {
	if (document.getElementById) {
		
		elProt = document.getElementById('optProt');
		elAss = document.getElementById('optAss');
		elMitt = document.getElementById('optMitt');
		
		if (el1.style.display == "none") {
			el1.style.display = "block";
			el2.style.display = "none";
			el3.style.display = "none";
		} else if (el2.style.display == "none"){
			el1.style.display = "none";
			el2.style.display = "block";
			el3.style.display = "none";
		} else if (el3.style.display == "none"){
			el1.style.display = "none";
			el2.style.display = "block";
			el3.style.display = "none";
		}
	}
}
