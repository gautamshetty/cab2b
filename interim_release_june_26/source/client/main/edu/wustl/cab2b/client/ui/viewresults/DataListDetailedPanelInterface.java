package edu.wustl.cab2b.client.ui.viewresults;


/**
 * This interface is implemented by each panel that can eb displayed in 
 * data list detailed section.
 * 
 * It provides methods to convert data of the panel to csv format.
 *  
 * 
 * @author Rahul Ner
 *
 */
public interface DataListDetailedPanelInterface {
    
    /**
     * This method returns  selected data in comma separated formats.
     * It also takes care of special characters and esacpes it with appropraite
     *  esacpe sequences.
     */
    String getCSVData();
    
    
    /**
     * return the number of rows selected for the export operation.
     * @return
     */
    int getNoOfSelectedRows();

}
