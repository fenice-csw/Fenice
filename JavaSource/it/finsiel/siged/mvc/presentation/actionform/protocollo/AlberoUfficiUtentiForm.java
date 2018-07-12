package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.Collection;

public interface AlberoUfficiUtentiForm extends AlberoUfficiForm {

    public int getUtenteSelezionatoId();

    public void setUtenteSelezionatoId(int utenteSelezionatoId);

    public Collection<UtenteVO> getUtenti();

    public void setUtenti(Collection<UtenteVO> utenti);
}