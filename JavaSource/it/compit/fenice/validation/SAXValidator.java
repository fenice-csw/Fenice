package it.compit.fenice.validation;

import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.IOException;


public class SAXValidator implements ErrorHandler {
  
  private boolean valid = true;
  
  public boolean isValid() {
    return valid; 
  }
  
  public void reset() {
    valid = true; 
  }
  
  public void warning(SAXParseException exception) {
    
    System.out.println("Warning: " + exception.getMessage());
    System.out.println(" at line " + exception.getLineNumber() 
     + ", column " + exception.getColumnNumber());
    valid = false;
    
  }
  
  public void error(SAXParseException exception) {
     
    System.out.println("Error: " + exception.getMessage());
    System.out.println(" at line " + exception.getLineNumber() 
     + ", column " + exception.getColumnNumber());
    valid = false;
    
  }
  
  public void fatalError(SAXParseException exception) {
     
    System.out.println("Fatal Error: " + exception.getMessage());
    System.out.println(" at line " + exception.getLineNumber() 
     + ", column " + exception.getColumnNumber()); 
     
  }
  

  public static void main(String[] args) {
  
    if (args.length <= 0) {
      System.out.println("Usage: java SAXValidator URL");
      return;
    }
    String document = args[0];
    
    try {
      XMLReader parser = XMLReaderFactory.createXMLReader();
      SAXValidator handler = new SAXValidator();
      parser.setErrorHandler(handler);
      parser.setFeature(
       "http://xml.org/sax/features/validation", true);
      parser.parse(document);
      if (handler.isValid()) {
        System.out.println(document + " is valid.");
      }
      else {
         System.out.println(document + " is well-formed.");
      }
    }
    catch (SAXParseException e) {
      System.out.print(document + " is not well-formed at ");
      System.out.println("Line " + e.getLineNumber() 
       + ", column " +  e.getColumnNumber() );
    }
    catch (SAXException e) {
      System.out.println("Could not check document because " 
       + e.getMessage());
    }
    catch (IOException e) { 
      System.out.println(
       "Due to an IOException, the parser could not check " 
       + document
      ); 
    }
  
  }

}
