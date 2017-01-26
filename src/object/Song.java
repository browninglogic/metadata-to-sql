package object;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import main.Tools;

public class Song implements Serializable, Comparable <Song>{

	private static final long serialVersionUID = 2;
	
	private String mTitle;
	private String mArtist;
	private String mAlbum;
	private String mGenre;
	private String mTrackNumber;
	private String mYear;
	private String mFilePath;
	
	public Song(String pTitle, String pArtist, String pAlbum, String pGenre, String pTrackNumber, String pYear, String pFilePath)
	{
		mTitle = pTitle;
		mArtist = pArtist;
		mAlbum = pAlbum;
		mGenre = pGenre;
		mTrackNumber = pTrackNumber;
		mYear = pYear;
		mFilePath = pFilePath;
	}
	
	//Factory method for the construction of a Song object, given the file itself
	public static Song ReadFromFile(File pSongFile) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
	{
		String filePath;
		AudioFile audioFile;
		Tag audioTag;
		
		String title;
		String artist;
		String album;
		String genre;
		String trackNumber;
		String year;
		
		//Read the audio file from disk, then get the tag info
		audioFile = AudioFileIO.read(pSongFile);
		audioTag = audioFile.getTag();
		filePath = pSongFile.getAbsolutePath();
		
		//Read the tags from the file
		title = Song.getTagKeyValue(audioTag, filePath, FieldKey.TITLE);
		artist = Song.getTagKeyValue(audioTag, filePath, FieldKey.ARTIST);
		album = Song.getTagKeyValue(audioTag, filePath, FieldKey.ALBUM);
		genre = Song.getTagKeyValue(audioTag, filePath, FieldKey.GENRE);
		trackNumber = Song.getTagKeyValue(audioTag, filePath, FieldKey.TRACK);
		year = Song.getTagKeyValue(audioTag, filePath, FieldKey.YEAR);
		
		return new Song(title, artist, album, genre, trackNumber, year, filePath);
	}
	
	public void writeChanges() throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException, CannotWriteException
	{
		//Read the file, song, and tag from file
		File songFile = new File(this.getFilePath());
		AudioFile audioFile = AudioFileIO.read(songFile);
		Tag audioTag = audioFile.getTag();
		
		//Write the tag fields in the file from the fields contained in the Song object
		Song.setTagKeyValue(audioTag, FieldKey.TITLE, this.getTitle());
		Song.setTagKeyValue(audioTag, FieldKey.ARTIST, this.getArtist());
		Song.setTagKeyValue(audioTag, FieldKey.ALBUM, this.getAlbum());
		Song.setTagKeyValue(audioTag, FieldKey.GENRE, this.getGenre());
		Song.setTagKeyValue(audioTag, FieldKey.TRACK, this.getTrackNumber());
		Song.setTagKeyValue(audioTag, FieldKey.YEAR, this.getYear());
		
		//Commit the changes
		audioFile.commit();
	}
	
	private static void setTagKeyValue(Tag pSongTag, FieldKey pTagKey, String pValue) throws KeyNotFoundException, FieldDataInvalidException
	{
		//If there was a non-null value provided, then either set or add the tag, depending on whether it already exists in the file
		if(pValue != null)
		{
			if(pSongTag.hasField(pTagKey))
			{
				pSongTag.setField(pTagKey, pValue);
			}
			else
			{
				pSongTag.addField(pTagKey, pValue);
			}
		}
		//If a null value was passed, but the tag field exists, then delete the tag field
		else
		{
			if(pSongTag.hasField(pTagKey))
			{
				pSongTag.deleteField(pTagKey);
			}
		}
	}
	
	//TODO Add support for the total tracks field, unsynced lyrics, and the relevant musicbrainz id fields
	
	private static String getTagKeyValue(Tag pSongTag, String pFilePath, FieldKey pTagKey)
	{

		List<String> tagMatches = pSongTag.getAll(pTagKey);
		
		switch(tagMatches.size())
		{
			//If the file has no tags for the field, then we don't want to create a blank one, so return null.
			case 0: return null;
			//This should the the standard case.  A single tag exists for this field, so return it.
			case 1: return tagMatches.get(0);
			//We don't support dealing with multiple tags per field, so throw an Exception if we come across one.
			default:
				throw new UnsupportedOperationException(String.format("%s has %s tags for %s.", pFilePath, tagMatches.size(), pTagKey.toString()));
		}
	}
	
	//TODO Add functionality to tell which files have multiple tags for a field.
	
	public String toString()
	{
		String songString = "";
		
		songString += String.format("Title: %s |", this.getTitle() != null ? this.getTitle() : "[missing]");
		songString += String.format("Artist: %s |", this.getArtist() != null ? this.getArtist() : "[missing]");
		songString += String.format("Album: %s |", this.getAlbum() != null ? this.getAlbum() : "[missing]");
		songString += String.format("Genre: %s |", this.getGenre() != null ? this.getGenre() : "[missing]");
		songString += String.format("Track Number: %s |", this.getTrackNumber() != null ? this.getTrackNumber() : "[missing]");
		songString += String.format("Year: %s |", this.getYear() != null ? this.getYear() : "[missing]");
		
		return songString;
	}
	
	public String getTitle()
	{
		return mTitle;
	}
	
	public String getArtist()
	{
		return mArtist;
	}
	
	public String getAlbum()
	{
		return mAlbum;
	}
	
	public String getGenre()
	{
		return mGenre;
	}
	
	public String getTrackNumber()
	{
		return mTrackNumber;
	}
	
	public String getYear()
	{
		return mYear;
	}
	
	public String getFilePath()
	{
		return mFilePath;
	}

	@Override
	public int compareTo(Song o) {
		//Compare by artist, then year, then album, then tracknumber
		int compareByArtist = Tools.convertNullString(this.getArtist()).compareToIgnoreCase(Tools.convertNullString(o.getArtist()));
		if(compareByArtist != 0)
			return compareByArtist;
		
		int compareByYear = Integer.compare(Tools.TryParseInt(this.getYear(), -1), Tools.TryParseInt(o.getYear(), -1));
		if(compareByYear != 0)
			return compareByYear;
		
		int compareByAlbum = Tools.convertNullString(this.getAlbum()).compareToIgnoreCase(Tools.convertNullString(o.getAlbum()));
		if(compareByAlbum != 0)
			return compareByAlbum;
		
		int compareByTrackNumber = Integer.compare(Tools.TryParseInt(this.getTrackNumber(), -1),  Tools.TryParseInt(o.getTrackNumber(), -1));
		if(compareByTrackNumber != 0)
			return compareByTrackNumber;
		
		return 0;
	}
}
