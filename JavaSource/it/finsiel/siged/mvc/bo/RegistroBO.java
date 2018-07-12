package it.finsiel.siged.mvc.bo;

import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

public final class RegistroBO {

    public static Date getDataAperturaRegistro(RegistroVO registro) {
        if (!registro.getDataBloccata()) {
            Date oggi = DateUtils.truncate(new Date(), Calendar.DATE);
            if (!oggi.equals(registro.getDataAperturaRegistro())) {
                RegistroDelegate delegate = RegistroDelegate.getInstance();
                int registroId = registro.getId().intValue();
                delegate.setDataAperturaRegistro(registroId, oggi.getTime());
                registro.setDataAperturaRegistro(oggi);
            }
        }
        return registro.getDataAperturaRegistro();
    }

    public static int getRegistroUfficialeId(Collection<RegistroVO> registri) {
        int registroId = -1;
        Iterator<RegistroVO> i = registri.iterator();
        while (i.hasNext()) {
            RegistroVO registro = i.next();
            if (registro.getUfficiale()) {
                registroId = registro.getId().intValue();
                break;
            }
        }
        return registroId;
    }

    public static RegistroVO getUnicoRegistro(Map<Integer,RegistroVO> registri) {
        Iterator<RegistroVO> i = registri.values().iterator();
        if (i.hasNext())
            return i.next();
        return null;
    }

    public static boolean controllaPermessoRegistro(int registroId,
            Utente utente) {
        return utente.getRegistri().get(new Integer(registroId)) != null;
    }

    public static Collection<RegistroVO> getRegistriOrdinatiByFlagUfficiale(
            Collection<RegistroVO> registriUtente) {
        List<RegistroVO> list = new ArrayList<RegistroVO>();
        RegistroVO reg;
        for (Iterator<RegistroVO> i = registriUtente.iterator(); i.hasNext();) {
            reg = i.next();
            list.add(reg);
        }
        Comparator<RegistroVO> c = new Comparator<RegistroVO>() {
            public int compare(RegistroVO reg1, RegistroVO reg2) {
                if ((reg1.getUfficiale() && reg2.getUfficiale())
                        || (!reg1.getUfficiale() && !reg2.getUfficiale()))
                    return 0;
                else if (reg1.getUfficiale() && !reg2.getUfficiale())
                    return -1;
                else
                    return 1;
                /*
                 * return Boolean.toString(reg2.getUfficiale())
                 * .compareToIgnoreCase( Boolean.toString(reg1.getUfficiale()));
                 */
            }
        };
        Collections.sort(list, c);
        return list;
    }

	public static int getRegistroPostaInternaId(Collection<RegistroVO> registri) {
		int registroId = -1;
		 	Iterator<RegistroVO> i = registri.iterator();
	        while (i.hasNext()) {
	            RegistroVO registro =  i.next();
	            if (registro.getCodRegistro().equals(Parametri.COD_REGISTRO_POSTA_INTERNA)) {
	                registroId = registro.getId().intValue();
	                break;
	            }
	        }
	        return registroId;
	}

}
