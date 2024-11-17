package com.shixin.business.domain;

public class UserExpand extends User{
	
	private String name;

	private Stuinfo stuinfo;

	private Staffinfo staffinfo;

	public String getName() {
		return name;
	}

	public UserExpand setName(String name) {
		this.name = name;
		return this;
	}

	public Stuinfo getStuinfo() {
		return stuinfo;
	}

	public void setStuinfo(Stuinfo stuinfo) {
		this.stuinfo = stuinfo;
	}

	public Staffinfo getStaffinfo() {
		return staffinfo;
	}

	public void setStaffinfo(Staffinfo staffinfo) {
		this.staffinfo = staffinfo;
	}
}
