CREATE TABLE album (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    release_date VARCHAR(10),
    release_date_precision VARCHAR(255),
    restriction VARCHAR(10),
    track_count INTEGER,
    type VARCHAR(11) NOT NULL
)  ENGINE=INNODB;

CREATE TABLE album_availability (
    album_id VARCHAR(64) NOT NULL,
    market_code VARCHAR(2) NOT NULL,
    PRIMARY KEY (album_id , market_code)
)  ENGINE=INNODB;

CREATE TABLE album_genre (
    album_id VARCHAR(64) NOT NULL,
    genre_id BIGINT NOT NULL,
    PRIMARY KEY (album_id , genre_id)
)  ENGINE=INNODB;

CREATE TABLE artist (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
)  ENGINE=INNODB;

CREATE TABLE artist_album (
    artists_id VARCHAR(64) NOT NULL,
    albums_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (artists_id , albums_id)
)  ENGINE=INNODB;

CREATE TABLE artist_track (
    artists_id VARCHAR(64) NOT NULL,
    tracks_id VARCHAR(64) NOT NULL,
    PRIMARY KEY (artists_id , tracks_id)
)  ENGINE=INNODB;

CREATE TABLE copyright (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    album_id VARCHAR(64)
)  ENGINE=INNODB;

CREATE TABLE genre (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    genre VARCHAR(100) NOT NULL
)  ENGINE=INNODB;

CREATE TABLE market (
    code VARCHAR(2) NOT NULL PRIMARY KEY
)  ENGINE=INNODB;

CREATE TABLE track (
    id VARCHAR(64) NOT NULL PRIMARY KEY,
    disc_number INTEGER NOT NULL,
    duration INTEGER NOT NULL,
    explicit BIT NOT NULL,
    name VARCHAR(255) NOT NULL,
    restriction VARCHAR(10),
    track_number INTEGER NOT NULL,
    album_id VARCHAR(64) NOT NULL
)  ENGINE=INNODB;

CREATE TABLE track_availability (
    track_id VARCHAR(64) NOT NULL,
    market_code VARCHAR(2) NOT NULL,
    PRIMARY KEY (track_id , market_code)
)  ENGINE=INNODB;

ALTER TABLE market ADD CONSTRAINT uk_market_code UNIQUE (code);

ALTER TABLE album_availability ADD CONSTRAINT fk_album_availability_market_code FOREIGN KEY (market_code) REFERENCES market (code);
ALTER TABLE album_availability ADD CONSTRAINT fk_album_availability_album_id FOREIGN KEY (album_id) REFERENCES album (id);
ALTER TABLE album_genre ADD CONSTRAINT fk_album_genre_genre_id FOREIGN KEY (genre_id) REFERENCES genre (id);
ALTER TABLE album_genre ADD CONSTRAINT fk_album_genre_album_id FOREIGN KEY (album_id) REFERENCES album (id);
ALTER TABLE artist_album ADD CONSTRAINT fk_artist_album_albums_id FOREIGN KEY (albums_id) REFERENCES album (id);
ALTER TABLE artist_album ADD CONSTRAINT fk_artist_album_artists_id FOREIGN KEY (artists_id) REFERENCES artist (id);
ALTER TABLE artist_track ADD CONSTRAINT fk_artist_track_tracks_id FOREIGN KEY (tracks_id) REFERENCES track (id);
ALTER TABLE artist_track ADD CONSTRAINT fk_artist_track_artists_id FOREIGN KEY (artists_id) REFERENCES artist (id);
ALTER TABLE copyright ADD CONSTRAINT fk_copyright_album_id FOREIGN KEY (album_id) REFERENCES album (id);
ALTER TABLE track ADD CONSTRAINT fk_track_album_id FOREIGN KEY (album_id) REFERENCES album (id);
ALTER TABLE track_availability ADD CONSTRAINT fk_track_availability_market_code FOREIGN KEY (market_code) REFERENCES market (code);
ALTER TABLE track_availability ADD CONSTRAINT fk_track_availability_track_id FOREIGN KEY (track_id) REFERENCES track (id);