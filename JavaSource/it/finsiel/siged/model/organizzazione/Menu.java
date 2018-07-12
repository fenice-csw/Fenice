package it.finsiel.siged.model.organizzazione;

import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class Menu {
    private MenuVO valueObject;

    private Menu parent;

    private SortedMap<Integer,Menu> children = new TreeMap<Integer,Menu>();
    
   
    
    public Menu(MenuVO valueObject) {
        this.valueObject = valueObject;
    }

    public MenuVO getValueObject() {
        return valueObject;
    }

    public String getLink() {
        return this.getValueObject().getLink();
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        if (this.parent != null) {
            this.parent.children.remove(this);
        }
        if (parent != null) {
            parent.children.put(new Integer(getValueObject().getPosition()),
                    this);
        }
        this.parent = parent;
    }

    public Collection<Menu> getChildren() {
        return children.values();
    }



	
    
    
}