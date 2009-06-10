package edu.wustl.cab2b.admin.searchdata.action;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.beans.AdminServiceMetadata;
import edu.wustl.cab2b.admin.bizlogic.ServiceInstanceBizLogic;
import edu.wustl.cab2b.admin.util.Cab2bConstants;

public class SaveServiceInstance extends BaseAction implements SessionAware, RequestAware {
	private static final long serialVersionUID = -4918500020071716455L;

	private Map session = null;

	private Map request = null;

	private String[] checkedServiceInstances = null;

	public void setRequest(final Map request) {
		this.request = request;
	}

	public Map getRequest() {
		return request;
	}

	@Override
	public String execute() {
		String userName = (String) getSession().get("UserName");

		Collection<AdminServiceMetadata> selectedServices = getSelectedServiceInstances();
		try {
			new ServiceInstanceBizLogic().saveServiceInstances(selectedServices, userName);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return Cab2bConstants.SUCCESS;
	}

	public Collection<AdminServiceMetadata> getSelectedServiceInstances() {
		List<AdminServiceMetadata> allServiceRecords = (List<AdminServiceMetadata>) getSession()
				.get("records");
		Collection<AdminServiceMetadata> selectedServiceMetaData = new HashSet<AdminServiceMetadata>();
		for (AdminServiceMetadata adminServiceMetaData : allServiceRecords) {
			String serviceURL = adminServiceMetaData.getHostingResearchCenter();
			for (String selectedServiceHost : getCheckedServiceInstances()) {
				if (serviceURL.compareTo(selectedServiceHost) == 0) {
					selectedServiceMetaData.add(adminServiceMetaData);
				}
			}
		}

		return selectedServiceMetaData;
	}

	public void setSession(final Map session) {
		this.session = session;
	}

	public Map getSession() {
		return session;
	}

	/**
	 * @return Returns the checkedServiceInstances.
	 */
	public String[] getCheckedServiceInstances() {
		return checkedServiceInstances;
	}

	/**
	 * @param checkedServiceInstances
	 *            The checkedServiceInstances to set.
	 */
	public void setCheckedServiceInstances(String[] checkedServiceInstances) {
		this.checkedServiceInstances = checkedServiceInstances;
	}

}
