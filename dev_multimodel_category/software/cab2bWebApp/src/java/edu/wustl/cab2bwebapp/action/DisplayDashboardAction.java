package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.cab2b.common.queryengine.ICab2bQuery;
import edu.wustl.cab2b.common.queryengine.querystatus.QueryStatus;
import edu.wustl.cab2b.common.queryengine.querystatus.URLStatus;
import edu.wustl.cab2b.common.user.UserInterface;
import edu.wustl.cab2b.server.queryengine.querystatus.QueryURLStatusOperations;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.QueryStatusDVO;

/**
 * @author chetan_pundhir
 * This action class is called to prepare for and forward to the application dash board.
 *
 */

public class DisplayDashboardAction extends Action {

    private static final Logger logger =
            edu.wustl.common.util.logger.Logger.getLogger(DisplayDashboardAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for setting 
     * parameters required at the dash board page.
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
        try {
            Collection<QueryStatusDVO> queryStatusDVOList = new ArrayList<QueryStatusDVO>();

            QueryURLStatusOperations opr = new QueryURLStatusOperations();
            Collection<QueryStatus> qsCollection =
                    opr.getAllQueryStatusByUser((UserInterface) request.getSession().getAttribute(Constants.USER));
            Iterator<QueryStatus> i = qsCollection.iterator();
            while (i.hasNext()) {
                QueryStatus qs = (QueryStatus) i.next();
                Set<URLStatus> urlStatus = qs.getUrlStatus();
                int failedHostingInstitutionCount = 0;
                for (URLStatus url : urlStatus) {
                    String status = url.getStatus();
                    if ("Failed".equalsIgnoreCase(status)) {
                        failedHostingInstitutionCount++;
                    }
                }
                QueryStatusDVO queryStatusDVO = new QueryStatusDVO();
                ICab2bQuery query = qs.getQuery();
                queryStatusDVO.setTitle(query.getName());
                queryStatusDVO.setType(query.getType());
                queryStatusDVO.setStatus(qs.getStatus());
                queryStatusDVO.setResultCount(qs.getResultCount());
                queryStatusDVO.setFailedHostingInstitutions(failedHostingInstitutionCount);
                queryStatusDVO.setExecutedOn(qs.getQueryStartTime());
                queryStatusDVO.setQueryConditions(qs.getQueryConditions());
                queryStatusDVO.setResultsFilePath(qs.getFileName());
                queryStatusDVOList.add(queryStatusDVO);
            }
            request.setAttribute("queryStatusDVOList", queryStatusDVOList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.displaydashboard.failure", e.getMessage());
            errors.add(Constants.FATAL_DISPLAY_DASHBOARD_FAILURE, error);
            saveErrors(request, errors);
            return mapping.findForward(Constants.FORWARD_FAILURE);
        }
        ActionForward actionForward = mapping.findForward(Constants.FORWARD_DASHBOARD);
        ActionForward actionRedirect = new ActionForward(actionForward.getName(), actionForward.getPath(), false);
        return actionRedirect;
    }
}