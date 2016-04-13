package org.factpub.gui.design;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.factpub.setting.FEConstants;

@SuppressWarnings("serial")
public class DNDScrollPane extends JPanel {
	ImageIcon DropIcon = new ImageIcon(getClass().getClassLoader().getResource(FEConstants.IMAGE_DND));
	private static final int PREF_W = 500;
	private static final int PREF_H = 400;

   public DNDScrollPane() throws IOException {
      JLabel label = new JLabel(DropIcon);
      JScrollPane scrollPane = new JScrollPane(label);

      setLayout(new BorderLayout());
      add(scrollPane);
   }

   @Override
   public Dimension getPreferredSize() {
      if (isPreferredSizeSet()) {
         return super.getPreferredSize();
      }
      return new Dimension(PREF_W, PREF_H);
   }

   private static void createAndShowGui() {
      DNDScrollPane mainPanel = null;
      try {
         mainPanel = new DNDScrollPane();
      } catch (IOException e) {
         e.printStackTrace();
         System.exit(-1);
      }

      JFrame frame = new JFrame("ImageInScrollPane");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.getContentPane().add(mainPanel);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui();
         }
      });
   }
}