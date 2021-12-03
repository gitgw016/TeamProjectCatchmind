package push_man.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class RankingVO implements Serializable{
	
	private static final long serialVersionUID = -3300444108894971204L;
	
	public int ranking;			//랭킹
	// 이름은 중복될 수 있으니 회원번호도 표기
	public int num;				//회원번호
	public String name;			//회원이름
	public int stage;			// 스테이지 번호
	public long score;			// clear 타임 - 점수
	public long regdate;		// clear한 시간
	
	public RankingVO() {}

	public RankingVO(int ranking, int num, String name, int stage, long score, long regdate) {
		this.ranking = ranking;
		this.num = num;
		this.name = name;
		this.stage = stage;
		this.score = score;
		this.regdate = regdate;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public String getScore() {
		return new SimpleDateFormat("HH:mm:ss:SS").format(this.score);
	}

	public void setScore(long score) {
		this.score = score;
	}

	public String getRegdate() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.regdate);
	}

	public void setRegdate(long regdate) {
		this.regdate = regdate;
	}

	@Override
	public String toString() {
		return "RankingVO [ranking=" + ranking + ", num=" + num + ", memberName=" + name + ", stage=" + stage
				+ ", score=" + getScore() + ", regdate=" + getRegdate() + "]";
	}

}





