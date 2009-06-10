package edu.wustl.cab2b.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import org.jdesktop.swingx.VerticalLayout;

import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.common.util.logger.Logger;


/**
 * The actual implementation of the {@link AbstractAdvancedSearchPanel} as is
 * required in the AddLimit section from the main search dialog.
 * 
 * @author mahesh_iyer/chetan_bh
 * 
 */


public class AddLimitAdvancedSearchPanel extends AbstractAdvancedSearchPanel
{	
	/**
	 * Constructor that invokes the base class version.
	 */
	AddLimitAdvancedSearchPanel()
	{		
		super();
		this.setLayout(new VerticalLayout(0));
		Logger.out.info("AddLimitAdvancedSearchPanel : VerticalLayout set.");
	}
	
	/**
	 * The abstract method implementation from the base class that adds
	 * components in a way required by this implementation of the
	 * {@link AbstractAdvancedSearchPanel}
	 * 
	 */		
	protected void addComponents()
	{				
			
		m_taskPane.getContentPane().setBackground(Color.WHITE);
		//Add all the componenets as required by this panel.
		m_taskPane.add(m_chkClass);
		m_taskPane.add("tab", m_chkClassDesc);
		m_taskPane.add("br", m_chkAttribute);
	//	m_taskPane.add("tab",m_chkClassDef);
		m_taskPane.add("tab",m_chkPermissibleValues);
		
		m_taskPane.add("br", m_radioText);
		m_taskPane.add(m_radioConceptCode);
		
	}
}