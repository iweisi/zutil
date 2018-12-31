package test.zcj.demo.designpattern.forms.composite;

public class FileLeaf extends FolderComponent {

	public FileLeaf(final String name) {
		super(name);
	}

	@Override
	public void add(final FolderComponent component) {
		// ...
	}

	@Override
	public void remove(final FolderComponent component) {
		// ...
	}

	@Override
	public void display() {
		System.out.println("文件:" + this.getName());
	}

}