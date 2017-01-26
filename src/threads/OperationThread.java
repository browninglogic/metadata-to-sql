package threads;

import java.io.File;

import main.MetadataToSqlGui;

public abstract class OperationThread implements Runnable {
	
	private MetadataToSqlGui mMainGUI;
	private File mLibraryRoot;
	private String mDBServer;
	private String mDBUsername;
	private String mDBPass;
	
	protected OperationThread(MetadataToSqlGui pMainGUI, File pLibraryRoot, String pDBServer, String pDBUsername, String pDBPass)
	{
		mMainGUI = pMainGUI;
		mLibraryRoot = pLibraryRoot;
		mDBServer = pDBServer;
		mDBUsername = pDBUsername;
		mDBPass = pDBPass;
	}
	
	protected File getLibraryRoot()
	{
		return mLibraryRoot;
	}
	
	protected String getDBServer()
	{
		return mDBServer;
	}
	
	protected String getDBUsername()
	{
		return mDBUsername;
	}
	
	protected String getDBPass()
	{
		return mDBPass;
	}
	
	protected MetadataToSqlGui getMainGUI()
	{
		return mMainGUI;
	}

}
