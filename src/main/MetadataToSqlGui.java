package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;

import object.MusicLibrary;
import threads.LibraryImporterThread;
import threads.LibraryUpdaterThread;
import threads.OperationThread;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class MetadataToSqlGui {

	private JFrame frmMetadataToSql;
	private JTextField txtSelectedDirectory;
	private final ButtonGroup bgOperationMode = new ButtonGroup();
	
	private File mSelectedDirectory = null;
	private JPasswordField pwdDatabasePassword;
	private JTextField txtDatabaseAddress;
	private JTextField txtDatabaseUsername;
	private JCheckBox chckbxLoadSerializedMusic;
	private JRadioButton rdbtnMusicLibraryToSQL;
	private JRadioButton rdbtnSqlToMusic;
	private JPanel pnlMusicLibraryToSQLOptions;
	private JPanel pnlSQLToMusicLibraryOptions;
	private JTextPane txtpnOutput;
	private JPanel panel;
	private JCheckBox chckbxWriteChanges;
	
	private boolean mSerializedLibraryExists;

	/**
	 * Create the application.
	 */
	public MetadataToSqlGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMetadataToSql = new JFrame();
		frmMetadataToSql.setTitle("Metadata to SQL");
		frmMetadataToSql.setBounds(100, 100, 694, 681);
		frmMetadataToSql.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{180, 361, 0};
		gridBagLayout.rowHeights = new int[]{25, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		frmMetadataToSql.getContentPane().setLayout(gridBagLayout);
		
		JButton btnSelectDirectory = new JButton("Select Directory");
		GridBagConstraints gbc_btnSelectDirectory = new GridBagConstraints();
		gbc_btnSelectDirectory.anchor = GridBagConstraints.WEST;
		gbc_btnSelectDirectory.insets = new Insets(0, 0, 5, 5);
		gbc_btnSelectDirectory.gridx = 0;
		gbc_btnSelectDirectory.gridy = 0;
		frmMetadataToSql.getContentPane().add(btnSelectDirectory, gbc_btnSelectDirectory);
		
		txtSelectedDirectory = new JTextField();
		txtSelectedDirectory.setEditable(false);
		GridBagConstraints gbc_txtSelectedDirectory = new GridBagConstraints();
		gbc_txtSelectedDirectory.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSelectedDirectory.insets = new Insets(0, 0, 5, 0);
		gbc_txtSelectedDirectory.gridx = 1;
		gbc_txtSelectedDirectory.gridy = 0;
		frmMetadataToSql.getContentPane().add(txtSelectedDirectory, gbc_txtSelectedDirectory);
		txtSelectedDirectory.setColumns(10);
		
		JLabel lblDatabaseAddress = new JLabel("Database Address");
		GridBagConstraints gbc_lblDatabaseAddress = new GridBagConstraints();
		gbc_lblDatabaseAddress.anchor = GridBagConstraints.WEST;
		gbc_lblDatabaseAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatabaseAddress.gridx = 0;
		gbc_lblDatabaseAddress.gridy = 1;
		frmMetadataToSql.getContentPane().add(lblDatabaseAddress, gbc_lblDatabaseAddress);
		
		txtDatabaseAddress = new JTextField();
		GridBagConstraints gbc_txtDatabaseAddress = new GridBagConstraints();
		gbc_txtDatabaseAddress.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDatabaseAddress.insets = new Insets(0, 0, 5, 0);
		gbc_txtDatabaseAddress.gridx = 1;
		gbc_txtDatabaseAddress.gridy = 1;
		frmMetadataToSql.getContentPane().add(txtDatabaseAddress, gbc_txtDatabaseAddress);
		txtDatabaseAddress.setColumns(10);
		
		JLabel lblDatabaseUsername = new JLabel("Database Username");
		GridBagConstraints gbc_lblDatabaseUsername = new GridBagConstraints();
		gbc_lblDatabaseUsername.anchor = GridBagConstraints.WEST;
		gbc_lblDatabaseUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatabaseUsername.gridx = 0;
		gbc_lblDatabaseUsername.gridy = 2;
		frmMetadataToSql.getContentPane().add(lblDatabaseUsername, gbc_lblDatabaseUsername);
		
		txtDatabaseUsername = new JTextField();
		GridBagConstraints gbc_txtDatabaseUsername = new GridBagConstraints();
		gbc_txtDatabaseUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDatabaseUsername.insets = new Insets(0, 0, 5, 0);
		gbc_txtDatabaseUsername.gridx = 1;
		gbc_txtDatabaseUsername.gridy = 2;
		frmMetadataToSql.getContentPane().add(txtDatabaseUsername, gbc_txtDatabaseUsername);
		txtDatabaseUsername.setColumns(10);
		
		JLabel lblDatabasePassword = new JLabel("Database Password");
		GridBagConstraints gbc_lblDatabasePassword = new GridBagConstraints();
		gbc_lblDatabasePassword.anchor = GridBagConstraints.WEST;
		gbc_lblDatabasePassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblDatabasePassword.gridx = 0;
		gbc_lblDatabasePassword.gridy = 3;
		frmMetadataToSql.getContentPane().add(lblDatabasePassword, gbc_lblDatabasePassword);
		
		pwdDatabasePassword = new JPasswordField();
		GridBagConstraints gbc_pwdDatabasePassword = new GridBagConstraints();
		gbc_pwdDatabasePassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_pwdDatabasePassword.insets = new Insets(0, 0, 5, 0);
		gbc_pwdDatabasePassword.gridx = 1;
		gbc_pwdDatabasePassword.gridy = 3;
		frmMetadataToSql.getContentPane().add(pwdDatabasePassword, gbc_pwdDatabasePassword);
		
		JLabel lblSelectOperationMode = new JLabel("Operation Mode");
		GridBagConstraints gbc_lblSelectOperationMode = new GridBagConstraints();
		gbc_lblSelectOperationMode.anchor = GridBagConstraints.WEST;
		gbc_lblSelectOperationMode.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectOperationMode.gridx = 0;
		gbc_lblSelectOperationMode.gridy = 4;
		frmMetadataToSql.getContentPane().add(lblSelectOperationMode, gbc_lblSelectOperationMode);
		
		JPanel pnlOperationMode = new JPanel();
		GridBagConstraints gbc_pnlOperationMode = new GridBagConstraints();
		gbc_pnlOperationMode.anchor = GridBagConstraints.WEST;
		gbc_pnlOperationMode.insets = new Insets(0, 0, 5, 0);
		gbc_pnlOperationMode.gridx = 1;
		gbc_pnlOperationMode.gridy = 4;
		frmMetadataToSql.getContentPane().add(pnlOperationMode, gbc_pnlOperationMode);
		
		rdbtnMusicLibraryToSQL = new JRadioButton("Music Library to SQL");
		rdbtnMusicLibraryToSQL.setSelected(true);
		bgOperationMode.add(rdbtnMusicLibraryToSQL);
		pnlOperationMode.add(rdbtnMusicLibraryToSQL);
		
		rdbtnSqlToMusic = new JRadioButton("SQL to Music Library");
		bgOperationMode.add(rdbtnSqlToMusic);
		pnlOperationMode.add(rdbtnSqlToMusic);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.WEST;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 5;
		frmMetadataToSql.getContentPane().add(panel, gbc_panel);
		
		pnlMusicLibraryToSQLOptions = new JPanel();
		panel.add(pnlMusicLibraryToSQLOptions);
		pnlMusicLibraryToSQLOptions.setBorder(new TitledBorder(null, "Music Library to SQL Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		chckbxLoadSerializedMusic = new JCheckBox("Load Serialized Music Library");
		chckbxLoadSerializedMusic.setHorizontalAlignment(SwingConstants.LEFT);
		chckbxLoadSerializedMusic.setToolTipText("Loads a previously loaded Music Library.  Use this with extreme caution: It can save you time if you know what you're doing, but if you make changes to your database without fully reloading them, then you can overwrite those changes.");
		pnlMusicLibraryToSQLOptions.add(chckbxLoadSerializedMusic);
		
		pnlSQLToMusicLibraryOptions = new JPanel();
		panel.add(pnlSQLToMusicLibraryOptions);
		pnlSQLToMusicLibraryOptions.setBorder(new TitledBorder(null, "SQL To Music Library Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		
		chckbxWriteChanges = new JCheckBox("Write Changes to Library");
		chckbxWriteChanges.setToolTipText("Specifies whether to actually write the changes noted in the DB to the music library.  It's wise to run with this unchecked first to confirm that everything looks good.");
		pnlSQLToMusicLibraryOptions.add(chckbxWriteChanges);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 6;
		frmMetadataToSql.getContentPane().add(scrollPane, gbc_scrollPane);
		
		txtpnOutput = new JTextPane();
		scrollPane.setViewportView(txtpnOutput);
		
		JButton btnGo = new JButton("Go");
		GridBagConstraints gbc_btnGo = new GridBagConstraints();
		gbc_btnGo.gridwidth = 2;
		gbc_btnGo.gridx = 0;
		gbc_btnGo.gridy = 7;
		frmMetadataToSql.getContentPane().add(btnGo, gbc_btnGo);

		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//If the user provided valid input, then initialize and start the proper thread.
				if(validateInput())
				{
					OperationThread mainOperation = null;
					
					//Create the proper OperatinThread depending on the user-selected OperationType
					switch(getSelectedOperationType())
					{
						case MusicLibraryToSql:
							mainOperation = new LibraryImporterThread(Main.getMainGUI(), getSelectedDirectory(), getDBHostName(), getDBUsername(), getDBPassword(), getLoadSerializedLibrary());
							break;
						case SqlToMusicLibrary:
							mainOperation = new LibraryUpdaterThread(Main.getMainGUI(), getSelectedDirectory(), getDBHostName(), getDBUsername(), getDBPassword(), getWriteChanges());
							break;
						default:
							throw new java.lang.UnsupportedOperationException("Unknown OperationType");
					}
					
					//Save the inputs for next time
					try {
						PropertyManager.saveToFile(Main.propertiesLocation, getSelectedDirectory(), getDBHostName(), getDBUsername());
					} catch (IOException e1) {
						// TODO Gracefully handle this exception
						Tools.showExceptionInfo(e1);
					}
					
					//Start the thread
					Thread mainThread = new Thread(mainOperation);
					mainThread.start();
				}
			}
		});

		btnSelectDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				
				//Only allow the selection of directories
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				//If the user successfully selected a directory, then set the reference
				if(fileChooser.showOpenDialog(frmMetadataToSql) == JFileChooser.APPROVE_OPTION)
				{
					mSelectedDirectory = fileChooser.getSelectedFile();
					selectedDirectoryChanged();
				}
			}
		});

		//Add action listeners to the operation mode radio buttons such that they both call operationModeChanged.
		rdbtnMusicLibraryToSQL.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            operationModeChanged();

	        }
	    });
		rdbtnSqlToMusic.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            operationModeChanged();

	        }
	    });
		
		//Setting the action command of the radio buttons so that they can be properly parsed later.
		rdbtnMusicLibraryToSQL.setActionCommand(OperationType.MusicLibraryToSql.toString());
		rdbtnSqlToMusic.setActionCommand(OperationType.SqlToMusicLibrary.toString());

		//Initialize the mode option panels
		operationModeChanged();
		
		//Set the text panel to automatically scroll as text is appended
		DefaultCaret caret = (DefaultCaret)txtpnOutput.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
	
	private void operationModeChanged()
	{
		Tools.setContainerComponentsEnabled(pnlMusicLibraryToSQLOptions, false);
		Tools.setContainerComponentsEnabled(pnlSQLToMusicLibraryOptions, false);
		
		switch(this.getSelectedOperationType())
		{
			case MusicLibraryToSql:
				Tools.setContainerComponentsEnabled(pnlMusicLibraryToSQLOptions, true);
				
				//Enable the checkbox to load serialized music if a serialized library exists
				chckbxLoadSerializedMusic.setEnabled(new File(MusicLibrary.serializationLocation).exists());
				break;
			case SqlToMusicLibrary:
				Tools.setContainerComponentsEnabled(pnlSQLToMusicLibraryOptions, true);
				break;
		}
	}
	
	//TODO disable the "Go" button during operation
	
	private boolean validateInput()
	{
		if(getSelectedDirectory() == null){
			JOptionPane.showMessageDialog(frmMetadataToSql, "You must select a music library directory.");
			return false;
		}
		
		if(getSelectedOperationType() == null){
			JOptionPane.showMessageDialog(frmMetadataToSql, "You must select an operation mode.");
			return false;
		}
		
		if(this.getDBHostName().isEmpty()){
			JOptionPane.showMessageDialog(frmMetadataToSql, "You must enter the database address.");
			return false;
		}
		
		if(this.getDBUsername().isEmpty()){
			JOptionPane.showMessageDialog(frmMetadataToSql, "You must enter the database username.");
			return false;
		}
		
		if(this.getDBPassword().isEmpty()){
			JOptionPane.showMessageDialog(frmMetadataToSql, "You must enter the database password.");
			return false;
		}
		
		return true;
	}
	
	//TODO Show a warning confirmation when the user selects the 'load from serialized library' option
	
	public void setVisible(boolean visible)
	{
		frmMetadataToSql.setVisible(true);
	}
	
	private boolean getLoadSerializedLibrary()
	{
		return chckbxLoadSerializedMusic.isSelected();
	}
	
	private boolean getWriteChanges()
	{
		return chckbxWriteChanges.isSelected();
	}
	
	public JFrame getFrame()
	{
		return frmMetadataToSql;
	}
	
	private String getDBHostName()
	{
		return txtDatabaseAddress.getText();
	}
	
	public void setDBHostName(String pDBHostName)
	{
		txtDatabaseAddress.setText(pDBHostName);
	}
	
	private String getDBUsername()
	{
		return txtDatabaseUsername.getText();
	}
	
	public void setDBUsername(String pDBUsername)
	{
		txtDatabaseUsername.setText(pDBUsername);
	}
	
	private String getDBPassword()
	{
		return new String(pwdDatabasePassword.getPassword());
	}
	
	private File getSelectedDirectory()
	{
		return mSelectedDirectory;
	}
	
	public void setSelectedDirectory(File pDirectory)
	{
		mSelectedDirectory = pDirectory;
		this.selectedDirectoryChanged();
	}
	
	private void selectedDirectoryChanged()
	{
		txtSelectedDirectory.setText(getSelectedDirectory().getAbsolutePath());
	}
	
	//Get the selected ActionCommand of the buttongroup, then parse it to the corresponding OperationType enum
	private OperationType getSelectedOperationType()
	{
		return OperationType.valueOf(bgOperationMode.getSelection().getActionCommand());
	}

	
	//Append a line to the end of the text pane
	public void appendLine(String appendText) throws BadLocationException
	{
		Document doc = txtpnOutput.getStyledDocument();
		
		//Append the provided string to the end of the text pane
		doc.insertString(doc.getLength(), String.format("%s\r\n", appendText), null);
	}

}
