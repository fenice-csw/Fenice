function WebScannerHandler(options) {
	var self = this;
	this._host = (!options.host)?'localhost':options.host;
	this._ports = (!options.ports)?[8051,8052,8053,8054,8055]:options.ports;
	this._portIndex = 0;
	this._websocket;
	this._resolution = options.resolution;
	this._imageFormat = (!options.imageFormat)?'jpeg':options.imageFormat;
	this._fileNamePrefix = (!options.fileNamePrefix)?'Pagina':options.fileNamePrefix;
	this._serverURL = options.serverURL;
	this._override = options.override;
	this._previewImageID = options.previewImageID;
	this._lastMessage;
	this._formFileName = options.formFileName;
	this._actionProperties = options.actionProperties;
	this._afterConnection = options.afterConnection;
	this._receiveScannerList = options.receiveScannerList;
	this._afterScannerSelect = options.afterScannerSelect;
	this._afterScan = options.afterScan;
	this._afterGetImage = options.afterGetImage;
	this._afterDeleteImage = options.afterDeleteImage;
	this._onProgressUpload = options.onProgressUpload;
	if(!this._onProgressUpload) {
		this._onProgressUpload = function() { return true; };
	}
	this._onUploadImagesComplete = options.onUploadImagesComplete;
	
	var wOpen = function onOpen(evt) {
		console.log("Connessione websocket aperta.");
		self._portIndex = 0;
		genericSend("GET_SCANNER_LIST", null, self._receiveScannerList);
		if(self._afterConnection) {
			self._afterConnection();
		}
	};
	var wClose = function onClose(evt) {
		self._websocket.close();
		self._portIndex++;
		if (self._portIndex < self._ports.length) {
			connect();
		} else {
			console.error("Nessuna porta disponibile");
		}
		console.log("connessione chiusa");
	};
	var wError = function onError(evt) {
		console.error("*****ERRORE*****"+ evt.data);
	};
	var wMessage = function onMessage(evt) {
		var clkRetVal;
		var errorState;
		var execCallback = true;
		if(evt.data instanceof ArrayBuffer) {
			console.log("immagine ricevuta");
			var blob = new Blob([new Uint8Array(evt.data)], {type: 'image/'+self._imageFormat});
			console.log("creazione anteprima");
			var img = $('#'+self._previewImageID)[0];
			img.src = URL.createObjectURL(blob);
			img.onload = function() {
			}
			
			console.log("anteprima creata");
			clkRetVal = blob;
		} else if(typeof evt.data === "string"){
			var jsonObj = JSON.parse(evt.data);
			if(jsonObj.success || jsonObj.success =='true') {
				switch (jsonObj.message) {
					case "CLOSE":
						alert("Un altro utente è connesso. Riprovare in seguito.");
						self._onClose(evt);
					break;
					case "WAIT":
						execCallback = false;
						setTimeout( function(){
							var jsonInput = {};
							jsonInput.opName = jsonObj.content;
							var message = JSON.stringify(jsonInput);
							self._websocket.send(message);
						}, 3000);
					break;
					default: 
						clkRetVal = jsonObj.content;
				}
			} else {
				errorState = jsonObj;
			}
		}
		if(self._lastMessage) {
			if(execCallback && self._lastMessage.callback) {
				self._lastMessage.callback(clkRetVal, errorState);
			}
		}
		if(!self._lastMessage && !self._lastMessage.callback && errorState) {
			console.error(errorState.message);
		}
	};
	function connect() {
		var url = "ws://" + self._host + ":" + self._ports[self._portIndex];
		console.log("connessione sulla socket: " + url);
		self._websocket = new WebSocket(url);
		self._websocket.binaryType = "arraybuffer";
		self._websocket.onopen = wOpen;
		self._websocket.onclose = wClose;
		self._websocket.onerror = wError;
		self._websocket.onmessage = wMessage;
	}
	
	var genericSend = function sendMessage(opName, params, callback) {
		var jsonInput = {};
		jsonInput.opName = opName;
		if (params) {
			jsonInput.params = params;
		}
		var message = JSON.stringify(jsonInput);
		self._lastMessage = {
			message : message,
			callback : callback
		};
		console.log("messaggio: "+opName);
		self._websocket.send(message);
		console.log("messaggio spedito");
	}
	
	this.beginScan = function(advanced, printerInfo) {
		var params = {
			resolution: self._resolution,
			imageFormat: self._imageFormat,
			advanced: advanced,
			override: self._override
		};
		if(printerInfo) {
			params.printerInfo = printerInfo;
		}
		genericSend("SCAN", params, self._afterScan);
	}
	
	this.getScannerList = function() {
		genericSend("GET_SCANNER_LIST", null, self._receiveScannerList);
	}
	
	this.selectScanner = function(scanner) {
		var params = {SID: scanner};
		genericSend("SELECT_SCANNER", params, self._afterScannerSelect);
	}
	
	this.getPreview = function(indice) {
		var params = {
			indice : indice,
			imageFormat : "jpeg",
		};
		genericSend("GET_IMAGE", params, self._afterGetImage);
	}
	
	this.deleteImage = function(indice) {
		var params = {
			indice : indice,
		};
		genericSend("DELETE_IMAGE", params, self._afterDeleteImage);
		$('#'+self._previewImageID).hide();
	}
	
	this.saveImages = function(start, end) {
		var self = this;
		var uploadSingleImage = function(image, errorState) {
			console.log("immagine definitiva ricevuta");
			var formdata = new FormData();
			formdata.append(self._formFileName, image);
			formdata.append("fileName", self._fileNamePrefix+"_"+(start+1)+"."+self._imageFormat);
			formdata.append("fileNumber", start+1);
			formdata.append("filetotals", end); 
			formdata.append(self._actionProperties, "true");
			console.log("spedizione immagine al server");
			$.ajax({
			   url: self._serverURL,
			   method: "POST",
			   data: formdata,
			   processData: false,
			   contentType: false
			}).always(function(response){
				console.log(response);
				var proceed = self._onProgressUpload(response, start+1, end);
				if(proceed) {
					self.saveImages(start+1, end);
				}
			});
		}
		if(start >= end) {
			console.log("upload finished!");
			if(self._onUploadImagesComplete) {
				self._onUploadImagesComplete();
			}
		} else {
			console.log("upload in corso image "+start);
			genericSend("GET_IMAGE", { indice : start }, uploadSingleImage);
		}	
	}
	connect();
}