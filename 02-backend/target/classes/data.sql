INSERT INTO LOGINAPP.USERS (USERNAME,PASSWORD, FIRST_NAME, LAST_NAME, EMAIL, BDAY)
VALUES
('admin', '$2a$10$4K8Vq5mw.nwxl.WRmuYCfevme82c73uGkEcnPbmm/3/YJ3UToie7m', 'admin-name','password is "admin"',  'admin@mail.com', '1990-09-01'),
('user', '$10$c.jE9xSuqj7amPDHopzlBeRXf/JKzx86RN2Qd7I645KtbpNM2QXZC', 'user-name','password is "user"',  'user@mail.com', '2000-09-01');
INSERT INTO ROLES(NAME) VALUES('USER'), ('ADMIN');
