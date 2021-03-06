ALTER TABLE album_availability DROP FOREIGN KEY fk_album_availability_market_id;
ALTER TABLE album_availability DROP FOREIGN KEY fk_album_availability_album_id;
ALTER TABLE album_genre DROP FOREIGN KEY fk_album_genre_genre;
ALTER TABLE album_genre DROP FOREIGN KEY fk_album_genre_album_id;
ALTER TABLE artist_album DROP FOREIGN KEY fk_artist_album_albums_id;
ALTER TABLE artist_album DROP FOREIGN KEY fk_artist_album_artists_id;
ALTER TABLE artist_track DROP FOREIGN KEY fk_artist_track_tracks_id;
ALTER TABLE artist_track DROP FOREIGN KEY fk_artist_track_artists_id;
ALTER TABLE copyright DROP FOREIGN KEY fk_copyright_album_id;
ALTER TABLE track DROP FOREIGN KEY fk_track_album_id;
ALTER TABLE track_availability DROP FOREIGN KEY fk_track_availability_market_id;
ALTER TABLE track_availability DROP FOREIGN KEY fk_track_availability_track_id;

DROP TABLE IF EXISTS album;
DROP TABLE IF EXISTS album_availability;
DROP TABLE IF EXISTS album_genre;
DROP TABLE IF EXISTS artist;
DROP TABLE IF EXISTS artist_album;
DROP TABLE IF EXISTS artist_track;
DROP TABLE IF EXISTS copyright;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS market;
DROP TABLE IF EXISTS track;
DROP TABLE IF EXISTS track_availability;

DROP TABLE IF EXISTS album_audit;
DROP TABLE IF EXISTS album_availability_audit;
DROP TABLE IF EXISTS album_copyright_audit;
DROP TABLE IF EXISTS album_genre_audit;
DROP TABLE IF EXISTS artist_audit;
DROP TABLE IF EXISTS artist_album_audit;
DROP TABLE IF EXISTS artist_track_audit;
DROP TABLE IF EXISTS copyright_audit;
DROP TABLE IF EXISTS genre_audit;
DROP TABLE IF EXISTS market_audit;
DROP TABLE IF EXISTS track_audit;
DROP TABLE IF EXISTS track_availability_audit;

DROP TABLE IF EXISTS revision_info;