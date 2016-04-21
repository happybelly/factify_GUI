package org.factpub.gui.design;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.factpub.utility.FEConstants;

@SuppressWarnings("serial")
public class DNDScrollPane extends JScrollPane {
	
	public DNDScrollPane(JTable fileTable) {
		JScrollPane scrollPane = new JScrollPane(fileTable);
	}

	ImageIcon DropIcon = new ImageIcon(getClass().getClassLoader().getResource(FEConstants.IMAGE_DND));
//	private static final int PREF_W = 500;
//	private static final int PREF_H = 400;

//   public DNDScrollPane(JTable fileTable){
//      JLabel label = new JLabel(DropIcon);
//      JScrollPane scrollPane = new JScrollPane(fileTable);
//     // setLayout(new BorderLayout());
//   }
//   
//   public DNDScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
//	      super(view, vsbPolicy, hsbPolicy);
//	      if (view instanceof JComponent) {
//	         ((JComponent) view).setOpaque(false);
//	      }
//   }
   

//   @Override
//   public Dimension getPreferredSize() {
//      if (isPreferredSizeSet()) {
//         return super.getPreferredSize();
//      }
//      return new Dimension(PREF_W, PREF_H);
//   }
//
//   private static void createAndShowGui() {
////      DNDScrollPane mainPanel = null;
////      
////      try {
////         mainPanel = new DNDScrollPane();
////      } catch (IOException e) {
////         e.printStackTrace();
////         System.exit(-1);
////      }
////
////      JFrame frame = new JFrame("ImageInScrollPane");
////      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
////      frame.getContentPane().add(mainPanel);
////      frame.pack();
////      frame.setLocationByPlatform(true);
////      frame.setVisible(true);
//   }
//
//   public static void main(String[] args) {
//      SwingUtilities.invokeLater(new Runnable() {
//         public void run() {
//            createAndShowGui();
//         }
//      });
//   }
}