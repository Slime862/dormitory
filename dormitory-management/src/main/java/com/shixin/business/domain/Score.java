package com.shixin.business.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "score")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Score implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    private String batchid;

    private String dormid;

    private Double sanitary;

    private Double tidy;

    private Double sum;

    private String orderscore;

    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBatchid() {
        return batchid;
    }

    public void setBatchid(String batchid) {
        this.batchid = batchid == null ? null : batchid.trim();
    }

    public String getDormid() {
        return dormid;
    }

    public void setDormid(String dormid) {
        this.dormid = dormid == null ? null : dormid.trim();
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOrderscore() {
        return orderscore;
    }

    public void setOrderscore(String orderscore) {
        this.orderscore = orderscore;
    }

    public String getStatus() {
        return status;
    }

    public Score setStatus(String status) {
        this.status = status;
        return this;
    }
}