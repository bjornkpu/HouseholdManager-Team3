SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS item_shoppinglist;
DROP TABLE IF EXISTS user_disbursement;
DROP TABLE IF EXISTS wallpost;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS chore_log;
DROP TABLE IF EXISTS chore;
DROP TABLE IF EXISTS shoppinglist_user;
DROP TABLE IF EXISTS shoppinglist;
DROP TABLE IF EXISTS user_party;
DROP TABLE IF EXISTS disbursement;
DROP TABLE IF EXISTS party;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS user;

SET FOREIGN_KEY_CHECKS = 1;
-- Oppretter tabeller med entitetsintegritet (primærnøkkel)

CREATE TABLE user(
  name VARCHAR(255),
  email VARCHAR(255) NOT NULL,
  password CHAR(64)NOT NULL,
  salt VARCHAR(20) NOT NULL,
  phone VARCHAR (15),
  CONSTRAINT user_pk PRIMARY KEY(email));

CREATE TABLE notification(
  id INTEGER (10) AUTO_INCREMENT,
  user_email VARCHAR(255) NOT NULL,
  text VARCHAR(255) NOT NULL,
  time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  seen INTEGER DEFAULT 0,
  CONSTRAINT notification_pk PRIMARY KEY(id)
);

CREATE TABLE party(
  id INTEGER NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  CONSTRAINT party_pk PRIMARY KEY(id));

CREATE TABLE wallpost(
  id INTEGER AUTO_INCREMENT,
  time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  message VARCHAR (255),
  party_id INTEGER (10) NOT NULL ,
  user_email VARCHAR(255) NOT NULL ,
  CONSTRAINT wallpost_pk PRIMARY KEY(id));

CREATE TABLE user_party(
  user_email VARCHAR(255) NOT NULL,
  party_id INTEGER NOT NULL,
  balance DOUBLE NOT NULL,
  status INTEGER(1),
  CONSTRAINT user_party_pk PRIMARY KEY(user_email,party_id));

CREATE TABLE chore(
  id INTEGER NOT NULL AUTO_INCREMENT,
  name VARCHAR(255),
  regularity INTEGER (4),
  deadline TIMESTAMP,
  party_id INTEGER NOT NULL,
  user_email VARCHAR(255),
  CONSTRAINT chore_pk PRIMARY KEY(id));

CREATE TABLE chore_log(
  user_email VARCHAR(80) NOT NULL,
  chore_id INTEGER(10) NOT NULL,
  done TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chore_log_pk PRIMARY KEY (user_email,chore_id, done)
);

CREATE TABLE item (
  id INTEGER(10) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL ,
  status INTEGER(1) NOT NULL,
  shoppinglist_id INTEGER(10),
  disbursement_id INTEGER(10),
  CONSTRAINT item_pk PRIMARY KEY (id));

CREATE TABLE shoppinglist(
  id INTEGER NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  party_id INTEGER NOT NULL,
  CONSTRAINT shippinglist_pk PRIMARY KEY (id));

CREATE TABLE shoppinglist_user (
  shoppinglist_id INTEGER(10),
  user_email      VARCHAR(80),
  CONSTRAINT shoppinglst_user_pk PRIMARY KEY (shoppinglist_id, user_email)
);


CREATE TABLE user_disbursement(
  user_email VARCHAR(255) NOT NULL,
  disp_id INTEGER(10) NOT NULL,
  accepted INTEGER(1) DEFAULT 0,
  CONSTRAINT user_disbursement_pk PRIMARY KEY (user_email,disp_id));

CREATE TABLE disbursement(
  id INTEGER(10) AUTO_INCREMENT,
  price DOUBLE,
  name VARCHAR(255),
  date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  payer_id VARCHAR(255) NOT NULL,
  party_id INTEGER(10) NOT NULL,
  CONSTRAINT disbursement_pk PRIMARY KEY(id));

CREATE TABLE payment(
  id INTEGER(10) AUTO_INCREMENT,
  payer_id VARCHAR (255) NOT NULL,
  receiver_id VARCHAR (255) NOT NULL,
  party_id INTEGER(10) NOT NULL,
  amount DOUBLE NOT NULL,
  active BIT NOT NULL DEFAULT 0,
  CONSTRAINT payment_pk PRIMARY KEY(id));



-- Legger på referanseintegritet (fremmednøkler)
ALTER TABLE notification
    ADD CONSTRAINT notification_fk FOREIGN KEY (user_email) REFERENCES user(email);


ALTER TABLE wallpost
  ADD CONSTRAINT wallpost_fk1 FOREIGN KEY(party_id)REFERENCES party(id);

ALTER TABLE wallpost
  ADD CONSTRAINT wallpost_fk2 FOREIGN KEY(user_email)REFERENCES user(email);

ALTER TABLE user_party
  ADD CONSTRAINT user_party_fk1 FOREIGN KEY(user_email)REFERENCES user(email);

ALTER TABLE user_party
  ADD CONSTRAINT user_party_fk2 FOREIGN KEY(party_id)REFERENCES party(id);

ALTER TABLE chore
  ADD CONSTRAINT chore_fk1 FOREIGN KEY(user_email)REFERENCES user(email);

