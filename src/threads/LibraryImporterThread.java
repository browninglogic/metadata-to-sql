package threads;

import java.io.File;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import main.MetadataToSqlGui;
import main.Tools;
import object.Database;
import object.MusicLibrary;
import object.Timer;

public class LibraryImporterThread extends OperationThread {
	
	private boolean mLoadSerializedLibrary;

	public LibraryImporterThread(MetadataToSqlGui pMainGUI, File pLibraryRoot, String pDBServer, String pDBUsername,
			String pDBPass, boolean pLoadSerializedLibrary) {
		super(pMainGUI, pLibraryRoot, pDBServer, pDBUsername, pDBPass);
		
		mLoadSerializedLibrary = pLoadSerializedLibrary;
	}

	@Override
	public void run() {
		Timer totalProcessingTime = new Timer();
		Timer operationTimer = new Timer();
		MusicLibrary library;
		
		try
		{
			if(!mLoadSerializedLibrary)
			{
				//Load the music library from disk, measuring and displaying the amount of time taken.
				this.getMainGUI().appendLine("Loading music library from disk...");
				operationTimer.start();
				library = MusicLibrary.LoadFromDirectory(this.getLibraryRoot());
				operationTimer.stop();
				this.getMainGUI().appendLine(String.format("Operation completed within %s minutes.\r\n\r\n", operationTimer.getTimeSpanInMinutes()));
				
				//Once the library has been loaded, serialize it so that it can be loaded more quickly in the future, with proper care, if the user chooses to do so.
				this.getMainGUI().appendLine("Serializing freshly loaded music library...");
				operationTimer.start();
				library.serialize();
				operationTimer.stop();
				this.getMainGUI().appendLine(String.format("Operation completed within %s seconds.\r\n\r\n", operationTimer.getTimeSpanInSeconds()));
			}
			else
			{
				//If the user tells us to do so, then load the previously-serialized library directly
				this.getMainGUI().appendLine("Loading serialized music library...");
				operationTimer.start();
				library = MusicLibrary.deserialize();
				operationTimer.stop();
				this.getMainGUI().appendLine(String.format("Operation completed within %s seconds.\r\n\r\n", operationTimer.getTimeSpanInSeconds()));
			}
			
			//Connect to the DB
			try(Database connection = Database.connect(this.getDBServer(), this.getDBUsername(), this.getDBPass()))
			{
				//Truncate the existing library data
				this.getMainGUI().appendLine("Clearing Music Library Table...");
				operationTimer.start();
				connection.clearMusicLibrary();
				operationTimer.stop();
				this.getMainGUI().appendLine(String.format("Operation completed within %s seconds.\r\n\r\n", operationTimer.getTimeSpanInSeconds()));

				//loop through the MusicLibrary and write each song to the DB
				this.getMainGUI().appendLine(String.format("Writing %s songs to the database...", library.getSongCount()));
				operationTimer.start();
				connection.insertLibrary(library);
				operationTimer.stop();
				this.getMainGUI().appendLine(String.format("Operation completed within %s minutes.\r\n\r\n", operationTimer.getTimeSpanInMinutes()));
			}
			
			totalProcessingTime.stop();
			this.getMainGUI().appendLine(String.format("Total processing time: %s minutes.\r\n\r\n", totalProcessingTime.getTimeSpanInMinutes()));
			
		}
		catch(Exception ex)
		{
			Tools.showExceptionInfo(ex);
		}
		
	}

}
