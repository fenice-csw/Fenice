package it.finsiel.siged.mvc.vo.lookup;

import it.finsiel.siged.mvc.vo.VersioneVO;

/**
 * @persistent
 * @rdbPhysicalName TIPI_DOCUMENTO
 * @rdbPhysicalPrimaryKeyName SYS_C0022087
 */
public class ParametriLdapVO extends VersioneVO {

    public ParametriLdapVO() {
    }

    private int versione;

    private int porta;

    private String use_ssl;

    private String host;

    private String dn;

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public String getUse_ssl() {
        return use_ssl;
    }

    public void setUse_ssl(String use_ssl) {
        this.use_ssl = use_ssl;
    }

    public int getVersione() {
        return versione;
    }

    public void setVersione(int versione) {
        this.versione = versione;
    }
}