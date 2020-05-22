package edu.usun.planning.calendar;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.usun.planning.PlanEntity;

/**
 * Represents either capacity reduction (from public holiday or vacation taken on a given day) 
 * or increase (e.g. over-times).
 * 
 * @author usun
 */
public class CapacityOverride extends PlanEntity {

	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Affected day.
	 */
	protected Calendar date;
	
	/**
	 * 1 - means no reduction, normal working day (default value); 0 - means day-off; 
	 * greater than 1 means overtime hours in addition to normal work hours.
	 */
	protected BigDecimal capacityFactor = new BigDecimal(1);
	
	/**
	 * Default constructor.
	 */
	public CapacityOverride() {
		super();
	}

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
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
		return new StringBuffer()
			.append("CapacityOverride{")
			.append("date=").append(this.getDate() == null ? "N/A" : sdf.format(this.getDate().getTime())).append(',')
			.append("capacityFactor=").append(this.getCapacityFactor() == null ? "N/A" : this.getCapacityFactor().toPlainString())
			.append('}').toString();
	}
}
