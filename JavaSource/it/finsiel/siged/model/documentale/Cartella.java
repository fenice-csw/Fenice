package it.finsiel.siged.model.documentale;

import it.finsiel.siged.mvc.vo.documentale.CartellaVO;
import it.finsiel.siged.mvc.vo.documentale.FileVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cartella {

    private CartellaVO valueObject;

    // K=integer[CartellVO.getId()],V=CartellaVO
    private Map<Integer,CartellaVO> cartelle = new HashMap<Integer,CartellaVO>(5);

    // K=integer[FileVO.getId()], V=FileVO
    private Map<Integer,FileVO> files = new HashMap<Integer,FileVO>(5);

    public Cartella() {
    }

    public void setValueObject(CartellaVO vo) {
        this.valueObject = vo;
    }

    public CartellaVO getValueObject() {
        return valueObject;
    }

    // ===== C A R T E L L E ===== //

    public void addCartella(CartellaVO cartella) {
        cartelle.put(cartella.getId(), cartella);
    }

    public void removeCartella(CartellaVO cartella) {
        cartelle.remove(cartella.getId());
    }

    public Collection<CartellaVO> getCartelle() {
        return cartelle.values();
    }

    public CartellaVO getCartella(int id) {
        return (CartellaVO) cartelle.get(new Integer(id));
    }

    // ===== F I L E S ===== //
    public void addFile(FileVO file) {
        files.put(file.getId(), file);
    }

    public void removeFile(FileVO file) {
        files.remove(file.getId());
    }

    public Collection<FileVO> getFiles() {
        return files.values();
    }

    public FileVO getFile(int id) {
        return (FileVO) files.get(new Integer(id));
    }

    public void reset() {
        cartelle.clear();
        files.clear();
    }
}