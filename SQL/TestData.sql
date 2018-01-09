
# INSERT INTO sport(sport_name) VALUES( 'Football');

# INSERT INTO user(user_type, fname, lname) VALUES ('Blood Collector', 'Vlad', 'Dracula');

# INSERT INTO login(user_id, username, password_token) VALUES ('44', 'nosferatu', '')

# INSERT INTO user(user_type, fname, lname, sport_id, nationality, phone, sex)
# VALUES ('Athlete', 'BK', 'Punsvik', 1,  'Norway', 97568091, 'Male');

# INSERT INTO user(user_type, fname, lname, sport_id, nationality, phone, sex)
# VALUES ('Athlete', 'Eirik', 'Oseberg', 2,  'Sweden', 84759683, 'Female');

# INSERT INTO bloodreading(reading_time, hgblevel, user_id) VALUE ('20170203', 13, 1);

# /* BLOODREADINGS TESTDATA */
# INSERT INTO bloodreading(reading_time, hgblevel, user_id) VALUE ('20170101', 11, 43);

/*LOCATION TESTDATA*/
# INSERT INTO location(loc_lat, loc_long, loc_alt, loc_name) VALUES (100.0, 100.0, 50, "Fjellhamar");

/*ATHLETE_LOCATION TESTDATA*/
# INSERT INTO athlete_location(user_id, loc_lat, loc_long, from_date, to_date) VALUES (3, 100.0, 100.0, "20070201", "20070220");

/*IMPLEMENTERE FUNKSJON TESTDATA*/
-- # INSERT INTO bloodreading(reading_time, hgblevel, user_id) VALUES ()


/*FREMFØRING TESTDATA*/

INSERT INTO user( user_type, fname, lname, nationality, phone, sex )
VALUES
  ('Athlete',  'Emma',  'Royds',  'USA','+14124963462',  'Female'),
  ('Athlete',  'Mutubu',  'Ngola',  'Ghana', '+23341212362',  'Male'),
  ('Athlete',  'Pernilla',  'Lindholm',  'Sweden',  '+46132536462',  'Female'),
  ('Athlete',  'Holger',  'Hjort',  'Denmark', '+45424963462',  'Male'),
  ('Athlete',  'Radmila',  'Janeckovà',  'Czech Republic',  '+4204124963462',  'Female'),
  ('Athlete',  'Pan',  'Yeong-Gi',  'South Korea',  '+82124963462',  'Male'),
  ('Athlete',  'Janar',  'Philak',  'Estonia',  '+372453345462',  'Male'),
  ('Athlete',  'Dimitrij',  'Salakovski',  'Russia',  '+7124342141231',  'Male'),
  ('Athlete',  'Alda',  'Thorleifurdottir',  'Iceland',  '+3541245342111',  'Female'),
  ('Athlete',  'Dan',  'Schalenbourg',  'Belgium',  '+321242963412',  'Male'),
  ('Athlete',  'Pan',  'Yeong-Gi',  'South Korea',  '+8212412563332',  'Male'),
  ('Athlete',  'Trees',  'Voordenhout',  'Nederlands',  '+31432532898',  'Female'),
  ('Athlete',  'Jaap',  'Van der Streijn',  'Netherlands',  '+311241242362',  'Male'),
  ('Athlete',  'Song',  'Xiu',  'China',  '+86124124122',  'Female'),
  ('Athlete',  'Selamawit',  'Kiros Abdella',  'Ethiopia',  '+2542187412',  'Female'),
  ('Athlete',  'Marta',  'Sokolowska',  'Poland',  '+481247453431',  'Female'),
  ('Athlete',  'Reginaldo',  'Bosco Vargas',  'Brazil',  '+554124512',  'Male'),
  ('Athlete',  'Ana',  'Correlia Barroso',  'Brazil',  '+554441255542',  'Female'),
  ('Athlete',  'Aurelia',  'Lenci',  'Italy',  '+39124963462',  'Female'),
  ('Athlete',  'Glebova',  'Vitamovna',  'Russia',  '+7124213462',  'Female'),

  ('Analyst',  'Christoph',  'Battier',  'France',  '+334454213',  'Male'),
  ('Analyst',  'Ana',  'Correlia Barroso',  'Brazil',  '+55442577742',  'Female'),
  ('Analyst',  'Ogura',  'Kazuma',  'Japan',  '+81442542132',  'Male'),

  ('Blood Collector',  'Dolorès',  'Barbier',  'France',  '+334724132',  'Female'),
  ('Blood Collector',  'Sven',  'Ammermann',  'Germany',  '+4912413598483',  'Male'),
  ('Blood Collector',  'Miroslav',  'Yakimov',  'Russia',  '+7442521322',  'Male'),
  ('Blood Collector',  'Nara',  'Chisato',  'Japan',  '+8152412442',  'Female'),
  ('Blood Collector',  'Ezven',  'Levas',  'Greece',  '+30442345742',  'Male');

