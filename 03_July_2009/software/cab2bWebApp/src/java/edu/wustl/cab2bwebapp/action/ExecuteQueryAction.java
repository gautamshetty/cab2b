/**
 *
 */

package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.globus.gsi.GlobusCredential;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.user.ServiceURLInterface;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2bwebapp.bizlogic.SavedQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.ExecuteQueryBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.QueryUpdateBizLogic;
import edu.wustl.cab2bwebapp.bizlogic.executequery.TransformedResultObjectWithContactInfo;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SavedQueryDVO;

/**
 * Action for executing query related operations.
 * @author deepak_shingan
 * @author chetan_patil
 * @author chetan_pundhir
 */
public class ExecuteQueryAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(ExecuteQueryAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code to retrieve the 
     * query results.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws IOException, ServletException {
        String actionForward = Constants.FORWARD_SEARCH_RESULTS;
        try {
            HttpSession session = request.getSession();
            if (request.getQueryString() == null) {
                String[] modelGroupNames = (String[]) request.getParameterValues(Constants.MODEL_GROUPS);
                SavedQueryBizLogic savedQueryBizLogic =
                        (SavedQueryBizLogic) session.getServletContext()
                            .getAttribute(Constants.SAVED_QUERY_BIZ_LOGIC);
                ICab2bQuery query =
                        savedQueryBizLogic.getQueryById(Long.parseLong(request.getParameter(Constants.QUERY_ID)));
                UserInterface user = (UserInterface) session.getAttribute(Constants.USER);

                String conditionstr = request.getParameter("conditionList");
                if (conditionstr == null) {
                    conditionstr = "";
                }

                //Update the IQuery according to parameterized conditions.
                String errorMessage =
                        new QueryUpdateBizLogic().setInputDataToQuery(conditionstr, query.getConstraints(), null,
                                                                      query);
                if (!errorMessage.equals("")) {
                    throw new Exception(errorMessage);
                } else {
                    Collection<ICab2bQuery> queries = new ArrayList<ICab2bQuery>();
                    queries.add(query);
                    GlobusCredential proxy = (GlobusCredential) session.getAttribute(Constants.GLOBUS_CREDENTIAL);
                    ExecuteQueryBizLogic executeQueryBizLogic =
                            new ExecuteQueryBizLogic(queries, proxy, user, modelGroupNames);
                    Map<ICab2bQuery, TransformedResultObjectWithContactInfo> searchResults =
                            executeQueryBizLogic.getSearchResults();

                    List<SavedQueryDVO> queryList = new ArrayList<SavedQueryDVO>();
                    SavedQueryDVO savedQuery = new SavedQueryDVO();
                    savedQuery.setName(query.getName());
                    savedQuery.setResultCount(searchResults.get(query).getResultForAllUrls().size());
                    queryList.add(savedQuery);

                    TransformedResultObjectWithContactInfo selectedQueryResult = searchResults.get(query);
                    List<String> urlsForSelectedQueries = query.getOutputUrls();
                    urlsForSelectedQueries.add(0, Constants.ALL_HOSTING_INSTITUTIONS);
                    Collection<ServiceURLInterface> failedURLS =
                            ExecuteQueryBizLogic.getFailedServiceUrls(selectedQueryResult.getFailedServiceUrl());
                    session.setAttribute(Constants.FAILED_SERVICES_COUNT, failedURLS != null ? failedURLS.size()
                            : 0);
                    session.setAttribute(Constants.FAILED_SERVICES, failedURLS);
                    session.setAttribute(Constants.SERVICE_INSTANCES, urlsForSelectedQueries);
                    session.setAttribute(Constants.SAVED_QUERIES, queryList);
                    session.setAttribute(Constants.SEARCH_RESULTS, searchResults);
                    session.setAttribute(Constants.SEARCH_RESULTS_VIEW, ExecuteQueryBizLogic
                        .getSearchResultsView(selectedQueryResult.getResultForAllUrls(), selectedQueryResult
                            .getAllowedAttributes()));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionMessage message =
                    new ActionMessage(
                            e.getMessage().equals(Constants.SERVICE_INSTANCES_NOT_CONFIGURED) ? "message.serviceinstancesnotconfigured"
                                    : "fatal.executequery.failure", e.getMessage());
            errors.add(Constants.FATAL_KYEWORD_SEARCH_FAILURE, message);
            saveErrors(request, errors);
            actionForward =
                    e.getMessage().equals(Constants.SERVICE_INSTANCES_NOT_CONFIGURED) ? Constants.FORWARD_HOME
                            : Constants.FORWARD_FAILURE;
        }
        return mapping.findForward(actionForward);
    }
}