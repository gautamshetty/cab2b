package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.gradient.BasicGradientPainter;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.wustl.cab2b.client.ui.controls.Cab2bButton;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.Cab2bTable;
import edu.wustl.cab2b.client.ui.controls.Cab2bTitledPanel;
import edu.wustl.cab2b.client.ui.main.IComponent;
import edu.wustl.cab2b.client.ui.main.ParseXMLFile;
import edu.wustl.cab2b.client.ui.main.SwingUIManager;
import edu.wustl.cab2b.client.ui.mainframe.NewWelcomePanel;
import edu.wustl.cab2b.client.ui.pagination.JPageElement;
import edu.wustl.cab2b.client.ui.pagination.JPagination;
import edu.wustl.cab2b.client.ui.pagination.NumericPager;
import edu.wustl.cab2b.client.ui.pagination.PageElement;
import edu.wustl.cab2b.client.ui.pagination.PageElementImpl;
import edu.wustl.cab2b.client.ui.query.ClientQueryBuilder;
import edu.wustl.cab2b.client.ui.query.IClientQueryBuilderInterface;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.exception.CheckedException;
import edu.wustl.cab2b.common.queryengine.Cab2bQueryObjectFactory;
import edu.wustl.cab2b.common.util.AttributeInterfaceComparator;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.common.util.EntityInterfaceComparator;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IRule;

/**
 * The class that contains commonalities required for displaying results from
 * the 'AddLimit' and 'choose category' section from the main search dialog.
 * 
 * @author mahesh_iyer/chetan_bh/gautam_shetty/Deepak_Shingan
 */
