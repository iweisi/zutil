package com.zcj.web.mybatis.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zcj.util.coder.database.TableColumnType;
import com.zcj.util.coder.java.query.QueryColumnType;
import com.zcj.util.coder.page.PageColumnType;

public class BasicTreeEntity extends BasicEntity {

	private static final long serialVersionUID = -3218419989046314842L;

	@PageColumnType(name = "上级", type = "select", keyValue = { "1='类型A'" })
	@QueryColumnType(value = { "=", "in" })
	private Long pid;// 父类别

	@PageColumnType(name = "名称", grid = true, must = true)
	@QueryColumnType("like")
	@TableColumnType(nullable = false)
	private String name;// 名称

	private List<?> show_children;

	private Long show_counts;// 用于计数

	@PageColumnType(name = "上级", grid = true, modify = false)
	private String show_pname;// 父类别名称

	/** 移除集合中重复的记录 */
	public static <T extends BasicTreeEntity> List<T> removeSame(List<T> clist) {
		List<T> result = new ArrayList<T>();
		if (clist != null && clist.size() > 0) {
			Map<Long, T> map = new LinkedHashMap<Long, T>();
			for (T c : clist) {
				map.put(c.getId(), c);
			}
			for (Map.Entry<Long, T> entry : map.entrySet()) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	/** 先转Tree结构，再转List结构 */
	public static <T extends BasicTreeEntity> List<T> convertToTreeList(List<T> list) {
		return convertToTreeList(list, null, null);
	}

	/** 先转Tree结构，再转List结构 */
	public static <T extends BasicTreeEntity> List<T> convertToTreeList(List<T> list, String prefixBegin, String prefix) {
		if (list != null && list.size() > 0) {
			list = convertTree(list);
			list = convertTreeToList(list, prefixBegin, prefix);
		}
		return list;
	}

	/** 集合数据转换为树形数据（根节点的 pid 必须为 null） */
	public static <T extends BasicTreeEntity> List<T> convertTree(List<T> list) {
		List<T> result = new ArrayList<T>();
		if (list != null && list.size() > 0) {
			for (T c : list) {
				c.setShow_children(null);
			}
			for (T c : list) {
				if (c.getPid() == null) {
					addChilds(c, list);
					result.add(c);
				}
			}
		}
		return result;
	}

	/**
	 * 集合数据转换为树形数据
	 * 
	 * @param list
	 *            集合数据
	 * @param topPidAllowNotNull
	 *            根节点的pid是否允许非空（true：只要节点的pid不存在，则就当做根节点；false：pid为空才当做根节点）
	 * @return
	 */
	public static <T extends BasicTreeEntity> List<T> convertTree(List<T> list, boolean topPidAllowNotNull) {
		List<T> result = new ArrayList<T>();
		if (list != null && list.size() > 0) {
			Set<Long> ids = new HashSet<Long>();
			if (topPidAllowNotNull) {
				for (T c : list) {
					ids.add(c.getId());
				}
			}
			for (T c : list) {
				if (c.getPid() == null || (topPidAllowNotNull && !ids.contains(c.getPid()))) {
					addChilds(c, list);
					result.add(c);
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static <T extends BasicTreeEntity> void addChilds(T link, List<T> list) {
		if (link != null && list != null) {
			Long cou = 0L;
			for (T c : list) {
				if (c.getPid() != null && c.getPid().equals(link.getId())) {
					List<T> chi = (List<T>) link.getShow_children();
					if (chi == null) {
						chi = new ArrayList<T>();
					}
					addChilds(c, list);
					chi.add(c);
					if (c.getShow_counts() != null && c.getShow_counts() != 0) {
						cou += c.getShow_counts();
					}
					link.setShow_children(chi);
					link.setShow_counts(cou);
				}
			}
		}
	}

	/** 树形数据转化为集合数据 */
	public static <T extends BasicTreeEntity> List<T> convertTreeToList(List<T> list, String prefixBegin, String prefix) {
		List<T> result = new ArrayList<T>();
		if (list != null && list.size() > 0) {
			addListChilds(list, result, 0, prefixBegin, prefix);
		}
		for (T a : result) {
			a.setShow_children(null);
		}
		return result;
	}

	/** 树形数据转化为集合数据 */
	public static <T extends BasicTreeEntity> List<T> convertTreeToList(List<T> list) {
		return convertTreeToList(list, null, null);
	}

	@SuppressWarnings("unchecked")
	private static <T extends BasicTreeEntity> List<T> addListChilds(List<T> list, List<T> result, int count,
			String prefixBegin, String prefix) {
		if (list != null && list.size() > 0) {
			for (T obj : list) {
				obj.setName(showName(obj.getName(), count, prefixBegin, prefix));
				result.add(obj);
				result = addListChilds((List<T>) obj.getShow_children(), result, count + 1, prefixBegin, prefix);
			}
		}
		return result;
	}

	private static String showName(String name, int count, String prefixBegin, String prefix) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		if (prefixBegin != null) {
			sb.append(prefixBegin);
		}
		while (index < count) {
			if (prefix != null) {
				sb.append(prefix);
			}
			index++;
		}
		sb.append(name);
		return sb.toString();
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getShow_counts() {
		return show_counts;
	}

	public void setShow_counts(Long show_counts) {
		this.show_counts = show_counts;
	}

	public String getShow_pname() {
		return show_pname;
	}

	public void setShow_pname(String show_pname) {
		this.show_pname = show_pname;
	}

	public List<?> getShow_children() {
		return show_children;
	}

	public void setShow_children(List<?> show_children) {
		this.show_children = show_children;
	}

}