
package it.finsiel.siged.mvc.vo.lookup;

import it.finsiel.siged.mvc.vo.BaseVO;

/**
 * @author Paolo Spadafora
 * 
 */
public class AooViewVO extends BaseVO {

    private String codice;

    private String nome;

    private String email;

    private String citta;

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
