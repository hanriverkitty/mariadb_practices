package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.CartVo;

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
		System.out.println("i");
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select a.quantity, a.user_no, a.book_no, b.title from cart a, book b where a.book_no=b.no");
				ResultSet rs = pstmt.executeQuery();
				) {
			System.out.println("hi");
			System.out.println(rs);
			while (rs.next()) {
				int quantity = rs.getInt(1);
				Long userNo = rs.getLong(2);
				Long BookNo = rs.getLong(3);
				String title = rs.getString(4);
				CartVo vo = new CartVo();
				vo.setQuantity(quantity);
				vo.setUserNo(userNo);
				vo.setBookNo(BookNo);
				vo.setBookTitle(title);
				System.out.println(vo.toString());
				result.add(vo);
			}
			
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}
	public int deleteByUserNoAndBookNo(Long userNo, Long bookNo) {
		int result = 0;
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from cart where user_no=? and book_no=?");) {
			pstmt.setLong(1, userNo);
			pstmt.setLong(2, bookNo);
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		return result;
		
	}
	public List<CartVo> findByUserNo(Long no) {
		List<CartVo> result = new ArrayList<>();
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select a.quantity, a.user_no, a.book_no, b.title from cart a, book b where a.book_no=b.no");
				ResultSet rs = pstmt.executeQuery();
				) {
			while (rs.next()) {
				int quantity = rs.getInt(1);
				Long userNo = rs.getLong(2);
				Long BookNo = rs.getLong(3);
				String title = rs.getString(4);
				CartVo vo = new CartVo();
				vo.setQuantity(quantity);
				vo.setUserNo(userNo);
				vo.setBookNo(BookNo);
				vo.setBookTitle(title);
				result.add(vo);
			}
			
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}
}
