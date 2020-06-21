package edu.usun.planning.sprint;

import java.util.List;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.PlanEntityUtils;
import edu.usun.planning.team.Person;

/**
 * Person capacity plan for the sprint.
 * 
 * Example breakdown:
 * Total: 8d = 5.5 + 0.55 + 0.55 + 0.165 + 1.235
 *		 Velocity: 11sp -> 5.5d as pure feature development
 *		 SCRUM (10%): 0.55d
 *		 Branches/Rebases/Release Management (5% + 5%) = 0.55d
 *		 Other costs (collaboration with other teams, 3%): 0.165d
 *
 * Bug fixing / support (22.5%): 1.235d - this is on-going bug fixing both internal, 
 * regression failures to features already delivered (this is on-going cost even beyond UAT) and UAT. 
 * Total UAT quota around 30% will be coming in waves and in around go-lives the velocity 
 * will be reduced to account for additional UAT. 
 * Another way to account for 7%+ difference with UAT quota is the fact that some devs and tests with velocity 
 * assigned less that 11sp will be assigned disproportionately more on support, 
 * and this should tilt totals for the team to match Business Case quotas on totals.
 * 
 * @author usun
 */
public class SprintPersonCapacity extends PlanEntity {
	
	/**
	 * Name of the capacity breakdown element/bucket to hold functional capacity corresponding to velocity plans.
	 */
	public static String FUNCTIONAL_CAPACITY = "Functional";

	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/** Sprint. */
	protected Sprint sprint;
	
	/** Person. */
	protected Person person;
	
	/** Capacity breakdown. */
	protected List<CapacityBreakdownElement> breakdown;
	
	/**
	 * Default constructor.
	 */
	public SprintPersonCapacity() {
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
	 * @return the breakdown
	 */
	public List<CapacityBreakdownElement> getBreakdown() {
		return breakdown;
	}

	/**
	 * @param breakdown the breakdown to set
	 */
	public void setBreakdown(List<CapacityBreakdownElement> breakdown) {
		this.breakdown = breakdown;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return PlanEntityUtils.toStringStandard(this, new String[] {
			"sprint", "person", "breakdown"});
	}
}
