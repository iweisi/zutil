package test.zcj.demo.designpattern.forms.composite;

import java.util.ArrayList;
import java.util.List;

public class FolderComposite extends FolderComponent {

	private final List<FolderComponent> components;

	public FolderComposite(final String name) {
		super(name);
		this.components = new ArrayList<FolderComponent>();
	}

	public FolderComposite() {
		this.components = new ArrayList<FolderComponent>();
	}

	@Override
	public void add(final FolderComponent component) {
		this.components.add(component);
	}

	@Override
	public void remove(final FolderComponent component) {
		this.components.remove(component);
	}

	@Override
	public void display() {
		System.out.println("文件夹:" + this.getName());
		for (final FolderComponent component : components) {
			component.display();
		}
	}

}