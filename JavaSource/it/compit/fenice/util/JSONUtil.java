package it.compit.fenice.util;

import it.compit.fenice.restful.GestioneArchiviData;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONUtil {

	public static String mapToJson(Map<String,String> map) throws JsonProcessingException{
	    ObjectMapper om = new ObjectMapper();
	    return om.writeValueAsString(map);
	}
	   
	public static String getJsonNodeValue(String json, String key) throws IOException{
	    String value=null;
		ObjectMapper om = new ObjectMapper();
	    JsonNode obj = om.readTree(json);
	    JsonNode node = obj.get(key);
	    value=node.textValue();
	    return value;
	}

	public static String objToData(GestioneArchiviData d) throws JsonProcessingException{
		ObjectMapper om = new ObjectMapper();		
		om.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,false);
	    om.configure(SerializationFeature.INDENT_OUTPUT,true);  
		return om.writeValueAsString(d);
	}
	
}
