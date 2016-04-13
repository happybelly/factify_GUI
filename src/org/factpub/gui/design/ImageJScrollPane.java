package org.factpub.gui.design;

import javax.swing.table.*;

import org.factpub.setting.FEConstants;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
    
class ImageJScrollPane extends JScrollPane
{
   private ImageIcon image = null;
  
   public ImageJScrollPane() {
      this(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
   }
  
   public ImageJScrollPane(Component view) {
      this(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
   }
    
   public ImageJScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
      super(view, vsbPolicy, hsbPolicy);
      if (view instanceof JComponent) {
         ((JComponent) view).setOpaque(false);
      }
   }
  
   public ImageJScrollPane(int vsbPolicy, int hsbPolicy) {
      this(null, vsbPolicy, hsbPolicy);
   }
  
   public void setBackgroundImage(ImageIcon image) {
      this.image = image;
   }
  
   public void paint(Graphics g) {
      // Do not use cached image for scrolling
      // getViewport().setBackingStoreEnabled(false);
  
      if (image != null) {
         Rectangle rect = getViewport().getViewRect();
         for (int x=0; x<rect.width; x+=image.getIconWidth()) {
            for (int y=0; y<rect.height; y+=image.getIconHeight()) {
               g.drawImage(image.getImage(), x, y, null, null);
            }
         }
  
         super.paint(g);
      }
   }
}
