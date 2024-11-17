package com.shixin.business.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.afterturn.easypoi.excel.annotation.Excel;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "repair")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Repair implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Excel(name = "宿舍号")
    private String dormid;

    private String stuid;

    @Excel(name = "报修类型", replace = {"灯_0", "床，桌椅_1", "门窗_2", "其他_3"})
    private Integer reason;

    @Excel(name = "报修时间", format = "yyyy-MM-dd")
    private Date date;

    @Excel(name = "说明")
    private String note;

    @Excel(name = "状态", replace = {"创建_0", "处理中_1", "完成_2"})
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getDormid() {
        return dormid;
    }

    public void setDormid(String dormid) {
        this.dormid = dormid == null ? null : dormid.trim();
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStuid() {
        return stuid;
    }

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }

}