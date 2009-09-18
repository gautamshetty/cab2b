package edu.wustl.cab2bwebapp.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import obr.ClientUtility;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import per.edu.wustl.Resource;
import edu.wustl.cab2bwebapp.bizlogic.caNanoLab.DisplayResourceBizlogic;
import edu.wustl.cab2bwebapp.constants.Constants;
import edu.wustl.cab2bwebapp.dvo.SearchResultDVO;

public class AnnotationResultAction extends Action {

    private static final Logger logger = edu.wustl.common.util.logger.Logger.getLogger(AnnotationResultAction.class);

    /**
     * The execute method is called by the struts action flow and it calls the code for generating 
     * dynamic HTML for add limit page.
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
        HttpSession session = request.getSession();
        String actionForward = null;
        try {
            String token = request.getParameter("token");
            List<List<SearchResultDVO>> listSearch = null;
            String results[] = ClientUtility.getAnnotations(ClientUtility.getResources(), null, token);
            if (results != null) {
                listSearch = DisplayResourceBizlogic.getResult(results,session);
            }
            session.setAttribute(Constants.SEARCH_RESULTS_VIEW, listSearch);
            session.setAttribute("token", token);
            actionForward = Constants.DISPLAY_RESOURCE;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("fatal.addlimit.failure");
            errors.add(Constants.FATAL_ADD_LIMIT_FAILURE, error);
            saveErrors(request, errors);
            actionForward = Constants.FORWARD_FAILURE;
        }
        return mapping.findForward(actionForward);
    }
}