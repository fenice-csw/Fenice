package it.finsiel.siged.mvc.presentation.tag;

import it.finsiel.siged.util.NumberUtil;
import it.finsiel.siged.util.tree.ObjectTree;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeViewTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String cartellaCorrente;

    private int cartellaId = -1;

    public String getCartellaCorrente() {
        return cartellaCorrente;
    }

    public void setCartellaCorrente(String cartellaCorrente) {
        this.cartellaCorrente = cartellaCorrente;
    }

    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

        DefaultMutableTreeNode sottoAlbero = (DefaultMutableTreeNode) req
                .getAttribute("albero_cartelle");
        if (NumberUtil.isInteger(getCartellaCorrente()))
            setCartellaId(NumberUtil.getInt(getCartellaCorrente()));

        try {
            out.print("<div id='cartelle'>");
           // ObjectTree node = (ObjectTree) sottoAlbero.getUserObject();
            out.print("<ul class=\"opened\">");
            mostraAlbero(out, sottoAlbero);
            out.print("</ul>");
            out.print("</div>");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private void mostraAlbero(JspWriter out, DefaultMutableTreeNode root) {
        if (root == null)
            return;
        try {
            ObjectTree parent = (ObjectTree) root.getUserObject();
            out.print("<li id=\"node_" + parent.getTreeId() + "\"" + " class="
                    + (parent.isNodeSelected() ? "'opened'" : "'closed'")
                    + ">\n");
            out.print("<span id=\"nodegroup_" + parent.getTreeId() + "\">"
                    + parent.getTreeDescription() + "</span>\n");
            Enumeration e = root.children();
            if (e.hasMoreElements()) {
                out.print("<ul id=\"subnode_" + parent.getTreeId() + "\">\n");
                while (e.hasMoreElements()) {
                    DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) e
                            .nextElement();
                    //ObjectTree curObj = (ObjectTree) subNode.getUserObject();

                    mostraAlbero(out, subNode);

                }
                out.print("    </ul>");
                out.print("<script type=\"text/javascript\">");
                out.print("<!-- \n initAlbero( " + parent.getTreeId()
                        + " ); \n // -->  \n </script>");
            }
            out.print("</li>\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public int doEndTag() throws JspException {
        return 0;
    }

	public int getCartellaId() {
		return cartellaId;
	}

	public void setCartellaId(int cartellaId) {
		this.cartellaId = cartellaId;
	}
}