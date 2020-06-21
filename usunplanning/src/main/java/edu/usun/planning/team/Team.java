package edu.usun.planning.team;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.PlanEntityUtils;

/**
 * Represents a team/squad.
 * 
 * @author usun
 */
public class Team extends PlanEntity {
	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/** Indication for internal mapping if there is no assigned team registered for a person. */
	public static final String TEAM_NOT_ASSIGNED = "N/A";
	
	
	/**
	 * Default constructor.
	 */
	public Team() {
		super();
		this.name = TEAM_NOT_ASSIGNED;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return PlanEntityUtils.toStringStandard(this, new String[] {
			"name"});
	}
}
