/* Generated by Together */

package it.finsiel.siged.mvc.vo;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;



/**
 * @db: registro
 */
public class ImpiegatiVO {
    private List listaImpiegato;
    
    
//  Il costruttore inizializzer� la lista
    public ImpiegatiVO() {
        listaImpiegato = new ArrayList();
    }


    // Questo metodo aggiunger� un nuovo impiegato alla lista
    public void addImpegato(ImpiegatoVO impiegato) {
        listaImpiegato.add(impiegato);
    }
    
    

    // Questo metodo verr� utilizzato per testare il nostro oggetto
    public String toString() {
      StringBuffer retBuff = new StringBuffer();
      for (int i=0; i<listaImpiegato.size(); i++) {
        retBuff.append(listaImpiegato.get(i));
        retBuff.append(System.getProperty("line.separator"));
      }
      return retBuff.toString();
    }
    
    public List getListaImpiegato() {
    return(this.listaImpiegato);
    }
    
    public void setListaImpiegato(Collection elenco) {
        this.listaImpiegato= (List)elenco;
        }
    
    public ImpiegatoVO getImpiegato(String matricola) {
        ImpiegatoVO impiegato =null;
        for (Iterator iter = this.listaImpiegato.iterator(); iter.hasNext();) {
            ImpiegatoVO element = (ImpiegatoVO) iter.next();
            if (element.getMatricola().equals(matricola)){
                impiegato=element;
                break;
            }
            
        }
        return(impiegato);
    }
    
    public static ImpiegatiVO parse(String filename) throws IOException, SAXException {
        Digester digester = new Digester();
        digester.setValidating( false );

        //Push the current object onto the stack
       // digester.push(this); //Creates a new instance of the Student class
        digester.addObjectCreate( "impiegati", ImpiegatiVO.class ); 
        digester.addObjectCreate( "impiegati/impiegato", ImpiegatoVO.class ); 
       // ora digester.addSetProperties("impiegati/impiegato");
        //setName method of the Student instance
        //Uses tag name as the property name
         
        //Uses setCourse method of the Student instance
        //Explicitly specify property name as 'course'
        digester.addBeanPropertySetter("impiegati/impiegato/MATRICOLA","matricola");
        digester.addBeanPropertySetter("impiegati/impiegato/NOME","nome");
        digester.addBeanPropertySetter("impiegati/impiegato/COGNOME","cognome");
        digester.addBeanPropertySetter("impiegati/impiegato/DATANASCITA","dataNascita");
        digester.addBeanPropertySetter("impiegati/impiegato/COMUNE","comune");
        digester.addBeanPropertySetter("impiegati/impiegato/PROVINCIA","provincia");
        digester.addBeanPropertySetter("impiegati/impiegato/QUALIFICA","qualifica");
        //Move to next employee
        digester.addSetNext( "impiegati/impiegato", "addImpegato" ); 
        
        //Print the contents of the Vector
        
        Reader reader = new StringReader(filename);
        /* il metodo parse vuole un reader per funzionare */ 
        return (ImpiegatiVO)digester.parse(reader);  
        
    }
    
   
}





    
    