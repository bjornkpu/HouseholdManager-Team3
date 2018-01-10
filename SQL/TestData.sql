

INSERT INTO user(name, email, password, phone) VALUES( 'Muts Duvudsun', 'en@h.no', 'passord',123456);
INSERT INTO user(name, email, password, phone) VALUES( 'Murtin Wungun', 'to@h.no', 'passord',12345623);
INSERT INTO user(name, email, password, phone) VALUES( 'Knat Waag', 'tre@h.no', 'passord',12343524);

INSERT INTO party(name, admin) VALUES ('Frex', 'en@h.no');
INSERT INTO party(name, admin) VALUES ('Brummun', 'tre@h.no');

INSERT INTO user_party(user_id,party_id,balance) VALUES ('en@h.no',1,0.2);
INSERT INTO user_party(user_id,party_id,balance) VALUES ('to@h.no',1,-2000.1);
INSERT INTO user_party(user_id,party_id,balance) VALUES ('tre@h.no',1,0);
INSERT INTO user_party(user_id,party_id,balance) VALUES ('tre@h.no',2,0);

INSERT INTO wallpost(message, party_id, user_id) VALUES ('Husk å dra ned i do, Murtin',1,'en@h.no');
INSERT INTO wallpost(message, party_id, user_id) VALUES ('Kommer ikke hjem før onsdag',1,'to@h.no');
INSERT INTO wallpost(message, party_id, user_id) VALUES ('Vi skal ha vors her fredag',1,'tre@h.no');
INSERT INTO wallpost(message, party_id, user_id) VALUES ('Er det her man skriver beskjeder?',2,'tre@h.no');

INSERT INTO chore(description, regularity, deadline, completed, party_id, user_id) VALUES ('Legge inn testdata',0,'29-01-18', 0, 1, 'en@h.no');
INSERT INTO chore(description, regularity, deadline, completed, party_id, user_id) VALUES ('Legge inn testdata igjen',0,'02-02-18', 0, 1, 'to@h.no');
INSERT INTO chore(description, regularity, deadline, completed, party_id, user_id) VALUES ('Kjedelig å legge inn testdata',0,'10-02-18', 0, 1, 'tre@h.no');
INSERT INTO chore(description, regularity, deadline, completed, party_id, user_id) VALUES ('Male vinduet',0,'12-03-18', 0, 1, NULL);
INSERT INTO chore(description, regularity, deadline, completed, party_id, user_id) VALUES ('Tømme badekaret',0,'29-08-18', 0, 1, NULL);

INSERT INTO item(name, price) VALUES ('Kjøttdeig', 20.40);
INSERT INTO item(name, price) VALUES ('Tacokrydder', 10.62);
INSERT INTO item(name, price) VALUES ('Lefser', 15.43);
INSERT INTO item(name, price) VALUES ('Mais', 5.0);
INSERT INTO item(name, price) VALUES ('Saus', 10.50);
INSERT INTO item(name, price) VALUES ('Rømme', 20.0);
INSERT INTO item(name, price) VALUES ('Fredrikstadpilsner', 34.40);
INSERT INTO item(name, price) VALUES ('Cola', 34.30);

INSERT INTO shoppinglist(name, party_id) VALUES ('Taco', 1);

INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Kjøttdeig', 20.40, 1, 1, 'Kjøp på REMA', 'en@h.no');
INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Tacokrydder', 10.62, 1, 1, 'Kjøp på REMA', 'en@h.no');
INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Lefser', 15.43, 1, 1, 'Kjøp på REMA', 'en@h.no');
INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Mais', 5.0, 1, 1, 'Kjøp på REMA', 'en@h.no');
INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Saus', 10.50, 1, 1, 'Kjøp på REMA', 'en@h.no');
INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Rømme', 20.00, 1, 1, 'Kjøp på REMA', 'to@h.no');
INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Fredrikstadpilsner', 34.40, 1, 6, 'Kjøp på Bunnpris', 'en@h.no');