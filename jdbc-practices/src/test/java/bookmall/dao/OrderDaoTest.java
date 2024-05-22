package bookmall.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import bookmall.vo.BookVo;
import bookmall.vo.CategoryVo;
import bookmall.vo.OrderBookVo;
import bookmall.vo.OrderVo;
import bookmall.vo.UserVo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderDaoTest {
	private static UserVo mockUserVo01 = new UserVo("데스트유저01", "test01@test.com", "1234", "010-0000-0000");

	private static CategoryVo mockCategoryVo01 = new CategoryVo("인문");
	private static CategoryVo mockCategoryVo02 = new CategoryVo("컴퓨터/IT");
	private static CategoryVo mockCategoryVo03 = new CategoryVo("예술");

	private static BookVo mockBookVo01 = new BookVo("과학혁명의 구조", 20000);
	private static BookVo mockBookVo02 = new BookVo("J2EE Development Without EJB", 32000);
	private static BookVo mockBookVo03 = new BookVo("서양미술사", 50000);

	private static OrderVo mockOrderVo = new OrderVo();

	private static OrderBookVo mockOrderBookVo01 = new OrderBookVo();
	private static OrderBookVo mockOrderBookVo02 = new OrderBookVo();

	private static UserDao userDao = new UserDao();
	private static CategoryDao categoryDao = new CategoryDao();
	private static BookDao bookDao = new BookDao();
	private static OrderDao orderDao = new OrderDao();

	@BeforeAll
	public static void setUp() {

		userDao.insert(mockUserVo01);

		// 카테고리 등록(3개)
		categoryDao.insert(mockCategoryVo01);
		categoryDao.insert(mockCategoryVo02);
		categoryDao.insert(mockCategoryVo03);

		// 서적 등록(3개)
		mockBookVo01.setCategoryNo(mockCategoryVo01.getNo());
		bookDao.insert(mockBookVo01);

		mockBookVo02.setCategoryNo(mockCategoryVo02.getNo());
		bookDao.insert(mockBookVo02);

		mockBookVo03.setCategoryNo(mockCategoryVo03.getNo());
		bookDao.insert(mockBookVo03);

		// 주문하기(1개)
		mockOrderVo.setUserNo(mockUserVo01.getNo());
		mockOrderVo.setNumber("20240520-000012");
		mockOrderVo.setPayment(82400);
		mockOrderVo.setShipping("서울시 은평구 진관3로 77 구파발 래미안 926-801");
		mockOrderVo.setStatus("배송준비");
		orderDao.insert(mockOrderVo);

		// 주문책(2개)
		mockOrderBookVo01.setOrderNo(mockOrderVo.getNo());
		mockOrderBookVo01.setBookNo(mockBookVo01.getNo());
		mockOrderBookVo01.setQuantity(1);
		mockOrderBookVo01.setPrice(20000);
		orderDao.insertBook(mockOrderBookVo01);

		mockOrderBookVo02.setOrderNo(mockOrderVo.getNo());
		mockOrderBookVo02.setBookNo(mockBookVo02.getNo());
		mockOrderBookVo02.setQuantity(2);
		mockOrderBookVo02.setPrice(64000);
		orderDao.insertBook(mockOrderBookVo02);
	}

	@Test
	@Order(1)
	public void testOrder() {
		OrderVo vo = null;

		vo = orderDao.findByNoAndUserNo(1234567L, mockUserVo01.getNo());
		assertNull(vo);

		vo = orderDao.findByNoAndUserNo(mockOrderVo.getNo(), mockUserVo01.getNo());
		assertNotNull(vo);
		assertEquals(mockOrderVo.getNumber(), vo.getNumber());
		assertEquals(mockOrderVo.getPayment(), vo.getPayment());
		assertEquals(mockOrderVo.getStatus(), vo.getStatus());
		assertEquals(mockOrderVo.getShipping(), vo.getShipping());

	}

	@Test
	@Order(2)
	public void testOrderBooks() {
		List<OrderBookVo> list = orderDao.findBooksByNoAndUserNo(mockOrderVo.getNo(), mockUserVo01.getNo());

		assertEquals(2, list.size());

		assertEquals(mockOrderBookVo01.getOrderNo(), list.get(0).getOrderNo());
		assertEquals(mockOrderBookVo01.getQuantity(), list.get(0).getQuantity());
		assertEquals(mockOrderBookVo01.getPrice(), list.get(0).getPrice());
		assertEquals(mockOrderBookVo01.getBookNo(), list.get(0).getBookNo());
		assertEquals(mockBookVo01.getTitle(), list.get(0).getBookTitle());

		assertEquals(mockOrderBookVo02.getOrderNo(), list.get(1).getOrderNo());
		assertEquals(mockOrderBookVo02.getQuantity(), list.get(1).getQuantity());
		assertEquals(mockOrderBookVo02.getPrice(), list.get(1).getPrice());
		assertEquals(mockOrderBookVo02.getBookNo(), list.get(1).getBookNo());
		assertEquals(mockBookVo02.getTitle(), list.get(1).getBookTitle());
	}

	@AfterAll
	public static void cleanUp() {
		// 주문책
		orderDao.deleteBooksByNo(mockOrderVo.getNo());

		// 주문
		orderDao.deleteByNo(mockOrderVo.getNo());

		// 서적
		bookDao.deleteByNo(mockBookVo01.getNo());
		bookDao.deleteByNo(mockBookVo02.getNo());
		bookDao.deleteByNo(mockBookVo03.getNo());

		// 카테고리
		categoryDao.deleteByNo(mockCategoryVo01.getNo());
		categoryDao.deleteByNo(mockCategoryVo02.getNo());
		categoryDao.deleteByNo(mockCategoryVo03.getNo());

		// 사용자
		userDao.deleteByNo(mockUserVo01.getNo());
	}
}
