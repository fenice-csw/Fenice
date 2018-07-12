
package it.finsiel.siged.mvc.vo;

import java.util.Date;


public class VersioneVO extends BaseVO {

	private static final long serialVersionUID = 1L;

	public VersioneVO() {
    }

    /**
     * @rdbPhysicalName VERSIONE
     * @rdbSize
     * @rdbDigits 0
     * @rdbLogicalType NUMBER
     */
    private int versione;

    /**
     * @rdbPhysicalName ROW_CREATED_TIME
     * @rdbSize 7
     * @rdbDigits 0
     * @rdbLogicalType TIMESTAMP
     */
    private Date rowCreatedTime;

    /**
     * @rdbPhysicalName ROW_CREATED_USER
     * @rdbSize 32
     * @rdbDigits 0
     * @rdbLogicalType VARCHAR
     */
    private String rowCreatedUser;

    /**
     * @rdbPhysicalName ROW_UPDATED_TIME
     * @rdbSize 7
     * @rdbDigits 0
     * @rdbLogicalType TIMESTAMP
     */
    private Date RowUpdatedTime;

    /**
     * @rdbPhysicalName ROW_UPDATED_USER
     * @rdbSize 32
     * @rdbDigits 0
     * @rdbLogicalType VARCHAR
     */
    private String rowUpdatedUser;

    /**
     * @return Returns the rowCreatedTime.
     */
    public Date getRowCreatedTime() {
        return rowCreatedTime;
    }

    /**
     * @param rowCreatedTime
     *            The rowCreatedTime to set.
     */
    public void setRowCreatedTime(Date rowCreatedTime) {
        this.rowCreatedTime = rowCreatedTime;
    }

    /**
     * @return Returns the rowCreatedUser.
     */
    public String getRowCreatedUser() {
        
    	return rowCreatedUser;
    }

    /**
     * @param rowCreatedUser
     *            The rowCreatedUser to set.
     */
    public void setRowCreatedUser(String rowCreatedUser) {
        this.rowCreatedUser = rowCreatedUser;
    }

    /**
     * @return Returns the rowUpdatedTime.
     */
    public Date getRowUpdatedTime() {
        return RowUpdatedTime;
    }

    /**
     * @param rowUpdatedTime
     *            The rowUpdatedTime to set.
     */
    public void setRowUpdatedTime(Date rowUpdatedTime) {
        RowUpdatedTime = rowUpdatedTime;
    }

    /**
     * @return Returns the rowUpdatedUser.
     */
    public String getRowUpdatedUser() {
        return rowUpdatedUser;
    }

    /**
     * @param rowUpdatedUser
     *            The rowUpdatedUser to set.
     */
    public void setRowUpdatedUser(String rowUpdatedUser) {
        this.rowUpdatedUser = rowUpdatedUser;
    }

    /**
     * @return Returns the versione.
     */
    public int getVersione() {
        return versione;
    }

    /**
     * @param versione
     *            The versione to set.
     */
    public void setVersione(int versione) {
        this.versione = versione;
    }
}