public class SearchResultPanel extends Cab2bPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	/** The pagination component to paginate the results of the search */
	private Cab2bPanel resultPanel = new Cab2bPanel();

	private Cab2bButton addLimitButtonTop;

	private Cab2bButton addLimitButtonBottom;

	private Cab2bButton editLimitButtonTop;

	private Cab2bButton editLimitButtonBottom;

	/**
	 * Saved reference to the content searchPanel that needs to be refreshed for
	 * appropriate events.
	 */
	protected ContentPanel contentPanel = null;

	/**
	 * Constructor
	 * 
	 * @param addLimitPanel
	 *            Reference to the parent content searchPanel that needs
	 *            refreshing.
	 * 
	 * @param result
	 *            The collectiond of entities.
	 */
	public SearchResultPanel(ContentPanel addLimitPanel, Set<EntityInterface> result) {
		initGUI(addLimitPanel, result);
	}

	/**
	 * Method initializes the searchPanel by appropriately laying out child
	 * components.
	 * 
	 * @param addLimitPanel
	 *            Reference to the parent content searchPanel that needs
	 *            refreshing.
	 * 
	 * @param result
	 *            The collectiond of entities.
	 * 
	 */
	private void initGUI(final ContentPanel addLimitPanel, Set<EntityInterface> resultSet) {
		this.setLayout(new RiverLayout());
		this.contentPanel = addLimitPanel;
		if (contentPanel instanceof AddLimitPanel) {
			((AddLimitPanel) contentPanel).setSearchResultPanel(this);
		}
		Vector<PageElement> pageElementCollection = new Vector<PageElement>();
		List<EntityInterface> resultList = new ArrayList<EntityInterface>(resultSet);
		Collections.sort(resultList, new EntityInterfaceComparator());
		for (EntityInterface entity : resultList) {
			// set the proper class name
			String className = edu.wustl.cab2b.common.util.Utility.getDisplayName(entity);
			String strDescription = entity.getDescription();

			// Create an instance of the PageElement. Initialize with the
			// appropriate data
			PageElement pageElement = new PageElementImpl();
			pageElement.setDisplayName(className);
			pageElement.setDescription(strDescription);
			pageElement.setUserObject(entity);
			pageElementCollection.add(pageElement);
		}

		NumericPager numPager = new NumericPager(pageElementCollection, getPageSize());
		/* Initalize the pagination component. */
		JPagination resultsPage = new JPagination(pageElementCollection, numPager, this, true);
		resultsPage.setSelectableEnabled(false);
		resultsPage.setGroupActionEnabled(false);
		resultsPage.addPageElementActionListener(this);
		resultPanel.add("hfill vfill ", resultsPage);

		JXTitledPanel titledSearchResultsPanel = displaySearchSummary(resultList.size());
		titledSearchResultsPanel.setContentContainer(resultPanel);
		this.add("hfill vfill", titledSearchResultsPanel);
	}

	/**
	 * Initiliasing/Adding Add Limit buttons
	 * 
	 * @param panelsToAdd
	 * @param entity
	 */
	private void initializeAddLimitButtons(final JXPanel[] panelsToAdd, final EntityInterface entity) {
		addLimitButtonTop = new Cab2bButton("Add Limit");
		addLimitButtonTop.setPreferredSize(new Dimension(95, 22));
		addLimitButtonTop.addActionListener(new AddLimitButtonListner(panelsToAdd, entity));
	}

	/**
	 * Initiliasing/Adding EditLimit buttons
	 * 
	 * @param panelsToAdd
	 * @param expression
	 */
	private void initializeEditLimitButtons(final JXPanel[] panelsToAdd,
			final IExpression expression) {
		editLimitButtonTop = new Cab2bButton("Edit Limit");
		editLimitButtonTop.addActionListener(new EditLimitButtonListner(panelsToAdd, expression));
		editLimitButtonTop.setPreferredSize(new Dimension(95, 22));
	}

	/**
	 * Sets result panel
	 * 
	 * @param resulPanel
	 */
	public void setResultPanel(Cab2bPanel resulPanel) {
		resultPanel.removeAll();
		resultPanel.add("hfill vfill ", resulPanel);
	}

	/**
	 * Removing result panel
	 */
	public void removeResultPanel() {
		resultPanel.removeAll();
	}

	/**
	 * Method to create AddLimitUI
	 * 
	 * @param entity
	 */
	protected JXPanel[] getAddLimitPanels(final EntityInterface entity) {
		final JXPanel[] componentPanel = getAddLimitComponentPanels(entity);
		Cab2bHyperlink detailsHyperlink = new Cab2bHyperlink();
		detailsHyperlink.setText("CDE Details");
		initializeAddLimitButtons(componentPanel, entity);
		detailsHyperlink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				Vector<String> headers = new Vector<String>();
				headers.add("Attribute Name");
				headers.add("Public ID");
				headers.add("Concept Codes");
				headers.add("Description");
				Object[] header = headers.toArray();
				Collection<AttributeInterface> attributeCollection = entity
						.getAttributeCollection();
				List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>(
						attributeCollection);
				Object[][] data = new Object[attributeCollection.size()][4];
				Collections.sort(attributeList, new AttributeInterfaceComparator());

				int i = 0;
				for (AttributeInterface attrInterface : attributeList) {
					data[i][0] = attrInterface.getName();
					data[i][1] = attrInterface.getPublicId();
					Collection<SemanticPropertyInterface> semanticPropColl = attrInterface
							.getSemanticPropertyCollection();
					String conceptCodeList = new String();
					for (SemanticPropertyInterface semProp : semanticPropColl) {
						if (conceptCodeList.equals("")) {
							conceptCodeList = semProp.getConceptCode();
						} else {
							conceptCodeList = conceptCodeList + ", " + semProp.getConceptCode();
						}
					}

					data[i][2] = conceptCodeList;
					data[i][3] = attrInterface.getDescription();
					i++;
				}
				Cab2bTable cab2bTable = new Cab2bTable(false, data, header);
				cab2bTable.setBorder(null);
				cab2bTable.setShowGrid(false);
				JScrollPane pane = new JScrollPane(cab2bTable,
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				pane.setBorder(null);
				WindowUtilities.showInDialog(NewWelcomePanel.mainFrame, pane, "CDE Details",
						Constants.WIZARD_SIZE2_DIMENSION, true, false);

			}
		});
		JXPanel[] finalPanelToadd = initializePanelsForAddConstraints(componentPanel);
		finalPanelToadd[0].add(addLimitButtonTop, BorderLayout.WEST);
		finalPanelToadd[0].add(detailsHyperlink, BorderLayout.EAST);

		return finalPanelToadd;

	}

	/**
	 * The action listener for the individual page elements.
	 * 
	 * @param actionEvent
	 *            The event that contains details of the click on the individual
	 *            page elements.
	 * 
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		Cab2bHyperlink<JPageElement> link = (Cab2bHyperlink<JPageElement>) (actionEvent.getSource());
		JPageElement jPageElement = link.getUserObject();
		jPageElement.resetLabel();

		JPagination pagination = jPageElement.getPagination();
		JPageElement selectedPageElement = pagination.getSelectedJPageElement();
		if (selectedPageElement != null) {
			selectedPageElement.resetHyperLink();
		}
		pagination.setSelectedJPageElement(jPageElement);

		/* This is the EntityInterface instance. */
		PageElement pageElement = jPageElement.getPageElement();
		final EntityInterface entity = (EntityInterface) pageElement.getUserObject();
		final JXPanel[] panelsToAdd = getAddLimitPanels(entity);

		if (getAddLimitComponentPanels(entity) != null) {
			// pass the appropriate class name for display
			performAction(panelsToAdd, edu.wustl.cab2b.common.util.Utility.getDisplayName(entity));
		}

		updateUI();
	}

	/**
	 * Get panels array to be displayed in add limit searchPanel
	 * 
	 * @param entity
	 * @return
	 */
	public JXPanel[] getAddLimitComponentPanels(final EntityInterface entity) {
		final Collection<AttributeInterface> attributeCollection = entity.getAttributeCollection();
		ParseXMLFile parseFile = null;
		try {
			parseFile = ParseXMLFile.getInstance();
		} catch (CheckedException ce) {
			CommonUtils.handleException(ce, this, true, true, false, false);
		}

		if (attributeCollection != null) {
			List<AttributeInterface> attributeList = new ArrayList<AttributeInterface>(
					attributeCollection);
			Collections.sort(attributeList, new AttributeInterfaceComparator());
			final JXPanel[] componentPanels = new Cab2bPanel[attributeList.size()];
			try {
				int i = 0;

				Dimension maxLabelDimension = CommonUtils.getMaximumLabelDimension(attributeList);
				for (AttributeInterface attribute : attributeList) {
					componentPanels[i++] = (JXPanel) SwingUIManager.generateUIPanel(parseFile,
							attribute, maxLabelDimension);
				}
			} catch (CheckedException e) {
				CommonUtils.handleException(e, this, true, true, false, false);
				// JXErrorDialog.showDialog(this,
				// ErrorCodeHandler.getErrorMessage(e.getErrorCode()), e);
			}
			return componentPanels;
		}
		return null;
	}

	/**
	 * Get panels array to be displayed in add limit searchPanel
	 * 
	 * @param entity
	 * @return
	 */
	public JXPanel[] getEditLimitPanels(final IExpression expression) {
		/* This is the EntityInterface instance. */
		final EntityInterface entity = expression.getConstraintEntity()
				.getDynamicExtensionsEntity();
		final JXPanel[] componentPanel = getAddLimitComponentPanels(entity);
		Cab2bHyperlink detailsHyperlinkEditPanel = new Cab2bHyperlink();
		detailsHyperlinkEditPanel.setText("Details");

		initializeEditLimitButtons(componentPanel, expression);
		JXPanel[] finalPanelToadd = initializePanelsForAddConstraints(componentPanel);
		finalPanelToadd[0].add(editLimitButtonTop, BorderLayout.WEST);
		finalPanelToadd[0].add(detailsHyperlinkEditPanel, BorderLayout.WEST);
		return finalPanelToadd;
	}

	public JXPanel[] initializePanelsForAddConstraints(JXPanel[] componentPanel) {
		Cab2bPanel cab2bPanel = new Cab2bPanel(new RiverLayout(5, 0));
		JXPanel[] finalPanelsToAdd = new Cab2bPanel[2];

		for (int j = 0; j < componentPanel.length; j++) {
			cab2bPanel.add("br", componentPanel[j]);
		}
		JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.getViewport().setBackground(Color.WHITE);
		pane.getViewport().add(cab2bPanel);
		pane.getViewport().setBorder(null);
		pane.setBorder(null);

		finalPanelsToAdd[0] = new Cab2bPanel(new RiverLayout(5, 5));
		finalPanelsToAdd[1] = new Cab2bPanel();
		finalPanelsToAdd[1].add("hfill vfill ", pane);

		return finalPanelsToAdd;
	}

	/**
	 * Method to handle 'Add Limit' button click event
	 */
	public void performAddLimitAction(JXPanel[] componentPanel, EntityInterface entity) {
		MainSearchPanel mainSearchPanel = (MainSearchPanel) ((JXPanel) contentPanel).getParent()
				.getParent();
		final Collection collection = entity.getAttributeCollection();
		final int size = collection.size();
		List<AttributeInterface> attributes = new ArrayList<AttributeInterface>(size);
		List<String> conditions = new ArrayList<String>(size);
		List<List<String>> values = new ArrayList<List<String>>();

		// Don't consider searchPanel first and searchPanel last for getting
		// attribute
		// values
		// because first and last panels are Add Limit button panels
		// for (int j = 1; j < componentPanel.length - 1; j++) {
		for (int j = 0; j < componentPanel.length; j++) {
			IComponent panel = (IComponent) componentPanel[j];
			String conditionString = panel.getCondition();

			// Check if condition is set for this searchPanel
			AttributeInterface attribute = getAttribute(collection, panel.getAttributeName());
			ArrayList<String> conditionValues = panel.getValues();
			if (0 == conditionString.compareToIgnoreCase("Between")
					&& (conditionValues.size() == 1)) {
				JOptionPane.showInternalMessageDialog((this.contentPanel).getParent().getParent()
						.getParent(), "Please enter both the values for between operator.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if ((conditionString.equals("Is Null")) || conditionString.equals("Is Not Null")
					|| (conditionValues.size() != 0)) {
				attributes.add(attribute);
				conditions.add(conditionString);
				values.add(conditionValues);
			}
		}

		if (attributes.size() == 0) {
			JOptionPane.showMessageDialog((this.contentPanel).getParent().getParent().getParent(),
					"Please add condition(s) before proceeding", "Add Limit Warning",
					JOptionPane.WARNING_MESSAGE);
		} else {
			if (mainSearchPanel.getQueryObject() == null) {
				IClientQueryBuilderInterface query = new ClientQueryBuilder();
				mainSearchPanel.setQueryObject(query);
				mainSearchPanel.getCenterPanel().getAddLimitPanel().setQueryObject(query);
			}

			IExpressionId expressionId = mainSearchPanel.getQueryObject().addRule(attributes,
					conditions, values);
			if (expressionId == null) {
				JOptionPane.showMessageDialog(mainSearchPanel.getParent(),
						"This rule cannot be added as it is not associated with the added rules.",
						"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				mainSearchPanel.getCenterPanel().getAddLimitPanel().refreshBottomCenterPanel(
						expressionId);
			}
		}
	}

	/**
	 * returns Attribute Interface for the name from the collection parameter
	 * 
	 * @param collection
	 * @param attributeName
	 * @return
	 */
	private AttributeInterface getAttribute(Collection collection, String attributeName) {
		AttributeInterface attribute = null;
		Iterator iterator = collection.iterator();

		while (iterator.hasNext()) {
			attribute = (AttributeInterface) iterator.next();
			if (attributeName.trim().equals(attribute.getName().trim())) {
				break;
			}
		}

		return attribute;
	}

	/**
	 * Method to perform edit limit action
	 */
	public void performEditLimitAction(JXPanel[] componentPanel, IExpression expression) {
		final Collection collection = expression.getConstraintEntity().getDynamicExtensionsEntity()
				.getAttributeCollection();
		List<ICondition> conditionList = new ArrayList<ICondition>();
		for (int j = 0; j < componentPanel.length; j++) {
			IComponent panel = (IComponent) componentPanel[j];
			String conditionString = panel.getCondition();

			AttributeInterface attribute = getAttribute(collection, panel.getAttributeName());
			ArrayList<String> values = panel.getValues();
			if (0 == conditionString.compareToIgnoreCase("Between") && (values.size() == 1)) {
				JOptionPane.showInternalMessageDialog((this.contentPanel).getParent().getParent()
						.getParent(), "Please enter both the values for between operator.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if ((conditionString.equals("Is Null")) || conditionString.equals("Is Not Null")
					|| (values.size() != 0)) {

				ICondition condition = Cab2bQueryObjectFactory.createCondition();
				condition.setAttribute(attribute);
				condition.setRelationalOperator(edu.wustl.cab2b.client.ui.query.Utility
						.getRelationalOperator(conditionString));
				for (int i = 0; i < values.size(); i++) {
					condition.addValue(values.get(i));
				}
				conditionList.add(condition);
			}
		}
		if (conditionList.size() != 0) {
			IRule rule = (IRule) expression.getOperand(0);
			rule.removeAllConditions();
			for (int i = 0; i < conditionList.size(); i++) {
				rule.addCondition(conditionList.get(i));
			}
		} else {
			MainSearchPanel mainSearchPanel = (MainSearchPanel) ((JXPanel) contentPanel)
					.getParent().getParent();
			JOptionPane.showInternalMessageDialog(mainSearchPanel.getParent(),
					"This rule cannot be added as it is not associated with the added rules.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * This method generates the search summary searchPanel
	 * 
	 * @param numberOfResults
	 *            number of results obtained
	 * @return summary searchPanel
	 */
	public JXTitledPanel displaySearchSummary(int numberOfResults) {
		String message = (numberOfResults == 0) ? "No result found." : "Search Results :- "
				+ "Total results ( " + numberOfResults + " )";
		JXTitledPanel titledSearchResultsPanel = new Cab2bTitledPanel(message);
		GradientPaint gp = new GradientPaint(new Point2D.Double(.05d, 0), new Color(185, 211, 238),
				new Point2D.Double(.95d, 0), Color.WHITE);
		titledSearchResultsPanel.setTitlePainter(new BasicGradientPainter(gp));
		titledSearchResultsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		titledSearchResultsPanel.setTitleFont(new Font("SansSerif", Font.BOLD, 11));
		titledSearchResultsPanel.setTitleForeground(Color.BLACK);

		return titledSearchResultsPanel;
	}

	/**
	 * The abstract method that needs to handle any refresh related activites
	 * 
	 * @param arrComponentPanel
	 *            This is the array of panels that forms the dynamically
	 *            generated criterion pages. Each searchPanel corresponds to one
	 *            attribute from the class/category Method to select appropriate
	 *            searchPanel and refresh the addLimit page
	 * 
	 * @param strClassName
	 *            The class/category name.
	 */
	public void performAction(JXPanel[] arrComponentPanel, String strClassName) {
		Container container = ((JXPanel) (contentPanel)).getParent();

		if (container instanceof SearchCenterPanel) {
			container = (SearchCenterPanel) container;
			/*
			 * Use the parent reference to in turn get a reference to the
			 * navigation searchPanel, and cause it to move to the next card.
			 */
			MainSearchPanel mainSearchPanel = (MainSearchPanel) (container.getParent());
			(mainSearchPanel.getNavigationPanel()).enableButtons();

			/*
			 * Get the searchPanel corresponding to the currently selcted card
			 * and refresh it.
			 */
			AddLimitPanel addLimitPanel = (AddLimitPanel) (mainSearchPanel.getCenterPanel()
					.getAddLimitPanel());
			addLimitPanel.addSearchPanel(mainSearchPanel.getCenterPanel().getChooseCategoryPanel()
					.getSearchPanel());
			addLimitPanel.refresh(arrComponentPanel, strClassName);
			// set search-result searchPanel in AddLimit searchPanel and move to
			// next tab
			if (mainSearchPanel.getCenterPanel().getSelectedCardIndex() == 0) {
				SearchResultPanel searchResultPanel = mainSearchPanel.getCenterPanel()
						.getChooseCategoryPanel().getSearchResultPanel();
				if (searchResultPanel != null) {
					addLimitPanel.addResultsPanel(searchResultPanel);
					mainSearchPanel.getCenterPanel().setAddLimitPanel(addLimitPanel);
				}
				(mainSearchPanel.getNavigationPanel()).showCard(true);
			}

		}
	}

	/**
	 * The abstract method that is to return the number of elements to be
	 * displayed/page.
	 * 
	 * @return int Value represents the number of elements/page.
	 */
	public int getPageSize() {
		return 3;
	};

	/**
	 * @return the addLimitButtonBottom
	 */
	public Cab2bButton getAddLimitButtonBottom() {
		return addLimitButtonBottom;
	}

	/**
	 * @return the addLimitButtonTop
	 */
	public Cab2bButton getAddLimitButtonTop() {
		return addLimitButtonTop;
	}

	/**
	 * Action Listener class for Add Limit buttons
	 * 
	 * @author Deepak_Shingan
	 * 
	 */
	class AddLimitButtonListner implements ActionListener {
		private JXPanel[] panelsToAdd;

		private EntityInterface entity;

		AddLimitButtonListner(final JXPanel[] panelsToAdd, final EntityInterface entity) {
			this.panelsToAdd = panelsToAdd;
			this.entity = entity;
		}

		public void actionPerformed(ActionEvent event) {
			performAddLimitAction(this.panelsToAdd, this.entity);
			AddLimitPanel.m_innerPane.setDividerLocation(242);
		}
	}

	/**
	 * Action Listener class for Edit Limit buttons
	 * 
	 * @author Deepak_Shingan
	 * 
	 */
	class EditLimitButtonListner implements ActionListener {
		private JXPanel[] panelsToAdd;

		private IExpression expression;

		EditLimitButtonListner(final JXPanel[] panelsToAdd, final IExpression expression) {
			this.panelsToAdd = panelsToAdd;
			this.expression = expression;
		}

		public void actionPerformed(ActionEvent event) {
			performEditLimitAction(this.panelsToAdd, this.expression);
		}
	}

}
