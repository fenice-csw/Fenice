package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;

import java.util.Collection;

public interface AlberoUfficiForm {
    public Collection<UfficioVO> getUfficiDipendenti();

    public void setUfficiDipendenti(Collection<UfficioVO> ufficiDipendenti);

    public UfficioVO getUfficioCorrente();

    public void setUfficioCorrente(UfficioVO ufficioCorrente);

    public int getUfficioCorrenteId();

    public void setUfficioCorrenteId(int ufficioCorrenteId);

    public String getUfficioCorrentePath();

    public void setUfficioCorrentePath(String ufficioCorrentePath);

    public int getUfficioSelezionatoId();

    public void setUfficioSelezionatoId(int ufficioSelezionatoId);
    
   

}