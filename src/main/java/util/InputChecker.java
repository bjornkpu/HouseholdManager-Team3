package util;

import javafx.util.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * <p>A convenience class containing many useful methods.
 * The class is purely static.</p>
 *
 * @author Fredrik Bakke
 * @author Bjørn Kristian Punsvik
 */
public class InputChecker {
	/**
	 * This class is purley static and should never be instantiated.
	 */
	private InputChecker() {}

	/**
	 * <p>Determines wether the {@code String} is empty.</p>
	 *
	 * <p>What defines an empty {@code String} is one or more of the following descriptions:
	 * <ul>
	 *     <li>it is {@code null}</li>
	 *     <li>it contains no characters</li>
	 *     <li>it contains only white space</li>
	 * </ul></p>
	 *
	 * @param s the {@code String}
	 * @return {@code true} if the {@code String} is empty, {@code false} otherwise
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.trim().equals("");
	}

	/**
	 * A private class field holding a {@code Pattern} for the {@link #isPhoneNumber(String)} method.
	 */
	private static Pattern phonePattern;

	/**
	 * Determines wether the {@code String} follows the format of a phone number.
	 *
	 * @param s the {@code String}
	 * @return {@code true} if it follows the format of a phone number, {@code false} otherwise
	 */
	public static boolean isPhoneNumber(String s) {
		if(isEmpty(s)) {
			return false;
		}

		s = s.replace(" ", "").trim();

		String phoneRegex = "[\\-\\+\\(\\)\\,\\.]"; //TODO commas
		String numbersOnly = s.replaceAll(phoneRegex, "");

		if (phonePattern == null) {
			phonePattern = Pattern.compile("\\d{1,15}");
		}

		Matcher matcher = phonePattern.matcher(numbersOnly);

		return matcher.matches();
	}

	/**
	 * A private class field holding a {@code Pattern} for the {@link #isEMail(String)} method.
	 */
	private static Pattern eMailPattern;

	/**
	 * Determines wether the {@code String} follows the format of an e-mail.
	 *
	 *
	 * @param s the {@code String}
	 * @return {@code true} if the {@code String} follows the format of an e-mail, {@code false} otherwise
	 *
	 * see <a href="http://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method">http://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method</a>
	 */
	public static boolean isEMail(String s) {
		if(isEmpty(s)) {
			return false;
		}
		s = s.trim();

		if (eMailPattern == null) {
			String regex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
			eMailPattern = Pattern.compile(regex);
		}

		Matcher matcher = eMailPattern.matcher(s);
		return matcher.matches();
	}

	/**
	 * <p>Finds the first and last name in the name and returns them in a {@code Pair<String, String>}
	 * where the key is the first name and the value is the last name.</p>
	 *
	 * <p>If the name contains any middle names they will be returned as part of the first name.</p>
	 *
	 * <p>A name can be in one of the following formats:
	 * <ul>
	 *     <li>"{@code Firstname With Middlenames Lastname}"</li>
	 *     <li>"{@code Lastname, Firstname With Middlenames}"</li>
	 * </ul>
	 * These will return as {@code Pair<>("Firstname with Middlenames", "Lastname")}.</p>
	 *
	 * @param name the name
	 * @return  a {@code Pair<String, String>} where the key is the first and any middle names and the value is the last name.
	 */
	public static Pair<String, String> splitName(String name) {
		if (isEmpty(name)) {
			return null;
		}
		String fname, lname;
		name = name.trim();

		if (name.contains(", ")) {
			fname = name.substring(name.indexOf(", ") + ", ".length());
			lname = name.substring(0, name.indexOf(", "));
		} else {
			fname = name.substring(0, name.lastIndexOf(" "));
			lname = name.substring(name.lastIndexOf(" ") + " ".length());
		}
		return new Pair<>(fname, lname);
	}

