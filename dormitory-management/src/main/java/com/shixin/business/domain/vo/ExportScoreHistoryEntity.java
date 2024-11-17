package com.shixin.business.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class ExportScoreHistoryEntity {
    @Excel(name = "评分名")
    private String scoringbatchName;

    @Excel(name = "宿舍楼号")
    private String dormid;

    @Excel(name = "学院")
    private String academy;

    @Excel(name = "整齐度评分")
    private Double sanitary;

    @Excel(name = "卫生评分")
    private Double tidy;

    @Excel(name = "总分")
    private Double sum;

    @Excel(name = "排名")
    private String orderscore;

    public String getScoringbatchName() {
        return scoringbatchName;
    }

    public void setScoringbatchName(String scoringbatchName) {
        this.scoringbatchName = scoringbatchName;
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

    public String getOrderscore() {
        return orderscore;
    }

    public void setOrderscore(String orderscore) {
        this.orderscore = orderscore;
    }
}
