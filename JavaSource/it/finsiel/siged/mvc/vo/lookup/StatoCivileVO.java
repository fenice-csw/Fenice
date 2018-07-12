
package it.finsiel.siged.mvc.vo.lookup;


public class StatoCivileVO {
    private String codice;

    private String descrizione;

    public StatoCivileVO(String codice, String descrizione) {
        this.codice = codice;
        this.descrizione = descrizione;
    }

    /**
     * @return Returns the codice.
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @param codice
     *            The codice to set.
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}