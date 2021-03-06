/*L
 * Copyright Georgetown University, Washington University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

package edu.wustl.cab2b.client.ui.mainframe;

import static edu.wustl.cab2b.client.ui.util.ClientConstants.EXPT_TAB_PRESSED;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.EXPT_TAB_UNPRESSED;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.HOME_TAB_PRESSED;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.HOME_TAB_UNPRESSED;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SEARCH_TAB_PRESSED;
import static edu.wustl.cab2b.client.ui.util.ClientConstants.SEARCH_TAB_UNPRESSED;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;

import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.cache.UserCache;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.mainframe.showall.ShowAllPanel;
import edu.wustl.cab2b.client.ui.mysettings.MySettingPanel;
import edu.wustl.cab2b.client.ui.searchDataWizard.MainSearchPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;

/**
 * This class creates a glassPane over the icons and adds tab-buttons to the
 * panel
 * 
 * @author hrishikesh_rajpathak
 * 
 */
public class GlobalNavigationGlassPane extends JComponent implements ActionListener {
    Cab2bLabel loggedInUserLabel;

    private ClassLoader loader = this.getClass().getClassLoader();

    private URL[] tabsImagesUnPressed = { loader.getResource(HOME_TAB_UNPRESSED), loader.getResource(SEARCH_TAB_UNPRESSED), loader.getResource(EXPT_TAB_UNPRESSED) };

    private URL[] tabsImagesPressed = { loader.getResource(HOME_TAB_PRESSED), loader.getResource(SEARCH_TAB_PRESSED), loader.getResource(EXPT_TAB_PRESSED) };

    private JButton[] tabButtons = new Cab2bButton[3];

    private JXPanel tabsPanel;

    private MainFrame mainFrame;

    private JXFrame frame;

    private JLabel middleLabel;

    private JLabel rightLabel;

    private JButton lastSelectedTab;

    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * @param leftLabel
     * @param middleLabel
     * @param rightLabel
     * @param mainFrame
     * @param frame
     */
    public GlobalNavigationGlassPane(
            JLabel leftLabel,
            JLabel middleLabel,
            JLabel rightLabel,
            MainFrame mainFrame,
            JXFrame frame) {
        this.frame = mainFrame;
        this.mainFrame = mainFrame;
        this.middleLabel = middleLabel;
        this.rightLabel = rightLabel;
        initUI();
    }

    /**
     * @return Search Button
     */
    public JButton getSearchButton() {
        return tabButtons[1];
    }

