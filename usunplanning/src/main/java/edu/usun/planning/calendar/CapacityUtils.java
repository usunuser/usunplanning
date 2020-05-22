package edu.usun.planning.calendar;

import java.util.Calendar;
import java.util.List;

/**
 * Some utilities to handle capacity related information.
 * 
 * @author usun
 */
public final class CapacityUtils {

	/**
	 * Private constructor.
	 */
	private CapacityUtils() {
		super();
	}
	
	/**
	 * Searches for the first capacity override record to match the provided date.
	 * @param dateToCheck The date to check for. Time of the day is ignored.
	 * @param capacityOverrides List of capacity overrides records to search within.
	 * @return the first capacity override record to match the provided date. Null if not found.
	 */
	public static CapacityOverride findCapacityOverride(Calendar dateToCheck, List<CapacityOverride> capacityOverrides) {
		if (dateToCheck == null || capacityOverrides == null || capacityOverrides.isEmpty()) {
			return null;
		}
		for (CapacityOverride capacityOverride : capacityOverrides) {
			if (capacityOverride == null || capacityOverride.getDate() == null) {
				continue;
			}
			Calendar capacityOverrideCal = capacityOverride.getDate();
			if (capacityOverrideCal.get(Calendar.DAY_OF_YEAR) == dateToCheck.get(Calendar.DAY_OF_YEAR) &&
					capacityOverrideCal.get(Calendar.YEAR) == dateToCheck.get(Calendar.YEAR)) {
				return capacityOverride;
			}
		}
		return null;
	}
}
