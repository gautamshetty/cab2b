/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

/**
 *
 */

package edu.wustl.cab2bwebapp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.modelgroup.ModelGroupInterface;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.modelgroup.ModelGroupOperations;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2bwebapp.bizlogic.ApplicationBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.ModelGroupBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * @author gaurav_mehta
 *
 */
public class Utility {

    /**
     * This method returns the EntityGroupInterface for given query
     * @param query
     * @return EntityGroupInterfcae object of query
     */
    public static EntityGroupInterface getEntityGroup(ICab2bQuery query) {
        EntityInterface outputEntity = query.getOutputEntity();
        if (edu.wustl.cab2b.common.util.Utility.isCategory(outputEntity)) {
            CategoryOperations categoryOperations = new CategoryOperations();
            Category category = categoryOperations.getCategoryByEntityId(outputEntity.getId());
            outputEntity = EntityCache.getInstance().getEntityById(category.getRootClass().getDeEntityId());
        }

        return edu.wustl.cab2b.common.util.Utility.getEntityGroup(outputEntity);
    }

    /**
     * This method returns a set of the entity groups that collectively forms one of the the given model groups names.
     *
     * @param modelGroupNames
     * @return
     */
    public static Set<EntityGroupInterface> getEntityGroupsForModelGroups(String[] modelGroupNames) {
        ModelGroupBizLogic modelGroupBizLogic = new ModelGroupBizLogic();
        final Set<EntityGroupInterface> entityGroups = new HashSet<EntityGroupInterface>();
        for (String modelGroupName : modelGroupNames) {
            entityGroups.addAll(modelGroupBizLogic.getEntityGroupsForModel(modelGroupName));
        }
        return entityGroups;
    }

    /**
     * This method returns list of query names.
     * @param queries
     * @return Collection<String>
     */
    public static List<String> getQueryNameList(Collection<ICab2bQuery> queries) {
        List<String> queryNameList = null;
        if (queries != null) {
            queryNameList = new ArrayList<String>();
            for (ICab2bQuery query : queries) {
                queryNameList.add(query.getName());
            }
        }
        return queryNameList;
    }

    /**
     * Method to verify model groups.
     * @param modelGroupNames
     * @param globusCredential
     * @param savedQueryBizLogic
     * @return Collection<ICab2bQuery>
     */
    public static Collection<ICab2bQuery> verifyModelGroups(String[] modelGroupNames,
                                                            GlobusCredential globusCredential,
                                                            SavedQueryBizLogic savedQueryBizLogic) {
        if (modelGroupNames == null || modelGroupNames.length == 0) {
            modelGroupNames = getModelGroups(globusCredential);
        }
        Set<EntityGroupInterface> entityGroups = Utility.getEntityGroupsForModelGroups(modelGroupNames);
        Collection<ICab2bQuery> keywordSearches = savedQueryBizLogic.getKeywordQueries(entityGroups);
        return keywordSearches;
    }

    /**
     * Method to get all model groups.
     * @param globusCredential
     * @return String[]
     */
    public static String[] getModelGroups(GlobusCredential globusCredential) {

        ModelGroupOperations modelGroupOperations = new ModelGroupOperations();
        List<ModelGroupInterface> modelGroups = new ArrayList<ModelGroupInterface>();
        if (globusCredential == null) {
            modelGroups.addAll(modelGroupOperations.getAllNonSecuredModelGroups());
        } else {
            modelGroups.addAll(modelGroupOperations.getAllModelGroups());
        }
        String[] modelGroupNames = new String[modelGroups.size()];
        for (int i = 0; i < modelGroups.size(); i++) {
            modelGroupNames[i] = modelGroups.get(i).getModelGroupName();
        }
        return modelGroupNames;
    }

    /**
     * This method returns the List of url's which are configured by user for that model group/entity group.
     * This method is required to set the url's in query
     * @param user
     * @param entityGroups
     * @return List<String>
     * @throws Exception
     */
    public static List<String> getUserConfiguredUrls(UserInterface user, String[] modelGroupNames)
            throws Exception {
        List<String> selectedUrls = new ArrayList<String>();
        if (modelGroupNames != null && modelGroupNames.length != 0) {
            Collection<ServiceURLInterface> userConfiguredUrls = user.getServiceURLCollection();

            List<ServiceURLInterface> userSelectedModelGroupInstances = new ArrayList<ServiceURLInterface>();

            for (int i = 0; i < modelGroupNames.length; i++) {
                userSelectedModelGroupInstances.addAll(new ApplicationBizLogic()
                    .getApplicationInstances(user, modelGroupNames[i]));
            }
            userSelectedModelGroupInstances.retainAll(userConfiguredUrls);
            for (ServiceURLInterface serviceUrl : userSelectedModelGroupInstances) {
                selectedUrls.add(serviceUrl.getUrlLocation());
            }
            if (selectedUrls.size() == 0) {
                if (!user.isAdmin()) {
                    selectedUrls = getUserConfiguredUrls(new UserOperations().getAdmin(), modelGroupNames);
                } else {
                    throw new Exception(Constants.SERVICE_INSTANCES_NOT_CONFIGURED);
                }
            }
        }
        return selectedUrls;
    }
}