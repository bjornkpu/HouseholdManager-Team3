

INSERT INTO user(name, email, password, phone) VALUES( 'Muts Duvudsun', 'en@h.no', 'passord',123456);
INSERT INTO user(name, email, password, phone) VALUES( 'Murtin Wungun', 'to@h.no', 'passord',12345623);
INSERT INTO user(name, email, password, phone) VALUES( 'Knat Waag', 'tre@h.no', 'passord',12343524);
INSERT INTO user(name, email, password, salt ,phone) VALUES( 'Naihan Skravahau', 'abcqwe', '18138372fad4b94533cd4881f03dc6c69296dd897234e0cee83f727e2e6b1f63','123',12343524);

INSERT INTO party(name, admin) VALUES ('Frex', 'en@h.no');
INSERT INTO party(name, admin) VALUES ('Brummun', 'tre@h.no');

INSERT INTO user_party(user_email,party_id,balance) VALUES ('en@h.no',1,0.2);
INSERT INTO user_party(user_email,party_id,balance) VALUES ('to@h.no',1,-2000.1);
INSERT INTO user_party(user_email,party_id,balance) VALUES ('tre@h.no',1,0);
INSERT INTO user_party(user_email,party_id,balance) VALUES ('tre@h.no',2,0);

INSERT INTO wallpost(message, party_id, user_email) VALUES ('Husk å dra ned i do, Murtin',1,'en@h.no');
INSERT INTO wallpost(message, party_id, user_email) VALUES ('Kommer ikke hjem før onsdag',1,'to@h.no');
INSERT INTO wallpost(message, party_id, user_email) VALUES ('Vi skal ha vors her fredag',1,'tre@h.no');
INSERT INTO wallpost(message, party_id, user_email) VALUES ('Er det her man skriver beskjeder?',2,'tre@h.no');

INSERT INTO chore(description, regularity, deadline,  party_id, user_email) VALUES ('Legge inn testdata',0,'29-01-18', 1, 'en@h.no');
INSERT INTO chore(description, regularity, deadline,  party_id, user_email) VALUES ('Legge inn testdata igjen',0,'02-02-18', 1, 'to@h.no');
INSERT INTO chore(description, regularity, deadline,  party_id, user_email) VALUES ('Kjedelig å legge inn testdata',0,'10-02-18',1, 'tre@h.no');
INSERT INTO chore(description, regularity, deadline,  party_id, user_email) VALUES ('Male vinduet',0,'12-03-18',1, NULL);
INSERT INTO chore(description, regularity, deadline,  party_id, user_email) VALUES ('Tømme badekaret',0,'29-08-18',1, NULL);

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
