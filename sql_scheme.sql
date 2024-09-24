CREATE DATABASE DictionarySecond;

CREATE TABLE WGroups
(
	Id INT AUTO_INCREMENT PRIMARY KEY,
	Name VARCHAR(200)
);

CREATE TABLE Wordbooks
(
	Id INT AUTO_INCREMENT PRIMARY KEY,
	WGroupId INT,
	Name VARCHAR(200),
	Result INT,
	LastDate VARCHAR(15),
	FOREIGN KEY (WGroupId) REFERENCES WGroups(Id) ON DELETE CASCADE
);

CREATE TABLE Words
(
	Id INT AUTO_INCREMENT PRIMARY KEY,
	WordbookId INT,
	RussianWord VARCHAR(200),
	ForeignWord VARCHAR(200),
	RussianExample VARCHAR(400),
	ForeignExample VARCHAR(400),
	Type INT,
	FOREIGN KEY (WordbookId) REFERENCES Wordbooks(Id) ON DELETE CASCADE
);

INSERT INTO WGroups(Name)
VALUES
('First'),
('Second');