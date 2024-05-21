package bookmall.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import bookmall.vo.BookVo;
import bookmall.vo.CategoryVo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookDaoTest {
	private static BookVo mockBookVo01 = new BookVo("과학혁명의 구조", 20000);
	private static BookVo mockBookVo02 = new BookVo("J2EE Development Without EJB", 32000);
	private static BookVo mockBookVo03 = new BookVo("서양미술사", 50000);
	private static BookDao bookDao = new BookDao();
	private static CategoryVo mockCategoryVo01 = new CategoryVo("인문");
	private static CategoryVo mockCategoryVo02 = new CategoryVo("컴퓨터/IT");
	private static CategoryVo mockCategoryVo03 = new CategoryVo("예술");
	private static CategoryDao categoryDao = new CategoryDao();

	@BeforeAll
	public static void setUp() {
		// 카테고리 등록(3개)
		categoryDao.insert(mockCategoryVo01);
		categoryDao.insert(mockCategoryVo02);
		categoryDao.insert(mockCategoryVo03);
		
		// 서적 등록(3개)
		mockBookVo01.setCategoryNo(mockCategoryVo01.getNo());
		mockBookVo02.setCategoryNo(mockCategoryVo02.getNo());
		mockBookVo03.setCategoryNo(mockCategoryVo03.getNo());
		
	}
	@Test
	@Order(1)
	public void testInsert() {
		bookDao.insert(mockBookVo01);
		bookDao.insert(mockBookVo02);
		bookDao.insert(mockBookVo03);
		assertEquals(3,bookDao.findAll().size());
	}
	
	@Test
	@Order(2)
	public void testDeleteByNo() {
		bookDao.deleteByNo(mockBookVo01.getNo());
		bookDao.deleteByNo(mockBookVo02.getNo());
		bookDao.deleteByNo(mockBookVo03.getNo());
		assertEquals(0,bookDao.findAll().size());
	}
}
