/*
 * Created on 2-dic-2004
 *
 * 
 */
package it.finsiel.siged.util;

import org.apache.log4j.Logger;


public final class NumberUtil {

    static Logger logger = Logger.getLogger(NumberUtil.class.getName());

    public static int getInt(String n) {
        try {
            return Integer.parseInt(n);
        } catch (NumberFormatException ne) {
            return -1;
        }
    }

    public static int getInt(Object n) {
        try {
            return Integer.parseInt(String.valueOf(n));
        } catch (Exception ne) {
            return -1;
        }
    }
    
    public static boolean isInteger(String n) {
        try {
            Integer.parseInt(n);
            return true;
        } catch (NumberFormatException ne) {
            return false;
        }
    }

    public static boolean isLong(String n) {
        try {
            Long.parseLong(n);
            return true;
        } catch (NumberFormatException ne) {
            return false;
        }
    }
    
    public static boolean isDouble(String n) {
        try {
            Double.parseDouble(n);
            return true;
        } catch (NumberFormatException ne) {
            return false;
        }
    }
    
    public static String threeChars(String n) {
		if(n == null || n.isEmpty()) {
			return "001";
		}
		char [] s = n.toCharArray();
		boolean changeSignificativeChar = false;
		for(int i = s.length -1; i >= 0; i--) {
			char c = s[i];
			if(i == s.length -1 || changeSignificativeChar) {
				changeSignificativeChar = false;
				if(c == '9') {
					s[i] = 'A';
				} else if(c == 'Z') {
					changeSignificativeChar = true;
					s[i] = '0';
				} else {
					s[i] += 1;
				}
			}
		}
		return String.valueOf(s);
	}
}
