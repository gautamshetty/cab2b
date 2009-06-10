package edu.wustl.cab2b.client.ui.main;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.controls.Cab2bComboBox;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.util.CommonUtils;
import edu.wustl.cab2b.common.util.Utility;

/**
 * An abstract class which provides the skeletal implementation of the IComponent interface 
 * and defines some more abstract method like getFirstComponent, getSecondComponent that needs 
 * to be implemented by the subclasses like NumberTypePanel, StringTypePanel, etc.
 * 
 * @author chetan_bh
 */
public abstract class AbstractTypePanel extends Cab2bPanel implements IComponent {
	
	public static final int CAB2B_FORMATTED_TEXT_FIELD_COLUMN_SIZE = 9;
	
	/**
	 * Label for displaying the attribute name.
	 */
	protected Cab2bLabel m_Name;
	
	/**
	 * ComboBox for displaying the conditions based on the data-type
	 */
	protected Cab2bComboBox m_Conditions;
	
	/**
	 * TextField for entering the alphanumeric text.
	 */
	protected JComponent m_NameEdit;
	
	/**
	 * Another TextField for entering the alphanumeric text.
	 */
	protected JComponent m_OtherEdit;
	
	/**
	 * Parsed xml file's data structure.
	 */
	protected ArrayList<String> conditionList;
	
	/**
	 * Entity representing attribute.
	 */
	protected AttributeInterface attributeEntity;
	
	/**
	 * Attribute name represented by the panel.
	 */
	protected String attributeName;
	
	/**
	 * 
	 */
	protected Boolean showCondition;
	
	public AbstractTypePanel(ArrayList<String> conditionList, AttributeInterface attributeEntity,Dimension maxLabelDimension) {
		this(conditionList, attributeEntity, new RiverLayout(), true, maxLabelDimension);
	}
	
	public AbstractTypePanel(ArrayList<String> conditionList, AttributeInterface attributeEntity, Boolean showCondition,Dimension maxLabelDimension) {
		this(conditionList, attributeEntity, new RiverLayout(), showCondition,maxLabelDimension);
	}
	
	public AbstractTypePanel(ArrayList<String> conditionList, AttributeInterface attributeEntity, LayoutManager layoutManager,Dimension maxLabelDimension) {
		this(conditionList, attributeEntity, layoutManager, true, maxLabelDimension);
	}
	
	public AbstractTypePanel(ArrayList<String> conditionList, AttributeInterface attributeEntity, LayoutManager layoutManager, Boolean showCondition, Dimension maxLabelDimension) {
		super(layoutManager);
		
		this.attributeEntity = attributeEntity;
		this.attributeName = attributeEntity.getName();
		this.showCondition = showCondition;
		
		String formattedString = attributeEntity.getName();
        if(!Utility.isCategory(attributeEntity.getEntity())) {
            formattedString = CommonUtils.getFormattedString(formattedString);
        }
        
		m_Name = new Cab2bLabel( formattedString+ " : ");
		m_Name.setPreferredSize(maxLabelDimension);//new Dimension(235,20)
		m_NameEdit = getFirstComponent();
		
		m_OtherEdit = getSecondComponent();
		m_OtherEdit.setEnabled(false);
		m_OtherEdit.setVisible(false);
		m_OtherEdit.setOpaque(false);
		
		final Border border = m_OtherEdit.getBorder();
		final EmptyBorder emptyBorder = new EmptyBorder(2,2,2,2);
		m_OtherEdit.setBorder(emptyBorder);
		
		add("tab", m_Name);
		add("tab", new Cab2bLabel());
		
		
		if(showCondition) {
			setCondtionControl(conditionList, border, emptyBorder);
		}
		
		add("tab", new Cab2bLabel());
		add("tab", m_NameEdit);
		add("tab", m_OtherEdit);
		
		setSize(new Dimension(300,100));
	}
	
	public String getCondition() {
		return (String)m_Conditions.getSelectedItem();
	}
		
	public abstract JComponent getFirstComponent();
	
	public abstract JComponent getSecondComponent();
	
	public  void setCondition(String str) {
		int itemCount = m_Conditions.getItemCount();
		for(int i = 0; i < itemCount; i++) {
			if(m_Conditions.getItemAt(i).toString().compareToIgnoreCase(str) == 0) {
				m_Conditions.setSelectedIndex(i);
			}
		}
	}
    
	public abstract void setComponentPreference(String condition);
    
    public String getAttributeName() {
    	return attributeName;
    }
    
    private void setCondtionControl(ArrayList<String> conditionList, final Border border, final EmptyBorder emptyBorder) {
    	this.conditionList = conditionList;
    	
    	/* Initializing  conditions can't be abstracted, since it varies from string type to number to date */
		m_Conditions = new Cab2bComboBox();
		m_Conditions.setPreferredSize(new Dimension(125,20));
		
		Collections.sort(conditionList);
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for(int i = 0;i < conditionList.size(); i++) {
			model.addElement(conditionList.get(i));
		}
		
		m_Conditions.setModel(model);
		m_Conditions.setMaximumRowCount(conditionList.size());
    	
		m_Conditions.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				conditionListenerAction(border, emptyBorder);
			}
		});
		
		m_Conditions.setSelectedIndex(0);
		add("tab", m_Conditions);
    }
	
    private void conditionListenerAction(final Border border, final EmptyBorder emptyBorder) {
		if(m_Conditions.getSelectedItem().equals("Between")) {
			m_NameEdit.setOpaque(true);
			m_NameEdit.setEnabled(true);
			m_NameEdit.setVisible(true);
			m_NameEdit.setBorder(border);
			m_OtherEdit.setOpaque(true);
			m_OtherEdit.setVisible(true);
			m_OtherEdit.setEnabled(true);
			m_OtherEdit.setBorder(border);
		} else if((m_Conditions.getSelectedItem().equals("Is Null")) || (m_Conditions.getSelectedItem().equals("Is Not Null"))) {
			m_NameEdit.setOpaque(false);
			m_NameEdit.setEnabled(false);
			m_NameEdit.setVisible(false);
			m_NameEdit.setBorder(emptyBorder);
			m_OtherEdit.setOpaque(false);
			m_OtherEdit.setEnabled(false);
			m_OtherEdit.setVisible(false);
			m_OtherEdit.setBorder(emptyBorder);
			ArrayList<String> values = new ArrayList<String>();
			values.add("");
			values.add("");
			setValues(values);
		} else {
			m_NameEdit.setOpaque(true);
			m_NameEdit.setEnabled(true);
			m_NameEdit.setVisible(true);
			m_NameEdit.setBorder(border);
			
			// If previously selected condition was 'Between' then clear the second text box
			ArrayList<String> oldValues = getValues();
			if(m_OtherEdit.isEnabled() && oldValues.size() == 2) {
				ArrayList<String> values = new ArrayList<String>();
				values.add(oldValues.get(0));
				values.add("");
				setValues(values);
			} else {
				setValues(oldValues);
			}
			m_OtherEdit.setOpaque(false);
			m_OtherEdit.setEnabled(false);
			m_OtherEdit.setVisible(false);
			m_OtherEdit.setBorder(emptyBorder);
		}
		setComponentPreference(getCondition());
	}
	
}