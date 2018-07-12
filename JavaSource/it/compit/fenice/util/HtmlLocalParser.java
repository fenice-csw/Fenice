package it.compit.fenice.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

public class HtmlLocalParser {
	
	public static String htmlToText(String html) {
		String htmlBody = getHTMLBody(html);
		htmlBody = removeHtmlElements(htmlBody, "style");
		htmlBody = removeHtmlElements(htmlBody, "script");
		htmlBody = removeHtmlComments(htmlBody);
		htmlBody = removeHtmlTags(htmlBody);
		htmlBody = StringEscapeUtils.unescapeHtml(htmlBody);
		return htmlBody;
	}

	public static String getHTMLBody(String html) {
		if(html != null) {
	        String lowerCaseContent = html.toLowerCase();
	        int tagStart = lowerCaseContent.indexOf("<body");
	        if(tagStart != -1 ) {
	        	int tagStartClose = lowerCaseContent.indexOf(">", tagStart) + 1;
	        	int tagEnd = lowerCaseContent.indexOf("</body>");
	        	if(tagEnd != -1) {
	            } else {
	            	tagEnd = tagStartClose;
	            }
        		if(tagEnd != tagStartClose) {
        			return html.substring(tagStartClose, tagEnd);
        		} else {
        			return "";
        		}
	        } else {
	        	return html;
	        }
		}
		return html;
    }
	
	
	public static String removeHtmlElements(String html, String element) {
		String newHtml = html;
		if(newHtml != null) {
	        String lowerCaseContent = newHtml.toLowerCase();
	        int tagStart = lowerCaseContent.indexOf("<"+element);
	        if(tagStart != -1 ) {
	        	int tagStartClose = lowerCaseContent.indexOf(">", tagStart) + 1;
	        	int tagEnd = lowerCaseContent.indexOf("</"+element+">");
	        	if(tagEnd == -1) {
	            	tagEnd = tagStartClose;
	            } else {
	            	tagEnd += element.length()+3;
	            }
	        	newHtml = newHtml.substring(0, tagStart)+newHtml.substring(tagEnd);
	        	newHtml = removeHtmlElements(newHtml, element);
	        }
		}
		return newHtml;
    }
	
	public static String removeHtmlComments(String html) {
		String newHtml = html;
		if(newHtml != null) {
			int cmtStart = -1;
			while((cmtStart = newHtml.indexOf("<!--")) != -1) {
				int cmtEnd = newHtml.indexOf("-->", cmtStart+3);
				if(cmtEnd == -1) {
					cmtEnd = newHtml.length();
				}
				newHtml = newHtml.substring(0, cmtStart)+newHtml.substring(cmtEnd+3);
			}
		}
		return newHtml;
	}
	
	public static String removeHtmlTags(String s) {
        if (s != null) {
        	Pattern whitespacePattern = Pattern.compile("\\s+", Pattern.CASE_INSENSITIVE);
            Pattern breakPattern = Pattern.compile("</?br>", Pattern.CASE_INSENSITIVE);
            Pattern paragraphPattern = Pattern.compile("</p>", Pattern.CASE_INSENSITIVE);
            Pattern divPattern = Pattern.compile("</div>", Pattern.CASE_INSENSITIVE);
            Pattern headPattern = Pattern.compile("</h\\d>", Pattern.CASE_INSENSITIVE);
            Pattern stripTagsPattern = Pattern.compile("<[^>]*>", Pattern.CASE_INSENSITIVE);
            Pattern trimSpacePattern = Pattern.compile("\n\\s+", Pattern.CASE_INSENSITIVE);
            s = whitespacePattern.matcher(s).replaceAll(" ");
            s = breakPattern.matcher(s).replaceAll("\n");
            s = paragraphPattern.matcher(s).replaceAll("\n\n");
            s = divPattern.matcher(s).replaceAll("\n\n");
            s = headPattern.matcher(s).replaceAll("\n\n");
            s = stripTagsPattern.matcher(s).replaceAll("");
            s = trimSpacePattern.matcher(s).replaceAll("\n").trim();
        }
        return s;
    }
}
