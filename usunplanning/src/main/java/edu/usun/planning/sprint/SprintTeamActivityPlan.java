package edu.usun.planning.sprint;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.activity.Activity;
import edu.usun.planning.team.Team;

/**
 * How much story points / velocity is planned by a team for a given activity within a given sprint.
 * Represents an assignment/plan.
 * 
 * @author usun
 */
public class SprintTeamActivityPlan extends PlanEntity {

	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/** Sprint. */
	protected Sprint sprint;
	
	/** Team. */
	protected Team team;
	
	/** Activity for a team during this sprint. */
	protected Activity activity;
	
	/** Team planned/assigned amount of story points for some activity within this sprint. */
	protected int storyPoints;
	
	/**
	 * Default constructor.
	 */
	public SprintTeamActivityPlan() {
		super();
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
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}

	/**
	 * @param activity the activity to set
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	/**
	 * @return the storyPoints
	 */
	public int getStoryPoints() {
		return storyPoints;
	}

	/**
	 * @param storyPoints the storyPoints to set
	 */
	public void setStoryPoints(int storyPoints) {
		this.storyPoints = storyPoints;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer().append(this.getName()).append('-').append(
			this.getTeam() == null ? Team.TEAM_NOT_ASSIGNED : getTeam().getName()).append(
				'-').append(this.getStoryPoints()).toString();
	}
	
}
