DROP DATABASE IF EXISTS equiz;
CREATE DATABASE IF NOT EXISTS equiz;
USE equiz;

DROP TABLE IF EXISTS course,
                     user,
                     topic,
                     quiz, 
                     quiz_attempt,
                     question,
                     quiz_question,
                     choice;

/*!50503 set default_storage_engine = InnoDB */;
/*!50503 select CONCAT('storage engine: ', @@default_storage_engine) as INFO */;

CREATE TABLE course (
    course_id     INT AUTO_INCREMENT             NOT NULL ,
    course_name   VARCHAR(50)     NOT NULL,
    PRIMARY KEY (course_id),
    UNIQUE  KEY (course_name)
);

/* Note - null date means the user has never logged in on the platform*/
CREATE TABLE user (
    user_id             INT(8)          NOT NULL,
    password            VARCHAR(30)     NOT NULL,
    first_name          VARCHAR(30)     NOT NULL,
    last_name           VARCHAR(30)     NOT NULL,
    last_login_date     DATE,
    course_id           INT             NOT NULL,
    is_lecturer         BOOLEAN         NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (course_id)  REFERENCES course (course_id)    ON DELETE CASCADE
);

CREATE TABLE topic (
   topic_id         INT AUTO_INCREMENT            NOT NULL,
   topic_name      VARCHAR(60)         NOT NULL,
   course_id        INT             NOT NULL,
   FOREIGN KEY (course_id)  REFERENCES course (course_id)    ON DELETE CASCADE,
   PRIMARY KEY (topic_id)
); 


CREATE TABLE quiz (
    quiz_id      INT AUTO_INCREMENT            NOT NULL,
    quiz_name     VARCHAR(60)    NOT NULL,
    course_id   INT             NOT NULL,
    max_attempts    INT         NOT NULL,
    due_date        DATE       NOT NULL,
    is_removed      BOOLEAN     NOT NULL,
    FOREIGN KEY (course_id)  REFERENCES course   (course_id)  ON DELETE CASCADE,
    PRIMARY KEY (quiz_id)
);

CREATE TABLE quiz_attempt (
    quiz_id     INT     NOT NULL,
    user_id     INT     NOT NULL,
    high_score  INT     NOT NULL    DEFAULT 0,
    attempts    INT     NOT NULL    DEFAULT 0,
    FOREIGN KEY (quiz_id) REFERENCES quiz (quiz_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE,
    PRIMARY KEY (quiz_id, user_id)    
);

CREATE TABLE question (
    question_id      INT AUTO_INCREMENT            NOT NULL,
    question_body      TEXT(600)     NOT NULL,
    topic_id        INT             NOT NULL,
    ans_id          INT             NOT NULL,
    is_removed      INT             NOT NULL DEFAULT 0,
    choice_1        VARCHAR(200)    NOT NULL,
    choice_2        VARCHAR(200)    NOT NULL,
    choice_3        VARCHAR(200),
    choice_4        VARCHAR(200),       
    FOREIGN KEY (topic_id) REFERENCES topic (topic_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (question_id)
) 
; 

CREATE TABLE quiz_question (
    quiz_id     INT         NOT NULL,
    question_id INT         NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES quiz (quiz_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (question_id) REFERENCES question (question_id) ON UPDATE CASCADE,
    PRIMARY KEY (quiz_id, question_id)   
)
;