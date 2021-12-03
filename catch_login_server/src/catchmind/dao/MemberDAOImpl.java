package catchmind.dao;

import java.sql.SQLException;

import catchmind.vo.MemberVO;

public class MemberDAOImpl extends DAO implements MemberDAO {

	@Override
	public boolean checkId(String id) {
		try {
			String sql = "SELECT * FROM userTbl WHERE mID = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			try {
				if(rs !=null)rs.close();
				if(pstmt !=null)pstmt.close();
			} catch (SQLException e) {}
		}
		return true;
	}

	@Override
	public boolean joinMember(MemberVO vo) {
		String sql = "INSERT INTO userTbl (name, mID, mPw) "+" VALUES(?,?,?)";
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
				if(pstmt !=null)pstmt.close();
			} catch (SQLException e) {}
		}
		return false;
	}

	@Override
	public MemberVO loginMember(MemberVO vo) {
		String sql = "SELECT * FROM userTbl "+ " WHERE mID = ? AND mPw = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getMemberId());
			pstmt.setString(2, vo.getMemberPw());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String name = rs.getString("name");
				vo.setMemberName(name);
				int num = rs.getInt("mNum");
				vo.setMemberNum(num);
				vo.setSuccess(true);
			}
		} catch (SQLException e) {
			
		}finally {
			try {
				if(rs != null)rs.close();
				if(pstmt != null)pstmt.close();
			} catch (SQLException e) {}
		}
		return vo;
	}

}
