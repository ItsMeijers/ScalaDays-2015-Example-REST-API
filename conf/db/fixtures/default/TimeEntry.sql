# --- !Ups
INSERT INTO TimeEntry values (1, '2015-04-20T11:41', '2015-04-20T13:00', 1, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (2, '2015-04-20T11:12', '2015-04-20T13:00', 1, 2, 'Implement feature 1');
INSERT INTO TimeEntry values (3, '2015-04-20T11:33', '2015-04-20T13:00', 3, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (4, '2015-04-20T11:20', '2015-04-20T13:00', 2, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (5, '2015-04-20T11:00', '2015-04-20T13:00', 5, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (6, '2015-04-20T11:01', '2015-04-20T13:00', 3, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (7, '2015-04-20T11:14', '2015-04-20T13:00', 4, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (8, '2015-04-20T11:41', '2015-04-20T13:00', 1, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (9, '2015-04-20T11:12', '2015-04-20T13:00', 3, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (10, '2015-04-20T11:55', '2015-04-20T13:00', 4, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (11, '2015-04-20T11:45', '2015-04-20T13:00', 5, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (12, '2015-04-20T11:12', '2015-04-20T13:00', 5, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (13, '2015-04-20T11:33', '2015-04-20T13:00', 1, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (14, '2015-04-20T11:22', '2015-04-20T13:00', 1, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (15, '2015-04-20T11:59', '2015-04-20T13:00', 2, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (16, '2015-04-20T11:41', '2015-04-20T13:00', 2, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (17, '2015-04-20T11:41', '2015-04-20T13:00', 3, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (18, '2015-04-20T11:41', '2015-04-20T13:00', 3, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (19, '2015-04-20T11:41', '2015-04-20T13:00', 4, 1, 'Implement feature 1');
INSERT INTO TimeEntry values (20, '2015-04-20T11:41', '2015-04-20T13:00', 3, 1, 'Implement feature 1');
ALTER SEQUENCE timeentry_id_seq RESTART WITH 21;

# --- !Downs
ALTER SEQUENCE timeentry_id_seq RESTART WITH 1;
DELETE FROM TimeEntry;