package edu.usun.planning.sprint;

import java.math.BigDecimal;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.PlanEntityUtils;
import edu.usun.planning.team.Team;

/**
 * Available teams, their members and their velocities during the sprint 
 * to be used up in planning/assignments.
 * 
 * @author usun
 */
public class SprintTeamAvailability extends PlanEntity {

	
	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/** Sprint. */
	protected Sprint sprint;
	
	/** Team. */
	protected Team team;
	
	/** Velocity/capacity of a team for a given sprint. In story points. An aggregated calculated value. */
	protected BigDecimal velocity;
	
	/**
	 * Default constructor.
	 */
	public SprintTeamAvailability() {
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
			"sprint", "team", "velocity"});
	}

}