ALTER TABLE chore
  ADD CONSTRAINT chore_fk2 FOREIGN KEY(party_id)REFERENCES party(id);

ALTER TABLE shoppinglist
  ADD CONSTRAINT shoppinglist_fk FOREIGN KEY(party_id)REFERENCES party(id);

ALTER TABLE disbursement
  ADD CONSTRAINT disbursement_fk1 FOREIGN KEY(payer_id)REFERENCES user(email);

ALTER TABLE disbursement
  ADD CONSTRAINT disbursement_fk2 FOREIGN KEY(party_id)REFERENCES party(id);

ALTER TABLE user_disbursement
  ADD CONSTRAINT user_disbursement_fk1 FOREIGN KEY(user_email)REFERENCES user(email);

ALTER TABLE user_disbursement
  ADD CONSTRAINT user_disbursement_fk2 FOREIGN KEY(disp_id)REFERENCES disbursement(id) ON DELETE CASCADE ;

ALTER TABLE item
  ADD CONSTRAINT item_fk1 FOREIGN KEY(disbursement_id) REFERENCES  disbursement(id);

ALTER TABLE item
  ADD CONSTRAINT item_fk2 FOREIGN KEY(shoppinglist_id) REFERENCES shoppinglist(id);

ALTER TABLE shoppinglist_user
  ADD CONSTRAINT shoppinglist_user_fk1 FOREIGN KEY (shoppinglist_id) REFERENCES shoppinglist(id);

ALTER TABLE shoppinglist_user
  ADD CONSTRAINT shoppinglist_user_fk2 FOREIGN KEY (user_email) REFERENCES user(email);

ALTER TABLE chore_log
  ADD CONSTRAINT chore_log_fk1 FOREIGN KEY (user_email) REFERENCES user(email);

ALTER TABLE chore_log
  ADD CONSTRAINT chore_log_fk2 FOREIGN KEY (chore_id) REFERENCES chore(id);

ALTER TABLE payment
  ADD CONSTRAINT payment_fk1 FOREIGN KEY (payer_id) REFERENCES user(email);

ALTER TABLE payment
  ADD CONSTRAINT payment_fk2 FOREIGN KEY (receiver_id) REFERENCES user(email);

ALter TABLE payment
  ADD CONSTRAINT payment_fk3 FOREIGN KEY (party_id) REFERENCES party(id);


#TESTDATA

# All passwords are 'qwe', hashed with sha256, salted with '123' and hashed again
INSERT INTO user(name, email, password, salt, phone) VALUES( 'Muts Duvudsun', 'en@h.no', 'd0a4906fe8234ceaf651e4fc4e045a6c0511e36d00b0a3565ece64a7e597498f','123',123456);
INSERT INTO user(name, email, password, salt, phone) VALUES( 'Murtin Wungun', 'to@h.no', 'd0a4906fe8234ceaf651e4fc4e045a6c0511e36d00b0a3565ece64a7e597498f','123',12345623);
INSERT INTO user(name, email, password, salt, phone) VALUES( 'Knat Waag', 'tre@h.no', 'd0a4906fe8234ceaf651e4fc4e045a6c0511e36d00b0a3565ece64a7e597498f','123',12343524);
INSERT INTO user(name, email, password, salt ,phone) VALUES( 'PW: qwe', 'abcqwe', 'd0a4906fe8234ceaf651e4fc4e045a6c0511e36d00b0a3565ece64a7e597498f','123',12343524);
INSERT INTO user(name, email, password, salt, phone) VALUES( 'geir', 'fire@h.no', 'd0a4906fe8234ceaf651e4fc4e045a6c0511e36d00b0a3565ece64a7e597498f','123',123456);
INSERT INTO user(name, email, password, salt, phone) VALUES( 'ove', 'fem@h.no', 'd0a4906fe8234ceaf651e4fc4e045a6c0511e36d00b0a3565ece64a7e597498f','123',12345623);
INSERT INTO user(name, email, password, salt, phone) VALUES( 'lisa', 'seks@h.no', 'd0a4906fe8234ceaf651e4fc4e045a6c0511e36d00b0a3565ece64a7e597498f','123',12343524);
INSERT INTO user(name, email, password, salt ,phone) VALUES( 'camilla', 'sju@h.no', 'd0a4906fe8234ceaf651e4fc4e045a6c0511e36d00b0a3565ece64a7e597498f','123',12343524);
INSERT INTO user(name, email, password, salt ,phone) VALUES( 'bjorn', 'bk@p.no', 'd0a4906fe8234ceaf651e4fc4e045a6c0511e36d00b0a3565ece64a7e597498f','123',12343524);

INSERT INTO notification(user_email,text) VALUES ('en@h.no', 'Dette er en notification');
INSERT INTO notification(user_email,text) VALUES ('en@h.no', 'Dette  er en notification til');
INSERT INTO notification(user_email,text) VALUES ('en@h.no', 'Tredje notifikasjon');
INSERT INTO notification(user_email,text) VALUES ('abcqwe', 'Dette er en notification');
INSERT INTO notification(user_email,text) VALUES ('abcqwe', 'Kristian må les dette');
INSERT INTO notification(user_email,text) VALUES ('abcqwe', 'Kristian må lese mer');


