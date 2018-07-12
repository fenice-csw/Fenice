
package it.finsiel.siged.mvc.presentation.helper;


public class MenuView {
    int id;

    String descrizione;

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        return descrizione;
    }
    
    public String getShortDescrizione(){
    	String[] descrizioneSplitted = descrizione.split("/");
    	return descrizioneSplitted[descrizioneSplitted.length-1];
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MenuView other = (MenuView) obj;
		if (id != other.id)
			return false;
		return true;
	}
    
    
}
