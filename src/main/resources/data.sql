/* Clear previous data */
DELETE FROM course;
DELETE FROM user;
DELETE FROM topic;
DELETE FROM quiz;
DELETE FROM quiz_attempt;
DELETE FROM question;
DELETE FROM quiz_question;

/* course_id is autoincremented. 
(course_id, course_name) */
INSERT INTO course VALUES
(1, 'Software Engineering'),
(2, 'Data Science and Analytics')
;

/* topic_id is auto incremented. Format: (topic_id, topic_name, course_id) */
INSERT INTO topic VALUES
(1, 'Data Structures and Algorithms in Java', 1),
(2, 'Machine Learning', 2),
(3, 'Statistics', 2)
;

/* user_id format: YYXXXX where YY is last 2 digit for year of enrollment or joining, XXXX is 4-digit running serial number 
(user_id, password, first_name, last_name, last_login_date, course_id, is_lecturer) */
INSERT INTO user VALUES
('230001', 'tenaciousD','Jack', 'Black', '2024-08-01', 1, 1),
('230002', 'muda', 'Dio', 'Brando', '2024-08-02', 2, 1),

('230003', 'risingforce', 'Yngwie', 'Malmsteen', '2024-07-29', 1, 0),
('230004', 'topgun' ,'Tom', 'Cruise', '2024-08-01', 1, 0),
('230005', 'racerx', 'Paul', 'Gilbert', '2024-07-30', 1, 0),
('230006', 'cream', 'Eric', 'Johnson', '2024-08-03', 1, 0),

('230007', 'ora', 'Jotaro', 'Kujo', '2024-08-02', 2, 0),
('230008', 'purplehermit', 'Joseph', 'Joestar', '2024-08-02', 2, 0)
;

/* quiz 
quiz_id is auto-incremented  
(quiz_id, quiz_name, course_id, max_attempts, due_date, is_removed) */
INSERT INTO quiz VALUES
(1, 'DSA Revision', 1, 10, '2024-11-30', 0),
(2, 'Machine Learning', 2, 3, '2024-12-30', 0),
(3, 'Basic Statistics 1', 2, 2, '2024-10-30', 0)
;

/* quiz_attempt 
attempts DEFAULT 0 and high_score DEFAULT 0
(quiz_id, user_id, attempts, high_score)*/
INSERT INTO quiz_attempt (quiz_id, user_id) VALUES
(1, 230001),
(2, 230002),
(1, 230003),
(1, 230004),
(1, 230005),
(1, 230006),
(2, 230007),
(2, 230008)
;

/* question
question_id is auto-incremented 
(question_id, question_body, topic_id, ans_id, is_removedm choice_1, choice_2, choice_3, choice_4) */
INSERT INTO question VALUES
(1, 'What is the time complexity of removing a node from tail of a Singly Linked List without a tail pointer?', 1, 1, 0,
'O(n)', 'O(1)', 'O(log n)', 'O(n log n)'),
(2, 'An ArrayList can be instantiated with a specified initial capacity.', 1, 1, 0,
'True', 'False', NULL, NULL),
(3, 'A Linked List can be accessed at any point with time complexity of O(1)', 1, 1, 0,
'True', 'False', NULL, NULL),
(4, 'Logistic Regression is used for classification.', 2, 1, 0,
'True', 'False', NULL, NULL),
(5, 'Which of the given machine learning is NOT a supervised learning technique?', 2, 3, 0,
'Linear Regression', 'Decision Tree', 'K-nearest neighbour', 'K-means clustering'), 
(6, 'Which of the following is NOT an ensemble machining learning model?', 2, 3, 0,
'Support Vector Machine', 'Random Forest', 'XGBoost', 'CatBoost'),
(7, 'What is the median value in the following number sequence: 2, 3, 3, 4, 4, 6, 8, 10', 3, 3, 0,
'6','1.5','4','12'),
(8, 'What is the mean value in the following number sequence: 2, 3, 3, 4, 4, 6, 8, 10', 3, 2, 0,
'4', '5','8','6')
;

/* quiz_question 
(quiz_id, question_id) */
INSERT INTO quiz_question VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 4),
(2, 5),
(2, 6),
(3, 7),
(3, 8)
;