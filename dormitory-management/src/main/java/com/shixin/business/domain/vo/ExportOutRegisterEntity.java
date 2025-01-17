package com.shixin.business.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class ExportOutRegisterEntity {

    @Excel(name = "年级")
    private String grade;

    @Excel(name = "学号")
    private String stuId;

    @Excel(name = "姓名")
    private String stuName;

    @Excel(name = "宿舍号")
    private String dormid;

    @Excel(name = "联系电话")
    private String phone;

    @Excel(name = "登记名")
    private String registerBatchName;

    @Excel(name = "离校时间", format = "yyyy-MM-dd")
    private Date leavetime;

    @Excel(name = "离校去向")
    private String city;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getDormid() {
        return dormid;
    }

    public void setDormid(String dormid) {
        this.dormid = dormid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegisterBatchName() {
        return registerBatchName;
    }

    public void setRegisterBatchName(String registerBatchName) {
        this.registerBatchName = registerBatchName;
    }

    public Date getLeavetime() {
        return leavetime;
    }

    public void setLeavetime(Date leavetime) {
        this.leavetime = leavetime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
