package it.compit.fenice.mvc.presentation.filter;

import java.io.File;
import java.io.FilenameFilter;


public class FileNameFilter implements FilenameFilter {
	
    private String tipo;
    
    private String nome;

    public FileNameFilter(String nome, String tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public boolean accept(File dir, String file) {
        File f = new File(dir, file);

        
        boolean flag1=true;
        if(tipo!=null && tipo!="*")
           flag1=(file.indexOf(tipo) == file.length()-tipo.length());

          boolean flag2=true;
          if(nome!=null && nome!="*")
             flag2=file.toUpperCase().indexOf(nome.toUpperCase())!=-1;
        return ( ( flag1 && flag2 ) || f.isDirectory()) && !f.isHidden();

    }
}
