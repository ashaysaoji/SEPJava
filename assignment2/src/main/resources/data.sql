INSERT INTO student (name, last_name, roll_number, email) VALUES ('John', 'Doe', 'CS101', 'john@test.com');

INSERT INTO teacher (name, last_name, email, subject) VALUES ('Jane', 'Smith', 'jane@test.com', 'Math');
INSERT INTO teacher (name, last_name, email, subject) VALUES ('Bob', 'Jones', 'bob@test.com', 'Science');

INSERT INTO student_teacher (student_id, teacher_id) VALUES (1, 1);
INSERT INTO student_teacher (student_id, teacher_id) VALUES (1, 2);