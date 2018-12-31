package test.zcj.demo.designpattern.forms.composite;

public abstract class FolderComponent {

	private String name;
	
	public FolderComponent() {
	}

	public FolderComponent(final String name) {
		this.name = name;
	}

	public abstract void add(FolderComponent component);

	public abstract void remove(FolderComponent component);

	public abstract void display();

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}