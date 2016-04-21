package org.factpub.gui;

import java.awt.Component;
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

import org.factpub.core.FEWrapper;
import org.factpub.network.PostFile;
import org.factpub.utility.FEConstants;
import org.factpub.utility.Utility;


public class DNDThreadSemaphore_backup extends Thread implements DropTargetListener{
	
	protected static final Component Component = null;
	private static List<File> filesArray = new ArrayList<File>();
	private static List<String> fnameArray = new ArrayList<String>();
	private static List<String> pathsArray = new ArrayList<String>();
	private static List<String> titlesArray = new ArrayList<String>();
	private int i = 0;
	private String[] row = new String[FEConstants.TABLE_COLUMN_NUM];
	
	private Semaphore semaphore; 

	// Default Constructor Must be Clarified for Threads
	public DNDThreadSemaphore_backup(Semaphore semaphore){
	}

	Semaphore smph = new Semaphore(FEConstants.MAX_THREADS);
	
	// For Thread
	private int row_thread;
	private File file;
	
	// Default Constructor Must be Clarified for Threads
	public DNDThreadSemaphore_backup(){
	}
		
	public DNDThreadSemaphore_backup(Semaphore semaphore, File file, int row_thread){
		this.semaphore = semaphore;
		this.file = file;
		this.row_thread = row_thread;
	}
	
	public static List<File> getFiles(){
		return filesArray;
	}
	
	public static List<String> getFname(){
		return fnameArray;
	}

	public static List<String> getFilePaths(){
		return pathsArray;
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
                    		//service.execute(new CoreDNDListener(file, i)); //<-----------------------------run sub process here!!
                    		new DNDThreadSemaphore_backup(smph, file, i).start();
                    		
                    	}else{
                    		row[0] = fnameArray.get(i);
                    		row[1] = "Invalid Input";
                    	}

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
	
	// Thread process
	@Override
	public void run() {
		
		try{
			
			this.semaphore.acquire();
			
			MainFrame.tableModel.setValueAt("Now Extracting...", row_thread, FEConstants.TABLE_COLUMN_STATUS);		
			
			
			//TableCellRenderer cellRenderer = MainPanel.fileTable.getCellRenderer(row_thread, FEConstants.TABLE_COLUMN_STATUS);
			//cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
			
			//TableCellRenderer cell = MainPanel.fileTable.getCellRenderer(row_thread, FEConstants.TABLE_COLUMN_STATUS);
			
			//MainPanel.fileTable.setDefaultRenderer(Color.class, new ColorRenderer(true));
			
	//		cell.getTableCellRendererComponent(MainPanel.fileTable, progressBar, true, true, row_thread, FEConstants.TABLE_COLUMN_STATUS){
	//			
	//		}
			
			
			//MainPanel.fileTable.getColumnModel().getColumn(TABLE_COLUMN_STATUS).setCellRenderer(new ProgressRenderer());
			String status = FEWrapper.GUI_Wrapper(file);  // <--------------------------- where FactExtractor is executed!
			// If success
			MainFrame.tableModel.setValueAt(status, row_thread, FEConstants.TABLE_COLUMN_STATUS);
			
			if(status == FEConstants.FE_STATUS_CODE_1){
				// Fact Extractor
				try{    		    		
		    		// Uploading Facts
		    		if(status.equals(FEConstants.STATUS_UPLOADING)){
		        		try{
		        			System.out.println(FEWrapper.fileNameMD5);
		        			
		        			// File name must be MD5!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! otherwise it does get error.
		        			File json = new File(FEWrapper.fileNameMD5);
		        			
		        			// with DOI --> uploadtoFactpub_DOI
		        			// without DOI --> uploadtoFactpub_noDOI
		//        			if(checkDOIexists(json)){
		//        				System.out.println("ParseJSON.checkDOIexists is called");
		//        			}
		        			        			
		        			List<String> res = PostFile.uploadToFactpub(json);
		        			//TableCellRenderer cell = MainPanel.fileTable.getCellRenderer(row_thread, col_thread);        			
		        			//status = "Upload Success!";      			

		        			//MainPanel.tableModel.setValueAt(res, row_thread, TABLE_COLUMN_STATUS);
		        			
		        			// If the server returns page title, put it into the array so browser can open the page when user click it.
		        			if(res.get(0).contains(FEConstants.SERVER_RES_TITLE_BEGIN)){
		        				//Embedding HyperLink
		        				String pageTitle = (String) res.get(0).subSequence(res.get(0).indexOf(FEConstants.SERVER_RES_TITLE_BEGIN) + FEConstants.SERVER_RES_TITLE_BEGIN.length(), res.get(0).indexOf(FEConstants.SERVER_RES_TITLE_END));
		        				pageTitle = pageTitle.replace(" ", "_");
		        				System.out.println(pageTitle);
		        				titlesArray.set(row_thread, pageTitle);
		        				MainFrame.tableModel.setValueAt("<html><u><font color=\"blue\">Upload Success!</font></u></html>", row_thread, FEConstants.TABLE_COLUMN_STATUS);
		
		        				//change table color        				
		        			}else{
		        				MainFrame.tableModel.setValueAt("Upload Success!", row_thread, FEConstants.TABLE_COLUMN_STATUS);
		        			}
		        			
		        			// embed HTML to the label
		        			}catch(Exception e){
		        			status = FEConstants.STATUS_UPLOAD_FAILED;
		        			MainFrame.tableModel.setValueAt(status, row_thread, FEConstants.TABLE_COLUMN_STATUS);
		        		}
		    		}	                    		
				}catch(Exception e){
					// If not success
					status = "Failed to upload.";
					MainFrame.tableModel.setValueAt(status, row_thread, FEConstants.TABLE_COLUMN_STATUS);
				}
			}
		}catch (InterruptedException e){
			System.out.println("Waiting error occured.");
			e.printStackTrace();
		}finally{
			this.semaphore.release();
		}
	}
}
//
//class ProgressRenderer extends DefaultTableCellRenderer {
//    
//    @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//    	final JProgressBar b = new JProgressBar(); 
//        
//        return this;
//    }
//}