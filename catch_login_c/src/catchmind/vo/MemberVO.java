package catchmind.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemberVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3459181516966951326L;
	
	private int order;			// 명령 번호
	private boolean success;	// 명령에 따른 처리 성공 여부
	
	private int memberNum;		// 회원 번호
	private String memberName;	// 회원 닉네임
	private String memberId;	// 회원 아이디
	private String memberPw;	// 회원 비밀번호
	
	public MemberVO() {}
	
	// 중복 아이디 체크용
	public MemberVO(String memberId) {
		this.memberId = memberId;
	}
	
	// 회원 로그인 및 일치 정보 확인용 생성자
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
	public MemberVO(int memberNum, String memberName, String memberId, String memberPw) {
		this.memberNum = memberNum;
		this.memberName = memberName;
		this.memberId = memberId;
		this.memberPw = memberPw;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
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

		
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MemberVO) {
			MemberVO m = (MemberVO)obj;
			if(this.memberId.equals(m.getMemberId()) && this.memberPw.equals(m.getMemberPw())) {
				// id와 pw가 일치하면 동일한 객체로 확인
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "MemberVO [order=" + order + ", success=" + success + ", memberNum=" + memberNum + ", memberName="
				+ memberName + ", memberId=" + memberId + ", memberPw=" + memberPw + "]";
	}
	
}
