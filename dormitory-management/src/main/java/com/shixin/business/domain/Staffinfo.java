package com.shixin.business.domain;

import java.io.Serializable;

import javax.persistence.*;

import cn.afterturn.easypoi.excel.annotation.Excel;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "staffinfo")
public class Staffinfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Excel(name = "职工号", orderNum = "0")
	private String id;
	@Excel(name = "姓名", orderNum = "1")
	private String staffname;
	@Excel(name = "性别", orderNum = "2", replace = { "男_0", "女_1" })
	private Integer sex;
	@Excel(name = "年龄", orderNum = "3")
	private Integer age;
	@Excel(name = "宿舍楼名称", orderNum = "4")
	private String dormname;
	@Excel(name = "宿舍楼栋号", orderNum = "5")
	private String dormno;
	@Excel(name = "电话", orderNum = "6")
	private String phone;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getStaffname() {
		return staffname;
	}

	public void setStaffname(String staffname) {
		this.staffname = staffname == null ? null : staffname.trim();
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getDormname() {
		return dormname;
	}

	public void setDormname(String dormname) {
		this.dormname = dormname == null ? null : dormname.trim();
	}

	public String getDormno() {
		return dormno;
	}

	public void setDormno(String dormno) {
		this.dormno = dormno == null ? null : dormno.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}
}