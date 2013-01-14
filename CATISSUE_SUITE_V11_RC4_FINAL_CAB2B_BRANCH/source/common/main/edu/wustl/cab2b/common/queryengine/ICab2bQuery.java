/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.cab2b.common.queryengine;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;

/**
 * @author chetan_patil
 *
 */
public interface ICab2bQuery extends IParameterizedQuery {
    // TODO need to be generalized for multiple outputs
    // TODO urls for intermodel categories
    List<String> getOutputUrls();

    void setOutputUrls(List<String> url);

    EntityInterface getOutputEntity();

    void setOutputEntity(EntityInterface outputEntity);
}
