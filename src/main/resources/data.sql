SET search_path TO equiz;

-- Clear previous data
DELETE FROM quiz_question;
DELETE FROM question;
DELETE FROM quiz_attempt;
DELETE FROM quiz;
DELETE FROM topic;
DELETE FROM "user";
DELETE FROM course;

-- course_id is SERIAL in PostgreSQL.
INSERT INTO course (course_id, course_name) VALUES
(1, 'Software Engineering'),
(2, 'Data Science and Analytics');

-- topic_id is SERIAL in PostgreSQL.
INSERT INTO topic (topic_id, topic_name, course_id) VALUES
(1, 'Data Structures and Algorithms in Java', 1),
(2, 'Machine Learning', 2),
(3, 'Statistics', 2),
(4, 'Object-oriented Programming in Java', 1);

-- user_id format: YYXXXX. user_id is text as it's a combination of strings and numbers.
INSERT INTO "user" (user_id, password, first_name, last_name, last_login_date, course_id, is_lecturer) VALUES
('230001', 'tenaciousD', 'Jack', 'Black', '2024-08-01', 1, TRUE),
('230002', 'muda', 'Dio', 'Brando', '2024-08-02', 2, TRUE),
('230003', 'risingforce', 'Yngwie', 'Malmsteen', '2024-07-29', 1, FALSE),
('230004', 'topgun', 'Tom', 'Cruise', '2024-08-01', 1, FALSE),
('230005', 'racerx', 'Paul', 'Gilbert', '2024-07-30', 1, FALSE),
('230006', 'cream', 'Eric', 'Johnson', '2024-08-03', 1, FALSE),
('230007', 'ora', 'Jotaro', 'Kujo', '2024-08-02', 2, FALSE),
('230008', 'purplehermit', 'Joseph', 'Joestar', '2024-08-02', 2, FALSE);

-- quiz_id is SERIAL in PostgreSQL.
INSERT INTO quiz (quiz_id, quiz_name, course_id, max_attempts, due_date, is_removed) VALUES
(1, 'DSA Revision', 1, 10, '2024-11-30', FALSE),
(2, 'Machine Learning', 2, 3, '2024-12-30', FALSE),
(3, 'Basic Statistics 1', 2, 2, '2024-10-30', FALSE),
(4, 'DSA Revision 2', 1, 10, '2050-11-30', FALSE),
(5, 'Machine Learning Revision', 2, 3, '2050-12-30', FALSE),
(6, 'OOP Revision', 1, 10, '2050-12-30', FALSE);

-- quiz_attempt: defaults for attempts and high_score can be set in PostgreSQL as well
INSERT INTO quiz_attempt (quiz_id, user_id) VALUES
(1, '230001'),
(2, '230002'),
(1, '230003'),
(1, '230004'),
(1, '230005'),
(1, '230006'),
(2, '230007'),
(2, '230008'),
(4, '230001'),
(4, '230003'),
(4, '230004'),
(4, '230005'),
(4, '230006'),
(6, '230001'),
(6, '230003'),
(6, '230004'),
(6, '230005'),
(6, '230006');

-- question_id is SERIAL in PostgreSQL.
INSERT INTO question (question_id, question_body, topic_id, ans_id, is_removed, choice_1, choice_2, choice_3, choice_4) VALUES
(1, 'What is the time complexity of removing a node from tail of a Singly Linked List without a tail pointer?', 1, 1, FALSE, 'O(n)', 'O(1)', 'O(log n)', 'O(n log n)'),
(2, 'An ArrayList can be instantiated with a specified initial capacity.', 1, 1, FALSE, 'True', 'False', NULL, NULL),
(3, 'A Linked List can be accessed at any point with time complexity of O(1)', 1, 2, FALSE, 'True', 'False', NULL, NULL),
(4, 'Logistic Regression is used for classification.', 2, 1, FALSE, 'True', 'False', NULL, NULL),
(5, 'Which of the given machine learning is NOT a supervised learning technique?', 2, 3, FALSE, 'Linear Regression', 'Decision Tree', 'K-nearest neighbour', 'K-means clustering'),
(6, 'Which of the following is NOT an ensemble machining learning model?', 2, 3, FALSE, 'Support Vector Machine', 'Random Forest', 'XGBoost', 'CatBoost'),
(7, 'What is the median value in the following number sequence: 2, 3, 3, 4, 4, 6, 8, 10', 3, 3, FALSE, '6', '1.5', '4', '12'),
(8, 'What is the mean value in the following number sequence: 2, 3, 3, 4, 4, 6, 8, 10', 3, 2, FALSE, '4', '5', '8', '6'),
(9, 'What does the S in SOLID stand for?', 4, 3, FALSE, 'Special Responsibility Principle', 'Sole Responsibility Principle', 'Single Responsibility Principle', 'Single Reversible Principle'),
(10, 'Using interfaces allows to achieve 100% abstraction', 4, 1, FALSE, 'True', 'False', NULL, NULL),
(11, 'What does the O in SOLID stand for?', 4, 2, FALSE, 'Only-Extendable Principle', 'Open-Closed Principle', 'Ordered Principle', 'Open-Extended Principle'),
(12, 'What does the I in SOLID stand for?', 4, 4, FALSE, 'Internal Segregation', 'Inside Segregation', 'Interface Specialisation', 'Interface Segregation'),
(13, 'What does the L in SOLID stand for?', 4, 1, FALSE, 'Liskov Substitution Principle', 'Liskov Super-sub-class Principle', 'Liskov Principle', 'Lesser Substitution Principle');

-- quiz_question does not need changes as this is straightforward
INSERT INTO quiz_question (quiz_id, question_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 4),
(2, 5),
(2, 6),
(3, 7),
(3, 8),
(4, 1),
(4, 2),
(4, 3),
(5, 4),
(5, 5),
(5, 6),
(6, 9),
(6, 10),
(6, 11),
(6, 12),
(6, 13);