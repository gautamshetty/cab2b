/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.server.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.common.authentication.util.AuthenticationUtility;
import edu.wustl.cab2b.common.exception.RuntimeException;
import edu.wustl.cab2b.common.queryengine.result.IRecord;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.common.util.Utility;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.cab2b.server.category.CategoryCache;
import edu.wustl.cab2b.server.datalist.DataListOperationsController;
import edu.wustl.cab2b.server.path.PathFinder;
import edu.wustl.cab2b.server.user.UserOperations;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.querysuite.metadata.path.CuratedPath;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.global.Constants;

/**
 * Operations class for utility bean
 * @author chandrakant_talele
 */
public class UtilityOperations extends DefaultBizLogic {
    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(UtilityOperations.class);

    /**
     * Hibernate DAO Type to use.
     */
    private static int daoType = Constants.HIBERNATE_DAO;

    /**
     * @param cab2bObject
     */
    public void insert(Object cab2bObject) {
        try {
            insert(cab2bObject, daoType);
            logger.debug(cab2bObject.getClass().getName() + " inserted Successfully ");
        } catch (BizLogicException e) {
            throw (new RuntimeException(e.getMessage(), e));
        } catch (UserNotAuthorizedException e) {
            throw (new RuntimeException(e.getMessage(), e));
        }
    }

    /**
     * @param cab2bObject
     */
    public void update(Object cab2bObject) {
        try {
            update(cab2bObject, daoType);
            logger.debug(cab2bObject.getClass().getName() + " inserted Successfully ");
        } catch (BizLogicException e) {
            logger.debug(Utility.getStackTrace(e));
            throw (new RuntimeException(e.getMessage(), e));
        } catch (UserNotAuthorizedException e) {
            logger.debug(Utility.getStackTrace(e));
            throw (new RuntimeException(e.getMessage(), e));
        }
    }

    /**
     * This method returns the list of tree set containing the unique record values for a given entity identifier.
     * Tree set stores the values in sorted order. 
     * @param entityId entityId
     * @return List of TreeSet
     */
    public List<TreeSet<Comparable<?>>> getUniqueRecordValues(Long entityId) {
        //getting record list
        List<IRecord> entityRecordList = new DataListOperationsController().getEntityRecords(entityId);
        if (entityRecordList == null || entityRecordList.size() == 0) {
            return null;
        }
        IRecord rec = entityRecordList.get(0);
        //initializing column value set
        List<TreeSet<Comparable<?>>> entityRecordValues = initEntityRecordValues(rec.getAttributes().size());
        for (IRecord entityRecord : entityRecordList) {
            process(entityRecord, entityRecordValues);
        }
        return entityRecordValues;
    }

    private List<TreeSet<Comparable<?>>> initEntityRecordValues(int size) {
        List<TreeSet<Comparable<?>>> list = new ArrayList<TreeSet<Comparable<?>>>(size);
        for (int i = 0; i < size; i++) {
            list.add(i, new TreeSet<Comparable<?>>());
        }
        return list;
    }

    private void process(IRecord record, List<TreeSet<Comparable<?>>> entityRecordValues) {
        int index = 0;
        List<AttributeInterface> attributeList = Utility.getAttributeList(record.getAttributes());
        for (AttributeInterface attribute : attributeList) {
            TreeSet<Comparable<?>> columnValues = entityRecordValues.get(index);
            Comparable<?> attributeValue = (Comparable<?>) record.getValueForAttribute(attribute);
            if (attributeValue != null) {
                columnValues.add(attributeValue);
            }
            index++;
        }
    }

    /**
     * Adds the curated path to cache
     * @param curatedPath path to be added to cache.
     * @param con 
     */
    public void addCuratedPathToCache(CuratedPath curatedPath, Connection con) {
        PathFinder.getInstance(con).addCuratedPath(curatedPath);
    }

    /**
     * This method returns the database identifier of the user, given the serialized DelegatedCredentialReference and
     * the grid type
     *
     * @param serializedDCR
     * @return user's database identifier
     */
    public static Long getLocalUserId(String serializedDCR) {
        String userName = AuthenticationUtility.getUsersGridId(serializedDCR);
        UserInterface user = new UserOperations().getUserByName(userName);
    
        Long userId = null;
        if (user != null) {
            userId = user.getUserId();
        }
        return userId;
    }

    public synchronized static void refreshCache() {
        logger.info("Refreshing cache initiated...");
        EntityCache.getInstance().refreshCache();

        Connection con = null;
        try {
            con = ConnectionUtil.getConnection();
            PathFinder.refreshCache(con, false);
        } finally {
            ConnectionUtil.close(con);
        }

        CategoryCache.getInstance().refreshCategoryCache();
        logger.info("Cache refreshing sucessfull");
    }
}
