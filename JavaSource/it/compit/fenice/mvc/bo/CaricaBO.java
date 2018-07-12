package it.compit.fenice.mvc.bo;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CaricaBO {

	public static CaricaVO getUnicaCarica(Map<Integer,CaricaVO> cariche) {
		Iterator<CaricaVO> i = cariche.values().iterator();
		if (i.hasNext())
			return (CaricaVO) i.next();
		return null;
	}

	public static int getCaricaIdByUfficio(Map<Integer,CaricaVO> cariche, int ufficioId) {
		Iterator<CaricaVO> i = cariche.values().iterator();
		if (i.hasNext()) {
			CaricaVO c = (CaricaVO) i.next();
			if (c.getUfficioId() == ufficioId)
				return c.getCaricaId();
		}
		return 0;
	}

	public static CaricaVO getUnicaCaricaAttiva(Map<Integer,CaricaVO> cariche) {
		Iterator<CaricaVO> i = cariche.values().iterator();
		if (i.hasNext()) {
			CaricaVO c = (CaricaVO) i.next();
			if (c.isAttivo()) {
				return c;
			}
		}
		return null;
	}

	public static Collection<CaricaVO> getCaricheOrdinate(Collection<CaricaVO> caricheSource) {
		List<CaricaVO> list = new ArrayList<CaricaVO>();
		CaricaVO car;
		for (Iterator<CaricaVO> i = caricheSource.iterator(); i.hasNext();) {
			car = (CaricaVO) i.next();
			list.add(car);
		}
		Comparator<CaricaVO> c = new Comparator<CaricaVO>() {
			public int compare(CaricaVO car1, CaricaVO car2) {
				return car1.getNome().compareToIgnoreCase(car2.getNome());
			}
		};
		Collections.sort(list, c);
		return list;
	}

}
