CKEDITOR.plugins.add('mysave',{
    init:function(a){
        var cmd = a.addCommand('mysave',{exec:CKsaveAjax});
        a.ui.addButton('MySave',{
            label:'Save',
            command:'mysave',
            icon:this.path+"images/save.png"
        });
    }
});

function CKsaveAjax(e){
    var theForm = e.element.$.form;
    if (typeof(theForm.onsubmit) == 'function'){
        theForm.onsubmit();
        return false;
    }
    else{
        theForm.submit();
    }
};


