package bookmall.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import bookmall.vo.BookVo;
import bookmall.vo.CartVo;
import bookmall.vo.UserVo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartDaoTest {
	private static CartVo mockCartVo01 = new CartVo();
	private static CartVo mockCartVo02 = new CartVo();
	private static CartDao cartDao = new CartDao();
	private static UserVo mockUserVo01 = new UserVo("데스트유저01", "test01@test.com", "1234", "010-0000-0000");
	private static BookVo mockBookVo01 = new BookVo("과학혁명의 구조", 20000);
	private static BookVo mockBookVo02 = new BookVo("J2EE Development Without EJB", 32000);
	private static UserDao userDao = new UserDao(); 
	private static BookDao bookDao = new BookDao();
	
	@BeforeAll
	public static void setUp() {
		userDao.insert(mockUserVo01);
		mockBookVo01.setCategoryNo(1L);
		mockBookVo02.setCategoryNo(1L);
		bookDao.insert(mockBookVo01);
		bookDao.insert(mockBookVo02);
		
	}
	
	@Test
	@Order(1)
	public void testInsert() {
		// 카트 담기(2개)
				mockCartVo01.setUserNo(mockUserVo01.getNo());
				mockCartVo01.setBookNo(mockBookVo01.getNo());
				mockCartVo01.setQuantity(1);
				cartDao.insert(mockCartVo01);
				
				mockCartVo02.setUserNo(mockUserVo01.getNo());
				mockCartVo02.setBookNo(mockBookVo02.getNo());
				mockCartVo02.setQuantity(2);		
				cartDao.insert(mockCartVo02);
	}
	@Test
	public void testCart() {
		List<CartVo> list = cartDao.findByUserNo(mockUserVo01.getNo());
		
		assertEquals(2, list.size());		

		assertEquals(mockBookVo01.getNo(), list.get(0).getBookNo());
		assertEquals(mockBookVo01.getTitle(), list.get(0).getBookTitle());
		assertEquals(mockCartVo01.getQuantity(), list.get(0).getQuantity());

		assertEquals(mockBookVo02.getNo(), list.get(1).getBookNo());
		assertEquals(mockBookVo02.getTitle(), list.get(1).getBookTitle());		
		assertEquals(mockCartVo02.getQuantity(), list.get(1).getQuantity());
	}
}
