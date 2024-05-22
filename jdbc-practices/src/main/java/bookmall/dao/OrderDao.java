package bookmall.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bookmall.vo.OrderBookVo;
import bookmall.vo.OrderVo;

public class OrderDao {
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

	public int insert(OrderVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("insert into orders values(null,?,?,?,?,?)");
			PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		) {
			
			pstmt1.setString(1, vo.getNumber());
			pstmt1.setString(2, vo.getStatus());
			pstmt1.setInt(3, vo.getPayment());
			pstmt1.setString(4, vo.getShipping());			
			pstmt1.setLong(5, vo.getUserNo());
			result = pstmt1.executeUpdate();
			
			
			ResultSet rs = pstmt2.executeQuery();
			vo.setNo(rs.next() ?  rs.getLong(1) : null);
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
		
	}
	
	public List<OrderVo> findAll() {
		List<OrderVo> result = new ArrayList<>();

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select * from orders");
				ResultSet rs = pstmt.executeQuery();) {
			while (rs.next()) {
				Long no = rs.getLong(1);
				String number = rs.getString(2);
				String status = rs.getString(3);
				int payment = rs.getInt(4);
				String shipping = rs.getString(5);
				Long userNo = rs.getLong(6);
			
				OrderVo vo = new OrderVo();
				vo.setNo(no);
				vo.setNumber(number);
				vo.setStatus(status);
				vo.setPayment(payment);
				vo.setShipping(shipping);
				vo.setUserNo(userNo);
				result.add(vo);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}

	public int insertBook(OrderBookVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("insert into order_book values(?,?,?,?)");
		) {
			pstmt1.setInt(1, vo.getQuantity());
			pstmt1.setLong(2, vo.getBookNo());
			pstmt1.setLong(3, vo.getOrderNo());
			pstmt1.setInt(4, vo.getPrice());
			result = pstmt1.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		return result;
		
	
	}

	public OrderVo findByNoAndUserNo(Long No, Long userNo) {
		OrderVo orderVo = new OrderVo();
		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select * from orders where user_no=? and no=?");
				) {
			pstmt.setLong(1,userNo);
			pstmt.setLong(2, No);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				Long no = rs.getLong(1);
				String number = rs.getString(2);
				String status = rs.getString(3);
				int price = rs.getInt(4);
				String address = rs.getString(5);
				
				orderVo.setNo(no);
				orderVo.setNumber(number);
				orderVo.setStatus(status);
				orderVo.setPayment(price);
				orderVo.setShipping(address);
				orderVo.setUserNo(userNo);
			}
			else return null;
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		return orderVo;
	}

	public List<OrderBookVo> findBooksByNoAndUserNo(Long No, Long userNo) {
		List<OrderBookVo> result = new ArrayList<>();

		try (Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("select b.quantity, b.book_no, b.order_no, b.price, d.title "
						+ "from orders a, order_book b, user c, book d "
						+ "where a.no=b.order_no and "
						+ "b.book_no=d.no and "
						+ "a.user_no = c.no and "
						+ "a.no=? and c.no=?");
				
				) {
			pstmt.setLong(1, No);
			pstmt.setLong(2, userNo);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int quantity = rs.getInt(1);
				Long bookNo = rs.getLong(2);
				Long orderNo = rs.getLong(3);
				int price = rs.getInt(4);
				String bookTitle = rs.getString(5);
			
				OrderBookVo vo = new OrderBookVo();
				vo.setQuantity(quantity);
				vo.setBookNo(bookNo);
				vo.setOrderNo(orderNo);
				vo.setPrice(price);
				vo.setBookTitle(bookTitle);
				result.add(vo);
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		return result;
	}

	public void deleteBooksByNo(Long no) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from order_book where order_no=?");
			) {
				pstmt.setLong(1, no);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		
	}

	public void deleteByNo(Long no) {
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement("delete from orders where no=?");
			) {
				pstmt.setLong(1, no);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		
	}
}
