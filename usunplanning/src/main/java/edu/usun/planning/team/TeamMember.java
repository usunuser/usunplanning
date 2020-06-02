package edu.usun.planning.team;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.calendar.WorkCalendar;

/**
 * Member of a team. Just a single team. 
 * If person participates in multiple teams, then several team member records should be created.
 * 
 * @author usun
 */
public class TeamMember extends PlanEntity {

	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/** Reference to a person acting as this team member. */
	protected Person person;
	
	/**
	 * Team member role.
	 */
	protected Role role;
	
	/**
	 * Team.
	 */
	protected Team team;
	
	/**
	 * Base velocity (functional) for a team member. 
	 * For instance, full time developer might be be 15 story points per 2-week sprint. 
	 * Whereas SCRUM master or PM will hold 0.
	 */
	protected BigDecimal baseVelocityPerSprint;
	
	/**
	 * Capacity factor 1.0 - full time 8h per day.
	 * 7h - 0.875
	 * 7h without 20% - 0.7
	 */
	protected BigDecimal capacityFactor;
	
	/**
	 * Work calendar for a team member 
	 * (typically represents location/office and shared among employees in the same location).
	 */
	protected WorkCalendar workCalendar;
	
	
	/**
	 * Start day of this team member (for a given team).
	 */
	protected Calendar startDate;
	
	/**
	 * Last working day of this team member (for a given team).
	 */
	protected Calendar endDate;
	
	/**
	 * Some notes about this team member.
	 */
	protected String notes;
	

	/**
	 * Default constructor.
	 */
	public TeamMember() {
		super();
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the workCalendar
	 */
	public WorkCalendar getWorkCalendar() {
		return workCalendar;
	}

	/**
	 * @param workCalendar the workCalendar to set
	 */
	public void setWorkCalendar(WorkCalendar workCalendar) {
		this.workCalendar = workCalendar;
	}

	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the baseVelocityPerSprint
	 */
	public BigDecimal getBaseVelocityPerSprint() {
		return baseVelocityPerSprint;
	}

	/**
	 * @param baseVelocityPerSprint the baseVelocityPerSprint to set
	 */
	public void setBaseVelocityPerSprint(BigDecimal baseVelocityPerSprint) {
		this.baseVelocityPerSprint = baseVelocityPerSprint;
	}

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * @return the capacityFactor
	 */
	public BigDecimal getCapacityFactor() {
		return capacityFactor;
	}

	/**
	 * @param capacityFactor the capacityFactor to set
	 */
	public void setCapacityFactor(BigDecimal capacityFactor) {
		this.capacityFactor = capacityFactor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb = new StringBuffer()
			.append("TeamMember{")
			.append("name=").append(this.getName()).append(',')
			.append("person=").append(this.getPerson() == null ? "N/A" : this.getPerson()).append(',')
			.append("role=").append(this.getRole() == null ? "N/A" : this.getRole()).append(',')
			.append("team=").append(this.getTeam() == null ? "N/A" : this.getTeam()).append(',')
			.append("baseVelocityPerSprint=").append(this.getBaseVelocityPerSprint() == null ? "N/A" : 
				this.getBaseVelocityPerSprint().toPlainString()).append(',')
			.append("capacityFactor=").append(this.getCapacityFactor() == null ? "N/A" : 
				this.getCapacityFactor().toPlainString()).append(',')
			.append("workCalendar=").append(this.getWorkCalendar() == null ? "N/A" : this.getWorkCalendar()).append(',')
			.append("startDate=").append(this.getStartDate() == null ? "N/A" : 
				sdf.format(this.getStartDate().getTime())).append(',')
			.append("endDate=").append(this.getEndDate() == null ? "N/A" : 
				sdf.format(this.getEndDate().getTime())).append(',')
			.append("notes=").append(this.getNotes());
			
		return sb.append('}').toString();
	}
}
