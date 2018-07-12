/*

 */
package it.finsiel.siged.mvc.vo.documentale;

import it.finsiel.siged.mvc.vo.BaseVO;
import it.finsiel.siged.util.tree.ObjectTree;

/**
 * @author Almaviva sud
 * 
 */
public class CartellaVO extends BaseVO implements ObjectTree {

    private int idx;

    private int caricaId;

    private int parentId;

    private boolean isRoot = false;

    private boolean nodeSelected = false;

    private String nome;

    private int aooId;

    public CartellaVO() {
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    
    public int getCaricaId() {
		return caricaId;
	}

	public void setCaricaId(int caricaId) {
		this.caricaId = caricaId;
	}

	public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.util.tree.ObjectTree#isSelected()
     */
    public boolean isSelected() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.util.tree.ObjectTree#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj) {
        return ((CartellaVO) obj).getTreeId() - (int) this.getTreeId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.util.tree.ObjectTree#getTreeId()
     */
    public int getTreeId() {
        return getId().intValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.util.tree.ObjectTree#getTreeParentId()
     */
    public int getTreeParentId() {
        return getParentId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.util.tree.ObjectTree#getTreeDescription()
     */
    public String getTreeDescription() {
        return getNome();
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.util.tree.ObjectTree#isNodeSelected()
     */
    public boolean isNodeSelected() {
        return this.nodeSelected;
    }

    public void setNodeSelected(boolean s) {
        this.nodeSelected = s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see it.finsiel.siged.util.tree.ObjectTree#toStringNode()
     */
    public String toStringNode() {
        return getNome();
    }

    public String toString() {
        return this.getId().intValue() + " - " + this.getNome();
    }
}
