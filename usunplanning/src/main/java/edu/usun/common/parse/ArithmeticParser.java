package edu.usun.common.parse;

import java.math.BigDecimal;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Provides parse utility methods for arithmetic expressions.
 * 
 * @author usun
 */
public final class ArithmeticParser {
	
	/**
	 * Functional interface to represent arithmetic operation executor.
	 * @param <T> The type to be used for operands.
	 */
	@FunctionalInterface
	protected static interface OperationExecutor<T> {
		/**
		 * Executes operation over given operands.
		 * @param operand1 The first (left) operand.
		 * @param operand2 The second (right) operand.
		 * @return The operation result.
		 */
		T operation(T operand1, T operand2);
	}
	
	/**
	 * Arithmetic operation info.
	 * @param <T> The type to be used for operands.
	 */
	protected static class Operation<T> {
		/** Character code for operation, e.g. '+'. */
		private char operationCode;
		/** Operation priority, the lower number - the higher priority. */
		private int priority;
		/** The operation executor method. */
		private OperationExecutor<T> operationExecutor;
		
		/**
		 * @param operationCode Character code for operation, e.g. '+'.
		 * @param priority Operation priority, the lower number - the higher priority.
		 * @param operationExecutor The operation executor method. Cannot be null.
		 */
		public Operation(char operationCode, int priority, OperationExecutor<T> operationExecutor) {
			super();
			if (operationExecutor == null) {
				throw new IllegalArgumentException("Operation executor implementation should be specified as a parameter.");
			}
			this.operationCode = operationCode;
			this.priority = priority;
			this.operationExecutor = operationExecutor;
		}

		/**
		 * @return the operationCode
		 */
		public char getOperationCode() {
			return operationCode;
		}

		/**
		 * @return the priority
		 */
		public int getPriority() {
			return priority;
		}

		/**
		 * @return the operationExecutor
		 */
		public OperationExecutor<T> getOperationExecutor() {
			return operationExecutor;
		}
		
		/**
		 * Comparison by priority. Lower value means higher priority.
		 * @param priorityToCompare The operator with priority to compare with.
		 * @return &gt;0 if this object has higher priority than the one we are comparing to, 0 if the same priority, &lt;0 if lower priority.
		 */
		public int comparePriorityTo(Operation<T> priorityToCompare) {
			if (priorityToCompare == null) {
				return 1;
			}
			return priorityToCompare.priority - this.priority;
		}
	}
	
	/** Separator for all tokens within postfix notation used internally. */
	protected static final String POSTFIX_TOKEN_SEPARATOR = " ";
	
	/** Open parentheses. */
	protected static final char PARENTHESES_OPEN = '(';
	/** Close parentheses. */
	protected static final char PARENTHESES_CLOSE = ')';

	/**
	 * Single instance.
	 */
	private static final ArithmeticParser INSTANCE = new ArithmeticParser();
	
	
	/**
	 * Maps operation character codes ('+', '-', '/', '*') to information about them, 
	 * including priority.
	 * Not thread safe, but should be ok, since we intend just to read it after singleton creation.
	 */
	private Map<Character, Operation<BigDecimal>> operations;
	
	/**
	 * Prevent explicit instantiation to support singleton pattern.
	 */
	private ArithmeticParser() {
		super();
		operations = new HashMap<>(8, 0.5f);
		operations.put('+', new Operation<BigDecimal>('+', 10, (o1, o2) -> o1.add(o2)));
		operations.put('-', new Operation<BigDecimal>('-', 10, (o1, o2) -> o1.subtract(o2)));
		operations.put('*', new Operation<BigDecimal>('*', 5, (o1, o2) -> o1.multiply(o2)));
		operations.put('/', new Operation<BigDecimal>('/', 5, (o1, o2) -> o1.divide(o2, BigDecimal.ROUND_HALF_UP)));
	}
	
