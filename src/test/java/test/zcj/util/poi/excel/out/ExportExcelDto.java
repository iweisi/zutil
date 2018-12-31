package test.zcj.util.poi.excel.out;

import java.util.ArrayList;
import java.util.List;

import test.zcj.util.poi.excel.TestUser;

import com.zcj.util.poi.excel.ExcelResources;

/**
 * 导出Excel辅助类
 * 
 * @author zouchongjin@sina.com
 * @data 2014年3月31日
 */
public class ExportExcelDto {
	
	public static final String FILENAME_TEMPLAT = "template.xls";// 源文件名（不要用英文，不然打包的时候文件名会乱码）
	public static final String FILENAME_EXPORT = "XXXXX用户账号.xls";// 导出的文件名

	private String xh;
	private String role;
	private String realname;
	private String username;
	private String password;
	
	public static List<ExportExcelDto> byEntityList(List<TestUser> userList) {
		if (userList == null || userList.size() == 0) {
			return new ArrayList<ExportExcelDto>();
		}
		List<ExportExcelDto> list = new ArrayList<ExportExcelDto>();
		
		int xh = 1;
		for (TestUser ws : userList) {
			ExportExcelDto s = new ExportExcelDto();
			s.setXh(String.valueOf(xh++));
			s.setPassword(ws.getPassword());
			s.setRealname(ws.getRealname());
			s.setUsername(ws.getUsername());
			s.setRole(ws.getRole());
			list.add(s);
		}

		return list;
	}

	@ExcelResources(order = 1)
	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	@ExcelResources(order = 2)
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@ExcelResources(order = 3)
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	@ExcelResources(order = 4)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@ExcelResources(order = 5)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
