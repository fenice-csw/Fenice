package it.finsiel.siged.mvc.vo;


public class RubricaListaVO extends VersioneVO {
   private int idLista   ;
   private int idRubrica ;
   private String tipoSoggetto;   


    public RubricaListaVO() {

    }


    public int getIdLista() {
        return idLista;
    }


    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }


    public int getIdRubrica() {
        return idRubrica;
    }


    public void setIdRubrica(int idRubrica) {
        this.idRubrica = idRubrica;
    }


    public String getTipoSoggetto() {
        return tipoSoggetto;
    }


    public void setTipoSoggetto(String tipoSoggetto) {
        this.tipoSoggetto = tipoSoggetto;
    }
    

}