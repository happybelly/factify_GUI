package org.factpub.gui.design;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

class ProgressRenderer extends DefaultTableCellRenderer {
//    private JProgressBar b = new JProgressBar();
//    private final JPanel p = new JPanel(new BorderLayout());
//    public ProgressRenderer() {
//        super();
//        setOpaque(true);
//        p.add(b);
//
//    }
    @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        Integer i = (Integer) value;
//        String text = "Done";
//        if (i < 0) {
//            text = "Canceled";
//        } else if (i < 100) {
//            b.setValue(i);
//            return p;
//        }
//        super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
        return this;
    }

}