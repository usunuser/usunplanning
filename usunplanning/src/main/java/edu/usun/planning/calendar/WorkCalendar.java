package edu.usun.planning.calendar;

import java.util.List;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.PlanEntityUtils;

/**
 * Work calendars for office locations (marks public holidays, short work-days).
 * 
 * @author usun
 */
public class WorkCalendar extends PlanEntity {
	
	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Overriding capacity due to public holidays.
	 */
	protected List<CapacityOverride> publicHolidays;
	
	/**
	 * Default constructor.
	 */
	public WorkCalendar() {
		super();
	}

	/**
	 * @return the publicHolidays
	 */
	public List<CapacityOverride> getPublicHolidays() {
		return publicHolidays;
	}

	/**
	 * @param publicHolidays the publicHolidays to set
	 */
	public void setPublicHolidays(List<CapacityOverride> publicHolidays) {
		this.publicHolidays = publicHolidays;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return PlanEntityUtils.toStringStandard(this, new String[] {
			"name", "publicHolidays"});
	}
}
