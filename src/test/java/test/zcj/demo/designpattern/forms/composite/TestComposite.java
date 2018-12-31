package test.zcj.demo.designpattern.forms.composite;

/**
 * 组合设计模式
 * 
 * 意图
 * 	将对象组合成树形结构以表示“部分-整体”的层次结构。Composite使得用户对单个对象和组合对象的使用具有一致性。
 * 
 * 适用环境
 * 	你想表示对象的部分-整体层次结构。 
 * 	你希望用户忽略组合对象与单个对象的不同，用户将统一地使用组合结构中的所有对象。
 * 
 * @author zouchongjin@sina.com
 * @data 2016年1月8日
 */
public class TestComposite {

	public static void main(final String[] args) {

		final FolderComponent folder2 = new FolderComposite("imgs");
		folder2.add(new FileLeaf("b.jpg"));
		folder2.add(new FileLeaf("c.jpg"));

		final FolderComponent folder = new FolderComposite("www");
		folder.add(new FileLeaf("a.text"));
		folder.add(new FileLeaf("e.text"));
		folder.add(folder2);
		folder.display();
	}

}