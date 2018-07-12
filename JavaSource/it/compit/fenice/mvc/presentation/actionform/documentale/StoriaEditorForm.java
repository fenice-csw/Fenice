package it.compit.fenice.mvc.presentation.actionform.documentale;

import it.compit.fenice.mvc.vo.documentale.EditorVO;

import java.util.Collection;

public class StoriaEditorForm extends EditorForm {

	private static final long serialVersionUID = 1L;
	
	private Collection<EditorVO> versioniEditor;

    public Collection<EditorVO> getVersioniEditor() {
        return versioniEditor;
    }

    public void setVersioniEditor(Collection<EditorVO> versioniEditor) {
        this.versioniEditor = versioniEditor;
    }

}
