package it.finsiel.siged.mvc.vo.lookup;

import it.finsiel.siged.mvc.vo.VersioneVO;
import it.finsiel.siged.util.NumberUtil;

public class IdentityVO extends VersioneVO {

	private static final long serialVersionUID = 1L;

	private String codice;

    private String name;

    private String description;

    public IdentityVO() {

    }

    public IdentityVO(String codice, String description) {
        this.codice = codice;
        this.description = description;
    }

    public IdentityVO(int id, String description) {
        setId(id);
        this.description = description;
    }

    public String getCodice() {
        return codice;
    }

    public int getCodiceAsInt() {
        return NumberUtil.getInt(codice);
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descrizione) {
        this.description = descrizione;
    }
}