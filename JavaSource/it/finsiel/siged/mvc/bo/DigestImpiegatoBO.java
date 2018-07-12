package it.finsiel.siged.mvc.bo;
import it.finsiel.siged.mvc.vo.ImpiegatiVO;
import it.finsiel.siged.mvc.vo.ImpiegatoVO;

import java.util.Vector;

import org.apache.commons.digester.Digester;
public class DigestImpiegatoBO {
    
    
      Vector<ImpiegatoVO> impiegati; 
      
      public DigestImpiegatoBO(){
    	  impiegati= new Vector<ImpiegatoVO>();
      }

      public void digest(String fileXML) {
        try {
          Digester digester = new Digester();
          digester.push(this); //Creates a new instance of the Student class
          digester.addObjectCreate( "impiegati", ImpiegatiVO.class ); 
          digester.addObjectCreate( "impiegati/impiegato", ImpiegatoVO.class ); 
          digester.addBeanPropertySetter( "impiegati/impiegato/matricola","matricola"); 
          digester.addBeanPropertySetter("impiegati/impiegato/nome","nome");
          digester.addBeanPropertySetter("impiegati/impiegato/cognome","cognome");
          digester.addBeanPropertySetter("impiegati/impiegato/data_nascita","dataascita");
          digester.addBeanPropertySetter("impiegati/impiegato/comune","comune");
          digester.addBeanPropertySetter("impiegati/impiegato/provincia","provincia");
          digester.addBeanPropertySetter("impiegati/impiegato/qualifica","qualifica");
          digester.addSetNext( "impiegati/impiegato", "addImpegato" );       
        }
        catch (Exception ex) {
          ex.printStackTrace();
          }
      }  

      public void addImpiegati( ImpiegatoVO imp ) {
        impiegati.add( imp );    
      }  
    }