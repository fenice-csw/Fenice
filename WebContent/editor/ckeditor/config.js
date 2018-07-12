CKEDITOR.editorConfig = function(config) {
	//
	config.filebrowserBrowseUrl = '/feniceWeb/editor/kcfinder/browse.php?type=files';
	config.filebrowserImageBrowseUrl = '/feniceWeb/editor/kcfinder/browse.php?type=images';
	config.filebrowserFlashBrowseUrl = '/feniceWeb/editor/kcfinder/browse.php?type=flash';
	config.filebrowserUploadUrl = '/feniceWeb/editor/kcfinder/upload.php?type=files';
	config.filebrowserImageUploadUrl = '/feniceWeb/editor/kcfinder/upload.php?type=images';
	config.filebrowserFlashUploadUrl = '/feniceWeb/editor/kcfinder/upload.php?type=flash';
	//
	config.language = 'it';
	config.uiColor = '#21759B';
	config.extraPlugins = 'mysave';
	config.toolbar = 'Fenice';
	config.toolbar_Fenice = [
			// ['NewPage','MySave','Preview','-','Templates'],
			[ 'NewPage', 'Save', 'Preview'],
			[ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord' ],
			[ 'Undo', 'Redo', '-', 'Find', 'Replace', '-', 'SelectAll',
					'RemoveFormat' ],
			//[ 'Image', 'Table', 'HorizontalRule', 'SpecialChar' ],
			['Table', 'HorizontalRule', 'SpecialChar' ],'/',
			[ 'Styles', 'Format', 'Font', 'FontSize', 'TextColor', 'BGColor' ],
			[ 'Bold', 'Italic', 'Strike' ],
			[ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent',
					'Blockquote' ], [ 'Maximize' ] ];
	config.toolbar_ULL = [
	 						// ['NewPage','MySave','Preview','-','Templates'],
	 						['Preview'],
	 						[ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord' ],
	 						[ 'Undo', 'Redo', '-', 'Find', 'Replace', '-', 'SelectAll',
	 								'RemoveFormat' ],
	 						//[ 'Image', 'Table', 'HorizontalRule', 'SpecialChar' ],
	 						['Table', 'HorizontalRule', 'SpecialChar' ],'/',
	 						[ 'Styles', 'Format', 'Font', 'FontSize', 'TextColor', 'BGColor' ],
	 						[ 'Bold', 'Italic', 'Strike' ],
	 						[ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent',
	 								'Blockquote' ], [ 'Maximize' ] ];
};

