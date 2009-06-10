/**
 * 
 */
package edu.wustl.cab2b.admin.searchdata.action;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import edu.wustl.cab2b.admin.action.BaseAction;
import edu.wustl.cab2b.admin.bizlogic.CreateCategoryBizLogic;
import edu.wustl.cab2b.admin.flex.CategoryDagPanel;
import edu.wustl.cab2b.admin.flex.DAGLink;
import edu.wustl.cab2b.admin.flex.DAGNode;
import static edu.wustl.cab2b.admin.util.AdminConstants.*;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.server.category.CategoryOperations;
import edu.wustl.cab2b.server.category.InputCategory;
import edu.wustl.cab2b.server.category.PersistCategory;
import edu.wustl.common.querysuite.metadata.category.Category;

/**
 * @author atul_jawale
 * @author lalit_chand
 */
public class CreateCategoryAttributeOrder extends BaseAction implements ServletRequestAware, ServletResponseAware,
        SessionAware {

    private static final long serialVersionUID = 5127133829185857760L;

    private Map session;

    private String formAction = null;
    
    private String title;

    private String description;

    private HttpServletResponse response;

    private HttpServletRequest servletRequest = null;

    public void setServletRequest(HttpServletRequest req) {
        this.servletRequest = req;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return servletRequest;
    }

    public void setSession(final Map session) {
        this.session = session;
    }

    /**
     * @return the session
     */
    public Map getSession() {
        return session;
    }

    /**
     * @return the action
     */
    public String getFormAction() {
        return formAction;
    }

    /**
     * @param action the action to set
     */
    public void setFormAction(String action) {
        this.formAction = action;
    }

    public String execute() throws RemoteException, IOException {
        Map<Long, List<String>> allAttributes = null;

        if (getFormAction() != null) {

            Map<String, String> unFormattedAttributeMap = (Map<String, String>) session.get("unFormattedAttributeMap");

            allAttributes = (Map<Long, List<String>>) getSession().get("attributeList");
            Map<String, String> allAttributeDisplayNameMap = new HashMap<String, String>();
            for (Long nodeId : allAttributes.keySet()) {
                List<String> nodeIDAttributeList = allAttributes.get(nodeId);
                for (String expIdAttributeName : nodeIDAttributeList) {
                    String[] temp = expIdAttributeName.split("\\.");
                    String attributeDisplayName = getRequest().getParameter(expIdAttributeName);
                    attributeDisplayName = attributeDisplayName == null ? expIdAttributeName
                            : attributeDisplayName;
                    allAttributeDisplayNameMap.put(temp[0] + "." + unFormattedAttributeMap.get(temp[1]),
                                                   attributeDisplayName);
                }
            }

            Set<DAGNode> dagNodeSet = null;
            Set<DAGLink> dagLinkSet = null;
            if ((session.get(CATEGORY_INSTANCE) !=null)) {
                CategoryDagPanel categoryDagPanel = (CategoryDagPanel) session.get(CATEGORY_INSTANCE);
                dagNodeSet = categoryDagPanel.getDagNodeSet();
                dagLinkSet = categoryDagPanel.getDagPathSet();
            }

            CreateCategoryBizLogic bizlogic = new CreateCategoryBizLogic(dagNodeSet, dagLinkSet,
                    allAttributeDisplayNameMap);
            InputCategory inputCategory = bizlogic.getInputCategory(getTitle(), getDescription());
            try {
                Category category = new PersistCategory().persistCategory(inputCategory, null);
                new CategoryOperations().saveCategory(category);
            } catch (Exception e) {
                e.printStackTrace();
                getRequest().setAttribute("exceptionMessage", e.getMessage());
                return FAILURE;

            } finally {
                getSession().remove("attributeList");
                getSession().remove("pathMap");
            }

            return PASS;
        } else {

            allAttributes = new HashMap<Long, List<String>>();

            Map<String, String> unFormattedAttributeMap = new HashMap<String, String>();
            Set<DAGNode> setOfNodes = null;
            if ((session.get(CATEGORY_INSTANCE) != null)) {
                CategoryDagPanel categoryDagPanel = (CategoryDagPanel) session.get(CATEGORY_INSTANCE);
                setOfNodes = categoryDagPanel.getDagNodeSet();
            }
            for (DAGNode dagNode : setOfNodes) {
                long nodeId = dagNode.getNodeId();
                List<String> attributes = new ArrayList<String>();
                List<String> attibutesList = dagNode.getAttributeList();
                for (String attribute : attibutesList) {
                    String[] attributeName = attribute.split(":");
                    attributes.add(nodeId + "." + CommonUtils.getFormattedString(attributeName[0]));
                    unFormattedAttributeMap.put(CommonUtils.getFormattedString(attributeName[0]), attributeName[0]);
                }
                allAttributes.put(nodeId, attributes);
            }
            getSession().put("unFormattedAttributeMap", unFormattedAttributeMap);
            getSession().put("attributeList", allAttributes);
            return SUCCESS;
        }

    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}