package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import object.Database;

public class PropertyManager {
	private static final String libraryDirectoryKey = "libraryDirectory";
	private static final String dbHostNameKey = "dbHostName";
	private static final String dbUsernameKey = "dbUsername";
	
	private Properties mProperties;
	
	private PropertyManager(Properties pProperties)
	{
		mProperties = pProperties;
	}
	
	public static PropertyManager loadFromFile(String pFilePath) throws IOException
	{
		Properties loadingProperties = new Properties();

		try(FileInputStream in = new FileInputStream(new File(pFilePath)))
		{
			loadingProperties.load(in);
		}
		
		return new PropertyManager(loadingProperties);
	}
	
	public static void saveToFile(String pFilePath, File pLibraryDirectory, String pDBHostName, String pDBUsername) throws FileNotFoundException, IOException
	{
		Properties savingProperties = new Properties();
		
		savingProperties.setProperty(PropertyManager.libraryDirectoryKey, pLibraryDirectory.getAbsolutePath());
		savingProperties.setProperty(PropertyManager.dbHostNameKey, pDBHostName);
		savingProperties.setProperty(PropertyManager.dbUsernameKey, pDBUsername);
		
		try(FileOutputStream out = new FileOutputStream(new File(pFilePath)))
		{
			savingProperties.store(out, "metadata-to-sql config");
		}
	}
	
	public File getLibraryDirectory()
	{
		String path = mProperties.getProperty(PropertyManager.libraryDirectoryKey);
		File pathFile = new File(path);
		
		if(pathFile.exists() && pathFile.isDirectory())
		{
			return pathFile;
		}
		
		return null;
	}
	
	public String getDBHostName()
	{
		return mProperties.getProperty(PropertyManager.dbHostNameKey);
	}
	
	public String getDBUsername()
	{
		return mProperties.getProperty(PropertyManager.dbUsernameKey);
	}
	
	//TODO Create a high-level reusable abstract class for this and migrate it to tools, then extend it with the necessary functionality here
}
