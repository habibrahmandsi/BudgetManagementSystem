package com.macrosoft.bms.data.model.Entity;

import javax.persistence.*;

/**
 * Created by habib on 7/12/17.
 */
@Entity
@Table(name = "caseTable")
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String name;

    private String description;

    private String brs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrs() {
        return brs;
    }

    public void setBrs(String brs) {
        this.brs = brs;
    }

    @Override
    public String toString() {
        return "Case{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", brs='" + brs + '\'' +
                '}';
    }
}
