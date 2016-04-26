package org.factpub.gui.design;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class ProgressRenderer extends JProgressBar implements TableCellRenderer {
    
	private final JProgressBar b = new JProgressBar();
    private final JPanel p = new JPanel(new BorderLayout());
    
    public ProgressRenderer() {
        super();
        b.setIndeterminate(true);
        setOpaque(true);
        p.add(b);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	Integer i = (Integer) value;
        String text = "Done";
        if (i < 0) {
            text = "Canceled";
        } else if (i < 100) {
            b.setValue(i);
            return p;
        }
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        return this;
    }
    
    @Override
    public void updateUI() {
        super.updateUI();
        if (p != null) {
            SwingUtilities.updateComponentTreeUI(p);
        }
    }
}