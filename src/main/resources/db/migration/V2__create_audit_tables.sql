CREATE TABLE revision_info (
    revision_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    revision_timestamp BIGINT(20) NOT NULL,
    user VARCHAR(50) NOT NULL
);

CREATE TABLE album_audit (
    id VARCHAR(64) NOT NULL,
    name VARCHAR(255) NOT NULL,
    release_date VARCHAR(10),
    release_date_precision VARCHAR(255),
    restriction VARCHAR(10),
    track_count INTEGER,
    type VARCHAR(11) NOT NULL,
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , id),
    CONSTRAINT fk_album_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;

CREATE TABLE album_availability_audit (
    album_id VARCHAR(64) NOT NULL,
    market_code VARCHAR(2) NOT NULL,
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , album_id , market_code),
    CONSTRAINT fk_album_availability_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;

CREATE TABLE album_copyright_audit (
    album_id VARCHAR(64) NOT NULL,
    id BIGINT NOT NULL,
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , album_id , id),
    CONSTRAINT fk_album_copyright_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;

CREATE TABLE album_genre_audit (
    album_id VARCHAR(64) NOT NULL,
    genre_id BIGINT NOT NULL,
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , album_id , genre_id),
    CONSTRAINT fk_album_genre_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;

CREATE TABLE artist_audit (
    id VARCHAR(64) NOT NULL,
    name VARCHAR(255) NOT NULL,
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , id),
    CONSTRAINT fk_artist_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;

CREATE TABLE artist_album_audit (
    artists_id VARCHAR(64) NOT NULL,
    albums_id VARCHAR(64) NOT NULL,
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , artists_id , albums_id),
    CONSTRAINT fk_artist_album_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;

CREATE TABLE artist_track_audit (
    artists_id VARCHAR(64) NOT NULL,
    tracks_id VARCHAR(64) NOT NULL,
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , artists_id , tracks_id),
    CONSTRAINT fk_artist_track_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;

CREATE TABLE copyright_audit (
    id BIGINT NOT NULL,
    text VARCHAR(255) NOT NULL,
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , id),
    CONSTRAINT fk_copyright_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;

CREATE TABLE genre_audit (
    id BIGINT NOT NULL,
    genre VARCHAR(100),
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , id),
    CONSTRAINT fk_genre_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;

CREATE TABLE market_audit (
    code VARCHAR(2) NOT NULL,
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , code),
    CONSTRAINT fk_market_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;

CREATE TABLE track_audit (
    id VARCHAR(64) NOT NULL,
    disc_number INTEGER NOT NULL,
    duration INTEGER NOT NULL,
    explicit BIT NOT NULL,
    name VARCHAR(255) NOT NULL,
    restriction VARCHAR(10),
    track_number INTEGER NOT NULL,
    album_id VARCHAR(64) NOT NULL,
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , id),
    CONSTRAINT fk_track_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;

CREATE TABLE track_availability_audit (
    track_id VARCHAR(64) NOT NULL,
    market_code VARCHAR(2) NOT NULL,
    revision_id BIGINT NOT NULL,
    revision_type TINYINT NOT NULL,
    PRIMARY KEY (revision_id , track_id , market_code),
    CONSTRAINT fk_track_availability_revinfo_rev_id FOREIGN KEY (revision_id)
        REFERENCES revision_info (revision_id)
)  ENGINE=INNODB;
