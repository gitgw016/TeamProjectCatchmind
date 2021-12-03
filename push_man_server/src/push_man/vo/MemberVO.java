package push_man.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

// 회원정보를 저장하고 통신에 사용할 Class
public class MemberVO implements Serializable{
	
	// class가 재컴파일되어도 
	// 동일한 설계도로 인식할 수 있도록 지정
	private static final long serialVersionUID = 3459181516966951326L;
	// 메뉴 번호
	private int order;	
	// 회원번호
	private int memberNum;
	// 회원 닉네임
	private String memberName;
	// 회원 아이디
	private String memberId;
	// 회원 비밀번호
	private String memberPw;
	// 회원 가입일
	private long regdate;
	// 명령에따른 처리 성공 여부
	private boolean success;
	
	public MemberVO() {}

	// 중복 아이디 체크용
	public MemberVO(String memberId) {
		this.memberId = memberId;
	}
	// 회원 로그인 및 일치정보 확인용 생성자
	public MemberVO(String memberId, String memberPw) {
		this.memberId = memberId;
		this.memberPw = memberPw;
	}

	// 회원가입용 생성자
	public MemberVO(String memberName, String memberId, String memberPw) {
		this.memberName = memberName;
		this.memberId = memberId;
		this.memberPw = memberPw;
	}
	
	// 모든 회원 정보를 저장할 생성자
	public MemberVO(int memberNum, String memberName, String memberId, String memberPw, long regdate) {
		this.memberNum = memberNum;
		this.memberName = memberName;
		this.memberId = memberId;
		this.memberPw = memberPw;
		this.regdate = regdate;
	}

	// alt + s + r
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberPw() {
		return memberPw;
	}

	public void setMemberPw(String memberPw) {
		this.memberPw = memberPw;
	}

	public String getRegdate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(this.regdate));
	}

	public void setRegdate(long regdate) {
		this.regdate = regdate;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	// alt + s + v : override
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MemberVO) {
			MemberVO m = (MemberVO)obj;
			if(this.memberId.equals(m.getMemberId())
				&&
				this.memberPw.equals(m.getMemberPw())
				) {
				// id 와 pw가 일치하면 동일한 객체로 확인
				return true;
			}
		}
		return false;
	}

	// alt + s + s +s
	@Override
	public String toString() {
		return "MemberVO [order=" + order + ", memberNum=" + memberNum + ", memberName=" + memberName + ", memberId="
				+ memberId + ", memberPw=" + memberPw + ", regdate=" + regdate + ", success=" + success + "]";
	}

}








