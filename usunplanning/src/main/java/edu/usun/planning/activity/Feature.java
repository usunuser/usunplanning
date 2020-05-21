package edu.usun.planning.activity;

import java.math.BigDecimal;

/**
 * Feature which has a specific total estimate.
 * 
 * @author usun
 */
public class Feature extends Activity {

	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Total remaining story points for this feature.
	 */
	protected BigDecimal remaningStoryPoints;
	
	/**
	 * Refers to customer identifier of the feature (e.g. DAC number in trackspace).
	 */
	protected String customerReference;
	
	/**
	 * Default constructor.
	 */
	public Feature() {
		super();
	}


	/**
	 * @return the remaningStoryPoints
	 */
	public BigDecimal getRemaningStoryPoints() {
		return remaningStoryPoints;
	}



	/**
	 * @param remaningStoryPoints the remaningStoryPoints to set
	 */
	public void setRemaningStoryPoints(BigDecimal remaningStoryPoints) {
		this.remaningStoryPoints = remaningStoryPoints;
	}



	/**
	 * @return the customerReference
	 */
	public String getCustomerReference() {
		return customerReference;
	}

	/**
	 * @param customerReference the customerReference to set
	 */
	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}
}
