package edu.usun.planning.util;

import java.util.Calendar;

/**
 * Dates/Calendar related utils.
 * 
 * @author usun
 */
public final class DateUtils {

	/**
	 * Private constructor.
	 */
	private DateUtils() {
		super();
	}
	
	/**
	 * Check if date ranges overlap or not.
	 * @param startA Date range 1 start.
	 * @param endA Date range 1 end.
	 * @param startB Date range 2 start.
	 * @param endB Date range 2 end.
	 * @return Boolean flag indicating if date ranges overlap or not.
	 */
	public static boolean doDateRangesOverlap(Calendar startA, Calendar endA, Calendar startB, Calendar endB) {
		return (startA == null || endB == null || !startA.after(endB)) && 
			(endA == null || startB == null || !endA.before(startB));
	}

	/**
	 * @param dateToCheck Date to check.
	 * @param startDate Start date in the range.
	 * @param endDate End date in the range.
	 * @return If date is within the given range.
	 */
	public static boolean isDateWithinRange(Calendar dateToCheck, Calendar startDate, Calendar endDate) {
		if (dateToCheck == null) {
			throw new IllegalArgumentException("dateToCheck parameter is null");
		}
		if (startDate == null && endDate == null) {
			return true;
		}
		if (startDate == null) {
			return dateToCheck.before(endDate);
		}
		if (endDate == null) {
			return dateToCheck.after(startDate);
		}
		
		return dateToCheck.after(startDate) && dateToCheck.before(endDate);
	}
	
	/**
	 * @param startDate Start date in the range.
	 * @param endDate End date in the range.
	 * @return Amount of standard Western work-days within given date range without taking into account any public holidays, i.e. 
	 * weekends (Saturday and Sunday) are non-working days and all other days of the week are considered work-days.
	 */
	public static int amountOfNormalWorkDaysWithinRange(Calendar startDate, Calendar endDate) {
		if (startDate == null) {
			throw new IllegalArgumentException("startDate parameter is null");
		}
		if (endDate == null) {
			throw new IllegalArgumentException("endDate parameter is null");
		}
		int workDaysAmount = 0;
		Calendar day = (Calendar) startDate.clone();
		while (!day.after(endDate)) {
			if (isStandardWorkDay(day)) {
				workDaysAmount++;
			}
			day.add(Calendar.DAY_OF_MONTH, 1);
		}
		return workDaysAmount;
	}
	
	/**
	 * @param day Date to check.
	 * @return if standard Western work-day (not Saturday nor Sunday)
	 */
	public static boolean isStandardWorkDay(Calendar day) {
		if (day == null) {
			return false;
		}
		int dayOfWeek = day.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
	}
}
