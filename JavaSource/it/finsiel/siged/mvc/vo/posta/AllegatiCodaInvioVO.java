/* Generated by Together */

package it.finsiel.siged.mvc.vo.posta;

/**
 * @persistent
 * @rdbPhysicalName ALLEGATI_CODA_INVIO
 * @rdbPhysicalPrimaryKeyName SYS_C0022134
 */
public class AllegatiCodaInvioVO {
    public AllegatiCodaInvioVO() {
    }

    /**
     * @rdbPhysicalName ALLEGATI_CODA_INVIO_ID
     * @rdbNotNull
     * @rdbSize 22
     * @rdbDigits 0
     * @rdbLogicalType DECIMAL
     * @rdbPrimaryKey
     */
    private long allegatiCodaInvioId;

    /**
     * @rdbPhysicalName DESC_ALLEGATO_FILE_NAME
     * @rdbNotNull
     * @rdbSize 254
     * @rdbDigits 0
     * @rdbLogicalType VARCHAR
     */
    private String descrizioneAllegatoFileName;

    /**
     * @rdbPhysicalName BLOB_ALLEGATO_CODA
     * @rdbNotNull
     * @rdbLogicalType OTHER
     */
    private Object blobAllegatoCoda;

    /** @rdbPhysicalName SYS_C0022135 */
    private CodaInvioVO fkCodaInvio;

    /**
     * @return Returns the allegatiCodaInvioId.
     */
    public long getAllegatiCodaInvioId() {
        return allegatiCodaInvioId;
    }

    /**
     * @param allegatiCodaInvioId
     *            The allegatiCodaInvioId to set.
     */
    public void setAllegatiCodaInvioId(long allegatiCodaInvioId) {
        this.allegatiCodaInvioId = allegatiCodaInvioId;
    }

    /**
     * @return Returns the blobAllegatoCoda.
     */
    public Object getBlobAllegatoCoda() {
        return blobAllegatoCoda;
    }

    /**
     * @param blobAllegatoCoda
     *            The blobAllegatoCoda to set.
     */
    public void setBlobAllegatoCoda(Object blobAllegatoCoda) {
        this.blobAllegatoCoda = blobAllegatoCoda;
    }

    /**
     * @return Returns the descrizioneAllegatoFileName.
     */
    public String getDescrizioneAllegatoFileName() {
        return descrizioneAllegatoFileName;
    }

    /**
     * @param descrizioneAllegatoFileName
     *            The descrizioneAllegatoFileName to set.
     */
    public void setDescrizioneAllegatoFileName(
            String descrizioneAllegatoFileName) {
        this.descrizioneAllegatoFileName = descrizioneAllegatoFileName;
    }

    /**
     * @return Returns the fkCodaInvio.
     */
    public CodaInvioVO getFkCodaInvio() {
        return fkCodaInvio;
    }

    /**
     * @param fkCodaInvio
     *            The fkCodaInvio to set.
     */
    public void setFkCodaInvio(CodaInvioVO fkCodaInvio) {
        this.fkCodaInvio = fkCodaInvio;
    }
}