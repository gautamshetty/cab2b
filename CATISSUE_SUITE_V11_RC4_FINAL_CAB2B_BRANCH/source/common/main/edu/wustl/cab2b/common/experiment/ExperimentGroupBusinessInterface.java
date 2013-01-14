/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.common.experiment;

import java.rmi.RemoteException;

import edu.wustl.cab2b.common.BusinessInterface;
import edu.wustl.cab2b.common.domain.ExperimentGroup;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

public interface ExperimentGroupBusinessInterface extends BusinessInterface {
	
	public ExperimentGroup addExperimentGroup(Object expGrp) throws BizLogicException, UserNotAuthorizedException, RemoteException;

    public ExperimentGroup addExperimentGroup(Long parentExperimentGroupId,ExperimentGroup experimentGroup , String dref ) throws BizLogicException, UserNotAuthorizedException, RemoteException, DAOException;

	public ExperimentGroup getExperimentGroup(Long identifier) throws DAOException, RemoteException;
	
}
