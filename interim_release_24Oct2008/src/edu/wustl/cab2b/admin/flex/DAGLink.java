package edu.wustl.cab2b.admin.flex;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class represents DAGPath from Flex.
 * 
 * @author lalit_chand
 */
public class DAGLink extends DAGObject {
    private static final long serialVersionUID = 1L;

    private String toolTip = null;

    private boolean isSelected = false;

    private long sourceNodeId = 0;

    private long destinationNodeId = 0;

    private String pathId;

    /**
     * @return toolTip of DAGPath
     */
    public String getToolTip() {
        return toolTip;
    }

    /**
     * Set toolTip of DAGPath
     */

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    /**
     * @return true if path is selected
     */

    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Set true if path is selected
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * It reads serialized data coming from flex client
     */

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // TODO Auto-generated method stub
        toolTip = in.readUTF();
        pathId = in.readUTF();
        isSelected = in.readBoolean();
        sourceNodeId = in.readInt();
        destinationNodeId = in.readInt();
    }

    /**
     * It serializes the DAGLink Object for communication with flex client
     */

    public void writeExternal(ObjectOutput out) throws IOException {
        // TODO Auto-generated method stub
        out.writeUTF(toolTip);
        out.writeUTF(pathId);
        out.writeBoolean(isSelected);
        out.writeInt((int) sourceNodeId);
        out.writeInt((int) destinationNodeId);
    }

    /**
     * @return pathId
     */

    public String getPathId() {
        return pathId;
    }

    /**
     * It sets pathId
     */

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    /**
     * @return sourceNodeId
     */

    public long getSourceNodeId() {
        return sourceNodeId;
    }

    /**
     * It sets sourceNodeId
     */

    public void setSourceNodeId(long sourceExpId) {
        this.sourceNodeId = sourceExpId;
    }

    /**
     * @return destinationNodeId
     */

    public long getDestinatioNodeId() {
        return destinationNodeId;
    }

    /**
     * It sets destinationNodeId
     */
    public void setDestinationNodeId(long destinationExpId) {
        this.destinationNodeId = destinationExpId;
    }

}