	/**
	 * Evaluate simple arithmetic expression. It supports number operands, 
	 * () parenthesis, +-/* operations and their priority is taken into account.
	 * Calculates the result and returns it.
	 * @param input The characters input representing the arithmetic expression to evaluate. 
	 * Whitespace characters will be ignored. If invalid expression - IllegalArgumentException will be thrown.
	 * @return The result of the arithmetic expression/
	 */
	public BigDecimal evaluateArithmeticExpression(CharSequence input) {
		String postfix = infixToPostfix(input);
		StringTokenizer st = new StringTokenizer(postfix, POSTFIX_TOKEN_SEPARATOR);
		Deque<BigDecimal> stackOperands = new LinkedList<>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.length() == 1 && this.operations.containsKey(token.charAt(0))) {
				// Operator found.
				Operation<BigDecimal> operation = this.operations.get(token.charAt(0));
				if (stackOperands.isEmpty()) {
					throw new IllegalArgumentException(
						"Input had incorrect format and failed to transform to correct postfix expression: " + 
							postfix);
				}
				BigDecimal operand2 = stackOperands.pop();
				if (stackOperands.isEmpty()) {
					throw new IllegalArgumentException(
						"Input had incorrect format and failed to transform to correct postfix expression: " + 
							postfix);
				}
				BigDecimal operand1 = stackOperands.pop();
				stackOperands.push(operation.operationExecutor.operation(operand1, operand2));
			} else {
				try {
					// Operand found.
					stackOperands.push(new BigDecimal(token));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(
						"Input had incorrect format and failed to transform to correct postfix expression: " + 
							postfix + ", failed to parse as number: " + token);
				}
			}
		}
		BigDecimal result = stackOperands.isEmpty() ? new BigDecimal("0") : 
			stackOperands.pop();
		// We shouldn't have anything left here in the stack.
		if (!stackOperands.isEmpty()) {
			throw new IllegalArgumentException(
				"Input had incorrect format and failed to transform to correct postfix expression: " + 
					postfix);
		}
		return result;
	}
	
	/**
	 * Convert infix notation like:
	 * (1+20)/5 
	 * to postfix notation like:
	 * 1 20 + 5 /
	 * @param input The input in infix notation. 
	 * Whitespace characters will be ignored. If invalid expression - IllegalArgumentException will be thrown.
	 * @return The converted result to postfix notation with ' ' as separator of tokens.
	 */
	protected String infixToPostfix(CharSequence input) {
		if (input == null) {
			throw new IllegalArgumentException("Input shouldn't be null.");
		}
		/*
		 * 1+20 				: 1 20 +
		 * 1+20*3				: 1 20 3 * +
		 * (1+20)*3				: 1 20 + 3 *
		 * 1*(20+3)				: 1 20 3 + *
		 */
		StringBuffer result = new StringBuffer();
		
		Deque<Character> operationAndParenthesesStack = new LinkedList<Character>();
		for (int charIndex = 0; charIndex < input.length(); charIndex++) {
			char inputChar = input.charAt(charIndex);
			if (Character.isWhitespace(inputChar)) {
				// Ignore whitespace characters.
				continue;
			}
			
			if (PARENTHESES_OPEN == inputChar) {
				operationAndParenthesesStack.push(inputChar);
			} else if (PARENTHESES_CLOSE == inputChar) {
				while (!operationAndParenthesesStack.isEmpty()) {
					char charFromStack = operationAndParenthesesStack.pop();
					if (charFromStack == PARENTHESES_OPEN) {
						break; // out of looping through stack
					}
					result.append(charFromStack).append(POSTFIX_TOKEN_SEPARATOR);
				}
			} else if (this.operations.containsKey(inputChar)) {
				// Operation found.
				if (operationAndParenthesesStack.isEmpty()) {
					operationAndParenthesesStack.push(inputChar);
				} else {
					Operation<BigDecimal> currentOperation = this.operations.get(inputChar);
					while (!operationAndParenthesesStack.isEmpty()) {
						char charFromStack = operationAndParenthesesStack.pop();
						if (charFromStack == PARENTHESES_OPEN) {
							operationAndParenthesesStack.push(charFromStack);
							break; // out of looping through stack
						} else if (this.operations.containsKey(charFromStack)) {
							Operation<BigDecimal> topOperation = this.operations.get(charFromStack);
							if (topOperation.comparePriorityTo(currentOperation) < 0) {
								operationAndParenthesesStack.push(charFromStack);
								break; // out of looping through stack
							} else {
								result.append(charFromStack).append(POSTFIX_TOKEN_SEPARATOR);
							}
						}
					} // end of loop through stack
					operationAndParenthesesStack.push(inputChar);
				}
			} else if (Character.isDigit(inputChar)) {
				// Construct multi-character operand
				StringBuffer operand = new StringBuffer();
				do {
					operand.append(inputChar);
					charIndex++;
				} while (charIndex < input.length() && Character.isDigit(inputChar = input.charAt(charIndex)));
				result.append(operand).append(POSTFIX_TOKEN_SEPARATOR);
				charIndex--;
			} else {
				// Unexpected character.
				throw new IllegalArgumentException("Unexpected character found: " + inputChar);
			}
			
		} // end of loop: for (int charIndex = 0; charIndex < input.length(); charIndex++)
		
		while (!operationAndParenthesesStack.isEmpty()) {
			result.append(operationAndParenthesesStack.pop()).append(POSTFIX_TOKEN_SEPARATOR);
		}
		
		return result.length() == 0 ? "" : 
			result.substring(0, result.length() - POSTFIX_TOKEN_SEPARATOR.length());
	}
	
	/**
	 * @return The single instance returned.
	 */
	public static ArithmeticParser getInstance() {
		return INSTANCE;
	}
}
