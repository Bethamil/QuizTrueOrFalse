CREATE SCHEMA QuizGameTrueOrFalse;

CREATE TABLE QuizGameTrueOrFalse.scores(
`id` int AUTO_INCREMENT PRIMARY KEY,
`name` VARCHAR(64),
`score` int,
`category` varchar(64));

CREATE USER 'QuizTrueFalseAdmin'@'localhost' IDENTIFIED BY 'QuizPW';
GRANT ALL PRIVILEGES ON QuizGameTrueOrFalse.* TO 'QuizTrueFalseAdmin'@'localhost';