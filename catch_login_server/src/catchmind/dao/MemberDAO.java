package catchmind.dao;

import catchmind.vo.MemberVO;

public interface MemberDAO {
	// 아이디 중복 체크 : 0
	// return true : 사용가능, false : 사용불가(중복아이디)
	public boolean checkId(String id);
	
	// 회원 가입 : 1
	// param : MemberVO - 회원정보 / return : true 회원가입 성공, false : 회원가입 실패
	public boolean joinMember(MemberVO vo);
	
	// 로그인 : 2
	// param : MemberVO - id, pw / return : MemberVO 로그인 성공 시 회원정보, 실패 시 null
	public MemberVO loginMember(MemberVO vo);
}
