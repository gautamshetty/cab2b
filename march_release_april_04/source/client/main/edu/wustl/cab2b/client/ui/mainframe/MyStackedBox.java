
package edu.wustl.cab2b.client.ui.mainframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wustl.cab2b.client.ui.RiverLayout;
import edu.wustl.cab2b.client.ui.WindowUtilities;
import edu.wustl.cab2b.client.ui.controls.Cab2bHyperlink;
import edu.wustl.cab2b.client.ui.controls.Cab2bLabel;
import edu.wustl.cab2b.client.ui.controls.Cab2bPanel;
import edu.wustl.cab2b.client.ui.controls.StackedBox;
import edu.wustl.common.util.logger.Logger;

public class MyStackedBox extends Cab2bPanel
{
	StackedBox stackedBox;
	JPanel mySearchQueriesPanel;
	JPanel popularSearchCategoryPanel;
	JPanel myExperimentsPanel;
	
	public MyStackedBox()
	{
		//this.setBorder(new LineBorder(Color.BLACK));  
		this.setLayout(new BorderLayout());
		stackedBox = new StackedBox();
		stackedBox.setTitleBackgroundColor(new Color(200, 200, 220));
		//stackedBox.setTitleBackgroundColor(new Color(224, 224, 224));
		JScrollPane scrollPane = new JScrollPane(stackedBox);
		scrollPane.setBorder(null);
		this.add(scrollPane, BorderLayout.CENTER);

		// the status pane
		mySearchQueriesPanel = new JPanel();
		mySearchQueriesPanel.setLayout(new RiverLayout(10, 5));
		mySearchQueriesPanel.setPreferredSize(new Dimension(250, 150));
		mySearchQueriesPanel.setOpaque(false);
		stackedBox.addBox("My Search Queries", mySearchQueriesPanel,"resources/images/mysearchqueries_icon.gif");

		// the profiling results
		popularSearchCategoryPanel = new JPanel();
		popularSearchCategoryPanel.setLayout(new RiverLayout(10, 5));
		popularSearchCategoryPanel.setOpaque(false);
		popularSearchCategoryPanel.setPreferredSize(new Dimension(250, 150));
		stackedBox.addBox("Popular Categories", popularSearchCategoryPanel,"resources/images/popularcategories_icon.gif");

		// the saved snapshots pane
		myExperimentsPanel = new JPanel();
		myExperimentsPanel.setLayout(new RiverLayout(10, 5));
		myExperimentsPanel.setPreferredSize(new Dimension(250, 150));
		myExperimentsPanel.setOpaque(false);
		stackedBox.addBox("My Experiments", myExperimentsPanel,"resources/images/arrow_icon_mo.gif"); //TODO relpace with exp_icon.
		
		stackedBox.setPreferredSize(new Dimension(250,500));
		stackedBox.setMinimumSize(new Dimension(250,500));
	}
	
	public void setDataForMySearchQueriesPanel(Vector data)
	{
		Logger.out.info("setDataForMySearchQueriesPanel :: data "+data);
		mySearchQueriesPanel.removeAll();
		mySearchQueriesPanel.add(new Cab2bLabel());
		Iterator iter = data.iterator();
		while(iter.hasNext())
		{
			Object obj = iter.next();
			String hyperlinkName = obj.toString();
			Cab2bHyperlink hyperlink = new Cab2bHyperlink();
			hyperlink.setText(hyperlinkName);
			mySearchQueriesPanel.add("br",hyperlink);
		}
		mySearchQueriesPanel.revalidate();
		this.validate();
	}
	
	public void setDataForPopularSearchCategoriesPanel(Vector data)
	{
		Logger.out.info("setDataForPopularSearchCategoriesPanel :: data "+data);
		popularSearchCategoryPanel.removeAll();
		popularSearchCategoryPanel.add(new Cab2bLabel());
		Logger.out.info("data "+data);
		Iterator iter = data.iterator();
		while(iter.hasNext())
		{
			Object obj = iter.next();
			String hyperlinkName = obj.toString();
			Cab2bHyperlink hyperlink = new Cab2bHyperlink();
			hyperlink.setText(hyperlinkName);
			popularSearchCategoryPanel.add("br",hyperlink);
		}
		popularSearchCategoryPanel.revalidate();
	}
	
	public void setDataForMyExperimentsPanel(Vector data)
	{
		Logger.out.info("setDataForMyExperimentsPanel :: data "+data);
		myExperimentsPanel.removeAll();
		myExperimentsPanel.add(new Cab2bLabel());
		Iterator iter = data.iterator();
		while(iter.hasNext())
		{
			Object obj = iter.next();
			String hyperlinkName = obj.toString();
			Cab2bHyperlink hyperlink = new Cab2bHyperlink();
			hyperlink.setBounds(new Rectangle(5,5,5,5));
			hyperlink.setText(hyperlinkName);
			myExperimentsPanel.add("br",hyperlink);
		}
		myExperimentsPanel.revalidate();
	}
	
	static JLabel makeBold(JLabel label)
	{
		label.setFont(label.getFont().deriveFont(Font.BOLD));
		return label;
	}
	
	public static void main(String[] args)
	{
		Logger.configure("");
		MyStackedBox myStackedBox = new MyStackedBox();
		WindowUtilities.showInFrame(myStackedBox, "MyStacked Box");
		
		Vector data = new Vector();
		data.add("SNP Experiment");
		
		myStackedBox.setDataForPopularSearchCategoriesPanel(data);
		
		
	}
	
	
	
}
