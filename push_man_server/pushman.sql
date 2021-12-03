DROP TABLE IF EXISTS push_man_member;
-- push_man 회원정보
CREATE TABLE IF NOT EXISTS push_man_member(
	memberNum int(11) PRIMARY KEY AUTO_INCREMENT,
	memberName VARCHAR(50) DEFAULT null,
	memberId VARCHAR(100) UNIQUE NOT NULL,
	memberPw VARCHAR(100) NOT NULL,
	regdate BIGINT DEFAULT 0
);

DESC push_man_member;

commit;
SELECT * FROM push_man_member;

-- push_man 기록 정보
DROP TABLE IF EXISTS push_man_score;

CREATE TABLE IF NOT EXISTS push_man_score(
	num int(11) PRIMARY KEY auto_increment,
	score BIGINT(20) default 0,
	stage int(8) NOT NULL,
	regDate BIGINT(20) DEFAULT 0,
	memberNum int(11),
	CONSTRAINT fk_memberNum FOREIGN KEY(memberNum) 
	REFERENCES push_man_member(memberNum)
);

DESC push_man_score;

-- rank
SELECT *, rank() over(ORDER BY hisal DESC) AS ranking FROM salgrade;

INSERT INTO push_man_score(score,stage,regDate,memberNum) VALUES(650000001,1,16000000000,2);

truncate push_man_score;
commit;
-- 해당 회원의 스테이지 별 랭킹 정보
SELECT * FROM (SELECT * , rank() over(ORDER BY score ASC) AS ranking 
FROM push_man_score WHERE stage = 1) AS r WHERE r.memberNum = 1;
--  public int ranking;
--	public int num;				//회원번호
--	public String memberName;	//회원이름
--	public int stage;			// 스테이지 번호
--	public long score;			// clear 타임 - 점수
--	public long regdate;		// clear한 시간
-- 스테이지별 1~10위 까지 랭킹 정보
SELECT rank() over(order by score ASC) AS ranking,
pmm.memberNum AS num, pmm.memberName AS name,
pms.stage, pms.score, 
pms.regdate FROM 
pushman.push_man_score AS pms , pushman.push_man_member AS pmm 
WHERE pms.memberNum = pmm.memberNum AND pms.stage = 1 
LIMIT 10;





























