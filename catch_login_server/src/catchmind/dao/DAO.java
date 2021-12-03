package catchmind.dao;

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
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/catchmind","root","12345");
		} catch (ClassNotFoundException e) {
			System.out.println("driver?");
		} catch (SQLException e) {
			System.out.println("db connection?");
		}
	}
}
