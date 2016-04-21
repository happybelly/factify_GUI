package org.factpub.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;
import org.factpub.core.FEWrapper;
import org.factpub.core.InitTempDir;
import org.factpub.network.AuthMediaWikiId;
import org.factpub.network.PostFile;
import org.factpub.utility.FEConstants;

public class MainFrameMultiThreads extends Thread implements FEConstants{

	public static JFrame frameMain;
	public static JTable fileTable;
	public static DefaultTableModel tableModel;
	private static JTextField textWikiID;
	private JPasswordField textWikiPass;
    public static String JSONFileDirPath = "";
	private static JLabel lblAnnouncement;
	
	/**
	 * MultiThreads
	 */
	private Semaphore semaphore; 

	// Default Constructor Must be Clarified for Threads
	public MainFrameMultiThreads(Semaphore semaphore){
	}

	// For Thread
	private int row_thread;
	private File file;
			
	public MainFrameMultiThreads(Semaphore semaphore, File file, int row_thread){
		this.semaphore = semaphore;
		this.file = file;
		this.row_thread = row_thread;
	}
	
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
		        				//titlesArray.set(row_thread, pageTitle);
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
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
				
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrameMultiThreads window = new MainFrameMultiThreads();
					window.frameMain.setVisible(true);
													
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	
	/**
	 * Constructor: this part is called once when the GUI application is launched.
	 */
	public MainFrameMultiThreads() {
		//Step 1: initialize GUI
		initGUIDesign();
		
		//Step 2: initialize %userhome%/factpub folder
		initTempDir();
	}

	/**
	 * Step 2
	 */
	private void initTempDir() {
		InitTempDir.makeTempDir();
		InitTempDir.makeJsonDir();
		InitTempDir.makeRuleINPUTDir();
		InitTempDir.copyRuleInputFiles();// Create Rule_INPUT files to RuleINPUTDir
	}
	
