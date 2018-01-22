package util;

import org.junit.Test;

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
		String testString = " \t nam   \n   \t nam kose\ngutt\n  <3  ";
		String expected   = "nam nam kosegutt <3";

		assertEquals("Whitespace removal",expected, InputChecker.removeExtraWhitespace(testString));
	}
}
