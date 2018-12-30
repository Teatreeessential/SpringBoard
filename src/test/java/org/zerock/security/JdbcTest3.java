package org.zerock.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
	"file:src/main/webapp/WEB-INF/spring/root-context.xml",
	"file:src/main/webapp/WEB-INF/spring/security-context.xml"
})
@Log4j
public class JdbcTest3 {
	@Setter(onMethod_=@Autowired)
	private PasswordEncoder pwencoder;
	@Setter(onMethod_=@Autowired)
	private DataSource ds;
	
	@Test
	public void testInsertMember() {
		String sql = "update tbl_member set userpw =? where userid='admin90'";
		
			
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, pwencoder.encode("pw90"));
				System.out.println(pwencoder.encode("pw90"));
				
				pstmt.executeUpdate();
				con.commit();
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					con.close();
					
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	}
}
