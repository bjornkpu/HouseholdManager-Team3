-- Sletter tabeller

DROP TABLE IF EXISTS item_shoppinglist;
DROP TABLE IF EXISTS wallpost;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS chore;
DROP TABLE IF EXISTS shoppinglist;
DROP TABLE IF EXISTS user_party;
DROP TABLE IF EXISTS party;
DROP TABLE IF EXISTS user;


-- Oppretter tabeller med entitetsintegritet (primærnøkkel)

CREATE TABLE user(
  name VARCHAR(30) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(100)NOT NULL,
  phone integer(15),
  CONSTRAINT user_pk PRIMARY KEY(email));

CREATE TABLE party(
  id INTEGER NOT NULL AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL,
  admin VARCHAR(255) NOT NULL,
  CONSTRAINT party_pk PRIMARY KEY(id));

CREATE TABLE wallpost(
  id INTEGER AUTO_INCREMENT,
  time TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  message VARCHAR (255),
  party_id INTEGER (10) NOT NULL ,
  user_id VARCHAR(255) NOT NULL ,
  CONSTRAINT wallpost_pk PRIMARY KEY(id));

CREATE TABLE user_party(
  user_id VARCHAR(255) NOT NULL,
  party_id INTEGER NOT NULL,
  balance DOUBLE NOT NULL,
  CONSTRAINT user_party_pk PRIMARY KEY(user_id,party_id));

CREATE TABLE chore(
  id INTEGER NOT NULL AUTO_INCREMENT,
  description VARCHAR(90),
  regularity INTEGER (4),
  deadline DATE,
  completed BIT,
  party_id INTEGER NOT NULL,
  user_id VARCHAR(255),
  CONSTRAINT chore_pk PRIMARY KEY(id));

CREATE TABLE item (
  name VARCHAR(30) NOT NULL ,
  price DOUBLE NOT NULL ,
  CONSTRAINT item_pk PRIMARY KEY (name,price));

CREATE TABLE shoppinglist(
  id INTEGER NOT NULL AUTO_INCREMENT,
  name VARCHAR(30) NOT NULL,
  party_id INTEGER NOT NULL,
  CONSTRAINT shippinglist_pk PRIMARY KEY (id));

CREATE TABLE item_shoppinglist(
  item_name VARCHAR(255),
  item_price DOUBLE,
  shoppinglist_id INTEGER,
  quantity INTEGER(3) NOT NULL,
  note VARCHAR(255),
  user_id VARCHAR(255) NOT NULL,
  CONSTRAINT item_shopinglist_pk PRIMARY KEY (item_name,item_price,shoppinglist_id));


-- Legger på referanseintegritet (fremmednøkler)

ALTER TABLE party
  ADD CONSTRAINT party_fk FOREIGN KEY(admin)REFERENCES user(email);

ALTER TABLE wallpost
  ADD CONSTRAINT wallpost_fk1 FOREIGN KEY(party_id)REFERENCES party(id);

ALTER TABLE wallpost
  ADD CONSTRAINT wallpost_fk2 FOREIGN KEY(user_id)REFERENCES user(email);

ALTER TABLE user_party
  ADD CONSTRAINT user_party_fk1 FOREIGN KEY(user_id)REFERENCES user(email);

ALTER TABLE user_party
  ADD CONSTRAINT user_party_fk2 FOREIGN KEY(party_id)REFERENCES party(id);

ALTER TABLE chore
  ADD CONSTRAINT chore_fk1 FOREIGN KEY(user_id)REFERENCES user(email);

ALTER TABLE chore
  ADD CONSTRAINT chore_fk2 FOREIGN KEY(party_id)REFERENCES party(id);

ALTER TABLE shoppinglist
  ADD CONSTRAINT shoppinglist_fk FOREIGN KEY(party_id)REFERENCES party(id);

ALTER TABLE item_shoppinglist
  ADD CONSTRAINT item_shoppinglist_fk1 FOREIGN KEY(user_id)REFERENCES user(email);

ALTER TABLE item_shoppinglist
  ADD CONSTRAINT item_shoppinglist_fk2 FOREIGN KEY(shoppinglist_id)REFERENCES shoppinglist(id);

ALTER TABLE item_shoppinglist
  ADD CONSTRAINT item_shoppinglist_fk13 FOREIGN KEY(item_name)REFERENCES item(name);

ALTER TABLE item_shoppinglist
  ADD CONSTRAINT item_shoppinglist_fk4 FOREIGN KEY(item_price)REFERENCES item(price);