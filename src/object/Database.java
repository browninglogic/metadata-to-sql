package object;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

//The Database class is a layer of abstraction between the application logic and the database.
public class Database implements AutoCloseable {
	
	java.sql.Connection mConnection;
	
	private Database(java.sql.Connection pConnection)
	{
		mConnection = pConnection;
	}

	//Factory connection method
	public static Database connect(String pHostName, String pUsername, String pPassword) throws SQLException
	{
		MysqlDataSource dataSource = new MysqlDataSource();
		java.sql.Connection conn;
		
		dataSource.setUser(pUsername);
		dataSource.setPassword(pPassword);
		dataSource.setServerName(pHostName);
		
		conn = dataSource.getConnection();
		
		return new Database(conn);
	}
	
	public void clearMusicLibrary() throws SQLException
	{
		CallableStatement cs = this.mConnection.prepareCall("{call MusicMeta.ClearMusicLibrary()}");
		cs.executeQuery();
	}
	
	private void insertSong(Song pInsertSong) throws SQLException
	{
		CallableStatement cs = this.mConnection.prepareCall("{call MusicMeta.InsertSong(?, ?, ?, ?, ?, ?, ?)}");
		cs.setString(1, pInsertSong.getTitle());
		cs.setString(2, pInsertSong.getArtist());
		cs.setString(3, pInsertSong.getAlbum());
		cs.setString(4, pInsertSong.getGenre());
		cs.setString(5, pInsertSong.getTrackNumber());
		cs.setString(6, pInsertSong.getYear());
		cs.setString(7, pInsertSong.getFilePath());
		cs.executeQuery();
	}
	
	public void insertLibrary(MusicLibrary pInsertLibrary) throws SQLException
	{
		for(Song loopSong : pInsertLibrary)
		{
			this.insertSong(loopSong);
			Thread.yield();
		}
	}
	
	public ArrayList<Song> getUpdatedSongs() throws SQLException
	{
		ArrayList<Song> updatedSongs = new ArrayList<Song>();
		CallableStatement cs = this.mConnection.prepareCall("{CALL MusicMeta.GetUpdatedSongs()}");
		ResultSet results;
		String title;
		String artist;
		String album;
		String genre;
		String trackNumber;
		String year;
		String filePath;
		
		cs.executeQuery();
		
		results = cs.getResultSet();
		
		while(results.next())
		{
			title = results.getString("TrackTitle");
			artist = results.getString("Artist");
			album = results.getString("Album");
			genre = results.getString("Genre");
			trackNumber = results.getString("TrackNumber");
			year = results.getString("Year");
			filePath = results.getString("FilePath");
			
			updatedSongs.add(new Song(title, artist, album, genre, trackNumber, year, filePath));
		}
		
		return updatedSongs;
	}

	@Override
	public void close() throws Exception {
		mConnection.close();
	}
	
	//TODO Migrate to PostgreSQL
}
