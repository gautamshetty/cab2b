package edu.wustl.cab2b.client.ui.parameterizedQuery;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Date;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.SearchNavigationPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.main.AbstractTypePanel;
import edu.wustl.cab2b.client.ui.mainframe.stackbox.StackBoxMySearchQueriesPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.ejb.EjbNamesConstants;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineBusinessInterface;
import edu.wustl.cab2b.common.ejb.queryengine.QueryEngineHome;

/**
 * Panel situated at bottom side of ParameterizedQueryMainPanel  
 * @author deepak_shingan
 *
 */
public class ParameterizedQueryNavigationPanel extends Cab2bPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Cab2bButton orderViewButton;

    private Cab2bButton saveButton;

    private Cab2bButton cancelButton;

    private ParameterizedQueryMainPanel parameterizedQueryMainPanel;

    public ParameterizedQueryNavigationPanel(ParameterizedQueryMainPanel panel) {
        parameterizedQueryMainPanel = panel;
        iniGUI();
    }

    /**
     * Method to generate UI 
     */
    private void iniGUI() {
        this.setLayout(new RiverLayout(5, 10));
        this.setBackground(new Color(240, 240, 240));
        orderViewButton = new Cab2bButton("Order View");
        orderViewButton.addActionListener(new OrderViewButtonActionListener());
        orderViewButton.setPreferredSize(new Dimension(120, 22));
        saveButton = new Cab2bButton("Save");
        saveButton.addActionListener(new saveButtonActionListener());
        cancelButton = new Cab2bButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                parameterizedQueryMainPanel.getDialog().dispose();
            }
        });

        this.add(" right ", orderViewButton);
        this.add(" right ", saveButton);
        this.add(" right ", cancelButton);
        this.add("br ", new Cab2bLabel());
    }

    private class OrderViewButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            ParameterizedQueryOrderPanel panel = new ParameterizedQueryOrderPanel(parameterizedQueryMainPanel);
            parameterizedQueryMainPanel.getDialog().dispose();
            panel.showInDialog();
        }
    }

    private class saveButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            Cab2bPanel condtionPanel = parameterizedQueryMainPanel.getParameterConditionPanel().getConditionPanel();
            ParameterizedQueryDataModel parameterizedQueryDataModel = parameterizedQueryMainPanel.getParameterizedQueryDataModel();

            for (int index = 0; index < condtionPanel.getComponentCount(); index++) {
                if (condtionPanel.getComponent(index) instanceof AbstractTypePanel) {
                    AbstractTypePanel panel = (AbstractTypePanel) condtionPanel.getComponent(index);
                    int conditionStatus = panel.isConditionValid(parameterizedQueryMainPanel);
                    if (conditionStatus == 0) {
                        parameterizedQueryDataModel.addCondition(panel.getExpressionId(),
                                                                 panel.getCondition(index));
                    } else if (conditionStatus < 0) {
                        return;
                    }
                }
            }
            ParameterizedQueryInfoPanel parameterizedQueryInfoPanel = parameterizedQueryMainPanel.getInformationQueryPanel();
            parameterizedQueryDataModel.setQueryDescription(parameterizedQueryInfoPanel.getQueryDecription());

            if (!parameterizedQueryInfoPanel.getQueryName().equals(""))
                parameterizedQueryDataModel.setQueryName(parameterizedQueryInfoPanel.getQueryName());
            else {
                parameterizedQueryDataModel.setQueryName((new Date().toString()));
            }
            saveQuery();
        }

        private void saveQuery() {
            QueryEngineBusinessInterface queryEngineBusinessInterface = (QueryEngineBusinessInterface) CommonUtils.getBusinessInterface(
                                                                                                                                        EjbNamesConstants.QUERY_ENGINE_BEAN,
                                                                                                                                        QueryEngineHome.class,
                                                                                                                                        parameterizedQueryMainPanel);
            try {
                queryEngineBusinessInterface.saveQuery(parameterizedQueryMainPanel.getParameterizedQueryDataModel().getQuery());
                SearchNavigationPanel.messageLabel.setText("Query "
                        + parameterizedQueryMainPanel.getParameterizedQueryDataModel().getQuery().getName()
                        + " saved successfully.");
                StackBoxMySearchQueriesPanel.getInstance().updateMySearchQueryPanel();
                updateUI();
            } catch (RemoteException exception) {
                CommonUtils.handleException(exception, parameterizedQueryMainPanel, true, true, true, false);
            } finally {
                parameterizedQueryMainPanel.getDialog().dispose();
            }
        }
    }
}