package edu.usun.planning.release;

import java.util.Calendar;

import edu.usun.planning.PlanEntity;
import edu.usun.planning.PlanEntityUtils;

/**
 * Release drop information (e.g. corresponds to JIRA release entity, 
 * i.e. a single drop to release management team or customer, 
 * not necessarily a go-live release/stream/project).
 * 
 * @author usun
 */
public class Release extends PlanEntity {
	
	/**
	 * For serialization format.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Project prefix, e.g. "usunplanning".
	 * Not the same as project tracking reference.
	 */
	protected String projectPrefix;
	
	/**
	 * Release description. Optional.
	 */
	protected String description;
	
	/**
	 * Project name/code in tracking system.
	 * To be used mostly for integration purposes.
	 */
	protected String projectTrackingReference;
	
	/**
	 * Date of delivery to integration team / branches.
	 * Pre-release stage.
	 */
	protected Calendar deliveryToIntegration;
	
	/**
	 * Date of delivery to customer.
	 */
	protected Calendar deliveryToCustomer;
	
	/**
	 * Default constructor.
	 */
	public Release() {
		super();
	}

	/**
	 * @param name The release name/version to set.
	 * @param projectPrefix The projectPrefix to set.
	 * @param description The description to set.
	 * @param projectTrackingReference The jiraProject to set.
	 * @param deliveryToIntegration The deliveryToIntegration to set.
	 * @param deliveryToCustomer The deliveryToCustomer to set.
	 */
	public Release(String name, String projectPrefix, String description, String projectTrackingReference,
		Calendar deliveryToIntegration, Calendar deliveryToCustomer) {
		super();
		this.name = name;
		this.projectPrefix = projectPrefix;
		this.description = description;
		this.projectTrackingReference = projectTrackingReference;
		this.deliveryToIntegration = deliveryToIntegration;
		this.deliveryToCustomer = deliveryToCustomer;
	}

	/**
	 * @return the projectPrefix
	 */
	public String getProjectPrefix() {
		return projectPrefix;
	}

	/**
	 * @param projectPrefix the projectPrefix to set
	 */
	public void setProjectPrefix(String projectPrefix) {
		this.projectPrefix = projectPrefix;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the projectTrackingReference
	 */
	public String getProjectTrackingReference() {
		return projectTrackingReference;
	}

	/**
	 * @param projectTrackingReference the projectTrackingReference to set
	 */
	public void setProjectTrackingReference(String projectTrackingReference) {
		this.projectTrackingReference = projectTrackingReference;
	}

	/**
	 * @return the deliveryToIntegration
	 */
	public Calendar getDeliveryToIntegration() {
		return deliveryToIntegration;
	}

	/**
	 * @param deliveryToIntegration the deliveryToIntegration to set
	 */
	public void setDeliveryToIntegration(Calendar deliveryToIntegration) {
		this.deliveryToIntegration = deliveryToIntegration;
	}

	/**
	 * @return the deliveryToCustomer
	 */
	public Calendar getDeliveryToCustomer() {
		return deliveryToCustomer;
	}

	/**
	 * @param deliveryToCustomer the deliveryToCustomer to set
	 */
	public void setDeliveryToCustomer(Calendar deliveryToCustomer) {
		this.deliveryToCustomer = deliveryToCustomer;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return PlanEntityUtils.toStringStandard(this, new String[] {
			"name", "projectPrefix", "description", "projectTrackingReference", 
			"deliveryToIntegration", "deliveryToCustomer"});
	}
}
