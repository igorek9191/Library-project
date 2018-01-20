DROP TABLE IF EXISTS `Books`;
CREATE TABLE IF NOT EXISTS `Books`(
  `BookID`      INT(4) NOT NULL PRIMARY KEY,
  `Name`        VARCHAR(255) NOT NULL,
  `Person_Id`   INT,
);

DROP TABLE IF EXISTS `Persons`;
CREATE TABLE IF NOT EXISTS `Persons`(
  `Id`              INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `Full_Name`       VARCHAR(255) NOT NULL,
  `Phone_Number`    VARCHAR (20) NOT NULL,
);

DROP TABLE IF EXISTS `History`;
CREATE TABLE IF NOT EXISTS `History`(
  `Id`                INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `Sys_Creation_Date` VARCHAR(255) NOT NULL,
  `BookID`            INT(4) NOT NULL,
  `Book_Name`         VARCHAR(255) NOT NULL,
  `Peson_Name`        VARCHAR(255) NOT NULL,
  `Phone_Number`      VARCHAR(20) NOT NULL,
  `Given_Date`        VARCHAR(255),
  `Return_Date`       VARCHAR(255),
);

