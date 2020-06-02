package edu.usun.planning.release;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.usun.planning.PlanEntity;

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
	 * Not the same as JIRA project code.
	 */
	protected String projectPrefix;
	
	/**
	 * Release description. Optional.
	 */
	protected String description;
	
	/**
	 * Project name/code in JIRA.
	 * To be used mostly for integration purposes.
	 */
	protected String jiraProject;
	
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
	 * @param jiraProject The jiraProject to set.
	 * @param deliveryToIntegration The deliveryToIntegration to set.
	 * @param deliveryToCustomer The deliveryToCustomer to set.
	 */
	public Release(String name, String projectPrefix, String description, String jiraProject,
		Calendar deliveryToIntegration, Calendar deliveryToCustomer) {
		super();
		this.name = name;
		this.projectPrefix = projectPrefix;
		this.description = description;
		this.jiraProject = jiraProject;
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
	 * @return the jiraProject
	 */
	public String getJiraProject() {
		return jiraProject;
	}

	/**
	 * @param jiraProject the jiraProject to set
	 */
	public void setJiraProject(String jiraProject) {
		this.jiraProject = jiraProject;
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb = new StringBuffer()
			.append("Release{")
			.append("name=").append(this.getName()).append(',')
			.append("projectPrefix=").append(this.getProjectPrefix()).append(',')
			.append("description=").append(this.getDescription()).append(',')
			.append("jiraProject=").append(this.getJiraProject()).append(',')
			.append("deliveryToIntegration=").append(this.getDeliveryToIntegration() == null ? "N/A" : 
				sdf.format(this.getDeliveryToIntegration().getTime())).append(',')
			.append("deliveryToCustomer=").append(this.getDeliveryToCustomer() == null ? "N/A" : 
				sdf.format(this.getDeliveryToCustomer().getTime()));
		return sb.append('}').toString();
	}
}
