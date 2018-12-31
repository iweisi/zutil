package com.zcj.web.mybatis.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.zcj.util.UtilString;

public class BasicEntity implements Serializable {

	private static final long serialVersionUID = 51124911023734190L;

	public static final String SHOWFILTER_TYPE_BASE = "宽松过滤";
	public static final String SHOWFILTER_TYPE_STRICT = "严格过滤";

	private Long id;

	/** 创建时间 */
	private Date ctime;

	/** 更新时间 */
	private Date utime;

	/** 插入数据库前的操作 */
	public void insertBefore() {
		if (id == null) {
			id = UtilString.getLongUUID();
		}
	}

	/** 相对路径转成可访问的完整URL路径 */
	public String formatUrl(String url, String website) {
		return UtilString.urlFill(url, website);
	}

	/** 补全额外属性的值 */
	public void showFormat() {

	}

	public static void showFormat(List<? extends BasicEntity> list) {
		if (list != null && list.size() > 0) {
			for (BasicEntity g : list) {
				g.showFormat();
			}
		}
	}

	/** 根据过滤规则调用指定的过滤方法 */
	public void showFilter(String showFilterType) {
		if (UtilString.isNotBlank(showFilterType)) {
			if (SHOWFILTER_TYPE_BASE.equals(showFilterType)) {
				showFilterBase();
			} else if (SHOWFILTER_TYPE_STRICT.equals(showFilterType)) {
				showFilterStrict();
			}
		}
	}

	/** 显示时过滤某些属性（宽松模式） */
	protected void showFilterBase() {

	}

	/** 显示时顾虑某些属性（严格模式：默认在宽松模式的基础上过滤） */
	protected void showFilterStrict() {
		showFilterBase();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

}
