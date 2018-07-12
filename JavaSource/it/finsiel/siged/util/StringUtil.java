package it.finsiel.siged.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

public final class StringUtil {

	public static String httpPostToString(HttpPost httppost) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("\nRequestLine:");
	    sb.append(httppost.getRequestLine().toString());
	    sb.append("\nHeader:");
	    for(Header header : httppost.getAllHeaders()){
	    	sb.append("\n"+header.getName() + ":" + header.getValue());
	    }
	    HttpEntity entity = httppost.getEntity();

	    String content = "";
	    if(entity != null){
	        try {
	            content = EntityUtils.toString(entity);
	        } catch (Exception e) {
	            e.printStackTrace();
	        } 
	    }
	    sb.append("\nContent:");
	    sb.append(content);


	    return sb.toString();
	}

	
	public static String[] unisciArray(String[] x, String[] y) {
		Set<String> s = new HashSet<String>();
		s.addAll(Arrays.asList(x));
		s.addAll(Arrays.asList(y));
		String[] z = s.toArray(new String[s.size()]);
		Arrays.sort(z);
		return z;

	}

	public static String getString(long l) {
		return String.valueOf(l);
	}

	public static String formattaNumerProtocollo(long num) {
		String n = String.valueOf(num);
		while (n.length() < 13) {
			n = "0" + n;
		}
		return n;
	}

	public static String formattaNumeroProtocollo(String num, int lunghezza) {
		String n = num == null ? "" : num;
		while (n.length() < lunghezza) {
			n = "0" + n;
		}
		return n;
	}

	public static String formattaNumeroProcedimento(String num, int lunghezza) {
		String n = num == null ? "" : num;
		while (n.length() < lunghezza) {
			n = "0" + n;
		}
		return n;
	}

	public static String formattaNumeroFaldone(String num, int lunghezza) {
		String n = num == null ? "" : num;
		while (n.length() < lunghezza) {
			n = "0" + n;
		}
		return n;
	}

	public static String formattaNumeroProtocollo(String num) {
		String n = num;
		while (n.length() < 12) {
			n = "0" + n;
		}
		return n;
	}

	public static String formattaNumeroProtocolli(String num) {
		String n = num;
		while (n.length() < 7) {
			n = "0" + n;
		}
		return n;
	}

	public static String getStringa(String s) {
		if (s == null)
			return "";
		return s;
	}
	
	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public static String[] split(String str, char x) {
		Vector<String> v = new Vector<String>();
		String str1 = new String();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == x) {
				v.add(str1);
				str1 = new String();
			} else {
				str1 += str.charAt(i);
			}
		}
		v.add(str1);
		String array[];
		array = new String[v.size()];
		for (int i = 0; i < array.length; i++) {
			if (v.elementAt(i).equals("")) {
				array[i] = null;

			} else {
				array[i] = new String((String) v.elementAt(i));
			}
		}
		return array;
	}

	public static String[] parseOggettoMail(String oggetto) {
		// AOO FULL - ROOT - 02/10/2014 - 0027887
		String[] result = new String[3];
		int indNumeroProto = oggetto.lastIndexOf('-');
		String num = "";
		if (indNumeroProto > 0){
			num = oggetto.substring(indNumeroProto + 2);
			result[0] = num;
			oggetto = oggetto.substring(0, indNumeroProto - 1);
		}
		int indData = oggetto.lastIndexOf('-');
		String data = "";
		if (indData > 0) {
			data = oggetto.substring(indData + 2);
			oggetto = oggetto.substring(0, indData);
			result[1] = data;
		}
		int numAoo = oggetto.indexOf('-');
		if (numAoo > 0) {
			String aoo = oggetto.substring(0, numAoo - 1);
			result[2] = aoo;
		}
		return result;
	}
	
	public static int parseNumeroprotocollo(String oggetto) {
		int numero = 0;
		if(oggetto!=null && !oggetto.equals("")){
		while (oggetto.startsWith("0")){
				oggetto = oggetto.substring(1);
				numero = Integer.valueOf(oggetto);
			}
		}
		return numero;
	}

}
