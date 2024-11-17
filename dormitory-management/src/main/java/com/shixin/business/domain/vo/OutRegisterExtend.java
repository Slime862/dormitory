package com.shixin.business.domain.vo;

import com.shixin.business.domain.OutRegister;

public class OutRegisterExtend extends OutRegister {

    private String grade;

    private String stuId;

    private String stuName;

    private String dormid;

    private String phone;

    private String registerBatchName;

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
}
