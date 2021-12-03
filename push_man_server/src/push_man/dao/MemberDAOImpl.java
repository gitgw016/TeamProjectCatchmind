package push_man.dao;

import java.sql.SQLException;

import push_man.vo.MemberVO;

public class MemberDAOImpl extends DAO implements MemberDAO{

	@Override
	public boolean checkId(String id) {
		try {
			String sql = "SELECT * FROM push_man_member WHERE memberId = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(rs != null)rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {}
		}
		return true;
	}

	@Override
	public boolean joinMember(MemberVO vo) {
		
		String sql = "INSERT INTO push_man_member("
				+ "memberName, memberId,memberPw,regdate) "
				+ "VALUES(?,?,?,now())";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getMemberName());
			pstmt.setString(2, vo.getMemberId());
			pstmt.setString(3, vo.getMemberPw());
			if(pstmt.executeUpdate() > 0) {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}finally {
			try {
				if(pstmt!=null)pstmt.close();
			} catch (SQLException e) {}
		}
		return false;
	}

	@Override
	public MemberVO loginMember(MemberVO vo) {
		
		String sql = "SELECT * FROM push_man_member "
				   + " WHERE memberId = ? AND memberPw = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getMemberId());
			pstmt.setString(2, vo.getMemberPw());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String name = rs.getString("memberName");
				vo.setMemberName(name);
				int num = rs.getInt("memberNum");
				vo.setMemberNum(num);
				long date = rs.getLong("regdate");
				vo.setRegdate(date);
				vo.setSuccess(true);
			}
		} catch (SQLException e) {
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch (SQLException e) {}
		}
		return vo;
	}

}









