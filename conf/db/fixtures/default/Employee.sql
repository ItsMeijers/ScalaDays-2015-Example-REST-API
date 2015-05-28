# --- !Ups
INSERT INTO Employee values (1, 'Thomas', null, 'Meijers', 'Junior Developer', '2015-01-01');
INSERT INTO Employee values (2, 'Nicolas', null, 'Leroux', 'CTO', '2015-01-01');
INSERT INTO Employee values (3, 'Wolfert', 'de', 'Kraker', 'Developer', '2015-01-01');
INSERT INTO Employee values (4, 'Sander', null, 'Ufkes', 'Developer', '2015-01-01');
INSERT INTO Employee values (5, 'Erik', null, 'Bakker', 'Software Engineer', '2015-01-01');
INSERT INTO Employee values (6, 'Vijay', null, 'Kiran', 'Software Engineer', '2015-01-01');
INSERT INTO Employee values (7, 'Bart', null, 'Schuller', 'Software Engineer', '2015-01-01');
INSERT INTO Employee values (8, 'Sietse', 'de', 'Kaper', 'Software Engineer', '2015-01-01');
ALTER SEQUENCE employee_id_seq RESTART WITH 9;

# --- !Downs
ALTER SEQUENCE employee_id_seq RESTART WITH 1;
DELETE FROM Employee;

