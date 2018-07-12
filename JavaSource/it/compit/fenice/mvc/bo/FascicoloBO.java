package it.compit.fenice.mvc.bo;

import it.finsiel.siged.mvc.business.FascicoloDelegate;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FascicoloBO {
	
	public static String getProgressivo(int fascicoloId) {
			FascicoloVO fVO=FascicoloDelegate.getInstance().getFascicoloById(fascicoloId).getFascicoloVO();
			if (fVO.getParentId() != 0) 
				return getProgressivo(fVO.getParentId())+"/"+String.valueOf(fVO.getProgressivo());
			else
				return String.valueOf(fVO.getProgressivo());
	}
	
	public static LinkedHashMap<Integer,ReportProtocolloView> sortHashMapByValues(Map<Integer,ReportProtocolloView> passedMap) {
	    List<Integer> mapKeys = new ArrayList<Integer>(passedMap.keySet());
	    List<ReportProtocolloView> mapValues = new ArrayList<ReportProtocolloView>(passedMap.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);       
	    LinkedHashMap<Integer,ReportProtocolloView> sortedMap = new LinkedHashMap<Integer,ReportProtocolloView>();	    
	    Iterator<ReportProtocolloView> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Object val = valueIt.next();
	        Iterator<Integer> keyIt = mapKeys.iterator();
	        while (keyIt.hasNext()) {
	            Object key = keyIt.next();
	            String comp1 = passedMap.get(key).toString();
	            String comp2 = val.toString();
	            if (comp1.equals(comp2)){
	                passedMap.remove(key);
	                mapKeys.remove(key);
	                sortedMap.put((Integer)key, (ReportProtocolloView)val);
	                break;
	            }

	        }
	    }
	    return sortedMap;
	}
	 
	
}
