
package it.finsiel.siged.mvc.vo.lookup;

import it.finsiel.siged.mvc.vo.VersioneVO;


public class ListaVO extends VersioneVO {
    private String tipo;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ListaVO(String tipo) {
        setTipo(tipo);
    }

    public ListaVO(char tipo) {
        setTipo(tipo + "");
    }

    /**
     * @rdbPhysicalName PERS_COGNOME
     * @rdbSize 100
     * @rdbDigits 0
     * @rdbLogicalType VARCHAR
     */
    private String cognome;

    /**
     * @rdbPhysicalName DESC_COMUNE_NASCITA
     * @rdbSize 50
     * @rdbDigits 0
     * @rdbLogicalType VARCHAR
     */
    private String nome;

    /**
     * @rdbPhysicalName SYS_C0022073
     * @rdbOptional
     */

    /**
     * @return Returns the cognome.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * @return Returns the nome.
     */
    public String getNome() {
        return nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * @param nome
     *            The nome to set.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @rdbPhysicalName PERS_REFERENTE
     * @rdbSize 90
     * @rdbDigits 0
     * @rdbLogicalType VARCHAR
     */

    /**
     * @rdbPhysicalName CODI_PARTITA_IVA
     * @rdbSize 11
     * @rdbDigits 0
     * @rdbLogicalType VARCHAR
     */
    private String partitaIva;

    /**
     * @return Returns the partitaIva.
     */
    public String getPartitaIva() {
        return partitaIva;
    }

    /**
     * @param ragioneSociale
     *            The ragioneSociale to set.
     */

    /**
     * @param dug
     *            The dug to set.
     */

    /**
     * @param partitaIva
     *            The partitaIva to set.
     */
    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

}