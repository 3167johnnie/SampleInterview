package sample;

import java.lang.reflect.Array;
import java.util.*;

public class Sort {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	List<Employee> emp = Array.asList(
			new Employee ("John",25,690000),
		new	Employee ("Ram",25,600000),
		new	Employee ("Sham",32,750000),
		new	Employee ("John",28,450000));
	
	
	emp.stream().sorted(x1,x2) -> x1.getName().compare

	}

}
