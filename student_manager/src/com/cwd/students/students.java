package com.cwd.students;

import java.io.Serializable;

public class students implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String name;
	private String num;
	private String sex;
	
	public students(){
		super();
	}
	public students(String name , String num,String sex) {
		super();
		this.name = name;
		this.num = num;
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "students [name=" + name + ", num=" + num + ", sex=" + sex + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
