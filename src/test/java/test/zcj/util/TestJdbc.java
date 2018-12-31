package test.zcj.util;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import com.zcj.util.UtilJdbc;
import com.zcj.util.jdbc.DbInfo;

public class TestJdbc {

	@Test
	public void t1() throws Exception {
		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://11.11.11.11:3306/mas", "ohedu", "ohedu123.");
		DbInfo info = UtilJdbc.getDbInfo(conn);
		if (conn != null) {
			conn.close();
			conn = null;
		}
		System.out.println(info);
	}

	@Test
	public void t2() {
		String result = UtilJdbc.query(UtilJdbc.DATABASETYPE_MYSQL, "192.168.1.88", 3306, "ohga_jiangxy", "root", "root",
				"select count(*) a from tbl_collect_car");
		System.out.println(result);
	}

}
