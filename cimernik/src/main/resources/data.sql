
INSERT INTO roomate_group(id, name, date_created) VALUES (1, 'Naj cimeri ikad', CURRENT_DATE()-40);

INSERT INTO roomate_group(id, name, date_created) VALUES (2, 'Košarka', CURRENT_DATE()-20);

INSERT INTO roomate_group(id, name, date_created) VALUES (3, 'Ekipa s tavana', CURRENT_DATE()-1);

INSERT INTO roomate_group(id, name, date_created) VALUES (4, 'Podrumare', CURRENT_DATE()-100);

INSERT INTO roomate_group(id, name, date_created) VALUES (5, 'Nemamo para', CURRENT_DATE()-2);



INSERT INTO user(id, group_id, name, surname ,username, password,enabled)
VALUES (1, 1, 'Ivo','Ivić','iivic','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, group_id, name, surname ,username, password,enabled)
VALUES (2, 1, 'Marko','Markić','mmarkic','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, group_id, name, surname ,username, password,enabled)
VALUES (3, 2,'Pero','Perić','pperic','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, group_id, name, surname ,username, password,enabled)
VALUES (4,2, 'Jure','Jurić','jjuric','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, name, surname ,username, password,enabled)
VALUES (5,'Adminko','Adminić','admin','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, group_id, name, surname ,username,password, enabled)
VALUES (6, 3,'Mate','Matić','mmatic','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, group_id, name, surname ,username,password,enabled)
VALUES (7, 3 ,'Ante','Antić','aantic','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);


INSERT INTO user(id, group_id, name, surname ,username,password,enabled)
VALUES (8, 4, 'Zrinka','Zrinkić','z','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, group_id, name, surname ,username,password,enabled)
VALUES (9, 4, 'Karla','Karlić','c','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, group_id, name, surname ,username,password,enabled)
VALUES (10, 4, 'Marina','Marinić','m','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, group_id, name, surname ,username,password,enabled)
VALUES (11, 4, 'Ana','Anić','a','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);

INSERT INTO user(id, name, surname ,username,password,enabled)
VALUES (12, 'Mara','Marić','mara','$2a$04$sajsx1aReuYPVjGD/8yPDOhAUpn8HDwIbsebgSlFelXuQBPXlrCS.', true);




INSERT INTO user_role (username, role) VALUES ('iivic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('mmarkic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('pperic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('jjuric', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('mmatic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('aantic', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('z', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('c', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('m', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('a', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('mara', 'ROLE_USER'); 


INSERT INTO user_role (username, role) VALUES ('admin', 'ROLE_USER'); 

INSERT INTO user_role (username, role) VALUES ('admin', 'ROLE_ADMIN'); 


INSERT INTO category (id, description) VALUES (1, 'Isplata duga');

INSERT INTO category (id, description) VALUES (2, 'Hrana i piće');

INSERT INTO category (id, description) VALUES (3, 'Kućanski poslovi');

INSERT INTO category (id, description) VALUES (4, 'Komunalne usluge');

INSERT INTO category (id, description) VALUES (5, 'Nekategorizirano');




INSERT INTO bill (id, user_id, group_id, title, price, date_created, last_update_date, description, category_id) 
VALUES(1, 1, 1, 'Čišćenje wc-a', 0, CURRENT_DATE()-50, NULL, 'Volim cistit',  3);


INSERT INTO bill (id, user_id, group_id, title, price, date_created, last_update_date, description, category_id) 
VALUES(2, 2, 1, 'Kruh', 8.5, CURRENT_DATE()-25, NULL, 'Kupio kruh',  2);


INSERT INTO bill (id, user_id, group_id, title, price, date_created, last_update_date, description, category_id) 
VALUES(3, 2, 1, 'Režije', 250, CURRENT_DATE()-20, NULL, 'Platio struju',  4);


INSERT INTO bill (id, user_id, group_id, title, price, date_created, last_update_date, description, category_id) 
VALUES(4, 1, 1, 'Kino', 320, CURRENT_DATE()-10, NULL, 'Kupio ulaznice za Wonder Woman',  5);

