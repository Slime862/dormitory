package com.shixin.business.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "scoringbatch")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Scoringbatch implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    private String name;

    private String dormname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public Scoringbatch setName(String name) {
        this.name = name == null ? null : name.trim();
        return this;
    }

    public String getDormname() {
        return dormname;
    }

    public Scoringbatch setDormname(String dormname) {
        this.dormname = dormname;
        return this;
    }
}