/*
 * Created on 18-apr-2005
 *
 */
package it.finsiel.siged.util.tree;

/**
 * @author Almaviva sud
 * 
 */
public interface ObjectTree extends Comparable {
    public int getTreeId();

    public int getTreeParentId();

    public String getTreeDescription();

    public boolean isNodeSelected();

    public void setNodeSelected(boolean s);

    public String toStringNode();

    public int compareTo(Object obj);

}
