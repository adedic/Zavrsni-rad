
INSERT INTO roomate_group(id, name) VALUES (1, 'Naj cimeri ikad');

INSERT INTO roomate_group(id, name) VALUES (2, '238 239');


INSERT INTO user(id, group_id, name, surname ,username,password,enabled)
VALUES (1, 1, 'Ivo','Ivić','iivic','$2a$10$yRFelMX/Yf2PnGAp985Rg.POv3UwmCAw3WQ3.iSsKMf5YVBDulwnW', true);

INSERT INTO user(id, group_id, name, surname ,username,password,enabled)
VALUES (2, 1, 'Marko','Markić','mmarkic','$2a$10$yRFelMX/Yf2PnGAp985Rg.POv3UwmCAw3WQ3.iSsKMf5YVBDulwnW', true);

INSERT INTO user(id, group_id, name, surname ,username,password,enabled)
VALUES (3,1, 'Pero','Perić','pperic','$2a$10$yRFelMX/Yf2PnGAp985Rg.POv3UwmCAw3WQ3.iSsKMf5YVBDulwnW', true);

INSERT INTO user(id, name, surname ,username,password,enabled)
VALUES (4,'Jure','Jurić','jjuric','$2a$10$yRFelMX/Yf2PnGAp985Rg.POv3UwmCAw3WQ3.iSsKMf5YVBDulwnW', true);

INSERT INTO user(id, name, surname ,username,password,enabled)
VALUES (5,'Adminko','Adminić','admin','$2a$10$yRFelMX/Yf2PnGAp985Rg.POv3UwmCAw3WQ3.iSsKMf5YVBDulwnW', true);


INSERT INTO user_role (username, role) VALUES ('iivic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('mmarkic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('pperic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('jjuric', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('admin', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('admin', 'ROLE_ADMIN'); 


INSERT INTO category (id, description) VALUES (1, 'Hrana i piće');

INSERT INTO category (id, description) VALUES (2, 'Kućanski poslovi');

INSERT INTO category (id, description) VALUES (3, 'Komunalne usluge');

INSERT INTO category (id, description) VALUES (4, 'Nekategorizirano:');




INSERT INTO bill (id, user_id, group_id, title, price, date_created, last_update_date, description, category_id) 
VALUES(1, 1, 1, 'Ćišćenje wc-a', 0, CURRENT_DATE()-2, NULL, 'Volim cistit',  2);


INSERT INTO bill (id, user_id, group_id, title, price, date_created, last_update_date, description, category_id) 
VALUES(2, 2, 1, 'Kruh', 8.5, CURRENT_DATE()-2, NULL, 'Kupio kruh',  1);


INSERT INTO bill (id, user_id, group_id, title, price, date_created, last_update_date, description, category_id) 
VALUES(3, 2, 1, 'Režije', 250, CURRENT_DATE(), NULL, 'Platio struju',  3);

