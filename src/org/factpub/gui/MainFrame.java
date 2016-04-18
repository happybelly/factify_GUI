package org.factpub.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

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
import org.factpub.setting.FEConstants;
import javax.swing.JPanel;

public class MainFrame implements FEConstants{

	public static JFrame frameMain;
	public static JTable fileTable;
	public static DefaultTableModel tableModel;
	private static JTextField textWikiID;
	private JPasswordField textWikiPass;
    //private static JLabel lblAuthorized;
	public static String JSONFileDirPath = "";
	private static JLabel lblAnnouncement;
	//public static JLabel lblAuthorized;
	public static JTextField txtUserAuth;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
				
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
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
	public MainFrame() {
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
		frameMain.setTitle("factpub");
		frameMain.setBounds(100, 100, 599, 400);
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMain.getContentPane().setLayout(null);
	    CoreDNDListener dndListener = new CoreDNDListener();
		
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
		        	String pageTitle = CoreDNDListener.getPageTitle(row);
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

	    JScrollPane ScrollPane = new JScrollPane(fileTable);
	    //ScrollPane.setBackgroundImage(new ImageIcon(getClass().getClassLoader().getResource(FEConstants.IMAGE_DND)));
        ScrollPane.getViewport().setOpaque(true);
	    ScrollPane.setViewportBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.GRAY));
        ScrollPane.setBounds(0,23,583,339);
        
        frameMain.getContentPane().add(ScrollPane);
	    
	    new DropTarget(ScrollPane, dndListener);
	    
	    JPanel dndPanel = new JPanel();
	    //ScrollPane.setRowHeaderView(dndPanel);
	    frameMain.getContentPane().add(dndPanel);
	    
	    JButton btnLogin = new JButton("Sign in");
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
	    btnLogin.setBounds(317, 0, 78, 23);
	    frameMain.getContentPane().add(btnLogin);
	    
	    textWikiID = new JTextField();
	    textWikiID.setText("<optional>");
	    textWikiID.setToolTipText("");
	    textWikiID.setBounds(49, 1, 97, 20);
	    frameMain.getContentPane().add(textWikiID);
	    textWikiID.setColumns(10);
	    
	    JLabel lblMediaWikId = new JLabel("Login:");
	    lblMediaWikId.setBounds(10, 4, 55, 14);
	    frameMain.getContentPane().add(lblMediaWikId);
	    
	    JLabel lblPassword = new JLabel("Password:");
	    lblPassword.setBounds(152, 4, 78, 14);
	    frameMain.getContentPane().add(lblPassword);
	    
	    textWikiPass = new JPasswordField();
	    textWikiPass.setToolTipText("");
	    textWikiPass.setColumns(10);
	    textWikiPass.setBounds(210, 1, 97, 20);
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
	    
	    txtUserAuth = new JTextField();
	    txtUserAuth.setEditable(false);
	    txtUserAuth.setBounds(397, 1, 97, 20);
	    frameMain.getContentPane().add(txtUserAuth);
	    txtUserAuth.setColumns(10);
	    
	    JButton btnRegister = new JButton("Sign up");
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
	    btnRegister.setBounds(493, 0, 90, 23);
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
		MainFrame.tableModel = tableModel;
	}
	
	public static JTable getFileTable() {
		return fileTable;
	}

	public static void setFileTable(JTable fileTable) {
		MainFrame.fileTable = fileTable;
	}
}
