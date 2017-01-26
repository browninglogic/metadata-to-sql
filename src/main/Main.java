package main;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import object.MusicLibrary;


public class Main {
	
	public static final String propertiesLocation = "sav/properties.cfg";

	private static MetadataToSqlGui mMainGUI;
	private static PropertyManager loadedInputs = null;
	
	public static MetadataToSqlGui getMainGUI()
	{
		return mMainGUI;
	}

	public static void main(String[] args) {
		
		//If there's a saved properties file, then load it
		if(new File(Main.propertiesLocation).exists())
		{
			try {
				loadedInputs = PropertyManager.loadFromFile(Main.propertiesLocation);
			} catch (IOException e) {
				//TODO Gracefully handle this exception
				Tools.showExceptionInfo(e);
			}
		}
		
		//Launch the GUI and pass any necessary information to it.
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mMainGUI = new MetadataToSqlGui();
					
					if(loadedInputs != null)
					{
						mMainGUI.setSelectedDirectory(loadedInputs.getLibraryDirectory());
						mMainGUI.setDBHostName(loadedInputs.getDBHostName());
						mMainGUI.setDBUsername(loadedInputs.getDBUsername());
					}
					
					mMainGUI.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	//TODO Organize the packages and classes in a more organized way

}
