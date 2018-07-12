/*
 * Created on 18-apr-2005
 *
 */
package it.finsiel.siged.util.tree;

import java.util.Arrays;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.collections.FastHashMap;
import org.apache.log4j.Logger;

/**
 * @author Almaviva sud
 * 
 */
public class TreeUtil {

    /**
     * Logging output for this plug in instance.
     */
    static Logger logger = Logger.getLogger(TreeUtil.class.getName());

    public static DefaultMutableTreeNode getTreeFromElements(
            FastHashMap elements, ObjectTree root) {

        DefaultMutableTreeNode top = new DefaultMutableTreeNode(root);

        Object[] objs = elements.values().toArray();
        Arrays.sort(objs); // Very Important !!!!!
        for (int i = 0; i < objs.length; i++) {
            ObjectTree obj = (ObjectTree) objs[i];
            // logger.debug("Processing Obj:" + obj.getTreeId());
            boolean added = false;
            for (int j = 0; j < objs.length; j++) {
                ObjectTree obj2 = (ObjectTree) objs[j];

                // logger.debug(" Obj:" + obj2.getTreeId());
                if (obj.getTreeParentId() == obj2.getTreeId()) {
                    addObj1ToObj2(top, obj, obj2);
                    added = true;
                    // if (logger.isDebugEnabled())
                    // printTree(top, 0);
                }
            }
            if (!added)// && loc.getId ()!= company.getId() )
            {
                DefaultMutableTreeNode temp = new DefaultMutableTreeNode(obj);
                if (!isChild(top, temp)) {
                    // logger.debug("new tree:" + obj.toStringNode());
                    top.add(temp);
                }
            }
        }
        return top;
    }

    /*
     * Add the loc tree to the loc2 tree ,
     */
    private static void addObj1ToObj2(DefaultMutableTreeNode top,
            ObjectTree obj, ObjectTree obj2) {

        // logger.debug("Adding:" + obj.getTreeDescription() + " to:" +
        // obj2.getTreeDescription());
        DefaultMutableTreeNode link = getTree(top, obj.getTreeId());
        if (link != null) {
            if (link.getUserObject() == null)
                link.setUserObject(obj);
        } else
            link = new DefaultMutableTreeNode(obj);
        DefaultMutableTreeNode uplink = getTree(top, obj2.getTreeId());
        if (uplink != null) {
            if (uplink.getUserObject() == null)
                uplink.setUserObject(obj2);
        } else
            uplink = new DefaultMutableTreeNode(obj2);
        uplink.add(link);
        top.add(uplink);
    }

    public static boolean isChild(DefaultMutableTreeNode root,
            DefaultMutableTreeNode child) {
        if (root == null)
            return false;
        Enumeration e = root.children();
        long id = ((ObjectTree) child.getUserObject()).getTreeId();
        boolean found = false;
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode d = (DefaultMutableTreeNode) e.nextElement();
            ObjectTree curObj = (ObjectTree) d.getUserObject();
            if (curObj.getTreeId() == id) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static DefaultMutableTreeNode getTree(DefaultMutableTreeNode root,
            int objId) {
        if (root == null)
            return new DefaultMutableTreeNode();
        ObjectTree curObj = (ObjectTree) root.getUserObject();
        if (curObj.getTreeId() == objId) {
            root.removeFromParent();
            return root;
        } else
            return getTree(root.getNextNode(), objId);
    }

    public static DefaultMutableTreeNode getSubTree(
            DefaultMutableTreeNode root, int objId) {
        if (root == null)
            return root;
        if (((ObjectTree) root.getUserObject()).getTreeId() == objId)
            return root;

        Enumeration nodes = root.children();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode siblingNode = (DefaultMutableTreeNode) nodes
                    .nextElement();
            ObjectTree obj = (ObjectTree) siblingNode.getUserObject();
            if (obj.getTreeId() == objId) {
                siblingNode.removeFromParent();
                return siblingNode;
            } else {
                if (!siblingNode.isLeaf()) {
                    getSubTree(siblingNode, objId);
                }
            }
        }
        return root;
    }

    public static void selectNode(DefaultMutableTreeNode root, int objId) {
        if (root == null)
            return;
        if (((ObjectTree) root.getUserObject()).getTreeId() == objId) {
            ((ObjectTree) root.getUserObject()).setNodeSelected(true);
            return;
        }

        Enumeration nodes = root.children();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode siblingNode = (DefaultMutableTreeNode) nodes
                    .nextElement();
            ObjectTree obj = (ObjectTree) siblingNode.getUserObject();
            if (obj.getTreeId() == objId) {
                ((ObjectTree) siblingNode.getUserObject())
                        .setNodeSelected(true);
                return;
            } else {
                if (!siblingNode.isLeaf()) {
                    selectNode(siblingNode, objId);
                }
            }
        }
        return;
    }

    public static DefaultMutableTreeNode[] getPathToNode(
            DefaultMutableTreeNode root, int id) {
        if (root == null)
            return null;
        if (((ObjectTree) root.getUserObject()).getTreeId() == id) {
            return getMutableTreeNodes(root.getPath());
        }

        Enumeration nodes = root.children();
        while (nodes.hasMoreElements()) {
            DefaultMutableTreeNode siblingNode = (DefaultMutableTreeNode) nodes
                    .nextElement();
            ObjectTree obj = (ObjectTree) siblingNode.getUserObject();
            if (obj.getTreeId() == id) {
                return getMutableTreeNodes(siblingNode.getPath());
            } else {
                if (!siblingNode.isLeaf()) {
                    getPathToNode(siblingNode, id);
                }
            }
        }
        return null;
    }

    private static DefaultMutableTreeNode[] getMutableTreeNodes(TreeNode[] path) {
        if (path == null)
            return null;
        DefaultMutableTreeNode[] nodes = new DefaultMutableTreeNode[path.length];
        for (int i = 0; path != null && i < path.length; i++) {
            nodes[i] = (DefaultMutableTreeNode) path[i];
        }
        return nodes;
    }

    public static void selectNodes(DefaultMutableTreeNode[] path) {
        for (int i = 0; path != null && i < path.length; i++) {
            ((ObjectTree) path[i].getUserObject()).setNodeSelected(true);
        }
    }

    public static void printTree(DefaultMutableTreeNode root, int level) {
        if (root == null)
            return;
        Enumeration e = root.children();
        if (root.getUserObject() != null)
            logger.debug(level + root.getLevel() + "   "
                    + ((ObjectTree) root.getUserObject()).toString());
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode d = (DefaultMutableTreeNode) e.nextElement();
            ObjectTree curObj = (ObjectTree) d.getUserObject();
            if (!d.isLeaf())
                printTree(d, level);
            else
                logger.debug(level + d.getLevel() + "   " + curObj.toString());
        }
        level++;
    }

}
