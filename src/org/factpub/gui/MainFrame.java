package org.factpub.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;
import org.factpub.core.InitTempDir;
import org.factpub.network.AuthMediaWikiId;
import org.factpub.utility.FEConstants;
import org.factpub.utility.Utility;

public class MainFrame implements FEConstants {

	public static JFrame frameMain;
	public static JTable fileTable;
	public static DefaultTableModel tableModel;
	private static JTextField textWikiID;
	private JPasswordField textWikiPass;
	// private static JLabel lblAuthorized;
	public static String JSONFileDirPath = "";

	private static JScrollPane scrollPane = new JScrollPane();
	
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
	 * Constructor: this part is called once when the GUI application is
	 * launched.
	 */
	public MainFrame() {
		// Step 1: initialize GUI
		initGUIDesign();

		// Step 2: initialize %userhome%/factpub folder
		initTempDir();
	}

	/**
	 * Step 2
	 */
	private void initTempDir() {
		InitTempDir.makeTempDir();
		InitTempDir.makeJsonDir();
		InitTempDir.makeRuleINPUTDir();
		InitTempDir.copyRuleInputFiles();// Create Rule_INPUT files to
											// RuleINPUTDir
		String logFile = InitTempDir.makeLogFile();
		
		// Set up log output stream
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(logFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintStream ps = new PrintStream(fos);
		//System.setOut(ps); // change the output stream so the log is saved in
							// file.
	}

	/**
	 * Step 1
	 */
	private void initGUIDesign() {

		// Enabling the Nimbus Look and Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

//			 for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
//			 {
//			 if ("Nimbus".equals(info.getName())) {
//			 UIManager.setLookAndFeel(info.getClassName());
//			 break;
//			 }
//			
//			
//			 }
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}

		frameMain = new JFrame();

		// Get Icon images
		////////////////////////////////////////////////////////////////////////////
		String iconDropPath = FEConstants.IMAGE_DND;
		String iconMainPath = "logo_factpub.png";

		ImageIcon MainIcon = new ImageIcon(getClass().getClassLoader().getResource(iconMainPath));
		ImageIcon DropIcon = new ImageIcon(getClass().getClassLoader().getResource(iconDropPath));

		frameMain.setIconImage(MainIcon.getImage());
		frameMain.setResizable(false);
		
		// Main Panel setting
		frameMain.setTitle(FEConstants.WINDOW_TITLE);
		frameMain.setBounds(100, 100, 421, 390);
		frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameMain.getContentPane().setLayout(null);
		DNDListener dndListener = new DNDListener();

		scrollPane.setViewportBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.GRAY));
		scrollPane.setBounds(0, 23, 415, 339);
		
		setViewportLabel(DropIcon);		

		frameMain.getContentPane().add(scrollPane);

		new DropTarget(scrollPane, dndListener);

		JButton btnLogin = new JButton("Login");

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

	public static void setViewportLabel(ImageIcon image){
		JViewport view = scrollPane.getViewport();
		
		//JLabel label = new JLabel(image);
		JPanel panel = new JPanel();
		//panel.add(label);
		
		JLayeredPane layerPane = new JLayeredPane();
		layerPane.setLayout(null);
		
		System.out.println(scrollPane.getBounds());
		
		JLabel labelBackground = new JLabel(image);
		labelBackground.setBounds(-1, -2, 415, 339);
		System.out.println(labelBackground.getBounds());
		
		String announcement = Utility.getAnnouncement();
		JLabel labelAnnounce = new JLabel(announcement);
		labelAnnounce.setBounds(150, 280, 150, 50);
		
		layerPane.add(labelBackground, new Integer(0));
		layerPane.add(labelAnnounce, new Integer(1));
		
		view.setView(layerPane);
	}
	
	public static void setViewportTable(){
		JViewport view = scrollPane.getViewport();
		System.out.println("setVieportTable was called");
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// Table
				tableModel = new DefaultTableModel(0, TABLE_COLUMN_NUM);
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
							String pageTitle = DNDListener.getPageTitle(row);
							String uriString = FEConstants.PAGE_CREATED + pageTitle;
							if (pageTitle != null) {
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
				DefaultTableColumnModel columnModel = (DefaultTableColumnModel) fileTable.getColumnModel();

				TableColumn column_1 = columnModel.getColumn(TABLE_COLUMN_FILE);
				column_1.setPreferredWidth(200);
				column_1.setHeaderValue("File");

				TableColumn column_2 = columnModel.getColumn(TABLE_COLUMN_STATUS);
				column_2.setPreferredWidth(200);
				column_2.setHeaderValue("Status");
		
		view.setView(fileTable);
	}
	
	public static DefaultTableModel getTableModel() {
		return tableModel;
	}

	public static void setTableModel(DefaultTableModel tableModel) {
		MainFrame.tableModel = tableModel;
	}

}