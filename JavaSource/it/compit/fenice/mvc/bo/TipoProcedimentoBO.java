package it.compit.fenice.mvc.bo;

import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.Iterator;
import java.util.Map;

public class TipoProcedimentoBO {

	public static void putAllegato(DocumentoVO doc, Map<String,DocumentoVO> documenti) {
		int idx = doc.getIdx();
		if (idx == 0) {
			idx = getNextDocIdx(documenti);
		}
		doc.setIdx(idx);
		documenti.put(String.valueOf(idx), doc);
	}

	private static int getNextDocIdx(Map<String,DocumentoVO> allegati) {
		int max = 0;
		Iterator<String> it = allegati.keySet().iterator();
		while (it.hasNext()) {
			String id = it.next();
			int cur = NumberUtil.getInt(id);
			if (cur > max)
				max = cur;
		}
		return max + 1;
	}

}