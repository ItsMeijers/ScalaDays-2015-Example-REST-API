# --- !Ups
INSERT INTO Project values (1, 'Play Scala Project', 'This is a sample project for the Scala Days');
INSERT INTO Project values (2, 'Akka Project', 'This is a yet another sample project for the Scala Days');
INSERT INTO Project values (3, 'Java Project', 'No not a Java project!');
INSERT INTO Project values (4, 'Scala Akka Play Project', 'This is a sample project for the Scala Days, again...');
ALTER SEQUENCE project_id_seq RESTART WITH 5;

# --- !Downs
ALTER SEQUENCE project_id_seq RESTART WITH 1;
DELETE FROM Project;