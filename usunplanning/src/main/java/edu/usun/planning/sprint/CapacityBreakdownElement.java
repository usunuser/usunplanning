package edu.usun.planning.sprint;

import java.math.BigDecimal;

import edu.usun.planning.PlanEntity;

/**
 * Capacity breakdown element/activity, e.g.:
 * SCRUM (10%): 0.55d
 * 
 * @author usun
 */
public class CapacityBreakdownElement extends PlanEntity {

	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Percentage of the total capacity this element takes.
	 */
	protected BigDecimal percentage;

	/**
	 * Capacity nominated in man-days.
	 */
	protected BigDecimal capacityManDays;
	
	/**
	 * Velocity in story points where applicable.
	 */
	protected BigDecimal velocity;
	
	/**
	 * Default constructor.
	 */
	public CapacityBreakdownElement() {
		super();
	}

	/**
	 * @return the percentage
	 */
	public BigDecimal getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the capacityManDays
	 */
	public BigDecimal getCapacityManDays() {
		return capacityManDays;
	}

	/**
	 * @param capacityManDays the capacityManDays to set
	 */
	public void setCapacityManDays(BigDecimal capacityManDays) {
		this.capacityManDays = capacityManDays;
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
		StringBuffer sb = new StringBuffer()
			.append("CapacityBreakdownElement{")
			.append("velocity=").append(this.getVelocity() == null ? "N/A" : 
				this.getVelocity().toPlainString()).append(',')
			.append("capacityManDays=").append(this.getCapacityManDays() == null ? "N/A" : 
				this.getCapacityManDays().toPlainString()).append(',')
			.append("percentage=").append(this.getPercentage() == null ? "N/A" : 
				this.getPercentage().toPlainString());
		return sb.append('}').toString();
	}
}
