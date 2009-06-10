package edu.wustl.cab2b.client.ui.controls.slider;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.Icon;
import javax.swing.JSlider;

/**
 * @author Hrishikesh Rajpathak
 * @author Deepak Shingan
 * @author Atul Jawale
 *
 */
public class MThumbSlider extends JSlider {
    protected int thumbNum = 2;

    protected BoundedRangeModel[] sliderModels;

    protected Icon[] thumbRenderers;

    protected Color[] fillColors;

    protected Color trackFillColor;

    private static final String uiClassID = "MThumbSliderUI";

    Vector inputData = new Vector();

    public int currentInputValueIndex;

    private BiSlider cab2bSliderUI;

    public MThumbSlider() {

    }

    public MThumbSlider(Vector objectInput, BiSlider cab2bSliderUI) {
        this.inputData = objectInput;
        this.cab2bSliderUI = cab2bSliderUI;

        initUI();
        updateUI();
    }

    public MThumbSlider(Vector objectInput, BiSlider slider, Object initialMin, Object initialMax) {
        this.inputData = objectInput;
        this.cab2bSliderUI = slider;

        initUI(initialMin, initialMax);
        updateUI();
    }

    protected void initUI(Object initialMin, Object initialMax) {
        initUI();
        this.setValueAt(inputData.indexOf(initialMin), 0);
        this.setValueAt(inputData.indexOf(initialMax), 1);

    }

    protected void initUI() {

        sliderModels = new BoundedRangeModel[2];
        thumbRenderers = new Icon[2];
        fillColors = new Color[2];
        for (int i = 0; i < 2; i++) {
            if (inputData != null || inputData.size() > 0)
                sliderModels[i] = new DefaultBoundedRangeModel(0, 0, 0, inputData.size() - 1);
            thumbRenderers[i] = null;
            fillColors[i] = null;
        }

        this.setValueAt(0, 0);
        this.setValueAt(inputData.size(), 1);
        this.setFillColorAt(Color.gray, 0);
        this.setFillColorAt(Color.yellow, 1);
        this.setTrackFillColor(Color.gray);
        this.setToolTipText("");
        this.setPaintTicks(true);
        this.setSnapToTicks(true);
        this.putClientProperty("JSlider.isFilled", Boolean.TRUE);

    }

    public void setObjectInput(Vector objectInput) {
        this.inputData = objectInput;
    }

    public void updateUI() {
        AssistantUIManager.setUIName(this);
        super.updateUI();
    }

    public String getUIClassID() {
        return uiClassID;
    }

    public int getThumbNum() {
        return thumbNum;
    }

    public int getValueAt(int index) {
        return getModelAt(index).getValue();
    }

    public void setValueAt(int n, int index) {
        getModelAt(index).setValue(n);
    }

    public Object getMinimumBarValue() {
        return inputData.get(getValueAt(0));
    }

    public Object getMaximumBarValue() {
        return inputData.get(getValueAt(1));
    }

    public int getMinimum() {
        return getModelAt(0).getMinimum();
    }

    public int getMaximum() {
        return getModelAt(0).getMaximum();
    }

    public BoundedRangeModel getModelAt(int index) {
        // System.out.println("model at index ="+index);
        return sliderModels[index];
    }

    public Icon getThumbRendererAt(int index) {
        return thumbRenderers[index];
    }

    public void setThumbRendererAt(Icon icon, int index) {
        System.out.println();
        thumbRenderers[index] = icon;
    }

    public Color getFillColorAt(int index) {
        return fillColors[index];
    }

    public void setFillColorAt(Color color, int index) {
        fillColors[index] = color;
    }

    public Color getTrackFillColor() {
        return trackFillColor;
    }

    public void setTrackFillColor(Color color) {
        trackFillColor = color;
    }

    public String getToolTipText(MouseEvent e) {
        return null;
    }

    public void setFilterValues(Object min, Object max) {
        cab2bSliderUI.setFilterValues(min, max);
        //        System.out.println("seting min and max" + min + " " + max);
    }

    public void displayMinValue(String min) {
        cab2bSliderUI.lowVal.setText(min);

    }

    public void displayMaxValue(String max) {
        cab2bSliderUI.hiVal.setText(max);
    }

    /**
     * Method to set min and max bar values 
     * 
     * @param minBound
     * @param maxBound
     */
    public void setRangeBounds(Comparable minBound, Comparable maxBound) {
        setValueAt(inputData.indexOf(minBound), 0);
        setValueAt(inputData.indexOf(maxBound), 1);
        if (minBound instanceof Date) {
            DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
            displayMinValue(dateFormatter.format(minBound));
            displayMaxValue(dateFormatter.format(maxBound));
        } else {
            displayMinValue((String) minBound);
            displayMaxValue((String) maxBound);
        }
    }
}