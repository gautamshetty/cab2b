/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.queryengine.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.wustl.cab2b.common.errorcodes.ErrorCodeConstants;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.CompoundQuery;
import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.MultiModelCategoryQuery;
import edu.wustl.cab2b.common.queryengine.ServiceGroup;
import edu.wustl.cab2b.common.queryengine.ServiceGroupItem;
import edu.wustl.cab2b.common.queryengine.result.ICategorialClassRecord;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.queryengine.querybuilders.dcql.constraints.GroupConstraint;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.cab2b.server.util.UtilityOperations;
import edu.wustl.common.querysuite.metadata.category.CategorialClass;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IRule;
import gov.nih.nci.cagrid.dcql.ForeignAssociation;
import gov.nih.nci.cagrid.dcql.Group;

/**
 * @author Deepak
 * @author pallavi_mistry
 */
public class QueryExecutorUtil {

    /**
     * It takes list of root ICRs for a URL and count the resulting number of Total spreadsheet records.
     * On the basis of max limit for total spreadsheet records, it returns 
     * true = if URL is feasible for conversion
     * or false = if its infeasible/or/ if num of records are 0 for failed URLs
     * A->B->D          A is the root categorial class and B,C,F are the child categorial classes for A
     *  ->C->G->H
     *  ->F
     * @param recordList : List of root ICR's
     * @param maxLimit
     * @return
     */
	
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(QueryExecutorUtil.class);

    public static boolean isURLFeasibleForConversion(List<ICategorialClassRecord> recordList, int maxLimit) {
        if (recordList != null) {
            if (recordList.size() > maxLimit) //if no of Root records only exceed the limit, return false 
                return false;

            int spreadSheetCount = getSpreadSheetRecordsCount(recordList);
            if (spreadSheetCount < maxLimit) {
                return true; // feasible URL good enough to be transformed
            } else {
                return false; // infeasible URL, can't invoke transformer
            }
        } else
            return false; //case of failed URL => num of records= 0, Transformer should not be invoked
    }

    /**
     * This method will take input as a CategoryRecord and just count the number of leaves of the tree.
     * It will return the total leaf count.
     * @param categorialChildRecord
     * @return
     */
    public static int countLeavesForEachChildRecord(ICategorialClassRecord categorialChildRecord)//B1,B2,C1,C2,F1,F2
    {
        int leavesCount = 0;
        Map<CategorialClass, List<ICategorialClassRecord>> categorialclassVsRecordsMap =
                categorialChildRecord.getChildrenCategorialClassRecords();

        if (categorialclassVsRecordsMap.isEmpty()) {
            return 1; //if leaf has come, return 1; D,H,F
        }

        for (CategorialClass categorialClass : categorialclassVsRecordsMap.keySet()) //G
        {
            for (ICategorialClassRecord childRecord : categorialclassVsRecordsMap.get(categorialClass)) {
                leavesCount = leavesCount + countLeavesForEachChildRecord(childRecord); //recursive call
            }
        }
        return leavesCount; //if its not a leaf, while returning in the called stack, return 0 for the interleaved nodes.
    }

    /**
     * It takes list of root ICRs for a URL and count the resulting number of Total spreadsheet records.
     * A->B->D          A is the root categorial class and B,C,F are the child categorial classes for A
     *  ->C->G->H
     *  ->F
     * @param recordList : List of root ICR's
     * @return currentSpreadsheetCount
     */
    public static int getSpreadSheetRecordsCount(List<ICategorialClassRecord> recordList) {
        int currentSpreadsheetCount = 0;
        if (recordList == null || recordList.size() == 0) {
            return currentSpreadsheetCount;
        }
        for (ICategorialClassRecord rootRecord : recordList) {
            int rootRecordCount = 1; // A1, A2,A3,A4
            Map<CategorialClass, List<ICategorialClassRecord>> categorialclassVsRecordsMap =
                    rootRecord.getChildrenCategorialClassRecords();
            for (CategorialClass categorialClass : categorialclassVsRecordsMap.keySet()) //B,C,F
            {
                int childLeavesCount = 0;
                for (ICategorialClassRecord categorialChildRecord : categorialclassVsRecordsMap
                    .get(categorialClass)) //B1,B2,C1,C2,F1,F2
                {
                    childLeavesCount = childLeavesCount + countLeavesForEachChildRecord(categorialChildRecord);
                }
                rootRecordCount = rootRecordCount * childLeavesCount;
            }
            currentSpreadsheetCount = currentSpreadsheetCount + rootRecordCount;
        }

        return currentSpreadsheetCount;
    }

    /**
     * This method returns the EntityGroupInterface for given query
     * @param query
     * @return EntityGroupInterfcae object of query
     */
    public static Collection<EntityGroupInterface> getEntityGroups(ICab2bQuery query) {
        Collection<EntityGroupInterface> entityGroups = new HashSet<EntityGroupInterface>();

        EntityInterface outputEntity = query.getOutputEntity();
        if (edu.wustl.cab2b.common.util.Utility.isCategory(outputEntity)) {
            entityGroups.add(UtilityOperations.getEntityGroupForCategory(outputEntity));
        } else if (edu.wustl.cab2b.common.util.Utility.isMultiModelCategory(outputEntity)) {
            MultiModelCategoryQuery mmcQuery = (MultiModelCategoryQuery) query;
            Collection<ICab2bQuery> subQueries = mmcQuery.getSubQueries();
            for (ICab2bQuery subQuery : subQueries) {
                outputEntity = subQuery.getOutputEntity();
                entityGroups.add(UtilityOperations.getEntityGroupForCategory(outputEntity));
            }
        } else {
            entityGroups.add(edu.wustl.cab2b.common.util.Utility.getEntityGroup(outputEntity));
        }
        return entityGroups;
    }

