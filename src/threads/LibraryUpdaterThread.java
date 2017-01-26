package threads;

import java.io.File;
import java.util.ArrayList;

import main.MetadataToSqlGui;
import main.Tools;
import object.Database;
import object.Song;
import object.Timer;

public class LibraryUpdaterThread extends OperationThread {
	
	private boolean mWriteChanges;

	public LibraryUpdaterThread(MetadataToSqlGui pMainGUI, File pLibraryRoot, String pDBServer, String pDBUsername,
			String pDBPass, boolean pWriteChanges) {
		super(pMainGUI, pLibraryRoot, pDBServer, pDBUsername, pDBPass);
		
		mWriteChanges = pWriteChanges;
	}

	@Override
	public void run() {
		Timer totalProcessingTime = new Timer();
		Timer operationTimer = new Timer();
		ArrayList<Song> updatedSongs;
		
		try
		{
			this.getMainGUI().appendLine("Loading updated songs from the database...");
			operationTimer.start();
			//Connect to the DB
			try(Database connection = Database.connect(this.getDBServer(), this.getDBUsername(), this.getDBPass()))
			{
				//Load the songs marked as "Updated" from the database
				updatedSongs = connection.getUpdatedSongs();
			}
			operationTimer.stop();
			this.getMainGUI().appendLine(String.format("Operation completed within %s seconds.\r\n\r\n", operationTimer.getTimeSpanInSeconds()));
			
			//Display the updated songs to the user and, if specified, commit the changes
			this.getMainGUI().appendLine(String.format("%s %s songs...\r\n\r\n", mWriteChanges ? "Writing" : "Displaying", updatedSongs.size()));
			operationTimer.start();
			for(Song loopSong : updatedSongs)
			{
				this.getMainGUI().appendLine(loopSong.toString());
				
				if(mWriteChanges)
				{
					loopSong.writeChanges();
				}
			}
			operationTimer.stop();
			this.getMainGUI().appendLine(String.format("Operation completed within %s seconds.\r\n\r\n", operationTimer.getTimeSpanInSeconds()));
		}
		catch(Exception ex)
		{
			Tools.showExceptionInfo(ex);
		}
	}

}
