

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

INSERT INTO shoppinglist(name, party_id) VALUES ('Taco', 1);
INSERT INTO disbursement(price,description,payer_id,party_id,date) VALUES (200.1,'Drikke til tacokveld', 'en@h.no', 1, '08-01-18');

INSERT INTO item(name, status, shoppinglist_id, dips_id) VALUES ('Kjøttdeig', 1, 1);
INSERT INTO item(name, status, shoppinglist_id, dips_id) VALUES ('Tacokrydder', 1,1);
INSERT INTO item(name, status, shoppinglist_id, dips_id) VALUES ('Lefser', 0,1);
INSERT INTO item(name, status, shoppinglist_id, dips_id) VALUES ('Mais', 0,1);
INSERT INTO item(name, status, shoppinglist_id, dips_id) VALUES ('Saus', 1,1);
INSERT INTO item(name, status, shoppinglist_id, dips_id) VALUES ('Rømme', 1,1);
INSERT INTO item(name, status, shoppinglist_id, dips_id) VALUES ('Fredrikstadpilsner', 2,1,1);
INSERT INTO item(name, status, shoppinglist_id, dips_id) VALUES ('Cola', 2,1,1);
