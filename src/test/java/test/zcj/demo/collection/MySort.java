package test.zcj.demo.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MySort {

	public static void main(String[] args) {
		
		//List顺序倒过来[2,1,3]==>[3,1,2]
		List<String> myList = new ArrayList<String>();
		myList.add("2");
		myList.add("1");
		myList.add("3");
		Collections.reverse(myList);
		System.out.println(myList);
		
		//根据对象的属性排序
		List<Student> students = new ArrayList<Student>();
		students.add(new Student(3));
		students.add(new Student(1));
		students.add(new Student(2));
		Collections.sort(students, new Comparator<Student>() {
			@Override
			public int compare(Student c1, Student c2) {
				if (c1.getId() > c2.getId()) {
					return 1;
				} else {
					if (c1.getId() == c2.getId()) {
						return 0;
					} else {
						return -1;
					}
				}
			}
		});
		for (Student s : students) {System.out.println(s.getId());}
	}
	
	static class Student {
		private int id;
		public Student() {
			super();
		}
		public Student(int id) {
			super();
			this.id = id;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}
}
