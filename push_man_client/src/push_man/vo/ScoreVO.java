package push_man.vo;

import java.io.Serializable;

public class ScoreVO implements Serializable{
	
	private static final long serialVersionUID = -8177391921612454810L;
	
	private int num;
	private long score;
	private int stage;
	private long regDate;
	private int memberNum;
	private int ranking;	// 순위

	public ScoreVO() {}
	
	public ScoreVO(long score, int stage, long regDate, int memberNum) {
		this.score = score;
		this.stage = stage;
		this.regDate = regDate;
		this.memberNum = memberNum;
	}

	public ScoreVO(int num, long score, int stage, long regDate, int memberNum, int ranking) {
		this.num = num;
		this.score = score;
		this.stage = stage;
		this.regDate = regDate;
		this.memberNum = memberNum;
		this.ranking = ranking;
	}

	// getter & setter & toString
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
	public long getRegDate() {
		return regDate;
	}
	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}
	public int getMemberNum() {
		return memberNum;
	}
	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	@Override
	public String toString() {
		return "ScoreVO [num=" + num + ", score=" + score + ", stage=" + stage + ", regDate=" + regDate + ", memberNum="
				+ memberNum + ", ranking=" + ranking + "]";
	}
		
}