INSERT INTO party(id,name) VALUES (1,'Frex');
INSERT INTO party(id,name) VALUES (2,'Brummun');
INSERT INTO party(id,name) VALUES (3,'BKs Hi');


INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('en@h.no',1,100,2);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('en@h.no',2,100,1);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('to@h.no',1,-20.1,0);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('tre@h.no',1,0,0);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('tre@h.no',2,0,1);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('fire@h.no',2,0.2,2);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('fem@h.no',2,-20.1,1);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('seks@h.no',2,0,1);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('sju@h.no',2,0,1);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('bk@p.no',3,0,1);

INSERT INTO wallpost(message, party_id, user_email) VALUES ('Dette var kult',1,'en@h.no');
INSERT INTO wallpost(message, party_id, user_email) VALUES ('Kommer ikke hjem før onsdag',1,'to@h.no');
INSERT INTO wallpost(message, party_id, user_email) VALUES ('Vi skal ha besøk her fredag',1,'tre@h.no');
INSERT INTO wallpost(message, party_id, user_email) VALUES ('Er det her man skriver beskjeder?',2,'tre@h.no');

INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Legge inn testdata',0,'29-01-18', 1, 'en@h.no');
INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Legge inn testdata igjen',0,'02-02-18', 1, 'to@h.no');
INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Gi team 3 A på prosjekt',0,'10-02-18',1, 'tre@h.no');
INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Male vinduet',0,'12-03-18',1, NULL);
INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Tømme badekaret',0,'29-08-18',1, NULL);

INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Legge inn testdata',0,'29-01-18', 2, 'en@h.no');
INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Legge inn testdata igjen',0,'02-02-18', 2, 'tre@h.no');
INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Spise Nachos',0,'02-02-18', 3, null);

INSERT INTO shoppinglist(name, party_id) VALUES ('Taco', 1);
INSERT INTO shoppinglist(name, party_id) VALUES ('Kino', 1);
INSERT INTO shoppinglist(name, party_id) VALUES ('DateNight', 1);

INSERT INTO disbursement(price,name,payer_id,party_id,date) VALUES (200.1,'Drikke til tacokveld', 'en@h.no', 1, '18-01-18');
INSERT INTO disbursement(price,name,payer_id,party_id,date) VALUES (300,'Liste1', 'to@h.no', 1, '18-01-08');
INSERT INTO disbursement(price,name,payer_id,party_id,date) VALUES (45,'Kvitto', 'tre@h.no', 1, '18-01-20');
INSERT INTO disbursement(price,name,payer_id,party_id,date) VALUES (20,'Såpeting', 'en@h.no', 1, '18-01-01');
INSERT INTO disbursement(price,name,payer_id,party_id,date) VALUES (200.1,'Kino', 'en@h.no', 1, '18-01-05');

INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Kjøttdeig', 0, 1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Tacokrydder', 0,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Lefser', 0,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Mais', 0,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Saus', 0,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Rømme', 0,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Ananas(Mats ikke med)', 0,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Cola', 0,1,1);

INSERT INTO user_disbursement(user_email, disp_id) VALUES ('en@h.no','1');
INSERT INTO user_disbursement(user_email, disp_id) VALUES ('to@h.no','1');

INSERT INTO chore_log(user_email,chore_id) VALUE ('en@h.no',1);
INSERT INTO chore_log(user_email,chore_id) VALUE ('tre@h.no',1);
INSERT INTO chore_log(user_email,chore_id) VALUE ('fire@h.no',1);
INSERT INTO chore_log(user_email,chore_id) VALUE ('fire@h.no',2);
INSERT INTO chore_log(user_email,chore_id) VALUE ('to@h.no',1);
INSERT INTO chore_log(user_email,chore_id) VALUE ('to@h.no',2);
INSERT INTO chore_log(user_email,chore_id) VALUE ('to@h.no',3);
INSERT INTO chore_log(user_email,chore_id) VALUE ('en@h.no',6);
INSERT INTO chore_log(user_email,chore_id) VALUE ('tre@h.no',7);
INSERT INTO chore_log(user_email,chore_id) VALUE ('tre@h.no',6);

INSERT INTO user_party (user_email, party_id, balance, status) VALUEs ('abcqwe',1,0,1);

INSERT INTO shoppinglist_user(user_email,shoppinglist_id) VALUEs ('user@householdmanager.com',1);
INSERT INTO shoppinglist_user(shoppinglist_id, user_email) VALUES (1,'en@h.no');
INSERT INTO shoppinglist_user(shoppinglist_id, user_email) VALUES (3,'en@h.no');

INSERT INTO payment(payer_id, receiver_id, party_id, amount) VALUES ('to@h.no','en@h.no', 1,50);
INSERT INTO payment(payer_id, receiver_id, party_id, amount) VALUES ('tre@h.no','en@h.no', 1,100);
INSERT INTO payment(payer_id, receiver_id, party_id, amount) VALUES ('to@h.no','en@h.no', 1, 50);
