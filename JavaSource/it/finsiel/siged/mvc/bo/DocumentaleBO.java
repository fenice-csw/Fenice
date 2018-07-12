package it.finsiel.siged.mvc.bo;

import it.finsiel.siged.model.documentale.Documento;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.documentale.CartellaVO;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.util.Date;
import java.util.Map;

public class DocumentaleBO {

    public static Documento getDefaultDocumento(Utente utente) {

        FileVO fileVo = new FileVO();
        DocumentoVO documentoVO = new DocumentoVO();
        fileVo.setRowCreatedUser(utente.getValueObject().getUsername());
        fileVo.setRowUpdatedUser(utente.getValueObject().getUsername());
        fileVo.setRowCreatedTime(new Date(System.currentTimeMillis()));
        fileVo.setRowUpdatedTime(new Date(System.currentTimeMillis()));
        fileVo.setOwnerCaricaId(utente.getCaricaInUso());
        Documento doc = new Documento();
        doc.setFileVO(fileVo);
        doc.getFileVO().setDocumentoVO(documentoVO);
        return doc;
    }

    public static void putObject(CartellaVO c, Map<String,CartellaVO> map) {
        int idx = map.size() + 1;
        c.setIdx(idx);
        map.put(String.valueOf(idx), c);
    }
    
}