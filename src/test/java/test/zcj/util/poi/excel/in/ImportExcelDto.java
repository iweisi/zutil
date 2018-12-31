package test.zcj.util.poi.excel.in;

import java.util.ArrayList;
import java.util.List;

import test.zcj.util.poi.excel.TestUser;

import com.zcj.util.poi.excel.ExcelResources;

/** 
 * 题目数据Excel导入帮助类
 * 
 * @author zouchongjin@sina.com
 * @data 2014年4月3日
 */
public class ImportExcelDto {
	
	private String role;// 角色
	private String username;// 账号
	private String password;// 密码
	private String realname;// 真实姓名
	
	public static List<TestUser> toEntityList(List<ImportExcelDto> dtoList) {
		if (dtoList == null || dtoList.size() == 0) {
			return new ArrayList<TestUser>();
		}
		List<TestUser> list = new ArrayList<TestUser>();
		
		for (ImportExcelDto d : dtoList) {
			TestUser t = new TestUser();
			t.setPassword(d.getPassword());
			t.setRealname(d.getRealname());
			t.setRole(d.getRole());
			t.setUsername(d.getUsername());
			list.add(t);
		}
		
		return list;
	}
	
	@ExcelResources(order = 1)
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@ExcelResources(order = 2)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@ExcelResources(order = 3)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@ExcelResources(order = 4)
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}

}
