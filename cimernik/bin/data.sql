
INSERT INTO roomate_group(id, name) VALUES (1, 'Naj cimeri ikad');

INSERT INTO roomate_group(id, name) VALUES (2, '238 239');


INSERT INTO user(id, group_id, name, surname ,username,password,enabled)
VALUES (1, 1, 'Ivo','Ivić','iivic','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, group_id, name, surname ,username,password,enabled)
VALUES (2, 1, 'Marko','Markić','mmarkic','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, name, surname ,username,password,enabled)
VALUES (3,'Pero','Perić','pperic','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, name, surname ,username,password,enabled)
VALUES (4,'Jure','Jurić','jjuric','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, name, surname ,username, password,enabled)
VALUES (5,'Adminko','Adminić','admin','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, name, surname ,username,password, enabled)
VALUES (6,'Mate','Matić','mmatic','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, name, surname ,username,password,enabled)
VALUES (7,'Ante','Antić','aantic','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);



INSERT INTO user_role (username, role) VALUES ('iivic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('mmarkic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('pperic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('jjuric', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('mmatic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('aantic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('admin', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('admin', 'ROLE_ADMIN'); 


INSERT INTO category (id, description) VALUES (1, 'Isplata duga');

INSERT INTO category (id, description) VALUES (2, 'Hrana i piće');

INSERT INTO category (id, description) VALUES (3, 'Kućanski poslovi');

INSERT INTO category (id, description) VALUES (4, 'Komunalne usluge');

INSERT INTO category (id, description) VALUES (5, 'Nekategorizirano');




INSERT INTO bill (id, user_id, group_id, title, price, date_created, last_update_date, description, category_id) 
VALUES(1, 1, 1, 'Čišćenje wc-a', 0, CURRENT_DATE()-2, NULL, 'Volim cistit',  3);


INSERT INTO bill (id, user_id, group_id, title, price, date_created, last_update_date, description, category_id) 
VALUES(2, 2, 1, 'Kruh', 8.5, CURRENT_DATE()-2, NULL, 'Kupio kruh',  2);


INSERT INTO bill (id, user_id, group_id, title, price, date_created, last_update_date, description, category_id) 
VALUES(3, 2, 1, 'Režije', 250, CURRENT_DATE()-20, NULL, 'Platio struju',  4);


INSERT INTO bill (id, user_id, group_id, title, price, date_created, last_update_date, description, category_id) 
VALUES(4, 1, 1, 'Kino', 320, CURRENT_DATE()-10, NULL, 'Kupio ulaznice za Wonder Woman',  5);