	/**
	 * Step 1
	 */
	private void initGUIDesign() {
		
		// Enabling the Nimbus Look and Feel
		try {
		     UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			 
//		     for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//		        if ("Nimbus".equals(info.getName())) {
//		            UIManager.setLookAndFeel(info.getClassName());
//		            break;
//		        }
//		        
//		        
//		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		frameMain = new JFrame();
		
		// Get Icon images
		////////////////////////////////////////////////////////////////////////////
		String iconDropPath = FEConstants.IMAGE_DND;
		String iconMainPath = "logo_factpub.png";
		
		ImageIcon MainIcon = new ImageIcon(getClass().getClassLoader().getResource(iconMainPath));
		ImageIcon DropIcon = new ImageIcon(getClass().getClassLoader().getResource(iconDropPath));
		
		frameMain.setIconImage(MainIcon.getImage());
		
		frameMain.setResizable(true);
		
		//URL iconUrl = this.getClass().getClassLoader().getResource("resources/logo_factpub.png");
		//ImageIcon icon = new ImageIcon(iconUrl);
		
		//frameMain.setIconImage(imgDrop);
		
		// Main Panel setting
		frameMain.setTitle(FEConstants.WINDOW_TITLE);
		frameMain.setBounds(100, 100, 431, 400);
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMain.getContentPane().setLayout(null);
	    DNDThreadSingle dndListener = new DNDThreadSingle();
		
        // Table
		tableModel = new DefaultTableModel(0,TABLE_COLUMN_NUM);
		JTable fileTable = new JTable(tableModel);
		fileTable.setBackground(Color.WHITE);
		fileTable.setAutoCreateRowSorter(true);
		fileTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		fileTable.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent event) {
		        int row = fileTable.rowAtPoint(event.getPoint());
		        int col = fileTable.columnAtPoint(event.getPoint());
		        if (col == TABLE_COLUMN_STATUS) {
		        	System.out.println("cell clicked at (row: " + row + ", col: " + col + ")");
		        	
		        	// Launch Browser
		        	Desktop desktop = Desktop.getDesktop();
		        	String pageTitle = DNDThreadSingle.getPageTitle(row);
		    		String uriString = FEConstants.PAGE_CREATED + pageTitle;
			    	if(pageTitle != null){
		    			try {
			    			URI uri = new URI(uriString);
			    			desktop.browse(uri);
			    		} catch (URISyntaxException e) {
			    			e.printStackTrace();
			    		} catch (IOException e) {
			    			e.printStackTrace();
			    		}
			    	}
		        }
		    }
		});
		
		// Column headersを設定
		DefaultTableColumnModel columnModel
	    = (DefaultTableColumnModel)fileTable.getColumnModel();

		TableColumn column_1 = columnModel.getColumn(TABLE_COLUMN_FILE);
	    column_1.setPreferredWidth(200);
	    column_1.setHeaderValue("File");
		
	    TableColumn column_2 = columnModel.getColumn(TABLE_COLUMN_STATUS);
	    column_2.setPreferredWidth(200);
	    column_2.setHeaderValue("Status");
	    
	    //DNDScrollPane scrollPane = new DNDScrollPane(fileTable);
	    JScrollPane scrollPane = new JScrollPane(fileTable);
	    
	    //scrollPane.setBackgroundImage(new ImageIcon(getClass().getClassLoader().getResource(FEConstants.IMAGE_DND)));
	    scrollPane.getViewport().setOpaque(true);
	    scrollPane.setViewportBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.GRAY));
	    scrollPane.setBounds(0,23,415,339);
        
        frameMain.getContentPane().add(scrollPane);
	    
	    new DropTarget(scrollPane, dndListener);
	   
	    //ImageIcon iconLogin =  new ImageIcon(getClass().getClassLoader().getResource(FEConstants.IMAGE_BUTTON_LOGIN));
	    JButton btnLogin = new JButton("Login");
	    
	    //btnLogin.setSelectedIcon(new ImageIcon("C:\\Users\\suns1\\Pictures\\Graphicloads-Filetype-Pdf.ico"));
	    btnLogin.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		String wikiID = textWikiID.getText();
	    		String wikiPass = new String(textWikiPass.getPassword());
	    		
	    		try {
					AuthMediaWikiId.authMediaWikiAccount(wikiID, wikiPass.toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		
	    	}
	    });
	    btnLogin.setBounds(296, -1, 78, 23);
	    frameMain.getContentPane().add(btnLogin);
	    
	    textWikiID = new JTextField();
	    textWikiID.setText("<optional>");
	    textWikiID.setToolTipText("");
	    textWikiID.setBounds(32, 0, 97, 20);
	    frameMain.getContentPane().add(textWikiID);
	    textWikiID.setColumns(10);
	    
	    JLabel lblMediaWikId = new JLabel("ID:");
	    lblMediaWikId.setBounds(10, 4, 29, 14);
	    frameMain.getContentPane().add(lblMediaWikId);
	    
	    JLabel lblPassword = new JLabel("Password:");
	    lblPassword.setBounds(139, 3, 67, 14);
	    frameMain.getContentPane().add(lblPassword);
	    
	    textWikiPass = new JPasswordField();
	    textWikiPass.setToolTipText("");
	    textWikiPass.setColumns(10);
	    textWikiPass.setBounds(198, 0, 97, 20);
	    frameMain.getContentPane().add(textWikiPass);
	    
	    // Announcement check
		ArrayList<String> contentList = null;
		String content = null;
		// Check if Announcement is available.	
		try{
			URL url = new URL(FEConstants.SERVER_ANNOUNCEMENT);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			contentList = new ArrayList<String>();
			while((line = in.readLine()) != null){
				contentList.add(line);
			}
			in.close();
			content = StringUtils.join(contentList, " ");
		} catch(MalformedURLException e){
			content = "NA";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			content = "NoInternet";
		}
	    
	    JLabel lblAnnouncement = new JLabel(content);
	    lblAnnouncement.setForeground(Color.BLUE);
	    lblAnnouncement.setBounds(0, 31, 734, 14);
	    frameMain.getContentPane().add(lblAnnouncement);
	    
	    
	    //ImageIcon iconRegister =  new ImageIcon(getClass().getClassLoader().getResource(FEConstants.IMAGE_BUTTON_REGISTER));
	    JButton btnRegister = new JButton("+");
	    btnRegister.setToolTipText("Create new factpub account");
	    btnRegister.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		System.out.println("Register clicked");
    	        try {
    	            Desktop desktop = Desktop.getDesktop();

    	            desktop.browse(new URI(FEConstants.PAGE_REGISTER));

    	        } catch (URISyntaxException ex) {
    	            ex.printStackTrace();
    	        } catch (IOException ex) {
    	            ex.printStackTrace();
    	        }
	    	}
	    });
	    
	    btnRegister.setBounds(374, -1, 40, 23);
	    frameMain.getContentPane().add(btnRegister);
	       
	}
	
	public static void refreshTable(int filenum){
	    //dndLabel.setText("processed: " + filenum);
		//fileTable.setModel(tableModel);
	}
	
	public static DefaultTableModel getTableModel() {
		return tableModel;
	}

	public static void setTableModel(DefaultTableModel tableModel) {
		MainFrameMultiThreads.tableModel = tableModel;
	}
	
	public static JTable getFileTable() {
		return fileTable;
	}

	public static void setFileTable(JTable fileTable) {
		MainFrameMultiThreads.fileTable = fileTable;
	}
}
