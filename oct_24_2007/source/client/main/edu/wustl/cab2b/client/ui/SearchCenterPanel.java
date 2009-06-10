package edu.wustl.cab2b.client.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.jdesktop.swingx.JXPanel;

import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;

/**
 * The center searchPanel from the main search dialog. The searchPanel uses a card layout to
 * manage all the cards. Each card is in turn a searchPanel corresponding to each tab
 * in th main search dialog.
 * 
 * @author mahesh_iyer
 * 
 */

public class SearchCenterPanel extends Cab2bPanel {
    private static final long serialVersionUID = 1L;

    /** An array of all the cards to be added to this searchPanel.*/
    public JXPanel[] m_arrCards = new Cab2bPanel[5];

    /** Identifier to identify the card corresponding to the advanced search searchPanel.*/
    public static final String m_strIdentifierChooseCateglbl = "Choose Search Category";

    /** Identifier to identify the card corresponding to the Add Limit searchPanel.*/
    public static final String m_strIdentifierAddLimitlbl = "Add Limit";

    /** Identifier to identify the card corresponding to the Define Search Results searchPanel.*/
    public static final String m_strDefineSearchResultslbl = "Define Search Results View";

    /** Identifier to identify the card corresponding to the View Search Results searchPanel.*/
    public static final String m_strViewSearchResultslbl = "View Search Results";

    /** Identifier to identify the card corresponding to the Data List searchPanel.*/
    public static final String m_strDataListlbl = "Data List";

    /** Index to indicate the currently selected index. Initialized to default value of 0.*/
    private int m_iCurrentlySelectedCard = 0;

    private AddLimitPanel addLimitPanel;

    private ChooseCategoryPanel chooseCategPanel;

    /**
     * HashMap of identifiers. This would be used by the navigation searchPanel to
     * bring up the appropriate card.
     */
    private Vector<String> m_vIdentifiers = new Vector<String>();

    /**
     * The method returns the number of cards currently selected
     * 
     * @return int The current card count.
     */
    public int getIdentifierCount() {
        return this.m_vIdentifiers.size();
    }

    /**
     * The method returns the index for the currently selected card.
     * 
     * @return int The index of the currently selected card.
     */
    public int getSelectedCardIndex() {
        return this.m_iCurrentlySelectedCard;
    }

    /**
     * The method returns the currently selected card.
     * 
     * @return JXPanel The currently selected card.
     */
    public JXPanel getSelectedCard() {

        return this.m_arrCards[this.getSelectedCardIndex()];
    }

    /**
     * The method sets the index of the currently selected card.
     * 
     * @param  iSelectedCard index of the card to be selected.
     */
    public void setSelectedCardIndex(int iSelectedCard) {
        this.m_iCurrentlySelectedCard = iSelectedCard;
    }

    /**
     * Constructor.
     */
    SearchCenterPanel() {
        initGUI();
        this.setBorder(new SearchDialogBorder());
    }

    /**
     * The method initializes the tabbed pane.
     */
    private void initGUI() {
        /*Set the card layout for the center searchPanel.*/
        this.setLayout(new CardLayout());

        /* First card initialization.*/
        chooseCategPanel = new ChooseCategoryPanel();
        this.add(chooseCategPanel, m_strIdentifierChooseCateglbl);
        chooseCategPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));

        /*
         * Add the identifier to the vector, where from it would be refrenced by
         * the navigation searchPanel.
         */
        this.m_vIdentifiers.add(m_strIdentifierChooseCateglbl);
        this.m_arrCards[0] = chooseCategPanel;

        /* Second card initialization.*/
        addLimitPanel = new AddLimitPanel();
        this.add(addLimitPanel, m_strIdentifierAddLimitlbl);
        this.m_vIdentifiers.add(m_strIdentifierAddLimitlbl);
        this.m_arrCards[1] = addLimitPanel;

        this.m_vIdentifiers.add(m_strDefineSearchResultslbl);
        this.m_vIdentifiers.add(m_strViewSearchResultslbl);
        this.m_vIdentifiers.add(m_strDataListlbl);
    }

    /**
     * The method returns the identifier associated with the given index.
     * 
     * @param index
     * @return
     */
    public String getIdentifier(int index) {
        return (String) this.m_vIdentifiers.elementAt(index);
    }

    public void reset() {
        addLimitPanel.resetPanel();
    }

    /**
     * get the choose category searchPanel instance 
     * @return the choose category searchPanel instance
     */
    public ChooseCategoryPanel getChooseCategoryPanel() {
        return chooseCategPanel;
    }

    /**
     * get the add limit searchPanel instance 
     * @return the add limit searchPanel instance
     */
    public AddLimitPanel getAddLimitPanel() {
        return addLimitPanel;
    }

    public void setChooseCategoryPanel(ChooseCategoryPanel chooseCategoryPanel) {
        this.chooseCategPanel = chooseCategoryPanel;
        this.add(chooseCategPanel, m_strIdentifierChooseCateglbl);
        this.m_vIdentifiers.add(m_strIdentifierChooseCateglbl);
        this.m_arrCards[0] = chooseCategPanel;
    }

    public void setAddLimitPanel(AddLimitPanel addLimitPanel) {
        this.addLimitPanel = addLimitPanel;
        this.add(addLimitPanel, m_strIdentifierAddLimitlbl);
        this.m_vIdentifiers.add(m_strIdentifierAddLimitlbl);
        this.m_arrCards[1] = addLimitPanel;
    }

}