	/**
	 * Takes a {@code Date} and returns that date, but with its hour, minute, second and millisecond set to 0.
	 * Used to isolate the actual date(day of month of year) from the {@code Date}-object.
	 *
	 * @param date the {@code Date}
	 * @return that date at midnight.
	 */
	public static Date getDateAtMidnight(Date date) {
		if(date == null) return null;

		Calendar calendar = Calendar.getInstance();

		calendar.setTime( date );
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * Checks if a {@code String} is formatted after a date format.
	 * @param dateToValidate the {@code String}
	 * @param dateFormat the date format
	 * @return {@code true} if the {@code String} is formatted after the dateformat, {@code false} otherwise
	 */
	public static boolean isThisDateValid(String dateToValidate, String dateFormat) {
		if(dateToValidate == null){
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);

		try {
			//if not valid, it will throw ParseException
			sdf.parse(dateToValidate);

			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * Gives a {@code String} representation of an {@code Object}.
	 * @param o the {@code Object}
	 * @return {@code "null"} if the {@code Object} is {@code null}, otherwise the {@code Object}'s  {@link Object#toString()}
	 */
	public static String toString(Object o) {
		return o != null ? o.toString() : "null";
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static String removeExtraWhitespace(String s) {
		if (s == null) return null;
		s = s.trim();
		s = s.replaceAll("[\\s&&[^ ]]+", " ");
		s = s.replaceAll(" +", " ");
		return s;
	}

	/**
	 *
	 * @param str
	 * @param searchStr
	 * @return
	 * @see <a href="http://stackoverflow.com/questions/14018478/string-contains-ignore-case">http://stackoverflow.com/questions/14018478/string-contains-ignore-case</a>
	 */
	public static boolean containsIgnoreCase(String str, String searchStr)     {
		if(str == null || searchStr == null) return false;

		final int length = searchStr.length();
		if (length == 0)
			return true;

		for (int i = str.length() - length; i >= 0; i--) {
			if (str.regionMatches(true, i, searchStr, 0, length))
				return true;
		}
		return false;
	}

	private static SimpleDateFormat dateTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * @param d
	 * @return
	 */
	public static String toDateTimeString(Date d) {
		return dateTimeFormat.format(d);
	}

	/**
	 * @param s
	 * @return
	 */
	public static Date fromDateTimeString(String s) {
		try {
			return dateTimeFormat.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * @param d
	 * @return
	 */
	public static String toDateString(Date d) {
		return dateFormat.format(d);
	}

	/**
	 * @param s
	 * @return
	 */
	public static Date fromDateString(String s) {
		try {
			return dateTimeFormat.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Checks if a given {@code Date} is during the time period of an athlete's stay at this {@code Location}.
	 * @param date the {@code Date} to check
	 * @return {@code true} if the {@code Date} is during this {@code AthleteLocation}'s time period, {@code false} if it isn't, or is {@code null}
	 */
	public static boolean isDuring(Date date, Date fromDate, Date toDate) {
		return date != null && date.compareTo(fromDate) >= 0 && date.compareTo(toDate) <= 0;
	}

	/**
	 *
	 * @param value
	 * @param places
	 * @return
	 *
	 * @see <a href="http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places">http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places</a>
	 */
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * TODO javadoc this shit
	 * @param string
	 * @return a capitalized version of the given {@code String}
	 * @see <a href="http://stackoverflow.com/questions/1892765/how-to-capitalize-the-first-character-of-each-word-in-a-string">http://stackoverflow.com/questions/1892765/how-to-capitalize-the-first-character-of-each-word-in-a-string</a>
	 */
	public static String capitalize(String string) {
		final int sl = string.length();
		final StringBuilder sb = new StringBuilder(sl);
		boolean lod = false;
		for(int s = 0; s < sl; s++) {
			final int cp = string.codePointAt(s);
			sb.appendCodePoint(lod ? Character.toLowerCase(cp) : Character.toUpperCase(cp));
			lod = Character.isLetterOrDigit(cp);
			if(!Character.isBmpCodePoint(cp)) s++;
		}
		return sb.toString();
	}

	/**
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean isSameDay(Date d1, Date d2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
				cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}
}
