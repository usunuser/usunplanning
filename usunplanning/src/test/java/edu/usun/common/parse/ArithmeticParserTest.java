package edu.usun.common.parse;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;

public class ArithmeticParserTest {

	@Test
	public void testEvaluateArithmeticExpression() {
		ArithmeticParser parser = ArithmeticParser.getInstance();
		assertTrue(new BigDecimal(0).compareTo(parser.evaluateArithmeticExpression("")) == 0);
		assertTrue(new BigDecimal(21).compareTo(parser.evaluateArithmeticExpression("1+20")) == 0);
		assertTrue(new BigDecimal(61).compareTo(parser.evaluateArithmeticExpression("1+20*3")) == 0);
		assertTrue(new BigDecimal(100).compareTo(parser.evaluateArithmeticExpression("100")) == 0);
		assertTrue(new BigDecimal(9).compareTo(parser.evaluateArithmeticExpression("(1+2)*3")) == 0);
		assertTrue(new BigDecimal(17).compareTo(parser.evaluateArithmeticExpression("(1-2)*3-4*5*(3-4)")) == 0);
		assertTrue(new BigDecimal(31).compareTo(parser.evaluateArithmeticExpression("1+(2+(3-4)*2+5)*6")) == 0);
		
	}

	@Test
	public void testInfixToPostfix() {
		ArithmeticParser parser = ArithmeticParser.getInstance();
		assertTrue("1 20 +".equals(parser.infixToPostfix("1+20")));
		assertTrue("1 20 3 * +".equals(parser.infixToPostfix("1+20*3")));
		assertTrue("100".equals(parser.infixToPostfix("100")));
		assertTrue("".equals(parser.infixToPostfix("	 	")));
		assertTrue("100 20 3 * +".equals(parser.infixToPostfix(" 100	+ 20	*			3  ")));
		assertTrue("".equals(parser.infixToPostfix("")));
		assertTrue("20 30 40 + *".equals(parser.infixToPostfix("20*(30+40)")));
		assertTrue("50 20 - 5 /".equals(parser.infixToPostfix("(50-20)/5")));
		assertTrue("1 2 + 3 4 - *".equals(parser.infixToPostfix("(1 + 2) * (3 - 4)")));
		assertTrue("1 2 3 4 - * +".equals(parser.infixToPostfix("1+2*(3-4)")));
		assertTrue("11 22 + 33 -".equals(parser.infixToPostfix("  11+22		-33")));
		try {
			parser.infixToPostfix(null);
			fail("Null was not checked");
		} catch (IllegalArgumentException e) {
		}
		try {
			parser.infixToPostfix("2+3 +A");
			fail("Invalid expression not validated.");
		} catch (IllegalArgumentException e) {
		}
		
		
	}

}
