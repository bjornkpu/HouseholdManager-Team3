
-- #DROP: Tables

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS login;
DROP TABLE IF EXISTS bloodreading;
DROP TABLE IF EXISTS sport;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS athlete_location;
DROP TABLE IF EXISTS log;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE sport (
  sport_id   INTEGER     NOT NULL AUTO_INCREMENT,
  sport_name VARCHAR(63) NOT NULL UNIQUE,

  CONSTRAINT sport_pk PRIMARY KEY (sport_id)
);

CREATE TABLE user (
  user_id     INTEGER NOT NULL AUTO_INCREMENT,
  user_type   ENUM('Athlete', 'Analyst', 'Blood Collector') NOT NULL,
  fname       VARCHAR(63),
  lname       VARCHAR(63),
  sport_id    INTEGER,
  nationality VARCHAR(63),
  phone       VARCHAR(16) UNIQUE,
  sex         ENUM('Male', 'Female'),

  CONSTRAINT user_pk PRIMARY KEY (user_id),
  CONSTRAINT user_fk FOREIGN KEY (sport_id) REFERENCES sport(sport_id)
);

CREATE TABLE login (
  user_id        INTEGER      NOT NULL,
  username       VARCHAR( 63) NOT NULL UNIQUE,
  password_token VARCHAR(128) NOT NULL,

  CONSTRAINT login_pk PRIMARY KEY (user_id),
  CONSTRAINT login_fk FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE bloodreading (
	reading_time DATETIME NOT NULL,
	hgblevel     DOUBLE   NOT NULL,
	user_id      INTEGER  NOT NULL,
	CONSTRAINT bloodreading_pk PRIMARY KEY (reading_time, user_id),
	CONSTRAINT bloodreading_fk FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE location (
	loc_lat  DOUBLE NOT NULL,
  loc_long DOUBLE NOT NULL,
	loc_alt  DOUBLE NOT NULL,
  loc_name VARCHAR(255),
  CONSTRAINT location_pk PRIMARY KEY (loc_lat, loc_long)
);

CREATE TABLE athlete_location (
	user_id   INTEGER NOT NULL,
	loc_lat   DOUBLE  NOT NULL,
  loc_long  DOUBLE  NOT NULL,
	from_date DATE    NOT NULL,
	to_date   DATE    NOT NULL,
	CONSTRAINT athlete_location_pk  PRIMARY KEY (user_id, loc_lat, loc_long, from_date),
	CONSTRAINT athlete_location_fk1 FOREIGN KEY (user_id) REFERENCES user    (user_id),
	CONSTRAINT athlete_location_fk2 FOREIGN KEY (loc_lat, loc_long) REFERENCES location(loc_lat, loc_long)
);


CREATE TABLE log (
  log_id   INTEGER NOT NULL AUTO_INCREMENT,
  issuer   VARCHAR(127) NOT NULL,
  log_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  query    TEXT NOT NULL,
  result   TEXT NOT NULL,

  CONSTRAINT log_pk PRIMARY KEY (log_id)
);

# INSERT

#INSERT INTO user(user_type, fname, lname) VALUES ('Analyst', 'Ass', 'McTits');
