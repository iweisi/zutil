package test.zcj.util.json.gson;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Since;

public class Student {

	private int id;
	
	@Since(1.1)
	private String name;

	public Student(int id) {
		this.id = id;
	}
	
	public Student(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	/** 构建一个Cat集合 */
	public static List<Student> initStudentList() {
		List<Student> students = new ArrayList<Student>();
		students.add(new Student(12, "sss"));
		students.add(new Student(16, "哎哎哎"));
		students.add(new Student(18, null));
		return students;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
