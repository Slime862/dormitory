package com.shixin.business.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Dorm")
public class Dorm implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Excel(name = "宿舍楼名称", orderNum = "0")
    private String dormname;

    @Excel(name = "宿舍楼栋号", orderNum = "1")
    private String dormno;

    @Excel(name = "房间号", orderNum = "2")
    private String roomno;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
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

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno == null ? null : roomno.trim();
    }
}