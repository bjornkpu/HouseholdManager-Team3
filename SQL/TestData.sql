

# All passwords are 'qwe', hashed with sha256, salted with '123' and hashed again
INSERT INTO user(name, email, password, salt, phone) VALUES( 'Muts Duvudsun', 'en@h.no', '6013b70eae6842660d203fd4983f2054f4c0d0400239e89fba48324200a37638','123',123456);
INSERT INTO user(name, email, password, salt, phone) VALUES( 'Murtin Wungun', 'to@h.no', '6013b70eae6842660d203fd4983f2054f4c0d0400239e89fba48324200a37638','123',12345623);
INSERT INTO user(name, email, password, salt, phone) VALUES( 'Knat Waag', 'tre@h.no', '6013b70eae6842660d203fd4983f2054f4c0d0400239e89fba48324200a37638','123',12343524);
INSERT INTO user(name, email, password, salt ,phone) VALUES( 'PW: qwe', 'abcqwe', '6013b70eae6842660d203fd4983f2054f4c0d0400239e89fba48324200a37638','123',12343524);

INSERT INTO party(name) VALUES ('Frex');
INSERT INTO party(name) VALUES ('Brummun');

INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('en@h.no',1,0.2,2);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('to@h.no',1,-2000.1,0);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('tre@h.no',1,0,0);
INSERT INTO user_party(user_email,party_id,balance,status) VALUES ('tre@h.no',2,0,2);

INSERT INTO wallpost(message, party_id, user_email) VALUES ('Husk å dra ned i do, Murtin',1,'en@h.no');
INSERT INTO wallpost(message, party_id, user_email) VALUES ('Kommer ikke hjem før onsdag',1,'to@h.no');
INSERT INTO wallpost(message, party_id, user_email) VALUES ('Vi skal ha vors her fredag',1,'tre@h.no');
INSERT INTO wallpost(message, party_id, user_email) VALUES ('Er det her man skriver beskjeder?',2,'tre@h.no');

INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Legge inn testdata',0,'29-01-18', 1, 'en@h.no');
INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Legge inn testdata igjen',0,'02-02-18', 1, 'to@h.no');
INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Kjedelig å legge inn testdata',0,'10-02-18',1, 'tre@h.no');
INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Male vinduet',0,'12-03-18',1, NULL);
INSERT INTO chore(name, regularity, deadline,  party_id, user_email) VALUES ('Tømme badekaret',0,'29-08-18',1, NULL);

INSERT INTO shoppinglist(name, party_id) VALUES ('Taco', 1);
INSERT INTO disbursement(price,name,payer_id,party_id,date) VALUES (200.1,'Drikke til tacokveld', 'en@h.no', 1, '08-01-18');

INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Kjøttdeig', 1, 1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Tacokrydder', 1,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Lefser', 0,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Mais', 0,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Saus', 1,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Rømme', 1,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Fredrikstadpilsner', 2,1,1);
INSERT INTO item(name, status, shoppinglist_id, disbursement_id) VALUES ('Cola', 2,1,1);

INSERT INTO user_disbursement(user_email, disp_id) VALUES ('en@h.no','1');
INSERT INTO user_disbursement(user_email, disp_id) VALUES ('to@h.no','1');


# INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Kjøttdeig', 20.40, 1, 1, 'Kjøp på REMA', 'en@h.no');
# INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Tacokrydder', 10.62, 1, 1, 'Kjøp på REMA', 'en@h.no');
# INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Lefser', 15.43, 1, 1, 'Kjøp på REMA', 'en@h.no');
# INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Mais', 5.0, 1, 1, 'Kjøp på REMA', 'en@h.no');
# INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Saus', 10.50, 1, 1, 'Kjøp på REMA', 'en@h.no');
# INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Rømme', 20.00, 1, 1, 'Kjøp på REMA', 'to@h.no');
# INSERT INTO item_shoppinglist(item_name,item_price,shoppinglist_id, quantity, note, user_id) VALUES ('Fredrikstadpilsner', 34.40, 1, 6, 'Kjøp på Bunnpris', 'en@h.no');