    /**
     * @param mmcQuery
     * @return
     */
    public static Map<String, List<String>> getAttributeNameValuesMap(MultiModelCategoryQuery mmcQuery) {
        Map<String, List<String>> attributeNameValuesMap = new HashMap<String, List<String>>();

        IExpression expression = mmcQuery.getConstraints().getExpression(1);
        try {
            IRule rule = (IRule) expression.getOperand(0);
            for (ICondition condition : rule) {
                attributeNameValuesMap.put(condition.getAttribute().getName(), condition.getValues());
            }
        } catch (ClassCastException e) {
            throw new IllegalStateException(
                    "The MultiModelCategoryQuery should contain only a single operand of type Rule", e);
        }

        return attributeNameValuesMap;
    }

    /**
     * This method returns the List of url's which are configured by user for that model group/entity group.
     * This method is required to set the url's in query
     * @param user
     * @param entityGroups
     * @return List<String>
     * @throws Exception
     */
    public static Map<EntityGroupInterface, List<String>> getUserConfiguredUrls(UserInterface user,
                                                                                String[] modelGroupNames) {
        Map<EntityGroupInterface, List<String>> entityGroupVsSelectedUrls =
                new HashMap<EntityGroupInterface, List<String>>();
        
    	logger.info("JJJ ************getUserConfiguredUrls*:");


        if (modelGroupNames != null && modelGroupNames.length != 0) {
            Collection<ServiceURLInterface> userConfiguredUrls = user.getServiceURLCollection();

            if (userConfiguredUrls == null || userConfiguredUrls.size() == 0) {
                if (!user.isAdmin()) {
                    entityGroupVsSelectedUrls =
                            getUserConfiguredUrls(new UserOperations().getAdmin(), modelGroupNames);
                	logger.info("JJJ ************admin*:");

                } else {
                    throw new RuntimeException(Constants.SERVICE_INSTANCES_NOT_CONFIGURED);
                }
            } else {
                for (ServiceURLInterface serviceUrl : userConfiguredUrls) {
                    String entityGroupName = serviceUrl.getEntityGroupName();
                	logger.info("JJJ ************userConfiguredUrls entityGroupName*:"+entityGroupName);

                    EntityGroupInterface entityGroup =
                            EntityCache.getInstance().getEntityGroupByName(entityGroupName);
                    List<String> urls = entityGroupVsSelectedUrls.get(entityGroup);
                    if (urls == null) {
                        urls = new ArrayList<String>();
                        entityGroupVsSelectedUrls.put(entityGroup, urls);
                    }
                    urls.add(serviceUrl.getUrlLocation());
                }
            }

        }
        return entityGroupVsSelectedUrls;
    }

    /**
     * @param query
     * @param proxy
     * @param user
     * @param modelGroupNames
     * @throws RuntimeException
     */
    public static void setOutputURLs(CompoundQuery query, GlobusCredential proxy, UserInterface user,
                                     String[] modelGroupNames) throws RuntimeException {
        Iterator<ICab2bQuery> subQueriesIterator = query.getSubQueries().iterator();
        while (subQueriesIterator.hasNext()) {

            ICab2bQuery subQuery = subQueriesIterator.next();
            if (subQuery instanceof CompoundQuery) {
                setOutputURLs((CompoundQuery) subQuery, proxy, user, modelGroupNames);
            } else {
                setOutputURLs(subQuery, proxy, user, modelGroupNames);
            }

            List<String> urls = subQuery.getOutputUrls();
            if (urls == null || urls.isEmpty()) {
                subQueriesIterator.remove();
            }
        }

        Collection<ICab2bQuery> subQueries = query.getSubQueries();
        List<String> urls = new ArrayList<String>();
        for (ICab2bQuery subQuery : subQueries) {
            List<String> subQueryURLs = subQuery.getOutputUrls();
            if (subQueryURLs != null) {
                urls.addAll(subQuery.getOutputUrls());
            }
        }

        if (urls.isEmpty()) {
            StringBuffer errorMessage = new StringBuffer("Incorrect or no database is configured.");
            throw new RuntimeException(errorMessage.toString(), ErrorCodeConstants.MG_008);
        }
        query.setOutputUrls(urls);
    }

    /**
     * @param queries
     * @param proxy
     * @param user
     * @param modelGroupNames
     * @throws RuntimeException
     */
    public static void setOutputURLs(ICab2bQuery query, GlobusCredential proxy, UserInterface user,
                                     String[] modelGroupNames) throws RuntimeException {

        Map<EntityGroupInterface, List<String>> entityGroupURLsMap = getUserConfiguredUrls(user, modelGroupNames);

        query.setOutputUrls(null);
        Collection<EntityGroupInterface> queryEntityGroups = getEntityGroups(query);
        
		List<String> newurls = new ArrayList<String>(1);      

        for (EntityGroupInterface queryEntityGroup : queryEntityGroups) {
            List<String> urls = entityGroupURLsMap.get(queryEntityGroup);
            int sgc=0;
            if(query.getServiceGroups().size() >0){
            	for(ServiceGroup group : query.getServiceGroups()){
            		for(ServiceGroupItem item : group.getItems()){
            			if(item.getTargetObject().toString().equals(query.getOutputEntity().getName())){
            				newurls.add(item.getServiceUrl().getUrlLocation());
            			}
            		}
            	}
            	query.setOutputUrls(newurls);

            } else
            if (urls != null && !urls.isEmpty()) {            	
                query.setOutputUrls(urls);
            }           
            	
        }
    }

}
