package it.compit.fenice.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomUtil {

	static final String[] typeName = { "none", "Element", "Attr", "Text",
			"CDATA", "EntityRef", "Entity", "ProcInstr", "Comment", "Document",
			"DocType", "DocFragment", "Notation" };

	public static Node getChild(Node parentNode, int childIndex) {
		Node childNode = parentNode.getChildNodes().item(childIndex);
		return childNode;
	}

	public static void removeStyleNode(Node node, String nodeName,
			String attributes) {
		if (node.getNodeName().equals(nodeName)
				&& matchAttributes(node.getAttributes(), attributes)) {
			node.getParentNode().removeChild(node);
		} else {
			NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				removeStyleNode(list.item(i), nodeName, attributes);
			}
		}
	}

	private static boolean matchAttributes(NamedNodeMap nnm, String matches) {
		String sAttrList = "";
		if (nnm != null && nnm.getLength() > 0) {
			for (int iAttr = 0; iAttr < nnm.getLength(); iAttr++) {
				sAttrList += nnm.item(iAttr).getNodeName();
				sAttrList += "=";
				sAttrList += nnm.item(iAttr).getNodeValue();
				if (sAttrList.equals(matches))
					return true;
			}

		}
		return false;
	}

	public static void addCss(Document doc, String body) {
		Element style = doc.createElement("style");
		style.setTextContent("body { font-family: \"Arial Unicode MS\"; }" +
			"@page { @bottom-right { content: \"Page \" counter(page);} }");
		Element root = doc.getDocumentElement();
		root.getElementsByTagName("head").item(0).appendChild(style);
	}

}
