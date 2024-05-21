package bookmall.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import bookmall.vo.UserVo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDaoTest {
	private static UserVo mockUserVo01 = new UserVo("데스트유저01", "test01@test.com", "1234", "010-0000-0000");
	private static UserVo mockUserVo02 = new UserVo("데스트유저02", "test02@test.com", "1234", "010-1111-1111");
	private static UserDao userDao = new UserDao(); 
	@BeforeAll
	public static void setUp() {
		// 사용자 추가(2명)
		userDao.insert(mockUserVo01);
		userDao.insert(mockUserVo02);
	}
	
	@Test
	@Order(1)
	public void testUser() {
		System.out.println(userDao.findAll().toString());
		assertEquals(2, userDao.findAll().size());
	}
	@Test
	@Order(2)
	public void testDeleteByNo() {
		// 사용자
		userDao.deleteByNo(mockUserVo01.getNo());
		userDao.deleteByNo(mockUserVo02.getNo());
		assertEquals(0,userDao.findAll().size());
	}
	
}
