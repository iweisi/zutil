package test.zcj.util.beanutils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestStudent2 {

	private Integer id;
	private String name;
	private Date createTime;
	private Boolean iniv;
	private List<String> apps;
	private Map<String, String> bpps;
	private Long click;
	
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
