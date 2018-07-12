
package it.finsiel.siged.mvc.presentation.helper;

import java.io.Serializable;


public class NavBarElement implements Serializable{
    
	private static final long serialVersionUID = 1L;

	private String value;

    private String title;

    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}