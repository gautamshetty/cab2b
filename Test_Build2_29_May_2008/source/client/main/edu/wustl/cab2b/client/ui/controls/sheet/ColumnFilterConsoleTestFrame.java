package edu.wustl.cab2b.client.ui.controls.sheet;

/*
 * ColumnFilterConsoleTestFrame.java
 *
 * Created on December 5, 2007, 3:57 PM
 */

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

/**
 *
 * @author  deepak_shingan
 */
public class ColumnFilterConsoleTestFrame extends javax.swing.JFrame {

    private ColumnFilterModel cfm1 = new ColumnFilterModel();

    private ColumnFilterModel cfm2 = new ColumnFilterModel();

    private ColumnFilterModel cfm3 = new ColumnFilterModel();

    private ColumnFilterVerticalConsole cfvc = new ColumnFilterVerticalConsole();

    /** Creates new form ColumnFilterConsoleTestFrame */
    public ColumnFilterConsoleTestFrame() {
        initComponents();

        loadSampleValues();

        cfm2.addPropertyChangeListener(new FilterChangeListener());
        cfvc.setModel(cfm1);

        pnlMain.add(cfvc, BorderLayout.CENTER);
    }

    private void loadSampleValues() {
        ArrayList sampleValues = new ArrayList();
        sampleValues.add("a");
        sampleValues.add("b");
        sampleValues.add("c");
        sampleValues.add("d");
        cfm1.setSampleValues(sampleValues);

        sampleValues.clear();
        sampleValues.add("x");
        sampleValues.add("y");
        sampleValues.add("z");
        cfm2.setSampleValues(sampleValues);

        sampleValues.clear();
        sampleValues.add("1");
        sampleValues.add("2");
        sampleValues.add("3");
        cfm3.setSampleValues(sampleValues);
    }

    public class FilterChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {

            if (evt.getPropertyName().equals(ColumnFilterModel.PROPERTY_ENUM_LIST_FILTER_CHANGED)) {

            } else if (evt.getPropertyName().equals(ColumnFilterModel.PROPERTY_RANGE_FILTER_CHANGED)) {

            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setMinimumSize(new java.awt.Dimension(200, 200));
        pnlMain.setPreferredSize(new java.awt.Dimension(200, 200));
        pnlMain.setLayout(new java.awt.BorderLayout());

        jButton1.setText("Install M1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        jButton2.setText("Install M2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        jButton3.setText("Install M3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);

        pnlMain.add(jPanel1, java.awt.BorderLayout.SOUTH);

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 366) / 2, (screenSize.height - 202) / 2, 366, 202);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        cfvc.setModel(cfm1);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        cfvc.setModel(cfm2);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        cfvc.setModel(cfm3);

    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ColumnFilterConsoleTestFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel pnlMain;
    // End of variables declaration//GEN-END:variables
}
