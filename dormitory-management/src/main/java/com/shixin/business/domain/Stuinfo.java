package com.shixin.business.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.afterturn.easypoi.excel.annotation.Excel;

@Entity
@Table(name = "stuinfo")
public class Stuinfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Excel(name = "学号", orderNum = "0")
	private String id;
	@Excel(name = "姓名", orderNum = "1")
	private String name;
	@Excel(name = "性别", orderNum = "2", replace = { "男_0", "女_1" }, suffix = "生")
	private Integer sex;
	@Excel(name = "年级", orderNum = "3")
	private String grade;
	@Excel(name = "学院", orderNum = "4")
	private String academy;
	@Excel(name = "专业", orderNum = "5")
	private String major;
	@Excel(name = "宿舍号", orderNum = "6")
	private String dormid;
	@Excel(name = "电话", orderNum = "7")
	private String phone;
	@Excel(name = "状态", orderNum = "8", replace = { "在校_0", "离校_1" })
	private Integer status;

	public String getId() {
		return id;
	}

	public Stuinfo setId(String id) {
		this.id = id == null ? null : id.trim();
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade == null ? null : grade.trim();
	}

	public String getAcademy() {
		return academy;
	}

	public void setAcademy(String academy) {
		this.academy = academy == null ? null : academy.trim();
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major == null ? null : major.trim();
	}

	public String getDormid() {
		return dormid;
	}

	public void setDormid(String dormid) {
		this.dormid = dormid == null ? null : dormid.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public Integer getStatus() {
		return status;
	}

	public Stuinfo setStatus(Integer status) {
		this.status = status;
		return this;
	}
}