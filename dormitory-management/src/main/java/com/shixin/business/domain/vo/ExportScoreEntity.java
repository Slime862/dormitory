package com.shixin.business.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

public class ExportScoreEntity implements Serializable {

    @Excel(name = "id", isColumnHidden = true,orderNum = "6")
    private String id;

    @Excel(name = "评分id", isColumnHidden = true,orderNum = "7")
    private String batchid;

    @Excel(name = "评分名称", orderNum = "0")
    private String name;

    @Excel(name = "宿舍号", orderNum = "1")
    private String dormid;

    @Excel(name = "学院", orderNum = "2")
    private String academy;

    @Excel(name = "整齐度评分", orderNum = "3", width = 15)
    private Double sanitary;

    @Excel(name = "卫生评分", orderNum = "4")
    private Double tidy;

    @Excel(name = "总分", orderNum = "5")
    private Double sum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDormid() {
        return dormid;
    }

    public void setDormid(String dormid) {
        this.dormid = dormid;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        this.academy = academy;
    }

    public Double getSanitary() {
        return sanitary;
    }

    public void setSanitary(Double sanitary) {
        this.sanitary = sanitary;
    }

    public Double getTidy() {
        return tidy;
    }

    public void setTidy(Double tidy) {
        this.tidy = tidy;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
}
