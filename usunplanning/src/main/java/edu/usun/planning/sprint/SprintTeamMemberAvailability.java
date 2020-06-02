package edu.usun.planning.sprint;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.team.TeamMember;

/**
 * Individual availability of the team member during some sprint.
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
	protected TeamMember member;
	
	/** Velocity/capacity of a team member for a given sprint. In story points. */
	protected int velocity;
	
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
	 * @return the member
	 */
	public TeamMember getMember() {
		return member;
	}

	/**
	 * @param member the member to set
	 */
	public void setMember(TeamMember member) {
		this.member = member;
	}

	/**
	 * @return the velocity
	 */
	public int getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
}
