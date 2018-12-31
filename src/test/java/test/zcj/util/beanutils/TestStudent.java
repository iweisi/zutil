package test.zcj.util.beanutils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestStudent {

	private Integer id;
	private String name;
	private Date createTime;
	private Boolean iniv;
	private List<String> apps;
	private Map<String, String> bpps;
	private Long click;
	
	public static Map<String, Object> initStudentMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		
		List<String> l = new ArrayList<String>();
		l.add("fsdaf");
		m.put("apps", l);
		
		Map<String, String> b = new HashMap<String, String>();
		b.put("1", "fasdf");
		m.put("bpps", b);
		
		m.put("click", 456L);
		m.put("createTime", new Date());
		m.put("id", 4);
		m.put("iniv", true);
		m.put("name", "fsadf");
		
		return m;
	}	
	
	public static TestStudent initStudent() {
		TestStudent ts = new TestStudent();
		
		List<String> l = new ArrayList<String>();
		l.add("fsdaf");
		ts.setApps(l);
		
		Map<String, String> b = new HashMap<String, String>();
		b.put("1", "fasdf");
		ts.setBpps(b);
		
		ts.setClick(123L);
		ts.setCreateTime(new Date());
		ts.setId(1);
		ts.setIniv(true);
		ts.setName("zou");
		
		return ts;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Boolean getIniv() {
		return iniv;
	}
	public void setIniv(Boolean iniv) {
		this.iniv = iniv;
	}
	public List<String> getApps() {
		return apps;
	}
	public void setApps(List<String> apps) {
		this.apps = apps;
	}
	public Map<String, String> getBpps() {
		return bpps;
	}
	public void setBpps(Map<String, String> bpps) {
		this.bpps = bpps;
	}
	public Long getClick() {
		return click;
	}
	public void setClick(Long click) {
		this.click = click;
	}
	
}
