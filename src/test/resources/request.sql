INSERT INTO users (login, password, role)
VALUES ('Robert', 'password','USER');

INSERT INTO users (login, password, role)
VALUES ('Scarlett', 'password','USER');

INSERT INTO users (login, password, role)
VALUES ('Chris', 'password','USER');

INSERT INTO users (login, password, role)
VALUES ('Mark', 'password','USER');

INSERT INTO users (login, password, role)
VALUES ('Jeremy', 'password','USER');

INSERT INTO users (login, password, role)
VALUES ('Admin', 'AdminPassword','ADMIN');

SELECT id, login FROM users
WHERE removed=FALSE
ORDER BY id
LIMIT 3 OFFSET 0
;

SELECT id, login FROM users
WHERE removed=TRUE
ORDER BY id
LIMIT 3 OFFSET 0
;

SELECT id, login FROM users
WHERE id=2;

UPDATE USERS SET password = 'top-secret'
WHERE id =1 and removed = false
RETURNING id, login, role;

UPDATE users SET removed = true
WHERE id = 1
RETURNING id, login, role;

UPDATE users SET removed = false
WHERE id = 1
RETURNING id, login, role;