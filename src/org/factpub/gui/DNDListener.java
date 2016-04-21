package org.factpub.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.table.DefaultTableModel;

import org.factpub.utility.FEConstants;
import org.factpub.utility.Utility;

public class DNDListener implements DropTargetListener{

	private static List<File> filesArray = new ArrayList<File>();
	private static List<String> fnameArray = new ArrayList<String>();
	private static List<String> pathsArray = new ArrayList<String>();
	private static List<String> titlesArray = new ArrayList<String>();
	private int i = 0;
	private String[] row = new String[FEConstants.TABLE_COLUMN_NUM];
	
	Semaphore smph = new Semaphore(FEConstants.MAX_THREADS);	

	public static String setPageTitle(int row, String pageTitle){
		return titlesArray.set(row, pageTitle);
	}
	
	public static String getPageTitle(int i){
		return titlesArray.get(i);
	}
	
	
    @Override
    public void drop(DropTargetDropEvent event) {
    	
        // Accept copy drops
        event.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = event.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        // Loop through the flavors
        for (DataFlavor flavor : flavors) {
        	            
        	try {
                // If the drop items are files
                if (flavor.isFlavorJavaFileListType()) {

                    // Get all of the dropped files
					List<File> files = (List) transferable.getTransferData(flavor);
                    
                    //////////////////////////////////
                    // Thread: Allocate Thread Pool //
                    //////////////////////////////////
                   
                    //ExecutorService service = Executors.newFixedThreadPool(files.size());
                    //ExecutorService service = Executors.newFixedThreadPool(FEConstants.MAX_THREADS);
                    
                    // Loop them through
                    for (File file : files) {
                    	
                        // Print out the file path
                    	filesArray.add(file);
                        fnameArray.add(file.getName());
                        pathsArray.add(file.getPath());
                        titlesArray.add(null);
                    	System.out.println(pathsArray.get(i));

                    	if (Utility.getSuffix(pathsArray.get(i)).equals("pdf")){
                    		row[0] = fnameArray.get(i);
                    		row[1] = "Waiting...";

                    		//////////////////////////////////
                            // Thread: Run process          //
                            //////////////////////////////////
                    		Thread thread = new Thread(new FEThread(smph, file, i));
                    		
                    		try{
                    			thread.start();
                    		}catch (ArrayIndexOutOfBoundsException e){
                    			row[1] = "ArrayIndexOutOfBoundsException error!";
                    			e.printStackTrace();
                    			
                    		}catch (Exception e){
                    			row[1] = "Unknown multithread error!";
                    			e.printStackTrace();
                    			
                    		}finally{
                    			
                    		}
                    		
                    	}else{
                    		row[0] = fnameArray.get(i);
                    		row[1] = "Invalid Input";
                    	}
                    	
                    	//Bug must exist here...
                    	DefaultTableModel tableModel = MainFrame.getTableModel();

                    	System.out.println(row[0]);
                    	System.out.println(row[1]);
						tableModel.addRow(row);
						
						MainFrame.setTableModel(tableModel);
						
						//JTable fileTable = MainPanel.getFileTable();
						//fileTable.setModel(tableModel);
						
						i = i + 1;
                    	
                    }
                /////////////////////////////////
                // Thread: close the pool      //
                /////////////////////////////////
                }

            } catch (Exception e) {

                // Print out the error stack
                e.printStackTrace();

            }
        }
        
        // Inform that the drop is complete
        event.dropComplete(true);

    }    
    
	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}	
}
