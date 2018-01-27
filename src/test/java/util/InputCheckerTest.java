package util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

import static org.junit.Assert.*;


/**
 * @author Bjørn Kristian Punsvik
 */
public class InputCheckerTest {

	@Test
	public void isEmptyTest(){
		String testString = "";
		String nullString = null;

		assertTrue(InputChecker.isEmpty(testString));
		assertTrue(InputChecker.isEmpty(nullString));
	}

	@Test
	public void isPhoneNumberTest(){
		String nullNumber = null;
		String characterNumber = "100-20+31(19)";
		String letterNumber = "200100a";
		String tooManyNumbersNumber = "1234567891234567";

		assertFalse("Nullnumber worked", InputChecker.isPhoneNumber(nullNumber));
		assertTrue("Characternumber worked", InputChecker.isPhoneNumber(characterNumber));
		assertFalse("Number with letters worked", InputChecker.isPhoneNumber(letterNumber));
		assertFalse("Number with too many numbers worked", InputChecker.isPhoneNumber(tooManyNumbersNumber));
	}

	@Test
	public void isEmailTest(){
		String nullEmail = null;
		String withoutAtEmail = "testemail.gmail.com";
		String withoutSiteEmail = "testemail@.com";
		String withoutDotComEmail = "testemail@gmail";
		String regularEmail = "testemail@gmail.com";

		assertFalse("Null email worked", InputChecker.isEMail(nullEmail));
		assertFalse("Email without @ worked", InputChecker.isEMail(withoutAtEmail));
		assertFalse("Email without site worked", InputChecker.isEMail(withoutSiteEmail));
		assertFalse("Email without .com worked", InputChecker.isEMail(withoutDotComEmail));
		assertTrue("Regular email didn't work", InputChecker.isEMail(regularEmail));
	}

	@Test
	public void splitNameTest(){
		String nullName = null;
		String commaSeparatedName = "Johnson, John";
		String spaceSeparatedName = "John Johnson";

		assertNull(InputChecker.splitName(nullName));
		assertEquals("Comma separated failed", InputChecker.splitName(commaSeparatedName).toString(),"John=Johnson");
		assertEquals("Space separated failed", InputChecker.splitName(spaceSeparatedName).toString(), "John=Johnson");
	}


	@Test
	public void removeExtraWhitespaceTest() {
		String x = null;
		assertTrue(InputChecker.removeExtraWhitespace(x) == null);
		String testString = " \t nam   \n   \t nam kose\ngutt\n  <3  ";
		String expected   = "nam nam kosegutt <3";

		assertEquals("Whitespace removal",expected, InputChecker.removeExtraWhitespace(testString));
	}

	@Test
	public void isNameTest(){
		String name = "Arne";
		String notName = "Bill123";
		String empty = "";
		assertTrue(InputChecker.isName(name));
		assertFalse(InputChecker.isName(notName));
		assertFalse(InputChecker.isName(empty));
	}
	@Test
	public void dateAtMidnightTest() {
		Date date = null;
		assertTrue(InputChecker.getDateAtMidnight(date) ==  null);
		date = new Date(1516993355956L);
		Date h = InputChecker.getDateAtMidnight(date);
		String d = h.toString();
		assertEquals(d,"Fri Jan 26 00:00:00 CET 2018");
	}
	@Test
	public void isThisDateValidTest() {
		String date = null;
		assertFalse(InputChecker.isThisDateValid(null,""));
		date = "01/26/2018 00:00:00";
		String format = "MM/dd/yyyy HH:mm:ss";
		assertTrue(InputChecker.isThisDateValid(date,format));
		date = "0303040404004"; // wrong format
		assertFalse(InputChecker.isThisDateValid(date,format));
	}

	@Test
	public void toStringTest(){
		String toStr = null;
		assertFalse(InputChecker.toString(toStr) == null);
		toStr = "Halla";
		assertEquals(InputChecker.toString(toStr),"Halla");
	}
	@Test
	public void containsIgnoreCaseTest(){
		String x = null;
		assertFalse(InputChecker.containsIgnoreCase(x,null));
		String halla = "HALLAPÅBALLA";
		String contain = "LLAP";
		assertTrue(InputChecker.containsIgnoreCase(halla,contain));
		contain = "IIIII";
		assertFalse(InputChecker.containsIgnoreCase(halla,contain));
		contain ="";
		assertTrue(InputChecker.containsIgnoreCase(halla,contain));
	}

	@Test
	public void toDateTimeStringTest() {
		Date date = new Date(1516993355956L);
		assertEquals(InputChecker.toDateTimeString(date),"2018-01-26 20:02:35");
	}
	@Test
	public void fromDateTimeStringTest() throws Exception {
		String x = "2018-01-26 20:02:35";
		Date date = new Date(1516993355956L);
		assertEquals(InputChecker.fromDateTimeString(x).toString(),date.toString());
		x = "2018/01/26 20:02:35";
		assertEquals(InputChecker.fromDateTimeString(x),null);
	}

	@Test
	public void toDateStringTest() {
		Date date = new Date(1516993355956L);
		assertEquals(InputChecker.toDateString(date),"2018-01-26");
	}
	@Test
	public void fromDateStringTest() throws Exception {
		String x = "2018-01-26 20:02:35";
		Date date = new Date(1516993355956L);
		assertEquals(InputChecker.fromDateString(x).toString(),date.toString());
		x = "2018/01/26";
		assertTrue(InputChecker.fromDateString(x) == null);
	}

	@Test
	public void isDuringTest() {
		Date date = new Date(1516993355956L);
		Date dateNow = new Date();
		Date between = new Date(1516993399956L);
		assertTrue(InputChecker.isDuring(between,date,dateNow));
		assertFalse(InputChecker.isDuring(null,date,dateNow));
		assertFalse(InputChecker.isDuring(between,dateNow,date));
	}
	@Test
	public void roundTest() {
		double x = 1.2222;
		int places = 2;
		assertEquals(InputChecker.round(x,places),1.22,0.002);

		try {
			InputChecker.round(-1,2);
			assertFalse(false);
		} catch (IllegalArgumentException e){
			assertTrue(true);
		}
		x = 1.227;
		assertNotEquals(InputChecker.round(x,places),1.22,0.002);
	}
	@Test
	public void capitalizeTest() {
		String x = null;
		String y = "halla";
		String d = "Halla";
		assertEquals(InputChecker.capitalize(y),d);

	}
	@Test
	public void isSameDayTest(){
		Date date = new Date(1516993355956L);
		Date dateNow = new Date();
		assertTrue(InputChecker.isSameDay(date,dateNow));
		date = new Date(1316993355956L);
		assertFalse(InputChecker.isSameDay(date,dateNow));
	}
}
