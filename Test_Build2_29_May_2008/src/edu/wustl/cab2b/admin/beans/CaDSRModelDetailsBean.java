/**
 * 
 */
package edu.wustl.cab2b.admin.beans;

/**
 * @author chetan_patil
 *
 */
public class CaDSRModelDetailsBean extends BaseBean implements Comparable<CaDSRModelDetailsBean> {
    private static final long serialVersionUID = 1L;

    /** Unique identifier of CaDSR Project */
    private String id;

    /** Long name of CaDSR Project */
    private String longName;

    /** Description of CaDSR Project */
    private String description;

    /**
     * 
     */
    public CaDSRModelDetailsBean() {
        super();
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the longName
     */
    public String getLongName() {
        return longName;
    }

    /**
     * @param longName the longName to set
     */
    public void setLongName(String longName) {
        this.longName = longName;
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

    public int compareTo(CaDSRModelDetailsBean anotherBean) {
        return this.longName.toLowerCase().compareTo(anotherBean.longName.toLowerCase());
    }

}