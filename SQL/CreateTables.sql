-- Sletter tabeller

DROP TABLE IF EXISTS item_shoppinglist;
DROP TABLE IF EXISTS user_disbursements;
DROP TABLE IF EXISTS wallpost;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS chore_log;
DROP TABLE IF EXISTS chore;
DROP TABLE IF EXISTS shoppinglist_user;
DROP TABLE IF EXISTS shoppinglist;
DROP TABLE IF EXISTS user_party;
DROP TABLE IF EXISTS disbursements;
DROP TABLE IF EXISTS disbursement;
DROP TABLE IF EXISTS party;
DROP TABLE IF EXISTS user;


-- Oppretter tabeller med entitetsintegritet (primærnøkkel)

CREATE TABLE user(
  name VARCHAR(30) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(100)NOT NULL,
  salt VARCHAR(20),
  phone VARCHAR (15),
  CONSTRAINT user_pk PRIMARY KEY(email));

CREATE TABLE party(
  id INTEGER NOT NULL AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL,
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
  name VARCHAR(90),
  regularity INTEGER (4),
  deadline DATE,
  party_id INTEGER NOT NULL,
  user_email VARCHAR(255),
  CONSTRAINT chore_pk PRIMARY KEY(id));

CREATE TABLE chore_log(
  user_email VARCHAR(80),
  chore_id INTEGER(10),
  done TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chore_log_pk PRIMARY KEY (user_email,chore_id)
);

CREATE TABLE item (
  id INTEGER(10) AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL ,
  status INTEGER(1) NOT NULL,
  shoppinglist_id INTEGER(10),
  dips_id INTEGER(10),
  CONSTRAINT item_pk PRIMARY KEY (id));

CREATE TABLE shoppinglist(
  id INTEGER NOT NULL AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL,
  party_id INTEGER NOT NULL,
  CONSTRAINT shippinglist_pk PRIMARY KEY (id));

CREATE TABLE shoppinglist_user (
  shoppinglist_id INTEGER(10),
  user_email      VARCHAR(80),
  CONSTRAINT shoppinglst_user_pk PRIMARY KEY (shoppinglist_id, user_email)
);


CREATE TABLE user_disbursements(
  user_email VARCHAR(255) NOT NULL,
  disp_id INTEGER(10) NOT NULL,
  CONSTRAINT user_disbursements_pk PRIMARY KEY (user_email,disp_id));

CREATE TABLE disbursements(
  id INTEGER(10) AUTO_INCREMENT,
  price DOUBLE,
  name VARCHAR(255),
  date DATE,
  payer_id VARCHAR(255) NOT NULL,
  party_id INTEGER(10) NOT NULL,
  CONSTRAINT disbursements_pk PRIMARY KEY(id));



-- Legger på referanseintegritet (fremmednøkler)


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

ALTER TABLE disbursements
  ADD CONSTRAINT disbursements_fk1 FOREIGN KEY(payer_id)REFERENCES user(email);

ALTER TABLE disbursements
  ADD CONSTRAINT disbursements_fk2 FOREIGN KEY(party_id)REFERENCES party(id);

ALTER TABLE user_disbursements
  ADD CONSTRAINT user_disbursements_fk1 FOREIGN KEY(user_email)REFERENCES user(email);

ALTER TABLE user_disbursements
  ADD CONSTRAINT user_disbursements_fk2 FOREIGN KEY(disp_id)REFERENCES disbursements(id);

ALTER TABLE item
  ADD CONSTRAINT item_fk1 FOREIGN KEY(dips_id) REFERENCES  disbursements(id);

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
