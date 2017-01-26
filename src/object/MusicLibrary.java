package object;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import main.Tools;

public class MusicLibrary implements Serializable, Iterable<Song> {

	private ArrayList<Song> mSongs;
	
	//For consistency and simplicity, we always want to serialize/deserialize to/from the same location
	public static final String serializationLocation = "sav/musicLibrary.ser";
	
	private static final long serialVersionUID = 2;
	
	private MusicLibrary(ArrayList<Song> pSongs)
	{
		mSongs = pSongs;
	}
	
	//TODO Migrate MusicLibrary and Song to a separate project to be used as a shared library, so that it can be expanded and reused in other applications, such as a cover art fetcher/embedder.
	
	public int getSongCount()
	{
		return mSongs.size();
	}
	
	public Song getSong(int pIndex)
	{
		return mSongs.get(pIndex);
	}
	
	public static MusicLibrary LoadFromDirectory(File pLibraryDirectory) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
	{
		ArrayList<Song> songs = new ArrayList<Song>();
		File[] libraryDirectoryFiles;
	
		//Get all files (excluding folders) within the provided directory and all subdirectories, so that we can easily loop through them.
		libraryDirectoryFiles = Tools.findAllFilesInDirectory(pLibraryDirectory);
		
		//Loop through all files in the directory and process accordingly
		for(File loopFile : libraryDirectoryFiles)
		{
			//Exclude .jpg & .tmp explicitly now, until configurable excluded filetypes have been implemented.
			if(!loopFile.getName().endsWith(".jpg") && !loopFile.getName().endsWith(".tmp"))
			{
				songs.add(Song.ReadFromFile(loopFile));
			}
			
			Thread.yield();
		}
		
		Collections.sort(songs);
		
		
		return new MusicLibrary(songs);
	}
	
	//TODO Write functionality for configurable included file types, to be loaded from a config file
	
	//Serialize the object
	public void serialize() throws IOException
	{
		FileOutputStream fileOut = new FileOutputStream(MusicLibrary.serializationLocation);
		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		
		objectOut.writeObject(this);
		objectOut.close();
		fileOut.close();
	}
	
	//TODO Implement error handling so that if the file is not found, we gracefully inform the user
	
	//Simple serialized load
	public static MusicLibrary deserialize() throws IOException, ClassNotFoundException
	{
		FileInputStream fileIn = new FileInputStream(MusicLibrary.serializationLocation);
		ObjectInputStream objectIn = new ObjectInputStream(fileIn);
		MusicLibrary reading;
		
		reading = (MusicLibrary) objectIn.readObject();
		
		objectIn.close();
		fileIn.close();
		
		return reading;
	}

	//Provide an iterator so that we can use Java's built-in for each loop over the library
	@Override
	public Iterator<Song> iterator() {
		return mSongs.iterator();
	}
	
}
