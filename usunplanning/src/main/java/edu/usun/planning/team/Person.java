package edu.usun.planning.team;

import java.util.List;
import java.util.stream.Collectors;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.calendar.CapacityOverride;
	
/**
 * Represents employee or contractor, can be assigned to teams.
 * 
 * @author usun
 */
public class Person extends PlanEntity {

	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Personal days off, vacations, overtime. 
	 * Normally covers the same period of time as workCalendar.
	 */
	protected List<CapacityOverride> personalCapacityOverrides;

	/**
	 * Default constructor.
	 */
	public Person() {
		super();
	}

	/**
	 * @return the personalCapacityOverrides
	 */
	public List<CapacityOverride> getPersonalCapacityOverrides() {
		return personalCapacityOverrides;
	}

	/**
	 * @param personalCapacityOverrides the personalCapacityOverrides to set
	 */
	public void setPersonalCapacityOverrides(List<CapacityOverride> personalCapacityOverrides) {
		this.personalCapacityOverrides = personalCapacityOverrides;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer()
			.append("Person{")
			.append("name=").append(this.getName()).append(',')
			.append("personalCapacityOverrides=[");

		if (this.getPersonalCapacityOverrides() != null && !this.getPersonalCapacityOverrides().isEmpty()) {
			sb.append(this.getPersonalCapacityOverrides()
				.stream()
				.map(CapacityOverride::toString)
				.collect(Collectors.joining(",")).toString());
		}
		
		sb.append(']');
		return sb.append('}').toString();
	}
}