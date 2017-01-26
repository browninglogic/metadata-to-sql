CREATE DATABASE `MusicMeta` /*!40100 DEFAULT CHARACTER SET latin1 */;

CREATE TABLE `MusicLibraryMetadata` (
  `SongID` int(11) NOT NULL AUTO_INCREMENT,
  `TrackTitle` varchar(1000) DEFAULT NULL,
  `Artist` varchar(1000) DEFAULT NULL,
  `Album` varchar(1000) DEFAULT NULL,
  `Genre` varchar(1000) DEFAULT NULL,
  `TrackNumber` varchar(10) DEFAULT NULL,
  `Year` varchar(10) DEFAULT NULL,
  `Updated` bit(1) NOT NULL DEFAULT b'0',
  `FilePath` varchar(1000) NOT NULL,
  `InsertedTimestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`SongID`)
) ENGINE=InnoDB AUTO_INCREMENT=61032 DEFAULT CHARSET=latin1;

DELIMITER $$
CREATE DEFINER=`dev`@`%` PROCEDURE `ClearMusicLibrary`()
BEGIN
	#This procedure completely clears the music library table for repopulation
	TRUNCATE TABLE MusicMeta.MusicLibraryMetadata;
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`dev`@`%` PROCEDURE `InsertSong`(
	In TrackTitle VARCHAR(1000),
	In Artist VARCHAR(1000),
	In Album VARCHAR(1000),
	In Genre VARCHAR(1000),
	In TrackNumber VARCHAR(10),
	In `Year` VARCHAR(10),
	In FilePath VARCHAR(1000)
)
BEGIN
	INSERT INTO MusicMeta.MusicLibraryMetadata (TrackTitle, Artist, Album, Genre, TrackNumber, `Year`, FilePath, Updated, InsertedTimeStamp)
	VALUES (TrackTitle, Artist, Album, Genre, TrackNumber, `Year`, FilePath, 0, NOW());
END$$
DELIMITER ;

DELIMITER $$
CREATE DEFINER=`dev`@`%` PROCEDURE `GetUpdatedSongs`()
BEGIN
SELECT *
FROM MusicMeta.MusicLibraryMetadata
WHERE Updated = 1;
END$$
DELIMITER ;
