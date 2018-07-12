package it.finsiel.siged.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * @author Almaviva sud.
 */
public class DateUtil {
	
	private DateUtil() {
	}

	public static Date getTomorrow() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +1);
		return cal.getTime();
	}
	
	public static Date getYesterdayMidnight() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static Date getToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static String getYesterdayDateString() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return dateFormat.format(cal.getTime());
	}
	
	public static Date addHourMinutesToToday(long hourMinutes) {
		Calendar c=Calendar.getInstance();
		c.setTimeInMillis(hourMinutes);
		Calendar cNow = Calendar.getInstance();
		cNow.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY));
		cNow.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
		return cNow.getTime();
	}
	
	public static String addDays(String date, int g) {
		Date d = toDate(date);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, g);
		return formattaData(c.getTimeInMillis());
	}
	
	public static String addDaysToDataOraString(String date, int g) {
		Date d =getDataOra(date);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, g);
		return formattaDataOra(c.getTimeInMillis());
	}
	
	public static String addDaysToDataOra(Date d, int g) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, g);
		return formattaDataOra(c.getTimeInMillis());
	}

	public static java.sql.Date addDays(Date date, int g) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, g);
		java.sql.Date d=new java.sql.Date(c.getTime().getTime());
		return d;
	}
	
	public static int getDiffDay(String date) {
		Date d = toDate(date);
		Calendar c = Calendar.getInstance();
		Calendar cNow = Calendar.getInstance();
		cNow.setTimeInMillis(System.currentTimeMillis());
		long ldate1 = d.getTime() + c.get(Calendar.ZONE_OFFSET)
				+ c.get(Calendar.DST_OFFSET);
		long ldate2 = cNow.getTime().getTime() + cNow.get(Calendar.ZONE_OFFSET)
				+ cNow.get(Calendar.DST_OFFSET);
		int hr1 = (int) (ldate1 / 3600000);
		int hr2 = (int) (ldate2 / 3600000);
		int days1 = (int) hr1 / 24;
		int days2 = (int) hr2 / 24;
		int dateDiff = days2 - days1;
		return dateDiff;
	}
	
	/*
	public static int getDiffHours(Date d) {
		Calendar c = Calendar.getInstance();
		Calendar cNow = Calendar.getInstance();
		cNow.setTimeInMillis(System.currentTimeMillis());
		long ldate1 = d.getTime() + c.get(Calendar.ZONE_OFFSET)
				+ c.get(Calendar.DST_OFFSET);
		long ldate2 = cNow.getTime().getTime() + cNow.get(Calendar.ZONE_OFFSET)
				+ cNow.get(Calendar.DST_OFFSET);
		int hr1 = (int) (ldate1 / 3600000);
		int hr2 = (int) (ldate2 / 3600000);
		int hrDiff = hr2 - hr1;
		return hrDiff;
	}
	*/

	public static int getYear(Date date) {
		int d = 0;
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			d = Integer.parseInt(df.format(date));
		}
		return d;
	}

	public static Date convertDate(Date date) {
		return toDate((formattaData(date.getTime())));
	}
	
	public static Date toDate(String date) {
		Date d = null;
		try {
			d = _data.parse(date);
		} catch (Exception e) {

		}
		return d;
	}

	public static boolean isData(long date) {
		return !"".equals(formattaData(date));
	}

	public static boolean isData(String date) {
		Date d = toDate(date);
		return d != null && _data.format(d).equals(date);
	}

	public static String formattaDataOra(long date) {
		try {
			return _dataOra.format(new Date(date));
		} catch (Exception e) {

		}
		return "";
	}

	public static Date getDataOra(String dataOra) {
		try {
			return _dataOra.parse(dataOra);
		} catch (Exception e) {

		}
		return null;
	}
	
	public static Date getOraMinuti(String dataOra) {
		try {
			return _time.parse(dataOra);
		} catch (Exception e) {

		}
		return null;
	}
	
	public static String formattaOraMinuti(long time) {
		try {
			return _time.format(new Date(time));
		} catch (Exception e) {

		}
		return "";
	}

	public static Date getData(String data) {
		try {
			return _data.parse(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getDataYYYYMMDD(String date) {
		try {
			return _data_yyyymmdd.parse(date);
		} catch (Exception e) {

		}
		return null;
	}
	
	public static String formattaData(long date) {
		try {
			return _data.format(new Date(date));
		} catch (Exception e) {
		}
		return "";
	}

	
	public static String getDataYYYYMMDD(long date) {
		try {
			return _data_yyyymmdd.format(new Date(date));
		} catch (Exception e) {

		}
		return "";
	}
	
	public static String getDataForIndex(long date) {
		try {
			return _data_index.format(new Date(date));
		} catch (Exception e) {

		}
		return "";
	}

	public static Date getDataFutura(long data, int anno) {
		try {

			return _dataCompatta.parse((String) String.valueOf(Integer
					.parseInt(_dataCompatta.format(new Date(data))))
					+ anno);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date getUltimoGiornoAnnoCorrente() {
		try {
			int anno=getYear(new Date(System.currentTimeMillis()));
			String data="31/12/"+anno;
			return _data.parse(data);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Date getUltimoGiornoAnno(int anno) {
		try {
			String data="31/12/"+anno;
			return _data.parse(data);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Date getPrimoGiornoAnno(int anno) {
		try {
			String data="01/01/"+anno;
			return _data.parse(data);
		} catch (Exception e) {
			return null;
		}
	}
	
	private final static SimpleDateFormat _dataOra = new SimpleDateFormat(
			"dd/MM/yyyy - HH:mm");

	private final static SimpleDateFormat _data = new SimpleDateFormat(
			"dd/MM/yyyy");

	private final static SimpleDateFormat _dataCompatta = new SimpleDateFormat(
			"ddMMyyyy");

	private final static SimpleDateFormat _data_yyyymmdd = new SimpleDateFormat(
			"yyyy-MM-dd");

	private final static SimpleDateFormat _data_index = new SimpleDateFormat(
			"yyyyMMdd");
	
	private final static SimpleDateFormat _time = new SimpleDateFormat(
			"HH:mm");
	
	public static int getAnnoCorrente() {
		int anno=getYear(new Date(System.currentTimeMillis()));
		return anno;
	}

}