DROP SCHEMA IF EXISTS equiz CASCADE;
CREATE SCHEMA IF NOT EXISTS equiz;

DROP TABLE IF EXISTS equiz.quiz_question;
DROP TABLE IF EXISTS equiz.question;
DROP TABLE IF EXISTS equiz.quiz_attempt;
DROP TABLE IF EXISTS equiz.quiz;
DROP TABLE IF EXISTS equiz.topic;
DROP TABLE IF EXISTS equiz.user;
DROP TABLE IF EXISTS equiz.course;

CREATE TABLE equiz.course (
    course_id     SERIAL             NOT NULL,
    course_name   VARCHAR(50)        NOT NULL,
    PRIMARY KEY (course_id),
    UNIQUE (course_name)
);

CREATE TABLE equiz.user (
    user_id             INTEGER         NOT NULL,
    password            VARCHAR(30)     NOT NULL,
    first_name          VARCHAR(30)     NOT NULL,
    last_name           VARCHAR(30)     NOT NULL,
    last_login_date     DATE,
    course_id           INTEGER         NOT NULL,
    is_lecturer         BOOLEAN         NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (course_id)  REFERENCES equiz.course (course_id)    ON DELETE CASCADE
);

CREATE TABLE equiz.topic (
   topic_id         SERIAL            NOT NULL,
   topic_name       VARCHAR(60)       NOT NULL,
   course_id        INTEGER           NOT NULL,
   FOREIGN KEY (course_id)  REFERENCES equiz.course (course_id)    ON DELETE CASCADE,
   PRIMARY KEY (topic_id)
);

CREATE TABLE equiz.quiz (
    quiz_id         SERIAL            NOT NULL,
    quiz_name       VARCHAR(60)       NOT NULL,
    course_id       INTEGER           NOT NULL,
    max_attempts    INTEGER           NOT NULL,
    due_date        DATE             NOT NULL,
    is_removed      BOOLEAN          NOT NULL,
    FOREIGN KEY (course_id)  REFERENCES equiz.course (course_id)  ON DELETE CASCADE,
    PRIMARY KEY (quiz_id)
);

CREATE TABLE equiz.quiz_attempt (
    quiz_id     INTEGER     NOT NULL,
    user_id     INTEGER     NOT NULL,
    high_score  INTEGER     NOT NULL    DEFAULT 0,
    attempts    INTEGER     NOT NULL    DEFAULT 0,
    FOREIGN KEY (quiz_id) REFERENCES equiz.quiz (quiz_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES equiz.user (user_id) ON DELETE CASCADE,
    PRIMARY KEY (quiz_id, user_id)
);

CREATE TABLE equiz.question (
    question_id      SERIAL            NOT NULL,
    question_body    TEXT             NOT NULL,
    topic_id         INTEGER          NOT NULL,
    ans_id           INTEGER          NOT NULL,
    is_removed       BOOLEAN          NOT NULL DEFAULT FALSE,
    choice_1         VARCHAR(200)     NOT NULL,
    choice_2         VARCHAR(200)     NOT NULL,
    choice_3         VARCHAR(200),
    choice_4         VARCHAR(200),
    FOREIGN KEY (topic_id) REFERENCES equiz.topic (topic_id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (question_id)
);

CREATE TABLE equiz.quiz_question (
    quiz_id     INTEGER     NOT NULL,
    question_id INTEGER     NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES equiz.quiz (quiz_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (question_id) REFERENCES equiz.question (question_id) ON UPDATE CASCADE,
    PRIMARY KEY (quiz_id, question_id)
);