    /**
     * Initializing UI
     */
    private void initUI() {
        middleLabel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        Cab2bPanel topMiddlePanel = new Cab2bPanel();
        topMiddlePanel.setPreferredSize(new Dimension(300, 40));
        topMiddlePanel.setMinimumSize(new Dimension(300, 40));
        topMiddlePanel.setMaximumSize(new Dimension(300, 40));
        topMiddlePanel.setOpaque(false);

        tabsPanel = new Cab2bPanel();
        tabsPanel.setPreferredSize(new Dimension(300, 30));
        tabsPanel.setMinimumSize(new Dimension(300, 30));
        tabsPanel.setMaximumSize(new Dimension(300, 30));
        tabsPanel.setOpaque(false);
        tabsPanel.setLayout(new HorizontalLayout(10));

        for (int i = 0; i < 3; i++) {
            ImageIcon icon = new ImageIcon(tabsImagesUnPressed[i]);
            tabButtons[i] = new Cab2bButton();
            tabButtons[i].setPreferredSize(new Dimension(85, 22));
            tabButtons[i].setIcon(icon);
            tabButtons[i].setBorder(null);
            tabButtons[i].addActionListener(this);
            tabsPanel.add(tabButtons[i]);
        }

        ImageIcon icon = new ImageIcon(tabsImagesPressed[0]);
        tabButtons[0].setIcon(icon);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        middleLabel.add(topMiddlePanel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        middleLabel.add(tabsPanel, gbc);

        rightLabel.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.weightx = 1.5;
        gbc.fill = GridBagConstraints.VERTICAL;
        rightLabel.add(new JLabel(""), gbc);

        Date date = new Date();
        String userName = UserCache.getInstance().getCurrentUser().getUserName();
        loggedInUserLabel = new Cab2bLabel(CommonUtils.getDisplayUserName(userName));
        loggedInUserLabel.setFont(new Font("Arial", Font.BOLD, 12));
        Cab2bLabel dateLabel = new Cab2bLabel(DateFormat.getDateInstance(DateFormat.LONG).format(date).toString());
        dateLabel.setForeground(Color.WHITE);
        Cab2bHyperlink logOutHyperLink = new Cab2bHyperlink();
        logOutHyperLink.setClickedColor(Color.WHITE);
        logOutHyperLink.setUnclickedColor(Color.WHITE);
        logOutHyperLink.setText("Logout");
        logOutHyperLink.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.logout();
            }
        });

        Cab2bHyperlink mySettingHyperlInk = new Cab2bHyperlink();
        mySettingHyperlInk.setClickedColor(Color.WHITE);
        mySettingHyperlInk.setUnclickedColor(Color.WHITE);
        mySettingHyperlInk.setText("MySettings");
        mySettingHyperlInk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MySettingPanel panel = new MySettingPanel(mainFrame);
            }
        });

        loggedInUserLabel.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        loggedInUserLabel.setForeground(Color.WHITE);

        JLabel line = new JLabel("|");
        line.setForeground(Color.WHITE);
        Cab2bPanel linkPanel = new Cab2bPanel();

        JLabel label = new JLabel(" ");
        linkPanel.add("br", label);
        linkPanel.add(loggedInUserLabel);
        // linkPanel.add("br ", label);
        linkPanel.add("br ", dateLabel);
        linkPanel.add("br ", logOutHyperLink);
        linkPanel.add("tab ", line);
        linkPanel.add("tab ", mySettingHyperlInk);
        linkPanel.setOpaque(false);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.VERTICAL;
        rightLabel.add(linkPanel, gbc);

        lastSelectedTab = tabButtons[0];
        this.repaint();
    }

    /**
     * @param e ActionEvent
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if (button == tabButtons[0]) {
            tabButtons[0].setIcon(new ImageIcon(tabsImagesPressed[0]));
            tabButtons[1].setIcon(new ImageIcon(tabsImagesUnPressed[1]));
            tabButtons[2].setIcon(new ImageIcon(tabsImagesUnPressed[2]));
            if (this.frame instanceof MainFrame) {
                MainFrame mainframePanel = (MainFrame) this.frame;
                mainframePanel.setHomeWelcomePanel();
            }
            lastSelectedTab = button;
        } else if (button == tabButtons[1]) {
            tabButtons[0].setIcon(new ImageIcon(tabsImagesUnPressed[0]));
            tabButtons[1].setIcon(new ImageIcon(tabsImagesPressed[1]));
            tabButtons[2].setIcon(new ImageIcon(tabsImagesUnPressed[2]));

            initializeMainSearchPanel();

            if (GlobalNavigationPanel.getMainSearchPanel() != null) {
                CommonUtils.launchSearchDataWizard();
            }

            MainSearchPanel.getDataList().clear();
            GlobalNavigationPanel.setMainSearchPanel(null);

            // Set the Home tab as pressed and Search tab as unpressed
            if (lastSelectedTab != null && lastSelectedTab == tabButtons[0]) {
                lastSelectedTab.setIcon(new ImageIcon(tabsImagesPressed[0]));
            } else if (lastSelectedTab != null && lastSelectedTab == tabButtons[2]) {
                lastSelectedTab.setIcon(new ImageIcon(tabsImagesPressed[2]));
            }
            tabButtons[1].setIcon(new ImageIcon(tabsImagesUnPressed[1]));
        } else if (button == tabButtons[2]) {
            setExperimentHomePanel();
        }
        this.updateUI();
        this.repaint();
    }

    /**
     * Method to set experiment home panel 
     */
    public void setExperimentHomePanel() {
        setExperimentTabSelected();
        MainFrame mainframePanel = (MainFrame) this.frame;
        mainframePanel.setExperimentHomePanel();
        lastSelectedTab = tabButtons[2];
    }

    /**
     * Sets experiment tab as selected 
     */
    public void setExperimentTabSelected() {
        tabButtons[0].setIcon(new ImageIcon(tabsImagesUnPressed[0]));
        tabButtons[1].setIcon(new ImageIcon(tabsImagesUnPressed[1]));
        tabButtons[2].setIcon(new ImageIcon(tabsImagesPressed[2]));
    }

    /**
     * Method to set ShowAllPanel(like ShowAllCategoryPanel, ShowAllQueryLinkPanel) on righthand side of homepage
     * @param panel 
     */
    public void setShowAllPanel(ShowAllPanel panel) {
        tabButtons[0].setIcon(new ImageIcon(tabsImagesPressed[0]));
        tabButtons[1].setIcon(new ImageIcon(tabsImagesUnPressed[1]));
        tabButtons[2].setIcon(new ImageIcon(tabsImagesUnPressed[2]));
        MainFrame mainframePanel = (MainFrame) this.frame;

        mainframePanel.setRightHandPanel(panel);
        lastSelectedTab = tabButtons[1];
    }

    /**
     * Method to set Search Panel in Search Wizard
     */
    public void initializeMainSearchPanel() {
        GlobalNavigationPanel.setMainSearchPanel(new MainSearchPanel());
        Dimension relDimension = CommonUtils.getRelativeDimension(MainFrame.getScreenDimension(), 0.90f, 0.85f);
        GlobalNavigationPanel.getMainSearchPanel().setPreferredSize(relDimension);
        GlobalNavigationPanel.getMainSearchPanel().setSize(relDimension);
    }
}
