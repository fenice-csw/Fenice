/* Generated by Together */

package it.finsiel.siged.mvc.vo.registro;

/**
 * @persistent
 * @rdbPhysicalName UTENTI_REGISTRI
 * @rdbPhysicalPrimaryKeyName UTENTI_REGISTRI_PKEY
 */
public class UtentiRegistroVO {
    public UtentiRegistroVO() {
    }

    /**
     * @return Returns the registroId.
     */
    public Integer getRegistroId() {
        return registroId;
    }

    /**
     * @param registroId
     *            The registroId to set.
     */
    public void setRegistroId(Integer registroId) {
        this.registroId = registroId;
    }

    /**
     * @return Returns the utenteId.
     */
    public Integer getUtenteId() {
        return utenteId;
    }

    /**
     * @param utenteId
     *            The utenteId to set.
     */
    public void setUtenteId(Integer utenteId) {
        this.utenteId = utenteId;
    }

    /**
     * @rdbPhysicalName SYS_C0022220
     * @rdbIdentifying
     */
    private Integer registroId;

    /**
     * @rdbPhysicalName SYS_C0022219
     * @rdbIdentifying
     */
    private Integer utenteId;
}