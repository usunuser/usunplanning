package edu.usun.planning.activity;

import java.math.BigDecimal;

/**
 * Feature which has a specific estimate 
 * (should be nominated in the same units as velocity).
 * 
 * @author usun
 */
public class Feature extends Activity {

	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Total remaining estimate for this feature,
	 * should be in the same units as used for velocity calculation  
	 * (e.g. story points).
	 */
	protected BigDecimal remaningEstimate;
	
	/**
	 * Usually it is a customer identifier of the feature, 
	 * can be reference to the external system 
	 * or customer specific code of the feature, or just a feature code 
	 * used by management in their plans.
	 */
	protected String featureReference;
	
	/**
	 * Default constructor.
	 */
	public Feature() {
		super();
	}

	/**
	 * @return the remaningEstimate
	 */
	public BigDecimal getRemaningEstimate() {
		return remaningEstimate;
	}

	/**
	 * @param remaningEstimate the remaningEstimate to set
	 */
	public void setRemaningEstimate(BigDecimal remaningEstimate) {
		this.remaningEstimate = remaningEstimate;
	}

	/**
	 * @return the featureReference
	 */
	public String getFeatureReference() {
		return featureReference;
	}

	/**
	 * @param featureReference the featureReference to set
	 */
	public void setFeatureReference(String featureReference) {
		this.featureReference = featureReference;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer()
			.append("Feature{")
			.append("name=").append(this.getName()).append(',')
			.append("trackingReference=").append(this.getTrackingReference()).append(',')
			.append("featureReference=").append(this.getFeatureReference()).append(',')
			.append("stream=").append(this.getStream())
			.append('}').toString();
	}
}
