package edu.wustl.cab2b.common.user;


public interface ServiceURLInterface {

	public String getEntityGroupName();

	
	/**
	 * @return Returns the urlId.
	 */
	Long getUrlId();

	/**
	 * @return Returns the urlLocation.
	 */
	String getUrlLocation();

	/**
	 * @return Returns the isAdminDefined.
	 */
	boolean isAdminDefined();
}