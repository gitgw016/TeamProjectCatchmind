CREATE TABLE userTbl(
	mNum INT PRIMARY KEY auto_increment,
	mID VARCHAR(100) NOT NULL UNIQUE,
	mPw VARCHAR(100) NOT NULL,
	name VARCHAR(100)
);

INSERT INTO userTbl(mID,mPw) VALUES('root', 'root');

SELECT * FROM userTbl;

DROP table userTbl;