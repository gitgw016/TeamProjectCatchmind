package push_man.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO {
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public DAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/pushman",
				"pushman",
				"12345"
			);
		} catch (ClassNotFoundException e) {
			System.out.println("Driver class가 존재하지 않음");
		} catch (SQLException e) {
			System.out.println("db 연결 정보가 일치하지 않음");
		}
		
	}
	
	
	
	
	
	

}
