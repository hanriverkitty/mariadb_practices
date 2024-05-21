package bookmall.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import bookmall.vo.CategoryVo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryDaoTest {
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
	}

	@Test
	@Order(1)
	public void testCategory() {
		assertEquals(3, categoryDao.findAll().size());
	}

	@Test
	@Order(2)
	public void testdeleteByno() {
		// 카테고리
		categoryDao.deleteByNo(mockCategoryVo01.getNo());
		categoryDao.deleteByNo(mockCategoryVo02.getNo());
		categoryDao.deleteByNo(mockCategoryVo03.getNo());
		assertEquals(0, categoryDao.findAll().size());
	}

}
