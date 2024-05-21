package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.CartVo;
import bookmall.vo.UserVo;

public class CartDao {
	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			String url = "jdbc:mariadb://192.168.0.197:3306/bookmall?charset=utf8";
			conn = DriverManager.getConnection(url, "bookmall", "bookmall");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}

		return conn;
	}
	public int insert(CartVo vo) {
		int result = 0;

		try (Connection conn = getConnection();
				PreparedStatement pstmt1 = conn.prepareStatement("insert into cart values(?,?,?)");
			) {
			pstmt1.setInt(1, vo.getQuantity());
			pstmt1.setLong(2, vo.getUserNo());
			pstmt1.setLong(3, vo.getBookNo());
			result = pstmt1.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}

	public List<CartVo> findAll() {
		List<CartVo> result = new ArrayList<>();

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select * from cart");
				ResultSet rs = pstmt.executeQuery();) {
			while (rs.next()) {
				int quantity = rs.getInt(1);
				Long userNo = rs.getLong(2);
				Long BookNo = rs.getLong(3);
				
				CartVo vo = new CartVo();
				vo.setQuantity(quantity);
				vo.setUserNo(userNo);
				vo.setBookNo(BookNo);
				result.add(vo);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}
	public void deleteByUserNoAndBookNo(Long userNo, Long no) {
		int result = 0;
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from cart where user_no=? and book_no=?");) {
			pstmt.setLong(1, no);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		return result;
		
	}
}
