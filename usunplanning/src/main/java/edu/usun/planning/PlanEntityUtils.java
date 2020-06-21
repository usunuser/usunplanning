package edu.usun.planning;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


/**
 * Utilities for PlanEntity objects.
 * 
 * @author usun
 */
public final class PlanEntityUtils {
	
	/**
	 * Used as a closure to convert custom field values to string representation.
	 * Used mostly for toString methods implementation.
	 */
	@FunctionalInterface
	public static interface StringRepresentation {
		/**
		 * To convert given value to String representation.
		 * @param value The value to convert to String format.
		 * @return String format of the value.
		 */
		String toStringFormat(Object value);
	}
	
	/** 
	 * Empty field value representation used in toString methods. 
	 */
	public static final String EMPTY_TO_STRING_VALUE = "";
	
	/**
	 * Default date format in string representation: yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * Private constructor.
	 */
	private PlanEntityUtils() {
		super();
	}
	
	/**
	 * Normalized value to be used in toString methods.
	 * @param value The value to normalize.
	 * @return The toString representation of the given field value.
	 */
	public static String toStringValue(String value) {
		return value == null ? EMPTY_TO_STRING_VALUE : value;
	}
	
	/**
	 * Normalized value to be used in toString methods.
	 * @param value The value to normalize.
	 * @return The toString representation of the given field value.
	 */
	public static String toStringValue(BigDecimal value) {
		return value == null ? EMPTY_TO_STRING_VALUE : value.toPlainString();
	}
	
	
	/**
	 * Normalized value to be used in toString methods.
	 * @param value The value to normalize.
	 * @return The toString representation of the given field value.
	 */
	public static String toStringValue(Date value) {
		return toStringValue(value, new SimpleDateFormat(DEFAULT_DATE_FORMAT));
	}
	
	/**
	 * Normalized value to be used in toString methods.
	 * @param value The value to normalize.
	 * @param formatter Date formatter.
	 * @return The toString representation of the given field value.
	 */
	public static String toStringValue(Date value, SimpleDateFormat formatter) {
		return value == null ? EMPTY_TO_STRING_VALUE : formatter.format(value);
	}
	
	/**
	 * Normalized value to be used in toString methods.
	 * @param value The value to normalize.
	 * @return The toString representation of the given field value.
	 */
	public static String toStringValue(Calendar value) {
		return toStringValue(value, new SimpleDateFormat(DEFAULT_DATE_FORMAT));
	}
	
	/**
	 * Normalized value to be used in toString methods.
	 * @param value The value to normalize.
	 * @param formatter Date formatter.
	 * @return The toString representation of the given field value.
	 */
	public static String toStringValue(Calendar value, SimpleDateFormat formatter) {
		return value == null ? EMPTY_TO_STRING_VALUE : formatter.format(value.getTime());
	}
	
	/**
	 * Normalized value to be used in toString methods.
	 * @param value The value to normalize.
	 * @return The toString representation of the given field value.
	 */
	public static String toStringValue(Collection<?> value) {
		if (value == null || value.isEmpty()) {
			return EMPTY_TO_STRING_VALUE;
		}
		return value.stream().map(e -> e.toString()).collect(Collectors.joining(",")).toString();
	}
	
	/**
	 * Normalized value to be used in toString methods.
	 * @param value The value to normalize.
	 * @return The toString representation of the given field value. PlanEntity#getName() value is returned.
	 */
	public static String toStringValue(PlanEntity value) {
		return value == null ? EMPTY_TO_STRING_VALUE : 
			(value.getName() == null ? EMPTY_TO_STRING_VALUE : value.getName());
	}
	
	/**
	 * Normalized value to be used in toString methods.
	 * @param value The value to normalize.
	 * @return The toString representation of the given field value.
	 */
	public static String toStringValue(Object value) {
		return value == null ? EMPTY_TO_STRING_VALUE : value.toString();
	}
	
	/**
	 * Normalized value to be used in toString methods.
	 * @param value The value to normalize.
	 * @param stringRepresentation The closure saying how to convert value to its string representation
	 * in case it's not null. Closure shouldn't be null, or NullPointerException will be thrown.
	 * @return The toString representation of the given field value.
	 */
	public static String toStringValue(Object value, StringRepresentation stringRepresentation) {
		return value == null ? EMPTY_TO_STRING_VALUE : stringRepresentation.toStringFormat(value);
	}
	
	/**
	 * Constructs standard toString value given fields. 
	 * They will be accessed through "getXXX" methods, not directly.
	 * @param entity The the main entity instance to convert to String representation.
	 * @param fieldNames Entity field names to convert to String presentation.
	 * @return The standard toString value, e.g. "Team{name=team A}"
	 */
	public static String toStringStandard(Object entity, String[] fieldNames) {
		if (entity == null || entity.getClass() == null) {
			return "{}";
		}
		StringBuffer sb = new StringBuffer();
		Class<?> entityClazz = entity.getClass();
		String entityClazzName = entityClazz == null || entityClazz.getName() == null ?
			EMPTY_TO_STRING_VALUE : entityClazz.getName();
		int packageSeparatorIndex = entityClazzName.lastIndexOf('.');
				
		sb.append(packageSeparatorIndex >= 0 && packageSeparatorIndex < (entityClazzName.length() - 1) ? 
			entityClazzName.substring(packageSeparatorIndex + 1): entityClazzName).append('{');
		
		if (fieldNames != null && fieldNames.length > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			boolean isFirst = true;
			for (String fieldName : fieldNames) {
				if (fieldName == null || fieldName.length() <= 0) {
					continue;
				}
				if (isFirst) {
					isFirst = false;
				} else {
					sb.append(',');
				}
				try {
					sb.append(fieldName).append('=');
					
					Method getMethod = entityClazz.getMethod(
						new StringBuffer().append("get").append(
							Character.toUpperCase(fieldName.charAt(0))).append(fieldName.substring(1)).toString());
					Object fieldValue = getMethod == null ? null : getMethod.invoke(entity);
					if (fieldValue == null) {
						sb.append(EMPTY_TO_STRING_VALUE);
					} else if (fieldValue instanceof String) {
						sb.append(toStringValue((String) fieldValue));
					} else if (fieldValue instanceof BigDecimal) {
						sb.append(toStringValue((BigDecimal) fieldValue));
					} else if (fieldValue instanceof Date) {
						sb.append(toStringValue((Date) fieldValue, sdf));
					} else if (fieldValue instanceof Calendar) {
						sb.append(toStringValue((Calendar) fieldValue, sdf));
					} else if (fieldValue instanceof Collection<?>) {
						sb.append('[').append(toStringValue((Collection<?>) fieldValue)).append(']');
					} else if (fieldValue instanceof PlanEntity) {
						sb.append(toStringValue((PlanEntity) fieldValue));
					} else {
						sb.append(toStringValue(fieldValue));
					}
				} catch (SecurityException | IllegalAccessException | 
					NoSuchMethodException | InvocationTargetException e) {
					e.printStackTrace(); // TODO proper logging
					continue;
				}
			} // end of for loop through fieldNames
		} // if field names are not empty
		sb.append('}');
		return sb.toString();
	}

}
