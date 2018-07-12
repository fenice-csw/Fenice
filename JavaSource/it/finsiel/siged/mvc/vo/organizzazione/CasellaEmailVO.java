package it.finsiel.siged.mvc.vo.organizzazione;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class CasellaEmailVO extends VersioneVO {
    private String server;

    /**
     * @return Returns the server.
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server
     *            The server to set.
     */
    public void setServer(String server) {
        this.server = server;
    }
}