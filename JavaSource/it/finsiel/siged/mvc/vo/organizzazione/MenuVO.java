package it.finsiel.siged.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.vo.IdentityVO;

public class MenuVO extends IdentityVO {
    
	private static final long serialVersionUID = 1L;

	private String title;

    private int position;

    private int parentId;

    private String link;
    
    private String uniqueName;
    
	public String getUniqueName() {
		return uniqueName!=null?uniqueName:"";
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String titolo) {
        this.title = titolo;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
}