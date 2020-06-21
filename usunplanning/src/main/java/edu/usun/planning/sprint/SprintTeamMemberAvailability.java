package edu.usun.planning.sprint;

import java.math.BigDecimal;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.PlanEntityUtils;
import edu.usun.planning.team.TeamMember;

/**
 * Individual availability/velocity of the team member during some sprint.
 * 
 * @author usun
 */
public class SprintTeamMemberAvailability extends PlanEntity {

	
	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/** Sprint. */
	protected Sprint sprint;
	
	/** Team member. */
	protected TeamMember teamMember;
	
	/** Velocity of a team member for a given sprint. In story points. */
	protected BigDecimal velocity;
	
	/**
	 * Default constructor.
	 */
	public SprintTeamMemberAvailability() {
		super();
	}

	/**
	 * @return the sprint
	 */
	public Sprint getSprint() {
		return sprint;
	}

	/**
	 * @param sprint the sprint to set
	 */
	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}

	/**
	 * @return the team member
	 */
	public TeamMember getTeamMember() {
		return teamMember;
	}

	/**
	 * @param teamMember the team member to set
	 */
	public void setTeamMember(TeamMember teamMember) {
		this.teamMember = teamMember;
	}

	/**
	 * @return the velocity
	 */
	public BigDecimal getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(BigDecimal velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return PlanEntityUtils.toStringStandard(this, new String[] {
			"sprint", "teamMember", "velocity"});
	}

}
