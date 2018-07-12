<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<link rel="stylesheet" href="css/bootstrap-theme.min.css">
	<link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container-fluid" id="container">
	<div class="row">
		<div id="progressBarContainer" class="col-md-2">
			<img class="media-object" id="preview" src="">
			<div id="progressUpload" class="progress" style="display: none; width: 100%; vertical-align: bottom;">
				<div id="progressInfo" class="active progress-bar progress-bar-striped" role="progressbar" 
					aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 1%;">
					<span id="progress-label"></span>
				</div>
			</div>
		</div>
		<div id="deleteContainer" style="display: none">
				<button id="btnDelete" class="btn btn-sm btn-danger" data-toggle="confirmation">
					<i class="glyphicon glyphicon-remove"></i>
				</button>
		</div>
		<div id="navbarContainer" class="col-md-2">
			<div class="sidebar-nav navbar navbar-collapse sidebar-navbar-collapse" role="navigation">
				<ul class="nav nav-pills nav-stacked">
					<li>
						<div class="input-group customDivs">
							<input type="checkbox" style="margin-right:5px" id="settings">
							<label for="settings" class="bluetext"><b>Impostazioni Avanzate</b></label>
						</div>
						<div class="input-group customDivs">
							<label class="bluetext"><b>Scanner Disponibili:</b></label>
							<select id="selectScanner" class="form-control"></select>
						</div>
					</li>
					<li>
						<div id="scanListGroup" class="input-group customDivs">
							<label class="bluetext"><b>Immagini acquisite:</b></label>
							<select class="form-control" id="scanList" size="10"></select>
						</div>
					</li>
					<li>
						<div class="btn-group-lg customDivs">
							<button id="beginScan" type="button" style="float:left" class="bigButton bluetext" disabled>
								Scansiona Pagina</button>
							<button id="confirm" type="button" style="float:right" class="bigButton bluetext" disabled>
								Salva ed esci</button>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-confirmation.min.js"></script>
<script src="js/websocket2.js"></script>
<script type="text/javascript">

var handler = new WebScannerHandler({
	host: 'localhost',
	override: false,
	formFileName: '<%=request.getParameter("formFileName")%>',
	actionProperties: '<%=request.getParameter("actionProperties")%>',
	previewImageID: $("#preview").attr("id"),
	serverURL : '<%=request.getParameter("url")%>',
	receiveScannerList: function(devices, errorState){
		console.log("lista scanner ricevuta");
		if (!handler._override) {
			$("#scanListGroup").show();
		}
		$("#selectScanner").empty();
		for(var key in devices) {
			$("#selectScanner").append(new Option(devices[key], key));
		}
		$("#beginScan").removeAttr("disabled");
	},
	afterScan : function(pages, errorState) {
		$('#progressUpload').hide();
		if(!handler._override) {
			if (pages > 0) {
				$("#scanList").empty();
				for (var i = 0; i < pages; i++) {
					console.log("popolamento scanList");
					
					$("#scanList").append(new Option("Pagina "+i, i));
				}
				console.log("popolamento completato");
			}
		}
		$("#beginScan").removeAttr("disabled");
		if (pages > 0) {
			$("#confirm").removeAttr("disabled");
		}
	},
	afterDeleteImage : function(pages, errorState) {
		console.log("cancellazione effettuata");
		$("#scanList").empty();
		for (var i = 0; i < pages; i++) {
			console.log("Ripopolamento scanList");
			$("#scanList").append(new Option("Pagina "+i, i));	
		}
		if(pages == 0) {
			$('#deleteContainer').hide();
		} else {
			$("#preview").show();
		}
	},
	onProgressUpload: function(response, currIndex, endIndex) {
		$('#progress-label').text(currIndex+" / "+ endIndex);
		var percent = currIndex*100/endIndex;
		$('#progressInfo').css('width', percent+'%');
		$('#progressInfo').attr('aria-valuenow', currIndex);
		$('#progressUpload').show();
		return true;
	},
	onUploadImagesComplete: function() {
		$('#progressUpload').fadeOut();
		$("#beginScan").removeAttr("disabled");
		$("#scanList").empty();
		$('#deleteContainer').hide();
		window.parent.location.reload();
	},
	afterGetImage : function() {
		console.log("ecco l'immagine!!!");
		$('#progressUpload').hide();
		$('#deleteContainer').show();
	}
} );

$("#beginScan").click(function(event){
	$('#confirm').attr("disabled", true);
	$("#deleteContainer").hide();
	$(this).attr("disabled", true);
	var advanced = $("#settings").prop("checked") ? true : false; 
	console.log("inizio scansione");
	$('#progressInfo').css('width', '100%');
	$('#progressUpload').show();
	var printerInfo = "<%=request.getParameter("nProt") %>";
	handler.beginScan(advanced, printerInfo.replace("\"", ""));
});

$("#scanList").on({
	"focus" : function() {
		this.blur();
	},"change" : function(event) {
		var choice = $(this).val();
		console.log("anteprima in creazione");
		this.blur();
		$('#progressUpload').show();
		handler.getPreview(choice);
	}
});

$("#btnDelete").confirmation({
	placement: "bottom",
	singleton: "true",
	title: "Vuoi cancellare l'immagine selezionata?",
	btnOkLabel: "SI",
	btnCancelLabel: "NO",
	title: "Sei sicuro?",
	onConfirm: function() {
		var toDelete = $("#scanList").val();
		handler.deleteImage(toDelete);
	}
});

$("#confirm").click(function(event) {
	$('#beginScan').attr("disabled", true);
	$('#deleteContainer').hide();
	console.log("conferma richiesta.");
	if(!handler._override) {
		var optionListLength = $("#scanList > option").length;
		if(optionListLength > 0) {
			$('#progress-label').text("1 / "+ optionListLength);
			$('#progressInfo').attr('aria-valuemax', optionListLength);
			$('#progressUpload').show();
			handler.saveImages(0, optionListLength);
		}
	} else {
		handler.saveImages(0, 1);
	}
});

$("#selectScanner").change(function(event) {
	event.preventDefault();
	$('#beginScan').attr("disabled", true);
	$('#confirm').attr("disabled", true);
	var scanner = $("#selectScanner").val();
	handler.selectScanner(scanner);
	$("#beginScan").removeAttr("disabled");
	$("#confirm").removeAttr("disabled");
});

</script>
</body>
</html>